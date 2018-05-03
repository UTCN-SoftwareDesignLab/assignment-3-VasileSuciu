package demo.controllers;

import demo.model.Patient;
import demo.model.PatientDTO;
import demo.model.validation.Notification;
import demo.service.PatientManagementServiceMySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
public class SecretaryController {
    @Autowired
    private PatientManagementServiceMySQL patientManagementServiceMySQL;

    @RequestMapping(value = "/secretary", method = RequestMethod.GET)
    public String showSecretaryPage(ModelMap model){
        model.addAttribute("errorMessage3", "");
        model.addAttribute("patient", new PatientDTO());
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        return "secretary";
    }

    @RequestMapping(value = "/secretary", params="deleteBtn", method = RequestMethod.POST)
    public String handlePatientDelete(ModelMap model,  @ModelAttribute("patient") PatientDTO patient) {
        if (patient.getId() != null) {
            patientManagementServiceMySQL.deletePatient(patient.getId());
        }
        else {
            model.addAttribute("errorMessage3", "Id should not be null");
        }
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        return "secretary";
    }

    @RequestMapping(value = "/secretary", params="updateBtn", method = RequestMethod.POST)
    public String handlePatientUpdate(ModelMap model,  @ModelAttribute("patient") PatientDTO patient) {
        if (patient.getId() != null) {
            Notification<Boolean> notification = patientManagementServiceMySQL.updatePatient(patient.getId(),
                    patient.getName(), patient.getIdCardNumber(), patient.getAddress());
            if (notification.hasErrors()) {
                model.addAttribute("errorMessage3", notification.getFormattedErrors());
            } else {
                model.addAttribute("errorMessage3", "");
            }
            model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        }
        else {
            model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
            model.addAttribute("errorMessage3","ID must not be null");
        }
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        return "secretary";
    }

    @RequestMapping(value = "/secretary", params="addPatient", method = RequestMethod.POST)
    public String handlePatientCreate(ModelMap model,  @ModelAttribute("patient") PatientDTO patient, @ModelAttribute("dateOfBirthString") String dob) {
        try {
            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(simpleDateFormat.parse(patient.getDateOfBirth()).getTime());
            Notification<Boolean> notification =  patientManagementServiceMySQL.addPatient(patient.getName(),
                    patient.getIdCardNumber(),patient.getPersonalNumericalCode(),date,patient.getAddress());
            if (notification.hasErrors()){
                model.addAttribute("errorMessage3",notification.getFormattedErrors());
            }
            else{
                model.addAttribute("errorMessage3", "");
            }
        }
        catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("patientList", patientManagementServiceMySQL.getAllPatients());
        return "secretary";
    }


    @RequestMapping(value = "/secretary", params="doctorBtn", method = RequestMethod.GET)
    public String handleDoctorView(ModelMap model) {
        return "redirect:doctor";
    }

    @RequestMapping(value = "/secretary", params="adminBtn", method = RequestMethod.GET)
    public String handleAdministratorView(ModelMap model) {
        return "redirect:administrator";
    }

    @RequestMapping(value = "/secretary", params="consultationBtn", method = RequestMethod.GET)
    public String handleConsultationView(ModelMap model) {
        return "redirect:consultation";
    }

}
