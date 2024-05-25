package se.what.inventorymanager.service;

import Utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.what.inventorymanager.domain.Equipment;
import se.what.inventorymanager.domain.User;
import se.what.inventorymanager.enums.EquipmentState;
import se.what.inventorymanager.enums.EquipmentType;
import se.what.inventorymanager.repository.EquipmentRepo;
import se.what.inventorymanager.repository.UnassignedEquipmentRepo;
import se.what.inventorymanager.repository.UserRepo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepo equipmentRepo;
    private final UserRepo userRepo;
    private final UnassignedEquipmentRepo unassignedEquipmentRepo;

    @Autowired
    public EquipmentService(EquipmentRepo equipmentRepo, UserRepo userRepo,
                            UnassignedEquipmentRepo unassignedEquipmentRepo) {
        this.equipmentRepo = equipmentRepo;
        this.userRepo = userRepo;
        this.unassignedEquipmentRepo = unassignedEquipmentRepo;
    }

    public void addNewEquipment() {
        Equipment equipment = new Equipment();
        equipment.setName(InputValidator.getUserDataString("Please enter name of the equipment:" +
                " "));

        Date purchaseDate = InputValidator.asDate(LocalDate.now());
        equipment.setPurchaseDate(purchaseDate);

        double purchasePrice = getPurchasePrice();
        if (purchasePrice < Integer.MAX_VALUE) {
            equipment.setPurchasePrice(purchasePrice);
        } else {
            System.out.println("\nInvalid price! Please enter a price below: " + Integer.MAX_VALUE);
            return;
        }

        EquipmentType type = getEquipmentType();
        equipment.setType(type);

        int assignToUserOrStock = InputValidator.getValidIntegerInput(1, 2);
        if (assignToUserOrStock == 1) {
            assignToUser(equipment);
        } else {
            equipment.setState(EquipmentState.unassigned);
            equipmentRepo.save(equipment);
            System.out.println(equipment + ". Added to stock");
        }
    }

    private double getPurchasePrice() {
        System.out.print("Please enter price of the equipment: ");
        return InputValidator.getValidDoubleInput();
    }

    private EquipmentType getEquipmentType() {
        System.out.println("Please enter equipment type (\n1 - laptop\n2 - phone\n3 - screen):");
        int inputType = InputValidator.getValidIntegerInput(1, 3);

        return switch (inputType) {
            case 1 -> EquipmentType.laptop;
            case 2 -> EquipmentType.phone;
            case 3 -> EquipmentType.screen;
            default -> throw new IllegalArgumentException("Invalid equipment type");
        };
    }

    private void assignToUser(Equipment equipment) {
        System.out.println(userRepo.findAll());
        System.out.print("Please enter the ID of the user you want to assign equipment to: ");
        int userIdChoice = InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);

        Optional<User> userOptional = userRepo.findById(userIdChoice);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            equipment.setUser(user);
            equipment.setState(EquipmentState.assigned);
            equipmentRepo.save(equipment);
            System.out.println(equipment + " added");
        } else {
            System.out.println("No user found with the given ID.");
        }
    }

    public boolean displayEquipment() {
        System.out.println(equipmentRepo.findAll());
        return false;
    }

    public void editEquipment() {
        boolean runEditMenu = true;
        while (runEditMenu) {
            System.out.println("Menu for editing equipment, please select an equipment id to begin editing: ");
            displayEssentialOfEquipment();

            System.out.println("Please enter the id of the equipment to begin editing:\nEnter '0' to abort");
            int selectEquipmentById = InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);

            Optional<Equipment> equipmentOptional = equipmentRepo.findById(selectEquipmentById);
            if (equipmentOptional.isPresent()) {
                Equipment equipment = equipmentOptional.get();
                editEquipmentDetails(equipment);
            } else {
                System.out.println("No equipment found with the given ID.");
            }
            runEditMenu = shouldReturnToEquipmentMenu();
        }
    }

    private void editEquipmentDetails(Equipment equipment) {
        boolean isEditingCurrentEquipment = true;
        while (isEditingCurrentEquipment) {
            int selectMenuOption = editEquipmentMenu();
            switch (selectMenuOption) {
                case 1 -> {
                    String editName = InputValidator.getUserDataString("Enter the new name, type '0' to abort: ");
                    if (!editName.equals("0")) {
                        equipment.setName(editName);
                        equipmentRepo.save(equipment);
                    }
                    isEditingCurrentEquipment = false;
                }
                case 2 -> {
                    double editPrice = InputValidator.getUserDataDouble("Enter the new price, type '0' to " +
                            "abort: ");
                    if (editPrice != 0) {
                        equipment.setPurchasePrice(editPrice);
                        equipmentRepo.save(equipment);
                    }
                    isEditingCurrentEquipment = false;
                }
                case 3 -> {
                    int newState = InputValidator.getUserDataInteger("Enter the new equipment state\n0 - " +
                            "Abort\n1 - assigned\n2 - unassigned: ", 0, 2);
                    if (newState != 0) {
                        equipment.setState(newState == 1 ? EquipmentState.assigned : EquipmentState.unassigned);
                        equipmentRepo.save(equipment);
                    }
                    isEditingCurrentEquipment = false;
                }
                case 4 -> deleteEquipment(equipment);
                case 0 -> isEditingCurrentEquipment = false;
            }
        }
    }

    private void deleteEquipment(Equipment equipment) {
        int confirmation = InputValidator.getUserDataInteger("Are you sure you want to delete " + equipment.getName() + "\n1 - Yes\n2 - No", 1, 2);
        if (confirmation == 1) {
            equipment.setState(EquipmentState.discontinued);
            equipmentRepo.save(equipment);
            System.out.println("Equipment deleted successfully.");
        } else {
            System.out.println("Equipment deletion cancelled.");
        }
    }

    private int editEquipmentMenu() {
        System.out.println("Please choose one of the following options: ");
        System.out.println("""
                0 - Quit menu
                1 - Edit name.
                2 - Edit price.
                3 - Edit equipment state
                4 - Delete equipment
                """);

        return InputValidator.getValidIntegerInput(0, 4);
    }

    private void displayEssentialOfEquipment() {
        List<Equipment> equipmentList = equipmentRepo.findAll();
        if (equipmentList.isEmpty()) {
            System.out.println("No equipment available.");
        } else {
            for (Equipment equipment : equipmentList) {
                System.out.println("ID: " + equipment.getId() + ", Name: " + equipment.getName() + ", Type: " + equipment.getType());
            }
        }
    }

    public void displayUnassignedEquipment() {
        System.out.println(unassignedEquipmentRepo.findAll());
    }

    public void displayLoggedInUsersEquipment(User foundUser) {
        List<Equipment> equipmentList = foundUser.getEquipmentList();
        if (!equipmentList.isEmpty()) {
            System.out.println("Your assigned equipment: ");
            for (Equipment tempList : equipmentList) {
                System.out.println("Id: " + tempList.getId() + " " + tempList.getName());
            }
        } else {
            System.out.println("You have no active equipment.");
        }
    }

    private boolean shouldReturnToEquipmentMenu() {
        int userDecision = InputValidator.getUserDataInteger("Do you want to return to Equipment " +
                "menu?\n1 - yes\n2 - No", 1, 2);
        return userDecision != 1;
    }
}
