# Plugin Development Guide

This guide explains how to create and integrate new backend plugins into the modular application.

## Overview

The plugin system is built on **PF4J** (Plugin Framework for Java), allowing you to add new features without modifying the core application.

## Plugin Structure

Each plugin is a Maven module that:
- Extends `ModularPlugin`
- Registers menu items
- Provides REST controllers
- Can define entities and repositories

## Step-by-Step: Creating a Plugin

### 1. Create Maven Module

```bash
cd backend
mkdir my-feature-plugin
cd my-feature-plugin
```

Create `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.modular</groupId>
        <artifactId>modular-backend-parent</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>my-feature-plugin</artifactId>
    <name>My Feature Plugin</name>

    <dependencies>
        <dependency>
            <groupId>com.modular</groupId>
            <artifactId>core</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.modular</groupId>
            <artifactId>plugin-loader</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.pf4j</groupId>
            <artifactId>pf4j</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.6.0</version>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifestEntries>
                            <Plugin-Id>my-feature-plugin</Plugin-Id>
                            <Plugin-Version>1.0.0</Plugin-Version>
                            <Plugin-Provider>My Company</Plugin-Provider>
                            <Plugin-Class>com.modular.myfeature.MyFeaturePlugin</Plugin-Class>
                        </manifestEntries>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. Create Plugin Class

```java
package com.modular.myfeature;

import com.modular.menu.dto.MenuItemDto;
import com.modular.plugin.ModularPlugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyFeaturePlugin extends ModularPlugin {

    public MyFeaturePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("My Feature Plugin started");
        // Initialize plugin resources
    }

    @Override
    public void stop() {
        System.out.println("My Feature Plugin stopped");
        // Cleanup resources
    }

    @Override
    public List<MenuItemDto> getMenuItems() {
        List<MenuItemDto> menuItems = new ArrayList<>();

        MenuItemDto menu = new MenuItemDto();
        menu.setItemId("my-feature");
        menu.setLabel("My Feature");
        menu.setPath("/my-feature");
        menu.setIcon("extension");
        menu.setModule("my-feature");
        menu.setOrder(20);
        menu.setActive(true);
        menu.setVisible(true);

        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");
        menu.setRolesAllowed(roles);

        menuItems.add(menu);
        return menuItems;
    }

    @Override
    public String getModuleName() {
        return "my-feature";
    }
}
```

### 3. Create Entity

```java
package com.modular.myfeature.entity;

import com.modular.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "my_feature_items")
public class MyFeatureItem extends BaseEntity {
    private String name;
    private String description;
    private Boolean active = true;
}
```

### 4. Create Repository

```java
package com.modular.myfeature.repository;

import com.modular.core.repository.BaseRepository;
import com.modular.myfeature.entity.MyFeatureItem;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFeatureRepository extends BaseRepository<MyFeatureItem> {
}
```

### 5. Create Service

```java
package com.modular.myfeature.service;

import com.modular.core.repository.BaseRepository;
import com.modular.core.service.BaseService;
import com.modular.myfeature.entity.MyFeatureItem;
import com.modular.myfeature.repository.MyFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class MyFeatureService extends BaseService<MyFeatureItem> {

    @Autowired
    private MyFeatureRepository repository;

    @Override
    protected BaseRepository<MyFeatureItem> getRepository() {
        return repository;
    }

    // Add custom business logic here
}
```

### 6. Create REST Controller

```java
package com.modular.myfeature.controller;

import com.modular.core.controller.BaseController;
import com.modular.core.service.BaseService;
import com.modular.myfeature.entity.MyFeatureItem;
import com.modular.myfeature.service.MyFeatureService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my-feature")
@Tag(name = "My Feature", description = "My Feature endpoints")

public class MyFeatureController extends BaseController<MyFeatureItem> {

    @Autowired
    private MyFeatureService service;

    @Override
    protected BaseService<MyFeatureItem> getService() {
        return service;
    }

    // Add custom endpoints here
}
```

### 7. Build the Plugin

```bash
mvn clean package
```

This creates `target/my-feature-plugin-1.0.0-jar-with-dependencies.jar`

### 8. Deploy the Plugin

Copy the JAR to the plugins directory:

```bash
cp target/my-feature-plugin-1.0.0-jar-with-dependencies.jar /path/to/plugins/
```

Or with Docker:

```bash
docker cp my-feature-plugin-1.0.0-jar-with-dependencies.jar modular-backend:/app/plugins/
docker restart modular-backend
```

### 9. Verify Plugin is Loaded

```bash
curl http://localhost:8080/api/plugins
```

The plugin should appear in the list. Menu items are automatically registered.

## Plugin Lifecycle

1. **Load**: PF4J scans the plugins directory
2. **Start**: Plugin's `start()` method is called
3. **Register**: Menu items are registered automatically
4. **Run**: Controllers and services are available
5. **Stop**: Plugin's `stop()` method is called on shutdown

## Best Practices

1. **Use `` annotation** on all Spring components (Controllers, Services)
2. **Keep dependencies `provided`** to avoid conflicts with the main app
3. **Follow naming conventions**: `{Feature}Plugin`, `{Feature}Controller`, etc.
4. **Add proper logging** in start/stop methods
5. **Handle errors gracefully** to prevent affecting other plugins

## Troubleshooting

### Plugin not loading

- Check plugin JAR is in the correct directory
- Verify `Plugin-Class` in MANIFEST.MF matches your class
- Check application logs for errors

### Controllers not registered

- Ensure `` annotation is present
- Verify `@RestController` and `@RequestMapping` are correct
- Check component scanning includes your package

### Menu items not appearing

- Verify `getMenuItems()` returns non-empty list
- Check role permissions match current user
- Ensure menu items are marked as `active` and `visible`

## Example Plugins

See `backend/example-product-plugin` for a complete working example.
