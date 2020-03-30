package com.kyleposluns.elytrarace.records;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public class RecordAdapter implements JsonSerializer<Record>, JsonDeserializer<Record> {

  private static final String PLAYER_ID = "player-id";

  private static final String ARENA_ID = "arena-id";

  private static final String DATE = "date";

  private static final String TIME = "time";

  private static final String POSITIONS = "positions";

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  @Override
  public Record deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject object = jsonElement.getAsJsonObject();
    UUID playerId = UUID.fromString(object.get(PLAYER_ID).getAsString());
    UUID arenaId = UUID.fromString(object.get(ARENA_ID).getAsString());
    long date = object.get(DATE).getAsLong();
    int time = object.get(TIME).getAsInt();
    JsonArray array = object.getAsJsonArray(POSITIONS);
    List<Vector> vectors = new ArrayList<>();
    for (JsonElement vectorElement : array) {
      JsonObject vectorObject = vectorElement.getAsJsonObject();
      double x = vectorObject.get(X).getAsDouble();
      double y = vectorObject.get(Y).getAsDouble();
      double z = vectorObject.get(Z).getAsDouble();
      vectors.add(new Vector(x, y, z));
    }
    return new RecordImpl(playerId, arenaId, date, time, vectors);
  }

  @Override
  public JsonElement serialize(Record record, Type type,
      JsonSerializationContext jsonSerializationContext) {
    JsonObject jo = new JsonObject();
    jo.addProperty(PLAYER_ID, record.getPlayerId().toString());
    jo.addProperty(ARENA_ID, record.getArenaId().toString());
    jo.addProperty(DATE, record.getDate());
    jo.addProperty(TIME, record.getTime());
    JsonArray positions = new JsonArray();
    for (Vector vector : record.getPositions()) {
      JsonObject jsonVector = new JsonObject();
      jsonVector.addProperty(X, vector.getX());
      jsonVector.addProperty(Y, vector.getY());
      jsonVector.addProperty(Z, vector.getZ());
      positions.add(jsonVector);
    }
    jo.add(POSITIONS, positions);
    return jo;
  }
}
