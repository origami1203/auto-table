package org.origami.table.auto.resolve;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import org.origami.table.auto.annotations.Table;
import org.origami.table.auto.core.TableMetadata;

/**
 * @author origami
 * @date 2023/8/10 12:12
 */
public class TableAnnotationResolver implements TableTypeAnnotationResolver {
    @Override
    public TableMetadata resolve(Class<?> entityClass) {
        Table tableAnno = entityClass.getAnnotation(Table.class);
        String tableName = tableAnno.value();
        String comment = tableAnno.comment();

        if (Strings.isNullOrEmpty(tableName)) {
            tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName());
        }

        return new TableMetadata().setTableName(tableName).setComment(comment);
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return entityClass.isAnnotationPresent(Table.class);
    }
}
