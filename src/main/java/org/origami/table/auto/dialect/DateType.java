package org.origami.table.auto.dialect;

/**
 * @author origami
 * @date 2023/8/15 21:47
 */
public interface DateType {
    
    String getTemplate();
    
    Long getLength();
    
    Integer getScale();
    
    Integer getPrecision();
}
