package org.mine;

import java.io.File;
import java.nio.file.Path;

public class FileServiceImpl implements FileService {
    private FileList fileList = new FileList();

    @Override
    public long countSourceFiles() {
        var pathList = fileList.listPaths();
        return fileList.count(pathList);
    }

    @Override
    public long countTargetFiles() {
        var pathList = fileList.listPath(PHOTO_BY_YEAR);
        return fileList.count(pathList);
    }

    @Override
    public void groupFiles() {
        var paths = fileList.listPaths();
        var grouped = fileList.groupByYear(paths);
        fileList.writeGroups(grouped);
    }
}
