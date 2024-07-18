package tech.wetech.admin3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author cjbi
 */

@ActiveProfiles({"test", "biz"})
@Testcontainers
@SpringBootTest(
  classes = Admin3ServerApplication.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIntegrationTest {


  private static final OracleContainer ORACLE_CONTAINER = new OracleContainer(
    DockerImageName.parse("gvenzl/oracle-xe:slim-faststart"))
    .withExposedPorts(1521).withLogConsumer(outputFrame -> {});
  private static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2.14-alpine"))
    .withExposedPorts(6379);

  @BeforeAll
  public static void beforeAll() {
    ORACLE_CONTAINER.start();
    redis.start();
  }
  @DynamicPropertySource
  static void loadProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", redis::getFirstMappedPort);

    registry.add("spring.datasource.url", ORACLE_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", ORACLE_CONTAINER::getUsername);
    registry.add("spring.datasource.password", ORACLE_CONTAINER::getPassword);

  }

}
