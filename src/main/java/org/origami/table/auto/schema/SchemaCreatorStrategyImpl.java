package org.origami.table.auto.schema;

import com.google.common.collect.ImmutableList;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.utils.SqlHelper;

import java.util.List;

/**
 * @author origami
 * @date 2023/8/16 19:26
 */
public class SchemaCreatorStrategyImpl implements SchemaStrategy {
    
    @Override
    public List<String> getSQL(TableMetadata tableMetadata) {
        return ImmutableList.of(SqlHelper.createTable(tableMetadata));
    }
}
