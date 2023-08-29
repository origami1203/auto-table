package org.origami.table.auto.schema.internel;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.exception.SQLException;
import org.origami.table.auto.schema.spi.Exporter;

import java.util.Map;
import java.util.Optional;

/**
 * 建表
 *
 * @author origami
 * @date 2023/8/24 21:10
 */
@RequiredArgsConstructor
public class StandardTableExporter implements Exporter<TableMetadata> {

    private final Dialect dialect;

    @Override
    public String[] getSqlCreateStrings(TableMetadata tableMetadata) {

        final StringBuilder sb = new StringBuilder(dialect.getCreateTableString());

        sb.append(' ').append(tableMetadata.getTableName()).append(" (");

        ColumnMetadata primaryColumn = tableMetadata.getPrimaryColumn();
        if (primaryColumn == null) {
            throw new SQLException(String.format("AutoTable: [%s] 没有主键", tableMetadata.getTableName()));
        }

        sb.append(primaryColumn.getColumnName())
          .append(' ')
          .append(dialect.getActualTypeName(primaryColumn.getJdbcType(), primaryColumn.getLength(),
              primaryColumn.getPrecision(), primaryColumn.getScale()))
          .append(" not null")
          .append(" primary key,");

        Map<String, ColumnMetadata> columns = tableMetadata.getColumns();

        for (Map.Entry<String, ColumnMetadata> entry : columns.entrySet()) {
            String columnName = entry.getKey();
            ColumnMetadata column = entry.getValue();

            sb.append(columnName).append(' ');

            String columnDefinition = column.getColumnDefinition();
            if (!Strings.isNullOrEmpty(columnDefinition)) {
                sb.append(columnDefinition).append(",");
                continue;
            }

            sb.append(dialect.getActualTypeName(column.getJdbcType(), column.getLength(), column.getPrecision(),
                column.getScale()));

            String defaultValue = column.getDefaultValue();
            if (!Strings.isNullOrEmpty(defaultValue)) {
                sb.append(" default ").append(defaultValue);
            }

            Boolean nullable = Optional.ofNullable(column.getNullable()).orElse(Boolean.TRUE);
            if (nullable) {
                sb.append(dialect.getNullColumnString()).append(' ');
            } else {
                sb.append(" not null ");
            }

            String columnComment = column.getComment();
            if (!Strings.isNullOrEmpty(columnComment)) {
                sb.append(dialect.getColumnComment(columnComment));
            }
            sb.append(",");
        }

        sb.deleteCharAt(sb.length() - 1).append(" ) ");

        String tableComment = tableMetadata.getComment();
        if (!Strings.isNullOrEmpty(tableComment)) {
            sb.append(dialect.getTableComment(tableComment)).append(' ');
        }

        sb.append(dialect.getTableTypeString());

        return new String[]{sb.toString()};
    }

    @Override
    public String[] getSqlDropStrings(TableMetadata exportable) {
        return new String[0];
    }

}
