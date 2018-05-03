package service;

import demo.model.Consultation;
import demo.model.Patient;
import demo.model.User;
import demo.model.builder.ConsultationBuilder;
import demo.model.builder.PatientBuilder;
import demo.model.builder.UserBuilder;
import demo.repository.ConsultationRepository;
import demo.repository.PatientRepository;
import demo.repository.UserRepository;
import demo.service.ConsultationManagementServiceMySQL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ConsultationManagementServiceMySQLTest {
    @Mock
    ConsultationRepository consultationRepository;

    @Mock
    PatientRepository patientRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ConsultationManagementServiceMySQL consultationManagementServiceMySQL;

    private Consultation consultation;

    @Before
    public void init() {
        Patient patient = new PatientBuilder()
                .setId(1L)
                .setName("patient")
                .setAddress("Cluj")
                .setPersonalNumericalNumber("12345")
                .setIdCardNumber("cx213")
                .setDateOfBirth(new Date(System.currentTimeMillis()))
                .build();
        User doctor = new UserBuilder()
                .setId(1L)
                .setName("doctor")
                .setPassword("LongPassword!1")
                .setUsername("doctor@UT.com")
                .setRoles("doctor")
                .build();
        consultation = new ConsultationBuilder()
                .setId(1L)
                .setDoctor(doctor)
                .setPatient(patient)
                .setConsultationDate(new Date(System.currentTimeMillis()+3600*24*30))
                .build();
    }

    @Test
    public void getAppointmentsForDoctorTest(){
        when(userRepository.findById(consultation.getDoctor().getId())).thenReturn(Optional.of(consultation.getDoctor()));
        when(consultationRepository.findByDoctor(consultation.getDoctor())).thenReturn(new ArrayList<>());
        Assert.assertFalse(consultationManagementServiceMySQL.getAppointmentsForDoctor(
                consultation.getDoctor().getId()).hasErrors());
    }

    @Test
    public void getAppointmentsForPatientTest(){
        when(patientRepository.findById(consultation.getPatient().getId())).thenReturn(Optional.of(consultation.getPatient()));
        when(consultationRepository.findByPatient(consultation.getPatient())).thenReturn(new ArrayList<>());
        Assert.assertFalse(consultationManagementServiceMySQL.getAppointmentsForPatient(
                consultation.getPatient().getId()).hasErrors());
    }

    @Test
    public void makeAppointmentTest(){
        when(userRepository.findById(consultation.getDoctor().getId())).thenReturn(Optional.of(consultation.getDoctor()));
        when(patientRepository.findById(consultation.getPatient().getId())).thenReturn(Optional.of(consultation.getPatient()));
        System.out.println(consultationManagementServiceMySQL.makeAppointment(
                consultation.getPatient().getId(), consultation.getDoctor().getId(), consultation.getConsultationDate()).getFormattedErrors());
        Assert.assertFalse(consultationManagementServiceMySQL.makeAppointment(
                consultation.getPatient().getId(), consultation.getDoctor().getId(), consultation.getConsultationDate()).hasErrors());
    }

    @Test
    public void updateAppointmentTest(){
        when(consultationRepository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        Assert.assertFalse(consultationManagementServiceMySQL
        .updateAppointment(consultation.getId(),new Date(System.currentTimeMillis()+24*3600000*5)).hasErrors());

    }

    @Test
    public void deleteAppointmentTest(){
        when(consultationRepository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        Assert.assertTrue(consultationManagementServiceMySQL.deleteAppointment(consultation.getId()));
    }

    @Test
    public void addConsultationObservationTest(){
        when(consultationRepository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        Assert.assertFalse(consultationManagementServiceMySQL
                .addConsultationObservation(consultation.getId(),"Test Observation").hasErrors());
    }

    @Test
    public void getAllConsultationsTest(){
        when(consultationRepository.findAll()).thenReturn(new ArrayList<>());
        Assert.assertEquals(consultationManagementServiceMySQL.getAllConsultations().size(),0);
    }

    @Test
    public void getConsultationTest(){
        when(consultationRepository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        Assert.assertNotNull(consultationManagementServiceMySQL.getConsultation(consultation.getId()));
    }
}
