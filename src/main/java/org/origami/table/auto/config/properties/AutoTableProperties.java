package org.origami.table.auto.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author origami
 * @date 2023/7/19 19:36
 */
@Data
@ConfigurationProperties(prefix = "table.auto")
public class AutoTableProperties {

    // none：不做任何操作
    // create：如果已存在，不做任何改变，如果不存在则创建
    // update：根据实体类的定义自动更新数据库表结构
    // create-drop：程序启动时创建，关闭应用时删除表
    // validate：验证实体类的定义与数据库表结构是否一致，但不做任何更改
    private String ddlAuto = "none";

    private String entityPackage;

    /**
     * 暂时只支持mysql
     */
    private String database = "mysql";


    // TODO
    /**
     * 统一前缀
     */
    private String prefix;

    // TODO
    /**
     * 统一后缀
     */
    private String suffix;

    // TODO
    /**
     * 字段是否默认为null
     */
    private Boolean defaultNull = true;

    private Map<String, Map<String, String>> custom;
}
