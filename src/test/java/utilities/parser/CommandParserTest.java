package utilities.parser;

import command.Command;
import command.ExitCommand;
import command.stock.AddStockCommand;
import errors.InvalidCommandException;
import org.junit.jupiter.api.Test;
import utilities.ui.Ui;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandParserTest {
    Ui ui = new Ui();
    CommandParser commandParser = new CommandParser();

    @Test
    public void processCommand_exitCommand_expectExitObject() {
        try {
            Command command = commandParser.processCommand("exit", "", Mode.STOCK);
            assertEquals(command.getClass(), ExitCommand.class);
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void processCommand_addCommand_expectAddStockObject() {
        try {
            Command command = commandParser.processCommand("add",
                    "n/name p/10 q/20 e/10-10-2021 d/desc m/100", Mode.STOCK);
            assertEquals(command.getClass(), AddStockCommand.class);
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseCommand_oneSeparator_expectTwoParts() {
        String inputString = "listorder i/1";
        String[] stringParts = commandParser.parseCommand(inputString);
        assertEquals(2, stringParts.length);
    }


    @Test
    public void parseParameters_twoParameters_expectTwoParts() {
        String inputString = "i/1 n/name";
        LinkedHashMap<String, String> parametersValues = commandParser.parseParameters(inputString);
        assertEquals(2, parametersValues.keySet().size());
    }

    @Test
    public void parseParameters_threeParameters_expectThreeParts() {
        String inputString = "i/1 n/name p/20";
        LinkedHashMap<String, String> parametersValues = commandParser.parseParameters(inputString);
        assertEquals(3, parametersValues.keySet().size());
    }

    @Test
    public void changeMode_modeStock_expectModePrescription() {
        Mode mode = commandParser.changeMode(ui, "prescription", Mode.STOCK);
        assertEquals(mode, Mode.PRESCRIPTION);
    }
}
