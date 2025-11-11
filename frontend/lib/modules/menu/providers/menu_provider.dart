import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:modular_app/core/di/service_locator.dart';
import 'package:modular_app/modules/menu/models/menu_item_model.dart';
import 'package:modular_app/modules/menu/services/menu_service.dart';

final menuServiceProvider = Provider<MenuService>((ref) => getIt<MenuService>());

final menuHierarchyProvider = FutureProvider<List<MenuItemModel>>((ref) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.getMenuHierarchy();
});
