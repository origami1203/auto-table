package org.origami.table.auto.core;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author origami
 * @date 2023/8/17 19:25
 */
@RequiredArgsConstructor
public class DatabaseMetadata {

    private final Map<String, TableMetadata> tables;

    private final String schema;

    private final String catalog;

    private DatabaseMetadata(String catalog, String schema, List<TableMetadata> tables) {
        this.catalog = catalog;
        this.schema = schema;
        this.tables = tables.stream().collect(Collectors.toMap(TableMetadata::getTableName, Function.identity()));
    }

    /**
     * 表是否存在
     *
     * @param tableName 表名
     * @return boolean
     */
    public boolean tableExists(String tableName) {
        return tables.containsKey(tableName);
    }

    public TableMetadata getByTableName(String tableName) {
        return tables.get(tableName);
    }

    public static DatabaseMetadata getDatabaseMetadata(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {

            List<TableMetadata> existsTables = new ArrayList<>();

            String catalog = conn.getCatalog();
            String schema = conn.getSchema();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});
            int i = 0;
            while (tables.next()) {
                // 相当于database name
                String tableCat = tables.getString("TABLE_CAT");
                // 表名
                String tableName = tables.getString("TABLE_NAME");
                String comment = tables.getString("REMARKS");

                TableMetadata tableMetadata = new TableMetadata();
                tableMetadata.setTableName(tableName).setComment(comment).setCatalog(tableCat);

                ResultSet columns = metaData.getColumns(catalog, schema, tableName, "%");

                while (columns.next()) {

                    String columnName = columns.getString("COLUMN_NAME");
                    // java.sql.Types的对应类型
                    int dataType = columns.getInt("DATA_TYPE");
                    // varchar bigint
                    String typeName = columns.getString("TYPE_NAME");
                    // COLUMN_SIZE列指定给定列的列大小
                    // 对于数值数据，这是最大精度。对于字符数据，这是以字符为单位的长度。varchar(255) 对应 255
                    // 对于日期时间数据类型，这是 String表示形式的长度（以字符为单位）（假设秒分数部分允许的最大精度）。
                    // 对于二进制数据，这是以字节为单位的长度。
                    // 对于 ROWID数据类型，这是以字节为单位的长度。对于列大小不适用的数据类型，返回Null
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    int decimalDigits = columns.getInt("DECIMAL_DIGITS");
                    int nullable = columns.getInt("NULLABLE");
                    String remarks = columns.getString("REMARKS");
                    String defaultValue = columns.getString("COLUMN_DEF");
                    // YES NO ""
                    String isAutoincrement = columns.getString("IS_AUTOINCREMENT");

                    ColumnMetadata columnMetadata = new ColumnMetadata();
                    columnMetadata.setColumnName(columnName)
                                  .setJdbcType(dataType)
                                  .setActualTypeName(typeName)
                                  .setLength(columnSize)
                                  .setNullable(DatabaseMetaData.columnNullable == nullable)
                                  .setScale(decimalDigits)
                                  .setComment(remarks)
                                  .setDefaultValue(defaultValue)
                                  .setIsAutoIncrement("YES".equals(isAutoincrement));

                    columnMetadata.setTable(tableMetadata);
                    tableMetadata.addColumnMetadata(columnMetadata);
                }
                closeResultSet(columns);
                setPrimaryKeyColumn(metaData, catalog, schema, tableName, tableMetadata);
                setUnique(metaData, catalog, schema, tableName, tableMetadata);

                existsTables.add(tableMetadata);
            }
            closeResultSet(tables);
            return new DatabaseMetadata(catalog, schema, existsTables);
        }

    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
                // ignore exception
            }
        }
    }

    /**
     * 查找主键
     *
     * @param metaData      元数据
     * @param catalog       catalog
     * @param schema        schema
     * @param tableName     表名
     * @param tableMetadata table元数据
     * @throws SQLException sqlexception异常
     */
    private static void setPrimaryKeyColumn(DatabaseMetaData metaData,
                                            String catalog,
                                            String schema,
                                            String tableName,
                                            TableMetadata tableMetadata) throws SQLException {
        ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);

        while (primaryKeys.next()) {
            String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");

            Iterator<Map.Entry<String, ColumnMetadata>> iterator = tableMetadata.getColumns().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, ColumnMetadata> entry = iterator.next();
                if (primaryKeyColumnName.equals(entry.getKey())) {
                    ColumnMetadata primaryColumn = entry.getValue();
                    primaryColumn.setPrimary(true).setUnique(true);
                    tableMetadata.setPrimaryColumn(primaryColumn);
                    iterator.remove();
                }
            }

        }
        closeResultSet(primaryKeys);
    }

    /**
     * 是否是唯一索引
     *
     * @param metaData      元数据
     * @param catalog       目录
     * @param schema        模式
     * @param tableName     表名
     * @param tableMetadata tableMetadata
     * @throws SQLException sqlexception异常
     */
    private static void setUnique(DatabaseMetaData metaData,
                                  String catalog,
                                  String schema,
                                  String tableName,
                                  TableMetadata tableMetadata) throws SQLException {
        ResultSet uniqueKeys = metaData.getIndexInfo(catalog, schema, tableName, true, false);

        while (uniqueKeys.next()) {
            String uniqueColumnName = uniqueKeys.getString("COLUMN_NAME");

            for (ColumnMetadata column : tableMetadata.getColumns().values()) {
                if (uniqueColumnName.equals(column.getColumnName())) {
                    tableMetadata.setPrimaryColumn(column.setUnique(true));
                    break;
                }
            }
        }
        closeResultSet(uniqueKeys);
    }
}
