package se.what.inventorymanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.what.inventorymanager.domain.Equipment;
import se.what.inventorymanager.domain.EquipmentSupport;
import se.what.inventorymanager.domain.User;
import se.what.inventorymanager.enums.EquipmentState;
import se.what.inventorymanager.enums.EquipmentStatus;
import se.what.inventorymanager.repository.EquipmentRepo;
import se.what.inventorymanager.repository.EquipmentSupportRepo;
import se.what.inventorymanager.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentSupportService {

    private final EquipmentSupportRepo equipmentSupportRepo;
    private final EquipmentRepo equipmentRepo;
    private final UserRepo userRepo;

    @Autowired
    public EquipmentSupportService(EquipmentSupportRepo equipmentSupportRepo,
                                   EquipmentRepo equipmentRepo, UserRepo userRepo) {
        this.equipmentSupportRepo = equipmentSupportRepo;
        this.equipmentRepo = equipmentRepo;
        this.userRepo = userRepo;
    }

    public List<EquipmentSupport> findAllSupportTickets() {
        return equipmentSupportRepo.findAll();
    }

    public void addTicket(User foundUser, int equipmentId, String description) {
        EquipmentSupport equipmentSupport = new EquipmentSupport();
        equipmentSupport.setStatus(EquipmentStatus.open);

        Optional<Equipment> optionalEquipment = equipmentRepo.findById(equipmentId);

        if (optionalEquipment.isPresent()) {
            Equipment equipment = optionalEquipment.get();
            equipmentSupport.setEquipment(equipment);

            if (!equipment.getState().equals(EquipmentState.in_repair)) {
                equipmentSupport.setDescription(description);

                int sumOfSupportRecords = equipmentSupport.getSupportRecord();
                sumOfSupportRecords++;
                equipmentSupport.setSupportRecord(sumOfSupportRecords);
                equipmentSupportRepo.save(equipmentSupport);

                int count = equipmentSupportRepo.countByEquipment(equipment);

                equipment.setState(EquipmentState.in_repair);
                equipmentSupport.setSupportRecord(count);
                equipmentSupportRepo.save(equipmentSupport);
                equipmentRepo.save(equipment);
            }
        }
    }

    public void editSupportTicket(int supportTicketId, String description, String statusInput) {
        Optional<EquipmentSupport> optionalEquipmentSupport = equipmentSupportRepo.findById(supportTicketId);

        if (optionalEquipmentSupport.isPresent()) {
            EquipmentSupport equipmentSupport = optionalEquipmentSupport.get();
            equipmentSupport.setDescription(description);

            EquipmentStatus newStatus = EquipmentStatus.valueOf(statusInput.toLowerCase());
            equipmentSupport.setStatus(newStatus);

            equipmentSupportRepo.save(equipmentSupport);
        }
    }

    public void deleteSupportTicket(int supportTicketId) {
        Optional<EquipmentSupport> equipmentSupportOptional = equipmentSupportRepo.findById(supportTicketId);
        if (equipmentSupportOptional.isPresent()) {
            EquipmentSupport equipmentSupport = equipmentSupportOptional.get();
            equipmentSupport.setStatus(EquipmentStatus.closed);
            equipmentSupportRepo.save(equipmentSupport);

            Optional<Equipment> foundEquipmentOptional = equipmentRepo.findById(
                    equipmentSupport.getEquipment().getId());

            if (foundEquipmentOptional.isPresent()) {
                Equipment foundEquipment = foundEquipmentOptional.get();
                foundEquipment.setState(EquipmentState.assigned);
                equipmentRepo.save(foundEquipment);
            }
        }
    }

    public void displayLoggedInUsersTickets(User foundUser) {
        foundUser = userRepo.getUserByUsernameAndPassword(foundUser.getUsername(), foundUser.getPassword());
        List<Equipment> equipmentList = foundUser.getEquipmentList();

        if (equipmentList.isEmpty()) {
            System.out.println("You have no assigned equipment");
            return;
        }

        boolean hasSupportTickets = false;
        for (Equipment equipment : equipmentList) {
            List<EquipmentSupport> equipmentSupportList = equipment.getEquipmentSupport();
            if (equipmentSupportList != null && !equipmentSupportList.isEmpty()) {
                System.out.println("Tickets for Equipment ID " + equipment.getId() + ":");
                for (EquipmentSupport equipmentSupport : equipmentSupportList) {
                    System.out.println(equipmentSupport);
                }
                hasSupportTickets = true;
            }
        }

        if (!hasSupportTickets) {
            System.out.println("You have no active support tickets\n");
        }

    }
}
