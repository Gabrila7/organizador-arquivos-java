package com.gabrila7.fileorganizer.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum que define as categorias de arquivos suportadas e suas extensões associadas.
 * 
 * @author Gabriel Vinícius (@Gabrila7)
 */
public enum FileCategory {

    DOCUMENTOS("Documentos", "📄", new String[]{
        "pdf", "doc", "docx", "txt", "odt", "rtf", "tex", "epub"
    }),

    PLANILHAS("Planilhas", "📊", new String[]{
        "xls", "xlsx", "csv", "ods", "tsv"
    }),

    APRESENTACOES("Apresentações", "📽️", new String[]{
        "ppt", "pptx", "odp", "key"
    }),

    IMAGENS("Imagens", "🖼️", new String[]{
        "png", "jpg", "jpeg", "gif", "bmp", "webp", "svg", "ico", "tiff", "psd"
    }),

    VIDEOS("Vídeos", "🎬", new String[]{
        "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "m4v"
    }),

    AUDIOS("Áudios", "🎵", new String[]{
        "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a"
    }),

    COMPACTADOS("Compactados", "📦", new String[]{
        "zip", "rar", "7z", "tar", "gz", "bz2", "xz", "iso"
    }),

    INSTALADORES("Instaladores", "⚙️", new String[]{
        "exe", "msi", "apk", "deb", "rpm", "bat", "cmd", "sh"
    }),

    CODIGOS("Códigos & Scripts", "💻", new String[]{
        "java", "py", "c", "cpp", "h", "cs", "js", "ts", "html", "css",
        "php", "rb", "go", "rs", "sql", "json", "xml", "yaml", "yml"
    }),

    OUTROS("Outros", "📁", new String[]{});

    private final String folderName;
    private final String icon;
    private final Set<String> extensions;

    FileCategory(String folderName, String icon, String[] extensions) {
        this.folderName = folderName;
        this.icon = icon;
        this.extensions = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(extensions)));
    }

    public String getFolderName() {
        return folderName;
    }

    public String getIcon() {
        return icon;
    }

    public Set<String> getExtensions() {
        return extensions;
    }

    /**
     * Identifica a categoria correspondente a partir da extensão do arquivo.
     *
     * @param extension Extensão do arquivo (sem ponto, em minúsculas).
     * @return Categoria correspondente ou OUTROS se não reconhecida.
     */
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
