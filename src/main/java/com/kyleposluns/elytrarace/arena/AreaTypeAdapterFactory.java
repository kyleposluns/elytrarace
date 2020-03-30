package com.kyleposluns.elytrarace.arena;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.kyleposluns.elytrarace.database.RuntimeTypeAdapterFactory;

public class AreaTypeAdapterFactory implements TypeAdapterFactory {

  private TypeAdapterFactory factory;

  public AreaTypeAdapterFactory() {
    this.factory = RuntimeTypeAdapterFactory.of(Area.class)
        .registerSubtype(CircleArea.class).registerSubtype(CuboidArea.class)
        .registerSubtype(CustomArea.class).registerSubtype(SphereArea.class);
  }


  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    return this.factory.create(gson, typeToken);
  }


  /*
  static class CircleAreaAdapter extends TypeAdapter<CircleArea> {

    private static final String CENTER = "center";

    private static final String RADIUS = "radius";

    private static final String AXIS = "axis";

    private static final String X = "x";

    private static final String Y = "y";

    private static final String Z = "z";

    private Gson gson;

    CircleAreaAdapter(Gson gson) {
      this.gson = gson;
    }

    @Override
    public void write(JsonWriter jsonWriter, CircleArea circleArea) throws IOException {
      jsonWriter.beginObject();
      String vector = this.gson.toJson(circleArea.getCenter());
      jsonWriter.name(CENTER).jsonValue(vector);
      jsonWriter.name(RADIUS).value(circleArea.getRadius());
      String axis = this.gson.toJson(circleArea.getAxis());
      jsonWriter.name(AXIS).value(axis);
      jsonWriter.endObject();
    }

    @Override
    public CircleArea read(JsonReader jsonReader) throws IOException {
      jsonReader.beginObject();
      Vector center = null;
      double radius = 0.0;
      Axis axis = null;
      while (jsonReader.hasNext()) {
        String name = jsonReader.nextName();
        switch (name) {
          case CENTER: {
            jsonReader.beginObject();
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            while (jsonReader.hasNext()) {
              String vectorName = jsonReader.nextName();
              switch (vectorName) {
                case X: {
                  x = jsonReader.nextDouble();
                  break;
                }
                case Y: {
                  y = jsonReader.nextDouble();
                  break;
                }
                case Z: {
                  z = jsonReader.nextDouble();
                  break;
                }
                default: {
                  throw new IllegalArgumentException(String
                      .format("Expecting one of [%s, %s, %s] but got %s.", X, Y, Z, vectorName));
                }
              }
            }
            center = new Vector(x, y, z);
            break;
          }
          case RADIUS: {
            radius = jsonReader.nextDouble();
            break;
          }
          case AXIS: {
            axis = Axis.valueOf(jsonReader.nextString());
            break;
          }
          default: {
            throw new IllegalArgumentException(String
                .format("Expecting one of [%s, %s, %s] but got %s.", CENTER, RADIUS, AXIS, name));
          }
        }
      }
      return new CircleArea(center, axis, radius);
    }
  }*/
}
