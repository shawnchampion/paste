package cloud.mnoob.task;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTask {

    @Resource
    private EntityManager entityManager;

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void clearExpiredRecords() {
        String hql = "delete from PasteRecord where expireTime < :now";
        entityManager.createQuery(hql).setParameter("now", System.currentTimeMillis()).executeUpdate();
    }
}
