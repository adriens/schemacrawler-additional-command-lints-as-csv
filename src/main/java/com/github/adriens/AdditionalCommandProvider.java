package com.github.adriens;


import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.executable.CommandDescription;
import schemacrawler.tools.executable.CommandProvider;
import schemacrawler.tools.executable.SchemaCrawlerCommand;
import schemacrawler.tools.executable.commandline.PluginCommand;
import schemacrawler.tools.options.Config;
import schemacrawler.tools.options.OutputOptions;

import java.util.Arrays;
import java.util.Collection;

/**
 * SchemaCrawler command plug-in.
 *
 * @see <a href="https://www.schemacrawler.com">SchemaCrawler</a>
 *
 * @author Automatically generated by SchemaCrawler 16.9.4
 */
public class AdditionalCommandProvider implements CommandProvider
{

  @Override
  public Collection<CommandDescription> getSupportedCommands() {
    CommandDescription desc = new CommandDescription(AdditionalCommand.COMMAND, "An additional command for schemacrawler to dump lints as csv files.");
    return Arrays.asList(desc);
  }

  @Override
  public SchemaCrawlerCommand newSchemaCrawlerCommand(String s) throws SchemaCrawlerException {
    if (!AdditionalCommand.COMMAND.equals(s))
    {
      throw new IllegalArgumentException("Cannot support command, " + s);
    }
    final AdditionalCommand scCommand = new AdditionalCommand();
    return scCommand;
  }

  @Override
  public boolean supportsSchemaCrawlerCommand(String s, SchemaCrawlerOptions schemaCrawlerOptions, Config config, OutputOptions outputOptions) {
    return AdditionalCommand.COMMAND.equals(s);
  }

  @Override
  public boolean supportsOutputFormat(String s, OutputOptions outputOptions) {
    return true;
  }

  @Override
  public PluginCommand getCommandLineCommand() {
    return PluginCommand.empty();
  }


}
