package com.wordduel.app

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wordduel.app.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameBoard: Array<Array<TextView>>
    private lateinit var keyboardButtons: MutableMap<Char, MaterialButton>
    
    private var currentWord = ""
    private var currentGuess = ""
    private var attempts = 0
    private var gameWon = false
    private var gameOver = false
    private var usedWords = mutableSetOf<String>()
    
    // Word list (will be expanded)
    private val words = listOf(
        "hello", "world", "apple", "beach", "chair", "dance", "eagle", "flame", 
        "grape", "house", "image", "juice", "knife", "lemon", "music", "night",
        "ocean", "peace", "queen", "river", "smile", "table", "unity", "voice",
        "water", "youth", "zebra", "brain", "cloud", "dream", "earth", "faith"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        initializeGame()
        setupClickListeners()
    }
    
    private fun setupUI() {
        supportActionBar?.hide()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        createGameBoard()
        createKeyboard()
    }
    
    private fun createGameBoard() {
        gameBoard = Array(6) { row ->
            Array(5) { col ->
                TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(4, 4, 4, 4)
                    }
                    
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 24f
                    gravity = android.view.Gravity.CENTER
                    setBackgroundResource(R.drawable.game_cell_background)
                    
                    binding.gameBoard.addView(this)
                }
            }
        }
    }
    
    private fun createKeyboard() {
        keyboardButtons = mutableMapOf()
        
        val keyRows = listOf(
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        )
        
        keyRows.forEachIndexed { rowIndex, row ->
            val rowLayout = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }
            
            row.forEach { char ->
                val keyButton = MaterialButton(this).apply {
                    text = char.toString()
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(4, 0, 4, 0)
                    }
                    
                    setOnClickListener { onKeyPress(char) }
                    setBackgroundColor(ContextCompat.getColor(context, R.color.keyboard_key_background))
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 16f
                    minWidth = 0
                    minHeight = 0
                    padding = 0
                    cornerRadius = 8
                }
                
                keyboardButtons[char] = keyButton
                rowLayout.addView(keyButton)
            }
            
            binding.keyboardContainer.addView(rowLayout)
        }
        
        // Add special keys
        val specialRow = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }
        
        // Enter key
        val enterButton = MaterialButton(this).apply {
            text = "ENTER"
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(4, 0, 4, 0)
            }
            setOnClickListener { submitGuess() }
            setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 14f
            minWidth = 0
            minHeight = 0
            padding = 0
            cornerRadius = 8
        }
        
        // Backspace key
        val backspaceButton = MaterialButton(this).apply {
            text = "⌫"
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(4, 0, 4, 0)
            }
            setOnClickListener { onBackspace() }
            setBackgroundColor(ContextCompat.getColor(context, R.color.secondary))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 18f
            minWidth = 0
            minHeight = 0
            padding = 0
            cornerRadius = 8
        }
        
        specialRow.addView(enterButton)
        specialRow.addView(backspaceButton)
        binding.keyboardContainer.addView(specialRow)
    }
    
    private fun initializeGame() {
        currentWord = words.random()
        currentGuess = ""
        attempts = 0
        gameWon = false
        gameOver = false
        usedWords.clear()
        
        updateGuessCounter()
        showMessage("Computer has chosen a word. Good luck!", Snackbar.LENGTH_LONG)
    }
    
    private fun setupClickListeners() {
        binding.btnPlayAgain.setOnClickListener {
            startNewGame()
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun onKeyPress(char: Char) {
        if (gameOver || currentGuess.length >= 5) return
        
        currentGuess += char.lowercase()
        updateGameBoardDisplay()
        
        // Add key press animation
        val scaleAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        scaleAnimation.duration = 100
        keyboardButtons[char]?.startAnimation(scaleAnimation)
    }
    
    private fun onBackspace() {
        if (currentGuess.isNotEmpty()) {
            currentGuess = currentGuess.dropLast(1)
            updateGameBoardDisplay()
        }
    }
    
    private fun updateGameBoardDisplay() {
        for (i in 0 until 5) {
            val cell = gameBoard[attempts][i]
            if (i < currentGuess.length) {
                cell.text = currentGuess[i].uppercase()
                cell.setBackgroundResource(R.drawable.game_cell_filled)
            } else {
                cell.text = ""
                cell.setBackgroundResource(R.drawable.game_cell_background)
            }
        }
    }
    
    private fun submitGuess() {
        if (currentGuess.length != 5) {
            showMessage("Word must be 5 letters long", Snackbar.LENGTH_SHORT)
            return
        }
        
        if (usedWords.contains(currentGuess)) {
            showMessage("You already guessed that word!", Snackbar.LENGTH_SHORT)
            return
        }
        
        if (!words.contains(currentGuess)) {
            showMessage("Not a valid word", Snackbar.LENGTH_SHORT)
            return
        }
        
        usedWords.add(currentGuess)
        
        // Generate feedback and apply to board
        val feedback = generateFeedback(currentGuess, currentWord)
        applyFeedbackToBoard(attempts, currentGuess, feedback)
        
        // Update keyboard colors
        updateKeyboardColors(currentGuess, feedback)
        
        if (currentGuess == currentWord) {
            gameWon = true
            gameOver = true
            showMessage("Congratulations! You guessed the word!", Snackbar.LENGTH_LONG)
            binding.btnPlayAgain.visibility = View.VISIBLE
        } else {
            attempts++
            updateGuessCounter()
            
            if (attempts >= 6) {
                gameOver = true
                showMessage("Game over! The word was: ${currentWord.uppercase()}", Snackbar.LENGTH_LONG)
                binding.btnPlayAgain.visibility = View.VISIBLE
            } else {
                currentGuess = ""
                showMessage("${6 - attempts} attempts remaining", Snackbar.LENGTH_SHORT)
            }
        }
    }
    
    private fun generateFeedback(guess: String, targetWord: String): Array<String> {
        val feedback = Array(5) { "absent" }
        val targetLetters = targetWord.toMutableList()
        val guessLetters = guess.toList()
        
        // First pass: mark correct letters
        for (i in 0 until 5) {
            if (guessLetters[i] == targetLetters[i]) {
                feedback[i] = "correct"
                targetLetters[i] = ' ' // Mark as used
            }
        }
        
        // Second pass: mark present letters
        for (i in 0 until 5) {
            if (feedback[i] == "correct") continue
            
            val letterIndex = targetLetters.indexOf(guessLetters[i])
            if (letterIndex != -1) {
                feedback[i] = "present"
                targetLetters[letterIndex] = ' ' // Mark as used
            }
        }
        
        return feedback
    }
    
    private fun applyFeedbackToBoard(rowIndex: Int, guess: String, feedback: Array<String>) {
        for (i in 0 until 5) {
            val cell = gameBoard[rowIndex][i]
            cell.text = guess[i].uppercase()
            
            val backgroundRes = when (feedback[i]) {
                "correct" -> R.drawable.game_cell_correct
                "present" -> R.drawable.game_cell_present
                else -> R.drawable.game_cell_absent
            }
            
            cell.setBackgroundResource(backgroundRes)
            
            // Add feedback animation
            val pulseAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            pulseAnimation.duration = 300
            cell.startAnimation(pulseAnimation)
        }
    }
    
    private fun updateKeyboardColors(guess: String, feedback: Array<String>) {
        for (i in 0 until 5) {
            val char = guess[i].uppercaseChar()
            val button = keyboardButtons[char] ?: continue
            
            val colorRes = when (feedback[i]) {
                "correct" -> R.color.correct
                "present" -> R.color.present
                else -> R.color.absent
            }
            
            button.setBackgroundColor(ContextCompat.getColor(this, colorRes))
        }
    }
    
    private fun updateGuessCounter() {
        binding.guessCounter.text = "Guess ${attempts + 1}/6"
    }
    
    private fun showMessage(message: String, duration: Int) {
        Snackbar.make(binding.root, message, duration).show()
    }
    
    private fun startNewGame() {
        binding.btnPlayAgain.visibility = View.GONE
        
        // Clear game board
        for (row in gameBoard) {
            for (cell in row) {
                cell.text = ""
                cell.setBackgroundResource(R.drawable.game_cell_background)
            }
        }
        
        // Reset keyboard colors
        keyboardButtons.values.forEach { button ->
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.keyboard_key_background))
        }
        
        initializeGame()
    }
}

