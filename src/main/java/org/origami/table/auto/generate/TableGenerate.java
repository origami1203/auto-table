package org.origami.table.auto.generate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.origami.table.auto.annotations.Column;
import org.origami.table.auto.annotations.Table;
import org.origami.table.auto.config.properties.AutoTableProperties;
import org.origami.table.auto.core.ColumnMetadata;
import org.origami.table.auto.core.TableMetadata;
import org.origami.table.auto.dialect.Dialect;
import org.origami.table.auto.dialect.MySQLDialect;
import org.origami.table.auto.exception.UnsupportedDatabaseException;
import org.origami.table.auto.resolve.ColumnManager;
import org.origami.table.auto.resolve.TableManager;
import org.origami.table.auto.utils.SqlHelper;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 运行的主程序
 *
 * @author origami
 * @date 2023/8/15 23:38
 */
@Slf4j
@Component
@EnableConfigurationProperties(AutoTableProperties.class)
public class TableGenerate {
    
    @Autowired
    private AutoTableProperties properties;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
    public void generate() {
        
        String ddlAuto = properties.getDdlAuto();
        if ("none".equalsIgnoreCase(ddlAuto)) {
            return;
        }
        Dialect dialect = getDialect();
        log.info("创建表格使用[{}]方言", properties.getDatabase());
        
        // 获取候选类
        Set<Class<?>> candidateClasses = getCandidateClasses();
        
        List<TableMetadata> tables = getTables(candidateClasses, dialect);
        
        for (TableMetadata table : tables) {
            String ddlScript = SqlHelper.createTable(table);
            jdbcTemplate.execute(ddlScript);
            log.info("数据库表[{}]创建成功", table.tableName());
        }
    }
    
    /**
     * 获取候选class
     *
     * @return {@code Set<Class<?>>}
     */
    private Set<Class<?>> getCandidateClasses() {
        Reflections reflections = new Reflections(properties.getEntityPackage());
        Set<Class<?>> tableNameAnnotationClass = reflections.getTypesAnnotatedWith(TableName.class);
        Set<Class<?>> tableAnnotationClass = reflections.getTypesAnnotatedWith(Table.class);
        
        return Sets.union(tableAnnotationClass, tableNameAnnotationClass);
    }
    
    /**
     * 获取方言
     *
     * @return {@code Dialect}
     */
    private Dialect getDialect() {
        String database = properties.getDatabase();
        
        if ("mysql".equalsIgnoreCase(database)) {
            return new MySQLDialect();
        }
        
        throw new UnsupportedDatabaseException(String.format("不支持的数据库类型: %s", database));
    }
    
    private List<TableMetadata> getTables(Set<Class<?>> candidateClasses, Dialect dialect) {
        List<TableMetadata> tables = new ArrayList<>();
        
        TableManager tableManager = TableManager.getInstance();
        ColumnManager columnManager = ColumnManager.getInstance(dialect);
        for (Class<?> tableClass : candidateClasses) {
            
            TableMetadata tableMetadata = tableManager.handle(tableClass);
            Set<Field> fields = ReflectionUtils.getAllFields(tableClass, filter());
            
            for (Field field : fields) {
                ColumnMetadata columnMetadata = columnManager.handle(field);
                
                if (columnMetadata.primary()) {
                    ColumnMetadata primaryColumn = tableMetadata.primaryColumn();
                    if (primaryColumn != null) {
                        throw new RuntimeException(String.format("%s中有多于一个的主键,请检查实体类",
                                                                 tableClass.getName()));
                    }
                    tableMetadata.primaryColumn(columnMetadata);
                    continue;
                }
                tableMetadata.addColumnMetadata(columnMetadata);
            }
            tables.add(tableMetadata);
        }
        return tables;
    }
    
    private Predicate<? super Field> filter() {
        return field -> {
            boolean exists = !field.getName().equals("serialVersionUID");
            
            if (field.isAnnotationPresent(TableField.class)) {
                TableField tableField = field.getAnnotation(TableField.class);
                exists = tableField.exist() && exists;
            }
            
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                exists = !column.ignore() && exists;
            }
            return exists;
        };
    }
}
