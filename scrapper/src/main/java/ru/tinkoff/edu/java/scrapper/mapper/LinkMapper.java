package ru.tinkoff.edu.java.scrapper.mapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.entity.Link;

@Component
public class LinkMapper {
    public LinkResponse toLinkResponse(Link link) {
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }

    public Link toLink(LinkDto linkDto) {
        return Link.builder()
            .url(linkDto.url())
            .updatedAt(linkDto.updatedAt())
            .lastCheckAt(OffsetDateTime.now())
            .lastCommitAt(linkDto.lastCommitAt())
            .issuesCount(linkDto.issuesCount())
            .answerCount(linkDto.answerCount())
            .build();
    }

    public ListLinksResponse toListLinksResponse(List<Link> links) {
        List<LinkResponse> linkResponseList = links.stream().map(link ->
            new LinkResponse(link.getId(), URI.create(link.getUrl()))).toList();
        return new ListLinksResponse(linkResponseList, linkResponseList.size());
    }
}
