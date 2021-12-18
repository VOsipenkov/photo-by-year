package org.mine;

import java.io.File;
import java.nio.file.Path;

/**
 * todo Document type FileServiceImpl
 */
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

    }
}
