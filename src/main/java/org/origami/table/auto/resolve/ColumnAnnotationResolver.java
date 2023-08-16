package org.origami.table.auto.resolve;

import org.origami.table.auto.core.ColumnMetadata;

import java.lang.reflect.Field;

/**
 * 数据库列类型注解解析<p/>
 * 例如{@link com.baomidou.mybatisplus.annotation.TableField}注解解析 <br/>
 * 例如{@link org.origami.table.auto.annotations.Column}注解解析 <br/>
 *
 * @author origami
 * @date 2023/8/10 20:55
 */
public interface ColumnAnnotationResolver {
    
    /**
     * 解析指定字段上的注解
     *
     * @param field entity的字段
     * @return {@code ColumnMetadata}
     */
    ColumnMetadata resolve(Field field);
    
    /**
     * 该解析器是否支持该字段解析
     *
     * @param field entity的字段
     * @return boolean
     */
    boolean supports(Field field);
}
