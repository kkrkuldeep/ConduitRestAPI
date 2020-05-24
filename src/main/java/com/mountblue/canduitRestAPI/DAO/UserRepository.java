package com.mountblue.canduitRestAPI.DAO;

import com.mountblue.canduitRestAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByName(String name);

    @Query(value = "select u from User u where u.name = ?1")
    public User findAllByName(String name);

    User findByEmailIgnoreCase(String emailId);
}
