import 'package:modular_app/shared/models/user_model.dart';

class AuthResponse {
  final String token;
  final String tokenType;
  final UserModel user;

  AuthResponse({
    required this.token,
    required this.tokenType,
    required this.user,
  });

  factory AuthResponse.fromJson(Map<String, dynamic> json) {
    return AuthResponse(
      token: json['token'] ?? '',
      tokenType: json['tokenType'] ?? 'Bearer',
      user: UserModel.fromJson(json['user'] ?? {}),
    );
  }
}
