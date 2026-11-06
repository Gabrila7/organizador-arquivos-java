package com.gabrila7.fileorganizer.ui;

import com.gabrila7.fileorganizer.model.OrganizationStats;
import com.gabrila7.fileorganizer.service.FileOrganizerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFrame extends JFrame {

    private JTextField pathField;
    private JButton selectFolderButton;
    private JButton startButton;
    private JCheckBox organizeOthersCheckBox;
    private JCheckBox dryRunCheckBox;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JLabel statusLabel;

    private Path selectedDirectory;
    private final FileOrganizerService service = new FileOrganizerService();

    public MainFrame() {
        super("Organizador de Arquivos");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 520);
        setMinimumSize(new Dimension(560, 440));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel("Organizador de Arquivos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel subtitleLabel = new JLabel("Separa arquivos da pasta selecionada em subpastas por extensao.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel controlsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pathField = new JTextField();
        pathField.setEditable(false);
        pathField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pathField.setText("Nenhuma pasta selecionada...");
        controlsPanel.add(pathField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        selectFolderButton = new JButton("Selecionar Pasta...");
        selectFolderButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        selectFolderButton.addActionListener(e -> chooseFolder());
        controlsPanel.add(selectFolderButton, gbc);

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        organizeOthersCheckBox = new JCheckBox("Mover extensoes desconhecidas para 'Outros'", true);
        dryRunCheckBox = new JCheckBox("Modo simulacao (apenas exibir no log)", false);
        optionsPanel.add(organizeOthersCheckBox);
        optionsPanel.add(dryRunCheckBox);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        controlsPanel.add(optionsPanel, gbc);

        JPanel actionPanel = new JPanel(new BorderLayout(10, 6));
        actionPanel.setBorder(new EmptyBorder(6, 0, 4, 0));

        startButton = new JButton("Iniciar Organizacao");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        startButton.setEnabled(false);
        startButton.addActionListener(e -> executeOrganization());

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Aguardando");

        actionPanel.add(startButton, BorderLayout.NORTH);
        actionPanel.add(progressBar, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        controlsPanel.add(actionPanel, gbc);

        panel.add(controlsPanel, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setText("Selecione uma pasta para comecar.\n");

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log"));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));

        statusLabel = new JLabel("Status: Aguardando");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel authorLabel = new JLabel("Gabriel Vinicius (@Gabrila7)");
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        authorLabel.setForeground(Color.GRAY);

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(authorLabel, BorderLayout.EAST);
        return panel;
    }

    private void chooseFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecione a pasta para organizar");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        if (downloadsDir.exists()) {
            chooser.setCurrentDirectory(downloadsDir);
        }

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            selectedDirectory = Paths.get(selected.getAbsolutePath());
            pathField.setText(selectedDirectory.toAbsolutePath().toString());
            startButton.setEnabled(true);
            logArea.append("\nPasta selecionada: " + selectedDirectory.toAbsolutePath() + "\n");
            statusLabel.setText("Status: Pronto");
        }
    }

    private void executeOrganization() {
        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma pasta primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean organizeOthers = organizeOthersCheckBox.isSelected();
        boolean dryRun = dryRunCheckBox.isSelected();

        setControlsEnabled(false);
        progressBar.setValue(0);
        progressBar.setString("Processando...");
        statusLabel.setText("Status: Em andamento...");
        logArea.append("\n--------------------------------------------------\n");

        SwingWorker<OrganizationStats, String> worker = new SwingWorker<OrganizationStats, String>() {
            @Override
            protected OrganizationStats doInBackground() throws Exception {
                service.setLogListener(message -> publish(message));
                service.setProgressListener((current, total) -> {
                    int percent = (int) (((double) current / total) * 100);
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(percent);
                        progressBar.setString(String.format("%d%% (%d/%d)", percent, current, total));
                    });
                });

                return service.organize(selectedDirectory, organizeOthers, dryRun);
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String msg : chunks) {
                    logArea.append(msg + "\n");
                }
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }

            @Override
            protected void done() {
                try {
                    OrganizationStats stats = get();
                    progressBar.setValue(100);
                    progressBar.setString("Concluido");
                    statusLabel.setText("Status: Concluido");

                    logArea.append("\n" + stats.generateSummary() + "\n");
                    logArea.setCaretPosition(logArea.getDocument().getLength());

                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            stats.generateSummary(),
                            dryRun ? "Simulacao Concluida" : "Organizacao Concluida",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (Exception ex) {
                    logArea.append("\n[ERRO] " + ex.getMessage() + "\n");
                    statusLabel.setText("Status: Erro");
                    JOptionPane.showMessageDialog(
                            MainFrame.this,
                            "Erro durante a execucao: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    setControlsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void setControlsEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
        selectFolderButton.setEnabled(enabled);
        organizeOthersCheckBox.setEnabled(enabled);
        dryRunCheckBox.setEnabled(enabled);
    }
}
