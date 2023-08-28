package org.origami.table.auto.schema;

import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 策略上下文
 *
 * @author origami
 * @date 2023/8/16 19:41
 */
public class SchemaContext {

    private final SchemaStrategy strategy;
    private final JdbcTemplate jdbcTemplate;

    private SchemaContext(SchemaStrategy strategy, JdbcTemplate jdbcTemplate) {
        this.strategy = strategy;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void handle(Dialect dialect, DatabaseMetadata databaseMetadata, TableMetadata tableMetadata) {
        strategy.handle(jdbcTemplate, dialect, databaseMetadata, tableMetadata);
    }

    public static SchemaContext of(SchemaStrategy strategy, JdbcTemplate jdbcTemplate) {
        return new SchemaContext(strategy, jdbcTemplate);
    }
}
