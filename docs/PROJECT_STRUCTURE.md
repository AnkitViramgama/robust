# Project Structure

Complete folder and file structure of the modular application.

## Overview

```
robust/
├── backend/                          # Spring Boot backend
├── frontend/                         # Flutter frontend
├── docker/                           # Docker configurations
├── .github/                          # GitHub Actions workflows
├── docs/                             # Documentation
├── docker-compose.yml                # Docker Compose configuration
└── README.md                         # Main documentation
```

## Backend Structure

```
backend/
├── pom.xml                           # Parent Maven POM
├── plugins/                          # Plugin deployment directory
│   └── .gitkeep
│
├── core/                             # Core module
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/modular/core/
│       │   ├── config/               # Configuration classes
│       │   │   ├── MongoConfig.java
│       │   │   ├── OpenApiConfig.java
│       │   │   └── SecurityConfig.java
│       │   ├── controller/           # Base controller
│       │   │   └── BaseController.java
│       │   ├── dto/                  # Data Transfer Objects
│       │   │   └── ApiResponse.java
│       │   ├── entity/               # Base entity
│       │   │   └── BaseEntity.java
│       │   ├── exception/            # Exception handling
│       │   │   ├── BadRequestException.java
│       │   │   ├── ResourceNotFoundException.java
│       │   │   ├── UnauthorizedException.java
│       │   │   └── GlobalExceptionHandler.java
│       │   ├── repository/           # Base repository
│       │   │   └── BaseRepository.java
│       │   ├── security/             # Security components
│       │   │   ├── JwtUtil.java
│       │   │   └── JwtAuthenticationFilter.java
│       │   ├── service/              # Base service
│       │   │   └── BaseService.java
│       │   └── util/                 # Utilities
│       └── resources/
│           └── application.yml
│
├── user-management/                  # User management module
│   ├── pom.xml
│   └── src/main/java/com/modular/user/
│       ├── controller/
│       │   ├── AuthController.java
│       │   └── UserController.java
│       ├── dto/
│       │   ├── UserDto.java
│       │   ├── RegisterRequest.java
│       │   ├── LoginRequest.java
│       │   └── AuthResponse.java
│       ├── entity/
│       │   └── User.java
│       ├── repository/
│       │   └── UserRepository.java
│       └── service/
│           ├── UserService.java
│           └── CustomUserDetailsService.java
│
├── role-management/                  # Role management module
│   ├── pom.xml
│   └── src/main/java/com/modular/role/
│       ├── controller/
│       │   └── RoleController.java
│       ├── dto/
│       │   └── RoleDto.java
│       ├── entity/
│       │   └── Role.java
│       ├── repository/
│       │   └── RoleRepository.java
│       └── service/
│           └── RoleService.java
│
├── menu-management/                  # Menu management module
│   ├── pom.xml
│   └── src/main/java/com/modular/menu/
│       ├── controller/
│       │   └── MenuController.java
│       ├── dto/
│       │   └── MenuItemDto.java
│       ├── entity/
│       │   └── MenuItem.java
│       ├── repository/
│       │   └── MenuItemRepository.java
│       └── service/
│           └── MenuService.java
│
├── plugin-loader/                    # Plugin infrastructure
│   ├── pom.xml
│   └── src/main/java/com/modular/plugin/
│       ├── ModularPlugin.java        # Base plugin class
│       ├── PluginConfig.java         # Plugin configuration
│       ├── PluginService.java        # Plugin management
│       └── PluginController.java     # Plugin API
│
├── main-app/                         # Main application
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/modular/
│       │   └── ModularApplication.java
│       └── resources/
│           └── logback-spring.xml
│
└── example-product-plugin/           # Example plugin
    ├── pom.xml
    └── src/main/java/com/modular/product/
        ├── ProductPlugin.java
        ├── controller/
        │   └── ProductController.java
        ├── dto/
        │   └── ProductDto.java
        ├── entity/
        │   └── Product.java
        ├── repository/
        │   └── ProductRepository.java
        └── service/
            └── ProductService.java
```

