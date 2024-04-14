package com.dedogames.sumary.worker.aws;

import com.dedogames.summary.shared.observability.SimpleLogger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DownloadDirectoryRequest;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import software.amazon.awssdk.regions.Region;
import java.nio.file.*;

@Service
public class S3Connector {
    private SimpleLogger logger = new SimpleLogger(S3Connector.class);

    private String endPoint;

    public Path getFullParquetdir() {
        return parquetdir;
    }

    private Path parquetdir;

    public S3Connector( ){

        Region region = Region.US_EAST_1;
        logger.info("Initialize Default S3Connector in  "+region.toString()+" with default S3 Client" );
        endPoint = null;

        Path homeDirectory = Paths.get(System.getProperty("user.dir"));
        Path localFolderPath = Paths.get("defaultDir");
        parquetdir  = homeDirectory.resolve(localFolderPath);
    }

    public S3Connector( String localFolder){

        Region region = Region.US_EAST_1;
        logger.info("Initialize S3Connector in  "+region.toString()+" with default S3 Client" );
        endPoint = null;

        Path homeDirectory = Paths.get(System.getProperty("user.dir"));
        Path localFolderPath = Paths.get(localFolder);
        parquetdir  = homeDirectory.resolve(localFolderPath);
    }
    public S3Connector(String endpoint,String strRegion, String localFolder) {
        Region region = Region.US_EAST_1;
        if(strRegion != "us-east-1"){
            region = Region.of(strRegion);
        }
        logger.info("Initialize S3Connector in  "+region.toString()+" with S3 Client endpoint: "+endpoint );

        Path homeDirectory = Paths.get(System.getProperty("user.dir"));
        Path localFolderPath = Paths.get(localFolder);
        parquetdir  = homeDirectory.resolve(localFolderPath);
        endPoint = endpoint;

    }

    public boolean downloadDirectory( String bucketName, String bucketFolder ) throws IOException {

        Files.createDirectories(parquetdir);
        logger.info("Directory created successfully: " + parquetdir);

        //Default client S3
        S3TransferManager transferManager = S3TransferManager.create();

        if(endPoint != null) {
            URI customEndpoint = URI.create(this.endPoint); // Replace with your endpoint

            S3AsyncClient s3AsyncClient = S3AsyncClient.builder()
                    .endpointOverride(customEndpoint)
                    .forcePathStyle(true)
                    .build();

            transferManager = S3TransferManager.builder()
                    .s3Client(s3AsyncClient)
                    .build();
        }

        DirectoryDownload directoryDownload = transferManager.downloadDirectory(DownloadDirectoryRequest.builder()
                .destination(parquetdir)
                .bucket(bucketName)
                .listObjectsV2RequestTransformer(l -> l.prefix(bucketFolder))
                .build());

        try {
            CompletedDirectoryDownload completedDirectoryDownload = directoryDownload.completionFuture().join();
        }catch(Exception e){
            logger.error(e.getMessage());
        }
       logger.info( listFilesWithDetails(parquetdir));
       return true;

    }

    public String listFilesWithDetails( Path directoryPath ) throws IOException {

        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + directoryPath);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Files downloaded:\n");
        int count = 1;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, path -> !Files.isDirectory(path))) {
            for (Path path : stream) {
                stringBuilder.append("  ").append(count++).append(" - ").append(path.getFileName()).append("\n");
            }
        }

        if (stringBuilder.length() == 0) {
            stringBuilder.append("No files found in directory: ").append(directoryPath);
        }

        return stringBuilder.toString();
    }
}



