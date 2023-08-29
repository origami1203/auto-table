package org.origami.table.auto.schema;

import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.DatabaseMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.exception.ValidationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * 校验策略，验证数据库与实体类的不同，但不做任何修改<br/>
 * 两者不一致会抛出异常
 *
 * @author origami
 * @date 2023/8/21 21:58
 */
@Slf4j
public class SchemaValidateStrategyImpl implements SchemaStrategy {
    @Override
    public void handle(JdbcTemplate jdbcTemplate,
                       Dialect dialect, DatabaseMetadata databaseMetadata, TableMetadata tableMetadata) {

        String tableName = tableMetadata.getTableName();

        if (!databaseMetadata.tableExists(tableName)) {
            log.error("AutoTable: 新增的实体类:[{}]", tableMetadata.getEntityClass().getName());
            throw new ValidationException(
                String.format("AutoTable: 校验失败,新增实体类[%s]", tableMetadata.getEntityClass().getName()));
        }

        Map<String, ColumnMetadata> alreadyExistColumns = databaseMetadata.getByTableName(tableName).getColumns();
        Map<String, ColumnMetadata> entityClassColumns = tableMetadata.getColumns();

        MapDifference<String, ColumnMetadata> difference =
            Maps.difference(alreadyExistColumns, entityClassColumns, ColumnMatedataEquivalence.INSTANCE);

        Map<String, ColumnMetadata> deletedColumns = difference.entriesOnlyOnLeft();
        Map<String, ColumnMetadata> addColumns = difference.entriesOnlyOnRight();
        Map<String, ColumnMetadata> bothExistColumns = difference.entriesInCommon();
        Map<String, MapDifference.ValueDifference<ColumnMetadata>> stringValueDifferenceMap =
            difference.entriesDiffering();

        log.info("实体类[{}]存在以下变更:", tableMetadata.getEntityClass().getSimpleName());

        deletedColumns.forEach((columnName, column) -> {
            log.info("[{}]数据表中存在名为[{}]的列,实体类中已删除", column.getTable().getTableName(), columnName);
        });
        addColumns.forEach((columnName, column) -> {
            log.info("[{}]实体类中新增名为[{}]的字段", column.getTable().getEntityClass().getSimpleName(), columnName);
        });
        bothExistColumns.forEach((columnName, column) -> {

        });

    }

    static final class ColumnMatedataEquivalence extends Equivalence<ColumnMetadata> {

        static final ColumnMatedataEquivalence INSTANCE = new ColumnMatedataEquivalence();

        @Override
        protected boolean doEquivalent(ColumnMetadata a, ColumnMetadata b) {
            return a.getColumnName().equals(b.getColumnName());
        }

        @Override
        protected int doHash(ColumnMetadata columnMetadata) {
            return columnMetadata.getColumnName().hashCode();
        }

    }
}
