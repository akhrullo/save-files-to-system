package uz.idev.simpleproject.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String originalFilename;
    private String contentType;
    private String generatedFilename;
    private String path;
    private long size;

    private LocalDate uploadedDate;

    @PrePersist
    public void prePersist() {
        this.uploadedDate = LocalDate.now();
    }
}
