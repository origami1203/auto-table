package org.origami.table.auto.exception;

/**
 * @author origami
 * @date 2023/8/15 23:48
 */
public class UnsupportedDatabaseException extends RuntimeException {
    
    public UnsupportedDatabaseException(String msg) {
        super(msg);
    }
}
