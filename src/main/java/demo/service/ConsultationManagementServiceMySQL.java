package demo.service;

import demo.database.Constants;
import demo.model.Consultation;
import demo.model.Patient;
import demo.model.User;
import demo.model.builder.ConsultationBuilder;
import demo.model.validation.ConsultationValidator;
import demo.model.validation.Notification;
import demo.repository.ConsultationRepository;
import demo.repository.PatientRepository;
import demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationManagementServiceMySQL {
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;


    public Notification<List<Consultation>> getAppointmentsForDoctor(Long doctorID){
        Notification<List<Consultation>> notification = new Notification<>();
        Optional<User> optionalUser = userRepository.findById(doctorID);
        if (validateOptionalDoctor(optionalUser)) {
            User user = optionalUser.get();
            if (user.getRoles().contains(Constants.Roles.DOCTOR)) {
                List<Consultation> consultationsList = new ArrayList<Consultation>();
                consultationRepository.findByDoctor(user).forEach(consultationsList::add);
                notification.setResult(consultationsList);
            }
            else {
                notification.addError("No such doctor!");
            }
        }
        else {
            notification.addError("No such doctor!");
        }
        return notification;
    }

    public Notification<List<Consultation>> getAppointmentsForPatient(Long patientID){
        Notification<List<Consultation>> notification = new Notification<>();
        Optional<Patient> optionalPatient = patientRepository.findById(patientID);
        if (validateOptionalPatient(optionalPatient)) {
            Patient patient = optionalPatient.get();
            List<Consultation> consultationsList = new ArrayList<Consultation>();
            consultationRepository.findByPatient(patient).forEach(consultationsList::add);
            notification.setResult(consultationsList);
        }
        else {
            notification.addError("No such patient!");
        }
        return notification;
    }

    public Notification<Boolean> makeAppointment(Long patientID, Long doctorID, Date date){
        Notification<Boolean> notification = new Notification<>();
        Optional<Patient> optionalPatient = patientRepository.findById(patientID);
        ConsultationBuilder consultationBuilder = new ConsultationBuilder();
        if (!validateOptionalPatient(optionalPatient)){
            notification.addError("No such patient");
            notification.setResult(Boolean.FALSE);
        }
        else {
            consultationBuilder.setPatient(optionalPatient.get());
        }
        Optional<User> optionalUser = userRepository.findById(doctorID);
        if (!validateOptionalDoctor(optionalUser)){
            notification.addError("No such doctor");
            notification.setResult(Boolean.FALSE);
        }
        else {
            consultationBuilder.setDoctor(optionalUser.get());
        }
        if (notification.hasErrors()) return notification;
        consultationBuilder.setConsultationDate(date);
        Consultation consultation = consultationBuilder.build();
        ConsultationValidator consultationValidator = new ConsultationValidator(consultation, consultationRepository);
        boolean consultationValid = consultationValidator.validate();
        if (consultationValid){
            consultationRepository.save(consultation);
            notification.setResult(Boolean.TRUE);
        }
        else {
            consultationValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        }
        return notification;
    }

    public Notification<Boolean> updateAppointment(Long appointmentID, Date date){
        Optional<Consultation> consultationOptional = consultationRepository.findById(appointmentID);
        Notification<Boolean> notification =  new Notification<>();
        if (!validateOptionalConsultation(consultationOptional)){
            notification.addError("No such appointment!");
            notification.setResult(Boolean.FALSE);
        }
        if (notification.hasErrors()){
            return notification;
        }
        Consultation consultation2 = consultationOptional.get();
        //System.out.println(consultationOptional.get().getConsultationDate());
        Consultation consultation = new Consultation();
        consultation.setId(consultation2.getId());
        consultation.setDoctor(consultation2.getDoctor());
        consultation.setPatient(consultation2.getPatient());
        consultation.setConsultationDate(date);
        //System.out.println(consultationOptional.get().getConsultationDate());
        ConsultationValidator consultationValidator = new ConsultationValidator(consultation, consultationRepository);
        if (!consultationValidator.validate()){
            consultationValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        }
        else {
            consultationRepository.save(consultation);
            notification.setResult(Boolean.TRUE);
        }
        return notification;
    }

    public boolean deleteAppointment(Long consultationID){
        Optional<Consultation> optionalConsultation = consultationRepository.findById(consultationID);
        if (validateOptionalConsultation(optionalConsultation)){
            consultationRepository.delete(optionalConsultation.get());
            return true;
        }
        return false;
    }

    public Notification<Boolean> addConsultationObservation(Long consultationID, String observations){
        Optional<Consultation> optionalConsultation = consultationRepository.findById(consultationID);
        Notification<Boolean> notification = new Notification<>();
        if (!validateOptionalConsultation(optionalConsultation)){
            notification.addError("No such appointment");
            notification.setResult(Boolean.FALSE);
            return notification;
        }
        if (observations == null || observations.trim().length()<1){
            notification.addError("Observation cannot be empty!");
            notification.setResult(Boolean.FALSE);
        }else {
            Consultation consultation = optionalConsultation.get();
            consultation.setObservations(observations);
            notification.setResult(Boolean.TRUE);
            consultationRepository.save(consultation);
        }
        return notification;
    }

    public List<Consultation> getAllConsultations(){
        List<Consultation> consultationList = new ArrayList<Consultation>();
        consultationRepository.findAll().forEach(consultationList::add);
        return consultationList;
    }

    /*
    public List<Consultation> getAllConsultationsForPatient(Patient patient){
        List<Consultation> consultationList = new ArrayList<Consultation>();
        if (patient!= null) {
            consultationRepository.findByPatient(patient).forEach(consultationList::add);
        }
        return consultationList;
    }
    */

    public Consultation getConsultation(Long consultationID) {
        if (consultationID != null){
            Optional<Consultation> consultationOptional = consultationRepository.findById(consultationID);
            if (validateOptionalConsultation(consultationOptional)){
                return  consultationOptional.get();
            }
        }
        return null;
    }

    private boolean validateOptionalPatient(Optional<Patient> optionalPatient){
        if (optionalPatient == null || !optionalPatient.isPresent()){
            return false;
        }
        return true;
    }

    private boolean validateOptionalDoctor(Optional<User> optionalUser){
        if (optionalUser == null || !optionalUser.isPresent()){
            return false;
        }
        return true;
    }

    private boolean validateOptionalConsultation(Optional<Consultation> optionalConsultation){
        if (optionalConsultation == null || !optionalConsultation.isPresent()){
            return false;
        }
        return true;
    }
}
