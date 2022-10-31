package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final static Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean checkUserExist(Integer id) {
        String sqlQuery = "select count(*) from users where user_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return result == 1;
    }

    @Override
    public boolean checkFriendshipExist(Integer userId, Integer friendId) {
        String sqlQuery = "select count(*) from friendship_approved where user_id = ? and friend_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendId);
        return result == 1;
    }

    @Override
    public boolean checkFriendshipRequestedByUser(Integer userId, Integer friendId) {
        String sqlQuery = "select count(*) from friendship_requests where user_id = ? and friend_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendId);
        return result == 1;
    }

    @Override
    public void removeFriendshipRequest(Integer userId, Integer friendId) {
        String sqlQuery = "delete from friendship_requests where user_id = ? and friend_id = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Friendship request removed");
    }

    @Override
    public int addNewUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public User getUser(int id) {
        String sqlQuery = "select user_id, name, email, login, birthdate " +
                "from users where user_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " + "name = ?, email = ?, login = ?, birthdate = ?" + " where user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());

        return getUser(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select user_id, name, email, login, birthdate from users";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
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

    @Override
    public List<User> getAllFriendsOfUser(Integer userId) {
        List<Integer> friendsId = getAllFriendsId(userId);
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(getUser(id));
        }
        return friends;
    }


    @Override
    public List<Integer> getAllFriendsId(Integer userId) {
        String sqlQuery = "select friend_id " + "from friendship_approved " +
                "where user_id = ?";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
    }

    @Override
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

    @Override
    public boolean checkIfUserInFriendsList(Integer userId, Integer friendId) {
        List<User> friends = getAllFriendsOfUser(userId);
        for (User f : friends) {
            if (f.getId() == friendId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void approveFriendShip(Integer userId, Integer friendId) {

        String sqlQuery = "insert into friendship_approved (user_id, friend_id)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void addFriendshipRequest(Integer userId, Integer friendId) {

        String sqlQuery = "insert into friendship_requests (user_id, friend_id)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }


    @Override
    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        // It is used to map each row of a ResultSet to an object.
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthdate"))
                .build();
    }
}
