# Modular Full-Stack Application

A production-ready, modular, cross-platform application built with **Flutter** (frontend) and **Spring Boot** (backend) with **MongoDB**, supporting **plugin-based feature addition** using PF4J.

## 🏗️ Architecture

This is a monorepo containing:
- **Backend**: Spring Boot 3.x + MongoDB + PF4J plugin system
- **Frontend**: Flutter (Web, Android, iOS, Windows, macOS, Linux)

### Key Features

✅ **Plugin Architecture** - Add/remove features dynamically using PF4J
✅ **Dynamic Menu System** - Backend-driven UI menus with role-based access
✅ **JWT Authentication** - Secure token-based authentication
✅ **Role & Permission Management** - Fine-grained access control
✅ **Cross-Platform Frontend** - Single codebase for all platforms
✅ **Modular Design** - Clean separation of concerns
✅ **Docker Support** - Full containerization with Docker Compose
✅ **CI/CD Ready** - GitHub Actions workflows included

---

## 📁 Project Structure

```
robust/
├── backend/
│   ├── core/                      # Base classes, security, config
│   ├── user-management/           # User authentication & management
│   ├── role-management/           # Role & permissions
│   ├── menu-management/           # Dynamic menu system
│   ├── plugin-loader/             # PF4J plugin infrastructure
│   ├── main-app/                  # Main Spring Boot application
│   └── example-product-plugin/    # Example plugin module
├── frontend/
│   ├── lib/
│   │   ├── core/                  # App config, DI, services
│   │   ├── modules/
│   │   │   ├── auth/              # Login, register
│   │   │   ├── dashboard/         # Main dashboard
│   │   │   ├── menu/              # Dynamic menu rendering
│   │   │   └── role/              # Role management UI
│   │   └── shared/                # Models, widgets, utilities
│   └── pubspec.yaml
├── docker/
│   ├── backend/
│   │   └── Dockerfile
│   └── frontend/
│       ├── Dockerfile
│       └── nginx.conf
├── .github/workflows/             # CI/CD pipelines
├── docker-compose.yml
└── README.md
```

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- Flutter 3.16+
- MongoDB 7.0+
- Docker & Docker Compose (optional)

### Option 1: Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd robust

# Start all services
docker-compose up -d

# Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Option 2: Manual Setup

#### Backend

```bash
cd backend

# Build all modules
mvn clean install

# Run the application
cd main-app
mvn spring-boot:run

# Or run the JAR
java -jar target/main-app-1.0.0.jar
```

**Configure MongoDB** in `backend/main-app/src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: modular_app
```

#### Frontend

```bash
cd frontend

# Install dependencies
flutter pub get

# Run on web
flutter run -d chrome

# Build for production
flutter build web
flutter build apk  # Android
flutter build ios  # iOS
```

**Configure API endpoint** in `frontend/.env`:

```env
API_BASE_URL=http://localhost:8080/api
```

---

## 📚 API Documentation

Once the backend is running, visit:

**Swagger UI**: http://localhost:8080/swagger-ui.html
**OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Key Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/register` | POST | Register new user |
| `/api/auth/login` | POST | Login and get JWT token |
| `/api/users/me` | GET | Get current user profile |
| `/api/roles` | GET | List all roles |
| `/api/menus/hierarchy` | GET | Get menu hierarchy for user |
| `/api/plugins` | GET | List loaded plugins |

---

## 🔌 Adding a New Plugin

See [docs/PLUGIN_GUIDE.md](docs/PLUGIN_GUIDE.md) for detailed instructions.

### Quick Example

1. **Create a new Maven module** in `backend/`:

```xml
<!-- backend/my-plugin/pom.xml -->
<parent>
    <groupId>com.modular</groupId>
    <artifactId>modular-backend-parent</artifactId>
    <version>1.0.0</version>
</parent>
<artifactId>my-plugin</artifactId>
```

2. **Create plugin class**:

```java
public class MyPlugin extends ModularPlugin {
    public MyPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public List<MenuItemDto> getMenuItems() {
        MenuItemDto menu = new MenuItemDto();
        menu.setItemId("my-feature");
        menu.setLabel("My Feature");
        menu.setPath("/my-feature");
        menu.setIcon("star");
        return List.of(menu);
    }

    @Override
    public String getModuleName() {
        return "my-plugin";
    }
}
```

3. **Add REST controllers** with `@Extension` annotation

4. **Build and deploy**:

```bash
mvn clean package
cp target/my-plugin-jar-with-dependencies.jar /path/to/plugins/
```

---

## 🎨 Adding a Frontend Module

See [docs/FRONTEND_GUIDE.md](docs/FRONTEND_GUIDE.md) for detailed instructions.

### Quick Example

1. **Create module directory**: `frontend/lib/modules/my_module/`

2. **Create module file**:

```dart
class MyModule extends Module {
  @override
  void routes(r) {
    r.child('/', child: (context) => const MyPage());
  }
}
```

3. **Register in app module**:

```dart
r.module('/my-module', module: MyModule());
```

---

## 🧪 Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
flutter test
```

---

## 🐳 Docker Deployment

### Build Images

```bash
# Backend
docker build -f docker/backend/Dockerfile -t modular-backend .

# Frontend
docker build -f docker/frontend/Dockerfile -t modular-frontend .
```

### Environment Variables

**Backend** (`docker-compose.yml`):
- `MONGO_HOST`, `MONGO_PORT`, `MONGO_DATABASE`
- `JWT_SECRET`
- `PLUGIN_PATH`

**Frontend** (`.env`):
- `API_BASE_URL`

---

## 📖 Documentation

- [Plugin Development Guide](docs/PLUGIN_GUIDE.md)
- [Frontend Module Guide](docs/FRONTEND_GUIDE.md)
- [API Client Generation](docs/API_CLIENT_GENERATION.md)
- [Deployment Guide](docs/DEPLOYMENT.md)

---

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- MongoDB
- PF4J (Plugin Framework)
- SpringDoc OpenAPI
- Logback

### Frontend
- Flutter 3.16+
- Riverpod (State Management)
- GetIt (Dependency Injection)
- Flutter Modular (Routing)
- Dio (HTTP Client)

### DevOps
- Docker & Docker Compose
- GitHub Actions
- Nginx

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

## 📄 License

This project is licensed under the Apache License 2.0.

---

## 👥 Authors

Generated by Claude Code - Modular Application Generator

---

## 🆘 Support

For issues and questions, please open a GitHub issue.
