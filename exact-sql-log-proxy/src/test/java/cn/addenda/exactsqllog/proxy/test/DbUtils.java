package cn.addenda.exactsqllog.proxy.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Properties;

public class DbUtils {

  static Properties properties;

  @Getter
  private static DataSource dataSource;

  static {
    try {
      String path = DbUtils.class.getClassLoader()
              .getResource("db.properties").getPath();
      properties = new Properties();
      properties.load(new FileInputStream(path));
    } catch (Exception e) {
      e.printStackTrace();
    }

    dataSource = dataSource2();
  }

  private static HikariDataSource dataSource0() {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setDriverClassName("org.h2.Driver");
    hikariDataSource.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
    hikariDataSource.setUsername("root");
    hikariDataSource.setPassword("root");
    hikariDataSource.setMaximumPoolSize(3);
    hikariDataSource.setAutoCommit(false);

    return hikariDataSource;
  }

  private static HikariDataSource dataSource1() {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setDriverClassName(properties.getProperty("driver"));
    hikariDataSource.setJdbcUrl(properties.getProperty("url"));
    hikariDataSource.setUsername(properties.getProperty("username"));
    hikariDataSource.setPassword(properties.getProperty("password"));
    hikariDataSource.setMaximumPoolSize(3);
    hikariDataSource.setAutoCommit(false);

    return hikariDataSource;
  }

  private static DruidDataSource dataSource2() {
    DruidDataSource druidDataSource = new DruidDataSource();
    druidDataSource.setDriverClassName(properties.getProperty("driver"));
    druidDataSource.setUrl(properties.getProperty("url"));
    druidDataSource.setUsername(properties.getProperty("username"));
    druidDataSource.setPassword(properties.getProperty("password"));
    druidDataSource.setTestWhileIdle(false);
    return druidDataSource;
  }

}
