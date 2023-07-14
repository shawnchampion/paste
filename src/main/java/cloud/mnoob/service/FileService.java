package cloud.mnoob.service;

import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.record.FileRecord;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileRecord doUpload(MultipartFile file) throws Exception;
    FileRecord getInfo(String code);
    void doDownload(String filename, String path, HttpServletResponse response) throws PasteException;
}
