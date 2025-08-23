# 🚀 WordDuel - Free Online Word Guessing Game

A modern, feature-rich word guessing game with authentication, local network play, and social features.

## 🌐 **Live Demo**
**Play Now**: [WordDuel Game](https://yourusername.github.io/wordduel/WordDuel.html)

## ✨ **Features**

### 🎮 **Game Modes**
- **One Player vs Bot**: Challenge the computer
- **Two Player Local**: Play with friends on the same device
- **Two Player Network**: Connect with friends via email, username, IP, or Bluetooth

### 🔐 **Authentication System**
- User registration and login
- Email and phone validation
- MFA support (Multi-Factor Authentication)
- Social login (Google, Apple)

### 🌐 **Network Features**
- **Local Network Discovery**: Find players by username on LAN
- **IP Connection**: Direct connection via IP address
- **Bluetooth Discovery**: Find nearby players (up to 4 players)
- **Email Invites**: Send game invitations to friends

### 💬 **Comment System**
- Share your gaming experience
- Rate and review the game
- View feedback from other players
- Track game statistics with comments

### 📊 **Statistics & Analytics**
- Game performance tracking
- Win/loss ratios
- Attempt counts
- Time-based statistics (daily, weekly, monthly)

## 🚀 **Free Hosting Setup**

### **Option 1: GitHub Pages (Recommended - 100% Free)**

1. **Create GitHub Repository**:
   ```bash
   git init
   git add .
   git commit -m "Initial WordDuel release"
   git branch -M main
   git remote add origin https://github.com/yourusername/wordduel.git
   git push -u origin main
   ```

2. **Enable GitHub Pages**:
   - Go to repository Settings
   - Scroll to "Pages" section
   - Select "Deploy from a branch"
   - Choose "main" branch and "/ (root)" folder
   - Click "Save"

3. **Your game will be live at**:
   ```
   https://yourusername.github.io/wordduel/
   ```

### **Option 2: Netlify (Free Tier)**

1. **Drag & Drop**:
   - Go to [netlify.com](https://netlify.com)
   - Drag your project folder to deploy
   - Get instant free hosting

2. **Custom Domain** (Optional):
   - Add your own domain name
   - Free SSL certificate included

### **Option 3: Vercel (Free Tier)**

1. **Connect GitHub**:
   - Go to [vercel.com](https://vercel.com)
   - Connect your GitHub repository
   - Automatic deployments on every push

## 🛠️ **Local Development**

### **Prerequisites**
- Modern web browser (Chrome, Firefox, Safari, Edge)
- No server required - runs entirely in browser

### **Quick Start**
1. Clone the repository
2. Open `index.html` in your browser
3. Or use a local server:
   ```bash
   # Python 3
   python -m http.server 8000
   
   # Node.js
   npx serve .
   
   # PHP
   php -S localhost:8000
   ```

## 📱 **Demo Credentials**

For testing purposes:
- **Email**: `test@test.com`
- **Password**: `test`

## 🔧 **Technical Details**

### **Frontend Technologies**
- **HTML5**: Semantic markup and modern features
- **CSS3**: Advanced styling with animations and gradients
- **JavaScript ES6+**: Modern JavaScript with async/await
- **Local Storage**: Persistent game data and comments

### **Game Logic**
- Word validation against expanded dictionary
- Intelligent feedback system (correct, present, absent)
- Multi-player game state management
- Real-time statistics tracking

### **Network Features**
- WebRTC for peer-to-peer connections
- Local network discovery via Web APIs
- Bluetooth device scanning (when available)
- Email-based invitation system

## 📊 **Comment System Features**

### **What Gets Captured**
- **User**: Anonymous player identification
- **Timestamp**: When the comment was made
- **Game Mode**: Bot vs Two Player
- **Performance**: Number of attempts
- **Feedback**: User's experience and suggestions

### **Data Storage**
- Comments stored locally in browser
- No external database required
- Privacy-focused approach
- Export functionality available

## 🌟 **Customization Options**

### **Themes**
- Multiple color schemes available
- Dark/Light mode toggle
- Customizable game board colors

### **Game Settings**
- Adjustable word length
- Custom game rules
- Difficulty levels
- Time limits (optional)

## 🔒 **Privacy & Security**

- **No Personal Data**: Only anonymous feedback collected
- **Local Storage**: All data stays on user's device
- **No Tracking**: No analytics or user behavior tracking
- **Open Source**: Transparent code for security review

## 🤝 **Contributing**

We welcome contributions! Here's how to help:

1. **Fork the repository**
2. **Create a feature branch**
3. **Make your changes**
4. **Test thoroughly**
5. **Submit a pull request**

### **Areas for Improvement**
- Additional game modes
- Enhanced network features
- More language support
- Mobile app development
- Advanced statistics

## 📄 **License**

This project is open source and available under the [MIT License](LICENSE).

## 🙏 **Acknowledgments**

- Inspired by Wordle and similar word games
- Built with modern web technologies
- Community-driven development
- Free and accessible for everyone

## 📞 **Support & Feedback**

- **Issues**: Report bugs on GitHub
- **Feature Requests**: Use GitHub discussions
- **Comments**: Share your experience in-game
- **Email**: Contact via repository

---

**🎉 Ready to play? Deploy to GitHub Pages and start your WordDuel adventure!**

*Built with ❤️ for the gaming community*
