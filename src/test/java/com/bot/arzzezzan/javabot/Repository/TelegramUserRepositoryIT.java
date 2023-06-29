package com.bot.arzzezzan.javabot.Repository;

import com.bot.arzzezzan.javabot.Repository.Entity.TelegramUser;
import com.bot.arzzezzan.javabot.Repository.TelegramUsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * Integration-level testing for {@link TelegramUsersRepository}.
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class TelegramUserRepositoryIT {

	@Autowired
	private TelegramUsersRepository telegramUserRepository;

	@Sql(scripts = {"/data.sql"})
	@Test
	public void shouldProperlyFindAllActiveUsers() {
		//when
		List<TelegramUser> users = telegramUserRepository.findAllByActiveTrue();

		//then
		Assertions.assertEquals(5, users.size());
	}

	@Sql(scripts = {"/data.sql"})
	@Test
	public void shouldProperlySaveTelegramUser() {
		//given
		TelegramUser telegramUser = new TelegramUser();
		telegramUser.setChatId("1234567890");
		telegramUser.setActive(false);
		telegramUserRepository.save(telegramUser);

		//when
		Optional<TelegramUser> saved = telegramUserRepository.findById(telegramUser.getChatId());

		//then
		Assertions.assertTrue(saved.isPresent());
		Assertions.assertEquals(telegramUser, saved.get());
	}
}