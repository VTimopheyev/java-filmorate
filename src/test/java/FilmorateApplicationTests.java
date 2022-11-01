import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	@Autowired
	UserDbStorage userStorage;
	User user1;
	User user2;
	User user3;

	public void createUsers (){
		User user1 = User.builder()
				.name("John")
				.login("Login1")
				.email("user1@email.mail")
				.birthday(new Date(1986, 04, 14))
				.build();
		this.user1 = user1;

		User user2 = User.builder()
				.name("Jack")
				.login("Login2")
				.email("user2@email.mail")
				.birthday(new Date(1988, 05, 22))
				.build();
		this.user2 = user2;

		User user3 = User.builder()
				.name("Joe")
				.login("Login3")
				.email("user3@email.mail")
				.birthday(new Date(1980, 11, 25))
				.build();
		this.user3 = user3;
	}

	@Test
	public void testCreateUsers (){
		createUsers();

		userStorage.addNewUser(user1);
		userStorage.addNewUser(user2);
		userStorage.addNewUser(user3);
		Optional<User> userOptional = Optional.of(userStorage.getUser(1));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testFindUserById() {
		Optional<User> userOptional = Optional.of(userStorage.getUser(1));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}
}
