package com.kyleposluns.elytrarace.config;

import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.database.Credentials;
import com.kyleposluns.elytrarace.database.ElytraDatabase;
import com.kyleposluns.elytrarace.database.GetDatabaseVisitor;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;
import java.util.Objects;
import org.bukkit.ChatColor;
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

  private static final String COLORS = "colors";

  private static final String PRIMARY = "primary";

  private static final String SECONDARY = "secondary";

  private static final String ALTERNATE = "alternate";

  private final Plugin plugin;

  private MessageFormatter messageFormatter;

  private Credentials credentials;

  public ERConfig(Plugin plugin, FileConfiguration configuration) {
    this.plugin = plugin;

    ConfigurationSection colorsSection = configuration.getConfigurationSection(COLORS);
    if (colorsSection == null) {
      throw new IllegalStateException("Cannot format messages properly.");
    }

    ChatColor primary = ChatColor.getByChar(
        Objects.requireNonNull(colorsSection.getString(PRIMARY)));

    ChatColor second = ChatColor.getByChar(
        Objects.requireNonNull(colorsSection.getString(SECONDARY)));

    ChatColor alternate = ChatColor.getByChar(
        Objects.requireNonNull(colorsSection.getString(ALTERNATE)));
    this.messageFormatter = new MessageFormatter(primary, second, alternate);

    ConfigurationSection databaseSection = configuration.getConfigurationSection(DATABASE);
    if (databaseSection == null) {
      throw new IllegalStateException("Cannot connect to a database without required fields");
    }
    if (databaseSection.contains(MYSQL)) {
      ConfigurationSection mysql = databaseSection.getConfigurationSection(MYSQL);
      assert mysql != null;
      this.credentials = new ElytraSQLCredentials(mysql.getString(HOST),
          mysql.getInt(PORT), mysql.getString(DATABASE_NAME),
          mysql.getString(USER_NAME), mysql.getString(
          PASSWORD));
    } else if (databaseSection.contains(MONGO)) {
      throw new UnsupportedOperationException();
    } else {
      throw new IllegalStateException("Cannot connect to a known type of database");
    }

  }

public MessageFormatter getMessageFormatter() {
    return this.messageFormatter;
}

  public ElytraDatabase getDatabase() {
    return this.credentials
        .visitCredentials(new GetDatabaseVisitor(this.plugin, this.messageFormatter));
  }

}
