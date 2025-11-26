# GitHub Pages Deployment — WordDuel

This document covers deploying the static WordDuel front-end to GitHub Pages and notes on Firebase authorized domains.

Option A — Simple: Serve from `main` branch root
1. Create a GitHub repository (if not already created) and push your `main` branch.
2. In the repository Settings → Pages, set Source to `main` branch and folder `/ (root)` and Save.
3. GitHub will publish the site to `https://<your-username>.github.io/<repo-name>/` (may take a minute).

PowerShell commands to push (example):
```powershell
# from repo root
git status
git add -A
git commit -m "Prepare for GitHub Pages deployment"
git push origin main
```

Option B — Use `gh-pages` branch (recommended for manual control)
1. Install the `gh-pages` npm tool locally if you want automated pushes (optional):
```powershell
npm install --global gh-pages
```
2. Publish the current build (here we have static files at repo root — WordDuel.html):
```powershell
# If you want to publish repo root as the site
git checkout -b gh-pages
# Optionally remove non-public files from this branch
git push -u origin gh-pages
```
3. In Settings → Pages select `gh-pages` branch as the source.

Notes about the app and filenames
- The app is `WordDuel.html`. For best compatibility with Pages, you can rename/copy it to `index.html` in the repo root or use `index.html` that imports `WordDuel.html` via an iframe. Recommended: create a simple `index.html` that redirects to `WordDuel.html` or rename.

Example: make `index.html` point to `WordDuel.html` (quick and dirty):
```html
<!doctype html>
<html>
  <head>
    <meta http-equiv="refresh" content="0; url=WordDuel.html">
  </head>
  <body>
    <p>Redirecting to <a href="WordDuel.html">WordDuel</a>...</p>
  </body>
</html>
```

Firebase authorized domains
- After you deploy, add the published origin(s) to Firebase console under Authentication → Settings → Authorized domains. Example:
  - `your-username.github.io`
  - `localhost` (for local testing)

CORS / CDN notes
- The adapter uses CDN imports from `https://www.gstatic.com/firebasejs/...` which are allowed from GitHub Pages.

Troubleshooting
- If the page shows console warnings about missing `window.firebaseConfig`, ensure your config snippet is included in the deployed HTML before the main app script runs.
- If you see auth redirect errors, ensure the authorized domain is added in Firebase Authentication settings.

Optional: automatic publishing via GitHub Actions
- If you want CI to auto-deploy to Pages, I can scaffold a GitHub Action workflow that builds (if needed) and pushes to `gh-pages` branch. Tell me if you'd like that.
