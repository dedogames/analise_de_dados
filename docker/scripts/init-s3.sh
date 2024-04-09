#!/bin/bash
set -e

echo "Initializing bucket..."

awslocal s3 mb s3://symmary-by-regions