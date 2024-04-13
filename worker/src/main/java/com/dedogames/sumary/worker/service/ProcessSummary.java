package com.dedogames.sumary.worker.service;

import com.amazonaws.SdkClientException;
import com.dedogames.sumary.shared.observability.SimpleLogger;
import com.dedogames.sumary.worker.aws.S3Connector;
import com.dedogames.sumary.worker.util.LoadParquetFile;
import jakarta.annotation.PostConstruct;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.hadoop.ParquetReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.net.URI;

@Service
public class ProcessSummary {


    private SimpleLogger logger = new SimpleLogger(ProcessSummary.class);

    @Value("${flow.bucketS3Name}")
    String bucketName;

    @Value("${flow.path}")
    String path;

    @Value("${flow.region}")
    String region;

    @Value("${flow.fileS3Name}")
    String fileS3Name;

    @Value("${flow.endpoint}")
    String endpoint;
    private S3Connector s3Conn;

    @PostConstruct
    void startFlow(){

        try{

//        s3Conn = new S3Connector(endpoint, region);
            s3Conn = new S3Connector( region);
        // 1 - Load from S3
        boolean ret = this.s3Flow();
        if(!ret) return;

        // 2 - Load Parquet from file and parse
        this.fileParquetFlow();

        // 3 - Save into database
        this.rdsFlow();

        }catch(Exception e){
            logger.error(e.toString());
        }

    }

    boolean s3Flow() throws Exception{

        s3Conn.downloadDirectory(bucketName,fileS3Name,path);
//        if(s3Conn.hasBucket(bucketName)) {
//            s3Conn.getObjectBytes(bucketName, fileS3Name, path);
//            return true;
//        }else{
//            logger.error("Bucket "+ bucketName + " Not found!");
//            return false;
//        }
        return false;
    }

    void fileParquetFlow() throws Exception{
        ParquetReader<GenericRecord> parquet = LoadParquetFile.load(fileS3Name);
        System.out.println("");
;    }

    void rdsFlow() throws Exception{

    }
}



/*
 S3Connector s3Conn = new S3Connector();
//        String bucketName ="symmary-by-regions";
//        String keyName = "regions.parquet";
        String arn = "arn:aws:iam::444914307613:role/k8s-app-discord-worker-prod";
        String bucketName = "olxbr-dl-zap-re-marketplace-balance";
        String keyName = "parquet-filters";
        String path = "/Users/gelson.rodrigues/projects/data_analysis/docker";

        Region region = Region.US_EAST_1;



        final String ACCESS_KEY = "aws_user";
        final String SECRET_KEY = "aws_pwd";
        S3Client s3Client = S3Client.builder()
//                .endpointOverride(URI.create("http://localhost:4566"))
//                .forcePathStyle(true)
                .crossRegionAccessEnabled(true)
                .useArnRegion(true)
                .region(region)
                .build();

        if(s3Conn.hasBucket(s3Client,bucketName)) {
            s3Conn.listBuckets(s3Client);
            s3Conn.getObjectBytes(s3Client, bucketName, keyName, path);
        }
* */
