package cloud.mnoob.task;

import cloud.mnoob.model.record.FileRecord;
import cloud.mnoob.repository.FileRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    @Resource
    private EntityManager entityManager;
    @Resource
    private FileRepository fileRepository;

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void cleanExpiredTextRecords() {
        String hql = "delete from TextRecord where expireTime < :now";
        entityManager.createQuery(hql).setParameter("now", System.currentTimeMillis()).executeUpdate();
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void cleanExpiredFileRecords() {
        List<FileRecord> fileRecordList = fileRepository.findByExpireTimeLessThan(System.currentTimeMillis());
        for (FileRecord fileRecord : fileRecordList) {
            File file = new File(fileRecord.getPath());
            if (file.exists()) {
                if (file.delete()) {
                    log.info("Clean -> delete file succeed: {}", fileRecord);
                } else {
                    log.error("Clean -> delete file failed: {}", fileRecord);
                    continue;
                }
            } else {
                log.error("Clean -> file not exist: {}", fileRecord);
            }
            fileRepository.delete(fileRecord);
        }
    }
}
