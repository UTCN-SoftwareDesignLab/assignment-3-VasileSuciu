package demo.controllers;

import demo.model.Consultation;
import demo.model.ConsultationDTO;
import demo.model.Patient;
import demo.model.User;
import demo.model.validation.Notification;
import demo.service.ConsultationManagementServiceMySQL;
import demo.service.PatientManagementServiceMySQL;
import demo.service.UserManagementServiceMySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import java.text.SimpleDateFormat;

@Controller
public class ConsultationController {
    @Autowired
    private ConsultationManagementServiceMySQL consultationManagementServiceMySQL;

    @Autowired
    private UserManagementServiceMySQL userManagementServiceMySQL;

    @Autowired
    private PatientManagementServiceMySQL patientManagementServiceMySQL;

    @RequestMapping(value = "/consultation", method = RequestMethod.GET)
    public String showConsultationPage(ModelMap model){
        model.addAttribute("errorMessage", "");
        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        model.addAttribute("consultationDTO", new ConsultationDTO());
        return "consultation";
    }

    /*
    @RequestMapping(value = "/consultation", params="deleteBtn", method = RequestMethod.POST)
    public String handleConsultationDelete(ModelMap model,  @ModelAttribute("consultationDTO") ConsultationDTO consultation) {
        if (consultation.getConsultationID()!=null) {
            consultationManagementServiceMySQL.deleteAppointment(consultation.getConsultationID());
        }
        else {
            model.addAttribute("errorMessage", "ID must not be null!");
        }

        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        return "consultation";
    }

    @RequestMapping(value = "/consultation", params="updateBtn", method = RequestMethod.POST)
    public String handleConsultationUpdate(ModelMap model,  @ModelAttribute("consultationDTO") ConsultationDTO consultation) {
        try {
            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(simpleDateFormat.parse(consultation.getConsultationDate()).getTime());
            if (consultation.getConsultationID() != null) {
                System.out.println(date.getTime());
                System.out.println(date.toString());
                Notification<Boolean> notification = consultationManagementServiceMySQL.updateAppointment(consultation.getConsultationID(),
                        date);
                if (notification.hasErrors()) {
                    model.addAttribute("errorMessage", notification.getFormattedErrors());
                } else {
                    model.addAttribute("errorMessage", "");
                }
                model.addAttribute("consultationList", consultationManagementServiceMySQL.getAllConsultations());
            } else {
                model.addAttribute("errorMessage", "ID must not be null");
            }
        }
        catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        return "consultation";
    }


    @RequestMapping(value = "/consultation",  params="createBtn",method = RequestMethod.POST)
    public String handlePatientCreateOld(ModelMap model,  @ModelAttribute("consultationDTO") ConsultationDTO consultation) {
        System.out.println("Old method");
        try {
            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(simpleDateFormat.parse(consultation.getConsultationDate()).getTime());
            Notification<Boolean> notification = consultationManagementServiceMySQL.makeAppointment(consultation.getPatientID(),
                    consultation.getDoctorID(), date);
            if (notification.hasErrors()) {
                model.addAttribute("errorMessage", notification.getFormattedErrors());
            } else {
                model.addAttribute("errorMessage", "");
            }
            model.addAttribute("consultationList", consultationManagementServiceMySQL.getAllConsultations());

        }
        catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        return "consultation";
    }
    */


    @RequestMapping(value = "/doctorChange", method = RequestMethod.GET)
    public String handleDoctorView(ModelMap model) {
        return "redirect:doctor";
    }

    @RequestMapping(value = "/administratorChange", method = RequestMethod.GET)
    public String handleAdministratorView(ModelMap model) {
        return "redirect:administrator";
    }

    @RequestMapping(value = "/secretaryChange",  method = RequestMethod.GET)
    public String handleConsultationView(ModelMap model) {
        return "redirect:secretary";
    }

    @RequestMapping(value = "/makeAppointment", method = RequestMethod.GET)
    public String handleConsultationCreate(ModelMap model, @RequestParam("doctorID") Long doctorID,
                                      @RequestParam("patientID") Long patientID,  @RequestParam("date") String appointmentDate, @ModelAttribute("consultationDTO") ConsultationDTO consultationDTO) {
        try {
            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(simpleDateFormat.parse(appointmentDate).getTime());
            Notification<Boolean> notification = consultationManagementServiceMySQL.makeAppointment(patientID,
                    doctorID, date);
            if (notification.hasErrors()) {
                //System.out.println(notification.getFormattedErrors());
                model.addAttribute("errorMessage", notification.getFormattedErrors());
            } else {
                model.addAttribute("errorMessage", "");
            }
            model.addAttribute("consultationList", consultationManagementServiceMySQL.getAllConsultations());
            System.out.println(notification.getFormattedErrors());
        }
        catch (Exception e){
            //e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        return "consultation";
    }

    @RequestMapping(value = "/updateAppointment", method = RequestMethod.GET)
    public String handleConsultationUpdate(ModelMap model, @RequestParam("consultationID") Long consultationID,
                                           @RequestParam("date") String appointmentDate, @ModelAttribute("consultationDTO") ConsultationDTO consultationDTO) {
        try {
            String pattern = "dd.MM.yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(simpleDateFormat.parse(appointmentDate).getTime());
            if (consultationID != null) {
                //System.out.println(date.getTime());
                //System.out.println(date.toString());
                Notification<Boolean> notification = consultationManagementServiceMySQL.updateAppointment(consultationID,
                        date);
                if (notification.hasErrors()) {
                    model.addAttribute("errorMessage", notification.getFormattedErrors());
                } else {
                    model.addAttribute("errorMessage", "");
                }
                model.addAttribute("consultationList", consultationManagementServiceMySQL.getAllConsultations());
            } else {
                model.addAttribute("errorMessage", "ID must not be null");
            }
        }
        catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        return "consultation";
    }

    @RequestMapping(value = "/deleteAppointment", method = RequestMethod.GET)
    public String handleConsultationDelete(ModelMap model,  @RequestParam("consultationID") Long consultationID, @ModelAttribute("consultationDTO") ConsultationDTO consultationDTO) {
        //System.out.println(consultationID+"something");
        //System.out.println(consultationDTO.getConsultationID());
        if (consultationID!=null) {
            consultationManagementServiceMySQL.deleteAppointment(consultationID);
        }
        else {
            model.addAttribute("errorMessage", "ID must not be null!");
        }

        model.addAttribute("doctorList", userManagementServiceMySQL.getAllDoctors());
        model.addAttribute("patientList",patientManagementServiceMySQL.getAllPatients());
        model.addAttribute("consultationList",consultationManagementServiceMySQL.getAllConsultations());
        return "consultation";
    }
}
