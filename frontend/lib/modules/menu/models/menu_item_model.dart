class MenuItemModel {
  final String id;
  final String itemId;
  final String label;
  final String path;
  final String? icon;
  final String? module;
  final int order;
  final List<String> rolesAllowed;
  final bool active;
  final bool visible;
  final List<MenuItemModel> children;

  MenuItemModel({
    required this.id,
    required this.itemId,
    required this.label,
    required this.path,
    this.icon,
    this.module,
    required this.order,
    required this.rolesAllowed,
    required this.active,
    required this.visible,
    this.children = const [],
  });

  factory MenuItemModel.fromJson(Map<String, dynamic> json) {
    return MenuItemModel(
      id: json['id'] ?? '',
      itemId: json['itemId'] ?? '',
      label: json['label'] ?? '',
      path: json['path'] ?? '',
      icon: json['icon'],
      module: json['module'],
      order: json['order'] ?? 0,
      rolesAllowed: List<String>.from(json['rolesAllowed'] ?? []),
      active: json['active'] ?? true,
      visible: json['visible'] ?? true,
      children: (json['children'] as List<dynamic>?)
              ?.map((child) => MenuItemModel.fromJson(child))
              .toList() ??
          [],
    );
  }
}
