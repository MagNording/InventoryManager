package se.what.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.what.inventorymanager.domain.UnassignedEquipment;

@Repository
public interface UnassignedEquipmentRepo extends JpaRepository <UnassignedEquipment, Integer> {
}
