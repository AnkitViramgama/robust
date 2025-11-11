# Frontend Module Development Guide

This guide explains how to add new modules to the Flutter frontend.

## Architecture

The frontend uses:
- **Flutter Modular** for routing
- **Riverpod** for state management
- **GetIt** for dependency injection
- **Dio** for HTTP requests

## Adding a New Module

### 1. Create Module Directory

```bash
cd frontend/lib/modules
mkdir my_module
cd my_module
```

Create this structure:

```
my_module/
├── models/
│   └── my_model.dart
├── services/
│   └── my_service.dart
├── providers/
│   └── my_provider.dart
├── pages/
│   └── my_page.dart
└── my_module.dart
```

### 2. Create Model

`models/my_model.dart`:

```dart
class MyModel {
  final String id;
  final String name;
  final String description;

  MyModel({
    required this.id,
    required this.name,
    required this.description,
  });

  factory MyModel.fromJson(Map<String, dynamic> json) {
    return MyModel(
      id: json['id'] ?? '',
      name: json['name'] ?? '',
      description: json['description'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'description': description,
    };
  }
}
```

### 3. Create Service

`services/my_service.dart`:

```dart
import 'package:modular_app/core/services/api_service.dart';
import 'package:modular_app/modules/my_module/models/my_model.dart';

class MyService {
  final ApiService _apiService;

  MyService(this._apiService);

  Future<List<MyModel>> getAll() async {
    final response = await _apiService.get('/my-feature');
    final List<dynamic> data = response.data['data'] ?? [];
    return data.map((item) => MyModel.fromJson(item)).toList();
  }

  Future<MyModel> getById(String id) async {
    final response = await _apiService.get('/my-feature/$id');
    return MyModel.fromJson(response.data['data']);
  }

  Future<MyModel> create(MyModel model) async {
    final response = await _apiService.post('/my-feature', data: model.toJson());
    return MyModel.fromJson(response.data['data']);
  }

  Future<MyModel> update(String id, MyModel model) async {
    final response = await _apiService.put('/my-feature/$id', data: model.toJson());
    return MyModel.fromJson(response.data['data']);
  }

  Future<void> delete(String id) async {
    await _apiService.delete('/my-feature/$id');
  }
}
```

### 4. Register Service in DI

Add to `lib/core/di/service_locator.dart`:

```dart
import 'package:modular_app/modules/my_module/services/my_service.dart';

Future<void> setupServiceLocator() async {
  // ... existing code ...

  // My Module Service
  getIt.registerSingleton<MyService>(MyService(getIt<ApiService>()));
}
```

### 5. Create Provider

`providers/my_provider.dart`:

```dart
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:modular_app/core/di/service_locator.dart';
import 'package:modular_app/modules/my_module/models/my_model.dart';
import 'package:modular_app/modules/my_module/services/my_service.dart';

final myServiceProvider = Provider<MyService>((ref) => getIt<MyService>());

final myItemsProvider = FutureProvider<List<MyModel>>((ref) async {
  final service = ref.watch(myServiceProvider);
  return await service.getAll();
});
```

### 6. Create Page

`pages/my_page.dart`:

```dart
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:modular_app/modules/my_module/providers/my_provider.dart';

class MyPage extends ConsumerWidget {
  const MyPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final itemsAsync = ref.watch(myItemsProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('My Feature'),
      ),
      body: itemsAsync.when(
        data: (items) => items.isEmpty
            ? const Center(child: Text('No items found'))
            : ListView.builder(
                itemCount: items.length,
                itemBuilder: (context, index) {
                  final item = items[index];
                  return ListTile(
                    title: Text(item.name),
                    subtitle: Text(item.description),
                  );
                },
              ),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(child: Text('Error: $error')),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Navigate to add/edit page
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
```

### 7. Create Module File

`my_module.dart`:

```dart
import 'package:flutter_modular/flutter_modular.dart';
import 'package:modular_app/modules/my_module/pages/my_page.dart';

class MyModule extends Module {
  @override
  void routes(r) {
    r.child('/', child: (context) => const MyPage());
  }
}
```

### 8. Register in Dashboard Module

Update `lib/modules/dashboard/dashboard_module.dart`:

```dart
import 'package:modular_app/modules/my_module/my_module.dart';

class DashboardModule extends Module {
  @override
  void routes(r) {
    r.child('/', child: (context) => const DashboardPage());
    r.child('/roles', child: (context) => const RolesPage());
    r.module('/my-feature', module: MyModule());  // Add this line
  }
}
```

### 9. Backend Menu Registration

The menu item will be automatically created by your backend plugin. The frontend will fetch it dynamically.

### 10. Test Your Module

```bash
flutter run -d chrome
```

Navigate to the menu item to see your new module in action.

## State Management Patterns

### Using FutureProvider for Data Fetching

```dart
final dataProvider = FutureProvider<MyData>((ref) async {
  final service = ref.watch(myServiceProvider);
  return await service.fetchData();
});
```

### Using StateNotifier for Complex State

```dart
class MyStateNotifier extends StateNotifier<MyState> {
  final MyService _service;

  MyStateNotifier(this._service) : super(MyState.initial());

  Future<void> loadData() async {
    state = state.copyWith(isLoading: true);
    try {
      final data = await _service.getAll();
      state = state.copyWith(data: data, isLoading: false);
    } catch (e) {
      state = state.copyWith(error: e.toString(), isLoading: false);
    }
  }
}

final myStateProvider = StateNotifierProvider<MyStateNotifier, MyState>((ref) {
  return MyStateNotifier(ref.watch(myServiceProvider));
});
```

## Routing

### Navigate to a route

```dart
Modular.to.navigate('/dashboard/my-feature');
```

### Navigate with parameters

```dart
Modular.to.navigate('/dashboard/my-feature/details', arguments: {'id': '123'});
```

### Get route arguments

```dart
final args = Modular.args.data as Map<String, dynamic>;
final id = args['id'];
```

## Best Practices

1. **Follow the module structure** consistently
2. **Use Riverpod providers** for state management
3. **Keep UI and logic separate** - use services for business logic
4. **Handle loading and error states** in all async widgets
5. **Use const constructors** where possible for performance
6. **Add proper navigation** using Modular
7. **Test your widgets** with `flutter test`

## Example Modules

See existing modules:
- `lib/modules/auth` - Authentication
- `lib/modules/role` - Role management
- `lib/modules/menu` - Dynamic menus
