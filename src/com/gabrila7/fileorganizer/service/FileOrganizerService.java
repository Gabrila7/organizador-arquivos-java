package com.gabrila7.fileorganizer.service;

import com.gabrila7.fileorganizer.model.FileCategory;
import com.gabrila7.fileorganizer.model.OrganizationStats;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    public OrganizationStats organize(Path targetDirectory, boolean organizeOthers, boolean dryRun) throws IOException {
        long startTime = System.currentTimeMillis();
        OrganizationStats stats = new OrganizationStats();

        if (targetDirectory == null || !Files.exists(targetDirectory) || !Files.isDirectory(targetDirectory)) {
            throw new IllegalArgumentException("Diretorio invalido ou inexistente: " + targetDirectory);
        }

        log(String.format("Organizando pasta: %s %s",
                targetDirectory.toAbsolutePath(), (dryRun ? "[SIMULACAO]" : "")));

        List<Path> filesToProcess = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDirectory)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    if (Files.isHidden(entry) || entry.getFileName().toString().startsWith(".")) {
                        stats.incrementIgnored();
                        continue;
                    }
                    filesToProcess.add(entry);
                }
            }
        }

        stats.setTotalFilesFound(filesToProcess.size());
        log(String.format("Arquivos encontrados na raiz: %d", filesToProcess.size()));

        if (filesToProcess.isEmpty()) {
            log("Nenhum arquivo solto para organizar.");
            stats.setExecutionTimeMillis(System.currentTimeMillis() - startTime);
            return stats;
        }

        int processedCount = 0;

        for (Path file : filesToProcess) {
            processedCount++;
            updateProgress(processedCount, filesToProcess.size());

            String fileName = file.getFileName().toString();
            String extension = extractExtension(fileName);
            FileCategory category = FileCategory.fromExtension(extension);

            if (category == FileCategory.OUTROS && !organizeOthers) {
                stats.incrementIgnored();
                log(String.format("Ignorado (sem categoria): %s", fileName));
                continue;
            }

            try {
                long fileSize = Files.size(file);
                Path destinationFolder = targetDirectory.resolve(category.getFolderName());

                if (!dryRun && !Files.exists(destinationFolder)) {
                    Files.createDirectories(destinationFolder);
                }

                Path destinationFile = resolveUniqueDestination(destinationFolder, fileName);

                if (!dryRun) {
                    Files.move(file, destinationFile, StandardCopyOption.ATOMIC_MOVE);
                }

                stats.incrementMoved(category, fileSize);
                log(String.format("[OK] %s -> %s/%s",
                        fileName, category.getFolderName(), destinationFile.getFileName()));

            } catch (Exception e) {
                stats.incrementError();
                log(String.format("[ERRO] Falha ao mover '%s': %s", fileName, e.getMessage()));
            }
        }

        stats.setExecutionTimeMillis(System.currentTimeMillis() - startTime);
        log(dryRun ? "Simulacao finalizada." : "Organizacao finalizada.");

        return stats;
    }

    public static String extractExtension(String fileName) {
        if (fileName == null) return "";
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

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
