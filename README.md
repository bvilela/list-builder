# List Builder

### Quality Status
[![ci](https://github.com/bvilela/list-builder/actions/workflows/2.ci.yml/badge.svg)](https://github.com/bvilela/list-builder/actions/workflows/3.ci.yml)
[![cd](https://github.com/bvilela/list-builder/actions/workflows/3.cd.yml/badge.svg)](https://github.com/bvilela/list-builder/actions/workflows/4.cd.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bvilela_list-builder&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bvilela_list-builder)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=bvilela_list-builder&metric=coverage)](https://sonarcloud.io/summary/new_code?id=bvilela_list-builder)

### Repository Statistics
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=bvilela_list-builder&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=bvilela_list-builder)
<!-- ![GitHub repo size](https://img.shields.io/github/repo-size/bvilela/list-builder)  -->
<!-- ![GitHub language count](https://img.shields.io/github/languages/count/bvilela/list-builder)  -->
<!-- ![GitHub open issues](https://img.shields.io/github/issues-raw/bvilela/list-builder)  -->
<!-- ![GitHub open pull requests](https://img.shields.io/github/issues-pr/bvilela/list-builder) -->

## Summary
Spring Boot Application to Builder Lists.

Application read data from JSON file, process and generate a PDF file.

### Technologies
* Maven
* Java 17
* Spring Boot 2.6.5
* [Lombok](https://projectlombok.org/)
* [itextpdf 5.5.13.3](https://itextpdf.com/en)
* Gson
* [jsoup](https://jsoup.org/) 1.15.1 (Java HTML Parser)
* [Java Util Validation Lib](https://github.com/bvilela/java-util-validation-lib) (Personal library with useful validations)
* [Google Calendar Util Lib](https://github.com/bvilela/google-calendar-util-lib) (Personal library with useful services for Google Calendar API)
* Static Code Analysis: SonarCloud
* [fmt-maven-plugin](https://github.com/spotify/fmt-maven-plugin)

### GitHub Action
* Build and Test Java with Maven (branch)
* Analyze SonarCloud (branch)
* Publish on GitHub Packages (tags)


## :heavy_check_mark: Check PMD rules locally
To check PMD rules in your machine, run follow command in `app` dir:
```
mvn pmd:check
```
