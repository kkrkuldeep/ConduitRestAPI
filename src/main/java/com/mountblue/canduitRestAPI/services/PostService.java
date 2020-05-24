package com.mountblue.canduitRestAPI.services;

import com.mountblue.canduitRestAPI.entity.Post;

import java.security.Principal;
import java.util.List;

public interface PostService {

    public List<Post> findAll();

    public Post findById(int theId);

    public void save(Post thePost);

    public String deleteById(int theId, Principal principal);

    public void addPostIntoAccount(Post post, int userId, String actionPerformed);
}
