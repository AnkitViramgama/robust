import 'package:modular_app/core/services/api_service.dart';
import 'package:modular_app/modules/role/models/role_model.dart';

class RoleService {
  final ApiService _apiService;

  RoleService(this._apiService);

  Future<List<RoleModel>> getAllRoles() async {
    final response = await _apiService.get('/roles');
    final List<dynamic> data = response.data['data'] ?? [];
    return data.map((item) => RoleModel.fromJson(item)).toList();
  }

  Future<RoleModel> createRole(RoleModel role) async {
    final response = await _apiService.post('/roles', data: role.toJson());
    return RoleModel.fromJson(response.data['data']);
  }

  Future<RoleModel> updateRole(String id, RoleModel role) async {
    final response = await _apiService.put('/roles/$id', data: role.toJson());
    return RoleModel.fromJson(response.data['data']);
  }

  Future<void> deleteRole(String id) async {
    await _apiService.delete('/roles/$id');
  }
}
