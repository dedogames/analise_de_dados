# Data Analisys
Projeto simples em Java 21


# Convert json to parquet and send to S3

### Docker
 
Build image with python
```bash
docker build   -t py_gen_parquet .
```

Run image
 ```bash
docker run -f py_gen_parquet
```

Show content inside bucket
 ```bash
awslocal s3 ls s3://symmary-by-regions --recursive --human-readable
```




