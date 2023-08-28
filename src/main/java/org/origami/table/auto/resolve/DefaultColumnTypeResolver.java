package org.origami.table.auto.resolve;

import com.google.common.base.CaseFormat;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.utils.JDBCTypeHelper;

import java.lang.reflect.Field;

/**
 * @author origami
 * @date 2023/8/15 19:41
 */
public class DefaultColumnTypeResolver implements ColumnAnnotationResolver {

    @Override
    public ColumnMetadata resolve(Field field) {
        Integer jdbcTypeCode = JDBCTypeHelper.getJdbcTypeCodeByClassType(field.getType());
        String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());

        return new ColumnMetadata().setColumnName(columnName).setJdbcType(jdbcTypeCode);
    }

    @Override
    public boolean supports(Field field) {
        return true;
    }
}
