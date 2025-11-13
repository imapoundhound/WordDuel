@echo off
echo ========================================
echo Complete Gradle Fix Script
echo ========================================
echo.
echo This script will fix:
echo 1. Missing Gradle init script
echo 2. Create necessary directories
echo.

set INIT_SCRIPT_DIR=C:\Users\richt\AppData\Roaming\Cursor\User\globalStorage\redhat.java\1.46.0\config_win\org.eclipse.osgi\58\0\.cp\gradle\init
set INIT_SCRIPT_FILE=%INIT_SCRIPT_DIR%\init.gradle

echo Step 1: Creating directory structure...
if not exist "%INIT_SCRIPT_DIR%" (
    mkdir "%INIT_SCRIPT_DIR%" 2>nul
    if %errorlevel% equ 0 (
        echo ✅ Directory created
    ) else (
        echo ❌ Failed to create directory
        echo Please run as Administrator
        pause
        exit /b 1
    )
) else (
    echo ✅ Directory already exists
)

echo.
echo Step 2: Creating init.gradle file...
(
echo // Gradle initialization script
echo // Created automatically to fix missing init script error
echo // This file is used by the Red Hat Java extension in Cursor
) > "%INIT_SCRIPT_FILE%"

if exist "%INIT_SCRIPT_FILE%" (
    echo ✅ init.gradle file created successfully
    echo    Location: %INIT_SCRIPT_FILE%
) else (
    echo ❌ Failed to create init.gradle file
    echo Please check permissions
    pause
    exit /b 1
)

echo.
echo ========================================
echo Fix Complete!
echo ========================================
echo.
echo IMPORTANT: You must restart Cursor/IDE for changes to take effect!
echo.
echo Next steps:
echo 1. Close Cursor completely
echo 2. Reopen Cursor
echo 3. Sync Gradle project (File ^> Sync Project with Gradle Files)
echo.
echo The deprecated Gradle features warning is normal and won't break your build.
echo It just means some features will need updating for Gradle 9.0 compatibility.
echo.
pause

