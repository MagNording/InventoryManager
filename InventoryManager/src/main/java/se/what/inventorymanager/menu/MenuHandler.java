package se.what.inventorymanager.menu;

import Utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.what.inventorymanager.domain.User;
import se.what.inventorymanager.enums.RoleType;
import se.what.inventorymanager.repository.*;
import se.what.inventorymanager.service.*;

@Component
public class MenuHandler {

    private final UserService userService;
    private final EquipmentSupportRepo equipmentSupportRepo;
    private final EquipmentService equipmentService;
    private final EquipmentSupportService equipmentSupportService;
    private final EquipmentOrderService equipmentOrderService;
    private final SearchRecordService searchRecordService;
    private final UserRepo userRepo;

    @Autowired
    public MenuHandler(UserService userService, EquipmentSupportRepo equipmentSupportRepo, EquipmentService equipmentService,
                       EquipmentSupportService equipmentSupportService, EquipmentOrderService equipmentOrderService,
                       SearchRecordService searchRecordService, UserRepo userRepo) {
        this.userService = userService;
        this.equipmentSupportRepo = equipmentSupportRepo;
        this.equipmentService = equipmentService;
        this.equipmentSupportService = equipmentSupportService;
        this.equipmentOrderService = equipmentOrderService;
        this.searchRecordService = searchRecordService;
        this.userRepo = userRepo;
    }

    public void login() {
        while (true) {
            System.out.print("Please enter username: ");
            String username = InputValidator.getValidStringInput();
            System.out.print("Please enter password: ");
            String password = InputValidator.getValidStringInput();

            User foundUser = userRepo.getUserByUsernameAndPassword(username, password);

            if (foundUser == null) {
                System.out.println("Incorrect username or password.");
            } else {
                RoleType userRole = foundUser.getRole();
                if (userRole == RoleType.admin || userRole == RoleType.superuser) {
                    menuAdmin(foundUser);
                } else {
                    menuUser(foundUser);
                }
                break;
            }
        }
    }

    public void menuAdmin(User foundUser) {
        int menuOption;
        do {
            System.out.println("""
                    Choose option below:
                    0 - Logout
                    1 - Manage Users
                    2 - Manage Equipment
                    3 - Manage Support tickets
                    4 - Manage orders
                    5 - View search records""");

            menuOption = InputValidator.getValidIntegerInput(0, 5);

            switch (menuOption) {
                case 1 -> manageUsersMenu(foundUser);
                case 2 -> manageEquipmentMenu();
                case 3 -> manageSupportTicketMenu(foundUser);
                case 4 -> equipmentOrderService.equipmentOrderMenu(foundUser);
                case 5 -> searchRecordService.searchRecordMenu();
            }
        } while (menuOption != 0);
    }

    public void menuUser(User foundUser) {
        int menuOption;
        do {
            System.out.println("""
                    Choose option below:
                    0 - Logout
                    1 - Search available equipment in stock
                    2 - Display your equipment
                    3 - Support ticket""");

            menuOption = InputValidator.getValidIntegerInput(0, 3);

            switch (menuOption) {
                case 1 -> searchRecordService.equipmentSearchQueryMenu();
                case 2 -> equipmentService.displayLoggedInUsersEquipment(foundUser);
                case 3 -> userSupportMenu(foundUser);
            }
        } while (menuOption != 0);
    }

    private void manageUsersMenu(User foundUser) {
        int menuOption;

        if (foundUser.getRole().equals(RoleType.superuser)) {
            System.out.println("Unauthorized access, only Admin can edit users...");
            return;
        }

        do {
            System.out.println("""
                    Choose option below:
                    0 - Back to Main Menu
                    1 - Display all users
                    2 - Add User
                    3 - Edit User""");

            menuOption = InputValidator.getValidIntegerInput(0, 3);

            switch (menuOption) {
                case 1 -> userService.findAllUsers();
                case 2 -> userService.addNewUser();
                case 3 -> userService.editUser();
            }
        } while (menuOption != 0);
    }

    private void manageEquipmentMenu() {
        int menuOption;

        do {
            System.out.println("""
                    Choose option below:
                    0 - Back to Main Menu
                    1 - Display equipments
                    2 - Add new Equipment to stock/user
                    3 - Edit equipment""");

            menuOption = InputValidator.getValidIntegerInput(0, 3);

            switch (menuOption) {
                case 1 -> adminDisplayEquipmentMenu();
                case 2 -> equipmentService.addNewEquipment();
                case 3 -> equipmentService.editEquipment();
            }
        } while (menuOption != 0);
    }

    private void adminDisplayEquipmentMenu() {
        int menuOption;

        do {
            System.out.println("""
                    Choose option below:
                    0 - Back to Main Menu
                    1 - Display all equipments
                    2 - Display all unassigned equipments""");

            menuOption = InputValidator.getValidIntegerInput(0, 2);

            switch (menuOption) {
                case 1 -> equipmentService.displayEquipment();
                case 2 -> equipmentService.displayUnassignedEquipment();
            }
        } while (menuOption != 0);
    }

    public void userSupportMenu(User foundUser) {
        int userChoice;
        do {
            System.out.println("""
                    Please choose option below:
                    0 - Back to main menu
                    1 - View your tickets
                    2 - Create a new support ticket
                    """);

            userChoice = InputValidator.getValidIntegerInput(0, 2);

            switch (userChoice) {
                case 1 -> equipmentSupportService.displayLoggedInUsersTickets(foundUser);
                case 2 -> handleAddTicket(foundUser);
            }

        } while (userChoice != 0);
    }

    public void manageSupportTicketMenu(User foundUser) {
        int menuOption;
        do {
            System.out.println("""
                    Choose option below:
                    0 - Back to Main Menu
                    1 - Open a new support ticket
                    2 - View support tickets
                    3 - Edit support tickets
                    4 - Delete Support-Ticket
                    """);

            menuOption = InputValidator.getValidIntegerInput(0, 4);

            switch (menuOption) {
                case 1 -> handleAddTicket(foundUser);
                case 2 -> System.out.println(equipmentSupportService.findAllSupportTickets());
                case 3 -> handleEditSupportTicket();
                case 4 -> handleDeleteSupportTicket();
            }
        } while (menuOption != 0);
    }

    private void handleAddTicket(User foundUser) {
        equipmentSupportService.displayLoggedInUsersTickets(foundUser);

        System.out.println("Enter the ID of the product you want to create a ticket on: \nEnter '0' to exit\n");
        int equipmentId = InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);

        if (equipmentId != 0) {
            System.out.println("Enter a short description of the problem:\n");
            String description = InputValidator.getValidStringInput();
            equipmentSupportService.addTicket(foundUser, equipmentId, description);
        }
    }

    private void handleEditSupportTicket() {
        System.out.println(equipmentSupportRepo.findAll());
        System.out.println("Provide the id number of your support ticket");
        int supportTicketId = InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);

        System.out.println("Change description of your ticket");
        String description = InputValidator.getValidStringInput();

        System.out.println("Change status of your ticket (open/closed)");
        String statusInput = InputValidator.getValidStringInput();

        equipmentSupportService.editSupportTicket(supportTicketId, description, statusInput);
    }

    private void handleDeleteSupportTicket() {
        System.out.println(equipmentSupportRepo.findAll());
        System.out.println("Please enter support ticket nr. you want to delete:");
        int supportTicketId = InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);
        equipmentSupportService.deleteSupportTicket(supportTicketId);
    }
}
