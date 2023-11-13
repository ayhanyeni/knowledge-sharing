package io.intake.ayhan.knowledge.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnowledgeForm {

//    @Length(min = 2, max = 50)
//    @NotNull

    @NotNull
    @Size(min = 1, max = 500)
    private String title;

    @NotNull
    @Size(min = 1, max = 150)
    private String author;

    private String dateMonth;

    @NotNull
    private Integer viewCount;

    @NotNull
    private Integer likeCount;

    @Size(max = 500)
    private String link;
}
