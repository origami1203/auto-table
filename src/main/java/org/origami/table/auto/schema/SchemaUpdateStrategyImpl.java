package org.origami.table.auto.schema;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author origami
 * @date 2023/8/16 20:57
 */
@Slf4j
public class SchemaUpdateStrategyImpl implements SchemaStrategy {
    @Override
    public void handle(JdbcTemplate jdbcTemplate,
                       Dialect dialect,
                       DatabaseMetadata databaseMetadata,
                       TableMetadata entityConvertedTable) {
        List<String> sqlStrings = new ArrayList<>();

        String tableName = entityConvertedTable.getTableName();

        if (!databaseMetadata.tableExists(tableName)) {
            log.info("AutoTable: 新建的实体类[{}],需要创建新的table", entityConvertedTable.getEntityClass().getName());
            sqlStrings.addAll(Arrays.asList(dialect.getTableExporter().getSqlCreateStrings(entityConvertedTable)));
        } else {
            TableMetadata existsTable = databaseMetadata.getByTableName(tableName);
            Map<String, ColumnMetadata> existsColumn = existsTable.getColumns();

            MapDifference<String, ColumnMetadata> difference =
                Maps.difference(existsColumn, entityConvertedTable.getColumns());

            Map<String, ColumnMetadata> addColumns = difference.entriesOnlyOnRight();

            for (ColumnMetadata columnMetadata : addColumns.values()) {
                sqlStrings.addAll(Arrays.asList(dialect.getAlertColumnExporter().getSqlCreateStrings(columnMetadata)));
            }
        }

        applySqlStrings(sqlStrings, jdbcTemplate);

    }

    private void applySqlStrings(List<String> sqlStrings, JdbcTemplate jdbcTemplate) {
        if (CollectionUtils.isEmpty(sqlStrings)) {
            return;
        }

        for (String sqlString : sqlStrings) {
            jdbcTemplate.execute(sqlString);
            if (sqlString.startsWith("create")) {
                log.debug("AutoTable: 建表:[{}]", sqlString);
                continue;
            }
            log.debug("AutoTable: add column语句:[{}]", sqlString);
        }
    }
}
