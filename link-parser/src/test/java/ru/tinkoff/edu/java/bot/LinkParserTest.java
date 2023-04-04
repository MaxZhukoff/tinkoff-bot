package ru.tinkoff.edu.java.bot;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.parser.LinkParser;
import ru.tinkoff.edu.java.parser.dto.GitHubLinkDto;
import ru.tinkoff.edu.java.parser.dto.LinkDto;
import ru.tinkoff.edu.java.parser.dto.StackOverflowLinkDto;
import ru.tinkoff.edu.java.parser.handler.GitHubLinkHandler;
import ru.tinkoff.edu.java.parser.handler.LinkHandler;
import ru.tinkoff.edu.java.parser.handler.StackOverflowLinkHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinkParserTest {
    @Test
    void parseLink_shouldParseValidLinkToLinkDto() {
        //given
        List<LinkHandler> handlers = List.of(new GitHubLinkHandler(), new StackOverflowLinkHandler());
        LinkParser linkParser = new LinkParser(handlers);

        //when
        LinkDto gitHubLinkDto = linkParser.parseLink("https://github.com/MaxZhukoff/tinkoff-bot/tree/hw3/bot/src/main");
        LinkDto gitHubLinkDto2 = linkParser.parseLink("https://github.com/MaxZhukoff/tinkoff-bot");
        LinkDto stackOverflowLinkDto = linkParser.parseLink("https://stackoverflow.com/questions/75924506/clang-cant-find-avr-pgmspace-h-in-nvim-with-coc");

        //then
        assertAll("Assert response LinkDto",
                () -> assertEquals(gitHubLinkDto, new GitHubLinkDto("MaxZhukoff", "tinkoff-bot")),
                () -> assertEquals(gitHubLinkDto2, new GitHubLinkDto("MaxZhukoff", "tinkoff-bot")),
                () -> assertEquals(stackOverflowLinkDto, new StackOverflowLinkDto(75924506))
        );
    }

    @Test
    void parseLink_shouldReturnNull_whenLinkInvalid() {
        //given
        List<LinkHandler> handlers = List.of(new GitHubLinkHandler(), new StackOverflowLinkHandler());
        LinkParser linkParser = new LinkParser(handlers);

        //when
        LinkDto blankLink = linkParser.parseLink("");
        LinkDto baseLink = linkParser.parseLink("https://github.com");
        LinkDto gitHubUsernameLink = linkParser.parseLink("https://github.com/MaxZhukoff");
        LinkDto stackOverFlowStringIdLink = linkParser.parseLink("https://stackoverflow.com/questions/abcdgsd/clang-cant-find-avr-pgmspace-h-in-nvim-with-coc");
        LinkDto stackOverFlowWithoutQuestionsLink = linkParser.parseLink("https://stackoverflow.com/75924506/clang-cant-find-avr-pgmspace-h-in-nvim-with-coc");
        LinkDto stackOverFlowWithoutQuestionsLink2 = linkParser.parseLink("https://stackoverflow.com/abc/75924506/clang-cant-find-avr-pgmspace-h-in-nvim-with-coc");
        LinkDto googleLink = linkParser.parseLink("https://www.google.com/");
        LinkDto googleGitHubLink = linkParser.parseLink("https://www.google.com/MaxZhukoff/tinkoff-bot");
        LinkDto googleStackOverflowLink = linkParser.parseLink("https://www.google.com/questions/75924506/clang-cant-find-avr-pgmspace-h-in-nvim-with-coc");

        //then
        assertAll("Assert response null",
                () -> assertNull(blankLink),
                () -> assertNull(baseLink),
                () -> assertNull(gitHubUsernameLink),
                () -> assertNull(stackOverFlowStringIdLink),
                () -> assertNull(stackOverFlowWithoutQuestionsLink),
                () -> assertNull(stackOverFlowWithoutQuestionsLink2),
                () -> assertNull(googleLink),
                () -> assertNull(googleGitHubLink),
                () -> assertNull(googleStackOverflowLink)
        );
    }
}
