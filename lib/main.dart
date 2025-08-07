import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';
import 'services/auth_service.dart';
import 'screens/home_page.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  final authService = AuthService();
  await authService.signInAnon();
  await testFirestoreWrite();

  runApp(const WordDuelApp());
}

Future<void> testFirestoreWrite() async {
  await FirebaseFirestore.instance.collection('test_duels').add({
    'timestamp': Timestamp.now(),
    'player': 'anon',
    'status': 'initialized',
  });
}

class WordDuelApp extends StatelessWidget {
  const WordDuelApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'WordDuel',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: const HomePage(),
    );
  }
}