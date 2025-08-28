package com.project.batch.domain.file;

import com.project.batch.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class PartitionedFileRepository {

    private final ConcurrentMap<String, File> fileMap = new ConcurrentHashMap<>();

    public File putFile(String partition, String prefix, String suffix) throws IOException {
        File file = FileUtils.createTempFile(prefix, suffix);
        fileMap.put(partition, file);
        return file;
    }

    public List<File> getFiles() {
        return fileMap.values().stream()
                .sorted(Comparator.comparing(File::getName))
                .toList();
    }

}
