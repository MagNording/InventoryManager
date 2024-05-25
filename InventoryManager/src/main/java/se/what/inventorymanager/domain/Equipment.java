package se.what.inventorymanager.domain;

import jakarta.persistence.*;
import se.what.inventorymanager.enums.EquipmentState;
import se.what.inventorymanager.enums.EquipmentType;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "equipment_name")
    private String name;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "purchase_price")
    private double purchasePrice;

    @Enumerated(EnumType.STRING)
    private EquipmentState state;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.EAGER)
    private List<EquipmentSupport> equipmentSupport;
    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private User user;

    public Equipment() {
    }

    public Equipment(String name, Date purchaseDate, double purchasePrice, EquipmentState state,
                     EquipmentType type, User user) {
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.state = state;
        this.type = type;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public EquipmentState getState() {
        return state;
    }

    public void setState(EquipmentState state) {
        this.state = state;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<EquipmentSupport> getEquipmentSupport() {
        return equipmentSupport;
    }

    public void setEquipmentSupport(EquipmentSupport equipmentSupport) {
        this.equipmentSupport = (List<EquipmentSupport>) equipmentSupport;
    }

    @Override
    public String toString() {
        return "\n\033[1mID:\033[0m " + id + " | " +
                " \033[1mName: \033[0m" + name + " | "  +
                "\033[1mPurchase Date: \033[0m" + purchaseDate + " | " +
                "\033[1mPurchase Price: \033[0m" + purchasePrice + " kr " +  " | " +
                "\033[1mState: \033[0m" + state + " | " +
                "\033[1mType: \033[0m" + type;
    }


}

