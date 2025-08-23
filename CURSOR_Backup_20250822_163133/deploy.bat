@echo off
echo ========================================
echo WordDuel Deployment Script
echo ========================================
echo.

echo This script will help you deploy WordDuel to GitHub Pages
echo.

echo Step 1: Initialize Git repository (if not already done)
if not exist ".git" (
    echo Initializing Git repository...
    git init
    echo ✅ Git repository initialized
) else (
    echo ✅ Git repository already exists
)

echo.
echo Step 2: Add all files to Git
git add .
echo ✅ Files added to Git

echo.
echo Step 3: Commit changes
git commit -m "WordDuel v1.0 - Complete game with authentication and comments"
echo ✅ Changes committed

echo.
echo Step 4: Set up remote origin (you'll need to provide your GitHub username)
set /p username="Enter your GitHub username: "
set /p reponame="Enter repository name (default: wordduel): "

if "%reponame%"=="" set reponame=wordduel

echo.
echo Setting remote origin to: https://github.com/%username%/%reponame%.git
git remote add origin https://github.com/%username%/%reponame%.git
echo ✅ Remote origin set

echo.
echo Step 5: Push to GitHub
git branch -M main
git push -u origin main
echo ✅ Code pushed to GitHub

echo.
echo ========================================
echo 🎉 Deployment Complete!
echo ========================================
echo.
echo Next steps:
echo 1. Go to https://github.com/%username%/%reponame%
echo 2. Go to Settings ^> Pages
echo 3. Select "Deploy from a branch"
echo 4. Choose "main" branch and "/ (root)" folder
echo 5. Click "Save"
echo.
echo Your game will be live at:
echo https://%username%.github.io/%reponame%/
echo.
echo Test the comment system by:
echo 1. Login with: test@test.com / test
echo 2. Play a game
echo 3. Leave a comment about your experience
echo.
pause
