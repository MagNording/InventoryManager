package se.what.inventorymanager.runner;

import Utils.InputValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.what.inventorymanager.menu.MenuHandler;

@Component
public class MyRunner implements CommandLineRunner {

    private final MenuHandler menuHandler;

    public MyRunner(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    @Override
    public void run(String... args) {
        int userchoice = 0;
        do {
            InputValidator.introText();
            System.out.println("""
            Please choose option below:
            1 - Login
            2 - Exit Program""");
            int userChoice = InputValidator.getValidIntegerInput(1, 2);

            switch (userChoice) {
                case 2 -> {
                    System.out.println("Thank you for using inventorymanager!");
                    System.exit(0);
                }
                case 1 -> menuHandler.login();
            }
        } while (userchoice != 2);
    }
}
