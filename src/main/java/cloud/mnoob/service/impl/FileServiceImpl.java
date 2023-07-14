package cloud.mnoob.service.impl;

import cloud.mnoob.config.PasteConfig;
import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.ResponseStatus;
import cloud.mnoob.model.record.FileRecord;
import cloud.mnoob.repository.FileRepository;
import cloud.mnoob.service.FileService;
import cloud.mnoob.util.PasteCodeUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileRepository fileRepository;

    @Override
    public FileRecord doUpload(MultipartFile file) throws Exception {
        FileStore store = Files.getFileStore(FileSystems.getDefault().getPath(PasteConfig.getInstance().getUploadPath()));
        String filename = file.getOriginalFilename();
        log.info("开始上传文件: {}, {} MB", filename, file.getSize() / 1000 / 1000);
        long usable = store.getUsableSpace();
        long total = store.getTotalSpace();
        log.info("当前可用存储空间: {} MB / {} MB", usable / 1000 / 1000, total / 1000 / 1000);
        if ((double) usable / total < 0.2) {
            log.warn("可用空间不足20%");
            throw new PasteException(ResponseStatus.FILE_ERROR_SPACE_OUT_LIMIT);
        }

        String code;
        int count = 0;
        do {
            // 尝试次数过多返回冲突
            if (count++ > PasteConfig.getInstance().getMaxRetries()) {
                log.error("Too much retries for file:{}", filename);
                throw new PasteException(ResponseStatus.CONFLICT, "知识没有被接纳～");
            }
            // 生成提取码
            code = String.valueOf(PasteCodeUtil.getRandomCode());
            log.debug(code);
        } while (fileRepository.findById(code).isPresent());

        String storePath = PasteConfig.getInstance().getUploadPath() + "/" + UUID.randomUUID();
        File storeFile = new File(storePath);
        file.transferTo(storeFile);

        FileRecord fileRecord = new FileRecord();
        fileRecord.setCode(code);
        fileRecord.setFilename(filename);
        fileRecord.setSize(file.getSize());
        fileRecord.setPath(storePath);
        fileRecord.setCreateTime(System.currentTimeMillis());
        fileRecord.setExpireTime(fileRecord.getCreateTime() + (long) PasteConfig.getInstance().getExpireMinutes() * 60 * 1000);
        fileRepository.save(fileRecord);

        return fileRecord;
    }

    @Override
    public FileRecord getInfo(String code) {
        Optional<FileRecord> fileRecord = fileRepository.findById(code);
        return fileRecord.orElse(null);
    }

    @Override
    public void doDownload(String filename, String path, HttpServletResponse response) throws PasteException {
        File file = new File(path);
        if (!file.exists()) {
            log.error("文件不存在: {}", path);
            throw new PasteException(ResponseStatus.FILE_ERROR_FILE_NOT_EXIST);
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        try (InputStream inputStream = new FileInputStream(path);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
