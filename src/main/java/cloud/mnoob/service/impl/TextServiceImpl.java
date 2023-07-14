package cloud.mnoob.service.impl;

import cloud.mnoob.config.PasteConfig;
import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.ResponseStatus;
import cloud.mnoob.model.record.TextRecord;
import cloud.mnoob.repository.TextRepository;
import cloud.mnoob.service.TextService;
import cloud.mnoob.util.PasteCodeUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TextServiceImpl implements TextService {
    @Resource
    private TextRepository textRepository;

    @Override
    public void doPaste(TextRecord record) throws PasteException {
        String code;
        int count = 0;
        do {
            // 尝试次数过多返回冲突
            if (count++ > PasteConfig.getInstance().getMaxRetries()) {
                log.error("Too much retries for text:{}", record.getText());
                throw new PasteException(ResponseStatus.CONFLICT, "知识没有被接纳～");
            }
            // 生成提取码
            code = String.valueOf(PasteCodeUtil.getRandomCode());
            log.debug(code);
        } while (textRepository.findById(code).isPresent());

        // 存入数据库
        record.setCode(code);
        record.setCreateTime(System.currentTimeMillis());
        record.setExpireTime(record.getCreateTime() + (long) PasteConfig.getInstance().getExpireMinutes() * 60 * 1000);
        textRepository.save(record);
    }

    @Override
    public TextRecord doCopy(String code) {
        Optional<TextRecord> record = textRepository.findById(code);
        return record.orElse(null);
    }
}
