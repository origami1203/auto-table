package org.origami.table.auto.resolve;

import com.google.common.collect.ImmutableList;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.exception.UnresolvableException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author origami
 * @date 2023/8/15 19:06
 */
public final class ColumnManager {
    
    private final List<ColumnAnnotationResolver> resolves;
    private static ColumnManager INSTANCE;
    
    public ColumnManager(Dialect dialect) {
        resolves = ImmutableList.of(new ColumnAnnotationResolverImpl(dialect),
                                    new TableIdAnnotationResolver(dialect),
                                    new TableFieldAnnotationResolverImpl(dialect),
                                    new DefaultColumnTypeResolver(dialect));
    }
    
    public ColumnMetadata handle(Field field) {
        for (ColumnAnnotationResolver resolver : resolves) {
            if (!resolver.supports(field)) {
                continue;
            }
            return resolver.resolve(field);
        }
        throw new UnresolvableException(String.format("不可解析的字段:[%s#%s]",
                                                      field.getDeclaringClass().getName(),
                                                      field.getName()));
    }
    
    public static synchronized ColumnManager getInstance(Dialect dialect) {
        if (INSTANCE == null) {
            INSTANCE = new ColumnManager(dialect);
        }
        return INSTANCE;
    }
    
}
