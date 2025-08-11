import 'package:flutter/material.dart';

class DuelPage extends StatelessWidget {
  const DuelPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Word Duel")),
      body: const Center(child: Text("Duel in progress...")),
    );
  }
}