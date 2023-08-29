package org.origami.table.auto.resolve;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.origami.table.auto.constant.Constants;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.utils.JDBCTypeHelper;

import java.lang.reflect.Field;

/**
 * @author origami
 * @date 2023/8/10 22:09
 */
public class TableIdAnnotationResolver implements ColumnAnnotationResolver {

    @Override
    public ColumnMetadata resolve(Field field) {
        TableId tableId = field.getAnnotation(TableId.class);
        boolean isAutoIncrement = false;
        Integer jdbcTypeCode = JDBCTypeHelper.getJdbcTypeCodeByClassType(field.getType());

        IdType idType = tableId.type();
        String columnName = tableId.value();

        if (Strings.isNullOrEmpty(columnName)) {
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }

        if (IdType.AUTO == idType) {
            isAutoIncrement = true;
        }

        return new ColumnMetadata().setJdbcType(jdbcTypeCode)
                                   .setColumnName(columnName)
                                   .setPrimary(true)
                                   .setIsAutoIncrement(isAutoIncrement)
                                   .setNullable(false)
                                   .setComment(Constants.DEFAULT_ID_COMMENT);
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(TableId.class);
    }
}
