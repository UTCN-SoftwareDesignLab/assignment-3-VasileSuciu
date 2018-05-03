package demo.model.builder;

import demo.model.Consultation;
import demo.model.Patient;
import demo.model.User;

import java.sql.Date;

public class ConsultationBuilder {
    private Consultation consultation;

    public ConsultationBuilder(){
        this.consultation = new Consultation();
    }

    public ConsultationBuilder setId(Long id){
        this.consultation.setId(id);
        return this;
    }

    public ConsultationBuilder setPatient(Patient patient){
        this.consultation.setPatient(patient);
        return this;
    }

    public ConsultationBuilder setDoctor(User doctor){
        this.consultation.setDoctor(doctor);
        return this;
    }

    public ConsultationBuilder setConsultationDate(Date consultationDate){
        this.consultation.setConsultationDate(consultationDate);
        return this;
    }

    public ConsultationBuilder setObservation(String observation){
        this.consultation.setObservations(observation);
        return this;
    }

    public Consultation build() {
        return this.consultation;
    }
}
