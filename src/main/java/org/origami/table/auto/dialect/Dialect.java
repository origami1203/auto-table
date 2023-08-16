package org.origami.table.auto.dialect;

import org.origami.table.auto.exception.UnmappedTypeException;

/**
 * 方言,仿照hibernate
 *
 * @author origami
 * @date 2023/8/15 8:11
 */
public abstract class Dialect {
    
    private final TypeNames typeNames = new TypeNames();
    
    public String getActualTypeName(Integer code, Long length, Integer precision, Integer scale) {
        final String result = typeNames.getByTypeCode(code, length, precision, scale);
        if (result == null) {
            throw new UnmappedTypeException(String.format("未映射的jdbc类型: %d", code));
        }
        return result;
    }
    
    public String getActualTypeName(Class<?> clazz, Long length, Integer precision, Integer scale) {
        
        final String result = typeNames.getByClassType(clazz, length, precision, scale);
        if (result == null) {
            throw new UnmappedTypeException(String.format("未映射的java类型: %s", clazz.getName()));
        }
        return result;
    }
    
    
    protected void registerColumnType(int code, DateType dateType) {
        typeNames.put(code, dateType);
    }
    
    protected void registerClassType(Class<?> clazz, DateType dateType) {
        typeNames.put(clazz, dateType);
    }
}
