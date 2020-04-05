package com.kyleposluns.elytrarace.database.sql;

import java.sql.Connection;

public interface SQLDeserializer<R> {

  R deserialize(Connection connection);

}
