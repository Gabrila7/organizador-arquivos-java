package com.gabrila7.fileorganizer.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum FileCategory {

    DOCUMENTOS("Documentos", new String[]{
        "pdf", "doc", "docx", "txt", "odt", "rtf", "tex", "epub"
    }),

    PLANILHAS("Planilhas", new String[]{
        "xls", "xlsx", "csv", "ods", "tsv"
    }),

    APRESENTACOES("Apresentacoes", new String[]{
        "ppt", "pptx", "odp", "key"
    }),

    IMAGENS("Imagens", new String[]{
        "png", "jpg", "jpeg", "gif", "bmp", "webp", "svg", "ico", "tiff", "psd"
    }),

    VIDEOS("Videos", new String[]{
        "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "m4v"
    }),

    AUDIOS("Audios", new String[]{
        "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a"
    }),

    COMPACTADOS("Compactados", new String[]{
        "zip", "rar", "7z", "tar", "gz", "bz2", "xz", "iso"
    }),

    INSTALADORES("Instaladores", new String[]{
        "exe", "msi", "apk", "deb", "rpm", "bat", "cmd", "sh"
    }),

    CODIGOS("Codigos", new String[]{
        "java", "py", "c", "cpp", "h", "cs", "js", "ts", "html", "css",
        "php", "rb", "go", "rs", "sql", "json", "xml", "yaml", "yml"
    }),

    OUTROS("Outros", new String[]{});

    private final String folderName;
    private final Set<String> extensions;

    FileCategory(String folderName, String[] extensions) {
        this.folderName = folderName;
        this.extensions = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(extensions)));
    }

    public String getFolderName() {
        return folderName;
    }

    public Set<String> getExtensions() {
        return extensions;
    }

    public static FileCategory fromExtension(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return OUTROS;
        }

        String extLower = extension.trim().toLowerCase();
        for (FileCategory category : values()) {
            if (category.extensions.contains(extLower)) {
                return category;
            }
        }
        return OUTROS;
    }
}
