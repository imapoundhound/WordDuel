// Simple Firebase adapter (ES module) for WordDuel
// Usage: import * as FB from './firebase/firebase-init.js';
// Then call `await FB.initFirebase(firebaseConfig);`

export let firebaseApp = null;
export let auth = null;
export let db = null;

// Initialize Firebase using CDN modular SDK imports so this file can be used in the browser
export async function initFirebase(config) {
  if (!config || !config.apiKey) throw new Error('Missing firebase config');

  const { initializeApp } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-app.js');
  const authMod = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-auth.js');
  const firestoreMod = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-firestore.js');

  firebaseApp = initializeApp(config);
  auth = authMod.getAuth(firebaseApp);
  db = firestoreMod.getFirestore(firebaseApp);

  return { firebaseApp, auth, db };
}

export async function registerWithEmail(email, password, displayName, phone = null) {
  if (!auth) throw new Error('Firebase not initialized (call initFirebase)');
  const { createUserWithEmailAndPassword, updateProfile } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-auth.js');
  const { doc, setDoc } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-firestore.js');

  const userCred = await createUserWithEmailAndPassword(auth, email, password);
  if (displayName) await updateProfile(userCred.user, { displayName });

  // Create a user document for stats and profile
  await setDoc(doc(db, 'users', userCred.user.uid), {
    email: userCred.user.email,
    displayName: displayName || null,
    phone: phone || null,
    createdAt: new Date().toISOString(),
    stats: { bot: { totalGames: 0, gamesWon: 0, bestScore: null, totalAttempts: 0, distribution: {} } }
  });

  return userCred.user;
}

export async function loginWithEmail(email, password) {
  if (!auth) throw new Error('Firebase not initialized (call initFirebase)');
  const { signInWithEmailAndPassword } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-auth.js');
  const userCred = await signInWithEmailAndPassword(auth, email, password);
  return userCred.user;
}

export async function logout() {
  if (!auth) return;
  const { signOut } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-auth.js');
  await signOut(auth);
}

export function onAuthStateChanged(cb) {
  if (!auth) throw new Error('Firebase not initialized (call initFirebase)');
  const { onAuthStateChanged } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-auth.js');
  return onAuthStateChanged(auth, cb);
}

export async function getCurrentUser() {
  if (!auth) throw new Error('Firebase not initialized (call initFirebase)');
  return auth.currentUser || null;
}

// Save or update bot stats for a given user uid (merges into the user doc)
export async function saveBotStatsForUser(uid, botStats) {
  if (!db) throw new Error('Firestore not initialized (call initFirebase)');
  const { doc, setDoc, updateDoc } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-firestore.js');
  const userRef = doc(db, 'users', uid);
  await setDoc(userRef, { stats: { bot: botStats } }, { merge: true });
}

// Read user profile and stats
export async function getUserDoc(uid) {
  if (!db) throw new Error('Firestore not initialized (call initFirebase)');
  const { doc, getDoc } = await import('https://www.gstatic.com/firebasejs/9.22.2/firebase-firestore.js');
  const d = await getDoc(doc(db, 'users', uid));
  return d.exists() ? d.data() : null;
}

// Convenience: convert Firebase user object to minimal profile used by the app
export function toAppUser(firebaseUser, userDoc) {
  if (!firebaseUser) return null;
  return {
    uid: firebaseUser.uid,
    email: firebaseUser.email,
    displayName: firebaseUser.displayName || (userDoc && userDoc.displayName) || null,
    stats: (userDoc && userDoc.stats) || { bot: { totalGames: 0, gamesWon: 0, bestScore: null, totalAttempts: 0, distribution: {} } }
  };
}
