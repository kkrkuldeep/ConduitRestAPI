package com.mountblue.canduitRestAPI.DAO;

import com.mountblue.canduitRestAPI.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Integer> {

    List<Comments> findByPostId(int theId);

}
