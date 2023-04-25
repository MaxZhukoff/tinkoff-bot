package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.service.HttpLinkParser;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

import static org.jooq.impl.DSL.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final HttpLinkParser httpLinkParser;
    private final DSLContext dslContext;

    @Override
    public LinkResponse add(AddLinkRequest addLinkRequest, Long tgChatId) {
        String link = addLinkRequest.link();
        URI uri = getURI(link);

        LinkDto linkDto = httpLinkParser.parse(link);

        Long linkId;
        @Nullable Result<Record1<Object>> result = dslContext.select(field("id"))
                .from(table("link"))
                .where(field("url").eq(link))
                .fetch();

        if (result.isEmpty()) {
            @Nullable Result<Record> insertedLinkIds = dslContext.insertInto(table("link"),
                            field("url"), field("updated_at"), field("last_check_at"),
                            field("last_commit_at"), field("issues_count"), field("answer_count"))
                    .values(link, now(), now(), linkDto.lastCommitAt(), linkDto.issuesCount(), linkDto.answerCount())
                    .returning(field("id"))
                    .fetch();

            linkId = insertedLinkIds.get(0).get(field("id", Long.class));

            dslContext.insertInto(table("link_chat"), field("link_id"), field("chat_id"))
                    .values(linkId, tgChatId)
                    .execute();
        } else {
            linkId = result.get(0).get(field("id", Long.class));
            dslContext.insertInto(table("link_chat"), field("link_id"), field("chat_id"))
                    .values(linkId, tgChatId)
                    .execute();
        }

        return new LinkResponse(linkId, uri);
    }

    @Override
    public LinkResponse remove(RemoveLinkRequest removeLinkRequest, Long tgChatId) {
        String link = removeLinkRequest.link();
        URI uri = getURI(link);

        Long linkId = dslContext.deleteFrom(table("link_chat"))
                .using(table("link"))
                .where(field("link_chat.link_id").eq(field("link.id")))
                .and(field("link.url").eq(link))
                .and(field("link_chat.chat_id").eq(tgChatId))
                .returning(field("link_id", Long.class))
                .fetchOneInto(Long.class);

        return new LinkResponse(linkId, uri);
    }

    @Override
    public Link update(UpdateLinkDto link) {
        return dslContext.update(table("link"))
                .set(field("updated_at"), link.updatedAt())
                .set(field("last_check_at"), now())
                .set(field("last_commit_at"), link.lastCommitAt())
                .set(field("issues_count"), link.issuesCount())
                .set(field("answer_count"), link.answerCount())
                .where(field("id").eq(link.id()))
                .returning()
                .fetchOneInto(Link.class);
    }

    @Override
    public void updateLastCheck(Long linkId) {
        dslContext.update(table("link"))
                .set(field("last_check_at"), now())
                .where(field("id").eq(linkId))
                .execute();
    }

    @Override
    public List<Long> getChatsId(Long linkId) {
        return dslContext.select(field("chat_id"))
                .from(table("link_chat"))
                .where(field("link_id").eq(linkId))
                .fetch(field("chat_id"), Long.class);
    }

    @Override
    public ListLinksResponse getAllTrackedLinks(Long tgChatId) {
        List<LinkResponse> linkResponses = dslContext.select(field("lc.link_id").as("id"), field("l.url").as("url"))
                .from(table("link_chat").as("lc"))
                .join(table("link").as("l")).on(field("lc.link_id").eq(field("l.id")))
                .where(field("lc.chat_id").eq(tgChatId))
                .fetchInto(LinkResponse.class);
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public List<Link> getAllLinksNotUpdatedAt(Long delayUpdatedMinutes) {
        OffsetDateTime delay = OffsetDateTime.now().minusMinutes(delayUpdatedMinutes);

        return dslContext.select(field("id"), field("url"), field("updated_at").cast(OffsetDateTime.class), field("last_check_at").cast(OffsetDateTime.class),
                        field("last_commit_at").cast(OffsetDateTime.class), field("issues_count"), field("answer_count"))
                .from(table("link"))
                .where(field("last_check_at").lt(delay))
                .fetchInto(Link.class);
    }

    private URI getURI(String link) {
        try {
            return new URL(link).toURI();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Incorrect link");
        }
    }
}
