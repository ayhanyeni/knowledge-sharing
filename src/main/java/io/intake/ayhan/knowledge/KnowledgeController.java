package io.intake.ayhan.knowledge;

import io.intake.ayhan.knowledge.pojo.KnowledgeForm;
import io.intake.ayhan.knowledge.pojo.KnowledgeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A rest controller for CRUD operations on knowledge data.
 *
 * @author Ayhan Yeni
 */

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    /**
     * Searches on knowledge data according to the parameters.
     * Users with WRITE or READ authority are allowed for this operation.
     *
     * @param author Input for search criteria on author field. Tries to make case-insensitive exact length match.
     * @param title Input for search criteria on title field. Tries to make case-insensitive exact length match.
     * @param minimumViews Input for search criteria on viewCount. The criteria match rows greater than or equal
     *                    to this value.
     * @param minimumLikes Input for search criteria on likeCount. The criteria match rows greater than or equal
     *                     to this value.
     * @param pageable  Page information to retrieve.
     * @return KnowledgeInfo list as paged list.
     */
    @PreAuthorize("hasAnyAuthority('SCOPE_READ', 'SCOPE_WRITE')")
    @GetMapping("")
    public ResponseEntity<Page<KnowledgeInfo>> search(
            @RequestParam(required = false) final String author, @RequestParam(required = false) final String title,
            @RequestParam(required = false) final Integer minimumViews,
            @RequestParam(required = false) final Integer minimumLikes, final Pageable pageable) {

        var resultPage = knowledgeService.search(author, title, minimumViews, minimumLikes, pageable);

        return ResponseEntity.ok(resultPage);
    }

    /**
     * Creates a new knowledge data in the db. Users only with WRITE authority are allowed for this operation.
     * @param form Knowledge form data.
     * @return The primary key (id) value of the newly created Knowledge entity.
     */
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("")
    public ResponseEntity<Long> create(@RequestBody final KnowledgeForm form) {

        Long id = knowledgeService.create(form);

        return ResponseEntity.ok(id);
    }

    /**
     * Updates a knowledge data in the db. Users only with WRITE authority are allowed for this operation.
     * @id Primary key value of the knowledge entity to be updated.
     * @param form Knowledge form data.
     */
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable final Long id, @Validated @RequestBody final KnowledgeForm form) {

        knowledgeService.update(id, form);

        return ResponseEntity.ok(null);
    }

    /**
     * Deletes a knowledge data in the db. Users only with WRITE authority are allowed for this operation.
     * @id Primary key value of the knowledge entity to be deleted.
     */
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {

        knowledgeService.delete(id);

        return ResponseEntity.ok(null);
    }
}
