package ru.practicum.model.user;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toUser(UserIn userIn) {
        return new User(
                userIn.getName(),
                userIn.getEmail()
        );
    }

    public static UserOut toUserOut(User user) {
        return new UserOut(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
