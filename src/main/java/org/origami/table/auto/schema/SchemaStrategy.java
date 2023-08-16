package org.origami.table.auto.schema;

import org.origami.table.auto.core.TableMetadata;

import java.util.List;

/**
 * schema策略接口
 *
 * @author origami
 * @date 2023/8/16 19:16
 */
public interface SchemaStrategy {
    
    /**
     * 根据table元数据获取sql
     *
     * @param tableMetadataList 表元数据列表
     * @return {@code List<String>}
     */
    List<String> getSQL(TableMetadata tableMetadata);
}
