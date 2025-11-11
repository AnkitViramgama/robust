# API Client Generation Guide

This guide explains how to generate Flutter API clients from the backend OpenAPI specification.

## Overview

The backend exposes an OpenAPI specification that can be used to auto-generate type-safe API clients for Flutter.

## Prerequisites

- Backend running (for OpenAPI spec)
- `openapi-generator-cli` installed
- OR use `openapi_generator` Flutter package

## Method 1: Using openapi-generator CLI

### 1. Install OpenAPI Generator

```bash
npm install -g @openapitools/openapi-generator-cli
```

### 2. Download OpenAPI Spec

```bash
curl http://localhost:8080/v3/api-docs > backend-api.json
```

### 3. Generate Dart Client

```bash
openapi-generator-cli generate \
  -i backend-api.json \
  -g dart-dio \
  -o frontend/lib/generated/api \
  --additional-properties=pubName=modular_api
```

### 4. Add Generated Package

Add to `pubspec.yaml`:

```yaml
dependencies:
  # ... existing dependencies ...

  # Generated API client
  dio: ^5.4.0
  json_annotation: ^4.8.1

dev_dependencies:
  # ... existing dev dependencies ...

  json_serializable: ^6.7.1
```

### 5. Use Generated Client

```dart
import 'package:modular_app/generated/api/api.dart';

final api = ModularApi(dio: dio);

// Use generated endpoints
final users = await api.getUsersApi().getAllUsers();
final roles = await api.getRolesApi().getAllRoles();
```

## Method 2: Using openapi_generator Package

### 1. Add Package

Add to `pubspec.yaml`:

```yaml
dev_dependencies:
  openapi_generator: ^4.10.0
```

### 2. Create Configuration

Create `frontend/openapi-generator-config.yaml`:

```yaml
generators:
  modular_api:
    generator: dart-dio
    output-dir: lib/generated/api
    input-spec:
      origin: URL
      url: http://localhost:8080/v3/api-docs
    additional-properties:
      pubName: modular_api
```

### 3. Generate

```bash
cd frontend
flutter pub run openapi_generator:generate -c openapi-generator-config.yaml
```

### 4. Regenerate on Changes

```bash
flutter pub run openapi_generator:generate -c openapi-generator-config.yaml --skip-validation
```

## Method 3: Manual Retrofit Setup (Recommended)

For more control, use Retrofit with manual models:

### 1. Add Dependencies

```yaml
dependencies:
  retrofit: ^4.0.3
  dio: ^5.4.0
  json_annotation: ^4.8.1

dev_dependencies:
  retrofit_generator: ^8.0.4
  build_runner: ^2.4.6
  json_serializable: ^6.7.1
```

### 2. Create API Interface

`lib/core/api/modular_api.dart`:

```dart
import 'package:dio/dio.dart';
import 'package:retrofit/retrofit.dart';
import 'package:modular_app/shared/models/user_model.dart';

part 'modular_api.g.dart';

@RestApi(baseUrl: "/api")
abstract class ModularApiClient {
  factory ModularApiClient(Dio dio, {String baseUrl}) = _ModularApiClient;

  @GET("/users/me")
  Future<ApiResponse<UserModel>> getCurrentUser();

  @GET("/roles")
  Future<ApiResponse<List<RoleModel>>> getAllRoles();

  @POST("/auth/login")
  Future<AuthResponse> login(@Body() LoginRequest request);
}
```

### 3. Generate Code

```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

### 4. Use Client

```dart
final api = ModularApiClient(dio);
final user = await api.getCurrentUser();
```

## Updating API Clients

Whenever backend APIs change:

1. **Download new OpenAPI spec**
2. **Regenerate clients**
3. **Update service implementations** if needed
4. **Test thoroughly**

## CI/CD Integration

Add to `.github/workflows/frontend-ci.yml`:

```yaml
- name: Generate API clients
  run: |
    cd frontend
    flutter pub run openapi_generator:generate -c openapi-generator-config.yaml
```

## Best Practices

1. **Version your API** to avoid breaking changes
2. **Commit generated code** to version control (or regenerate in CI)
3. **Add type safety** with proper models
4. **Handle errors** from API calls gracefully
5. **Use interceptors** for authentication
6. **Mock API clients** for testing

## Troubleshooting

### Generator fails

- Ensure backend is running
- Check OpenAPI spec is valid at `/v3/api-docs`
- Update generator version

### Type mismatches

- Regenerate with `--delete-conflicting-outputs`
- Clear build cache: `flutter clean && flutter pub get`

### Auth not working

- Check JWT interceptor is configured
- Verify token is being sent in headers
