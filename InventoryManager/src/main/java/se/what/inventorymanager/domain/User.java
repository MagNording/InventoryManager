package se.what.inventorymanager.domain;
import jakarta.persistence.*;
import se.what.inventorymanager.enums.RoleType;

import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String department;
    private String email;
    private String telephone;
    private String username;
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Equipment> equipmentList;

    public User(String name, String department, String email, String telephone, String username, String password, RoleType role) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.telephone = telephone;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    @Override
    public String toString() {
        return "\n\033[1mUser id:\033[0m " + id + " | " +
                " \033[1mFull Name: \033[0m" + name + " | "  +
                "\033[1mDepartment: \033[0m" + department + " | " +
                "\033[1mEmail: \033[0m" + email + " | " +
                "\033[1mTelephone: \033[0m" + telephone + " | " +
                "\033[1mUsername: \033[0m" + username + " | " +
                "\033[1mPassword: \033[0m" + password + " | " +
                "\033[1mRole: \033[0m " + role;
    }
}

;
