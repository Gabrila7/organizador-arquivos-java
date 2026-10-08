package com.gabrila7.fileorganizer.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Armazena e formata as métricas e estatísticas do processo de organização.
 * 
 * @author Gabriel Vinícius (@Gabrila7)
 */
public class OrganizationStats {

    private int totalFilesFound = 0;
    private int filesMoved = 0;
    private int filesIgnored = 0;
    private int errorsCount = 0;
    private long totalBytesProcessed = 0L;
    private long executionTimeMillis = 0L;

    private final Map<FileCategory, Integer> categoryCounts = new EnumMap<>(FileCategory.class);

    public OrganizationStats() {
        for (FileCategory cat : FileCategory.values()) {
            categoryCounts.put(cat, 0);
        }
    }

    public void incrementMoved(FileCategory category, long fileSizeBytes) {
        this.filesMoved++;
        this.totalBytesProcessed += fileSizeBytes;
        this.categoryCounts.put(category, categoryCounts.get(category) + 1);
    }

    public void incrementIgnored() {
        this.filesIgnored++;
    }

    public void incrementError() {
        this.errorsCount++;
    }

    public void setTotalFilesFound(int totalFilesFound) {
        this.totalFilesFound = totalFilesFound;
    }

    public void setExecutionTimeMillis(long executionTimeMillis) {
        this.executionTimeMillis = executionTimeMillis;
    }

    public int getTotalFilesFound() {
        return totalFilesFound;
    }

    public int getFilesMoved() {
        return filesMoved;
    }

    public int getFilesIgnored() {
        return filesIgnored;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    public long getTotalBytesProcessed() {
        return totalBytesProcessed;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }

    public Map<FileCategory, Integer> getCategoryCounts() {
        return Collections.unmodifiableMap(categoryCounts);
    }

    /**
     * Converte o tamanho em bytes para representação legível (KB, MB, GB).
     */
    public String getFormattedSize() {
        if (totalBytesProcessed < 1024) {
            return totalBytesProcessed + " B";
        }
        int exp = (int) (Math.log(totalBytesProcessed) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", totalBytesProcessed / Math.pow(1024, exp), pre);
    }

    /**
     * Retorna um resumo formatado para exibição.
     */
    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumo da Organização ===\n");
        sb.append(String.format("• Arquivos organizados: %d de %d\n", filesMoved, totalFilesFound));
        sb.append(String.format("• Volume total movido: %s\n", getFormattedSize()));
        sb.append(String.format("• Tempo de execução: %.2f segundos\n", executionTimeMillis / 1000.0));
        if (filesIgnored > 0) {
            sb.append(String.format("• Arquivos ignorados (ocultos/pastas): %d\n", filesIgnored));
        }
        if (errorsCount > 0) {
            sb.append(String.format("• Erros encontrados: %d\n", errorsCount));
        }
        sb.append("\nDivisão por categoria:\n");
        for (Map.Entry<FileCategory, Integer> entry : categoryCounts.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(String.format("  %s %s: %d arquivo(s)\n",
                        entry.getKey().getIcon(), entry.getKey().getFolderName(), entry.getValue()));
            }
        }
        return sb.toString();
    }
}
