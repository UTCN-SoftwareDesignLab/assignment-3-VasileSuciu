package service;

import demo.model.Patient;
import demo.model.builder.PatientBuilder;
import demo.repository.PatientRepository;
import demo.service.PatientManagementServiceMySQL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PatientManagementServiceMySQLTest {
    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientManagementServiceMySQL patientManagementServiceMySQL;

    Patient patient;

    @Before
    public void init() {
        patient = new PatientBuilder()
                .setId(1L)
                .setName("patient")
                .setAddress("Cluj")
                .setPersonalNumericalNumber("12345")
                .setIdCardNumber("cx213")
                .setDateOfBirth(new Date(System.currentTimeMillis()))
                .build();
    }

    @Test
    public void addPatientTest(){
        Assert.assertFalse(patientManagementServiceMySQL.addPatient(
                patient.getName(), patient.getIdCardNumber(), patient.getPersonalNumericalCode(),
                patient.getDateOfBirth(), patient.getAddress()).hasErrors());
    }

    @Test
    public void updatePatientTest(){
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        Assert.assertFalse(patientManagementServiceMySQL.updatePatient(
                patient.getId(), "name", patient.getIdCardNumber(), patient.getAddress()
        ).hasErrors());
    }

    @Test
    public void deletePatientTest(){
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        Assert.assertTrue(patientManagementServiceMySQL.deletePatient(patient.getId()));
    }

    @Test
    public void getPatientTest(){
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        Assert.assertNotNull(patientManagementServiceMySQL.getPatient(patient.getId()));
    }

    @Test
    public void getAllPatients(){
        List<Patient> patientList = new ArrayList<Patient>();
        when(patientRepository.findAll()).thenReturn(patientList);
        Assert.assertEquals(patientManagementServiceMySQL.getAllPatients().size(),0);
    }
}
