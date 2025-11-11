package com.modular.plugin;

import org.pf4j.PluginManager;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Plugin configuration
 */
@Configuration
public class PluginConfig {

    @Value("${plugin.path:./plugins}")
    private String pluginPath;

    @Bean
    public PluginManager pluginManager() {
        Path pluginsRoot = Paths.get(pluginPath);
        PluginManager pluginManager = new SpringPluginManager(pluginsRoot);
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        return pluginManager;
    }
}
