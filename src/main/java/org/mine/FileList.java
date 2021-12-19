package org.mine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.List.of;
import static org.mine.FileService.PHOTO_BY_YEAR;

public class FileList {
    private static final String LAST_MODIFIED_TIME = "lastModifiedTime";
    private static final String CREATION_TIME = "creationTime";
    private static final String PNG = "png";
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private static final String DATE_IN_NAME_REGEXP =
        "(\\d{4})[-.:]{0,1}(\\d{2})[-.:]{0,1}(\\d{2})('T'){0,1}(\\d{2}){0,1}[-.:]{0,1}(\\d{2}){0,1}[:.-]{0,1}(\\d{2}){0,1}";
    private static final DateTimeFormatter FORMAT1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
    private static final DateTimeFormatter FORMAT2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter FORMAT3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
    private static final DateTimeFormatter FORMAT4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter FORMAT5 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private static final DateTimeFormatter FORMAT6 = DateTimeFormatter.ofPattern("yyyy-MM-dd'_'HH-mm-ss");
    private static final DateTimeFormatter FORMAT7 = DateTimeFormatter.ofPattern("yyyy:MM:dd'_'HH:mm:ss");
    private static final DateTimeFormatter FORMAT8 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final List<DateTimeFormatter> PATTERNS_WITH_TIME = of(FORMAT1, FORMAT2, FORMAT3,
        FORMAT4, FORMAT5, FORMAT6, FORMAT7);
    private static final List<DateTimeFormatter> PATTERNS = of(FORMAT8);

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
            out.println("Ошибка во время чтения списка путей..");
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

    public void writeGroups(HashMap<Integer, List<Path>> paths) {
        paths.keySet().forEach(year -> createDir(year));
        paths.forEach((k, v) -> v.forEach(pathSource -> fileCopy(k, pathSource)));
    }

    public long count(List<Path> paths) {
        return paths.stream().count();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void fileCopy(Integer year, Path source) {
        try {
            var name = source.getFileName().toString();
            var target = Path.of(PHOTO_BY_YEAR.toString(), year.toString(), name).normalize();
            Files.copy(source, target);
        } catch (Exception e) {
            out.println("При копировании файлов возникла ошибка");
            e.printStackTrace();
        }
    }

    private void createDir(Integer name) {
        try {
            Files.createDirectories(Path.of(PHOTO_BY_YEAR.toString(), name.toString()));
        } catch (IOException e) {
            out.format("Не удалось создать директорию с именем %s", name);
            e.printStackTrace();
        }
    }

    /**
     * выбираем самое раннее упоминания о файле из аттрибутов и имени
     *
     * @param path
     * @return
     */
    private int getAttribute(Path path) {
        try {
            var lastModifiedText = Files.getAttribute(path, LAST_MODIFIED_TIME).toString();
            var creationTimeText = Files.getAttribute(path, CREATION_TIME).toString();
            var fileName = path.getFileName().toString();
            var matcher = Pattern.compile(DATE_IN_NAME_REGEXP).matcher(fileName);
            String creationByName = null;
            if (matcher.find()) {
                creationByName = matcher.group();
            }

            return IntStream.of(getYear(lastModifiedText), getYear(creationTimeText), getYear(creationByName))
                .min().orElse(Integer.MAX_VALUE);
        } catch (Exception e) {
            out.println("error whith path " + path);
        }
        return 0;
    }

    private int getYear(String date) {
        if (date != null) {
            for (DateTimeFormatter formatter : PATTERNS_WITH_TIME) {
                try {
                    var localDateTime = LocalDateTime.parse(date, formatter);
                    return localDateTime.getYear();
                } catch (Exception e) {
                }
            }

            for (DateTimeFormatter formatter : PATTERNS) {
                try {
                    var localDate = LocalDate.parse(date, formatter);
                    return localDate.getYear();
                } catch (Exception e) {
                }
            }
        }

        return Integer.MAX_VALUE;
    }
}
