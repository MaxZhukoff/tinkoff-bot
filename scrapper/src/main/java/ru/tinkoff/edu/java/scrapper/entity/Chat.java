package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "links")
@Entity
public class Chat {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY)
    private List<Link> links = new ArrayList<>();
}
