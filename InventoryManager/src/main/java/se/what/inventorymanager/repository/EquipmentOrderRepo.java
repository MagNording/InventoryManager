package se.what.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.what.inventorymanager.domain.EquipmentOrder;

@Repository
public interface EquipmentOrderRepo extends JpaRepository <EquipmentOrder,Integer> {



}
