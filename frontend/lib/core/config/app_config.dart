import 'package:flutter_dotenv/flutter_dotenv.dart';

class AppConfig {
  static String get apiBaseUrl => dotenv.env['API_BASE_URL'] ?? 'http://localhost:8080/api';
  static String get appName => dotenv.env['APP_NAME'] ?? 'Modular App';
  static String get environment => dotenv.env['ENVIRONMENT'] ?? 'development';

  static const String tokenKey = 'auth_token';
  static const String userKey = 'user_data';
}
