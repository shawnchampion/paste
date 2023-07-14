package cloud.mnoob.repository;

import cloud.mnoob.model.record.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileRecord, String> {
    List<FileRecord> findByExpireTimeLessThan(long expireTime);
}
