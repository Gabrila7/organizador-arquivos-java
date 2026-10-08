package com.gabrila7.fileorganizer;

import com.gabrila7.fileorganizer.ui.MainFrame;

import javax.swing.*;

/**
 * Ponto de entrada da aplicação Organizador de Arquivos.
 * 
 * @author Gabriel Vinícius (@Gabrila7)
 */
public class Main {

    public static void main(String[] args) {
        // Configura o visual nativo do sistema operacional (Windows/Linux/Mac)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Se falhar, usa o padrão do Swing
        }

        // Inicia a interface gráfica na Event Dispatch Thread (boa prática obrigatória do Swing)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
