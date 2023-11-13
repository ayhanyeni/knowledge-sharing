package io.intake.ayhan.knowledge;


import io.intake.ayhan.entity.Knowledge;
import io.intake.ayhan.knowledge.pojo.KnowledgeForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class KnowledgeServiceTest {

    private static final Long[] TEST_IDS = {0L, 1L};

    @Mock
    private KnowledgeRepository knowledgeRepository;

    private KnowledgeService knowledgeService;

    @BeforeEach
    public void setup() {
        knowledgeService = new KnowledgeService(knowledgeRepository);
    }

    @Test
    public void create_WithValidData_Success() {

        Mockito.when(knowledgeRepository.save(Mockito.any(Knowledge.class))).thenReturn(
                    Knowledge.builder()
                        .id(TEST_IDS[1])
                        .title("Test title")
                        .author("Ayhan")
                        .build()
                );

        KnowledgeForm form = KnowledgeForm.builder()
                .title("Test Information")
                .author("Io Tester")
                .dateMonth("April 2020")
                .viewCount(50)
                .likeCount(40)
                .link("https://ted.com/talks/test_knowledge1")
                .build();

        Long id = knowledgeService.create(form);

        Assertions.assertEquals(TEST_IDS[1], id);
        Mockito.verify(knowledgeRepository).save(Mockito.any(Knowledge.class));
    }

    @Test
    public void update_WithValidData_Success() {


        Mockito.when(knowledgeRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.of(Knowledge.builder()
                        .id(TEST_IDS[1])
                        .title("Test title")
                        .author("Ayhan")
                        .build())
        );

        KnowledgeForm form = KnowledgeForm.builder()
                .title("Test Information")
                .author("Io Tester")
                .dateMonth("April 2020")
                .viewCount(50)
                .likeCount(40)
                .link("https://ted.com/talks/test_knowledge1")
                .build();

        knowledgeService.update(TEST_IDS[1], form);

        Mockito.verify(knowledgeRepository).save(Mockito.any(Knowledge.class));
    }


    @Test
    public void delete_WithValidId_Success() {

        Mockito.when(knowledgeRepository.findById(Mockito.anyLong())).thenReturn(
                Optional.of(Knowledge.builder()
                        .id(TEST_IDS[1])
                        .title("Test title")
                        .author("Ayhan")
                        .build())
        );
        knowledgeService.delete(TEST_IDS[1]);

        Mockito.verify(knowledgeRepository).delete(Mockito.any(Knowledge.class));
    }
}
