package se.what.inventorymanager.domain;

import jakarta.persistence.*;
import se.what.inventorymanager.enums.EquipmentType;

import java.util.Date;

@Entity
@Table(name = "equipment_order")
public class EquipmentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    double price;

    @Column(name = "estimated_del_date")
    private Date estDelDate;

    @Column(name = "order_date")
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @ManyToOne
    @JoinColumn(name = "order_by_user",referencedColumnName = "id")
    private User user;



    public EquipmentOrder() {
    }

    public EquipmentOrder(String name, double price, Date estDelDate, Date orderDate, EquipmentType type, User user) {
        this.name = name;
        this.price = price;
        this.estDelDate = estDelDate;
        this.orderDate = orderDate;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getEstDelDate() {
        return estDelDate;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setEstDelDate(Date estDelDate) {
        this.estDelDate = estDelDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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


    @Override
    public String toString() {
        return "\n\033[1mEquipmentOrder\033[0m" +
                " \033[1mId:\033[0m " + id +
                " | \033[1mName:\033[0m '" + name + '\'' +
                " | \033[1mPrice:\033[0m " + price + " kr " +
                " | \033[1mEstimated Delivery Date:\033[0m " + estDelDate +
                " | \033[1mOrderDate:\033[0m " + orderDate +
                " | \033[1mUser:\033[0m " + user.getName()
                ;
    }

}
