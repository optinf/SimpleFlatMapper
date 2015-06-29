[![Maven Central](https://img.shields.io/maven-central/v/org.simpleflatmapper/simpleFlatMapper.svg)](https://maven-badges.herokuapp.com/maven-central/org.simpleflatmapper/simpleFlatMapper)
[![JavaDoc](https://img.shields.io/badge/javadoc-1.10.1-blue.svg)](http://www.javadoc.io/doc/org.simpleflatmapper/simpleFlatMapper)
[![License](https://img.shields.io/github/license/arnaudroger/simpleFlatMapper.svg)](https://raw.githubusercontent.com/arnaudroger/SimpleFlatMapper/master/LICENSE)

[![Java 6](https://img.shields.io/badge/java-6-orange.svg)](#java-6)[![Java 7](https://img.shields.io/badge/java-7-green.svg)](#java-7)[![Java 8](https://img.shields.io/badge/java-8-brightgreen.svg)](#java-8)

[![Build Status](https://img.shields.io/travis/arnaudroger/SimpleFlatMapper.svg)](https://travis-ci.org/arnaudroger/SimpleFlatMapper)
[![Coverage Status](https://img.shields.io/coveralls/arnaudroger/SimpleFlatMapper.svg)](https://coveralls.io/r/arnaudroger/SimpleFlatMapper)

## notable issues
* Concurrency issue [#183](https://github.com/arnaudroger/SimpleFlatMapper/issues/183) for join mapper fixed in [1.9.1](https://github.com/arnaudroger/SimpleFlatMapper/issues?q=milestone%3A1.9.1)
* To many classes generated with CsvParser Dsl with version 1.7.4 and 1.7.5 if you don't cache the DSL object. asm disabled in  [1.7.6](https://github.com/arnaudroger/SimpleFlatMapper/issues?q=milestone%3A1.7.6) and proper class cache in [1.7.7](https://github.com/arnaudroger/SimpleFlatMapper/issues?q=milestone%3A1.7.7). issues [#154](https://github.com/arnaudroger/SimpleFlatMapper/issues/154) [#156](https://github.com/arnaudroger/SimpleFlatMapper/issues/156)

# Simple Flat Mapper (SFM) [Release Notes](https://github.com/arnaudroger/SimpleFlatMapper/wiki/SimpleFlatMapper-v1.10.1)

[Javadoc](http://arnaudroger.github.io/SimpleFlatMapper/javadoc/)

Now with :
- 1-N support for [JdbcMapping](https://github.com/arnaudroger/SimpleFlatMapper/wiki/SimpleFlatMapper-JdbcMapper-1-N-relationship) and [CsvMapping](https://github.com/arnaudroger/SimpleFlatMapper/wiki/SimpleFlatMapper-CsvMapper-1-N-relationship)
- Fasttuple and jOOL tuple support.
- Java8 time api
- Excel POI
- [CsvWriter](https://github.com/arnaudroger/SimpleFlatMapper/wiki/CsvWriter)

SFM provides fast and easy to use mapper for

- [ResultSet aka micro ORM](https://github.com/arnaudroger/SimpleFlatMapper/wiki/JdbcMapper) with discriminator field and 1-n support
- [Csv](https://github.com/arnaudroger/SimpleFlatMapper/wiki/CsvParser#with-csvmapper) with 1-n support
- [Excel POI](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Excel-Mapper) with 1-n support
- [Jooq and Sql2o](https://github.com/arnaudroger/SimpleFlatMapper/wiki/SFM-Integration-in-Jooq)
- [SpringJdbcTemplate](src/main/java/org/sfm/jdbc/spring)
- [QueryDSL](src/main/java/org/sfm/querydsl)

All with 

- Constructor injection.
- Factory method injection.
- Setter and Field injection.
- Java 8 Lambdas, Optional and Time support.

It also provides one of the fastest [csv parser](https://github.com/arnaudroger/SimpleFlatMapper/wiki/CsvParser) available [See Csv Benchmark](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Csv-Performance).

The API is lambda friendly and the java 8 jars expose the mapped in form of Stream.

## Why?

SFM focuses on simplicity of use and performance. Current ORM Mapping solution are intrusive and/or quite slow -
can easily double your retrieval time in prod like setups, and Hibernate cache won't save you either because it
still needs to inflate the object from a tuple.

SFM is a library and does not force a framework on you it plugs on top of jdbc, jooq, java io, spring jdbc.

SFM makes also the assumption that it should be able to figure out the mapping rule without you being explicit about it.
Object will most of the time match the structure of a query or a csv. But it still allow you to customized the mapping
definition. [More details.](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Property-Mapping)

SFM also supports Constructor injection and respect the semantic of final fields.

[More storyfied Why.](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Why-extended-version)

## How Fast?

The Jdbc Mapper is the fastest mapper that I benchmarked.
On a local Mysql it adds a less than of 4% over pure jdbc. 
The next fastest would be Roma that add between 10 and 15% - number to be confirmed, the main reason being the column name based instead of index access of the value in the resultSet -. 
MyBatis and Hibernate adds more than 65% for 100 rows ... 115% and 95% for 1000 rows.

The Csv Parser is about 25% faster than jackson-csv for 1000 rows, 40% for the mapper  see [Csv Benchmark](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Csv-Performance).

The Jooq integration give you a mapping to object for almost no cost.

[Mysql Benchmark](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Jdbc-Performance-Local-Mysql), [In Mem HsqlDb](https://github.com/arnaudroger/SimpleFlatMapper/wiki/Jdbc-Performance-In-Memory-HsqlDb)

## Getting Started

### Binaries

The binaries are available in maven central. 
[![Maven Central](https://img.shields.io/maven-central/v/org.simpleflatmapper/simpleFlatMapper.svg)](https://maven-badges.herokuapp.com/maven-central/org.simpleflatmapper/simpleFlatMapper)
There is a build for

#### Java 8

```xml
		<dependency>
			<groupId>org.simpleflatmapper</groupId>
			<artifactId>simpleFlatMapper</artifactId>
			<version>1.10.1</version>
		</dependency>
```

#### Java 7

```xml
		<dependency>
			<groupId>org.simpleflatmapper</groupId>
			<artifactId>simpleFlatMapper</artifactId>
			<version>1.10.1</version>
			<classifier>jdk17</classifier>
		</dependency>
```

#### Java 6

```xml
		<dependency>
			<groupId>org.simpleflatmapper</groupId>
			<artifactId>simpleFlatMapper</artifactId>
			<version>1.10.1</version>
			<classifier>jdk16</classifier>
		</dependency>
```
### Asm

Asm is now a non optional dependency in the pom. but it will still work without it so if you have a conflict issue with an old version of asm you can exclude it.

### Quick Samples

The mapper are all thread safe, and it is recommended to instantiate one per class to map as most of the work is done on
instantiation.

Those samples show how to build a list from the forEach method. These is not the recommended way of processing the
stream of data. As much as you can you would need to define your own RowHandler and do the work in there.

#### Jdbc

```java
public class MyDao {
	JdbcMapper<MyObject> mapper = JdbcMapperFactory.newInstance().newMapper(MyObject.class);

	public List<MyObject> findAll() throws SQLException {
		try (Connection conn = getConnection();
		     PreparedStatement ps = conn.prepareStatement("select * from my_table");
		     ResultSet rs = ps.executeQuery();) {
			return mapper.stream(rs).collect(Collectors.toList());
		}
	}

}
```

#### Csv

```java
CsvParser
        .separator('\t')
        .mapTo(String.class, Date.class, MyObject.class)
        .stream(reader)
        .forEach(System.out::println);
```

### Building it

The build is using [Maven](http://maven.apache.org/).

```
git clone https://github.com/arnaudroger/SimpleFlatMapper.git
cd SimpleFlatMapper
mvn install
```

 
