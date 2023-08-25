package org.origami.table.auto.utils;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.origami.table.auto.constant.Constants;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.enums.SqlScript;

import java.util.Map;

/**
 * @author origami
 * @date 2023/8/14 23:45
 */
@Deprecated
@UtilityClass
public class SqlHelper {

    public String createTable(TableMetadata tableMetadata) {
        String createTableScript = SqlScript.CREATE_TABLE.getScript();

        ColumnMetadata primaryColumn = tableMetadata.getPrimaryColumn();
        if (primaryColumn == null) {
            throw new RuntimeException(String.format("%s 没有主键", tableMetadata.getTableName()));
        }
        Map<String, ColumnMetadata> columns = tableMetadata.getColumns();

        StringBuilder sb = new StringBuilder();
        String idComment = primaryColumn.getComment();
        if (Strings.isNullOrEmpty(idComment)) {
            idComment = "主键id";
        }
        sb.append(primaryColumn.getColumnName())
          .append(Constants.SPACE)
          .append(primaryColumn.getActualTypeName())
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

        for (ColumnMetadata column : columns.values()) {
            sb.append(column.getColumnName()).append(Constants.SPACE);

            String columnDefinition = column.getColumnDefinition();

            // 若定义了columnDefinition,则全部走columnDefinition，不再走其他所有自定义配置
            if (!Strings.isNullOrEmpty(columnDefinition)) {
                sb.append(columnDefinition).append(Constants.COMMA);
            } else {

                sb.append(column.getActualTypeName()).append(Constants.SPACE);

                if (column.getNullable()) {
                    sb.append(Constants.DEFAULT).append(Constants.SPACE).append(Constants.NULLABLE);
                } else {
                    sb.append(Constants.NOT_NULL);
                }
                String comment = column.getComment();
                if (!Strings.isNullOrEmpty(comment)) {
                    sb.append(Constants.SPACE)
                      .append(Constants.COMMENT)
                      .append(Constants.SPACE)
                      .append(Constants.QUOTA)
                      .append(comment)
                      .append(Constants.QUOTA);
                }
                sb.append(Constants.COMMA);
            }
        }

        String sql = String.format(createTableScript, tableMetadata.getTableName(), sb.deleteCharAt(sb.length() - 1));

        if (!Strings.isNullOrEmpty(tableMetadata.getComment())) {
            sql = sql + Constants.COMMENT + Constants.SPACE + Constants.QUOTA + tableMetadata.getComment() +
                  Constants.QUOTA;
        }
        return sql;
    }

    public String addColumn(ColumnMetadata columnMetadata) {
        String sqlTemplate = SqlScript.ADD_COLUMN.getScript();

        StringBuilder sb = new StringBuilder();

        sb.append(columnMetadata.getColumnName())
          .append(Constants.SPACE)
          .append(columnMetadata.getActualTypeName())
          .append(Constants.SPACE);

        if (columnMetadata.getDefaultValue() != null) {
            sb.append(columnMetadata.getDefaultValue()).append(Constants.SPACE);
        }
        if (columnMetadata.getNullable()) {
            sb.append(Constants.NULLABLE).append(Constants.SPACE);
        }

        if (!Strings.isNullOrEmpty(columnMetadata.getComment())) {
            sb.append(Constants.COMMENT).append(Constants.SPACE);
        }
        sb.append(";");

        return String.format(sqlTemplate, columnMetadata.getTable().getTableName(), sb.toString());
    }
}
