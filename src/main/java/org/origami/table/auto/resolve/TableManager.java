package org.origami.table.auto.resolve;

import com.google.common.collect.ImmutableList;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.exception.UnresolvableException;

import java.util.List;

/**
 * @author origami
 * @date 2023/8/15 19:06
 */
public final class TableManager {
    
    private final List<TableTypeAnnotationResolver> resolves;
    private static final TableManager INSTANCE = new TableManager();
    
    private TableManager() {
        resolves = ImmutableList.of(new TableAnnotationResolver(),
                                    new TableNameAnnotationResolver(),
                                    new DefaultTableTypeResolverImpl());
    }
    
    public TableMetadata handle(Class<?> entity) {
        for (TableTypeAnnotationResolver resolver : resolves) {
            if (!resolver.supports(entity)) {
                continue;
            }
            return resolver.resolve(entity);
        }
        throw new UnresolvableException(String.format("不可解析的实体类:[%s]", entity.getName()));
    }
    
    public static TableManager getInstance() {
        return INSTANCE;
    }
    
}
