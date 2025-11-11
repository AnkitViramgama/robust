import 'dart:convert';
import 'package:modular_app/core/config/app_config.dart';
import 'package:shared_preferences/shared_preferences.dart';

class StorageService {
  late SharedPreferences _prefs;

  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  // Token management
  Future<void> saveToken(String token) async {
    await _prefs.setString(AppConfig.tokenKey, token);
  }

  Future<String?> getToken() async {
    return _prefs.getString(AppConfig.tokenKey);
  }

  Future<void> removeToken() async {
    await _prefs.remove(AppConfig.tokenKey);
  }

  // User data management
  Future<void> saveUser(Map<String, dynamic> user) async {
    await _prefs.setString(AppConfig.userKey, jsonEncode(user));
  }

  Future<Map<String, dynamic>?> getUser() async {
    final userStr = _prefs.getString(AppConfig.userKey);
    if (userStr != null) {
      return jsonDecode(userStr);
    }
    return null;
  }

  Future<void> removeUser() async {
    await _prefs.remove(AppConfig.userKey);
  }

  // Generic storage
  Future<void> setString(String key, String value) async {
    await _prefs.setString(key, value);
  }

  String? getString(String key) {
    return _prefs.getString(key);
  }

  Future<void> setBool(String key, bool value) async {
    await _prefs.setBool(key, value);
  }

  bool? getBool(String key) {
    return _prefs.getBool(key);
  }

  Future<void> clear() async {
    await _prefs.clear();
  }
}
