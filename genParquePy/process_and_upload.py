import json
import pyarrow as pa
import pyarrow.parquet as pq
import boto3
import os
# Load environment variables (or use hardcoded values)
FILE_JSON = os.getenv("FILE_JSON", "simulate_etl/regions.json")
URL_LOCALSTACK = os.getenv("URL_LOCALSTACK", "http://localhost:4566")
AWS_ACCESS_KEY_ID = os.getenv("AWS_ACCESS_KEY_ID")
AWS_SECRET_ACCESS_KEY = os.getenv("AWS_SECRET_ACCESS_KEY")
S3_BUCKET = os.getenv("S3_BUCKET", "symmary-by-regions")

# Load JSON data
with open(FILE_JSON, "r") as f:
    data = json.load(f)

# Create Parquet table and write data
table = pa.Table.from_pydict(data)
pq.write_table(table, "regions.parquet")

# Configure LocalStack S3 client
s3 = boto3.client(
    "s3",
    endpoint_url=URL_LOCALSTACK,
    aws_access_key_id=AWS_ACCESS_KEY_ID,
    aws_secret_access_key=AWS_SECRET_ACCESS_KEY,
)

s3.create_bucket(Bucket=S3_BUCKET)
print(f"Created bucket {S3_BUCKET}.")

# Upload Parquet file to S3
s3.upload_file("regions.parquet", S3_BUCKET, "regions.parquet")

print("Successfully processed and uploaded data to S3!")

