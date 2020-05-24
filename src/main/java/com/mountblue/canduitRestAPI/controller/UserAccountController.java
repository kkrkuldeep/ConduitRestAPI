package com.mountblue.canduitRestAPI.controller;

import com.mountblue.canduitRestAPI.DAO.ConfirmationTokenRepository;
import com.mountblue.canduitRestAPI.DAO.UserRepository;
import com.mountblue.canduitRestAPI.entity.User;
import com.mountblue.canduitRestAPI.services.AuthoritiesService;
import com.mountblue.canduitRestAPI.services.EmailSenderService;
import com.mountblue.canduitRestAPI.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserAccountController {

    private final Logger logger = LogManager.getLogger(UserAccountController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    private EmailSenderService emailSenderService; // first it create constructor of it

    @PostMapping(value="/register")
    public void registerUser(@RequestBody User user) {
        logger.info("Save register method called...");
        userService.registerUser(user);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public void confirmUserAccount(@RequestParam("token") String confirmationToken) {
        System.out.println("confirm account Method called");
        logger.info("Confirmation: " + confirmationToken);
        userService.confirmUser(confirmationToken);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody User user)
    {
        logger.info("Inside Reset password Link : ");
        System.out.println("User: " + user);
        userService.resetPassword(user);
    }

    @RequestMapping(value="/confirm-reset-password", method= {RequestMethod.GET, RequestMethod.POST})
    public void restPassword(@RequestParam("token") String confirmationToken, @RequestBody User user){
        logger.info("calling confirm reset password");
        System.out.println("confirmationToken: " + confirmationToken);
        System.out.println("User: " + user);
        userService.confirmResetPassword(confirmationToken, user);

    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthoritiesService getAuthoritiesService() {
        return authoritiesService;
    }

    public void setAuthoritiesService(AuthoritiesService authoritiesService) {
        this.authoritiesService = authoritiesService;
    }

    public ConfirmationTokenRepository getConfirmationTokenRepository() {
        return confirmationTokenRepository;
    }

    public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public EmailSenderService getEmailSenderService() {
        return emailSenderService;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Override
    public String toString() {
        return "UserAccountController{" +
                "userRepository=" + userRepository +
                ", confirmationTokenRepository=" + confirmationTokenRepository +
                ", emailSenderService=" + emailSenderService +
                '}';
    }
}