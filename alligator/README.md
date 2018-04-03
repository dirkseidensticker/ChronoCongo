# alligator - Allen Transformer

[![java](https://img.shields.io/badge/jdk-1.8-red.svg)](#)  [![maven](https://img.shields.io/badge/maven-3.5.0-orange.svg)](#) [![output](https://img.shields.io/badge/output-jar-red.svg)](#)   [![license](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/dirkseidensticker/ChronoCongo/blob/master/alligator/LICENSE)

![logo](https://github.com/dirkseidensticker/ChronoCongo/raw/master/img/alligator-400h.png)

## Prerequisites

The code is developed using and tested with:

* maven 3.5.0
* Netbeans 8.2
* Apache Tomcat 8.0.27.0
* JDK 1.8

## Maven

The `alligator` library is build using `maven` as JAR-file.

For details have a look at [pom.xml](https://github.com/dirkseidensticker/ChronoCongo/tree/master/alligator/pom.xml).

[Download](http://maven.apache.org/download.cgi) and  [install](https://www.mkyong.com/maven/how-to-install-maven-in-windows/) `maven` and [run](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) it.

## Setup

Download and install `maven`.

Run git clone `https://github.com/dirkseidensticker/ChronoCongo.git` to create a local copy of this repository.

Run `mvn install` to install all required dependencies.

Run `mvn clean install site` for cleaning, building, testing and generating the documentation files.

Run the jar-file using `mvn exec:java`.

In order to run the Main Class in Netbeans use `Run / Debug`.

Running `mvn test` will run the unit tests with `JUnit`.

## Developers and License Holder

Florian Thiery<sup>1</sup>, RGZM<sup>2</sup>

<sup>1</sup> Florian Thiery M.Sc. [`ORCID:0000-0002-3246-3531`](http://orcid.org/0000-0002-3246-3531)

<sup>2</sup> Römisch-Germanisches Zentralmuseum Mainz, Leibniz-Forschungsinstitut für Archäologie [`rgzm.de`](http://rgzm.de/)
