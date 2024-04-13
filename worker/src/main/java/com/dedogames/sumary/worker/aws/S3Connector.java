package com.dedogames.sumary.worker.aws;



import com.amazonaws.AmazonServiceException;
import com.dedogames.sumary.shared.observability.SimpleLogger;
import com.dedogames.sumary.worker.util.LoadParquetFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

import java.io.File;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.util.Map;


@Service
public class S3Connector {
    private SimpleLogger logger = new SimpleLogger(S3Connector.class);

    private S3Client s3Client;

    private String endPoint;

    public S3Connector(){}
    public S3Connector(String endpoint,String strRegion) {


        Region region = Region.US_EAST_1;
        if(strRegion != null && !strRegion.isEmpty()){
            region = Region.of(strRegion);
        }

        logger.info("Initialize s3 with endpoint:"+ endpoint + " in Region: "+ region.toString());
        s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .forcePathStyle(true)
                .crossRegionAccessEnabled(true)
                .useArnRegion(true)
                .region(region)
                .build();

    }
//arn:aws:iam::183337677225:role/cli-s3-marketplace-balance-prod
    public S3Connector(String strRegion) {

        Region region = Region.US_EAST_1;
        if(strRegion == null && !strRegion.isEmpty()){
            region = Region.of(strRegion);
        }

        logger.info("Initialize s3 in Region: "+ region.toString());

        s3Client = S3Client.builder()
                .crossRegionAccessEnabled(true)
                .useArnRegion(true)
                .region(region)
                .build();
    }

    public boolean hasBucket( String bucketName){
        try{
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
            for (Bucket bucket : listBucketsResponse.buckets()) {
                System.out.println(bucket.name().toString());
               if(bucketName.contains(bucket.name())){
                   return true;
               }
            }
         } catch (Exception e){
             logger.error(e.toString());
         }
        return false;
    }


    public void downloadDirectory(String bucket_name,String key_prefix, String dir_path ){
        TransferManager xfer_mgr = TransferManagerBuilder.standard().build();

        try {
            MultipleFileDownload xfer = xfer_mgr.downloadDirectory(
                    bucket_name, key_prefix, new File(dir_path));
            // loop with Transfer.isDone()
            XferMgrProgress.showTransferProgress(xfer);
            // or block with Transfer.waitForCompletion()
            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        xfer_mgr.shutdownNow();
    }
    public void getObjectBytes(  String bucketName, String keyName, String localFolder) throws IOException {

            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> dataMap = mapper.readValue(objectBytes.asInputStream(), Map.class);
//            GenericRecord gen = (GenericRecord) dataMap;
            byte[] data = objectBytes.asByteArray();
            LoadParquetFile.save(data, keyName);
            logger.info("Successfully obtained bytes from an S3 object");
    }

}



