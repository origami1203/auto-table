package org.origami.table.auto.schema;

import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;

import java.util.List;

/**
 * 策略上下文
 *
 * @author origami
 * @date 2023/8/16 19:41
 */
public class SchemaContext {
    
    private final SchemaStrategy strategy;
    
    private SchemaContext(SchemaStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<String> getSQLString(Dialect dialect, DatabaseMetadata databaseMetadata, TableMetadata tableMetadata) {
        return strategy.getSQLString(dialect, databaseMetadata, tableMetadata);
    }
    
    public static SchemaContext of(SchemaStrategy strategy) {
        return new SchemaContext(strategy);
    }
}
