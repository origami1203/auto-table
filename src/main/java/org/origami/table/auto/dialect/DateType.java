package org.origami.table.auto.dialect;

/**
 * 数据库数据类型
 *
 * @author origami
 * @date 2023/8/15 21:47
 */
public interface DateType {

    String getTemplate();

    Integer getLength();

    Integer getScale();

    Integer getPrecision();
}
