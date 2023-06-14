package cloud.mnoob.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "record")
public class PasteRecord {
    @Id
    private String code;
    private String text;
    private Long createTime;
    private Long expireTime;
}
