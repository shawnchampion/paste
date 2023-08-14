package cloud.mnoob.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("paste")
public class PasteConfig {
    private static PasteConfig instance;

    // 数字提取码位数
    private int codeLength = 6;
    // 粘贴内容过期时间（单位：分钟）
    private int expireMinutes = 15;
    // 生成提取码允许最大冲突次数
    private int maxRetries = 10;
    // 文件上传位置
    private String uploadPath = "/root/paste/files";

    @PostConstruct
    private void initialize() {
        instance = this;
    }

    public static PasteConfig getInstance() {
        return instance;
    }
}
