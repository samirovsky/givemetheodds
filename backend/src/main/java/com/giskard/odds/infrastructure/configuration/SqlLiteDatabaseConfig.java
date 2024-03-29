package com.giskard.odds.infrastructure.configuration;

import com.giskard.odds.application.data.MillenniumFalconConfig;
import java.io.IOException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class SqlLiteDatabaseConfig {

  private final MillenniumFalconConfig millenniumFalconConfig;

  public SqlLiteDatabaseConfig(MillenniumFalconConfig millenniumFalconConfig) {
    this.millenniumFalconConfig = millenniumFalconConfig;
  }

  @Bean
  public DataSource dataSource() throws IOException {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlite:%s".formatted(millenniumFalconConfig.routesDb()));
    return dataSource;
  }
}
