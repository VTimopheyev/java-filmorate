package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
@Component
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final static Logger log = LoggerFactory.getLogger(FriendDbStorage.class);


    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {

        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public List<User> addFriend(Integer userId, Integer friendId) {
        if (checkFriendshipRequestedByUser(userId, friendId)) {

            removeFriendshipRequest(userId, friendId);
            approveFriendShip(userId, friendId);

            log.info("Friendship approved");

            return getAllFriendsOfUser(userId);
        }

        String sqlQuery = "insert into friendship_requests (user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, friendId, userId);

        approveFriendShip(userId, friendId);

        log.info("Friendship requested by friend");
        return getAllFriendsOfUser(userId);
    }

    public List<User> getAllFriendsOfUser(Integer userId) {
        List<Integer> friendsId = getAllFriendsId(userId);
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(userDbStorage.getUser(id));
        }
        return friends;
    }


    public List<Integer> getAllFriendsId(Integer userId) {
        String sqlQuery = "select friend_id " + "from friendship_approved " +
                "where user_id = ?";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
    }

    public void removeUserFromFriends(Integer userId, Integer friendId) {

        if (!checkIfUserInFriendsList(userId, friendId)) {
            log.info("There`s no such friend of user");
        }

        if (checkFriendshipRequestedByUser(friendId, userId)) {

            String sqlQuery = "delete from friendship_approved where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
            log.info("Friend removed");

            sqlQuery = "delete from friendship_requests where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery, friendId, userId);
            log.info("Friendship Request declined");
        }

        if (checkFriendshipExist(userId, friendId) && checkFriendshipExist(friendId, userId)) {
            String sqlQuery = "delete from friendship_approved where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
            log.info("Friend removed");

            sqlQuery = "delete from friendship_approved where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery, friendId, userId);
            log.info("Friendship removed");
        }
    }

    public void approveFriendShip(Integer userId, Integer friendId) {

        String sqlQuery = "insert into friendship_approved (user_id, friend_id)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public void addFriendshipRequest(Integer userId, Integer friendId) {

        String sqlQuery = "insert into friendship_requests (user_id, friend_id)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public boolean checkIfUserInFriendsList(Integer userId, Integer friendId) {
        List<User> friends = getAllFriendsOfUser(userId);
        for (User f : friends) {
            if (f.getId() == friendId) {
                return true;
            }
        }
        return false;
    }

    public boolean checkFriendshipExist(Integer userId, Integer friendId) {
        String sqlQuery = "select count(*) from friendship_approved where user_id = ? and friend_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendId);
        return result == 1;
    }

    public boolean checkFriendshipRequestedByUser(Integer userId, Integer friendId) {
        String sqlQuery = "select count(*) from friendship_requests where user_id = ? and friend_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendId);
        return result == 1;
    }

    public void removeFriendshipRequest(Integer userId, Integer friendId) {
        String sqlQuery = "delete from friendship_requests where user_id = ? and friend_id = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Friendship request removed");
    }
}
