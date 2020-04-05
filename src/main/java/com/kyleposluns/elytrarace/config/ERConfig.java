package com.kyleposluns.elytrarace.config;

import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.database.GetDatabaseVisitor;
import com.kyleposluns.elytrarace.database.mongo.ElytraMongoCredentials;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ERConfig {

  private static final String DATABASE = "database";

  private static final String MYSQL = "mysql";

  private static final String MONGO = "mongo";

  private static final String HOST = "host";

  private static final String PORT = "port";

  private static final String DATABASE_NAME = "database_name";

  private static final String USER_NAME = "user";

  private static final String PASSWORD = "password";

  private Credentials credentials;

  public ERConfig(FileConfiguration configuration) {
    ConfigurationSection databaseSection = configuration.getConfigurationSection(DATABASE);
    if (databaseSection == null) {
      throw new IllegalStateException("Cannot connect to a database without required fields");
    }
    if (databaseSection.contains(MYSQL)) {
      this.credentials = new ElytraSQLCredentials(databaseSection.getString(HOST),
          databaseSection.getInt(PORT), databaseSection.getString(DATABASE_NAME),
          databaseSection.getString(USER_NAME), databaseSection.getString(
          PASSWORD));
    } else if (databaseSection.contains(MONGO)) {
      throw new UnsupportedOperationException();
    } else {
      throw new IllegalStateException("Cannot connect to a known type of database");
    }
  }

  public ElytraDatabase getDatabase() {
    return this.credentials.visitCredentials(new GetDatabaseVisitor());
  }

}
