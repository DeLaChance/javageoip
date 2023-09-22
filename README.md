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

## Build
To build docker image for DB:
```
VERSION=`X`
sudo docker build -t geoipdb:${VERSION} .

image=$(docker image ls | egrep '^geoipdb' | awk '{ print $3 }')
docker tag $image luciendelachance/geoipdb:${VERSION}
docker push luciendelachance/geoipdb:${VERSION}
```

## Cluster
To start minikube:
`minikube start --driver=none --addons=ingress --cni=flannel --install-addons=true --kubernetes-version=v1.23`

To get access to DB:
`minikube service postgresdb-service`


## TODO:
- Dockerize app itself 
- Helm charts