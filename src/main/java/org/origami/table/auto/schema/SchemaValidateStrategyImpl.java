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
import org.springframework.util.CollectionUtils;

import java.sql.JDBCType;
import java.util.Map;
import java.util.Objects;

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
                       Dialect dialect,
                       DatabaseMetadata databaseMetadata,
                       TableMetadata tableMetadata) {

        String tableName = tableMetadata.getTableName();

        if (!databaseMetadata.tableExists(tableName)) {
            log.error("AutoTable: 校验失败,存在新增的实体类:[{}]", tableMetadata.getEntityClass().getName());
            throw new ValidationException(
                String.format("AutoTable: 校验失败,新增实体类[%s]", tableMetadata.getEntityClass().getName()));
        }

        Map<String, ColumnMetadata> alreadyExistColumns = databaseMetadata.getByTableName(tableName).getColumns();
        Map<String, ColumnMetadata> entityClassColumns = tableMetadata.getColumns();

        MapDifference<String, ColumnMetadata> difference =
            Maps.difference(alreadyExistColumns, entityClassColumns, ColumnMatedataEquivalence.INSTANCE);

        Map<String, ColumnMetadata> deletedColumns = difference.entriesOnlyOnLeft();
        Map<String, ColumnMetadata> addColumns = difference.entriesOnlyOnRight();
        Map<String, MapDifference.ValueDifference<ColumnMetadata>> ValueDifferenceMap = difference.entriesDiffering();

        if (!CollectionUtils.isEmpty(addColumns) || !CollectionUtils.isEmpty(deletedColumns) ||
            !CollectionUtils.isEmpty(ValueDifferenceMap)) {
            log.error("AutoTable: 实体类[{}]存在变化", tableMetadata.getEntityClass().getName());
            throw new ValidationException(
                String.format("AutoTable: 实体类[%s]存在变化", tableMetadata.getEntityClass().getName()));
        }

        for (ColumnMetadata column : deletedColumns.values()) {
            log.error("AutoTable: [{}]数据表中存在名为[{}]的列,实体类中已删除", column.getTable().getTableName(),
                column.getColumnName());
        }
        for (ColumnMetadata column : addColumns.values()) {
            log.error("AutoTable: [{}]实体类中新增列名为[{}]的column", column.getTable().getEntityClass().getName(),
                column.getColumnName());
        }

        for (MapDifference.ValueDifference<ColumnMetadata> valueDifference : ValueDifferenceMap.values()) {
            ColumnMetadata tableColumn = valueDifference.leftValue();
            ColumnMetadata entityColumn = valueDifference.rightValue();

            log.error("AutoTable: [{}]表的[{}]字段数据类型发生变化,数据库类型:[{}],实体类类型:[{}]",
                tableMetadata.getTableName(), tableColumn.getColumnName(),
                JDBCType.valueOf(tableColumn.getJdbcType()).getName(),
                JDBCType.valueOf(entityColumn.getJdbcType()).getName());
        }
    }

    static final class ColumnMatedataEquivalence extends Equivalence<ColumnMetadata> {

        static final ColumnMatedataEquivalence INSTANCE = new ColumnMatedataEquivalence();

        @Override
        protected boolean doEquivalent(ColumnMetadata a, ColumnMetadata b) {
            return Objects.equals(a.getColumnName(), b.getColumnName()) &&
                   Objects.equals(a.getJdbcType(), b.getJdbcType());
        }

        @Override
        protected int doHash(ColumnMetadata columnMetadata) {
            return Objects.hash(columnMetadata.getColumnName(), columnMetadata.getJdbcType());
        }

    }
}
