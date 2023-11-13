package io.intake.ayhan.knowledge;

import io.intake.ayhan.entity.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestKnowledgeRepository extends JpaRepository<Knowledge, Long>, JpaSpecificationExecutor<Knowledge> {
}
