import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Button {
    public static JButton createButton(MainWindow mainWindow, String iconPath, int width, int height) {
        JButton button = new JButton();

        java.net.URL imgUrl = mainWindow.getClass().getResource(iconPath);
        Icon icon = new ImageIcon(imgUrl);
        Image img = ((ImageIcon) icon).getImage();
        Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        Icon newIcon = new ImageIcon(newImg);

        button.setIcon(newIcon);
        button.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // Configuração do botão Abrir

        return button;
    }

    public static void buttonCompileActionPerformed(java.awt.event.ActionEvent evt, javax.swing.JTextArea sourceInput, Semantico sem, List<Symbol> symbols, javax.swing.JTextArea console, JTable table) {// GEN-FIRST:event_buttonCompileActionPerformed
        Lexico lex = new Lexico();
        Sintatico sint = new Sintatico();
        lex.setInput(new StringReader(sourceInput.getText()));
        sem.symbolTableShow.clearTable();
        sem.symbolTable.clearTable();
    
        symbols.clear();
    
        try {
            sint.parse(lex, sem);
            
            String mensagemNaoUtilizadas = WarningVerification.verificarVariaveisNaoUtilizadas(sem.symbolTableShow);
            String mensagemNaoInicializadas = WarningVerification.verificarVariaveisNaoInicializadas(sem.symbolTableShow);
            
            StringBuilder mensagem = new StringBuilder("Compilado com sucesso!\n");
            
            if (!mensagemNaoUtilizadas.equals("Compilado com sucesso!\n")) {
                mensagem.append(mensagemNaoUtilizadas).append("\n");
            }
            if (!mensagemNaoInicializadas.equals("Compilado com sucesso!\n")) {
                mensagem.append(mensagemNaoInicializadas);
            }
            
            console.setText(mensagem.toString());
            
            updateTable(sem, table);
        } catch (LexicalError | SyntaticError | SemanticError ex) {
            console.setText("Problema na compilação: " + ex.getLocalizedMessage());
            symbols.clear();
            updateTable(sem, table);
        }
        
    }// GEN-LAST:event_buttonCompileActionPerformed
    


    public static void buttonPasteCustomCode(ActionEvent evt, javax.swing.JTextArea sourceInput) {
        List<String> codigos = codigosPadroes.getCodigosPadroes();
    
        if (!codigos.isEmpty()) {
            // Obtém o código atual baseado no índice atual
            String codigoAtual = codigos.get(codigoAtualIndex);
            sourceInput.setText(codigoAtual);
            
            // Incrementa o índice para obter o próximo código na próxima vez que o botão for clicado
            codigoAtualIndex = (codigoAtualIndex + 1) % codigos.size();
        } else {
            sourceInput.setText("Nenhum código padrão disponível.");
        }
    }

    public static void buttonSaveActionPerformed(java.awt.event.ActionEvent evt, javax.swing.JTextArea console, javax.swing.JTextArea sourceInput) {
        JFileChooser fileChooser = new JFileChooser();
        if (lastSavedFilePath != null) {
            console.setText("Arquivo foi salvo com sucesso!\n" + lastSavedFilePath);
            fileChooser.setCurrentDirectory(new File(lastSavedFilePath).getParentFile());
            File selectedFile = new File(lastSavedFilePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                writer.write(sourceInput.getText());
                lastSavedFilePath = selectedFile.getAbsolutePath();
                console.setText("Arquivo foi salvo com sucesso!\n" + lastSavedFilePath);
                //setTitle(selectedFile.getName() + " - IDE do Professor");

            } catch (IOException e) {
                console.setText("Erro ao salvar o arquivo: " + e.getMessage());
            }

        } else {
            buttonSaveAsActionPerformed(evt, console, sourceInput);
        }
    }

    public static void buttonSaveAsActionPerformed(java.awt.event.ActionEvent evt, javax.swing.JTextArea console, javax.swing.JTextArea sourceInput) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            if (!filePath.contains(".")) {
                selectedFile = new File(filePath + ".clz");
            } else if (!filePath.endsWith(".clz")) {
                JOptionPane.showMessageDialog(null, "A extensão do arquivo deve ser .clz", "Erro de Extensão",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                writer.write(sourceInput.getText());
                lastSavedFilePath = selectedFile.getAbsolutePath();
                //setTitle(selectedFile.getName() + " - IDE do Professor");
                console.setText("Arquivo salvo com sucesso!");
            } catch (IOException e) {
                console.setText("Erro ao salvar o arquivo: " + e.getMessage());
            }
        }
    }

    public static void buttonOpenActionPerformed(java.awt.event.ActionEvent evt, javax.swing.JTextArea console, javax.swing.JTextArea sourceInput) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.getName().endsWith(".clz")) {
                JOptionPane.showMessageDialog(null, "A extensão do arquivo deve ser .clz", "Erro de Extensão",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                sourceInput.setText(content.toString());
                console.setText("Arquivo aberto com sucesso!");
                //setTitle(selectedFile.getName() + " - IDE do Professor");
                lastSavedFilePath = selectedFile.getAbsolutePath();

            } catch (IOException e) {
                console.setText("Erro ao abrir o arquivo: " + e.getMessage());
            }
        }
    }


    private static void updateTable(Semantico sem, JTable table) {
        List<Symbol> symbols = sem.symbolTableShow.getSymbols();
        String[] columnNames = { "ID", "Tipo", "Inicializado", "Usado", "Escopo", "Parametro", "Posição", "Vetor",
                "Matriz", "Referência", "Função", "Procedimento" };
        Object[][] data = new Object[symbols.size()][columnNames.length];
    
        for (int i = 0; i < symbols.size(); i++) {
            Symbol symbol = symbols.get(i);
            data[i] = new Object[] { symbol.getId(), symbol.getTipo(), symbol.isIni(), symbol.isUsada(),
                    symbol.getEscopo(), symbol.isParam(), symbol.getPos(), symbol.isVet(), symbol.isMatriz(),
                    symbol.isRef(), symbol.isFunc(), symbol.isProc() };
        }
    
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
    }

    private static int codigoAtualIndex = 0;
    private static CodigosPadroes codigosPadroes = new CodigosPadroes();
    private static String lastSavedFilePath; // Variável para armazenar o último caminho do arquivo salvo

}
