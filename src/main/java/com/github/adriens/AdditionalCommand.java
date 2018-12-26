package com.github.adriens;


import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import schemacrawler.schema.Column;
import schemacrawler.schema.Property;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.executable.BaseSchemaCrawlerCommand;
import schemacrawler.tools.lint.Lint;
import schemacrawler.tools.lint.LintUtility;
import schemacrawler.tools.lint.LintedCatalog;
import schemacrawler.tools.lint.LinterConfigs;
import schemacrawler.tools.lint.Linters;
import schemacrawler.tools.lint.executable.LintOptions;
import schemacrawler.tools.lint.executable.LintOptionsBuilder;

/**
 * SchemaCrawler command plug-in.
 *
 * @see <a href="https://www.schemacrawler.com">SchemaCrawler</a>
 * @author Automatically generated by SchemaCrawler 15.01.02
 */
public class AdditionalCommand
  extends BaseSchemaCrawlerCommand
{

  private static final Logger LOGGER = Logger
    .getLogger(AdditionalCommand.class.getName());

  static final String COMMAND = "csv";
  // public static final String DEFAULT_LINTS_FILENAME = "lints.csv";
  private String lintsFilename;
  private static final String DEFAULT_LINTS_FILENAME_PREFIX = "schemacrawler-lints";
  private static final String DEFAULT_LINTS_FILENAME_PREFIX_ROWCOUNT = "schemacrawler-tables";
  private static final String DEFAULT_LINTS_FILENAME_PREFIX_COLUMNS = "schemacrawler-columns";
  private static final String NEW_LINE_SEPARATOR = "\n";
  private static final Object[] FILE_HEADER = {
                                                "sclint-dbenv,sclint-dbid,sclint-runid",
                                                "sclint-hitid",
                                                "sclint-linterId",
                                                "sclint-severity",
                                                "sclint-objectName",
                                                "sclint-message",
                                                "sclint-value" };
  private static final Object[] FILE_HEADER_TABLE_ROW_COUNT = {
                                                                "sclint-dbenv",
                                                                "sclint-dbid",
                                                                "sclint-runid",
                                                                "sclint-hitid",
                                                                "sclint-tableFullName",
                                                                "sclint-tableName",
                                                                "sclint-schemaName",
                                                                "sclint-tableType",
                                                                "sclint-tableNbColumns",
                                                                "sclint-tableNbRows" };
  private static final Object[] FILE_HEADER_TABLE_COLUMNS = {
                                                              "sclint-dbenv",
                                                              "sclint-dbid",
                                                              "sclint-runid",
                                                              "sclint-schema-name",
                                                              "sclint-table-name",
                                                              "sclint-table-fullname",
                                                              "sclint-table-remarks",
                                                              "sclint-table-type",
                                                              "sclint-column-short-name",
                                                              "sclint-column-name",
                                                              "sclint-column-fullname",
                                                              "sclint-column-ordinal-position",
                                                              "sclint-column-remarks",
                                                              "sclint-column-default-value",
                                                              "sclint-column-size",
                                                              "sclint-column-type-typename",
                                                              "sclint-column-type-fullname",
                                                              "sclint-column-type-name",
                                                              "sclint-column-type-java-sqltype-group" };

  protected AdditionalCommand()
  {
    super(COMMAND);
  }

  @Override
  public void checkAvailibility()
    throws Exception
  {
    // Nothing additional to check at this point. The Command should be
    // available
    // after the class is loaded, and imports are resolved.
  }

  @Override
  public void execute()
    throws Exception
  {
    // TODO: Possibly process command-line options, which are available
    // in additionalConfiguration
    
    // Adjust SchemaInfoLevel to retrieve server info
    SchemaInfoLevelBuilder schemaInfoLevelBuilder = SchemaInfoLevelBuilder
      .standard().fromOptions(schemaCrawlerOptions.getSchemaInfoLevel());
    schemaInfoLevelBuilder.setRetrieveServerInfo(true);
    SchemaCrawlerOptionsBuilder schemaCrawlerOptionsBuilder = SchemaCrawlerOptionsBuilder.builder()
      .fromOptions(schemaCrawlerOptions);
    schemaCrawlerOptionsBuilder.withSchemaInfoLevel(schemaInfoLevelBuilder.toOptions());
    this.setSchemaCrawlerOptions(schemaCrawlerOptionsBuilder.toOptions());
    
    // Options
    final LintOptions lintOptions = LintOptionsBuilder.builder()
      .fromConfig(additionalConfiguration).toOptions();
    // setLintsFilename(additionalConfiguration.getStringValue("outputfile",
    // DEFAULT_LINTS_FILENAME));

    // get and set dbid
    String dbId = additionalConfiguration.getStringValue("dbid", "");
    LOGGER.log(Level.INFO, String.format("Got input dbId : <%s>", dbId));

    // get and set dbenv
    // dbenv
    String dbEnv = additionalConfiguration.getStringValue("dbenv", "")
      .toLowerCase();
    LOGGER.log(Level.INFO, String.format("Got input dbEnv : <%s>", dbEnv));

    String dbCatalogName = getDbCatalogName();
    LOGGER.log(Level.INFO, String.format("DATABASE NAME : <%s>", dbCatalogName));


    CSVPrinter csvFilePrinter;
    FileWriter fileWriter;
    CSVFormat csvFileFormat = CSVFormat.DEFAULT
      .withRecordSeparator(NEW_LINE_SEPARATOR);
    String runId = catalog.getCrawlInfo().getRunId();

    setLintsFilename(String
      .format("%s-%s.csv", DEFAULT_LINTS_FILENAME_PREFIX, runId));
    LOGGER.log(Level.INFO,
               String.format("Generating lints for run <%s> ...", runId));
    final LinterConfigs linterConfigs = LintUtility
      .readLinterConfigs(lintOptions, getAdditionalConfiguration());

    final Linters linters = new Linters(linterConfigs);
    final LintedCatalog lintedCatalog = new LintedCatalog(catalog,
                                                          connection,
                                                          linters);
    Iterator<Lint<?>> lintIter = lintedCatalog.getCollector().iterator();
    // feed the csv
    Lint aLint;
    fileWriter = new FileWriter(getLintsFilename());

    // initialize CSVPrinter object
    csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

    // Create CSV file header
    csvFilePrinter.printRecord(FILE_HEADER);

    while (lintIter.hasNext())
    {
      aLint = lintIter.next();

      List lintDataRecord = new ArrayList();

      lintDataRecord.add(dbEnv);
      lintDataRecord.add(dbId);
      lintDataRecord.add(runId);
      lintDataRecord.add(aLint.getLintId().toString());

      lintDataRecord.add(aLint.getLinterId());
      lintDataRecord.add(aLint.getSeverity().toString().toUpperCase());
      lintDataRecord.add(aLint.getObjectName());
      lintDataRecord.add(aLint.getMessage());
      lintDataRecord.add(aLint.getValueAsString());

      csvFilePrinter.printRecord(lintDataRecord);
    }
    fileWriter.flush();
    fileWriter.close();
    LOGGER.log(Level.INFO,
               String.format("Lint runid : <%s> generated.", runId));

    try
    {
      generateTableRowCountCsv(dbEnv, dbId, runId);
    }
    catch (Exception ex)
    {
      LOGGER.log(Level.SEVERE,
                 "Was not able to perform table row count operation : "
                               + ex.getMessage());
      throw ex;
    }

    try
    {
      generateTableColumnsCsv(dbEnv, dbId, runId);
    }
    catch (Exception ex)
    {
      LOGGER.log(Level.SEVERE,
                 "Was not able to perform table columns reporting : "
                               + ex.getMessage());
      throw ex;
    }
  }

  private String getDbCatalogName()
  {
    String dbCatalogName = "";
    Map<String, String> serverInfoMap = new HashMap<>();
    for (Property serverInfoProperty: catalog.getDatabaseInfo().getServerInfo())
    {
      serverInfoMap.put(serverInfoProperty.getName(),
                        serverInfoProperty.getValue().toString());
      System.err.println("Key/Value : <" + serverInfoProperty.getName() + "/" + serverInfoProperty.getValue().toString() + ">");
      
    }
    System.err.println("Map size : <" + serverInfoMap.size() + ">");
    LOGGER.info("MAP : " + serverInfoMap.toString());
    
    if (serverInfoMap.containsKey("SERVICE_NAME"))
    {
      // Oracle SERVICE_NAME
      dbCatalogName = serverInfoMap.get("SERVICE_NAME");
    }
    else if (serverInfoMap.containsKey("current_database"))
    {
      // PostgreSQL SERVICE_NAME
      dbCatalogName = serverInfoMap.get("current_database");
    }
    else if (serverInfoMap.containsKey("ServerName"))
    {
      // Microsoft SQL Server ServerName
      dbCatalogName = serverInfoMap.get("ServerName");
    }
    else if (serverInfoMap.containsKey("CATALOG_NAME"))
    {
      // HyperSQL (HSQLDB) CATALOG_NAME
      dbCatalogName = serverInfoMap.get("CATALOG_NAME");
    }     

    return dbCatalogName;
  }

  public String getLintsFilename()
  {
    return this.lintsFilename;
  }

  public void setLintsFilename(String aLintsFilename)
  {
    this.lintsFilename = aLintsFilename;
  }

  public void generateTableRowCountCsv(String aDbEnv,
                                       String aDbId,
                                       String aRunId)
    throws Exception
  {
    LOGGER.log(Level.INFO, "Counting rows for each table ...");
    String rowCountFilename = String
      .format("%s-%s-.csv", DEFAULT_LINTS_FILENAME_PREFIX_ROWCOUNT, aRunId);
    LOGGER.log(Level.INFO,
               "Putting table row count datas in <" + rowCountFilename + ">");

    CSVPrinter csvFilePrinter;
    FileWriter fileWriter;
    CSVFormat csvFileFormat = CSVFormat.DEFAULT
      .withRecordSeparator(NEW_LINE_SEPARATOR);

    fileWriter = new FileWriter(rowCountFilename);

    // initialize CSVPrinter object
    csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
    // Create CSV file header
    csvFilePrinter.printRecord(FILE_HEADER_TABLE_ROW_COUNT);
    for (final Schema schema: catalog.getSchemas())
    {
      for (final Table table: catalog.getTables(schema))
      {
        // for each table, count the number of rows
        String sql = "select count(1) from " + table.getFullName();
        Statement stmt = null;
        try
        {
          stmt = connection.createStatement();
          ResultSet rs = stmt.executeQuery(sql);
          while (rs.next())
          {
            int nbRows = rs.getInt(1);
            List lintDataRecord = new ArrayList();
            // runtime datas

            lintDataRecord.add(aDbEnv);
            lintDataRecord.add(aDbId);
            lintDataRecord.add(aRunId);
            lintDataRecord.add(UUID.randomUUID().toString());

            // table centric datas
            LOGGER.log(Level.INFO,
                       "Found <" + nbRows + "> rows in <" + table.getFullName()
                                   + ">");
            String tableFullName = table.getFullName();
            String tableName = table.getName();
            String tableRemarks = table.getRemarks();
            String tableSchemaName = table.getSchema().getName();
            String tableType = table.getTableType().getTableType();
            int tableNbColumns = table.getColumns().size();

            lintDataRecord.add(tableFullName);
            lintDataRecord.add(tableName);
            lintDataRecord.add(tableSchemaName);
            lintDataRecord.add(tableType);
            lintDataRecord.add(tableNbColumns);
            lintDataRecord.add(nbRows + "");

            csvFilePrinter.printRecord(lintDataRecord);

          }
        }
        catch (SQLException ex)
        {
          LOGGER.log(Level.SEVERE,
                     "Not able to count rows : " + ex.getMessage());
        }
        finally
        {
          if (stmt != null)
          {
            stmt.close();
          }
        }
      }
    }
    fileWriter.flush();
    fileWriter.close();
  }

  public void generateTableColumnsCsv(String aDbEnv,
                                      String aDbId,
                                      String aRunId)
    throws Exception
  {
    LOGGER.log(Level.INFO, "Dumping each column of each table ...");
    String rowCountFilename = String
      .format("%s-%s-.csv", DEFAULT_LINTS_FILENAME_PREFIX_COLUMNS, aRunId);
    LOGGER.log(Level.INFO,
               "Putting table row count datas in <" + rowCountFilename + ">");

    CSVPrinter csvFilePrinter;
    FileWriter fileWriter;
    CSVFormat csvFileFormat = CSVFormat.DEFAULT
      .withRecordSeparator(NEW_LINE_SEPARATOR);

    fileWriter = new FileWriter(rowCountFilename);

    // initialize CSVPrinter object
    csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
    // Create CSV file header
    csvFilePrinter.printRecord(FILE_HEADER_TABLE_COLUMNS);
    for (final Schema schema: catalog.getSchemas())
    {
      for (final Table table: catalog.getTables(schema))
      {
        // for each table, count the number of rows
        String lSchema = table.getSchema().getName();
        String lTableName = table.getName();
        String lTableFullName = table.getFullName();
        String lTableRemarks = table.getRemarks();
        String lTableType = table.getTableType().getTableType();

        // fetch columns
        for (final Column column: table.getColumns())
        {
          // names
          column.getShortName();
          column.getName();
          column.getFullName();
          column.getOrdinalPosition();
          column.getRemarks();

          // type centric
          column.getDefaultValue();
          column.getSize();

          // type centric datas
          // column.getColumnDataType().getBaseType().getDatabaseSpecificTypeName();
          // column.getColumnDataType().getBaseType().getFullName().toString();
          // column.getColumnDataType().getBaseType().getJavaSqlType().getName();
          // column.getColumnDataType().getBaseType().getJavaSqlType().getJavaSqlTypeGroup().toString();
          List lintDataRecord = new ArrayList();
          // runtime datas
          lintDataRecord.add(aDbEnv);
          lintDataRecord.add(aDbId);
          lintDataRecord.add(aRunId);

          lintDataRecord.add(table.getSchema().getName());
          lintDataRecord.add(table.getName());
          lintDataRecord.add(table.getFullName());
          lintDataRecord.add(table.getRemarks());
          lintDataRecord.add(table.getTableType().toString());
          lintDataRecord.add(column.getShortName());
          lintDataRecord.add(column.getName());
          lintDataRecord.add(column.getFullName());
          lintDataRecord.add(column.getOrdinalPosition());
          lintDataRecord.add(column.getRemarks());
          lintDataRecord.add(column.getDefaultValue());
          lintDataRecord.add(column.getSize());
          lintDataRecord.add(column.getType().getName());
          lintDataRecord.add(column.getType().getFullName());
          lintDataRecord.add(column.getType().getName());
          lintDataRecord.add(column.getType().getJavaSqlType().getName());
          lintDataRecord.add(column.getType().getJavaSqlType()
            .getJavaSqlTypeGroup().toString());

          csvFilePrinter.printRecord(lintDataRecord);

        }

        fileWriter.flush();
      }
    }
    fileWriter.flush();
    fileWriter.close();
  }
}
