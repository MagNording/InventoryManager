package se.what.inventorymanager.service;

import Utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.what.inventorymanager.domain.Equipment;
import se.what.inventorymanager.domain.User;
import se.what.inventorymanager.enums.EquipmentState;
import se.what.inventorymanager.enums.RoleType;
import se.what.inventorymanager.repository.AssignedEquipmentRepo;
import se.what.inventorymanager.repository.EquipmentRepo;
import se.what.inventorymanager.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AssignedEquipmentRepo assignedEquipmentRepo;
    private final EquipmentRepo equipmentRepo;

    @Autowired
    public UserService(UserRepo userRepo, AssignedEquipmentRepo assignedEquipmentRepo, EquipmentRepo equipmentRepo) {
        this.userRepo = userRepo;
        this.assignedEquipmentRepo = assignedEquipmentRepo;
        this.equipmentRepo = equipmentRepo;
    }

    public void addNewUser() {
        User user = new User();
        user.setName(InputValidator.getUserDataString("Please enter the user's full name: "));
        user.setDepartment(InputValidator.getUserDataString("Please enter the user's department: "));
        user.setEmail(InputValidator.getUserDataString("Please enter the user's email: "));
        user.setTelephone(InputValidator.getUserDataString("Please enter the user's telephone number: "));
        user.setUsername(InputValidator.getUserDataString("Please choose a username for the user: "));
        user.setPassword(InputValidator.getUserDataString("Please choose a password for the user: "));
        user.setRole(selectRole());
        userRepo.save(user);
        System.out.println("You added: " + user);
    }

    private RoleType selectRole() {
        System.out.println("Please select the user role from the following numerical options: " +
                "\n1. Admin" +
                "\n2. Superuser" +
                "\n3. User");
        int editRole = InputValidator.getValidIntegerInput(1, 3);
        return switch (editRole) {
            case 1 -> RoleType.admin;
            case 2 -> RoleType.superuser;
            case 3 -> RoleType.user;
            default -> throw new IllegalArgumentException("Invalid role selection");
        };
    }

    public void editUser() {
        boolean runEditMenu = true;
        while (runEditMenu) {
            int userId = getUserIdToEdit();
            if (userId == 0) return;

            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                runEditMenu = handleEditMenu(user);
            } else {
                System.out.println("No user with the selected ID found.");
            }
        }
    }

    private int getUserIdToEdit() {
        System.out.println("Welcome to the menu for editing user, please select a user id from " +
                "the list below to begin editing:");
        System.out.println(userRepo.findAll());
        System.out.print("Please enter the id of the specific user to begin editing\nEnter '0' to exit: ");
        return InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);
    }

    private boolean handleEditMenu(User user) {
        System.out.println("""
            Please choose from the following options to edit:
            1. Edit name.
            2. Edit department.
            3. Edit email.
            4. Edit telephone number.
            5. Edit username.
            6. Edit password.
            7. Edit role.
            8. Remove user.
            9. Quit menu.
            """);
        System.out.print("Please choose an option by entering the menu number: ");
        int selectMenuOption = InputValidator.getValidIntegerInput(1, 9);

        switch (selectMenuOption) {
            case 1 -> updateUserField(user, "name");
            case 2 -> updateUserField(user, "department");
            case 3 -> updateUserField(user, "email");
            case 4 -> updateUserField(user, "telephone number");
            case 5 -> updateUserField(user, "username");
            case 6 -> updateUserField(user, "password");
            case 7 -> updateUserRole(user);
            case 8 -> removeUser();
            case 9 -> {
                return false;
            }
        }
        return true;
    }

    private void updateUserField(User user, String field) {
        System.out.println("Enter the new " + field + ": ");
        String newValue = InputValidator.getValidStringInput();
        switch (field) {
            case "name" -> user.setName(newValue);
            case "department" -> user.setDepartment(newValue);
            case "email" -> user.setEmail(newValue);
            case "telephone number" -> user.setTelephone(newValue);
            case "username" -> user.setUsername(newValue);
            case "password" -> user.setPassword(newValue);
        }
        userRepo.save(user);
    }

    private void updateUserRole(User user) {
        user.setRole(selectRole());
        userRepo.save(user);
        System.out.println("Updated user: " + user);
    }

    private void removeUser() {
        System.out.println("Please enter the id of the user you want to remove: ");
        int deleteUserById = InputValidator.getValidIntegerInput();

        if (userRepo.existsUserById(deleteUserById)) {
            Optional<User> deletedUser = userRepo.findById(deleteUserById);
            if (deletedUser.isPresent()) {
                List<Equipment> equipmentList = deletedUser.get().getEquipmentList();
                if (!equipmentList.isEmpty()) {
                    for (Equipment assignedEquipment : equipmentList) {
                        assignedEquipment.setState(EquipmentState.unassigned);
                        equipmentRepo.save(assignedEquipment);
                    }
                }
                userRepo.deleteById(deleteUserById);
                System.out.println("User has been successfully deleted");
            } else {
                System.out.println("No user found for the given ID.");
            }
        } else {
            System.out.println("No deletion occurred, user ID not found.");
        }
    }

    public void findAllUsers() {
        System.out.println(userRepo.findAll());
        System.out.println("");
    }

    public void displayEquipmentOwner() {
        System.out.println(assignedEquipmentRepo.findAll());
    }
}
