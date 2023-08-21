package org.origami.table.auto.schema;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.utils.SqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author origami
 * @date 2023/8/16 20:57
 */
public class SchemaUpdateStrategyImpl implements SchemaStrategy {
    @Override
    public List<String> getSQLString(Dialect dialect,
                                     DatabaseMetadata databaseMetadata,
                                     TableMetadata entityConvertedTable) {
        String tableName = entityConvertedTable.getTableName();
        if (!databaseMetadata.tableExists(tableName)) {
            return ImmutableList.of(SqlHelper.createTable(entityConvertedTable));
        }
        
        TableMetadata existsTable = databaseMetadata.getByTableName(tableName);
        Map<String, ColumnMetadata> existsColumn = existsTable.getColumns();
        
        MapDifference<String, ColumnMetadata> difference =
                Maps.difference(existsColumn, entityConvertedTable.getColumns());
        
        Map<String, ColumnMetadata> newColumn = difference.entriesOnlyOnRight();
        
        List<String> result = new ArrayList<>();
        for (ColumnMetadata columnMetadata : newColumn.values()) {
            result.add(SqlHelper.addColumn(columnMetadata));
        }
        
        return result;
    }
}
