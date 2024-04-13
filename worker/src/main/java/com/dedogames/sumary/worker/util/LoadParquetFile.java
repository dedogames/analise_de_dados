package com.dedogames.sumary.worker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.InputFile;

import java.io.*;

public class LoadParquetFile {

    public static ParquetReader<GenericRecord> load(String fileNameParqeut)  throws Exception {

        String currentFolder = System.getProperty("user.dir");
        String json= null;

        Path file = new Path(currentFolder+ "/"+fileNameParqeut);

        ObjectMapper objectMapper = new ObjectMapper();
        ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(file).build();
//
//        ParquetReader<GenericRecord> reader = null;
//        try{
//            reader = AvroParquetReader.<GenericRecord>builder(file).build();
//        }catch(Exception e){
//            System.out.println(e.toString());
//        }
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
