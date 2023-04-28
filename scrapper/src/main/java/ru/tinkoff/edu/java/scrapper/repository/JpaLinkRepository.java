package ru.tinkoff.edu.java.scrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    Optional<Link> findByUrlAndChatsId(String url, Long chatId);

    @Query("SELECT l FROM Link l WHERE l.lastCheckAt < :checkTime")
    List<Link> findAllLinksNotUpdatedAt(@Param("checkTime") OffsetDateTime checkTime);

}
