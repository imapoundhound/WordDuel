# 🚀 WordDuel Android App

A modern, Material Design Android implementation of the WordDuel word guessing game!

## ✨ **Features Implemented:**

### **🎯 Core Gameplay**
- **Wordle-style gameplay** - Guess 5-letter words in 6 attempts
- **Visual feedback** - Green (correct), Yellow (present), Gray (absent)
- **Smart keyboard** - On-screen keyboard with color-coded feedback
- **Game board** - 6x5 grid with smooth animations

### **🎮 Game Modes**
- **One Player vs Bot** - Computer picks random words
- **Two Player** - Challenge friends locally
- **Friend System** - Find friends by email or random matchmaking

### **🎨 Modern UI/UX**
- **Material Design 3** - Latest Android design standards
- **Gradient backgrounds** - Beautiful purple-blue gradients
- **Smooth animations** - Entrance animations and feedback effects
- **Responsive layout** - Works on all screen sizes

## 🛠️ **Setup Instructions:**

### **1. Prerequisites**
- Android Studio (latest version)
- Android SDK 24+ (Android 7.0+)
- Kotlin support enabled

### **2. Import Project**
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to this folder and select it
4. Wait for Gradle sync to complete

### **3. Build & Run**
1. Connect Android device or start emulator
2. Click the green "Run" button (▶️)
3. Select your device and click "OK"

## 📱 **App Structure:**

### **Activities:**
- **MainActivity** - Game mode selection screen
- **GameActivity** - Bot mode gameplay
- **TwoPlayerActivity** - Two-player mode (coming soon)
- **StatsActivity** - Game statistics (coming soon)

### **Key Files:**
- `app/src/main/java/com/wordduel/app/MainActivity.kt` - Main menu logic
- `app/src/main/java/com/wordduel/app/GameActivity.kt` - Core game logic
- `app/src/main/res/layout/activity_main.xml` - Main menu UI
- `app/src/main/res/layout/activity_game.xml` - Game screen UI

## 🎯 **Current Status:**

### **✅ Completed:**
- Main menu with game mode selection
- Bot mode gameplay (fully functional)
- Game board with 6x5 grid
- On-screen keyboard with feedback
- Word validation and scoring
- Smooth animations and transitions
- Material Design UI components

### **🚧 In Progress:**
- Two-player mode implementation
- Statistics tracking system
- Friend system integration
- Word list expansion

### **📋 Next Steps:**
1. **Two Player Mode** - Complete local multiplayer
2. **Statistics System** - Track wins, losses, streaks
3. **Word List** - Expand to 12,000+ words
4. **Online Features** - Real multiplayer over internet
5. **Settings & Customization** - Themes, difficulty levels

## 🎨 **Customization:**

### **Colors:**
- Edit `app/src/main/res/values/colors.xml`
- Change gradient colors in `gradient_background.xml`

### **Words:**
- Expand word list in `GameActivity.kt`
- Add more words to the `words` list

### **UI:**
- Modify layouts in `res/layout/` folder
- Update strings in `res/values/strings.xml`

## 🐛 **Troubleshooting:**

### **Common Issues:**
1. **Gradle sync fails** - Check internet connection, update Android Studio
2. **Build errors** - Clean project (Build → Clean Project)
3. **App crashes** - Check logcat for error messages
4. **Layout issues** - Verify device compatibility (API 24+)

### **Performance Tips:**
- Use release builds for testing
- Enable R8 optimization in release builds
- Monitor memory usage in large word lists

## 🚀 **Future Enhancements:**

### **Short Term:**
- Complete two-player mode
- Add sound effects and music
- Implement statistics tracking
- Expand word dictionary

### **Long Term:**
- Online multiplayer
- Social features (leaderboards, friends)
- Custom themes and skins
- Accessibility improvements
- Multiple languages

## 📞 **Support:**

If you encounter any issues:
1. Check the logcat for error messages
2. Verify all dependencies are synced
3. Ensure device meets minimum requirements (API 24+)
4. Try cleaning and rebuilding the project

---

**Happy coding! 🎮✨**

The WordDuel Android app brings the web version's excitement to mobile with native performance and modern Material Design aesthetics.

