package ru.tinkoff.edu.java.scrapper.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Link;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    Optional<Link> findByUrlAndChatsId(String url, Long chatId);

    @Query("SELECT l FROM Link l WHERE l.lastCheckAt < :checkTime")
    List<Link> findAllLinksNotUpdatedAt(@Param("checkTime") OffsetDateTime checkTime);

}
