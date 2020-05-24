package com.mountblue.canduitRestAPI.DAO;

import com.mountblue.canduitRestAPI.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);

    @Query(value = "select c from ConfirmationToken c where c.user = ?1")
    ConfirmationToken findByUser(int userId);

}
