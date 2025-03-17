package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, ToServerBookingDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> permitOrRejectBooking(long userId, long bookingId, boolean approved) {
        String path = String.format("/%d?approved=%s", bookingId, approved);
        return patch(path, userId);
    }

    public ResponseEntity<Object> getBooking(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getUsersBookingsInAscOrder(long userId, String state) {
        Map<String, Object> parameters = Map.of("state", state);
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsOfUsersItemsInAscOrder(long userId, String state) {
        Map<String, Object> parameters = Map.of("state", state);
        return get("/owner?state={state}", userId, parameters);
    }
}
