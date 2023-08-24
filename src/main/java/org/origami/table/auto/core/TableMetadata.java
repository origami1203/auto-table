package org.origami.table.auto.core;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库表元数据
 *
 * @author origami
 * @date 2023/8/9 22:47
 */
@Data
@Accessors(chain = true)
public class TableMetadata {

    private Class<?> entityClass;

    private String catalog;

    private String tableName;

    private String comment;

    private ColumnMetadata primaryColumn;

    private Map<String, ColumnMetadata> columns;

    public void addColumnMetadata(ColumnMetadata columnMetadata) {
        if (columns == null) {
            columns = new HashMap<>();
        }
        columns.put(columnMetadata.getColumnName(), columnMetadata);
    }

    public boolean containsColumn(String columnName) {
        return columns.containsKey(columnName);
    }
}
