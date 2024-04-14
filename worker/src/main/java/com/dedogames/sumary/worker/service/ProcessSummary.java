package com.dedogames.sumary.worker.service;


import com.dedogames.sumary.shared.observability.SimpleLogger;

import com.dedogames.sumary.worker.aws.S3Connector;
import com.dedogames.sumary.worker.util.ParquetFileManager;
import jakarta.annotation.PostConstruct;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.parquet.hadoop.ParquetReader;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessSummary {


    private SimpleLogger logger = new SimpleLogger(ProcessSummary.class);

    @Value("${flow.bucketS3Name}")
    String bucketName;

    @Value("${flow.path}")
    String localFolder;

    @Value("${flow.region}")
    String region;

    @Value("${flow.fileS3Name}")
    String fileS3Name;

    @Value("${flow.endpoint:#{null}}")
    String endpoint;
//    private S3Connector s3Conn;

    private S3Connector s3Connector;

    @PostConstruct
    void startFlow() {

        try {

            s3Connector = new S3Connector(endpoint, region, localFolder);

            // 1 - Load from S3
            boolean ret = this.s3Flow();
            if (!ret) return;

            // 2 - Load Parquet from file and parse
            this.localParquetRds();

            // 3 - Save into database
            //    this.rdsFlow();

        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    boolean s3Flow() throws Exception {
        return s3Connector.downloadDirectory(bucketName, fileS3Name);
    }

    void localParquetRds() throws Exception {
        GenericRecord record;
        String field1 = null;
        String field2 = "";
        String fullPath = s3Connector.getFullParquetdir().toString();
        List<ParquetReader<GenericRecord>> listGenericRecords =  ParquetFileManager.loadParquetFilesFromFolder(fullPath);
        for (ParquetReader<GenericRecord> reader : listGenericRecords) {

            while ((record = reader.read()) != null) {
                field1 = record.get("field1").toString();
                Object filtersObject = record.get("field2");
                if (filtersObject instanceof Utf8) {
                    try {
                        JSONObject filters = new JSONObject();
                        Utf8 utf8 = (Utf8) filtersObject;
                        field2 = utf8.toString();
                    } catch (Exception e) {
                        logger.error("Error creating JSON object for filters: " + e.getMessage());
                    }
                } else {
                    logger.error("Error: filters field is not a GenericRecord");
                }

                System.out.println("field1: "+ field1 + " field2: "+ field2);
            }
        }
        ;
    }

    void rdsFlow() throws Exception {

    }
}

