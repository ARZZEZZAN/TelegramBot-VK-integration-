package com.bot.arzzezzan.javabot.Config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
 @Value("${db.url}")
 private String URL_DB;
 @Value("${db.username}")
 private String DB_LOGIN;
 @Value("${db.password}")
 private String DB_PASSWORD;
 @Value("${db.driver}")
 private String DRIVER;


 @Bean
 public DataSource hikariDataSource() {
  HikariConfig hikariConfig = new HikariConfig();
  hikariConfig.setDriverClassName(DRIVER);
  hikariConfig.setJdbcUrl(URL_DB);
  hikariConfig.setUsername(DB_LOGIN);
  hikariConfig.setPassword(DB_PASSWORD);
  return new HikariDataSource(hikariConfig);
 }
}
