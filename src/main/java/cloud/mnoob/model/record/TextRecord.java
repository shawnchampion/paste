package cloud.mnoob.model.record;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "text_record")
public class TextRecord {
    @Id
    private String code;
    private String text;
    private Long createTime;
    private Long expireTime;
}
