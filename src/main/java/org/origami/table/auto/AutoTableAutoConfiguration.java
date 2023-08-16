package org.origami.table.auto;

import org.origami.table.auto.generate.TableGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 *
 * @author origami
 * @date 2023/8/16 8:06
 */
@Import(TableGenerate.class)
@Configuration(proxyBeanMethods = false)
public class AutoTableAutoConfiguration {
    
    @Autowired
    TableGenerate tableGenerate;
    
    @Bean
    ApplicationRunner runner() {
        return args -> {
            tableGenerate.generate();
        };
    }
    
    
}
