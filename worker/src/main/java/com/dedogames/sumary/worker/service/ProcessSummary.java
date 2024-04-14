package com.dedogames.sumary.worker.service;


import com.dedogames.sumary.shared.observability.SimpleLogger;

import com.dedogames.sumary.worker.aws.S3Connector;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

            s3Connector = new S3Connector(endpoint, region);

            // 1 - Load from S3
            boolean ret = this.s3Flow();
            if (!ret) return;

            // 2 - Load Parquet from file and parse
          //  this.fileParquetFlow();

            // 3 - Save into database
        //    this.rdsFlow();

        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    boolean s3Flow() throws Exception {
        return s3Connector.downloadDirectory(bucketName, fileS3Name,localFolder );
    }

    void fileParquetFlow() throws Exception {
//        ParquetReader<GenericRecord> parquet = ParquetFileManager.load(fileS3Name);
        System.out.println("");
        ;
    }

    void rdsFlow() throws Exception {

    }
}

