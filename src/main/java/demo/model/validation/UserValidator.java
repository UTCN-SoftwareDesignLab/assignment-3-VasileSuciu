package demo.model.validation;

import demo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserValidator {
    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final User user;

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    public UserValidator(User user) {
        this.user = user;
        errors = new ArrayList<>();
    }

    public boolean validate() {
        if (user!=null) {
            validateUsername(user.getUsername());
            validatePassword(user.getPassword());
            validateName(user.getName());
            validateRoles(user.getRoles());
        }
        else {
            errors.add("No such user!");
        }
        return errors.isEmpty();
    }

    public boolean validateExceptPassword() {
        if (user!=null) {
            validateUsername(user.getUsername());
            validateRoles(user.getRoles());
            validateName(user.getName());
        }
        else {
            errors.add("No such user!");
        }
        return errors.isEmpty();
    }


    private void validateUsername(String username) {
        if (username == null){
            errors.add("Username should not be empty");
        }
        else {
            if (!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(username).matches()) {
                errors.add("Invalid email!");
            }
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            errors.add("Password should not be empty");
        }
        else {
            if (password.length() < MIN_PASSWORD_LENGTH) {
                errors.add("Password too short!");
            }
            if (!containsSpecialCharacter(password)) {
                errors.add("Password must contain at least one special character!");
            }
            if (!containsDigit(password)) {
                errors.add("Password must contain at least one number!");
            }
         }
    }

    private void validateRoles(String roles){
        if (roles == null || roles.isEmpty()){
            errors.add("Each user should have one role!");
        }
        else {
            if (roles.trim().contains(" ")){
                errors.add("No more than one role allowed!");
            }
        }
    }

    private void validateName(String name){
        if (name == null){
            errors.add("Name should not be null!");
        }
        else {
            if (name.trim().length()<1 || containsDigit(name) || containsSpecialCharacter(name)) {
                errors.add("Name should not be empty and contain only letters!");
            }
        }
    }

    private boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        return m.find();
    }

    private static boolean containsDigit(String s) {
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true;
                }
            }
        }
        return false;
    }


}
