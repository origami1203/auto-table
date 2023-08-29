package org.origami.table.auto.resolve;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.utils.JDBCTypeHelper;

import java.lang.reflect.Field;

/**
 * @author origami
 * @date 2023/8/10 20:56
 */
@RequiredArgsConstructor
public class TableFieldAnnotationResolverImpl implements ColumnAnnotationResolver {

    @Override
    public ColumnMetadata resolve(Field field) {
        TableField tableFieldAnno = field.getAnnotation(TableField.class);
        String columnName = tableFieldAnno.value();
        JdbcType jdbcType = tableFieldAnno.jdbcType();
        String numericScale = tableFieldAnno.numericScale();
        Integer scale = null;
        Integer jdbcTypeCode = null;
        if (!Strings.isNullOrEmpty(numericScale)) {
            scale = Integer.valueOf(numericScale);
        }

        if (Strings.isNullOrEmpty(columnName)) {
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }

        if (JdbcType.UNDEFINED == jdbcType) {
            jdbcTypeCode = JDBCTypeHelper.getJdbcTypeCodeByClassType(field.getType());
        } else {
            jdbcTypeCode = jdbcType.TYPE_CODE;
        }

        return new ColumnMetadata().setPrimary(false)
                                   .setUnique(false)
                                   .setJdbcType(jdbcTypeCode)
                                   .setColumnName(columnName)
                                   .setScale(scale);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(TableField.class);
    }
}
