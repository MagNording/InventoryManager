package se.what.inventorymanager.domain;
import jakarta.persistence.*;
import se.what.inventorymanager.enums.EquipmentState;
import se.what.inventorymanager.enums.EquipmentType;

import java.util.Date;

@Entity
@Table(name ="unassignedequipment")
public class UnassignedEquipment {
    @Id
    private int id;

    @Column(name="equipment_name")
    private String name;

    @Column(name="purchase_date")
    private Date purchaseDate;

    @Column(name="purchase_price")
    private double purchasePrice;

    @Enumerated(EnumType.STRING)
    private EquipmentState state;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @Override
    public String toString() {
        return "\n\033[1mThe " + type + " of model " + name + " is not assigned to any user.\033[0m";
    }

}