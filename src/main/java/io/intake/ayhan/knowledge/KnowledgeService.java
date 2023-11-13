package io.intake.ayhan.knowledge;

import io.intake.ayhan.exception.ItemNotFoundException;
import io.intake.ayhan.entity.Knowledge;
import io.intake.ayhan.knowledge.pojo.KnowledgeForm;
import io.intake.ayhan.knowledge.pojo.KnowledgeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * A spring-boot service class for CRUD operations on knowledge data.
 *
 * @author Ayhan Yeni
 */

@RequiredArgsConstructor
@Service
@Transactional
public class KnowledgeService {

    final KnowledgeRepository knowledgeRepository;

    public Long create(final KnowledgeForm form) {

        Knowledge knowledge = Knowledge.builder()
                .title(form.getTitle())
                .author(form.getTitle())
                .dateMonth(form.getDateMonth())
                .viewCount(form.getViewCount())
                .likeCount(form.getLikeCount())
                .link(form.getLink())
                .build();

        knowledge = knowledgeRepository.save(knowledge);

        return knowledge.getId();
    }


    public void update(final Long id, final KnowledgeForm form) {

        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("knowledgeNotFound"));

        knowledge.setTitle(form.getTitle());
        knowledge.setAuthor(form.getTitle());
        knowledge.setDateMonth(form.getDateMonth());
        knowledge.setViewCount(form.getViewCount());
        knowledge.setLikeCount(form.getLikeCount());
        knowledge.setLink(form.getLink());

        knowledgeRepository.save(knowledge);
    }

    public void delete(final Long id) {

        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("knowledgeNotFound"));

        knowledgeRepository.delete(knowledge);
    }

    /**
     * Searches on knowledge data according to the parameters.
     *
     * @param author Input for search criteria on author field. Tries to make case-insensitive exact length match.
     * @param title Input for search criteria on title field. Tries to make case-insensitive exact length match.
     * @param minimumViews Input for search criteria on viewCount. The criteria match rows greater than or equal
     *                    to this value.
     * @param minimumLikes Input for search criteria on likeCount. The criteria match rows greater than or equal
     *      *                    to this value.
     * @param pageable  Page information to retrieve.
     * @return KnowledgeInfo list as paged list.
     */
    public Page<KnowledgeInfo> search(final String author, final String title, final Integer minimumViews,
                                      final Integer minimumLikes, final Pageable pageable) {

        Specification<Knowledge> specificationAuthor = (root, query, builder) -> author == null ? builder.conjunction() :
                builder.equal(builder.lower(root.get("author")), author.toLowerCase(Locale.ROOT));

        Specification<Knowledge> specificationTitle = (root, query, builder) -> title == null ? builder.conjunction() :
                builder.equal(builder.lower(root.get("title")), title.toLowerCase(Locale.ROOT));

        Specification<Knowledge> specificationViews = (root, query, builder) -> minimumViews == null ? builder.conjunction() :
                builder.greaterThanOrEqualTo(root.get("viewCount"), minimumViews);

        Specification<Knowledge> specificationLikes = (root, query, builder) -> minimumLikes == null ? builder.conjunction() :
                builder.greaterThanOrEqualTo(root.get("likeCount"), minimumLikes);

        Specification<Knowledge> specificationCombined = Specification.where(specificationAuthor)
                .and(specificationTitle)
                .and(specificationViews)
                .and(specificationLikes);

        return knowledgeRepository.findAll(specificationCombined, pageable)
                .map(k -> KnowledgeInfo.builder()
                        .id(k.getId())
                        .author(k.getAuthor())
                        .title(k.getTitle())
                        .dateMonth(k.getDateMonth())
                        .viewCount(k.getViewCount())
                        .likeCount(k.getLikeCount())
                        .link(k.getLink())
                        .build()
                );
    }
}
