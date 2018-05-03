package demo.model.builder;

import demo.model.Patient;

import java.sql.Date;

public class PatientBuilder {
    private Patient patient;

    public  PatientBuilder(){
        this.patient = new Patient();
    }

    public PatientBuilder setId(Long id){
        patient.setId(id);
        return this;
    }

    public PatientBuilder setName(String name){
        patient.setName(name);
        return this;
    }

    public PatientBuilder setIdCardNumber(String idCardNumber){
        patient.setIdCardNumber(idCardNumber);
        return this;
    }

    public PatientBuilder setPersonalNumericalNumber(String personalNumericalNumber){
        patient.setPersonalNumericalCode(personalNumericalNumber);
        return this;
    }

    public PatientBuilder setDateOfBirth(Date dateOfBirth){
        patient.setDateOfBirth(dateOfBirth);
        return this;
    }

    public PatientBuilder setAddress(String address){
        patient.setAddress(address);
        return this;
    }

    public Patient build() {
        return this.patient;
    }
}
