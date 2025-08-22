@echo off
echo ========================================
echo Android Studio Gradle Fix Script
echo ========================================
echo.

echo This script will fix common Gradle environment variable issues.
echo Please run as Administrator for best results.
echo.

echo Setting environment variables...
echo.

REM Set GRADLE_HOME
echo Setting GRADLE_HOME...
setx GRADLE_HOME "C:\Gradle\gradle-8.2/M
if %errorlevel% equ 0 (
    echo ✅ GRADLE_HOME set successfully
) else (
    echo ❌ Failed to set GRADLE_HOME. Try running as Administrator.
)

echo.

REM Set JAVA_HOME (common location)
echo Setting JAVA_HOME...
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M
if %errorlevel% equ 0 (
    echo ✅ JAVA_HOME set successfully
) else (
    echo ⚠️  JAVA_HOME may already be set or JDK not found at default location
)

echo.

REM Add to PATH
echo Adding Gradle and Java to PATH...
setx PATH "%PATH%;%GRADLE_HOME%\bin;%JAVA_HOME%\bin" /M
if %errorlevel% equ 0 (
    echo ✅ PATH updated successfully
) else (
    echo ❌ Failed to update PATH. Try running as Administrator.
)

echo.
echo ========================================
echo Environment Variables Set!
echo ========================================
echo.
echo Next steps:
echo 1. Close all Command Prompt windows
echo 2. Restart Android Studio
echo 3. Try importing your WordDuel project
echo.
echo If you still have issues, check the Android_Studio_Gradle_Fix.md file
echo for detailed troubleshooting steps.
echo.
pause
