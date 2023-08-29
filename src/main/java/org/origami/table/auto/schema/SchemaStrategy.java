package org.origami.table.auto.schema;

import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * schema策略接口
 *
 * @author origami
 * @date 2023/8/16 19:16
 */
public interface SchemaStrategy {

    /**
     * 处理数据
     *
     * @param jdbcTemplate     jdbcTemplate
     * @param dialect          方言
     * @param databaseMetadata 数据库元数据
     * @param tableMetadata    table元数据
     */
    void handle(JdbcTemplate jdbcTemplate,
                Dialect dialect,
                DatabaseMetadata databaseMetadata,
                TableMetadata tableMetadata);
}
