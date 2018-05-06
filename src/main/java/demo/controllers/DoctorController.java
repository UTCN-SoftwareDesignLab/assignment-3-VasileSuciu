package demo.controllers;

import demo.model.Consultation;
import demo.model.Patient;
import demo.model.PatientDTO;
import demo.model.validation.Notification;
import demo.service.ConsultationManagementServiceMySQL;
import demo.service.PatientManagementServiceMySQL;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DoctorController {
    @Autowired
    private PatientManagementServiceMySQL patientManagementServiceMySQL;
    @Autowired
    private ConsultationManagementServiceMySQL consultationManagementServiceMySQL;

    @RequestMapping(value = "/doctor", method = RequestMethod.GET)
    public String showDoctorPage(ModelMap model){
        model.addAttribute("errorMessage", "");
        //model.addAttribute("patient", new Patient());
        //model.addAttribute("consultation", new Consultation());
        List<Patient> patients = patientManagementServiceMySQL.getAllPatients();
        model.addAttribute("patientList", patients);
        List<Consultation> consultations = new ArrayList<>();
        if (patients!=null && patients.size()>0) {
            consultations = consultationManagementServiceMySQL.getAppointmentsForPatient(patients.get(0).getId()).getResult();
        }
        model.addAttribute("consultationList", consultations);
        model.addAttribute("consultationList2", consultations);
        return "doctor";
    }

    /*
    @RequestMapping(value = "/doctor", params="patientBtn", method = RequestMethod.POST)
    public String handlePatientChange(ModelMap model,  @ModelAttribute("patient") Patient patient) {
        List<Consultation> consultations = new ArrayList<>();
        /*consultationManagementServiceMySQL.getAllConsultationsForPatient(
                patientManagementServiceMySQL.getPatient(patient.getId()));
        Notification<List<Consultation>> notification = consultationManagementServiceMySQL.getAppointmentsForPatient(patient.getId());
        if (notification.hasErrors()){
            model.addAttribute("errorMessage", notification.getFormattedErrors());
        }
        else{
            consultations = notification.getResult();
        }
        model.addAttribute("consultationList", consultations);
        model.addAttribute("consultationList2", consultations);
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("patient", new Patient());
        model.addAttribute("consultation", new Consultation());
        return "doctor";
    }

    @RequestMapping(value = "/doctor", params="consultationBtn", method = RequestMethod.POST)
    public String handleConsultationObservation(ModelMap model,  @ModelAttribute("consultation") Consultation consultation) {
        Notification<Boolean> notification = consultationManagementServiceMySQL.addConsultationObservation(consultation.getId(),
                consultation.getObservations());
        if (notification.hasErrors()){
            model.addAttribute("errorMessage", notification.getFormattedErrors());
        }
        consultation = consultationManagementServiceMySQL.getConsultation(consultation.getId());
        if (consultation != null) {
            System.out.println(consultation.getPatient().getId());
            //System.out.println(patientManagementServiceMySQL.getPatient(patient.getId()));
            List<Consultation> consultations = consultationManagementServiceMySQL.getAppointmentsForPatient(consultation.getPatient().getId()).getResult();
            System.out.println(consultations);
            model.addAttribute("consultationList", consultations);
            model.addAttribute("consultationList2", consultations);
            model.addAttribute("errorMessage", "");
        }
        else {
            model.addAttribute("errorMessage","Invalid consultation id");
        }
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("patient", new Patient());
        model.addAttribute("consultation", new Consultation());
        return "doctor";
    }

    @RequestMapping(value = "/doctor", params="secretaryBtn", method = RequestMethod.GET)
    public String handleSecretaryView(ModelMap model) {
        return "redirect:secretary";
    }

    @RequestMapping(value = "/doctor", params="adminBtn", method = RequestMethod.GET)
    public String handleAdministratorView(ModelMap model) {
        return "redirect:administrator";
    }

    @RequestMapping(value = "/doctor", params="consultationBtn", method = RequestMethod.GET)
    public String handleConsultationView(ModelMap model) {
        return "redirect:consultation";
    }
    */
    @RequestMapping(value = "/addObservation", method = RequestMethod.GET)
    public String handleConsultationObservation(ModelMap model, @RequestParam("consultationID") Long consultationID,
                                                @RequestParam("observation") String observation) {
        System.out.println(consultationID);
        Notification<Boolean> notification = consultationManagementServiceMySQL.addConsultationObservation(consultationID,
                observation);
        if (notification.hasErrors()){
            model.addAttribute("errorMessage", notification.getFormattedErrors());
        }
        Consultation consultation = consultationManagementServiceMySQL.getConsultation(consultationID);
        if (consultation != null) {
            List<Consultation> consultations = consultationManagementServiceMySQL.getAppointmentsForPatient(consultation.getPatient().getId()).getResult();
            model.addAttribute("consultationList", consultations);
            model.addAttribute("consultationList2", consultations);
            model.addAttribute("errorMessage", "");
        }
        else {
            model.addAttribute("errorMessage","Invalid consultation id");
        }
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("patient", new Patient());
        model.addAttribute("consultation", new Consultation());
        return "doctor";
    }

    @RequestMapping(value = "/viewPatient", method = RequestMethod.GET)
    public String handlePatientChange(ModelMap model, @RequestParam("patientID") Long patientID) {
        //System.out.println("Patients ID " +patientID);
        List<Consultation> consultations = new ArrayList<>();
        Notification<List<Consultation>> notification = consultationManagementServiceMySQL.getAppointmentsForPatient(patientID);
        if (notification.hasErrors()){
            model.addAttribute("errorMessage", notification.getFormattedErrors());
        }
        else{
            consultations = notification.getResult();
        }
        model.addAttribute("consultationList", consultations);
        model.addAttribute("consultationList2", consultations);
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("patient", new Patient());
        model.addAttribute("consultation", new Consultation());
        return "doctor";
    }

    @RequestMapping(value = "/secretaryChange2", method = RequestMethod.GET)
    public String handleSecretaryView(ModelMap model) {
        return "redirect:secretary";
    }

    @RequestMapping(value = "/administratorChange2", method = RequestMethod.GET)
    public String handleAdministratorView(ModelMap model) {
        return "redirect:administrator";
    }

    @RequestMapping(value = "/consultationChange2", method = RequestMethod.GET)
    public String handleConsultationView(ModelMap model) {
        return "redirect:consultation";
    }
}
