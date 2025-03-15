package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CommentDtoMapper {
    private final UserRepository userRepository;

    public FromServerCommentDto convertToDto(Comment comment) {
        String authorName = userRepository.findById(comment.getAuthorId()).get().getName();
        return new FromServerCommentDto(comment.getId(), authorName, comment.getItemId(), comment.getText(),
                comment.getCreated());
    }

    public List<FromServerCommentDto> convertToDto(List<Comment> comments) {
        List<Long> commentsAuthorsIds = comments.stream().map(Comment::getAuthorId).toList();
        Map<Long, User> commentAuthors = new HashMap<>();
        userRepository.findByIdIn(commentsAuthorsIds).forEach(author -> commentAuthors.put(author.getId(), author));

        Function<Comment, FromServerCommentDto> convertComment =
                comment -> new FromServerCommentDto(comment.getId(),
                        commentAuthors.get(comment.getAuthorId()).getName(),
                        comment.getItemId(),
                        comment.getText(),
                        comment.getCreated()
                );
        return comments.stream().map(convertComment).toList();
    }

}