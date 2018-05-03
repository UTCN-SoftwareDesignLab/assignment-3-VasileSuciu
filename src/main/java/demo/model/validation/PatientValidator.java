package demo.model.validation;

import demo.model.Patient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientValidator {
    private Patient patient;
    private List<String> errors;

    public PatientValidator(Patient patient){
        this.patient = patient;
        errors = new ArrayList<String>();
    }

    public List<String> getErrors(){
        return errors;
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public boolean validate(){
        validateName(patient.getName());
        validateAddress(patient.getAddress());
        validateIDCardNumber(patient.getIdCardNumber());
        validatePersonalNumericalCode(patient.getPersonalNumericalCode());
        return errors.isEmpty();
    }

    private void validateDateOfBirth(Date dateOfBirth){
        if (dateOfBirth == null){
            errors.add("Date of birth cannot be null!");
        }
        else{
            if (dateOfBirth.after(new Date(System.currentTimeMillis()))){
                errors.add("Date of birth after today not allowed!");
            }
        }
    }

    private void validatePersonalNumericalCode(String pnc){
        if (pnc == null || pnc.trim().length() != 5){
            errors.add("Personal Numerical Code should be 5 digits long");
        }
        for (Character c: pnc.trim().toCharArray()){
            if (!Character.isDigit(c)){
                errors.add("Personal Numerical Code should contain only digits");
                return;
            }
        }
    }

    private void validateIDCardNumber(String idcard){
        if (idcard == null || idcard.trim().length() != 5){
            errors.add("ID Card Number should be 5 characters long");
        }
        else {
            char[] chars = idcard.trim().toCharArray();
            if (!Character.isAlphabetic(chars[0]) || !Character.isAlphabetic(chars[1])) {
                errors.add("First two characters of ID Card Number should be letters");
            }

            if (!Character.isDigit(chars[2]) || !Character.isDigit(chars[3]) || !Character.isDigit(chars[4])) {
                errors.add("Last three characters of ID Card Number should be digits");
            }
        }
    }

    private void validateAddress(String address){
        if (address == null || address.trim().length()<1){
            errors.add("Address should not be empty!");
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
