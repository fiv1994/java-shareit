package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestsService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.function.Supplier;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceIntegrationTests {

    private final ItemService itemService;
    private final UserService userService;
    private final RequestsService requestsService;

    private User user1;
    private User user2;

    @Test
    public void serviceCreatesAndGetsRequestWhenDataIsOk() {
        ItemRequest requestToCreate = new ItemRequest(user1.getId(), "Description");
        long createdRequestId = requestsService.createRequest(requestToCreate).getId();
        ItemRequest result = requestsService.getRequest(createdRequestId);
        boolean condition1 = result.getDescription().equals(requestToCreate.getDescription());
        boolean condition2 = result.getCreatorId() == user1.getId();
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void serviceGetsRequestsOfUserWhenDataIsOk() {
        testTemplate(() -> requestsService.getRequestsOfUser(user1.getId()));
    }

    @Test
    public void serviceGetsRequestsOfOtherUsersWhenDataIsOk() {
        testTemplate(() -> requestsService.getRequestsOfOthers(user2.getId()));
    }

    @Test
    public void serviceCanAddResponseOnRequestWhenDataIsOk() {
        ItemRequest requestToCreate1 = new ItemRequest(user1.getId(), "Description1");
        long createdRequestId1 = requestsService.createRequest(requestToCreate1).getId();

        Item itemToCreate = new Item(0L,
                user1,
                "Item1",
                "Description1",
                true);

        long createdItemId = itemService.createItem(itemToCreate, createdRequestId1).getId();

        ItemRequest result = requestsService.getRequest(createdRequestId1);
        boolean condition1 = result.getItems().size() == 1;
        boolean condition2 = result.getItems().getFirst().getId() == createdItemId;

        Assertions.assertTrue(condition1 && condition2);
    }

    private void testTemplate(Supplier<List<ItemRequest>> getListOfRequests) {
        long userFstId = user1.getId();
        ItemRequest requestToCreate1 = new ItemRequest(userFstId, "Description1");
        ItemRequest requestToCreate2 = new ItemRequest(userFstId, "Description2");
        long createdRequestId1 = requestsService.createRequest(requestToCreate1).getId();
        long createdRequestId2 = requestsService.createRequest(requestToCreate2).getId();
        List<ItemRequest> requests = getListOfRequests.get();
        boolean condition1 = requests.size() == 2;
        boolean condition2 = requests.getFirst().getId() == createdRequestId2;
        boolean condition3 = requests.getLast().getId() == createdRequestId1;
        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @BeforeEach
    public void createTwoUsers() {
        User userFstToCreate = new User(0L, "Bob", "bob@mail.ru");
        user1 = userService.createUser(userFstToCreate);

        User userSndToCreate = new User(0L, "Kate", "kate@mail.ru");
        user2 = userService.createUser(userSndToCreate);
    }
}
