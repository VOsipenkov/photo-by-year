package org.mine;

import java.nio.file.Path;

public interface FileService {
    Path PHOTO_TEMP = Path.of("C:\\photo-temp");
    Path PHOTO_BY_YEAR = Path.of("C:\\photo-by-year");

    long countSourceFiles();

    long countTargetFiles();

    void groupFiles();
}
