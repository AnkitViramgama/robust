import 'package:flutter_modular/flutter_modular.dart';
import 'package:modular_app/modules/dashboard/pages/dashboard_page.dart';
import 'package:modular_app/modules/role/pages/roles_page.dart';

class DashboardModule extends Module {
  @override
  void routes(r) {
    r.child('/', child: (context) => const DashboardPage());
    r.child('/roles', child: (context) => const RolesPage());
  }
}
