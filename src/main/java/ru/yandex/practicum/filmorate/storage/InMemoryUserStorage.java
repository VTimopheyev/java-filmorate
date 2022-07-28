package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private int count = 0;

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public User addNewUser(User user) {
        count++;
        user.setId(count);
        users.put(count, user);
        return users.get(count);
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public ArrayList<User> sendAllUsersList(){
        ArrayList<User> list = new ArrayList<>();
        for (int id : users.keySet()) {
            list.add(users.get(id));
        }
        log.info("Users list has been sent");
        return list;
    }

    public void addNewFriendToUser(Integer id, Integer friendId){
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        log.info("Friend added to friends list");
    }

    public void removeUserFromFriends (Integer id, Integer friendId){
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        log.info("Friend was removed from user`s list of friends");
    }

    public List<User> getCommonFriendsList (Integer id, Integer friendId){
        List <User> commonFriends = new ArrayList <User>();
        for (Integer friend : users.get(id).getFriends()) {
            if (users.get(friendId).getFriends().contains(friend)){
                commonFriends.add(users.get(friend));
            }
        }
        return commonFriends;
    }
}
