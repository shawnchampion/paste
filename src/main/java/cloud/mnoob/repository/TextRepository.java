package cloud.mnoob.repository;

import cloud.mnoob.model.record.TextRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<TextRecord, String> {
}
