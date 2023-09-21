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
