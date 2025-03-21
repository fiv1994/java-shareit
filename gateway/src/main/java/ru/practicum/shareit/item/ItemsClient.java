package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemsClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemsClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ToServerItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> getItem(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getItemsOfUser(long userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> patchItem(String itemIdString, long userId, Item item) {
        return patch("/" + itemIdString, userId, item);
    }

    public ResponseEntity<Object> searchAvailableItems(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search", 0L, parameters);
    }

    public ResponseEntity<Object> createComment(long itemId, long userId, Comment comment) {
        String url = String.format("/%d/comment", itemId);
        return post(url, userId, comment);
    }

}
