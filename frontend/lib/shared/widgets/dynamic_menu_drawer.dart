import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:modular_app/modules/menu/models/menu_item_model.dart';

class DynamicMenuDrawer extends StatelessWidget {
  final List<MenuItemModel> menuItems;

  const DynamicMenuDrawer({super.key, required this.menuItems});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          const DrawerHeader(
            decoration: BoxDecoration(
              color: Colors.blue,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Icon(Icons.apps, size: 48, color: Colors.white),
                SizedBox(height: 16),
                Text(
                  'Modular App',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
          ...menuItems.map((item) => _buildMenuItem(context, item)),
        ],
      ),
    );
  }

  Widget _buildMenuItem(BuildContext context, MenuItemModel item) {
    if (item.children.isNotEmpty) {
      return ExpansionTile(
        leading: _getIcon(item.icon),
        title: Text(item.label),
        children: item.children.map((child) => _buildMenuItem(context, child)).toList(),
      );
    }

    return ListTile(
      leading: _getIcon(item.icon),
      title: Text(item.label),
      onTap: () {
        Navigator.pop(context);
        Modular.to.navigate('/dashboard${item.path}');
      },
    );
  }

  Icon _getIcon(String? iconName) {
    if (iconName == null) return const Icon(Icons.circle_outlined);

    final iconMap = {
      'dashboard': Icons.dashboard,
      'people': Icons.people,
      'person': Icons.person,
      'settings': Icons.settings,
      'inventory': Icons.inventory,
      'shopping_cart': Icons.shopping_cart,
      'home': Icons.home,
      'menu': Icons.menu,
    };

    return Icon(iconMap[iconName] ?? Icons.circle_outlined);
  }
}
