package uz.idev.simpleproject.dto.attachment;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentResponseDto {
    private String fileName;
    private String contentType;
    private long size;
    private LocalDate uploadedDate;
    private String fileDownloadUri;

}