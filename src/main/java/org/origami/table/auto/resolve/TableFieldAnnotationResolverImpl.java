package org.origami.table.auto.resolve;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.origami.table.auto.constant.Constants;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * @author origami
 * @date 2023/8/10 20:56
 */
@RequiredArgsConstructor
public class TableFieldAnnotationResolverImpl implements ColumnAnnotationResolver {
    
    private final Dialect dialect;
    
    @Override
    public ColumnMetadata resolve(Field field) {
        TableField tableFieldAnno = field.getAnnotation(TableField.class);
        String columnName = tableFieldAnno.value();
        JdbcType jdbcType = tableFieldAnno.jdbcType();
        String numericScale = tableFieldAnno.numericScale();
        String actualTypeName;
        Integer scale = null;
        if (!Strings.isNullOrEmpty(numericScale)) {
            scale = Integer.valueOf(numericScale);
        }
        
        if (Strings.isNullOrEmpty(columnName)) {
            columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
        
        
        if (JdbcType.UNDEFINED == jdbcType) {
            actualTypeName = dialect.getActualTypeName(field.getType(), null, null, scale);
        } else {
            actualTypeName = dialect.getActualTypeName(jdbcType.TYPE_CODE, null, null, scale);
        }
        
        if (Strings.isNullOrEmpty(actualTypeName)) {
            throw new RuntimeException("actualTypeName不能为空");
        }
        
        return new ColumnMetadata().actualTypeName(actualTypeName)
                                   .primary(false)
                                   .unique(false)
                                   .columnName(columnName)
                                   .scale(scale);
    }
    
    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(TableField.class);
    }
}
