package se.what.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.what.inventorymanager.domain.Equipment;
import se.what.inventorymanager.enums.EquipmentState;
import se.what.inventorymanager.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepo extends JpaRepository<Equipment, Integer> {
    Optional<Equipment> findById(int id);
    List<Equipment> findByState (EquipmentState state);
    boolean existsEquipmentByUser (Optional<User> user);
}




