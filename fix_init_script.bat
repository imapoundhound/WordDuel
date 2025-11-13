@echo off
echo ========================================
echo Fix Gradle Init Script Error
echo ========================================
echo.
echo This script will create the missing Gradle init script
echo that the Red Hat Java extension is trying to use.
echo.

set INIT_SCRIPT_PATH=C:\Users\richt\AppData\Roaming\Cursor\User\globalStorage\redhat.java\1.46.0\config_win\org.eclipse.osgi\58\0\.cp\gradle\init\init.gradle

echo Creating directory structure...
mkdir "%INIT_SCRIPT_PATH%\.." 2>nul
if %errorlevel% neq 0 (
    echo Creating parent directories...
    mkdir "C:\Users\richt\AppData\Roaming\Cursor\User\globalStorage\redhat.java\1.46.0\config_win\org.eclipse.osgi\58\0\.cp\gradle\init" 2>nul
)

echo Creating init.gradle file...
(
echo // Gradle initialization script
echo // Created automatically to fix missing init script error
echo // This file is used by the Red Hat Java extension in Cursor
echo.
) > "%INIT_SCRIPT_PATH%"

if exist "%INIT_SCRIPT_PATH%" (
    echo ✅ Successfully created init.gradle at:
    echo    %INIT_SCRIPT_PATH%
    echo.
    echo The Gradle build error should now be resolved.
) else (
    echo ❌ Failed to create init.gradle file.
    echo Please check that you have write permissions to:
    echo %INIT_SCRIPT_PATH%
    echo.
    echo Try running this script as Administrator.
)

echo.
echo ========================================
echo Fix Complete!
echo ========================================
echo.
echo Next steps:
echo 1. Close and reopen Cursor/Android Studio
echo 2. Sync your Gradle project
echo 3. The error should be resolved
echo.
pause

