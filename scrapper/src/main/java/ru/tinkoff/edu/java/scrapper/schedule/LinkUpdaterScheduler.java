package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@schedulerInterval}")
    public void update() {
        log.info("Updated");
    }
}
