package cloud.mnoob.model.record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "file_record")
public class FileRecord {
    @Id
    private String code;
    private String filename;
    private Long size;
    @JsonIgnore
    private String path;
    private Long createTime;
    private Long expireTime;
}
