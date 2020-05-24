package com.mountblue.canduitRestAPI.services;

import com.mountblue.canduitRestAPI.entity.User;

import java.util.List;

public interface UserService {

    public List<User> findAll();

    public User findById(int theId);

    public void save(User theUser);

    public void deleteById(int theId);

    public void registerUser(User user);

    public void confirmUser(String confirmationToken);

    public void resetPassword(User user);

    public void confirmResetPassword(String confirmationToken, User user);
}
