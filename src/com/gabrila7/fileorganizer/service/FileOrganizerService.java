package com.gabrila7.fileorganizer.service;

import com.gabrila7.fileorganizer.model.FileCategory;
import com.gabrila7.fileorganizer.model.OrganizationStats;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Serviço responsável por escanear o diretório, categorizar arquivos,
 * tratar colisões de nomes e mover os arquivos de forma segura.
 * 
 * @author Gabriel Vinícius (@Gabrila7)
 */
public class FileOrganizerService {

    private Consumer<String> logListener;
    private BiConsumer<Integer, Integer> progressListener;

    public void setLogListener(Consumer<String> logListener) {
        this.logListener = logListener;
    }

    public void setProgressListener(BiConsumer<Integer, Integer> progressListener) {
        this.progressListener = progressListener;
    }

    private void log(String message) {
        if (logListener != null) {
            logListener.accept(message);
        }
    }

    private void updateProgress(int current, int total) {
        if (progressListener != null) {
            progressListener.accept(current, total);
        }
    }

    /**
     * Executa a organização de arquivos no diretório fornecido.
     *
     * @param targetDirectory Diretório a ser organizado.
     * @param organizeOthers  Se verdadeiro, arquivos não categorizados vão para "Outros". Se falso, permanecem no lugar.
     * @param dryRun          Se verdadeiro, apenas simula as movimentações sem alterar nada em disco.
     * @return Estatísticas completas da execução.
     * @throws IOException Se houver falha de leitura do diretório.
     */
    public OrganizationStats organize(Path targetDirectory, boolean organizeOthers, boolean dryRun) throws IOException {
        long startTime = System.currentTimeMillis();
        OrganizationStats stats = new OrganizationStats();

        if (targetDirectory == null || !Files.exists(targetDirectory) || !Files.isDirectory(targetDirectory)) {
            throw new IllegalArgumentException("Diretório inválido ou inexistente: " + targetDirectory);
        }

        log(String.format("Iniciando organização em: %s %s",
                targetDirectory.toAbsolutePath(), (dryRun ? "[MODO SIMULAÇÃO]" : "")));

        // 1. Listar apenas arquivos regulares diretamente na raiz da pasta selecionada
        List<Path> filesToProcess = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDirectory)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    // Ignora arquivos ocultos ou de sistema temporários
                    if (Files.isHidden(entry) || entry.getFileName().toString().startsWith(".")) {
                        stats.incrementIgnored();
                        continue;
                    }
                    filesToProcess.add(entry);
                }
            }
        }

        stats.setTotalFilesFound(filesToProcess.size());
        log(String.format("Arquivos detectados para análise: %d", filesToProcess.size()));

        if (filesToProcess.isEmpty()) {
            log("Nenhum arquivo elegível encontrado para organização.");
            stats.setExecutionTimeMillis(System.currentTimeMillis() - startTime);
            return stats;
        }

        int processedCount = 0;

        // 2. Processar cada arquivo
        for (Path file : filesToProcess) {
            processedCount++;
            updateProgress(processedCount, filesToProcess.size());

            String fileName = file.getFileName().toString();
            String extension = extractExtension(fileName);
            FileCategory category = FileCategory.fromExtension(extension);

            // Se for categoria OUTROS e o usuário optou por não mover arquivos desconhecidos
            if (category == FileCategory.OUTROS && !organizeOthers) {
                stats.incrementIgnored();
                log(String.format("Mantido na raiz (não categorizado): %s", fileName));
                continue;
            }

            try {
                long fileSize = Files.size(file);
                Path destinationFolder = targetDirectory.resolve(category.getFolderName());

                if (!dryRun && !Files.exists(destinationFolder)) {
                    Files.createDirectories(destinationFolder);
                }

                // Tratar colisão de nomes para evitar sobrescrita acidental
                Path destinationFile = resolveUniqueDestination(destinationFolder, fileName);

                if (!dryRun) {
                    Files.move(file, destinationFile, StandardCopyOption.ATOMIC_MOVE);
                }

                stats.incrementMoved(category, fileSize);
                log(String.format("%s %s ➜ %s/%s",
                        category.getIcon(), fileName, category.getFolderName(), destinationFile.getFileName()));

            } catch (Exception e) {
                stats.incrementError();
                log(String.format("❌ Erro ao mover '%s': %s", fileName, e.getMessage()));
            }
        }

        stats.setExecutionTimeMillis(System.currentTimeMillis() - startTime);
        log(dryRun ? "Simulação concluída com sucesso!" : "Organização concluída com sucesso!");

        return stats;
    }

    /**
     * Extrai a extensão de um arquivo sem o ponto.
     */
    public static String extractExtension(String fileName) {
        if (fileName == null) return "";
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Resolve conflito de nomes adicionando um sufixo numérico (ex: arquivo (1).pdf) caso o destino já exista.
     */
    private Path resolveUniqueDestination(Path folder, String originalName) {
        Path target = folder.resolve(originalName);
        if (!Files.exists(target)) {
            return target;
        }

        String baseName;
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = originalName.substring(0, dotIndex);
            extension = originalName.substring(dotIndex);
        } else {
            baseName = originalName;
        }

        int counter = 1;
        while (Files.exists(target)) {
            String newName = String.format("%s (%d)%s", baseName, counter, extension);
            target = folder.resolve(newName);
            counter++;
        }

        return target;
    }
}
