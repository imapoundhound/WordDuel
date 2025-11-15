# 🧪 TWO-PLAYER MODE - VISUAL TEST CHECKLIST

**Quick 5-Minute Smoke Test**  
**Tester:** No One  
**Date:** _________  
**Build:** WordDuel.html (Nov 14, 2025)

---

## ✅ PRE-TEST CHECKLIST

- [ ] Clear browser cache (Ctrl+Shift+Del)
- [ ] Close all browser tabs
- [ ] Open fresh browser window
- [ ] Navigate to `WordDuel.html`
- [ ] Open DevTools Console (F12)

---

## 🎯 TEST 1: SAME-DEVICE GAME (5 MIN)

### **Start to Finish Flow**

#### **Login**
- [ ] Login screen appears
- [ ] Enter: test@test.com / test
- [ ] Click "Login"
- [ ] Redirects to game mode selection

#### **Select Two-Player**
- [ ] See "Player vs. Bot" and "Two Player" buttons
- [ ] Click "Two Player"
- [ ] Friend selector appears

#### **Friend Selector Screen**
- [ ] See 5 options:
  - [ ] 🎮 Same Device (Pass & Play)
  - [ ] 🌐 Local Network
  - [ ] 📧 Find Friend by Email
  - [ ] 🎲 Random Matchmaking
  - [ ] ← Back to Game Modes

#### **Start Same-Device Game**
- [ ] Click "🎮 Same Device (Pass & Play)"
- [ ] Game board appears
- [ ] Keyboard is HIDDEN
- [ ] Guess counter is HIDDEN
- [ ] Message: "Player 1: Enter your 5-letter secret word"

#### **Player 1 Word Entry**
- [ ] Click in first cell
- [ ] Type: C
  - [ ] Shows as: ●
- [ ] Type: R
  - [ ] Shows as: ●
- [ ] Type: A
  - [ ] Shows as: ●
- [ ] Type: N
  - [ ] Shows as: ●
- [ ] Type: E
  - [ ] Shows as: ●
- [ ] Word fully masked: ●●●●●
- [ ] Press Enter

#### **Pass Device Screen**
- [ ] Full-screen overlay appears
- [ ] Title: "🔄 Pass Device"
- [ ] Message: "Player 1's word has been set!"
- [ ] Large button: "Player 2 Ready! ✓"
- [ ] Cancel button visible
- [ ] Previous word NOT visible anywhere

#### **Continue to Player 2**
- [ ] Click "Player 2 Ready! ✓"
- [ ] Overlay disappears
- [ ] Game board clears
- [ ] All cells empty
- [ ] Message: "Player 2: Enter your 5-letter secret word"
- [ ] Focus on first cell

#### **Player 2 Word Entry**
- [ ] Type: SLATE
  - [ ] Each letter shows as: ●
  - [ ] Complete word: ●●●●●
- [ ] Press Enter

#### **Ready to Play Screen**
- [ ] Full-screen overlay appears
- [ ] Title: "🎮 Ready to Play!"
- [ ] "Both players have set their secret words!"
- [ ] Game rules displayed:
  - [ ] Mentions 6 attempts
  - [ ] Explains color coding
  - [ ] Shows who goes first
- [ ] Button: "Start Guessing! 🎯"
- [ ] Cancel button visible

#### **Start Guessing Phase**
- [ ] Click "Start Guessing! 🎯"
- [ ] Overlay disappears
- [ ] Fresh game board (empty)
- [ ] Keyboard appears (all keys active)
- [ ] Guess counter appears
- [ ] Message: "Player 1: Guess Player 2's word! (6 attempts)"
- [ ] Focus on first cell of row 1

#### **Make First Guess**
- [ ] Type: STARE
- [ ] Letters appear in real-time (not masked)
- [ ] Press Enter
- [ ] Tiles flip with colors:
  - [ ] S: GREEN (first letter of SLATE)
  - [ ] T: YELLOW (T is in SLATE)
  - [ ] A: YELLOW (A is in SLATE)
  - [ ] R: GRAY (not in SLATE)
  - [ ] E: GREEN (last letter of SLATE)
- [ ] Keyboard updates:
  - [ ] S: GREEN
  - [ ] T: YELLOW
  - [ ] A: YELLOW
  - [ ] R: GRAY
  - [ ] E: GREEN
- [ ] Guess counter: "Guess 1 of 6"
- [ ] Focus moves to next row

#### **Win Condition Test**
- [ ] Type: SLATE (exact match)
- [ ] Press Enter
- [ ] All tiles turn GREEN
- [ ] Success message appears
- [ ] Shows number of attempts
- [ ] Celebration animation (if any)

---

## 🧭 TEST 2: NAVIGATION (2 MIN)

### **Every Back Button Works**

#### **From Friend Selector**
- [ ] Click "🎮 Same Device"
- [ ] Game starts
- [ ] NO BACK BUTTON (game started)

#### **Re-navigate**
- [ ] Refresh page
- [ ] Login again
- [ ] Click "Two Player"

