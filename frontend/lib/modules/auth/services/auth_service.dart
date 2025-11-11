import 'package:modular_app/core/services/api_service.dart';
import 'package:modular_app/core/services/storage_service.dart';
import 'package:modular_app/modules/auth/models/auth_response.dart';
import 'package:modular_app/shared/models/user_model.dart';

class AuthService {
  final ApiService _apiService;
  final StorageService _storageService;

  AuthService(this._apiService, this._storageService);

  Future<AuthResponse> login(String username, String password) async {
    final response = await _apiService.post('/auth/login', data: {
      'username': username,
      'password': password,
    });

    final authResponse = AuthResponse.fromJson(response.data['data']);
    await _storageService.saveToken(authResponse.token);
    await _storageService.saveUser(authResponse.user.toJson());

    return authResponse;
  }

  Future<AuthResponse> register({
    required String username,
    required String email,
    required String password,
    String? firstName,
    String? lastName,
  }) async {
    final response = await _apiService.post('/auth/register', data: {
      'username': username,
      'email': email,
      'password': password,
      'firstName': firstName,
      'lastName': lastName,
    });

    final authResponse = AuthResponse.fromJson(response.data['data']);
    await _storageService.saveToken(authResponse.token);
    await _storageService.saveUser(authResponse.user.toJson());

    return authResponse;
  }

  Future<void> logout() async {
    await _apiService.post('/auth/logout');
    await _storageService.removeToken();
    await _storageService.removeUser();
  }

  Future<bool> isAuthenticated() async {
    final token = await _storageService.getToken();
    return token != null;
  }

  Future<UserModel?> getCurrentUser() async {
    final userData = await _storageService.getUser();
    if (userData != null) {
      return UserModel.fromJson(userData);
    }
    return null;
  }
}
