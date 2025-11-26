# Firebase Adapter Usage (WordDuel)

This file shows how to wire the `firebase/firebase-init.js` adapter into `WordDuel.html`.

1) Add the module script to `WordDuel.html` near your other scripts (example):

```html
<script type="module">
  import * as FB from './firebase/firebase-init.js';

  const firebaseConfig = {
    apiKey: "YOUR_API_KEY",
    authDomain: "YOUR_AUTH_DOMAIN",
    projectId: "YOUR_PROJECT_ID",
    storageBucket: "YOUR_STORAGE_BUCKET",
    messagingSenderId: "YOUR_SENDER_ID",
    appId: "YOUR_APP_ID"
  };

  // Initialize Firebase
  FB.initFirebase(firebaseConfig).then(() => {
    console.log('Firebase initialized');

    // Example: listen for auth state changes and update UI
    FB.auth && FB.auth.onAuthStateChanged && FB.auth.onAuthStateChanged((user) => {
      // The adapter exports `auth` as a top-level variable; prefer using onAuthStateChanged wrapper below
    });

    // Recommended: use the adapter's onAuthStateChanged wrapper
    FB.onAuthStateChanged(async (firebaseUser) => {
      if (firebaseUser) {
        const doc = await FB.getUserDoc(firebaseUser.uid);
        const appUser = FB.toAppUser(firebaseUser, doc);
        // Replace local session: set UI to logged-in and show mode selector
        // e.g., window.showLoggedInUser(appUser) or set in local app state
        console.log('Logged in as', appUser.email);
      } else {
        console.log('Not logged in');
      }
    });
  }).catch(err => console.error('Firebase init failed', err));

  // Example usage wrappers:
  // await FB.registerWithEmail('hi@example.com', 'password123', 'PlayerOne');
  // await FB.loginWithEmail('hi@example.com', 'password123');
  // await FB.logout();
</script>
```

2) Replacing local demo auth calls
- Where `registerUser()` currently writes to `localStorage`, replace the logic with a call to `FB.registerWithEmail()` and then use `FB.getUserDoc()` to fetch stats or profile.
- Where `loginUser()` currently validates against localStorage, call `FB.loginWithEmail()`.
- Where you persist bot stats to a user record, call `FB.saveBotStatsForUser(uid, botStats)`.

3) Notes
- The adapter uses the Firebase CDN modular SDK (imports at runtime). No npm install required for a static site.
- For local development, you can use `localhost` as an authorized domain in Firebase console.
- For production, update Firestore security rules so users can only access their own docs.

4) Next steps I can do for you (pick one):
- Wire `WordDuel.html` to use this adapter (replace demo auth with Firebase calls).
- Add a migration helper to export local users for manual import into Firestore.
- Implement serverless migration script to import demo users (not recommended without re-checking password handling).
