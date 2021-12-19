package org.mine;

import java.io.File;
import java.nio.file.Path;

public class FileServiceImpl implements FileService {
    private FileList fileList = new FileList();

    @Override
    public long countSourceFiles() {
        var pathList = fileList.listFilesByPath();
        return fileList.count(pathList);
    }

    @Override
    public long countTargetFiles() {
        var pathList = fileList.listFilesByPath(PHOTO_BY_YEAR);
        return fileList.count(pathList);
    }

    @Override
    public void groupFiles() {
        var images = fileList.listFilesByPath();
        var grouped = fileList.groupByYear(images);
        fileList.writeGroups(grouped);
    }
}
