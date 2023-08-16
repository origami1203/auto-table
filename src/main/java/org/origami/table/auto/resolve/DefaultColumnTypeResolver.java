package org.origami.table.auto.resolve;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.dialect.Dialect;

import java.lang.reflect.Field;

/**
 * @author origami
 * @date 2023/8/15 19:41
 */
@RequiredArgsConstructor
public class DefaultColumnTypeResolver implements ColumnAnnotationResolver {
    
    private final Dialect dialect;
    
    @Override
    public ColumnMetadata resolve(Field field) {
        String actualTypeName = dialect.getActualTypeName(field.getType(), null, null, null);
        String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        
        return new ColumnMetadata().columnName(columnName).actualTypeName(actualTypeName);
    }
    
    @Override
    public boolean supports(Field field) {
        return true;
    }
}
