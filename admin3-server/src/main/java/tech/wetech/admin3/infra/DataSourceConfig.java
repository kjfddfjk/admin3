package tech.wetech.admin3.infra;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;


  @Bean
  public HikariConfig hikariConfig() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url); // 设置jdbcUrl
    config.setPassword(password);
    config.setUsername(username);
    return config;
  }

  @Bean
  public DataSource dataSource(HikariConfig hikariConfig) {
    return new HikariDataSource(hikariConfig);
  }
}
