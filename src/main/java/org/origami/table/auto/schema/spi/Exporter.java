package org.origami.table.auto.schema.spi;

import org.origami.table.auto.dialect.Dialect;

/**
 * 定义用于创建和删除 table,index等的sql语句，暂时只用来创建table
 *
 * @author origami
 * @date 2023/8/24 20:48
 */
public interface Exporter<T> {

    /**
     * Get the commands needed for creation.
     *
     * @return The commands needed for creation scripting.
     */
    String[] getSqlCreateStrings(T exportable);

    /**
     * Get the commands needed for dropping.
     *
     * @return The commands needed for drop scripting.
     */
    String[] getSqlDropStrings(T exportable);

}
