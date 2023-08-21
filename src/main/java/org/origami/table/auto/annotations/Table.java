package org.origami.table.auto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * table注解
 *
 * @author origami
 * @date 2023/8/5 12:18
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 表名,默认为类名转下划线
     */
    String value() default "";

    /**
     * 备注
     */
    String comment() default "";
}
