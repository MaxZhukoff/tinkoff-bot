import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationEnvironmentTest extends IntegrationEnvironment {
    @SneakyThrows
    @Test
    void getTables_shouldReturnCreatedTables() {
        Connection connection = getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        Set<String> expectedTablesSet = Set.of("chat", "databasechangelog", "databasechangeloglock", "link", "link_chat");
        Set<String> actualTablesSet = new HashSet<>();

        ResultSet resultSet = metaData.getTables(null, "public", "%", null);
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            actualTablesSet.add(tableName);
        }

        assertTrue(actualTablesSet.containsAll(expectedTablesSet));
    }
}
