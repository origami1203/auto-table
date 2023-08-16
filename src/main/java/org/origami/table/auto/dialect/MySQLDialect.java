package org.origami.table.auto.dialect;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * mysql方言类
 *
 * @author origami
 * @date 2023/8/15 18:46
 */
public class MySQLDialect extends Dialect {
    
    public MySQLDialect() {
        registerColumnType(Types.BIT, MySQLDataType.BIT);
        registerColumnType(Types.BOOLEAN, MySQLDataType.BIT);
        registerColumnType(Types.TINYINT, MySQLDataType.TINYINT);
        registerColumnType(Types.SMALLINT, MySQLDataType.SMALLINT);
        registerColumnType(Types.INTEGER, MySQLDataType.INT);
        registerColumnType(Types.BIGINT, MySQLDataType.BIGINT);
        registerColumnType(Types.VARCHAR, MySQLDataType.VARCHAR);
        registerColumnType(Types.CHAR, MySQLDataType.CHAR);
        registerColumnType(Types.DATE, MySQLDataType.DATE_TIME);
        registerColumnType(Types.TIMESTAMP, MySQLDataType.TIMESTAMP);
        registerColumnType(Types.DECIMAL, MySQLDataType.DECIMAL);
        registerColumnType(Types.DOUBLE, MySQLDataType.DOUBLE);
        registerColumnType(Types.FLOAT, MySQLDataType.FLOAT);
        
        registerClassType(boolean.class,MySQLDataType.BIT);
        registerClassType(Boolean.class, MySQLDataType.BIT);
        registerClassType(byte.class, MySQLDataType.TINYINT);
        registerClassType(Byte.class, MySQLDataType.TINYINT);
        registerClassType(Short.class, MySQLDataType.SMALLINT);
        registerClassType(short.class, MySQLDataType.SMALLINT);
        registerClassType(int.class, MySQLDataType.INT);
        registerClassType(Integer.class, MySQLDataType.INT);
        registerClassType(long.class, MySQLDataType.BIGINT);
        registerClassType(Long.class, MySQLDataType.BIGINT);
        registerClassType(Date.class, MySQLDataType.DATE_TIME);
        registerClassType(LocalDateTime.class, MySQLDataType.DATE_TIME);
        registerClassType(LocalDate.class, MySQLDataType.DATE);
        registerClassType(LocalTime.class, MySQLDataType.TIME);
        registerClassType(Timestamp.class, MySQLDataType.TIMESTAMP);
        registerClassType(BigDecimal.class, MySQLDataType.DECIMAL);
        registerClassType(double.class, MySQLDataType.DOUBLE);
        registerClassType(Double.class, MySQLDataType.DOUBLE);
        registerClassType(Float.class, MySQLDataType.FLOAT);
        registerClassType(float.class, MySQLDataType.FLOAT);
        registerClassType(String.class, MySQLDataType.VARCHAR);
    }
    
}
