package io.intake.ayhan.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "KNOWLEDGE",
    indexes = {
            @Index(columnList = "AUTHOR", name = "IX_KNOWLEDGE__AUTHOR"),
            @Index(columnList = "TITLE", name = "IX_KNOWLEDGE__TITLE"),
            @Index(columnList = "VIEW_COUNT", name = "IX_KNOWLEDGE__VIEW_COUNT"),
            @Index(columnList = "LIKE_COUNT", name = "IX_KNOWLEDGE__LIKE_COUNT")
    }
)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Knowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AUTHOR", length = 100)
    private String author;

    @Column(name = "DATE_MONTH")
    private String dateMonth;

    @Column(name = "VIEW_COUNT", length = 512, nullable = false)
    private Integer viewCount;

    @Column(name = "LIKE_COUNT", nullable = false)
    private Integer likeCount;

    @Column(name = "LINK", length = 1024, nullable = false)
    private String link;
}
