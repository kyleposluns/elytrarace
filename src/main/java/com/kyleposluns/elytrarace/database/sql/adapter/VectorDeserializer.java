package com.kyleposluns.elytrarace.database.sql.adapter;

import com.kyleposluns.elytrarace.database.sql.SQLDeserializer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.util.Vector;

public class VectorDeserializer implements SQLDeserializer<Vector> {

  private static final String X = "x";

  private static final String Y = "y";

  private static final String Z = "z";

  private final int id;

  public VectorDeserializer(int id) {
    this.id = id;
  }

  @Override
  public Vector deserialize(Connection connection) {
    Vector vector = null;
    try {
      CallableStatement statement = connection.prepareCall("{CALL get_vector(?)}");
      statement.setInt(1, this.id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        double x = rs.getDouble(X);
        double y = rs.getDouble(Y);
        double z = rs.getDouble(Z);
        vector = new Vector(x, y, z);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return vector;
  }

}
