import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:modular_app/core/di/service_locator.dart';
import 'package:modular_app/modules/auth/services/auth_service.dart';
import 'package:modular_app/shared/models/user_model.dart';

final authServiceProvider = Provider<AuthService>((ref) => getIt<AuthService>());

final currentUserProvider = FutureProvider<UserModel?>((ref) async {
  final authService = ref.watch(authServiceProvider);
  return await authService.getCurrentUser();
});

final isAuthenticatedProvider = FutureProvider<bool>((ref) async {
  final authService = ref.watch(authServiceProvider);
  return await authService.isAuthenticated();
});
