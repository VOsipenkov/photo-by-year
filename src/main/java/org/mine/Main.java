package org.mine;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class Main {
    private static FileService fileService = new FileServiceImpl();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JButton fileCountInSource = new JButton("Count in source");
        fileCountInSource.setSize(new Dimension(100, 50));
        JButton fileCountInTarget = new JButton("Count in target");
        JButton fileGroup = new JButton("Grouped by year in C:\\photo-by-year");
        JButton clean = new JButton("clean C:\\photo-by-year");
        JTextField jTextField = new JTextField();
        jTextField.setColumns(10);
        JPanel panel = new JPanel();
        panel.add(fileCountInSource);
        panel.add(fileCountInTarget);
        panel.add(fileGroup);
        panel.add(jTextField);
        frame.getContentPane().add(panel);
        listenersConfig(fileCountInSource, fileCountInTarget, fileGroup, jTextField, clean);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setVisible(true);
    }

    private static void listenersConfig(JButton fileCountInSource, JButton fileCountInTarget,
        JButton fileGroup, JTextField jTextField, JButton clean) {
        fileCountInSource.addActionListener(e -> {
            var count = fileService.countSourceFiles();
            System.out.format("В текущей директории найдено %d фотографий %n", count);
            jTextField.setText(String.valueOf(count));
        });

        fileCountInTarget.addActionListener(e -> {
            var count = fileService.countTargetFiles();
            System.out.format("В директории %s найдено %d фотографий %n ", FileService.PHOTO_BY_YEAR, count);
            jTextField.setText(String.valueOf(count));
        });

        fileGroup.addActionListener(e -> {
            System.out.println("Группировка начата");
            var startCount = fileService.countSourceFiles();
            fileService.groupFiles();
            var targetCount = fileService.countTargetFiles();
            assert startCount == targetCount;
            System.out.println("Группировка завершена");
            jTextField.setText("Готово");
        });

        clean.addActionListener(e -> {
            var path = Path.of("C:\\photo-by-year");
            //todo логика очистки
        });
    }
}
