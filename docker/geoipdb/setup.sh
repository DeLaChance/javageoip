#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 -U $POSTGRES_USER -tc "SELECT 1 FROM pg_database WHERE datname = 'geoip'" | grep -q 1 || psql -U $POSTGRES_USER -c "CREATE DATABASE geoip"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "geoip" <<-EOSQL
    GRANT ALL PRIVILEGES ON DATABASE geoip TO $POSTGRES_USER;
    CREATE SCHEMA IF NOT EXISTS geoip;

    create table if not exists geoip.countries (
        code char(3) not null primary key,
        full_name varchar(100) not null
    );

    create table if not exists geoip.geo_ip_ranges (
        id serial primary key,
        beginIp int not null,
        endIp int not null,
        countryCode char(3) not null references geoip.countries(code)
    );

    INSERT INTO geoip.countries (code, full_name)
    VALUES
        ('US ', 'United States of America'),
        ('NL ', 'Netherlands')
	ON CONFLICT (code) DO NOTHING;

    INSERT INTO geoip.geo_ip_ranges (id, beginip, endip, countrycode) 
    VALUES
        (0, 117901063, 134744072, 'US '),
        (1, 0, 16843009, 'NL ')
    ON CONFLICT (id) DO NOTHING;
EOSQL