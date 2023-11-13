package io.intake.ayhan.knowledge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.intake.ayhan.configuration.WebSecurityConfiguration;
import io.intake.ayhan.entity.Knowledge;
import io.intake.ayhan.knowledge.pojo.KnowledgeForm;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(WebSecurityConfiguration.class)
public class KnowledgeControllerIT {

    private static final Long[] TEST_IDS = {0L, 1L};

    private MockMvc mockMvc;

    @Autowired
    TestKnowledgeRepository knowledgeRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private String getTokenForUser(final String userName, final String password) throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/token")
                        .with(httpBasic(userName, password)))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void post_WhenValidDataAndValidUser_Success() throws Exception {

        String token = getTokenForUser("admin", "password");

        KnowledgeForm form = KnowledgeForm.builder()
                .title("Test Information")
                .author("Io Tester")
                .dateMonth("April 2020")
                .viewCount(50)
                .likeCount(40)
                .link("https://ted.com/talks/test_knowledge1")
                .build();


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/knowledge")
                        .header("Authorization", "Bearer " + token)
                        .content(new ObjectMapper().writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        Integer value = JsonPath.read(result.getResponse().getContentAsString(), "$");
        Long id = Long.valueOf(value);

        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Newly added Knowledge cannot be found"));

        Assertions.assertEquals(form.getTitle(), knowledge.getTitle());
        Assertions.assertEquals(form.getViewCount(), knowledge.getViewCount());
    }

    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void post_WhenValidDataAndInValidUser_Forbidden() throws Exception {

        String token = getTokenForUser("user1", "password1");

        KnowledgeForm form = KnowledgeForm.builder()
                .title("Test Information")
                .author("Io Tester")
                .dateMonth("April 2020")
                .viewCount(50)
                .likeCount(40)
                .link("https://ted.com/talks/test_knowledge1")
                .build();


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/knowledge")
                        .header("Authorization", "Bearer " + token)
                        .content(new ObjectMapper().writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void post_WhenInValidDataAndValidUser_Fail() throws Exception {

        String token = getTokenForUser("admin", "password");

        KnowledgeForm form = KnowledgeForm.builder()
                .title(null)
                .author(null)
                .dateMonth("April 2020")
                .viewCount(50)
                .likeCount(40)
                .link("https://ted.com/talks/test_knowledge1")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/knowledge")
                        .header("Authorization", "Bearer " + token)
                        .content(new ObjectMapper().writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void update_WhenValidDataAndValidUser_Success() throws Exception {

        String token = getTokenForUser("admin", "password");

        KnowledgeForm form = KnowledgeForm.builder()
                .title("Test Information")
                .author("Io Tester")
                .dateMonth("April 2020")
                .viewCount(50)
                .likeCount(40)
                .link("https://ted.com/talks/test_knowledge1")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/knowledge/{id}", TEST_IDS[1])
                        .header("Authorization", "Bearer " + token)
                        .content(new ObjectMapper().writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Knowledge knowledge = knowledgeRepository.findById(TEST_IDS[1])
                .orElseThrow(() -> new RuntimeException("Newly added Knowledge cannot be found"));

        Assertions.assertEquals(form.getTitle(), knowledge.getTitle());
        Assertions.assertEquals(form.getViewCount(), knowledge.getViewCount());
    }


    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void delete_WhenValidIdAndValidUser_Success() throws Exception {

        String token = getTokenForUser("admin", "password");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/knowledge/{id}", TEST_IDS[1])
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertTrue(knowledgeRepository.findById(TEST_IDS[1]).isEmpty());
    }

    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void delete_WhenInValidIdAndValidUser_Fail() throws Exception {

        String token = getTokenForUser("admin", "password");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/knowledge/{id}", TEST_IDS[0])
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql({"/sql/init_test_data.sql"})
    public void search_WhenValidUser_Success() throws Exception {

        String token = getTokenForUser("user1", "password1");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/knowledge?author={author}", "test author 1")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", Matchers.is(2)));
    }
}
