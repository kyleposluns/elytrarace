package com.kyleposluns.elytrarace.arena;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationAdapter extends TypeAdapter<Location> {

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  private static final String WORLD_ID = "world-id";

  private static final String YAW = "yaw";

  private static final String PITCH = "pitch";

  @Override
  public void write(JsonWriter writer, Location location) throws IOException {
    writer.beginObject();
    writer.name(WORLD_ID).value(Objects.requireNonNull(location.getWorld()).getUID().toString());
    writer.name(X).value(location.getX());
    writer.name(Y).value(location.getY());
    writer.name(Z).value(location.getZ());
    writer.name(YAW).value(location.getYaw());
    writer.name(PITCH).value(location.getPitch());
    writer.endObject();
  }

  @Override
  public Location read(JsonReader reader) throws IOException {
    UUID worldId = null;
    double x = 0.0;
    double y = 0.0;
    double z = 0.0;
    float yaw = 0;
    float pitch = 0;
    while (reader.hasNext()) {
      String name = reader.nextName();
      switch (name) {
        case WORLD_ID: {
          worldId = UUID.fromString(reader.nextString());
          break;
        }
        case X: {
          x = reader.nextDouble();
          break;
        }
        case Y: {
          y = reader.nextDouble();
          break;
        }
        case Z: {
          z = reader.nextDouble();
          break;
        }
        case YAW: {
          yaw = (float) reader.nextDouble();
          break;
        }
        case PITCH: {
          pitch = (float) reader.nextDouble();
          break;
        }
        default: {
          throw new IllegalArgumentException(String
              .format("Expected one of [%s, %s, %s, %s, %s, %s), but got: %s", WORLD_ID, X, Y, Z,
                  YAW, PITCH, name));
        }
      }

    }
    assert worldId != null;
    return new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
  }
}
