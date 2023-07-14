package cloud.mnoob.controller;

import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.Response;
import cloud.mnoob.model.ResponseStatus;
import cloud.mnoob.model.record.FileRecord;
import cloud.mnoob.service.FileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@CrossOrigin
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    public Response<FileRecord> upload(@RequestParam("file") MultipartFile file) {
        try {
            FileRecord fileRecord = fileService.doUpload(file);
            log.info("Upload -> {}", fileRecord);

            return new Response<>(fileRecord);
        } catch (PasteException pe) {
            log.error(pe);
            return new Response<>(pe.getErrorCode(), pe.getErrorMessage());
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }

    @GetMapping("/info")
    public Response<FileRecord> info(@RequestParam String code) {
        try {
            FileRecord fileRecord = fileService.getInfo(code);
            if (fileRecord == null) {
                log.error("No record with code:{}", code);
                return new Response<>(ResponseStatus.NOT_FOUND, "这是一块知识的盲区~");
            }
            log.info("Info -> " + fileRecord);
            return new Response<>(fileRecord);
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }

    @GetMapping("/download")
    public Response<FileRecord> download(@RequestParam String code, HttpServletResponse response) {
        FileRecord fileRecord = fileService.getInfo(code);
        if (fileRecord == null) {
            log.error("No record with code:{}", code);
            return new Response<>(ResponseStatus.NOT_FOUND, "这是一块知识的盲区~");
        }
        try {
            fileService.doDownload(fileRecord.getFilename(), fileRecord.getPath(), response);
            log.info("Download -> " + fileRecord);
            return null;
        } catch (PasteException pe) {
            log.error(pe);
            return new Response<>(pe.getErrorCode(), pe.getErrorMessage());
        } catch (Exception e) {
            log.error(e);
            return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, "知识错乱！");
        }
    }
}
