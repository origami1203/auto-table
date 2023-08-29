package org.origami.table.auto.schema;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 建表
 *
 * @author origami
 * @date 2023/8/16 19:26
 */
@Slf4j
public class SchemaCreatorStrategyImpl implements SchemaStrategy {

    @Override
    public void handle(JdbcTemplate jdbcTemplate,
                       Dialect dialect,
                       DatabaseMetadata databaseMetadata,
                       TableMetadata tableMetadata) {

        log.debug("AutoTable: 新建表:[{}]", tableMetadata.getTableName());
        String[] sqlCreateStrings = dialect.getTableExporter().getSqlCreateStrings(tableMetadata);

        applySqlStrings(sqlCreateStrings, jdbcTemplate);
    }

    private void applySqlStrings(String[] sqlCreateStrings, JdbcTemplate template) {
        if (sqlCreateStrings == null) {
            return;
        }

        for (String sqlString : sqlCreateStrings) {
            if (Strings.isNullOrEmpty(sqlString)) {
                return;
            }
            template.execute(sqlString);
            log.debug("AutoTable: 建表语句:[{}]", sqlString);
        }
    }

}
