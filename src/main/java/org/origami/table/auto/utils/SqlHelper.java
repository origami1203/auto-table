package org.origami.table.auto.utils;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.origami.table.auto.constant.Constants;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.enums.SqlScript;

import java.util.List;

/**
 * @author origami
 * @date 2023/8/14 23:45
 */
@UtilityClass
public class SqlHelper {
    
    public String createTable(TableMetadata tableMetadata) {
        String createTableScript = SqlScript.CREATE_TABLE.getScript();
        
        ColumnMetadata primaryColumn = tableMetadata.primaryColumn();
        if (primaryColumn == null) {
            throw new RuntimeException(String.format("%s 没有主键", tableMetadata.tableName()));
        }
        List<ColumnMetadata> columns = tableMetadata.columns();
        
        StringBuilder sb = new StringBuilder();
        String idComment = primaryColumn.comment();
        if (Strings.isNullOrEmpty(idComment)) {
            idComment = "主键id";
        }
        sb.append(primaryColumn.columnName())
          .append(Constants.SPACE)
          .append(primaryColumn.actualTypeName())
          .append(Constants.SPACE)
          .append(Constants.NOT_NULL)
          .append(Constants.SPACE)
          .append(Constants.PRIMARY_KEY)
          .append(Constants.SPACE)
          .append(Constants.COMMENT)
          .append(Constants.SPACE)
          .append(Constants.QUOTA)
          .append(idComment)
          .append(Constants.QUOTA)
          .append(Constants.COMMA);
        
        for (ColumnMetadata column : columns) {
            sb.append(column.columnName())
              .append(Constants.SPACE)
              .append(column.actualTypeName())
              .append(Constants.SPACE);
            
            if (column.nullable()) {
                sb.append(Constants.DEFAULT).append(Constants.SPACE).append(Constants.NULLABLE);
            } else {
                sb.append(Constants.NOT_NULL);
            }
            String comment = column.comment();
            if (!Strings.isNullOrEmpty(comment)) {
                sb.append(Constants.SPACE).append(Constants.COMMENT).append(comment);
            }
            sb.append(Constants.COMMA);
        }
        
        String sql =
                String.format(createTableScript, tableMetadata.tableName(), sb.deleteCharAt(sb.length() - 1),// 删除最后的逗号
                              tableMetadata.engine(), tableMetadata.charset());
        
        if (!Strings.isNullOrEmpty(tableMetadata.comment())) {
            sql = sql + Constants.COMMENT + Constants.QUOTA + Constants.QUOTA + tableMetadata.comment() +
                  Constants.QUOTA;
        }
        return sql;
    }
}
