import 'package:flutter_modular/flutter_modular.dart';
import 'package:modular_app/modules/auth/auth_module.dart';
import 'package:modular_app/modules/dashboard/dashboard_module.dart';

class AppModule extends Module {
  @override
  void routes(r) {
    r.module('/', module: AuthModule());
    r.module('/dashboard', module: DashboardModule());
  }
}
