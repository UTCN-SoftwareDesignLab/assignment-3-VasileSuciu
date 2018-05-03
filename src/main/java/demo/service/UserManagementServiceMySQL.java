package demo.service;

import demo.database.Constants;
import demo.model.User;
import demo.model.builder.UserBuilder;
import demo.model.validation.Notification;
import demo.model.validation.UserValidator;
import demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementServiceMySQL {
    @Autowired
    private UserRepository userRepository;

    public Notification<Boolean> register(String username, String name,String password, String roles) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(Constants.Roles.ROLES).filter(s -> roles.toLowerCase().contains(s.toLowerCase())).forEach(s -> stringBuilder.append(s + " "));
        User user = new UserBuilder()
                .setUsername(username)
                .setName(name)
                .setPassword(password)
                .setRoles(stringBuilder.toString().trim())
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();

        if (!userValid) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
            return userRegisterNotification;
        } else {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
            userRegisterNotification.setResult(Boolean.TRUE);
            return userRegisterNotification;
        }
    }

    public Notification<User> login(String username, String password) {
        Notification<User> notification = new Notification<>();
        if (password!=null && username !=null) {
            //System.out.println(username+" "+password);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //System.out.println(username+" "+bCryptPasswordEncoder.encode(password));
            User user = userRepository.findByUsernameAndPassword(username, bCryptPasswordEncoder.encode(password));
            if (user != null) {
                notification.setResult(user);
            } else {
                notification.addError("Invalid username or password!");
            }
        }
        else {
            notification.addError("Invalid username or password!");
        }
        return notification;
    }


    public Notification<Boolean> updateUser(String username, String name, String password, String roles) {
        User user = userRepository.findByUsername(username);
        boolean passwordChanged = false;
        if (user!=null) {
            if (roles!= null && roles.trim().length()>0) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(Constants.Roles.ROLES).filter(s -> roles.toLowerCase().contains(s.toLowerCase())).forEach(s -> stringBuilder.append(s + " "));
                user.setRoles(stringBuilder.toString());
            }
            passwordChanged = (password != null) && (password.trim().length() > 0);
            if (passwordChanged) {
                user.setPassword(password);
            }
            if (name!=null && name.trim().length()>0){
                user.setName(name);
            }
        }
        UserValidator userValidator = new UserValidator(user);
        boolean userValid;
        if (passwordChanged){
            userValid = userValidator.validate();
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        else {
            userValid = userValidator.validateExceptPassword();
        }
        Notification<Boolean> userNotification = new Notification<>();

        if (!userValid){
            userValidator.getErrors().forEach(userNotification::addError);
            userNotification.setResult(Boolean.FALSE);
            return userNotification;
        }
        else {
            userNotification.setResult(Boolean.TRUE);
            userRepository.save(user);
        }
        return userNotification;
    }

    public User getUser(String user) {
        return userRepository.findByUsername(user);
    }

    public boolean deleteUser(String user) {
        User user1 = userRepository.findByUsername(user);
        if (user1 != null) {
            userRepository.delete(userRepository.findByUsername(user));
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<User> getAllDoctors() {
        List<User> users = getAllUsers();
        List<User> doctors = users.stream().filter(s->s.getRoles().contains(Constants.Roles.DOCTOR)).collect(Collectors.toList());
        return doctors;
    }

}
