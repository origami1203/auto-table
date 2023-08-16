package org.origami.table.auto.resolve;

import org.origami.table.auto.core.TableMetadata;

/**
 * 默认实现类，当其他解析器不能解析时，使用该解析器解析
 *
 * @author origami
 * @date 2023/8/14 19:00
 */
public class DefaultTableTypeResolverImpl implements TableTypeAnnotationResolver {
    @Override
    public TableMetadata resolve(Class<?> entityClass) {
        return null;
    }
    
    @Override
    public boolean supports(Class<?> entityClass) {
        return true;
    }
}
