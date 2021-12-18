package org.mine;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static FileService fileService = new FileServiceImpl();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JButton fileCountInSource = new JButton("Посчитать для копирования");
        fileCountInSource.setSize(new Dimension(100, 50));
        JButton fileCountInTarget = new JButton("Посчитать скопированные");
        JButton fileGroup = new JButton("Сгруппировать по годам в C:\\photo-by-year");
        JButton fileCopy = new JButton("Скопировать в C:\\photo");
        JTextField jTextField = new JTextField();
        jTextField.setColumns(10);
        JPanel panel = new JPanel();
        panel.add(fileCountInSource);
        panel.add(fileCountInTarget);
        panel.add(fileCopy);
        panel.add(fileGroup);
        panel.add(jTextField);
        frame.getContentPane().add(panel);
        listenersConfig(fileCountInSource, fileCountInTarget, fileCopy, fileGroup, jTextField);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setVisible(true);
    }

    private static void listenersConfig(JButton fileCountInSource, JButton fileCountInTarget,
        JButton fileCopy, JButton fileGroup, JTextField jTextField) {
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
    }
}
