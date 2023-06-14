package cloud.mnoob.repository;

import cloud.mnoob.model.PasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasteRepository extends JpaRepository<PasteRecord, String> {
}
