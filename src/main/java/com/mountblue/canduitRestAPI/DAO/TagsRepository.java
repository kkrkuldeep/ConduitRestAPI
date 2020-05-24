package com.mountblue.canduitRestAPI.DAO;

import com.mountblue.canduitRestAPI.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query(value="select t.name from Tags t where t.id = ?1")
    public String getTagName(int id);
}
