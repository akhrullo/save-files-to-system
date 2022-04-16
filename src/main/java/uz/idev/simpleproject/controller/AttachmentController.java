package uz.idev.simpleproject.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.idev.simpleproject.criteria.AttachmentCriteria;
import uz.idev.simpleproject.dto.attachment.AttachmentDto;
import uz.idev.simpleproject.dto.attachment.AttachmentResponseDto;
import uz.idev.simpleproject.service.AttachmentService;
import uz.idev.simpleproject.service.FileStorageService;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public record AttachmentController(FileStorageService fileStorageService,
                                   AttachmentService attachmentService) {

    @PostMapping("/upload")
    public ResponseEntity<AttachmentResponseDto> uploadFile(MultipartHttpServletRequest request) {
        return ResponseEntity.ok(attachmentService.save(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<AttachmentResponseDto>> getAttachments(@RequestBody AttachmentCriteria criteria,
                                                                      Pageable pageable){
        return ResponseEntity.ok(attachmentService.getAttachments(criteria, pageable));
    }

    @GetMapping(value = "/download/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        AttachmentDto attachmentDto = fileStorageService.loadResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentDto.getOriginalFilename() + "\"")
                .body(attachmentDto.getResource());

    }

}
