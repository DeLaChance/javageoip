#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 -U $POSTGRES_USER -tc "SELECT 1 FROM pg_database WHERE datname = 'geoip'" | grep -q 1 || psql -U $POSTGRES_USER -c "CREATE DATABASE geoip"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "geoip" <<-EOSQL
    GRANT ALL PRIVILEGES ON DATABASE geoip TO $POSTGRES_USER;
    CREATE SCHEMA IF NOT EXISTS geoip;
EOSQL