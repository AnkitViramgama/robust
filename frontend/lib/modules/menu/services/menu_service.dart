import 'package:modular_app/core/services/api_service.dart';
import 'package:modular_app/modules/menu/models/menu_item_model.dart';

class MenuService {
  final ApiService _apiService;

  MenuService(this._apiService);

  Future<List<MenuItemModel>> getMenuHierarchy() async {
    final response = await _apiService.get('/menus/hierarchy');
    final List<dynamic> data = response.data['data'] ?? [];
    return data.map((item) => MenuItemModel.fromJson(item)).toList();
  }

  Future<List<MenuItemModel>> getAllMenuItems() async {
    final response = await _apiService.get('/menus');
    final List<dynamic> data = response.data['data'] ?? [];
    return data.map((item) => MenuItemModel.fromJson(item)).toList();
  }
}
