package ru.tinkoff.edu.java.scrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;

@Repository
public interface JpaTgChatRepository extends JpaRepository<Chat, Long> {
}
