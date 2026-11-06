package com.gabrila7.fileorganizer.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

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

    public String getFormattedSize() {
        if (totalBytesProcessed < 1024) {
            return totalBytesProcessed + " B";
        }
        int exp = (int) (Math.log(totalBytesProcessed) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", totalBytesProcessed / Math.pow(1024, exp), pre);
    }

    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Resumo da Organizacao ---\n");
        sb.append(String.format("Arquivos movidos: %d de %d\n", filesMoved, totalFilesFound));
        sb.append(String.format("Volume processado: %s\n", getFormattedSize()));
        sb.append(String.format("Tempo total: %.2f s\n", executionTimeMillis / 1000.0));
        if (filesIgnored > 0) {
            sb.append(String.format("Ignorados: %d\n", filesIgnored));
        }
        if (errorsCount > 0) {
            sb.append(String.format("Erros: %d\n", errorsCount));
        }
        sb.append("\nPor categoria:\n");
        for (Map.Entry<FileCategory, Integer> entry : categoryCounts.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(String.format(" - %s: %d arquivo(s)\n",
                        entry.getKey().getFolderName(), entry.getValue()));
            }
        }
        return sb.toString();
    }
}
