package com.kyleposluns.elytrarace.database;


import com.kyleposluns.elytrarace.database.sql.ElytraSQLCredentials;
import com.kyleposluns.elytrarace.database.sql.ElytraSQLDatabase;
import org.bukkit.plugin.Plugin;

public class GetDatabaseVisitor implements CredentialsVisitor<ElytraDatabase> {

  private Plugin plugin;

  public GetDatabaseVisitor(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public ElytraDatabase visitSQLDBCredentials(ElytraSQLCredentials sqlCredentials) {
    return new ElytraSQLDatabase(plugin, sqlCredentials);
  }
}
