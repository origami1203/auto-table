package org.origami.table.auto.dialect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * mysql的数据类型
 *
 * @author origami
 * @date 2023/8/11 12:47
 */
@Getter
@AllArgsConstructor
public enum MySQLDataType implements DateType {
    
    // @formatter:off
    BIT("bit($l)", 1L, null, null),
    TINYINT("tinyint", null, null, null),
    SMALLINT("smallint", null, null, null),
    INT("int", null, null, null),
    BIGINT("bigint", null, null, null),
    VARCHAR("varchar($l)", 255L, null, null),
    CHAR("char($l)", 10L, null, null),
    DECIMAL("decimal($p,$s)", null, 10, 2),
    DATE_TIME("datetime", null, null, null),
    FLOAT("float($p)", null, 10, null),
    DOUBLE("double(%p)", null, 10, null),
    DATE("date", null, null, null),
    TIME("time", null, null, null),
    TIMESTAMP("timestamp", null, null, null),
    BLOB("blob", null, null, null),
    UNDEFINED("undefined", null, null, null),
    ;
    // @formatter:on
    
    /**
     * 模板
     */
    private final String template;
    /**
     * 字符串类型长度
     */
    private final Long length;
    /**
     * 精度
     */
    private final Integer precision;
    /**
     * 小数点位数
     */
    private final Integer scale;
}
