package demo.websocket;

import demo.model.Consultation;
import demo.model.validation.Notification;
import demo.service.ConsultationManagementServiceMySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
public class GreetingController {

    @Autowired
    ConsultationManagementServiceMySQL consultationManagementServiceMySQL;

    @MessageMapping("/appointment")
    @SendTo("/message/appointment")
    public Greeting greeting(AppointmentMessage message) throws Exception {
        System.out.println("We are here!");
        //Thread.sleep(1000); // simulated delay
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(simpleDateFormat.parse(message.getAppointmentDate()).getTime());
        Notification<Consultation> notification = consultationManagementServiceMySQL.checkAppointmnet(message.getPatientID(),
                message.getDoctorID(), date);
        if (notification.hasErrors()){
            return null;
        }
        //return new Greeting(HtmlUtils.htmlEscape(message.getDoctorID() + " " +
               // message.getPatientID() + " " + message.getAppointmentDate()) );
        System.out.println("this is not null");
        Consultation consultation = notification.getResult();
        String text = "Appointment with "+ consultation.getPatient().getName() + " on " +
                consultation.getConsultationDate();
        return  new Greeting(text);
    }

}