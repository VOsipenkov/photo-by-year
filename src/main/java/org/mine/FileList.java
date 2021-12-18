package org.mine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mine.FileService.PHOTO_BY_YEAR;

public class FileList {
    private static final String ATTRIBUTE = "year";
    private static final String PNG = "png";
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";

    /**
     * Из текущей директории собираются все пути
     *
     * @throws IOException
     */
    public List<Path> listPaths() {
        return listPath(Path.of("./"));
    }

    public List<Path> listPath(Path path) {
        try {
            return Files.list(path)
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toString().endsWith(PNG)
                    || p.toString().endsWith(JPG)
                    || p.toString().endsWith(JPEG))
                .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Ошибка во время чтения списка путей..");
        }

        return null;
    }

    /**
     * Передаются все пути. Группируются по годам
     *
     * @param paths
     */
    public HashMap<String, List<Path>> groupByYear(List<Path> paths) {
        var byYear = new HashMap<String, List<Path>>();
        paths.stream().forEach(path -> {
            var year = getAttribute(path);
            var value = byYear.get(year);
            if (value == null) {
                value = new ArrayList<>();
            }
            value.add(path);
            byYear.put(year, value);
        });
        return byYear;
    }

    public void writeGroups(HashMap<String, List<Path>> paths) {
        paths.keySet().forEach(year -> createDir(year));
        paths.forEach((k, v) -> v.forEach(pathSource -> fileCopy(k, pathSource)));
    }

    private void fileCopy(String year, Path source) {
        try {
            var name = source.toString();
            var target = Path.of(PHOTO_BY_YEAR.toString(), name);
            Files.copy(source, target);
        } catch (Exception e) {
            System.out.println("При копировании файлов возникла ошибка");
            e.printStackTrace();
        }
    }

    private void createDir(String name) {
        try {
            Files.createFile(Path.of(PHOTO_BY_YEAR.toString(), name));
        } catch (IOException e) {
            System.out.format("Не удалось создать директорию с именем %s", name);
            e.printStackTrace();
        }
    }

    /**
     * из полученного пути извлекается значение параметра год
     *
     * @param path
     * @return
     */
    private static String getAttribute(Path path) {
        try {
            var value = Files.getAttribute(path, ATTRIBUTE);
            return (String) value;
        } catch (Exception e) {
            System.out.println("error whith path " + path);
        }
        return null;
    }

    public long count(List<Path> paths) {
        return paths.stream().count();
    }
}
