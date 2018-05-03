package demo.model.validation;

import demo.database.Constants;
import demo.model.Consultation;
import demo.model.Patient;
import demo.model.User;
import demo.repository.ConsultationRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ConsultationValidator {
    private Consultation consultation;
    private List<String> errors;
    private ConsultationRepository consultationRepository;

    public ConsultationValidator(Consultation consultation, ConsultationRepository consultationRepository){
        this.consultation = consultation;
        this.errors = new ArrayList<String>();
        this.consultationRepository = consultationRepository;
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public List<String> getErrors(){
        return errors;
    }

    public boolean validate(){
        validateDoctor(consultation.getDoctor());
        validatePatient(consultation.getPatient());
        validateConsultationDate(consultation.getConsultationDate());
        return errors.isEmpty();
    }

    private void validateDoctor(User doctor){
        if (doctor == null ||
                !doctor.getRoles().contains(Constants.Roles.DOCTOR)){
            errors.add("No such doctor!");
        }
    }

    private void validateConsultationDate(Date consultationDate){
        if (consultationDate == null){
            errors.add("Consultation date cannot be null");
        }
        else{
            if (consultationDate.before(new Date(System.currentTimeMillis()))){
                System.out.println(consultationDate);
                errors.add("Consultation cannot be set before today");
            }
            else{
                if (errors.isEmpty()){
                    List<Consultation> consultationList = consultationRepository.findByDoctor(consultation.getDoctor());
                    for (Consultation c : consultationList) {
                        if (c.getConsultationDate().getTime() == consultationDate.getTime()) {
                            //System.out.println(c.getConsultationDate().getTime());
                            //System.out.println(c.getConsultationDate().toString());
                            //System.out.println(consultationDate.getTime());
                            //System.out.println(consultationDate.toString());
                            errors.add("Doctor not available at this date");
                            return;
                        }
                    }
                }
            }
        }
    }

    private void validatePatient(Patient patient){
        if (patient == null){
            errors.add("Patient cannot be null");
        }
    }
}
