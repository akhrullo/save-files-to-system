package uz.idev.simpleproject.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.idev.simpleproject.dto.attachment.AttachmentDto;
import uz.idev.simpleproject.dto.attachment.AttachmentResponseDto;
import uz.idev.simpleproject.entity.Attachment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttachmentMapper {
    public List<AttachmentResponseDto> convertToAttachmentResponseDto(List<Attachment> attachments) {
        return attachments.stream().map(this::convertToAttachmentResponseDto).collect(Collectors.toList());
    }


    public AttachmentResponseDto convertToAttachmentResponseDto(Attachment attachment) {
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(attachment.getGeneratedFilename())
                .toUriString();

        return AttachmentResponseDto.builder()
                .fileName(attachment.getOriginalFilename())
                .contentType(attachment.getContentType())
                .size(attachment.getSize())
                .uploadedDate(attachment.getUploadedDate())
                .fileDownloadUri(fileDownloadUri)
                .build();
    }



    public Attachment convertToStudent(AttachmentDto attachmentDto) {
        Attachment attachment = new Attachment();
        attachment.setOriginalFilename(attachmentDto.getOriginalFilename());
        attachment.setGeneratedFilename(attachmentDto.getGeneratedFilename());
        return attachment;
    }

}
