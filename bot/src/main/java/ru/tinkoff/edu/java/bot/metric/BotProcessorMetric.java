package ru.tinkoff.edu.java.bot.metric;

import io.micrometer.core.instrument.Metrics;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BotProcessorMetric {
    public static void incrementHandledMessageCount() {
        Metrics.counter("handled_message_count").increment();
    }
}
