# geolatlong
geolatlong provides latitude, longitude to city mapping

cities mapping data taken from http://download.geonames.org/export/dump/


### Usage Example ###


```java

try {
	GeoReader reader = new GeoReader.Builder().faster().withDefaultCache(10000000).build();
	Location loc = reader.get(10.0388179, 76.449791);
	System.out.println(loc.getCity() + "/" + loc.getCountry());
}
```
