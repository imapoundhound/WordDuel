@echo off
echo ========================================
echo WordDuel Android Build - Simple Version
echo ========================================
echo.

echo 🚀 Building WordDuel Android App...
echo.

echo Step 1: Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java not found! Please install JDK 17 or later
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
)

echo.
echo Step 2: Checking if Gradle is available...
gradle --version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Gradle found, using system Gradle
    echo.
    echo Step 3: Building with system Gradle...
    gradle build
) else (
    echo ⚠️  Gradle not found in PATH
    echo.
    echo Step 3: Trying to download and use Gradle...
    echo.
    echo Option A: Install Gradle manually
    echo 1. Download from: https://gradle.org/releases/
    echo 2. Extract to C:\Gradle\gradle-8.5
    echo 3. Add C:\Gradle\gradle-8.5\bin to PATH
    echo.
    echo Option B: Use Android Studio
    echo 1. Open project in Android Studio
    echo 2. Build → Build Bundle(s) / APK(s) → Build APK(s)
    echo.
    echo Option C: Use command line with full path
    echo If you have Gradle installed elsewhere, provide the full path
    echo.
    set /p gradlepath="Enter full path to gradle.bat (or press Enter to skip): "
    if not "%gradlepath%"=="" (
        echo Building with: %gradlepath%
        "%gradlepath%" build
    ) else (
        echo Skipping build, please use one of the options above
    )
)

echo.
if %errorlevel% equ 0 (
    echo.
    echo ✅ Build successful!
    echo.
    echo Your APK should be in: app/build/outputs/apk/debug/
    echo.
    echo Next steps:
    echo 1. Install APK on your device
    echo 2. Test the app functionality
    echo 3. Prepare for Google Play Store
) else (
    echo.
    echo ❌ Build failed or skipped
    echo.
    echo Please use one of the build options mentioned above
)

echo.
pause
