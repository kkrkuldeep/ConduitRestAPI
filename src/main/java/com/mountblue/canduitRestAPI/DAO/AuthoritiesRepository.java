package com.mountblue.canduitRestAPI.DAO;

import com.mountblue.canduitRestAPI.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
}
