closure-utilities
=================

A bunch of java-based utilities for google closure projects

### Automated Build

[![Build Status](https://travis-ci.org/StefanLiebenberg/closure-utilities.png?branch=master)](https://travis-ci.org/StefanLiebenberg/closure-utilities)

This project is automatically built by [travis](https://travis-ci.org/StefanLiebenberg/closure-utilities) and deployed to a maven repository on github.

### Maven

```xml
...
<dependencies>
  ...
  <dependency>
   <groupId>liebenberg</groupId>
   <artifactId>closure-utilities</artifactId>
   <version>1.0</version>
  </dependency>
  ...
</dependencies>

...

<repositories>
  ...
  <repository>
    <name>Github - Stefan Liebenberg</name>
    <id>github-StefanLiebenberg</id>
    <releases>
      <enabled>true</enabled>
    </releases>
    <url>https://raw.github.com/StefanLiebenberg/maven/releases</url>
  </repository>
  <repository>
    <id>Github - Stefan Liebenberg - Snapshots</id>
    <name>github-StefanLiebenberg-Snapshots</name>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
    <url>https://raw.github.com/StefanLiebenberg/maven/snapshots</url>
  </repository>
</repositories>
...
```