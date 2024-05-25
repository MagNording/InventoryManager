package se.what.inventorymanager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.what.inventorymanager.domain.AssignedEquipment;

@Repository
public interface AssignedEquipmentRepo extends JpaRepository<AssignedEquipment, Integer> {
}
