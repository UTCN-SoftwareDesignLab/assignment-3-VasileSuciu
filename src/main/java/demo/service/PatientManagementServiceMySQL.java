package demo.service;

import demo.model.Patient;
import demo.model.builder.PatientBuilder;
import demo.model.validation.Notification;
import demo.model.validation.PatientValidator;
import demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientManagementServiceMySQL {
    @Autowired
    private PatientRepository patientRepository;

    public Notification<Boolean> addPatient(String name, String idCardNumber, String personalNumericalNumber,
                                            Date dateOfBirth, String address){
        PatientBuilder patientBuilder = new PatientBuilder();
        Patient patient = patientBuilder.setName(name)
                .setIdCardNumber(idCardNumber)
                .setPersonalNumericalNumber(personalNumericalNumber)
                .setAddress(address)
                .setDateOfBirth(dateOfBirth)
                .build();
        PatientValidator patientValidator =  new PatientValidator(patient);
        Notification<Boolean> notification = new Notification<>();
        if (patientValidator.validate()){
            notification.setResult(Boolean.TRUE);
            patientRepository.save(patient);
        }
        else {
            patientValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        }
        return notification;

    }

    public Notification<Boolean> updatePatient(Long patienID, String name, String idCardNumber, String address){
        Optional<Patient> optionalPatient = patientRepository.findById(patienID);
        Notification<Boolean> notification= new Notification<>();
        if (!validateOptionalPatient(optionalPatient)){
            notification.addError("No such patient");
            notification.setResult(Boolean.FALSE);
            return notification;
        }
        Patient patient = optionalPatient.get();
        if (name!= null && name.trim().length()>0){
            patient.setName(name);
        }
        if (idCardNumber!= null && idCardNumber.trim().length()>0){
            patient.setIdCardNumber(idCardNumber);
        }
        if (address!= null && address.trim().length()>0){
            patient.setAddress(address);
        }
        PatientValidator patientValidator = new PatientValidator(patient);
        if (patientValidator.validate()){
            notification.setResult(Boolean.TRUE);
            patientRepository.save(patient);
        }
        else{
            patientValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        }
        return notification;
    }

    public boolean deletePatient(Long patientID){
        Optional<Patient> optionalPatient = patientRepository.findById(patientID);
        if (validateOptionalPatient(optionalPatient)){
            patientRepository.delete(optionalPatient.get());
            return true;
        }
        return false;
    }

    public Patient getPatient(Long patientID){
        if (patientID!=null) {
            Optional<Patient> optionalPatient = patientRepository.findById(patientID);
            if (validateOptionalPatient(optionalPatient)) {

                return optionalPatient.get();
            }
        }
        return null;
    }

    public List<Patient> getAllPatients(){
        List<Patient> patientList = new ArrayList<Patient>();
        patientRepository.findAll().forEach(s->patientList.add(s));
        return patientList;
    }

    private boolean validateOptionalPatient(Optional<Patient> optionalPatient){
        if (optionalPatient == null || !optionalPatient.isPresent()){
            return false;
        }
        return true;
    }

}
