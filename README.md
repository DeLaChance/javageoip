# javageoip
A Java app intended to answer simple geo ip queries mapping an IPv4 address to a country (code & fullname):

## API
GET [http://localhost:8080/api/geoip/8.8.8.8/country](http://localhost:8080/api/geoip/8.8.8.8/country)

```
{
  "query": {
    "ipAddress": "8.8.8.8",
    "ipAddressNumeric": 134744072
  },
  "country": {
    "code": "US",
    "fullName": "United States of America"
  }
}
```

## Commands

## Build Docker 
To build docker image for DB:
```
VERSION=`1.0.1`
cd javageoip/

image=$(docker image ls | egrep '^javageoip' | head -n 1 | | awk '{ print $3 }')
docker tag $image luciendelachance/javageoip:${VERSION}
sudo docker build -t javageoip:${VERSION} .

cd ../

cd docker/geoipdb/
sudo docker build -t geoipdb:${VERSION} .

image=$(docker image ls | egrep '^geoipdb' | head -n 1 | | awk '{ print $3 }')
docker tag $image luciendelachance/geoipdb:${VERSION}
docker push luciendelachance/geoipdb:${VERSION}
```

## Cluster
For testing on a local cluster, I installed minikube on an Ubuntu 22.04 (WSL Windows).

To start minikube:
`minikube start --driver=none --addons=ingress --cni=flannel --install-addons=true --kubernetes-version=v1.23`

To get access to DB:
`minikube service postgresdb-service`


## TODO:
- Helm charts
- Schemahero