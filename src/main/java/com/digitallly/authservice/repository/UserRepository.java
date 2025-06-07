package com.digitallly.authservice.repository;

import com.digitallly.authservice.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

}
