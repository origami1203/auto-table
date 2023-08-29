package org.origami.table.auto.dialect;

import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.exception.UnmappedTypeException;
import org.origami.table.auto.schema.internel.AlertColumnExporter;
import org.origami.table.auto.schema.internel.StandardTableExporter;
import org.origami.table.auto.schema.spi.Exporter;

/**
 * 方言,仿照hibernate
 *
 * @author origami
 * @date 2023/8/15 8:11
 */
public abstract class Dialect {

    private final TypeNames typeNames = new TypeNames();

    public String getActualTypeName(Integer code, Integer length, Integer precision, Integer scale) {
        final String result = typeNames.getByTypeCode(code, length, precision, scale);
        if (result == null) {
            throw new UnmappedTypeException(String.format("未映射的jdbc类型: %d", code));
        }
        return result;
    }

    /**
     * 获取alter table语句
     *
     * @param tableName 表名
     * @return {@code String}
     */
    public String getAlterTableString(String tableName) {
        final StringBuilder sb = new StringBuilder("alter table ");
        sb.append(tableName);
        return sb.toString();
    }

    protected void registerColumnType(int code, DateType dateType) {
        typeNames.put(code, dateType);
    }

    protected void registerClassType(Class<?> clazz, DateType dateType) {
        typeNames.put(clazz, dateType);
    }

    /**
     * 用于建表的字符串
     *
     * @return {@code String}
     */
    public String getCreateTableString() {
        return "create table ";
    }

    public String getTableComment(String comment) {
        return "comment '" + comment + "'";
    }

    public String getColumnComment(String comment) {
        return "comment '" + comment + "'";
    }

    public String getNullColumnString() {
        return "";
    }

    public String getAlterTableString() {
        return "alter table";
    }

    public String getAddColumnString() {
        return "add";
    }

    public String getDefaultString() {
        return "default";
    }

    /**
     * 数据库自定义配置，如mysql配置engine
     */
    public String getTableTypeString() {
        return "";
    }

    private StandardTableExporter tableExporter = new StandardTableExporter(this);
    private AlertColumnExporter alertColumnExporter = new AlertColumnExporter(this);

    public Exporter<TableMetadata> getTableExporter() {
        return tableExporter;
    }

    public Exporter<ColumnMetadata> getAlertColumnExporter() {
        return alertColumnExporter;
    }

    public String getActualTypeName(Class<?> clazz, Integer length, Integer precision, Integer scale) {

        final String result = typeNames.getByClassType(clazz, length, precision, scale);
        if (result == null) {
            throw new UnmappedTypeException(String.format("未映射的java类型: %s", clazz.getName()));
        }
        return result;
    }
}
