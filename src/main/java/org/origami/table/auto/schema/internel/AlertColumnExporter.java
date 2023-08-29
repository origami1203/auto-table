package org.origami.table.auto.schema.internel;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.schema.spi.Exporter;

import java.util.Optional;

/**
 * 添加字段
 *
 * @author origami
 * @date 2023/8/24 22:24
 */
@RequiredArgsConstructor
public class AlertColumnExporter implements Exporter<ColumnMetadata> {

    private final Dialect dialect;

    @Override
    public String[] getSqlCreateStrings(ColumnMetadata exportable) {
        final StringBuilder sb = new StringBuilder(dialect.getAlterTableString());
        sb.append(' ')
          .append(exportable.getTable().getTableName())
          .append(' ')
          .append(dialect.getAddColumnString())
          .append(' ')
          .append(exportable.getColumnName())
          .append(' ')
          .append(dialect.getActualTypeName(exportable.getJdbcType(), exportable.getLength(), exportable.getPrecision(),
              exportable.getScale()));

        Boolean nullable = Optional.ofNullable(exportable.getNullable()).orElse(Boolean.TRUE);

        if (nullable) {
            sb.append(dialect.getNullColumnString());
        } else {
            sb.append("not null");
        }
        sb.append(' ');

        String defaultValue = exportable.getDefaultValue();
        if (!Strings.isNullOrEmpty(defaultValue)) {
            sb.append(dialect.getDefaultString()).append(' ').append(defaultValue);
        }

        return new String[]{sb.toString()};
    }

    @Override
    public String[] getSqlDropStrings(ColumnMetadata exportable) {
        return new String[0];
    }
}
