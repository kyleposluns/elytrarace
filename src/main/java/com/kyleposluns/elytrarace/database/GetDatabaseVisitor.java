package com.kyleposluns.elytrarace.database;


import com.kyleposluns.elytrarace.MessageFormatter;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLDatabase;
import org.bukkit.plugin.Plugin;

public class GetDatabaseVisitor implements CredentialsVisitor<ElytraDatabase> {

  private Plugin plugin;

  private MessageFormatter messageFormatter;

  public GetDatabaseVisitor(Plugin plugin, MessageFormatter messageFormatter) {
    this.plugin = plugin;
    this.messageFormatter = messageFormatter;
  }

  @Override
  public ElytraDatabase visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials) {
    return new ElytraSQLDatabase(plugin, this.messageFormatter, sqlCredentials);
  }
}
