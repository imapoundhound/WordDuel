package com.wordduel.app

import android.view.View
import androidx.glance.visibility
import kotlin.text.isEmpty
import kotlin.text.random
import kotlin.text.uppercase

// Inside GameActivity class
private fun initializeGame() {
    // ... (implementation as previously discussed)
    // Example:
    if(words.isEmpty()) {
        // Handle empty word list
        return
    }
    currentWord = words.random().uppercase()
    currentGuess = ""
    attempts = 0
    gameWon = false
    gameOver = false
    // etc.
    clearGameBoardDisplay()
    resetKeyboardColors()
    updateGuessCounter()
    binding.btnPlayAgain.visibility = View.GONE
}