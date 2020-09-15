
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.adriens.AdditionalCommand;
import org.junit.Test;

import schemacrawler.tools.executable.CommandDescription;
import schemacrawler.tools.executable.CommandRegistry;

import java.util.Iterator;

public class TestCommandPlugin
{

  @Test
  public void testCommandPlugin() throws Exception
  {
    final CommandRegistry registry = CommandRegistry.getCommandRegistry();
    assertTrue(registry.getSupportedCommands().size() > 0);
    Iterator<CommandDescription> it = registry.getSupportedCommands().iterator();
    boolean csvCommandFound = false;
    while (it.hasNext()){
      if(it.next().getName().equals("csv")){
        csvCommandFound = true;
        break;
      }
    }

    assertTrue(csvCommandFound);
  }

}
