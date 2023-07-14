package cloud.mnoob.controller;

import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.record.TextRecord;
import cloud.mnoob.model.Response;
import cloud.mnoob.model.ResponseStatus;
import cloud.mnoob.service.TextService;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@CrossOrigin
public class TextController {
    @Resource
    private TextService textService;

    @GetMapping("/copy")
    public Response<TextRecord> copy(@RequestParam String code) {
        log.debug(code);
        try {
            // 根据提取码查询粘贴记录
            TextRecord textRecord = textService.doCopy(code);
            if (textRecord == null) {
                log.error("No record with code:{}", code);
                return new Response<>(ResponseStatus.NOT_FOUND, "这是一块知识的盲区~");
            }
            log.info("Copy -> " + textRecord);
            return new Response<>(textRecord);
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }

    @PostMapping("/paste")
    public Response<TextRecord> paste(@RequestBody TextRecord record) {
        log.debug(record.getText());
        try {
            textService.doPaste(record);
            log.info("Paste -> " + record);
            return new Response<>(record);
        } catch (PasteException pe) {
            log.error(pe);
            return new Response<>(pe.getErrorCode(), pe.getErrorMessage());
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }
}
