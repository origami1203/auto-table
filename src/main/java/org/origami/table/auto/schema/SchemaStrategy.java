package org.origami.table.auto.schema;

import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;

import java.util.List;

/**
 * schema策略接口
 *
 * @author origami
 * @date 2023/8/16 19:16
 */
public interface SchemaStrategy {
    
    List<String> getSQLString(Dialect dialect, DatabaseMetadata databaseMetadata, TableMetadata tableMetadata);
}
