package com.giskard.odds.infrastructure.configuration;

import com.giskard.odds.application.data.MillenniumFalconConfig;
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
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlite:/Users/samirtabib/Projects/databases/universe.db");
    // dataSource.setUrl("jdbc:sqlite:%s".formatted(millenniumFalconConfig.routesDb()));
    return dataSource;
  }
}
