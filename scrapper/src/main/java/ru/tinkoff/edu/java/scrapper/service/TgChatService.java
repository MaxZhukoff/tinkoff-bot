package ru.tinkoff.edu.java.scrapper.service;

public interface TgChatService {
    void register(Long tgChatId);

    void unregister(Long tgChatId);
}
