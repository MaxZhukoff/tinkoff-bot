package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "chats")
@ToString(exclude = "chats")
@Builder
@Entity
public class Link {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "last_check_at")
    private OffsetDateTime lastCheckAt;

    @Column(name = "last_commit_at")
    private OffsetDateTime lastCommitAt;

    @Column(name = "issues_count")
    private Integer issuesCount;

    @Column(name = "answer_count")
    private Integer answerCount;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "link_chat",
               joinColumns = {@JoinColumn(name = "link_id", nullable = false, updatable = false)},
               inverseJoinColumns = {@JoinColumn(name = "chat_id", nullable = false, updatable = false)}
    )
    private List<Chat> chats = new ArrayList<>();

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void removeChat(Chat chat) {
        chats.remove(chat);
    }
}
