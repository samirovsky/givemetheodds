package com.giskard.odds.command;

import com.giskard.odds.api.exception.CannotCalculateOddsCheckedException;
import com.giskard.odds.infrastructure.service.FileDeserializer;
import com.giskard.odds.service.JourneyOddsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static com.giskard.odds.tools.MultipartFileTools.getExtMultipartFile;

@Slf4j
@Component
@Command(name = "give-me-the-odds", mixinStandardHelpOptions = true)
public class OddsCommand implements Callable<Integer> {
    @Autowired
    private JourneyOddsService journeyOddsService;

    @Autowired
    private FileDeserializer fileDeserializer;

    @Autowired
    private FileDeserializer objectMapper;
    @Parameters(description = "positional params")
    private List<String> positionals;

    @Override
    public Integer call() throws IOException, CannotCalculateOddsCheckedException {
        MultipartFile empire = getExtMultipartFile(positionals.get(0), "empire");
        MultipartFile config = getExtMultipartFile(positionals.get(1), "config");
        double odds = journeyOddsService.calculateOdds(config, empire);
        log.info("The odds are: " + odds + "%");
        return 0;
    }
}