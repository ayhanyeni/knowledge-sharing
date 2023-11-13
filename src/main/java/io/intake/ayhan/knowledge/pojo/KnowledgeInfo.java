package io.intake.ayhan.knowledge.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnowledgeInfo {

    private Long id;

    private String title;

    private String author;

    private String dateMonth;

    private Integer viewCount;

    private Integer likeCount;

    private String link;
}
