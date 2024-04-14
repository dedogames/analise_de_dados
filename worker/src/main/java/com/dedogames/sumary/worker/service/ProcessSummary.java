package com.dedogames.sumary.worker.service;


import com.dedogames.summary.shared.entities.SummaryByRegions;
import com.dedogames.summary.shared.observability.SimpleLogger;

import com.dedogames.sumary.worker.aws.S3Connector;
import com.dedogames.sumary.worker.util.ParquetFileManager;
import com.dedogames.summary.shared.repositories.RepoSummaryByRegions;
import jakarta.annotation.PostConstruct;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.hadoop.ParquetReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Value("${flow.folderS3Path:#{null}}")
    String folderS3Path;

    @Value("${flow.endpoint:#{null}}")
    String endpoint;


    @Autowired
    private RepoSummaryByRegions repo;

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


        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    boolean s3Flow() throws Exception {
        return s3Connector.downloadDirectory(bucketName, folderS3Path);
    }

    void localParquetRds() throws Exception {
        GenericRecord record;

        String fullPath = s3Connector.getFullParquetdir().toString();
        //Load parquet from file
        List<ParquetReader<GenericRecord>> listGenericRecords =  ParquetFileManager.loadParquetFilesFromFolder(fullPath);
        for (ParquetReader<GenericRecord> reader : listGenericRecords) {

            while ((record = reader.read()) != null) {
                List<String> products = new ArrayList<>();
                GenericRecord regionsRecord = (GenericRecord) record.get("regions");
                String state = regionsRecord.get("state").toString();
                String percent = regionsRecord.get("percent").toString();

                //Get list products
                List<GenericRecord> productRecords = (List<GenericRecord>) regionsRecord.get("products");
                for(GenericRecord prod : productRecords){
                    products.add( prod.get("element").toString());
                }
                logger.info(" Summary save into Postgress : State: "+state + " | Percent: "+ percent + "% |  Products:"+ products.toString());
                repo.save(new SummaryByRegions(state,Double.parseDouble(percent), products.toString(), LocalDateTime.now()));

            }
        }
        ;
    }

    void rdsFlow() throws Exception {

    }
}

