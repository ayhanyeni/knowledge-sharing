package io.intake.ayhan.knowledge;

import io.intake.ayhan.entity.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Long>, JpaSpecificationExecutor<Knowledge> {

    Page<Knowledge> findAll(@Nullable Specification<Knowledge> spec, Pageable pageable);

}
