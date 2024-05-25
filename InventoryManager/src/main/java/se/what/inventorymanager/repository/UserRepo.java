package se.what.inventorymanager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import se.what.inventorymanager.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    User getUserByUsernameAndPassword(String username, String password);
    boolean existsUserById(int id);

}
