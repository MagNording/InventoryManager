package se.what.inventorymanager.domain;
import jakarta.persistence.*;
import se.what.inventorymanager.enums.EquipmentStatus;

@Entity
@Table(name = "equipment_support")
public class EquipmentSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EquipmentStatus status;

    @Column(name = "support_record")
    private int supportRecord;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @Column(name = "description")
    private String description;


    public EquipmentSupport() {
    }

    public EquipmentSupport(EquipmentStatus status, int supportRecord, String description,
                            Equipment equipment) {
        this.status = status;
        this.supportRecord = supportRecord;
        this.description = description;
        this.equipment = equipment;
    }

    public int getId() {
        return id;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public int getSupportRecord() {
        return supportRecord;
    }

    public void setSupportRecord(int supportRecord) {
        this.supportRecord = supportRecord;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }


    @Override
    public String toString() {
        return "\n\033[1mEquipmentSupport\033[0m" +
                " \033[1mId:\033[0m " + id +
                " | \033[1mStatus:\033[0m " + status +
                " | \033[1mSupport Record:\033[0m " + supportRecord +
                " | \033[1mDescription:\033[0m " + description +
                " | \033[1mEquipment:\033[0m " + (equipment != null ? equipment.getId() : null)
                ;
    }



}
