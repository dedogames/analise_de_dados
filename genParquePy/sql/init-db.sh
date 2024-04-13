#!/bin/bash
set -e

echo "Initializing database..."
# Start PostgreSQL service
service postgresql start

# Execute commands as postgres user
su - postgres -c "psql -c \"CREATE USER postgres WITH PASSWORD 'root';\" || true"
su - postgres -c "psql -d my_company -f /scripts/summary_by_regions.sql"
su - postgres -c "psql -c '\dt;'"

# Stop PostgreSQL service
service postgresql stop

# Start PostgreSQL service in the foreground
exec "$@"