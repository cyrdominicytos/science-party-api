package fr.istic.science.repository;

import fr.istic.science.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(path = "userss")
public interface UserRepository extends JpaRepository<User, Long> {
    // You can define additional methods here if needed
}
