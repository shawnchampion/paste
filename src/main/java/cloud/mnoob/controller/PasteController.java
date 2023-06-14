package cloud.mnoob.controller;

import cloud.mnoob.model.PasteRecord;
import cloud.mnoob.model.Response;
import cloud.mnoob.model.ResponseStatus;
import cloud.mnoob.repository.PasteRepository;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@Log4j2
@CrossOrigin
public class PasteController {
    @Resource
    private PasteRepository pasteRepository;

    // 粘贴内容过期时间（单位：分钟）
    private final static int EXPIRE_MINUTE = 15;
    // 生成提取码允许最大冲突次数
    private final static int MAX_RETRY = 10;
    // 数字提取码位数
    private final static int CODE_LENGTH = 6;

    @GetMapping("/copy")
    public Response<PasteRecord> copy(@RequestParam String code) {
        log.debug(code);
        try {
            // 根据提取码查询粘贴记录
            Optional<PasteRecord> record = pasteRepository.findById(code);
            if (record.isPresent()) {
                log.info("Copy -> " + record.get());
                return new Response<>(record.get());
            } else {
                log.error("No record with code:{}", code);
                return new Response<>(ResponseStatus.NOT_FOUND, "这是一块知识的盲区~");
            }
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }

    @PostMapping("/paste")
    public Response<PasteRecord> paste(@RequestBody PasteRecord record) {
        log.debug(record.getText());
        try {
            String code;
            int count = 0;
            do {
                // 尝试次数过多返回冲突
                if (count++ > MAX_RETRY) {
                    log.error("Too much retries for text:{}", record.getText());
                    return new Response<>(ResponseStatus.CONFLICT, "知识没有被接纳～");
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

            log.info("Paste -> " + record);
            return new Response<>(record);
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }

    private int getRandomCode() {
        int origin = (int) Math.pow(10, CODE_LENGTH - 1);
        return new Random().nextInt(origin, origin * 10);
    }
}
