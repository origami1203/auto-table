package org.origami.table.auto.resolve;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import org.origami.table.auto.constant.Constants;
import org.origami.table.auto.core.TableMetadata;

/**
 * @author origami
 * @date 2023/8/10 22:27
 */
public class TableNameAnnotationResolver implements TableTypeAnnotationResolver {
    @Override
    public TableMetadata resolve(Class<?> entityClass) {
        TableName tableNameAnno = entityClass.getAnnotation(TableName.class);
        String tableName = tableNameAnno.value();

        if (Strings.isNullOrEmpty(tableName)) {
            tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName());
        }
        return new TableMetadata().setTableName(tableName).setEntityClass(entityClass);
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return entityClass.isAnnotationPresent(TableName.class);
    }
}
