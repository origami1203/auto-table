package org.origami.table.auto.dialect;


import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author origami
 * @date 2023/8/7 19:16
 */
public final class TypeHandlerRegistry {
    
    private static final Map<Class<?>, MySQLDataType> classMysqlTypeMap = new ConcurrentHashMap<>();
    private static final Map<Integer, MySQLDataType> jdbcTypeMysqlTypeMap = new ConcurrentHashMap<>();
    
    static {
        register(Integer.class, MySQLDataType.INT);
        register(int.class, MySQLDataType.INT);
        register(Long.class, MySQLDataType.BIGINT);
        register(long.class, MySQLDataType.BIGINT);
        register(String.class, MySQLDataType.VARCHAR);
        register(boolean.class, MySQLDataType.BIT);
        register(Boolean.class, MySQLDataType.BIT);
        register(float.class, MySQLDataType.FLOAT);
        register(Float.class, MySQLDataType.FLOAT);
        register(double.class, MySQLDataType.DOUBLE);
        register(Double.class, MySQLDataType.DOUBLE);
        register(BigDecimal.class, MySQLDataType.DECIMAL);
        register(Date.class, MySQLDataType.DATE_TIME);
        register(LocalDateTime.class, MySQLDataType.DATE_TIME);
        register(LocalDate.class, MySQLDataType.DATE);
        register(Timestamp.class, MySQLDataType.TIMESTAMP);
        
        register(Types.BIT, MySQLDataType.BIT);
        register(Types.BOOLEAN, MySQLDataType.BIT);
        register(Types.TINYINT, MySQLDataType.TINYINT);
        register(Types.SMALLINT, MySQLDataType.SMALLINT);
        register(Types.INTEGER, MySQLDataType.INT);
        register(Types.BIGINT, MySQLDataType.BIGINT);
        register(Types.VARCHAR, MySQLDataType.VARCHAR);
        register(Types.CHAR, MySQLDataType.CHAR);
        register(Types.DATE, MySQLDataType.DATE_TIME);
        register(Types.TIMESTAMP, MySQLDataType.TIMESTAMP);
        register(Types.DECIMAL, MySQLDataType.DECIMAL);
        register(Types.DOUBLE, MySQLDataType.DOUBLE);
        register(Types.FLOAT, MySQLDataType.FLOAT);
        
    }
    
    
    public static <T> void register(Class<T> javaType, MySQLDataType type) {
        classMysqlTypeMap.put(javaType, type);
    }
    
    public static <T> void register(Integer jdbcType, MySQLDataType type) {
        jdbcTypeMysqlTypeMap.put(jdbcType, type);
    }
    
    /**
     * 获取类对应的jdbc类型，如果没有对应值，返回null
     *
     * @param clazz java类型
     * @return {@code JdbcType}
     */
    public static MySQLDataType getMysqlTypeByClassType(Class<?> clazz) {
        Assert.notNull(clazz, "class不能为空");
        return classMysqlTypeMap.get(clazz);
    }
    
    public static MySQLDataType getMysqlTypeByJdbcType(Integer jdbcType) {
        Assert.notNull(jdbcType, "jdbcType不能为空");
        return jdbcTypeMysqlTypeMap.get(jdbcType);
    }
}
