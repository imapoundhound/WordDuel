package com.wordduel.app

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButton
import com.wordduel.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupClickListeners()
        startEntranceAnimation()
    }
    
    private fun setupUI() {
        // Hide action bar for full-screen experience
        supportActionBar?.hide()
        
        // Set status bar to transparent
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }
    
    private fun setupClickListeners() {
        // Bot mode card click
        binding.cardBotMode.setOnClickListener {
            animateCardClick(binding.cardBotMode) {
                startGame(GameMode.BOT)
            }
        }
        
        // Two player card click
        binding.cardTwoPlayer.setOnClickListener {
            animateCardClick(binding.cardTwoPlayer) {
                startGame(GameMode.TWO_PLAYER)
            }
        }
        
        // Stats button click
        binding.btnStats.setOnClickListener {
            animateButtonClick(binding.btnStats) {
                startStatsActivity()
            }
        }
    }
    
    private fun startEntranceAnimation() {
        // Fade in animation for title
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.tvAppTitle.startAnimation(fadeIn)
        
        // Slide up animation for subtitle
        val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        binding.tvSubtitle.startAnimation(slideUp)
        
        // Staggered animation for game mode cards
        val slideUpDelayed = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideUpDelayed.startOffset = 300
        binding.cardBotMode.startAnimation(slideUpDelayed)
        
        val slideUpDelayed2 = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideUpDelayed2.startOffset = 500
        binding.cardTwoPlayer.startAnimation(slideUpDelayed2)
        
        // Fade in animation for stats button
        val fadeInDelayed = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeInDelayed.startOffset = 700
        binding.btnStats.startAnimation(fadeInDelayed)
    }
    
    private fun animateCardClick(card: MaterialCardView, onComplete: () -> Unit) {
        val scaleDown = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        scaleDown.duration = 150
        
        card.startAnimation(scaleDown)
        
        // Execute callback after animation
        card.postDelayed({
            onComplete()
        }, 150)
    }
    
    private fun animateButtonClick(button: MaterialButton, onComplete: () -> Unit) {
        val scaleDown = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        scaleDown.duration = 100
        
        button.startAnimation(scaleDown)
        
        // Execute callback after animation
        button.postDelayed({
            onComplete()
        }, 100)
    }
    
    private fun startGame(gameMode: GameMode) {
        val intent = when (gameMode) {
            GameMode.BOT -> Intent(this, GameActivity::class.java)
            GameMode.TWO_PLAYER -> Intent(this, TwoPlayerActivity::class.java)
        }
        
        intent.putExtra("GAME_MODE", gameMode.name)
        startActivity(intent)
        
        // Add slide transition animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
    
    private fun startStatsActivity() {
        val intent = Intent(this, StatsActivity::class.java)
        startActivity(intent)
        
        // Add fade transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    override fun onResume() {
        super.onResume()
        // Reset any animations or states when returning to main screen
    }
    
    enum class GameMode {
        BOT,
        TWO_PLAYER
    }
}

