package io.intake.ayhan.knowledge;

import io.intake.ayhan.entity.Knowledge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class KnowledgeDataFileLoader implements CommandLineRunner {

    private static final String COMMA_DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    private final ApplicationContext appContext;
    private final KnowledgeRepository knowledgeRepository;

    @Override
    public void run(String...args) throws Exception {

        if (args.length > 0) {
            loadDataCsv(args[0]);
        }
    }

    private void loadDataCsv(final String dataFilePath) {

        boolean isFailed = false;

        if (dataFilePath.toLowerCase().endsWith(".csv")) {
            try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
                String line;
                int lineNo = 1;
                while ((line = br.readLine()) != null) {

                    String[] values = line.split(COMMA_DELIMITER);

                    if (values.length == 6) {
                        try {
                            Knowledge knowledge = Knowledge.builder()
                                    .title(values[0])
                                    .author(values[1])
                                    .dateMonth(values[2])
                                    .viewCount(Integer.parseInt(values[3]))
                                    .likeCount(Integer.parseInt(values[4]))
                                    .link(values[5])
                                    .build();

                            knowledgeRepository.save(knowledge);
                        } catch (NumberFormatException e) {
                            log.error("Cannot parse the line " + lineNo);
                        }
                    } else {
                        log.error("Cannot parse the line " + lineNo);
                    }

                    lineNo++;
                }

                log.info(lineNo + " entries are loaded from file " + dataFilePath);
            } catch (IOException e) {
                log.error("File cannot be opened: " + dataFilePath);
                isFailed = true;
            }
        } else {
            log.error("Invalid knowledge data file. The data file name must to have .csv extension.");
            isFailed = true;
        }

        if (isFailed) {
            SpringApplication.exit(appContext, () -> -1);
        }
    }
}
