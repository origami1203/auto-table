package org.origami.table.auto.exception;

/**
 * sql异常
 *
 * @author origami
 * @date 2023/8/23 21:57
 */
public class SQLException extends RuntimeException {

    public SQLException(String message) {
        super(message);
    }

}
