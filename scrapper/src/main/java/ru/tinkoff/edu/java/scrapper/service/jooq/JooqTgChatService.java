package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import static org.jooq.impl.DSL.*;

@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    private final DSLContext dslContext;

    @Override
    public void register(Long tgChatId) {
        dslContext.insertInto(table("chat"), field("id"))
                .values(value(tgChatId))
                .onConflictDoNothing()
                .execute();
    }

    @Override
    public void unregister(Long tgChatId) {
        dslContext.deleteFrom(table("chat"))
                .where(field("id").eq(tgChatId))
                .returning(field("id"))
                .fetchOne();
    }
}
