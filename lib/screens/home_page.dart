import 'package:flutter/material.dart';
import 'lobby_page.dart';
import 'duel_page.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("WordDuel Home")),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              child: const Text("Go to Lobby"),
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const LobbyPage()),
              ),
            ),
            ElevatedButton(
              child: const Text("Start Duel"),
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const DuelPage()),
              ),
            ),
          ],
        ),
      ),
    );
  }
}