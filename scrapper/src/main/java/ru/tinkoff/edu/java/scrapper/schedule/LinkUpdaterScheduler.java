package ru.tinkoff.edu.java.scrapper.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

@Slf4j
@RequiredArgsConstructor
@Service
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@schedulerInterval}")
    public void update() {
        linkUpdater.update();
        log.info("Updated");
    }
}
