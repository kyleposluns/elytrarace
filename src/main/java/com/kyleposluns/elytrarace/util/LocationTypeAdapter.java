package com.kyleposluns.elytrarace.util;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;

public class LocationTypeAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

  private static final String WORLD = "world";

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  private static final String YAW = "yaw";

  private static final String PITCH = "pitch";


  @Override
  public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject obj = jsonElement.getAsJsonObject();

    UUID worldId = UUID.fromString(obj.get(WORLD).getAsString());

    double x = obj.get(X).getAsDouble();

    double y = obj.get(Y).getAsDouble();

    double z = obj.get(Z).getAsDouble();

    float yaw = obj.get(YAW).getAsFloat();

    float pitch = obj.get(PITCH).getAsFloat();


    World world = Bukkit.getWorld(worldId);
    return new Location(world, x, y, z, yaw, pitch);
  }

  @Override
  public JsonElement serialize(Location location, Type type,
                               JsonSerializationContext jsonSerializationContext) {
    JsonObject obj = new JsonObject();

    obj.addProperty(WORLD, location.getWorld().getUID().toString());
    obj.addProperty(X, location.getX());
    obj.addProperty(Y, location.getY());
    obj.addProperty(Z, location.getZ());
    obj.addProperty(YAW, location.getYaw());
    obj.addProperty(PITCH, location.getPitch());
    return obj;
  }
}
