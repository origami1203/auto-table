package org.origami.table.auto.annotations;

import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仿照jpa的@column注解
 *
 * @author origami
 * @date 2023/8/4 23:56
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {

    /**
     * 是否是主键
     */
    boolean primary() default false;

    /**
     * 对应到数据库的column列名,默认为字段名小驼峰转下划线
     */
    String name() default "";

    /**
     * TODO 是否是唯一索引
     */
    // boolean unique() default false;

    /**
     * 字段是否可为空
     */
    boolean nullable() default true;

    /**
     * 类似jpa的columnDefinition<br/>
     * 与jdbcType指定时，columnDefinition定义优先
     */
    String columnDefinition() default "";

    /**
     * 对应的jdbcType,与columnDefinition同时指定时，columnDefinition优先
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * varchar类型的字符长度
     */
    int length() default 255;

    /**
     * 精度，big-decimal
     */
    int precision() default 10;

    /**
     * 小数点后保留位数
     */
    int scale() default 2;

    /**
     * 备注
     */
    String comment() default "";

    /**
     * 是否忽略创建该字段
     */
    boolean ignore() default false;

    /**
     * 默认值
     */
    String defaultValue() default "";

}
