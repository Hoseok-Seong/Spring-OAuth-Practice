package shop.mtcoding.getintherelogin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import shop.mtcoding.getintherelogin.model.User;

public class UserStore {
    private static List<User> userList = new ArrayList<>();

    static { // main 실행 전에 메모리에 띄워짐
        userList.add(
                new User(1, "kakao_2705570829", UUID.randomUUID().toString(), "ssar@gmail.com", "kakao"));
    }

    public static void save(User user) {
        userList.add(user);
    }

    public static User findByUsername(String username) {
        // Optional<User> findUser = userList.stream()
        // .filter((User user) -> user.getUsername().equals(username))
        // .findFirst();
        // return findUser;
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
