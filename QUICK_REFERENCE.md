# 🎯 WordDuel One-Player Quick Reference

## Instant Test Guide

### 🚀 Launch Game
1. Open: `WordDuel.html` in browser
2. Login: `test@test.com` / `test`
3. Click: **One Player**
4. Click: **Start Game**

---

## ✅ Quick Validation Checks

### Check #1: Game Starts (10 seconds)
- [ ] Computer picks random word (check console)
- [ ] 6×5 game board appears
- [ ] On-screen keyboard visible
- [ ] Guess counter shows 6 empty dots

### Check #2: Input Works (30 seconds)
- [ ] Click keyboard letters → appear in cells
- [ ] Click backspace → removes last letter
- [ ] Try invalid word → error message
- [ ] Enter valid 5-letter word → accepts

### Check #3: Feedback Colors (1 minute)
- [ ] Correct letter/position → GREEN
- [ ] Correct letter/wrong position → YELLOW  
- [ ] Letter not in word → GRAY
- [ ] Keyboard keys match cell colors

### Check #4: Win/Loss (2 minutes)
- [ ] Guess correct word → celebration + "Congratulations!"
- [ ] 6 wrong guesses → word revealed + "Game over!"
- [ ] Statistics update after game ends
- [ ] "Play Again" and "Back to Menu" buttons work

### Check #5: Statistics Persist (30 seconds)
- [ ] Play one game
- [ ] Refresh browser page
- [ ] Login again
- [ ] Check statistics still there

---

## 🎮 Sample Game Walkthrough

### Target Word: APPLE

```
Guess 1: STARE
Result: S(gray) T(gray) A(yellow) R(gray) E(yellow)
Message: "Try again! 5 attempts remaining."

Guess 2: PLANE  
Result: P(yellow) L(yellow) A(green) N(gray) E(green)
Message: "Try again! 4 attempts remaining."

Guess 3: APPLE
Result: A(green) P(green) P(green) L(green) E(green)
Message: "Congratulations! You guessed the word!"
🎉 Celebration animations play
```

---

## 🐛 Common Issues & Solutions

### Issue: Can't type letters
**Solution**: Click directly on keyboard buttons (typing on physical keyboard doesn't work)

### Issue: Word rejected as invalid
**Solution**: Make sure it's a real 5-letter English word from the dictionary

### Issue: Statistics don't save
**Solution**: Check if browser allows localStorage (enable cookies)

### Issue: Game stuck
**Solution**: Refresh page and login again

### Issue: Colors don't make sense
**Solution**: Remember: Yellow = right letter, wrong spot; Green = right letter, right spot

---

## 📊 Statistics Explained

- **Total Games**: Number of games you've played
- **Games Won**: Number of times you guessed correctly
- **Best Score**: Fewest guesses needed to win
- **Average Attempts**: Your typical number of guesses
- **Distribution**: Bar chart showing wins per guess count (1-6)

---

## 🎲 Test Words to Try

### Easy Words (common):
- HOUSE
- WORLD
- HAPPY
- BRAIN
- LIGHT

### Medium Words:
- CRISP
- FLUTE
- GRADE
- PLUMB
- SWIFT

### Hard Words (uncommon letters):
- FJORD
- JAZZY
- QUIRK
- WALTZ
- ZEBRA

---

## 🔍 Debug Mode

Open browser console (F12) to see:
- Selected word (for testing)
- Validation messages
- Statistics updates
- Error logs

**Tip**: Don't look at the console during real play - it shows the answer!

---

## ⚡ Performance Targets

- Page load: < 2 seconds
- Keyboard click: < 100ms response
- Guess submission: < 500ms feedback
- Statistics load: < 500ms
- Game reset: < 1 second

If slower, check browser or refresh page.

---

## 🎯 Win Rate Guide

- **80%+ wins**: Expert level! 🏆
- **60-79% wins**: Very good! 🌟
- **40-59% wins**: Getting better! 💪
- **20-39% wins**: Keep practicing! 📚
- **<20% wins**: Just starting! 🎮

---

## 📁 Files Created

1. **IMPLEMENTATION_COMPLETE.md** - Full summary
2. **ONE_PLAYER_TEST_PLAN.md** - Detailed test plan
3. **BUG_FIXES.md** - Bug documentation
4. **QUICK_REFERENCE.md** - This file
5. **WordDuel.html** - Game file (with fixes applied)

---

## 🚨 Emergency Commands

If game breaks:
1. **Hard Refresh**: Ctrl+Shift+R (or Cmd+Shift+R on Mac)
2. **Clear Data**: F12 → Application → Clear Storage
3. **Reset Stats**: Console → `localStorage.clear()`
4. **Start Fresh**: Close browser completely, reopen

---

## ✅ Final Checklist

Before declaring "complete":
- [ ] Played 3+ games successfully
- [ ] Won at least once
- [ ] Lost at least once  
- [ ] Statistics showing correctly
- [ ] Stats persist after refresh
- [ ] No console errors
- [ ] Keyboard responsive
- [ ] Feedback colors correct
- [ ] Celebration plays on win
- [ ] Can play multiple games in a row

**All checked? Game is READY! 🎉**

---

## 💪 You're Ready To Play!

**Just open WordDuel.html and start guessing words. Have fun!**

Questions? Check the other documentation files for details.

---

Last Updated: October 12, 2025
Version: 1.0
