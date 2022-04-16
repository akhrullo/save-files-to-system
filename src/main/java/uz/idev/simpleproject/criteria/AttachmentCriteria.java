package uz.idev.simpleproject.criteria;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachmentCriteria {
    private String filename;
    private long minSize;
    private long maxSize;
    private LocalDate fromDate;
    private LocalDate toDate;
}
