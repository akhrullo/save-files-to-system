package uz.idev.simpleproject.dto.attachment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@Builder
public class AttachmentDto {
    private String originalFilename;
    private String contentType;
    private String generatedFilename;
    private String path;
    private long size;
    private Resource resource;


}
