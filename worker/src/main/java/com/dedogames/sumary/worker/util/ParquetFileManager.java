package com.dedogames.sumary.worker.util;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.hadoop.fs.Path;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParquetFileManager {

    public static List<ParquetReader<GenericRecord>> loadParquetFilesFromFolder(String folderPath) throws Exception {
        List<ParquetReader<GenericRecord>> readers = new ArrayList<>();
        File directory = new File(folderPath);

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + folderPath);
        }

        // Get all files with `.parquet` extension
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".parquet")) {
                ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(new Path(file.getAbsolutePath())).build();
                readers.add(reader);
            }
        }

        return readers;
    }

}