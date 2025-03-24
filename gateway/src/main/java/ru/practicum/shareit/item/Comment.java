package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private long id;

    private long authorId;

    private long itemId;

    @NotBlank
    private String text;

    private LocalDateTime created;
}