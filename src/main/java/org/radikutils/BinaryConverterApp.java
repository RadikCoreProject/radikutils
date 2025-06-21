package org.radikutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BinaryConverterApp extends JFrame {
    private JTextField inputField;
    private JComboBox<String> encodingSelector;
    private JTextArea outputField;
    private JButton toBinaryBtn, toTextBtn, clearBtn, copyBtn;

    public BinaryConverterApp() {
        initUI();
    }

    private void initUI() {
        setTitle("Binary Coder/Decoder");
        setSize(500, 212);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));


        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(50, 40, 50));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.BOLD, 24));
        inputField.setBackground(new Color(64, 64, 64));
        inputField.setForeground(new Color(100, 200, 100));
        inputField.setCaretColor(Color.GREEN);
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.add(inputField, BorderLayout.CENTER);


        String[] encodings = {"UTF-8", "Windows-1251", "KOI8-R", "UTF-16", "UTF-32"};
        encodingSelector = new JComboBox<>(encodings);
        encodingSelector.setFont(new Font("Arial", Font.PLAIN, 16));
        encodingSelector.setBackground(new Color(64, 64, 80));
        encodingSelector.setForeground(Color.WHITE);
        encodingSelector.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(encodingSelector, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.NORTH);


        outputField = new JTextArea();
        outputField.setFont(new Font("Courier", Font.PLAIN, 14));
        outputField.setBackground(new Color(50, 40, 50));
        outputField.setForeground(new Color(100, 200, 100));
        outputField.setEditable(false);
        outputField.setLineWrap(false);
        outputField.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputField);
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.setBackground(new Color(30, 30, 30));

        toBinaryBtn = createButton("Текст → Бинарник", new Color(64, 80, 64));
        toBinaryBtn.addActionListener(this::convertToBinary);

        toTextBtn = createButton("Бинарник → Текст", new Color(80, 64, 64));
        toTextBtn.addActionListener(this::convertToText);

        clearBtn = createButton("Очистить", new Color(64, 64, 64));
        clearBtn.addActionListener(e -> clearFields());

        copyBtn = createButton("Копировать", new Color(64, 64, 64));
        copyBtn.addActionListener(e -> copyToClipboard());

        buttonPanel.add(toBinaryBtn);
        buttonPanel.add(toTextBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(copyBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void convertToBinary(ActionEvent e) {
        String text = inputField.getText();
        String encoding = (String) encodingSelector.getSelectedItem();

        try {
            byte[] bytes;
            switch (encoding) {
                case "UTF-8":
                    bytes = text.getBytes(StandardCharsets.UTF_8);
                    break;
                case "Windows-1251":
                    bytes = text.getBytes(Charset.forName("Windows-1251"));
                    break;
                case "KOI8-R":
                    bytes = text.getBytes(Charset.forName("KOI8-R"));
                    break;
                case "UTF-16":
                    bytes = text.getBytes(StandardCharsets.UTF_16);
                    break;
                case "UTF-32":
                    bytes = text.getBytes(Charset.forName("UTF-32"));
                    break;
                default:
                    bytes = text.getBytes();
            }

            StringBuilder binary = new StringBuilder();
            for (byte b : bytes) {
                binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')).append(" ");
            }

            outputField.setText(binary.toString().trim());
        } catch (Exception ex) {
            outputField.setText("Ошибка кодирования: " + ex.getMessage());
        }
    }

    private void convertToText(ActionEvent e) {
        String binaryStr = inputField.getText().trim();
        String encoding = (String) encodingSelector.getSelectedItem();

        if (binaryStr.isEmpty()) {
            outputField.setText("Введите бинарный код для декодирования");
            return;
        }

        try {
            String[] parts = binaryStr.split("\\s+");
            List<Byte> bytesList = new ArrayList<>();

            for (String part : parts) {

                String cleanPart = part.replaceAll("[^01]", "");

                if (!cleanPart.isEmpty()) {
                    try {
                        byte b = (byte) Integer.parseInt(cleanPart, 2);
                        bytesList.add(b);
                    } catch (NumberFormatException ex) {

                    }
                }
            }

            if (bytesList.isEmpty()) {
                throw new IllegalArgumentException("Не найдено валидных бинарных данных");
            }

            byte[] bytes = new byte[bytesList.size()];
            for (int i = 0; i < bytesList.size(); i++) {
                bytes[i] = bytesList.get(i);
            }

            String text;
            switch (encoding) {
                case "UTF-8":
                    text = new String(bytes, StandardCharsets.UTF_8);
                    break;
                case "Windows-1251":
                    text = new String(bytes, Charset.forName("Windows-1251"));
                    break;
                case "KOI8-R":
                    text = new String(bytes, Charset.forName("KOI8-R"));
                    break;
                case "UTF-16":
                    text = new String(bytes, StandardCharsets.UTF_16);
                    break;
                case "UTF-32":
                    text = new String(bytes, Charset.forName("UTF-32"));
                    break;
                default:
                    text = new String(bytes);
            }

            outputField.setText(text);
        } catch (Exception ex) {
            outputField.setText("Ошибка декодирования: " + ex.getMessage());
        }
    }

    private void copyToClipboard() {
        String text = outputField.getText();
        if (!text.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(text), null);
        }
    }

    private void clearFields() {
        inputField.setText("");
        outputField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BinaryConverterApp app = new BinaryConverterApp();
            app.setVisible(true);
        });
    }
}