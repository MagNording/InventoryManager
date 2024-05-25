package Utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

public class InputValidator {

    private static final Scanner input = new Scanner(System.in);

    public static void introText() {
        System.out.println("Welcome to");
        System.out.println("""
                 _                      _                                                           \s
                (_)_ ____   _____ _ __ | |_ ___  _ __ _   _  /\\/\\   __ _ _ __   __ _  __ _  ___ _ __\s
                | | '_ \\ \\ / / _ \\ '_ \\| __/ _ \\| '__| | | |/    \\ / _` | '_ \\ / _` |/ _` |/ _ \\ '__|
                | | | \\ V /  __/ | | | || (_) | |  | |_| / /\\/\\ \\ (_| | | | | (_| | (_| |  __/ |  \s
                |_|_| |_|\\_/ \\___|_| |_|\\__\\___/|_|   \\__, \\/    \\/\\__,_|_| |_|\\__,_|\\__, |\\___|_|  \s
                                                      |___/                          |___/          \s
                """);
    }

    public static int getValidIntegerInput(int minValue, int maxValue) {
        int userInput;
        while (true) {
            try {
                userInput = Integer.parseInt(input.nextLine());
                if (userInput < minValue || userInput > maxValue) {
                    System.out.println("Invalid entry, please enter a number between " + minValue + " and " + maxValue + "...");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry, please enter a number between " + minValue + " and " + maxValue + "...");
            }
        }
        return userInput;
    }

    public static int getValidIntegerInput() {
        int userInput;
        while (true) {
            try {
                userInput = Integer.parseInt(input.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry, please enter a valid integer.");
            }
        }
        return userInput;
    }

    public static double getValidDoubleInput() {
        double userInput;
        while (true) {
            try {
                userInput = Double.parseDouble(input.nextLine().replace(',', '.'));
                if (userInput < 0) {
                    System.out.println("Incorrect value, please enter a value larger than 0...");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Incorrect value, please enter a valid price: ");
            }
        }
        return userInput;
    }

    public static Date getValidDate(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        try {
            return sdf.parse(inputDate);
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid date.");
            return null;
        }
    }

    public static Date getValidDeliveryDate() {
        Date date;
        while (true) {
            String dateStr = input.nextLine();
            date = getValidDate(dateStr);
            if (date != null) {
                break;
            }
        }
        return date;
    }

    public static String getValidStringInput() {
        String userInput;
        while (true) {
            userInput = input.nextLine();
            if (userInput.matches("[-a-zA-ZåäöÅÄÖ0-9@._ ]+")) {
                if (!userInput.isEmpty()) {
                    break;
                } else {
                    System.out.println("Entry cannot be blank.");
                }
            } else {
                System.out.println("Incorrect format, you cannot use special characters!");
            }
        }
        return userInput;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String getUserDataString(String prompt) {
        System.out.print(prompt);
        return getValidStringInput();
    }

    public static Double getUserDataDouble(String prompt) {
        System.out.print(prompt);
        return getValidDoubleInput();
    }

    public static int getUserDataInteger(String prompt) {
        System.out.print(prompt);
        return getValidIntegerInput(0, Integer.MAX_VALUE);
    }

    public static int getUserDataInteger(String prompt, int minValue, int maxValue) {
        System.out.print(prompt);
        return getValidIntegerInput(0, Integer.MAX_VALUE);
    }
}
