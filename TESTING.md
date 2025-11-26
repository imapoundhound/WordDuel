# TESTING.md — WordDuel (Firebase-enabled)

This document describes the minimal QA steps and checklist to validate the app after enabling Firebase. It assumes you have set `window.firebaseConfig` and enabled the feature flag (`WD_USE_FIREBASE = true`) in `WordDuel.html`.

Quick start (local)
- Serve the repo or open `WordDuel.html` in a browser that allows module imports (modern Chrome/Edge/Firefox). For GitHub Pages deploy see `GH_PAGES_DEPLOY.md`.
- Ensure `window.firebaseConfig` is present (copy/paste from Firebase console) before the main app script runs.

Essential test checklist
1) Firebase initialization
- Open DevTools Console and reload the page.
- Verify console prints: `Attempting to initialize Firebase adapter...` then `Firebase initialized successfully.`
- If it logs a missing config, ensure `window.firebaseConfig` is defined before app script execution.

2) Registration (Email/Password)
- In the app, switch to the Register tab and create a new account using a test email you control.
- Expected:
  - Firebase registers the user and creates a Firestore `users/{uid}` document (check Firebase console → Firestore).
  - The UI should auto-hide the auth box and show the game mode selector.
  - You should see a success toast message.
- Edge cases:
  - Try the same email twice — Firebase should return an error and the UI shows an error message.
  - Invalid email format or short password should block registration (client validation).

3) Login
- Sign out (use app's logout UI or `logout()` from console via adapter), then log in with the registered email/password.
- Expected:
  - Login succeeds, session is stored (`wordduel_session` localStorage) with `uid` and email.
  - Game mode selector is visible.

4) One Player vs Bot flow
- Choose `One Player vs Bot`.
- Verify the game board, keyboard, and guess counter appear.
- Play a game and either win or exhaust attempts.
- Expected:
  - After the game ends, check Firestore `users/{uid}.stats.bot` updated (or at least the adapter attempted to write).
  - If not visible immediately, check for console logs from the adapter.

5) Stats validation
- After playing multiple games, inspect the Firestore user doc and confirm `stats.bot.totalGames`, `gamesWon`, and `distribution` change as expected.
- Also check client-side `wordduel_users` (local fallback) to ensure no conflicts (we're not migrating old users automatically).

6) Logout & Session
- Click logout and confirm local session cleared and UI returns to login/register.
- Reload the page to validate session persistence behavior (Firebase auth persists by default; `onAuthStateChanged` should restore UI).

7) Error conditions
- Disable `window.firebaseConfig` or introduce a malformed config and confirm the app falls back to demo behavior (localStorage) and logs a warning.
- Test network failure: block outbound requests to `firebase` CDN and confirm graceful fallback to demo and visible console messages.

8) Browser & platform checks
- Test at least Chrome (latest), Edge (latest), and Firefox ESR.
- Mobile: test on mobile Chrome or Safari; ensure inputs and overlays behave correctly and keyboard focus is ok.

9) Security & console check
- Confirm no secret keys or service account files are present in the repo.
- Check console for any uncaught exceptions during auth flows and report them.

Notes & next steps
- The app currently writes per-user stats to Firestore when available and keeps local fallback.
- We did not migrate existing `wordduel_users` from localStorage. If you want those migrated, I can prepare a careful migration helper that either creates Firestore user docs and sends password-reset emails or exports user email addresses only.

Reporting bugs
- Please open issues on the repository with steps to reproduce, browser/OS, and console logs. For small UI issues, attach screenshots.