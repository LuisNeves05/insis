package resource.property;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;


    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir( final String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
