package se.what.inventorymanager.domain;
import jakarta.persistence.*;

@Entity
@Table(name ="equipmentowner")
public class AssignedEquipment {
    @Id
    private int id;
    private String name;
    private String email;
    @Column(name = "equipment_name")
    private String equipmentName;



    @Override
    public String toString() {
        return "\n\033[1mAssignedEquipment:\033[0m " + " | " +
                "\033[1mId:\033[0m " + id + " | " +
                "\033[1mName:\033[0m " + name + " | " +
                "\033[1mEmail:\033[0m " + email + " | "  +
                "\033[1mEquipmentName:\033[0m " + equipmentName;
    }



}
