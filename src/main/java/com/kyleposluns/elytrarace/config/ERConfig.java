package com.kyleposluns.elytrarace.config;

import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.mongo.ElytraMongoCredentials;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ERConfig {

  private static final String SERVER_NAME = "server_name";

  private static final String PORT = "port";

  private static final String DATABASE_NAME = "database_name";

  private static final String USER_NAME = "user";

  private static final String PASSWORD = "password";

  private static final String DATABASE = "database";

  private final String serverName;

  private final int port;

  private final String databaseName;

  private final String userName;

  private final String password;

  public ERConfig(FileConfiguration configuration) {
    ConfigurationSection databaseSection = configuration.getConfigurationSection(DATABASE);
    if (databaseSection == null) {
      throw new IllegalArgumentException("Cannot connect to a database without required fields");
    }

    this.serverName = databaseSection.getString(SERVER_NAME);
    this.port = databaseSection.getInt(PORT);
    this.databaseName = databaseSection.getString(DATABASE_NAME);
    this.userName = databaseSection.getString(USER_NAME);
    this.password = databaseSection.getString(PASSWORD);
  }


  public Credentials getConnectionInfo() {
    return new ElytraMongoCredentials(this.serverName, port, this.databaseName, this.userName, this.password);
  }

}
