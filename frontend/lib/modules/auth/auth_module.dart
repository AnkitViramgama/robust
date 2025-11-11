import 'package:flutter_modular/flutter_modular.dart';
import 'package:modular_app/modules/auth/pages/login_page.dart';
import 'package:modular_app/modules/auth/pages/register_page.dart';

class AuthModule extends Module {
  @override
  void routes(r) {
    r.child('/', child: (context) => const LoginPage());
    r.child('/login', child: (context) => const LoginPage());
    r.child('/register', child: (context) => const RegisterPage());
  }
}
