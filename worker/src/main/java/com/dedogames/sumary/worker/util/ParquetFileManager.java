package com.dedogames.sumary.worker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ParquetFileManager {

    public static String listFilesWithDetails(java.nio.file.Path directoryPath) throws IOException {

        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + directoryPath);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Files downloaded:\n");
        int count = 1;

        try (DirectoryStream<java.nio.file.Path> stream = Files.newDirectoryStream(directoryPath, path -> !Files.isDirectory(path))) {
            for (java.nio.file.Path path : stream) {
                stringBuilder.append("  ").append(count++).append(" - ").append(path.getFileName()).append("\n");
            }
        }

        if (stringBuilder.length() == 0) {
            stringBuilder.append("No files found in directory: ").append(directoryPath);
        }

        return stringBuilder.toString();
    }

    public Set<String> getListFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static ParquetReader<GenericRecord> load(String fileNameParqeut)  throws Exception {

        String json= null;

        java.nio.file.Path homeDirectory = Paths.get(System.getProperty("user.dir"));
        Path dir = homeDirectory.resolve(fileNameParqeut);
        InputFile  ipFile = (InputFile) dir;
        ObjectMapper objectMapper = new ObjectMapper();
        ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(ipFile).build();

        return reader;
    }

    public static void save(byte[] data, String fileName) throws IOException {
        String currentFolder = System.getProperty("user.dir");
        File myFile = new File(currentFolder+ "/"+fileName);
        OutputStream os = new FileOutputStream(myFile);
        os.write(data);
        os.close();
    }
}
