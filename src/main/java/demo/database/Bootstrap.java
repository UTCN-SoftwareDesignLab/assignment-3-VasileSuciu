package demo.database;

import demo.model.Patient;
import demo.model.User;
import demo.model.validation.Notification;
import demo.service.ConsultationManagementServiceMySQL;
import demo.service.PatientManagementServiceMySQL;
import demo.service.UserManagementServiceMySQL;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;

@Service
public class Bootstrap implements InitializingBean{
    @Autowired
    private UserManagementServiceMySQL userManagementServiceMySQL;
    @Autowired
    private ConsultationManagementServiceMySQL consultationManagementService;
    @Autowired
    private PatientManagementServiceMySQL patientManagementServiceMySQL;

    @Override
    @Transactional()
    public void afterPropertiesSet() throws Exception {
        bootstrapUsers();
        bootstrapPatients();
        bootstrapConsultation();
    }

    private void bootstrapUsers(){
        System.out.println("Bootstrapping users");
        userManagementServiceMySQL.register("admin@UT.com","Administrator", "LongPassword!1",Constants.Roles.ADMINISTRATOR);
        userManagementServiceMySQL.register("secretary@UT.com","Secretary", "LongPassword!1",Constants.Roles.SECRETARY);
        userManagementServiceMySQL.register("doctor@UT.com","Doctor", "LongPassword!1",Constants.Roles.DOCTOR);
        System.out.println("Done bootstrapping users");
    }

    private void bootstrapPatients(){
        System.out.println("Bootstrapping patients");
        patientManagementServiceMySQL.addPatient("PatientOne","ax123", "12345",
                new Date(System.currentTimeMillis()-3600*24*365*20),"Alba Iulia");
        patientManagementServiceMySQL.addPatient("PatientTwo","cx123", "12346",
                new Date(System.currentTimeMillis()-3600*24*365*30),"Cluj Napoca");
        System.out.println("Done bootstrapping patients");
    }

    private void bootstrapConsultation(){
        System.out.println("Bootstrapping consultations");
        User user = userManagementServiceMySQL.getAllDoctors().get(0);
        Patient patient1 = patientManagementServiceMySQL.getAllPatients().get(0);
        Patient patient2 = patientManagementServiceMySQL.getAllPatients().get(1);
        consultationManagementService.makeAppointment(patient1.getId(), user.getId(), new Date(System.currentTimeMillis()+3600*24*2));
        consultationManagementService.makeAppointment(patient2.getId(), user.getId(), new Date(System.currentTimeMillis()+3600*24*3));
        System.out.println("Done bootstrapping consultation");
    }



}
