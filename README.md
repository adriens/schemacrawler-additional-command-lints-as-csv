[![Build Status](https://travis-ci.org/adriens/schemacrawler-additional-command-lints-as-csv.svg?branch=master)](https://travis-ci.org/adriens/schemacrawler-additional-command-lints-as-csv)
[![](https://jitpack.io/v/adriens/schemacrawler-additional-command-lints-as-csv.svg)](https://jitpack.io/#adriens/schemacrawler-additional-command-lints-as-csv)

# schemacrawler-additional-command-lints-as-csv

An additional [command](http://www.schemacrawler.com/plugins.html) for
[schemacrawler](http://www.schemacrawler.com/) to dump lints as csv files,
with some additional fields.

See [linkedIn article](https://www.linkedin.com/pulse/continuous-database-linting-dashboards-bring-analytics-adrien-sales/) for more
 details or check the [Pinterest album for more samples](https://www.pinterest.com/rastadidi/schemacrawler-lint-dashboards/)

# Install steps

Build the jar :

```
mvn clean package
```

then copy the jar to `$SCHEMACRAWLER_HOME/lib` and you're done with install steps.

This `jar` adds the following command with the following options :

```
-c=csv -dbid=666 -dbenv=hell
```

- `-c=csv` : tells that we want to dump the lints as a csv file
- `-dbid`  : optional paramater if you want to stick on a given database
- `-dbenv` : optional paramater if you want to tag a database to an environment (typically prod, dev, test, ...)

For each run, you then get two `csv` files in your working directory :

- `schemacrawler-lints-<UUID>.csv` : this file contains lint outputs
- `schemacrawler-tables-<UUID>.csv` : this file contains datas reporting number of rows and columns of tables, with schema, tableName, ...

To load these two files, you need dedicated [logstash](https://www.elastic.co/products/logstash) configuration files.
Therefore, you have two logstash configuration files samples :

- for lints, check `logstash-lints.conf`
- for table datas, check `logstash-tables.conf`

**For each, you have to customize index names and `input.file.path` according to your needs.**

# Contribute

You can contribute code, but also your own dashoard realizations. Therefore, just make a PR that :

- add an image to the `img` directory
- add the screenshot to the dedicated `SCREENSHOTS.md` file or a link to a video : any cool demo is welcome
- also you can ask (fill and issue on Github for that) to contribute to the dedicated [Pinterest album](https://www.pinterest.com/rastadidi/schemacrawler-lint-dashboards/)

# Contribute ideas

If you have ideas for dashboard but don't know how to create them, but still are
convinced that the are interesting, please fill an issue on the project,
explaining what you'd like to produce. A hand made drawing can also be a
very good beginning !

# Details and samples

See [linkedIn article](https://www.linkedin.com/pulse/continuous-database-linting-dashboards-bring-analytics-adrien-sales/) for more details or visit the dedicated [Pinterest album](https://www.pinterest.com/rastadidi/schemacrawler-lint-dashboards/).

![Simple Kibana dashboard screenshot](img/COVER.png "Screenshot")
