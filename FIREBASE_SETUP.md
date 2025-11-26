# Firebase Setup Guide for WordDuel

This document walks through creating a Firebase project and retrieving the config needed to initialize the client-side adapter added to this repo.

IMPORTANT: The scaffold in this repo is intended to help wire Firebase Auth and Firestore into the existing static app. You must follow all security guidance below before using Firebase in production.

1) Create a Firebase Project
- Visit https://console.firebase.google.com/ and create a new project (e.g., `wordduel-demo`).

2) Add a Web App
- In the project overview, click "Add app" → Web.
- Enter an app nickname and optionally enable Firebase Hosting if you want to host from Firebase.
- After creating the app, you'll get a `firebaseConfig` object. Copy it.
the ffollwoing is what was generated for the initialzation fo firebase. The remaining steps will be down below that 
npm install firebase
// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyBVpa04XgKNnN4txudjKdPiZOOd6r_BzhM",
  authDomain: "wordduel-demo.firebaseapp.com",
  projectId: "wordduel-demo",
  storageBucket: "wordduel-demo.firebasestorage.app",
  messagingSenderId: "278714517048",
  appId: "1:278714517048:web:a659e67842edc645ac88cd",
  measurementId: "G-Y0D5TPZJ1G"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

3) Enable Authentication
- From the left menu, choose "Authentication" → "Sign-in method".
- Enable **Email/Password** provider (and others if you want Google/Facebook, etc.).
- Optionally customize email templates.

4) Create Firestore (for user stats)
- In the left menu, choose "Firestore Database" and create a database in production or test mode.
- If you use production mode, set appropriate security rules to allow only authenticated users to read/write their own document.

Example Firestore rules (simple starter):

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

5) Authorized Domains (for OAuth / redirects)
- In the Firebase console under Authentication → Settings, add the allowed domain(s) where you'll host the static site (e.g., `yourusername.github.io`, `localhost`).

6) Add the `firebaseConfig` to the client
- Use the `firebase/firebase-init.js` module included in this repo. In your `WordDuel.html` add a module script that imports the adapter and calls `initFirebase(firebaseConfig)` (see `firebase/USAGE.md` for snippet).

7) Security Notes
- Do not store secret keys in client-side source control. The `firebaseConfig` object is not a secret (it identifies your project) but ensure service accounts and server secrets are not committed.
- Use Firestore security rules to prevent unauthorized access.
- For production: enforce strong password rules, email verification, and consider multi-factor auth.

8) Optional: migrate existing local demo users
- If you have demo users stored in `localStorage` you can export and import them into Firestore using a small script (not included). Be careful with passwords: local demo stores hashed passwords client-side; migrating users should require re-setting passwords or send password-reset emails.

9) Deploying
- For a static site, GitHub Pages, Netlify, or Vercel are good options. Register those origins in the Firebase console so Auth flows work properly.

If you want, I can:
- Add a small migration helper script that lists local users for manual import, or
- Wire `WordDuel.html` to use the Firebase adapter directly (I can make those edits next).
