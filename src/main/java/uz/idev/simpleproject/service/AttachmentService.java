package uz.idev.simpleproject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.idev.simpleproject.criteria.AttachmentCriteria;
import uz.idev.simpleproject.dto.attachment.AttachmentResponseDto;
import uz.idev.simpleproject.entity.Attachment;
import uz.idev.simpleproject.mapper.AttachmentMapper;
import uz.idev.simpleproject.repository.AttachmentRepository;

import javax.persistence.criteria.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;
    private final AttachmentMapper mapper;

    public AttachmentService(AttachmentRepository attachmentRepository, FileStorageService fileStorageService, AttachmentMapper mapper) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
        this.mapper = mapper;
    }

    public AttachmentResponseDto save(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();

        MultipartFile file = request.getFile(fileNames.next());
        if (Objects.isNull(file)) {
            throw new RuntimeException("File can not be null");
        }
        Attachment attachment = fileStorageService.store(file);
        attachmentRepository.save(attachment);

        return mapper.convertToAttachmentResponseDto(attachment);
    }

    public List<AttachmentResponseDto> getAttachments(AttachmentCriteria criteria, Pageable pageable) {
        List<Attachment> attachments = attachmentRepository.findAll((Specification<Attachment>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();

            if (Objects.nonNull(criteria.getFromDate()) && Objects.nonNull(criteria.getToDate()) && criteria.getFromDate().isBefore(criteria.getToDate())) {
                p = cb.and(p, cb.between(root.get("uploadedDate"), criteria.getFromDate(), criteria.getToDate()));
            }
            if (criteria.getMaxSize() > criteria.getMinSize()) {
                p = cb.and(p, cb.between(root.get("size"), criteria.getMinSize(), criteria.getMaxSize()));
            }
            if (!StringUtils.isEmpty(criteria.getFilename())) {
                p = cb.and(p, cb.like(root.get("originalFilename"), "%" + criteria.getFilename() + "%"));
            }

            cq.orderBy(cb.desc(root.get("originalFilename")), cb.asc(root.get("id")));
            return p;

        }, pageable).getContent();

        return mapper.convertToAttachmentResponseDto(attachments);
    }

}
