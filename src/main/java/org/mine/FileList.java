package org.mine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mine.FileService.PHOTO_BY_YEAR;

public class FileList {
    private static final String ATTRIBUTE = "creationTime";
    private static final String PNG = "png";
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";
    /**
     * Из текущей директории собираются все пути
     *
     * @throws IOException
     */
    public List<Path> listFilesByPath() {
        return listFilesByPath(Path.of("./"));
    }

    public List<Path> listFilesByPath(Path path) {
        try {
            return Files.find(path, 10, (p, attr) -> attr.isRegularFile() && isImage(p))
                .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Ошибка во время чтения списка путей..");
        }
        return null;
    }

    private boolean isImage(Path path) {
        var name = path.toString();
        return name.endsWith(PNG) || name.endsWith(JPG) || name.endsWith(JPEG);
    }

    /**
     * Передаются все пути. Группируются по годам
     *
     * @param paths
     */
    public HashMap<Integer, List<Path>> groupByYear(List<Path> paths) {
        var byYear = new HashMap<Integer, List<Path>>();
        paths.stream().forEach(path -> {
            var date = getAttribute(path);
            var localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(FORMAT));
            var year  = localDateTime.getYear();
            var value = byYear.get(year);
            if (value == null) {
                value = new ArrayList<>();
            }
            value.add(path);
            byYear.put(year, value);
        });
        return byYear;
    }

    public void writeGroups(HashMap<Integer, List<Path>> paths) {
        paths.keySet().forEach(year -> createDir(year));
        paths.forEach((k, v) -> v.forEach(pathSource -> fileCopy(k, pathSource)));
    }

    private void fileCopy(Integer year, Path source) {
        try {
//            var name = source.toString();
            var target = Path.of(PHOTO_BY_YEAR.toString(), year.toString());
            Files.copy(source, target);
        } catch (Exception e) {
            System.out.println("При копировании файлов возникла ошибка");
            e.printStackTrace();
        }
    }

    private void createDir(Integer name) {
        try {
            Files.createDirectories(Path.of(PHOTO_BY_YEAR.toString(), name.toString()));
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
            return ((FileTime)value).toString();
        } catch (Exception e) {
            System.out.println("error whith path " + path);
        }
        return null;
    }

    public long count(List<Path> paths) {
        return paths.stream().count();
    }
}
