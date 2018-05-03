package demo.repository;

import demo.model.Consultation;
import demo.model.Patient;
import demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsultationRepository extends CrudRepository<Consultation,Long> {
    List<Consultation> findByPatient(Patient patient);

    List<Consultation> findByDoctor(User doctor);

    List<Consultation> findByConsultationDate(Date consultationDate);

}