#### **Try Each Option**
- [ ] Click "🌐 Local Network"
  - [ ] Sub-menu appears
  - [ ] Click "Back"
  - [ ] Returns to friend selector

- [ ] Click "📧 Find Friend by Email"
  - [ ] Email input appears
  - [ ] Click "Back"
  - [ ] Returns to friend selector

- [ ] Click "🎲 Random Matchmaking"
  - [ ] Match screen appears
  - [ ] Click "Back"
  - [ ] Returns to friend selector

- [ ] Click "← Back to Game Modes"
  - [ ] Returns to Bot/Two-Player selection

---

## 📧 TEST 3: EMAIL INPUT (1 MIN)

### **No Focus Issues**

- [ ] Navigate to "Two Player" → "Find Friend by Email"
- [ ] Click in email field
- [ ] Type: test@example.com
  - [ ] All characters go to email field
  - [ ] No characters jump to other inputs
  - [ ] Can backspace normally
  - [ ] Can delete normally
- [ ] Click "Send Invite"
  - [ ] Shows success message
  - [ ] Displays invite code
- [ ] Click "Back"
  - [ ] Returns cleanly

---

## 🎲 TEST 4: RANDOM MATCH (1 MIN)

### **Progressive Status Updates**

- [ ] Navigate to "Two Player" → "Random Matchmaking"
- [ ] See informative pre-screen:
  - [ ] Explains wait time
  - [ ] Shows benefits
- [ ] Click "Find Match Now!"
- [ ] Status updates appear:
  - [ ] "🔍 Connecting to matchmaking server..."
  - [ ] "⚡ Searching for available players..."
  - [ ] "🎯 Finding player with similar rank..."
  - [ ] "✨ Match found! Connecting..."
- [ ] Updates happen ~1-2 seconds apart
- [ ] "Cancel Search" button visible entire time
- [ ] After 5 seconds, shows "match found"

---

## ❌ TEST 5: CANCEL FLOWS (2 MIN)

### **Can Exit Anywhere**

#### **During Word Entry**
- [ ] Start same-device game
- [ ] Player 1 typing word...
- [ ] Try to cancel (no button during gameplay)
- [ ] Expected: Must finish or refresh

#### **During Pass Device**
- [ ] Get to "Pass Device" screen
- [ ] Click "Cancel Game"
  - [ ] Returns to friend selector
  - [ ] Game state cleared

#### **During Ready to Play**
- [ ] Get both players' words entered
- [ ] On "Ready to Play" screen
- [ ] Click "Cancel Game"
  - [ ] Returns to friend selector
  - [ ] Game state cleared

#### **During Random Match Wait**
- [ ] Start random match search
- [ ] Click "Cancel Search"
  - [ ] Stops searching
  - [ ] Returns to friend selector

---

## 🐛 CONSOLE CHECKS

### **Check for Errors**

**Open Console (F12) and verify:**

- [ ] No red errors during login
- [ ] No red errors during word entry
- [ ] No red errors during pass device
- [ ] No red errors during guessing
- [ ] No red errors during navigation

**Expected Console Output:**
```
Game initialized. Target word set: [word]
Player 1 word set: crane
Player 2 word set: slate
About to validate word: stare
Feedback generated: ['correct', 'present', 'present', 'absent', 'correct']
Comparing: stare vs slate
```

---

## ✅ PASS CRITERIA

**All checks must pass:**

1. **Same-device game completes start-to-finish** ✓
2. **Words masked during entry** ✓
3. **Pass device screens work** ✓
4. **Navigation never traps user** ✓
5. **Email input works cleanly** ✓
6. **Random match has clear status** ✓
7. **Cancel buttons function** ✓
8. **No console errors** ✓

---

## 🔴 FAILURE CONDITIONS

**If ANY of these occur, FAIL the test:**

- [ ] Word appears unmasked during entry
- [ ] Can see other player's word
- [ ] Stuck on screen with no way out
- [ ] Email input characters jump around
- [ ] Random match hangs with no feedback
- [ ] Console shows errors
- [ ] Game crashes or freezes
- [ ] Colors don't match correctly
- [ ] Keyboard doesn't update

---

## 📝 NOTES SECTION

**Issues Found:**
```
1. ___________________________________________
2. ___________________________________________
3. ___________________________________________
```

**Performance:**
```
- Loading time: ______ seconds
- Word entry smoothness: ______ (1-10)
- Transition smoothness: ______ (1-10)
```

**UX Feedback:**
```
- Confusing areas: _______________________
- Smooth areas: _________________________
- Suggestions: __________________________
```

---

## ✅ SIGN-OFF

**Test completed:** _____ / _____ / _____

**Result:** 
- [ ] ✅ PASS - Ready to ship
- [ ] ⚠️ MINOR ISSUES - Fix and retest
- [ ] ❌ MAJOR ISSUES - Needs work

**Tested by:** ___________________

**Notes:** ___________________________________

---

**REMEMBER:** 
- Clear cache before each test
- Use fresh browser session
- Check console for errors
- Test on different browsers if possible
- Have fun! This is supposed to be a game! 🎮
