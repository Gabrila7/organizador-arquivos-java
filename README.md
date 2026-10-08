# 📁 Organizador Inteligente de Arquivos Desktop (Java Swing)

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Interface-Swing-blue?style=for-the-badge" alt="Java Swing" />
  <img src="https://img.shields.io/badge/Plataforma-Windows%20%7C%20Linux%20%7C%20macOS-informational?style=for-the-badge" alt="Plataforma" />
  <img src="https://img.shields.io/badge/Licen%C3%A7a-MIT-green?style=for-the-badge" alt="Licença" />
</p>

Uma aplicação desktop em **Java** desenvolvida para resolver a desorganização frequente em pastas de downloads e diretórios de trabalho. Com um único clique, o sistema categoriza, agrupa e move arquivos para subdiretórios específicos de forma segura, inteligente e assíncrona.

---

## ✨ Funcionalidades Principais

- **Categorização Automática Inteligente:** Identifica e agrupa arquivos com base em suas extensões:
  - 📄 **Documentos:** PDF, DOCX, TXT, ODT, RTF, EPUB
  - 📊 **Planilhas:** XLS, XLSX, CSV, ODS
  - 🖼️ **Imagens:** PNG, JPG, JPEG, GIF, WEBP, SVG, PSD
  - 🎬 **Vídeos:** MP4, MKV, AVI, MOV, WEBM
  - 🎵 **Áudios:** MP3, WAV, FLAC, AAC, OGG
  - 📦 **Compactados:** ZIP, RAR, 7Z, TAR, GZ, ISO
  - ⚙️ **Instaladores:** EXE, MSI, APK, DEB, BAT
  - 💻 **Códigos & Scripts:** JAVA, PY, C, CPP, JS, TS, HTML, CSS, SQL, JSON
  - 📁 **Outros:** Arquivos não mapeados
- **Tratamento Anti-Sobrescrita (Zero Perda de Dados):** Se um arquivo com o mesmo nome já existir na pasta de destino, o sistema resolve o conflito adicionando automaticamente um sufixo numérico (ex: `relatorio (1).pdf`), garantindo total integridade dos seus dados.
- **Interface Não-Bloqueante (Multithreading):** Utiliza `SwingWorker` para executar as operações de I/O em segundo plano, mantendo a interface fluida, responsiva e com barra de progresso em tempo real.
- **Modo Simulação (Dry Run):** Permite visualizar no log tudo o que seria movido antes de realizar qualquer alteração real em disco.
- **Proteção de Arquivos do Sistema:** Ignora automaticamente subpastas já existentes, arquivos ocultos e arquivos de configuração.
- **Relatório Completo de Execução:** Ao final da operação, exibe o volume total em MB/GB processado, tempo de execução e a contagem detalhada por categoria.

---

## 🏗️ Estrutura do Projeto

O projeto adota padrões de **Orientação a Objetos** e separação de responsabilidades (MVC simplificado):

```text
organizador-arquivos-java/
├── .gitignore
├── compilar_e_executar.bat     # Script de execução rápida para Windows
├── README.md                   # Documentação do projeto
└── src/
    └── com/
        └── gabrila7/
            └── fileorganizer/
                ├── Main.java                        # Ponto de entrada da aplicação
                ├── model/
                │   ├── FileCategory.java            # Enum com categorias e mapeamento de extensões
                │   └── OrganizationStats.java       # Métricas e relatórios do processo
                ├── service/
                │   └── FileOrganizerService.java    # Lógica de negócio e manipulação NIO de arquivos
                └── ui/
                    └── MainFrame.java               # Interface gráfica desenvolvida em Java Swing
```

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 8+ (compatível com Java 8, 11, 17, 21+)
- **GUI:** Java Swing (com Look & Feel nativo do sistema operacional)
- **I/O e Manipulação de Arquivos:** `java.nio.file` (`Path`, `Files`, `DirectoryStream`, `StandardCopyOption`)
- **Concorrência:** `javax.swing.SwingWorker` para processamento em background

---

## 🚀 Como Executar o Projeto

### Opção 1: Via IDE (Recomendado)
1. Clone este repositório:
   ```bash
   git clone https://github.com/Gabrila7/file-organizer-gui.git
   ```
2. Abra a pasta do projeto no **IntelliJ IDEA**, **Eclipse** ou **VS Code**.
3. Navegue até o arquivo `src/com/gabrila7/fileorganizer/Main.java`.
4. Clique com o botão direito e selecione **Run 'Main'**.

### Opção 2: Pelo Terminal / Linha de Comando (Windows)
Se você estiver no Windows, basta dar dois cliques no arquivo:
```cmd
compilar_e_executar.bat
```

Ou execute manualmente no terminal:
```bash
# 1. Cria a pasta de binários e compila
javac -encoding UTF-8 -d bin src/com/gabrila7/fileorganizer/model/*.java src/com/gabrila7/fileorganizer/service/*.java src/com/gabrila7/fileorganizer/ui/*.java src/com/gabrila7/fileorganizer/*.java

# 2. Executa a aplicação
java -cp bin com.gabrila7.fileorganizer.Main
```

---

## 💡 Como Usar

1. Ao abrir o aplicativo, clique em **"Selecionar Pasta..."** e aponte para a pasta que deseja organizar (ex: `Downloads`).
2. Se desejar apenas testar sem mover nenhum arquivo real, marque a opção **"Modo Simulação"**.
3. Clique em **"🚀 Iniciar Organização"**.
4. Acompanhe o log em tempo real e, ao final, confira o resumo com as estatísticas dos arquivos organizados.

---

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

---

## 👤 Autor

Desenvolvido por **Gabriel Vinícius**  
- GitHub: [@Gabrila7](https://github.com/Gabrila7)  
- Graduando em Ciência da Computação (4º Período)
