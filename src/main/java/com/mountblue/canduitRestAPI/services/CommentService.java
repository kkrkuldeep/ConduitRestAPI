package com.mountblue.canduitRestAPI.services;


import com.mountblue.canduitRestAPI.entity.Comments;

import java.util.List;

public interface CommentService {
    public List<Comments> findAll();

    public Comments findById(int theId);

    public void save(Comments theComment);

    public void deleteById(int theId);

}
