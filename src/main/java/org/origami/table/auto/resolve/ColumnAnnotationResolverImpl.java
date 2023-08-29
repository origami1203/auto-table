package org.origami.table.auto.resolve;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.origami.table.auto.annotations.Column;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.utils.JDBCTypeHelper;

import java.lang.reflect.Field;

/**
 * @author origami
 * @date 2023/8/19 12:21
 */
@RequiredArgsConstructor
public class ColumnAnnotationResolverImpl implements ColumnAnnotationResolver {

    @Override
    public ColumnMetadata resolve(Field field) {
        Column columnAnno = field.getAnnotation(Column.class);

        String columnDefinition = columnAnno.columnDefinition();
        String columnName = columnAnno.name();
        boolean primary = columnAnno.primary();
        boolean nullable = columnAnno.nullable();
        String comment = columnAnno.comment();
        String defaultValue = columnAnno.defaultValue();
        JdbcType jdbcType = columnAnno.jdbcType();
        int length = columnAnno.length();
        int scale = columnAnno.scale();
        int precision = columnAnno.precision();

        Integer jdbcTypeCode = null;

        if (Strings.isNullOrEmpty(columnName)) {
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }

        if (JdbcType.UNDEFINED == jdbcType) {
            jdbcTypeCode = JDBCTypeHelper.getJdbcTypeCodeByClassType(field.getType());
        } else {
            jdbcTypeCode = jdbcType.TYPE_CODE;
        }

        if (primary) {
            nullable = false;
        }

        return new ColumnMetadata().setColumnName(columnName)
                                   .setPrimary(primary)
                                   .setNullable(nullable)
                                   .setComment(comment)
                                   .setJdbcType(jdbcTypeCode)
                                   .setLength(length)
                                   .setPrecision(precision)
                                   .setScale(scale)
                                   .setDefaultValue(defaultValue)
                                   .setColumnDefinition(columnDefinition);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(Column.class);
    }
}
