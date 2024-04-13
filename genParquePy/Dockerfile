# Base image with Python environment
FROM python:3.8

# Install required libraries
WORKDIR /
RUN apt-get update && apt-get install telnet postgresql-client vim -y
RUN pip install pyarrow boto3

# Copy Python script and data
COPY . .

# Define environment variables (optional)
ENV FILE_JSON="simulate_etl/regions.json"
ENV URL_LOCALSTACK="http://localstack:4566"
ENV AWS_ACCESS_KEY_ID="aws_user"
ENV AWS_SECRET_ACCESS_KEY="aws_root"
ENV S3_BUCKET="summary-by-regions"

# Entrypoint script execution
#ENTRYPOINT ["python", "process_and_upload.py"]

# Optional command to run script during build (for testing)
# CMD ["python", "process_and_upload.py"]

#keep runing images
ENTRYPOINT ["tail", "-f", "/dev/null"]
