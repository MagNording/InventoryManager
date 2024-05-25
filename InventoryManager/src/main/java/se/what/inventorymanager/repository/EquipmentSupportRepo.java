package se.what.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.what.inventorymanager.domain.Equipment;
import se.what.inventorymanager.enums.EquipmentStatus;
import se.what.inventorymanager.domain.EquipmentSupport;

import java.util.List;


@Repository
public interface EquipmentSupportRepo extends JpaRepository<EquipmentSupport, Integer> {
    List<EquipmentSupport> findByEquipmentId(int equipmentId);
    List<EquipmentSupport> findByStatus(EquipmentStatus status);
    List<EquipmentSupport> findBySupportRecord(int supportRecord);

    int countByEquipment(Equipment equipment);
}
 