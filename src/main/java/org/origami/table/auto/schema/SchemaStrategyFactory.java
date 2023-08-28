package org.origami.table.auto.schema;

/**
 * SchemaStrategy的简单工厂类
 *
 * @author origami
 * @date 2023/8/28 18:06
 */
public final class SchemaStrategyFactory {

    private SchemaStrategyFactory() {}

    public static SchemaStrategy getSchemaStrategy(String ddlAuto) {
        SchemaStrategy strategy = null;
        switch (ddlAuto) {
            case "none":
                break;
            case "create":
                strategy = new SchemaCreatorStrategyImpl();
                break;
            case "update":
                strategy = new SchemaUpdateStrategyImpl();
                break;
            case "validate":
                strategy = new SchemaValidateStrategyImpl();
                break;
            default:
                throw new IllegalArgumentException(String.format("不支持的ddl-auto方式[%s]", ddlAuto));
        }

        return strategy;
    }
}
