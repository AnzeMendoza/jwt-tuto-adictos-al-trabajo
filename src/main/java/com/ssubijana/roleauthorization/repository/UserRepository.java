package com.ssubijana.roleauthorization.repository;

import com.ssubijana.roleauthorization.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    User findById(long id);

    List<User> findAll();
}
