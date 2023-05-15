package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
