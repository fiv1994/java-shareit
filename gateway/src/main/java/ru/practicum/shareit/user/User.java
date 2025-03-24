package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    @NotBlank(message = "Name of user can not be blank.")
    private String name;

    @NotBlank(message = "Email of user can not be blank.")
    @Email(message = "Not correct format of email")
    private String email;
}
