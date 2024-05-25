package se.what.inventorymanager.service;

import Utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.what.inventorymanager.domain.EquipmentOrder;
import se.what.inventorymanager.domain.User;
import se.what.inventorymanager.enums.EquipmentType;
import se.what.inventorymanager.repository.EquipmentOrderRepo;
import se.what.inventorymanager.repository.EquipmentRepo;
import se.what.inventorymanager.repository.UserRepo;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class EquipmentOrderService {

    private final EquipmentOrderRepo equipmentOrderRepo;
    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentOrderService(EquipmentOrderRepo equipmentOrderRepo, EquipmentService equipmentService) {
        this.equipmentOrderRepo = equipmentOrderRepo;
        this.equipmentService = equipmentService;
    }

    public void equipmentOrderMenu(User foundUser) {
        int userChoice;
        do {
            System.out.println("""
                    please choose option below:
                    0 - Back to Admin menu
                    1 - Create new order
                    2 - Display all orders
                    3 - Edit orders
                    """);

            userChoice = InputValidator.getValidIntegerInput(0, 3);
            switch (userChoice) {
                case 1 -> createNewOrder(foundUser);
                case 2 -> printAllOrders();
                case 3 -> editOrder(foundUser);
            }
        } while (userChoice != 0);
    }

    public void createNewOrder(User foundUser) {
        EquipmentOrder equipmentOrder = new EquipmentOrder();
        equipmentOrder.setName(InputValidator.getUserDataString("Please enter name of equipment:"));

        double orderPrice = InputValidator.getUserDataDouble("Please enter price of equipment:");
        if (orderPrice < Integer.MAX_VALUE) {
            equipmentOrder.setPrice(orderPrice);
        } else {
            System.out.println("\nInvalid price! Please enter a price below: " + Integer.MAX_VALUE);
            return;
        }

        System.out.println("Enter a date (yyMMdd):");
        Date estimatedDeliveryDate = InputValidator.getValidDeliveryDate();
        equipmentOrder.setEstDelDate(estimatedDeliveryDate);

        equipmentOrder.setType(getEquipmentType());

        equipmentOrder.setOrderDate(InputValidator.asDate(LocalDate.now()));
        equipmentOrder.setUser(foundUser);

        equipmentOrderRepo.save(equipmentOrder);
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

    public void editOrder(User foundUser) {
        System.out.println(equipmentOrderRepo.findAll());
        System.out.println("Please enter order ID of order you want to edit:");
        int foundId = InputValidator.getValidIntegerInput(0, Integer.MAX_VALUE);
        Optional<EquipmentOrder> equipmentOrderOptional = equipmentOrderRepo.findById(foundId);

        if (equipmentOrderOptional.isPresent()) {
            EquipmentOrder equipmentOrder = equipmentOrderOptional.get();
            System.out.println("""
                Please enter option below:
                0 - Back to admin menu
                1 - Change estimated delivery date
                2 - Remove order
                """);

            int userChoice = InputValidator.getValidIntegerInput(0, 2);
            switch (userChoice) {
                case 1 -> changeDelDate(equipmentOrder);
                case 2 -> removeOrder(equipmentOrder);
            }
        } else {
            System.out.println("Unable to find order by that ID");
        }
    }

    public void changeDelDate(EquipmentOrder equipmentOrder) {
        System.out.println("Please enter new delivery date (yyMMdd):");
        Date estimatedDeliveryDate = InputValidator.getValidDeliveryDate();
        equipmentOrder.setEstDelDate(estimatedDeliveryDate);
        equipmentOrderRepo.save(equipmentOrder);
    }

    public void removeOrder(EquipmentOrder equipmentOrder) {
        System.out.print("Are you sure you want to remove order with id: " + equipmentOrder.getId() + " from database?\n" +
                "1 - Yes\n" +
                "2 - No, and return to menu");
        int userChoice = InputValidator.getValidIntegerInput(1, 2);
        if (userChoice == 1) {
            equipmentOrderRepo.delete(equipmentOrder);
            System.out.println("Order " + equipmentOrder.getId() + " was removed");
            System.out.println("\nPlease add a " + equipmentOrder.getType() + " of model " + equipmentOrder.getName() +
                    " with price " + equipmentOrder.getPrice() + " kr to stock or user");
            equipmentService.addNewEquipment();
        } else {
            System.out.println("Returning to menu...");
        }
    }

    public void printAllOrders() {
        System.out.println(equipmentOrderRepo.findAll());
    }
}
