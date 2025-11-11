import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:modular_app/core/di/service_locator.dart';
import 'package:modular_app/modules/auth/providers/auth_provider.dart';
import 'package:modular_app/modules/auth/services/auth_service.dart';
import 'package:modular_app/modules/menu/providers/menu_provider.dart';
import 'package:modular_app/shared/widgets/dynamic_menu_drawer.dart';

class DashboardPage extends ConsumerWidget {
  const DashboardPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final userAsync = ref.watch(currentUserProvider);
    final menuAsync = ref.watch(menuHierarchyProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () async {
              await getIt<AuthService>().logout();
              Modular.to.navigate('/login');
            },
          ),
        ],
      ),
      drawer: DynamicMenuDrawer(menuItems: menuAsync.value ?? []),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.dashboard, size: 100, color: Colors.blue),
            const SizedBox(height: 24),
            Text(
              'Welcome to the Dashboard',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            const SizedBox(height: 16),
            userAsync.when(
              data: (user) => user != null
                  ? Column(
                      children: [
                        Text(
                          'Hello, ${user.firstName ?? user.username}!',
                          style: Theme.of(context).textTheme.titleLarge,
                        ),
                        const SizedBox(height: 8),
                        Text(
                          user.email,
                          style: Theme.of(context).textTheme.bodyMedium,
                        ),
                      ],
                    )
                  : const Text('No user data'),
              loading: () => const CircularProgressIndicator(),
              error: (error, stack) => Text('Error: $error'),
            ),
            const SizedBox(height: 32),
            menuAsync.when(
              data: (menus) => Column(
                children: [
                  Text(
                    'Available Menu Items: ${menus.length}',
                    style: Theme.of(context).textTheme.bodyLarge,
                  ),
                  const SizedBox(height: 16),
                  Text(
                    'Use the drawer to navigate',
                    style: Theme.of(context).textTheme.bodyMedium,
                  ),
                ],
              ),
              loading: () => const CircularProgressIndicator(),
              error: (error, stack) => const Text('Error loading menus'),
            ),
          ],
        ),
      ),
    );
  }
}
