# Organizador de Arquivos Desktop

Aplicativo desktop simples em Java com interface gráfica Swing para organizar automaticamente pastas bagunçadas (como a pasta de Downloads). O programa varre os arquivos soltos na pasta e move cada um para subpastas de acordo com o tipo de extensão (Documentos, Imagens, Vídeos, Planilhas, etc.).

Projeto prático desenvolvido para exercitar conceitos de orientação a objetos, manipulação de arquivos com a API `java.nio` e programação assíncrona com `SwingWorker`.

---

## O que o programa faz

- **Organização por tipo de arquivo:** separa automaticamente arquivos em pastas como Documentos, Planilhas, Imagens, Vídeos, Músicas, Compactados, etc.
- **Proteção contra sobrescrita:** se já existir um arquivo com o mesmo nome na pasta de destino, o programa renomeia adicionando um número no final (ex: `relatorio (1).pdf`) em vez de apagar o arquivo antigo.
- **Modo simulação:** permite rodar o teste sem mover nada de verdade, apenas mostrando no log o que seria alterado.
- **Interface responsiva:** a movimentação dos arquivos roda em segundo plano com `SwingWorker`, evitando que a janela congele durante o processo e atualizando a barra de progresso.
- **Ignora arquivos ocultos:** arquivos ocultos ou pastas que já existem dentro do diretório não são modificados.

---

## Estrutura do Projeto

```text
organizador-arquivos-java/
├── compilar_e_executar.bat     # Atalho para compilar e abrir no Windows
├── README.md
└── src/
    └── com/
        └── gabrila7/
            └── fileorganizer/
                ├── Main.java                        # Ponto de entrada (inicia a interface)
                ├── model/
                │   ├── FileCategory.java            # Enum com os tipos de arquivo e extensões
                │   └── OrganizationStats.java       # Contadores e relatório final
                ├── service/
                │   └── FileOrganizerService.java    # Lógica de leitura e movimentação dos arquivos
                └── ui/
                    └── MainFrame.java               # Janela gráfica Swing
```

---

## Como Rodar

### Requisitos
- Java JDK 8 ou superior instalado (testado no JDK 17 e 21).

### Opção 1: Pelo Terminal / CMD (Windows)
Basta dar dois cliques no arquivo `compilar_e_executar.bat` ou rodar no terminal:

```cmd
javac -encoding UTF-8 -d bin src/com/gabrila7/fileorganizer/model/*.java src/com/gabrila7/fileorganizer/service/*.java src/com/gabrila7/fileorganizer/ui/*.java src/com/gabrila7/fileorganizer/*.java
java -cp bin com.gabrila7.fileorganizer.Main
```

### Opção 2: Pela IDE (VS Code, IntelliJ ou Eclipse)
1. Abra a pasta do projeto na sua IDE.
2. Abra o arquivo `src/com/gabrila7/fileorganizer/Main.java`.
3. Clique em **Run** / Executar.

---

## Como Usar

1. Clique em **"Selecionar Pasta..."** e escolha a pasta que você quer arrumar (ex: sua pasta `Downloads`).
2. Se quiser apenas conferir o que ele faria antes de mover de verdade, marque a caixinha **"Modo Simulação"**.
3. Clique em **"Iniciar Organização"**.
4. Acompanhe o log na tela e veja o resumo com o total de arquivos movidos ao final.

---

## Autor

Gabriel Vinícius  
Estudante de Ciência da Computação  
GitHub: [@Gabrila7](https://github.com/Gabrila7)