## Frontend Structure

```
frontend/
├── pubspec.yaml                      # Flutter dependencies
├── analysis_options.yaml             # Linter configuration
├── .env                              # Environment variables
│
├── lib/
│   ├── main.dart                     # Application entry point
│   │
│   ├── core/                         # Core functionality
│   │   ├── app_module.dart           # Main app module
│   │   ├── config/
│   │   │   └── app_config.dart       # App configuration
│   │   ├── di/
│   │   │   └── service_locator.dart  # Dependency injection setup
│   │   └── services/
│   │       ├── api_service.dart      # API wrapper
│   │       └── storage_service.dart  # Local storage
│   │
│   ├── shared/                       # Shared components
│   │   ├── models/
│   │   │   └── user_model.dart
│   │   ├── widgets/
│   │   │   └── dynamic_menu_drawer.dart
│   │   ├── services/
│   │   └── utils/
│   │
│   └── modules/                      # Feature modules
│       │
│       ├── auth/                     # Authentication module
│       │   ├── auth_module.dart
│       │   ├── models/
│       │   │   └── auth_response.dart
│       │   ├── pages/
│       │   │   ├── login_page.dart
│       │   │   └── register_page.dart
│       │   ├── providers/
│       │   │   └── auth_provider.dart
│       │   └── services/
│       │       └── auth_service.dart
│       │
│       ├── dashboard/                # Dashboard module
│       │   ├── dashboard_module.dart
│       │   └── pages/
│       │       └── dashboard_page.dart
│       │
│       ├── menu/                     # Menu module
│       │   ├── models/
│       │   │   └── menu_item_model.dart
│       │   ├── providers/
│       │   │   └── menu_provider.dart
│       │   └── services/
│       │       └── menu_service.dart
│       │
│       └── role/                     # Role module
│           ├── models/
│           │   └── role_model.dart
│           ├── pages/
│           │   └── roles_page.dart
│           └── services/
│               └── role_service.dart
│
├── test/                             # Tests
├── assets/                           # Static assets
│   ├── images/
│   └── fonts/
└── build/                            # Build output (gitignored)
```

## Docker Structure

```
docker/
├── backend/
│   └── Dockerfile                    # Backend Dockerfile
└── frontend/
    ├── Dockerfile                    # Frontend Dockerfile
    └── nginx.conf                    # Nginx configuration
```

## Documentation

```
docs/
├── PLUGIN_GUIDE.md                   # Plugin development guide
├── FRONTEND_GUIDE.md                 # Frontend module guide
├── API_CLIENT_GENERATION.md          # API client generation
├── DEPLOYMENT.md                     # Deployment guide
└── PROJECT_STRUCTURE.md              # This file
```

## GitHub Actions

```
.github/
└── workflows/
    ├── backend-ci.yml                # Backend CI/CD
    └── frontend-ci.yml               # Frontend CI/CD
```

## Key Files

| File | Purpose |
|------|---------|
| `backend/pom.xml` | Parent Maven configuration |
| `backend/core/src/main/resources/application.yml` | Application configuration |
| `frontend/pubspec.yaml` | Flutter dependencies |
| `frontend/.env` | Environment variables |
| `docker-compose.yml` | Multi-container orchestration |
| `.gitignore` | Git ignore rules |
| `README.md` | Main documentation |

## Module Dependencies

```
main-app
├── core
├── user-management
│   └── core
├── role-management
│   └── core
├── menu-management
│   └── core
└── plugin-loader
    ├── core
    └── menu-management

example-product-plugin
├── core (provided)
├── menu-management (provided)
└── plugin-loader (provided)
```

## File Counts

- **Backend Java files**: ~50
- **Frontend Dart files**: ~30
- **Configuration files**: ~15
- **Documentation files**: ~5
- **Total lines of code**: ~8,000+
