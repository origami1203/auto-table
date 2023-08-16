package org.origami.table.auto.resolve;

import org.origami.table.auto.core.TableMetadata;

/**
 * 数据库表类型的注解解析器，例如@TabelName，@Table
 *
 * @author origami
 * @date 2023/8/10 8:07
 */
public interface TableTypeAnnotationResolver {
    
    /**
     * 解析指定的entity类
     *
     * @param entityClass 实体类
     * @return {@code TableMetadata}
     */
    TableMetadata resolve(Class<?> entityClass);
    
    /**
     * 是否支持对该类解析
     *
     * @param entityClass 实体类
     * @return boolean
     */
    boolean supports(Class<?> entityClass);
    
}
