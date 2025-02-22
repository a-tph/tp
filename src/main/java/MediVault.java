import command.Command;
import command.CommandList;
import errors.InvalidCommandException;
import inventory.Prescription;
import inventory.Medicine;
import inventory.Order;
import inventory.Stock;
import utilities.parser.CommandParser;
import utilities.parser.DateParser;
import utilities.parser.Mode;
import utilities.storage.Storage;
import utilities.ui.Ui;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static utilities.parser.Mode.PRESCRIPTION;
import static utilities.parser.Mode.ORDER;
import static utilities.parser.Mode.STOCK;

//@@author alvintan01
/**
 * Helps to start the application, and initialise all variables.
 * It will continuously prompt for input from the user until "EXIT" is received.
 */

public class MediVault {
    private static Logger logger = Logger.getLogger("MediVault");
    private Mode mode = Mode.STOCK;

    public MediVault() {
        // For testing, uncomment generateData() && comment the 3 lines
        // For storage, comment generateData() && uncomment the 3 lines
        //generateData();
        ArrayList<Medicine> medicines = Medicine.getInstance();
        Storage storage = Storage.getInstance();
        medicines.addAll(storage.loadData());
        logger.log(Level.INFO, "All variables are initialised.");
    }

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        logger.log(Level.INFO, "Medivault is starting up");
        new MediVault().run();
    }

    /**
     * Prompts input from user and processes it indefinitely until "EXIT" is received.
     */
    private void run() {
        Ui ui = Ui.getInstance();
        ui.printWelcomeMessage();
        CommandParser commandParser = new CommandParser();

        String userInput = "";

        // Loops till exit is received
        while (true) {
            System.out.print("[" + mode + "] > ");
            // Reads user input
            userInput = ui.getInput();
            try {
                String[] userCommand = commandParser.parseCommand(userInput);
                String commandString = userCommand[0];
                String commandParameters = userCommand[1];

                // Check is user is changing modes
                if (commandString.equalsIgnoreCase(STOCK.name()) || commandString.equalsIgnoreCase(PRESCRIPTION.name())
                        || commandString.equalsIgnoreCase(ORDER.name())) {
                    mode = commandParser.changeMode(ui, commandString, mode);
                    continue;
                }

                Command command = commandParser.processCommand(commandString, commandParameters, mode);
                command.execute();

                if (commandString.equals(CommandList.EXIT)) { // User entered exit
                    break;
                }
            } catch (InvalidCommandException e) {
                // Invalid Command
                ui.printInvalidCommandMessage();
                logger.log(Level.WARNING, "An invalid command was entered!");
            }
        }
        logger.log(Level.INFO, "MediVault is shutting down");
    }

    /**
     * Temporary function to generate data to test features like delete and update. To be removed once add function is
     * complete.
     */
    public void generateData() {
        try {
            ArrayList<Medicine> medicines = Medicine.getInstance();
            medicines.add(new Stock("PANADOL", 20, 20, DateParser.stringToDate("13-9-2021"),
                    "BEST MEDICINE TO CURE HEADACHES, FEVER AND PAINS", 1000));
            medicines.add(new Stock("PANADOL", 20, 10, DateParser.stringToDate("14-9-2021"),
                    "BEST MEDICINE TO CURE HEADACHES, FEVER AND PAINS", 1000));
            medicines.add(new Stock("VICODIN", 10, 20, DateParser.stringToDate("30-9-2021"),
                    "POPULAR DRUG FOR TREATING ACUTE OR CHRONIC MODERATE TO MODERATELY SEVERE PAIN",
                    500));
            medicines.add(new Stock("SIMVASTATIN", 20, 25, DateParser.stringToDate("10-10-2021"),
                    "TREATS HIGH CHOLESTEROL AND REDUCES THE RISK OF STROKE", 800));
            medicines.add(new Stock("LISINOPRIL", 20, 25, DateParser.stringToDate("15-10-2021"),
                    "USED FOR TREATING HYPOTHYROIDISM", 800));
            medicines.add(new Stock("AZITHROMYCIN", 20, 35, DateParser.stringToDate("15-10-2021"),
                    "USED FOR TREATING EAR, THROAT, AND SINUS INFECTIONS", 100));
            medicines.add(new Order("PANADOL", 100, DateParser.stringToDate("9-10-2021")));
            medicines.add(new Order("VICODIN", 30, DateParser.stringToDate("9-10-2021")));
            Order order = new Order("VICODIN", 50, DateParser.stringToDate("10-10-2021"));
            order.setDelivered();
            medicines.add(order);
            medicines.add(new Order("SIMVASTATIN", 20, DateParser.stringToDate("11-10-2021")));
            medicines.add(new Order("LISINOPRIL", 200, DateParser.stringToDate("12-10-2021")));
            medicines.add(new Order("AZITHROMYCIN", 100, DateParser.stringToDate("13-10-2021")));
            medicines.add(new Order("PANADOL", 50, DateParser.stringToDate("13-10-2021")));
            medicines.add(new Prescription("PANADOL", 10, "S1234567A",
                    DateParser.stringToDate("9-10-2021"), "Jane", 1));
            medicines.add(new Prescription("VICODIN", 10, "S2345678B",
                    DateParser.stringToDate("10-10-2021"), "Peter", 3));
            medicines.add(new Prescription("SIMVASTATIN", 10, "S1234567A",
                    DateParser.stringToDate("11-10-2021"), "Sam", 4));
            medicines.add(new Prescription("LISINOPRIL", 10, "S3456789C",
                    DateParser.stringToDate("12-10-2021"), "Jane", 5));
            medicines.add(new Prescription("AZITHROMYCIN", 10, "S2345678B",
                    DateParser.stringToDate("13-10-2021"), "Peter", 6));
        } catch (ParseException e) {
            Ui ui = Ui.getInstance();
            ui.print("Unable to parse date!");
        }
    }

}
