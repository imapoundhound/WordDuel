@echo off
echo ========================================
echo WordDuel Android Build Script
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
echo Step 2: Building Android project...
echo.

REM Try to build the project
gradlew.bat build

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
    echo ❌ Build failed! Check the error messages above.
    echo.
    echo Common solutions:
    echo 1. Make sure Android SDK is installed
    echo 2. Check that ANDROID_HOME is set
    echo 3. Verify Gradle wrapper is working
)

echo.
pause
