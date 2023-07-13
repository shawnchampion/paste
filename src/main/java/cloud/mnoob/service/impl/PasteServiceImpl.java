package cloud.mnoob.service.impl;

import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.PasteRecord;
import cloud.mnoob.model.ResponseStatus;
import cloud.mnoob.repository.PasteRepository;
import cloud.mnoob.service.PasteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class PasteServiceImpl implements PasteService {
    @Resource
    private PasteRepository pasteRepository;

    // 粘贴内容过期时间（单位：分钟）
    private final static int EXPIRE_MINUTE = 15;
    // 生成提取码允许最大冲突次数
    private final static int MAX_RETRY = 10;
    // 数字提取码位数
    private final static int CODE_LENGTH = 6;

    @Override
    public void doPaste(PasteRecord record) throws PasteException {
        String code;
        int count = 0;
        do {
            // 尝试次数过多返回冲突
            if (count++ > MAX_RETRY) {
                log.error("Too much retries for text:{}", record.getText());
                throw new PasteException(ResponseStatus.CONFLICT, "知识没有被接纳～");
            }
            // 生成提取码
            code = String.valueOf(getRandomCode());
            log.debug(code);
        } while (pasteRepository.findById(code).isPresent());

        // 存入数据库
        record.setCode(code);
        record.setCreateTime(System.currentTimeMillis());
        record.setExpireTime(record.getCreateTime() + EXPIRE_MINUTE * 60 * 1000);
        pasteRepository.save(record);
    }

    @Override
    public PasteRecord doCopy(String code) {
        Optional<PasteRecord> record = pasteRepository.findById(code);
        return record.orElse(null);
    }

    private int getRandomCode() {
        int origin = (int) Math.pow(10, CODE_LENGTH - 1);
        return new Random().nextInt(origin, origin * 10);
    }
}
