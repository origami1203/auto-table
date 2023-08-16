package org.origami.table.auto.core;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表元数据
 *
 * @author origami
 * @date 2023/8/9 22:47
 */
@Data
@Accessors(chain = true, fluent = true)
public class TableMetadata {
    
    private String tableName;
    
    private String charset;
    
    private String engine;
    
    private String comment;
    
    private ColumnMetadata primaryColumn;
    
    private List<ColumnMetadata> columns;
    
    public void addColumnMetadata(ColumnMetadata columnMetadata) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(columnMetadata);
    }
}
