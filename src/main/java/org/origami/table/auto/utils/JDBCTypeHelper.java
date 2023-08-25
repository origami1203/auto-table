package org.origami.table.auto.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据
 *
 * @author origami
 * @date 2023/8/23 22:36
 */
public class JDBCTypeHelper {

    private static final Map<Class<?>, JdbcType> classJdbcTypeMap = new HashMap<>();

    static {
        register(String.class, JdbcType.VARCHAR);
        register(long.class, JdbcType.BIGINT);
        register(Long.class, JdbcType.BIGINT);
        register(int.class, JdbcType.INTEGER);
        register(Integer.class, JdbcType.INTEGER);
        register(short.class, JdbcType.SMALLINT);
        register(Short.class, JdbcType.SMALLINT);
        register(byte.class, JdbcType.TINYINT);
        register(Byte.class, JdbcType.TINYINT);
        register(boolean.class, JdbcType.BIT);
        register(Boolean.class, JdbcType.BIT);
        register(double.class, JdbcType.DOUBLE);
        register(Double.class, JdbcType.DOUBLE);
        register(float.class, JdbcType.FLOAT);
        register(Float.class, JdbcType.FLOAT);
        register(LocalDate.class, JdbcType.DATE);
        register(LocalTime.class, JdbcType.TIME);
        register(LocalDateTime.class, JdbcType.TIMESTAMP);
        register(Date.class, JdbcType.TIMESTAMP);
        register(BigDecimal.class, JdbcType.DECIMAL);
    }

    public static void register(Class<?> clazz, JdbcType jdbcType) {
        classJdbcTypeMap.put(clazz, jdbcType);
    }

    public static int getJdbcTypeCodeByClassType(Class<?> clazz) {
        JdbcType jdbcType = classJdbcTypeMap.get(clazz);
        return jdbcType == null ? JdbcType.OTHER.TYPE_CODE : jdbcType.TYPE_CODE;
    }
}
