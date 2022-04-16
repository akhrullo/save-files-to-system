package uz.idev.simpleproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.idev.simpleproject.dto.attachment.AttachmentDto;
import uz.idev.simpleproject.entity.Attachment;
import uz.idev.simpleproject.exception.NotFoundException;
import uz.idev.simpleproject.repository.AttachmentRepository;
import uz.idev.simpleproject.utils.Utils;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    public static final String UPLOADS_PATH = "uploads";
    private final Utils utils;
    private final AttachmentRepository attachmentRepository;


    public Attachment store(@NonNull MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String generatedName = System.currentTimeMillis() + utils.encodeToMd5(Objects.requireNonNull(originalFilename)) + "." + extension;

        Path rootPath = getGeneratedPath(generatedName);
        try {
            Files.copy(file.getInputStream(), rootPath, StandardCopyOption.REPLACE_EXISTING);
            return Attachment.builder()
                    .originalFilename(originalFilename)
                    .generatedFilename(generatedName)
                    .contentType(file.getContentType())
                    .path(rootPath.toString())
                    .size(file.getSize())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error:  " + e.getMessage());
        }

    }

    public Path getGeneratedPath(String generatedName) {
        Path dailyPath = getPaths();
        if (!Files.exists(dailyPath)) {
            try {
                Files.createDirectories(dailyPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dailyPath.resolve(generatedName);
    }

    private Path getPaths() {
        LocalDate now = LocalDate.now();
        return Paths.get(FileStorageService.UPLOADS_PATH,
                String.valueOf(now.getYear()),
                String.valueOf(now.getMonthValue()),
                String.valueOf(now.getDayOfMonth()));
    }

    public AttachmentDto loadResource(@NonNull String fileName) {
        Attachment attachment = attachmentRepository.findByGeneratedFilename(fileName).orElseThrow(() -> {
            throw new NotFoundException("file not found");
        });

        FileSystemResource resource = new FileSystemResource(attachment.getPath());

        return AttachmentDto.builder()
                .resource(resource)
                .originalFilename(attachment.getOriginalFilename())
                .generatedFilename(attachment.getGeneratedFilename())
                .contentType(attachment.getContentType())
                .size(attachment.getSize()).build();
    }
}
