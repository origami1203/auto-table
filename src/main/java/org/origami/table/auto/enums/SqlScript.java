package org.origami.table.auto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * sql语句模板
 *
 * @author origami
 * @date 2023/8/8 19:51
 */
@Getter
@AllArgsConstructor
public enum SqlScript {
    
    CREATE_TABLE("CREATE TABLE IF NOT EXISTS %s (%s) ENGINE=%s CHARSET = %s");
    
    private final String script;
}
