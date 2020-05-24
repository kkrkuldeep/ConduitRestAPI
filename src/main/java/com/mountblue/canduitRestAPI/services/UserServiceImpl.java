package com.mountblue.canduitRestAPI.services;

import com.mountblue.canduitRestAPI.DAO.ConfirmationTokenRepository;
import com.mountblue.canduitRestAPI.DAO.UserRepository;
import com.mountblue.canduitRestAPI.controller.UserAccountController;
import com.mountblue.canduitRestAPI.entity.Authorities;
import com.mountblue.canduitRestAPI.entity.ConfirmationToken;
import com.mountblue.canduitRestAPI.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger logger = LogManager.getLogger(UserAccountController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int theId) {

        Optional<User> result = userRepository.findById(theId);

        User theUser;

        if (result.isPresent()) {
            theUser = result.get();
        } else {
            throw new RuntimeException("Did not find user id - " + theId);
        }

        return theUser;
    }

    @Override
    public void save(User theUser) {
        userRepository.save(theUser);
    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public void registerUser(User user) {

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        Authorities authorities = authoritiesService.findById(2);
        if (existingUser != null) {
            logger.info("This email already exists!");
            logger.error("Error");
        } else {

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(false);
            user.setAuthorities(authorities);
            userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("kkrkuldeepkumar@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "https://spring-rest-1-0.herokuapp.com/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            logger.info("Register mail Send Successfully");
        }
    }

    @Override
    public void confirmUser(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            Authorities authorities = authoritiesService.findById(2);

            // add salting here

            user.setEnabled(true);
            user.setAuthorities(authorities);
            userRepository.save(user);
        } else {
            logger.info("This email already exists!");
            logger.error("Error");
        }
    }

    @Override
    public void resetPassword(User user) {

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail()); // It have my user

        if (existingUser != null) {

            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getEmail());
            mailMessage.setSubject("Reset Password");
            mailMessage.setFrom("kkrkuldeepkumar@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "https://spring-rest-1-0.herokuapp.com/confirm-reset-password?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

        } else {
            logger.info("This email already exists!");
            logger.error("Error");
        }
    }

    @Override
    public void confirmResetPassword(String confirmationToken, User user) {

        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            User existingUser = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(existingUser);

        } else {
            logger.info("This email already exists!");
            logger.error("Error");
        }
    }
}
