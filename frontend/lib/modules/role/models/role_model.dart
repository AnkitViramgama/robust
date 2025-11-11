class RoleModel {
  final String id;
  final String name;
  final String? description;
  final List<String> permissions;
  final bool systemRole;

  RoleModel({
    required this.id,
    required this.name,
    this.description,
    required this.permissions,
    required this.systemRole,
  });

  factory RoleModel.fromJson(Map<String, dynamic> json) {
    return RoleModel(
      id: json['id'] ?? '',
      name: json['name'] ?? '',
      description: json['description'],
      permissions: List<String>.from(json['permissions'] ?? []),
      systemRole: json['systemRole'] ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'permissions': permissions,
      'systemRole': systemRole,
    };
  }
}
