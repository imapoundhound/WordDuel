 # 🚀 Android Studio Gradle Environment Variables Fix
## Complete Solution for Windows 10/11

### 🚨 **The Problem**
Android Studio can't find Gradle because the environment variables aren't properly set up. This is a common issue that prevents projects from building.

---

## 🔧 **Solution 1: Automatic Fix (Recommended)**

### Step 1: Download Gradle
1. Go to [Gradle Releases](https://gradle.org/releases/)
2. Download the latest stable version (currently 8.5)
3. Extract to: `C:\Gradle\gradle-8.5`

### Step 2: Set Environment Variables
1. Press `Win + R`, type `sysdm.cpl`, press Enter
2. Click **Advanced** tab → **Environment Variables**
3. Under **System Variables**, click **New**
4. **Variable name**: `GRADLE_HOME`
5. **Variable value**: `C:\Gradle\gradle-8.5`
6. Click **OK**

### Step 3: Add to PATH
1. In **System Variables**, find **Path**, click **Edit**
2. Click **New**, add: `%GRADLE_HOME%\bin`
3. Click **OK** on all dialogs

### Step 4: Verify
1. Open **Command Prompt** (new window)
2. Type: `gradle --version`
3. Should show Gradle version info

---

## 🛠️ **Solution 2: Manual Android Studio Fix**

### Step 1: Open Android Studio
1. Launch Android Studio
2. Go to **File** → **Settings** (or **Android Studio** → **Preferences** on Mac)

### Step 2: Configure Gradle
1. **Build, Execution, Deployment** → **Build Tools** → **Gradle**
2. **Gradle JDK**: Select your JDK (usually bundled)
3. **Use Gradle from**: Select **Specified location**
4. **Gradle location**: Browse to `C:\Gradle\gradle-8.5`
5. Click **Apply** → **OK**

### Step 3: Sync Project
1. Click **File** → **Sync Project with Gradle Files**
2. Or click the **Sync Now** button in the toolbar

---

## 🔍 **Solution 3: Project-Level Fix**

### Step 1: Check Project Structure
1. **File** → **Project Structure**
2. **Project** tab: Verify **Gradle Version**
3. **Modules** tab: Check **Android Gradle Plugin Version**

### Step 2: Update Gradle Wrapper
1. Open `gradle/wrapper/gradle-wrapper.properties`
2. Update `distributionUrl` to latest version:
   ```properties
   distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
   ```

### Step 3: Update Build Files
1. **Project-level** `build.gradle`:
   ```gradle
   buildscript {
       dependencies {
           classpath 'com.android.tools.build:gradle:8.2.0'
       }
   }
   ```

2. **App-level** `build.gradle`:
   ```gradle
   android {
       compileSdk 34
       defaultConfig {
           minSdk 24
           targetSdk 34
       }
   }
   ```

---

## 🚨 **Troubleshooting Common Issues**

### Issue 1: "Gradle not found"
**Fix**: Set `GRADLE_HOME` environment variable

### Issue 2: "JDK not found"
**Fix**: 
1. Download JDK 17 or 21 from Oracle/OpenJDK
2. Set `JAVA_HOME` environment variable
3. Add `%JAVA_HOME%\bin` to PATH

### Issue 3: "Build tools not found"
**Fix**:
1. **Tools** → **SDK Manager**
2. **SDK Tools** tab
3. Install **Android SDK Build-Tools**

### Issue 4: "Gradle daemon failed"
**Fix**:
1. **File** → **Invalidate Caches and Restart**
2. Delete `.gradle` folder in project
3. Restart Android Studio

---

## 📋 **Complete Environment Setup**

### Required Environment Variables
```batch
# System Variables
JAVA_HOME=C:\Program Files\Java\jdk-17
GRADLE_HOME=C:\Gradle\gradle-8.5
ANDROID_HOME=C:\Users\[Username]\AppData\Local\Android\Sdk

# Add to PATH
%JAVA_HOME%\bin
%GRADLE_HOME%\bin
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\tools
```

### Verification Commands
```batch
# Check Java
java -version
javac -version

# Check Gradle
gradle --version

# Check Android SDK
adb version
```

---

## 🎯 **Quick Fix Script**

Create `fix_gradle.bat` in your project folder:
```batch
@echo off
echo Fixing Android Studio Gradle issues...

echo Setting environment variables...
setx GRADLE_HOME "C:\Gradle\gradle-8.5" /M
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M

echo Adding to PATH...
setx PATH "%PATH%;%GRADLE_HOME%\bin;%JAVA_HOME%\bin" /M

echo Done! Restart Android Studio and try again.
pause
```

---

## 🔄 **Alternative: Use Gradle Wrapper**

### What is Gradle Wrapper?
- Self-contained Gradle distribution
- No need to install Gradle globally
- Ensures consistent versions across team

### Enable in Project
1. **File** → **Settings** → **Build Tools** → **Gradle**
2. **Use Gradle from**: Select **Gradle Wrapper**
3. Click **Apply**

### Benefits
- ✅ No environment variables needed
- ✅ Consistent Gradle versions
- ✅ Works on any machine
- ✅ No manual Gradle installation

---

## 📱 **WordDuel Android Project Setup**

### Step 1: Import Project
1. **File** → **Open**
2. Navigate to your WordDuel Android project
3. Select the project folder

### Step 2: Let Gradle Sync
1. Android Studio will automatically sync
2. Wait for "Gradle sync finished" message
3. If errors occur, follow troubleshooting steps above

### Step 3: Build Project
1. **Build** → **Make Project**
2. Or press `Ctrl + F9`
3. Check **Build** tab for any errors

---

## 🎉 **Success Indicators**

### When It's Working
- ✅ Gradle sync completes without errors
- ✅ Project builds successfully
- ✅ No red underlines in code
- ✅ Run button is enabled
- ✅ Logcat shows device connection

### Common Success Messages
```
Gradle sync finished
Build completed successfully
APK(s) generated successfully
```

---

## 🆘 **Still Having Issues?**

### Advanced Troubleshooting
1. **Check Android Studio Logs**:
   - **Help** → **Show Log in Explorer**
   - Look for Gradle-related errors

2. **Reset Android Studio**:
   - **File** → **Invalidate Caches and Restart**
   - Choose **Invalidate and Restart**

3. **Reinstall Android Studio**:
   - Backup your projects
   - Download fresh copy from [developer.android.com](https://developer.android.com/studio)
   - Install with default settings

### Get Help
- [Android Studio Documentation](https://developer.android.com/studio)
- [Gradle Documentation](https://gradle.org/docs/)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android-studio)

---

## 🏆 **Summary**

### What We Fixed
1. ✅ Gradle environment variables
2. ✅ Java JDK setup
3. ✅ Android SDK configuration
4. ✅ Project-level Gradle settings
5. ✅ Alternative Gradle Wrapper approach

### Next Steps
1. Restart Android Studio
2. Import your WordDuel project
3. Let Gradle sync complete
4. Build and run your app!

**Your Android development journey starts now! 🚀📱**
