package com.kyleposluns.elytrarace.database.sql;

import java.sql.Connection;

public interface SQLSerializer<T> {

  void serialize(Connection connection, T object);

}
