import 'package:dio/dio.dart';
import 'package:get_it/get_it.dart';
import 'package:modular_app/core/config/app_config.dart';
import 'package:modular_app/core/services/api_service.dart';
import 'package:modular_app/core/services/storage_service.dart';
import 'package:modular_app/modules/auth/services/auth_service.dart';
import 'package:modular_app/modules/menu/services/menu_service.dart';
import 'package:modular_app/modules/role/services/role_service.dart';

final getIt = GetIt.instance;

Future<void> setupServiceLocator() async {
  // Storage Service
  final storageService = StorageService();
  await storageService.init();
  getIt.registerSingleton<StorageService>(storageService);

  // Dio
  final dio = Dio(BaseOptions(
    baseUrl: AppConfig.apiBaseUrl,
    connectTimeout: const Duration(seconds: 30),
    receiveTimeout: const Duration(seconds: 30),
  ));

  // Add interceptor for authentication
  dio.interceptors.add(InterceptorsWrapper(
    onRequest: (options, handler) async {
      final token = await storageService.getToken();
      if (token != null) {
        options.headers['Authorization'] = 'Bearer $token';
      }
      return handler.next(options);
    },
  ));

  getIt.registerSingleton<Dio>(dio);

  // API Service
  getIt.registerSingleton<ApiService>(ApiService(dio));

  // Auth Service
  getIt.registerSingleton<AuthService>(AuthService(
    getIt<ApiService>(),
    getIt<StorageService>(),
  ));

  // Menu Service
  getIt.registerSingleton<MenuService>(MenuService(getIt<ApiService>()));

  // Role Service
  getIt.registerSingleton<RoleService>(RoleService(getIt<ApiService>()));
}
