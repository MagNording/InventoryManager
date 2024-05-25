package se.what.inventorymanager.service;

import Utils.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.what.inventorymanager.domain.Equipment;
import se.what.inventorymanager.domain.SearchRecord;
import se.what.inventorymanager.repository.*;

import java.util.*;

@Service
public class SearchRecordService {

    @Autowired
    private SearchRecordRepo searchRecordRepo;

    @Autowired
    private UnassignedEquipmentRepo unassignedEquipmentRepo;

    @Autowired
    private EquipmentRepo equipmentRepo;

    public void searchRecordMenu() {
        int userInput;
        do {
            System.out.println("\nPlease choose an option by number\nSearch record:\n" +
                    "0 - Return to main menu\n" +
                    "1 - Display all search words\n" +
                    "2 - Display all unique search words\n" +
                    "3 - Display number of searches by type of search\n" +
                    "4 - Delete all search records");
            userInput = InputValidator.getValidIntegerInput(0, 4);
            switch (userInput) {
                case 1 -> printAllSearches();
                case 2 -> printUniqueQueries();
                case 3 -> sumOfQueries();
                case 4 -> deleteSearchRecord();
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (userInput != 0);
        System.out.println("Returning back to main menu.\n\n");
    }

    public void printAllSearches() {
        System.out.println("Search record:");
        var foundRecords = searchRecordRepo.findAll();
        for (SearchRecord foundRecord : foundRecords) {
            System.out.println(foundRecord);
        }
    }

    public void printUniqueQueries() {
        System.out.println("Display all unique search words:");
        Set<String> uniqueQueries = new HashSet<>();
        var foundRecords = searchRecordRepo.findAll();

        for (SearchRecord foundRecord : foundRecords) {
            String query = foundRecord.getQuery();
            String[] words = query.split("\\s+");
            Collections.addAll(uniqueQueries, words);
        }
        for (String word : uniqueQueries) {
            System.out.println("query " + word);
        }
    }

    public void sumOfQueries() {
        System.out.println("See how many times a certain query occurs in users search " +
                "history.");
        String searchByQuery = InputValidator.getUserDataString("What query would you like to display?");
        int count = 0;
        var foundRecords = searchRecordRepo.findAll();
        for (SearchRecord foundRecord : foundRecords) {
            if (foundRecord.getQuery().contains(searchByQuery)) {
                count++;
            }
        }
        if (count > 0) {
            System.out.println("'" + searchByQuery + "'" + " has been searched for " + count + " times");
        } else {
            System.out.println("None found");
        }
    }

    public void deleteSearchRecord() {
        System.out.println("\nAre you sure you want to delete all search records?\n" +
                "It will not be possible to recover deleted records.\n" +
                "0 - Keep records and exit to menu\n" +
                "1 - Delete all records.");
        int deleteOrNo = InputValidator.getValidIntegerInput(0, 1);
        if (deleteOrNo == 1) {
            searchRecordRepo.deleteAll();
            System.out.println("All records deleted.");
        } else {
            System.out.println("Not deleted.");
        }
    }

    public void equipmentSearchQueryMenu() {
        int menuOption;
        do {
            System.out.println("""
                    Choose option below:
                    0 - Back to main menu
                    1 - Free text search equipment
                    2 - Display all available equipment
                    """);

            menuOption = InputValidator.getValidIntegerInput(0, 2);

            switch (menuOption) {
                case 1 -> {
                    SearchRecord searchRecord = new SearchRecord();
                    String query = InputValidator.getUserDataString("Type search:");
                    searchRecord.setQuery(query);
                    searchRecordRepo.save(searchRecord);

                    List<Equipment> foundEquipment = equipmentRepo.findAll();
                    boolean found = false;

                    for (Equipment foundEq : foundEquipment) {
                        if (foundEq.getName().toLowerCase().contains(query.toLowerCase())) {
                            System.out.println("Found:");
                            System.out.println(foundEq);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("\nNone found.\n");
                    } else {
                        System.out.println("\nPlease contact your manager to request loan with Id of Equipment\n");
                    }
                }
                case 2 -> System.out.println("The following equipment is not assigned to anybody, " +
                        "please contact your manager to request loan with Id of Equipment\n" +
                        unassignedEquipmentRepo.findAll());
            }
        } while (menuOption != 0);
    }
}
