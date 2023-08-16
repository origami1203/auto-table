package org.origami.table.auto.dialect;

import org.origami.table.auto.constant.Constants;
import org.origami.table.auto.exception.UnmappedTypeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * 用于获取实际对应的type，例如string获取varchar(255)
 * 与org.hibernate.dialect.TypeNames一致
 *
 * @author origami
 * @date 2023/8/15 8:15
 */
public final class TypeNames {
    /**
     * jdbcType和实际类型的映射，例如Types.VARCHAR 和 varchar(%l)
     */
    private final Map<Integer, DateType> codeTypeMap = new HashMap<>();
    
    /**
     * 指定java类与jdbc type的映射，例如String.class和 varchar(%l)
     */
    private final Map<Class<?>, DateType> clazzTypeMap = new HashMap<>();
    
    /**
     * get default type name for specified type
     *
     * @param typeCode the type key
     * @return the default type name associated with specified key
     */
    public String getByTypeCode(final int typeCode, Long length, Integer precision, Integer scale) {
        final Integer integer = typeCode;
        final DateType dateType = codeTypeMap.get(integer);
        if (dateType == null) {
            throw new UnmappedTypeException(String.format("未映射的jdbc类型: %d", typeCode));
        }
        return getString(length, precision, scale, dateType);
    }
    
    /**
     * get type name for specified type and size
     *
     * @param length    the SQL length
     * @param scale     the SQL scale
     * @param precision the SQL precision
     * @return the associated name with smallest capacity >= size, if available and the default type name otherwise
     */
    public String getByClassType(Class<?> clazz, Long length, Integer precision, Integer scale) {
        DateType dateType = clazzTypeMap.get(clazz);
        if (dateType == null) {
            throw new UnmappedTypeException(String.format("未映射的java类型: %s", clazz.getName()));
        }
        return getString(length, precision, scale, dateType);
    }
    
    
    /**
     * Register a default (non-weighted) typeCode mapping
     *
     * @param typeCode the type key
     * @param dateType The mapping (type name)
     */
    public void put(int typeCode, DateType dateType) {
        final Integer integer = Integer.valueOf(typeCode);
        codeTypeMap.put(integer, dateType);
    }
    
    public void put(Class<?> classType, DateType value) {
        clazzTypeMap.put(classType, value);
    }
    
    /**
     * Check whether or not the provided typeName exists.
     *
     * @param typeName the type name.
     * @return true if the given string has been registered as a type.
     */
    public boolean containsTypeName(final String typeName) {
        return this.codeTypeMap.containsValue(typeName);
    }
    
    /**
     * 占位符替换
     *
     * @param template  模版
     * @param length    长度
     * @param precision 精度
     * @param scale     小数点位数
     * @return 将varchar(% l)转换为varchar(255)，将decimal(%p,%s)转换为decimal(10,2)
     */
    private static String replace(String template, long length, int precision, int scale) {
        template = replaceOnce(template, "$s", Integer.toString(scale));
        template = replaceOnce(template, "$l", Long.toString(length));
        return replaceOnce(template, "$p", Integer.toString(precision));
    }
    
    private static String replaceOnce(String template, String placeholder, String replacement) {
        if (template == null) {
            return null;  // returning null!
        }
        int loc = template.indexOf(placeholder);
        if (loc < 0) {
            return template;
        } else {
            return template.substring(0, loc) + replacement + template.substring(loc + placeholder.length());
        }
    }
    
    private String getString(Long length, Integer precision, Integer scale, DateType dateType) {
        if (length == null) {
            length = Optional.ofNullable(dateType.getLength()).orElse(Constants.DEFAULT_LONG_PLACEHOLDER);
        }
        if (precision == null) {
            precision = Optional.ofNullable(dateType.getPrecision()).orElse(Constants.DEFAULT_INTEGER_PLACEHOLDER);
        }
        if (scale == null) {
            scale = Optional.ofNullable(dateType.getScale()).orElse(Constants.DEFAULT_INTEGER_PLACEHOLDER);
        }
        
        return replace(dateType.getTemplate(), length, precision, scale);
    }
}
