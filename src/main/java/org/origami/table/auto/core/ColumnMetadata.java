package org.origami.table.auto.core;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.origami.table.auto.constant.Constants;

/**
 * 数据列元数据
 *
 * @author origami
 * @date 2023/8/9 22:50
 */
@Data
@ToString(exclude = "table")
@Accessors(chain = true)
public class ColumnMetadata {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列定义
     */
    private String columnDefinition;

    /**
     * jdbc类型
     */
    private Integer jdbcType;

    /**
     * 是否可为null
     */
    private Boolean nullable = Constants.DEFAULT_NULLABLE;

    /**
     * 是否是主键
     */
    private Boolean primary = false;

    /**
     * 备注
     */
    private String comment;

    /**
     * 是否唯一
     */
    private Boolean unique;

    /**
     * 不同数据库对于同一种类型可能有不同的叫法
     */
    private String actualTypeName;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 自增
     */
    private Boolean isAutoIncrement;

    /**
     * 长度,用于varchar，char，text等
     */
    private Integer length;

    /**
     * 小数点位数
     */
    private Integer scale;

    /**
     * 精度
     */
    private Integer precision;

    /**
     * 所属表
     */
    private TableMetadata table;
}
