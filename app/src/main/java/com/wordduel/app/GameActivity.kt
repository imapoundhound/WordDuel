package com.wordduel.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity // Added for TextView Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wordduel.app.databinding.ActivityGameBinding // Ensure this matches your package
// import java.util.Locale // Not strictly needed if using .uppercase()
import kotlin.random.Random

// Enum for feedback - Placed outside the class for broader accessibility if needed, or can be a nested class.
enum class CharFeedback {
    CORRECT_POSITION, // Green
    WRONG_POSITION,   // Yellow
    NOT_IN_WORD       // Gray
}

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameBoard: Array<Array<TextView>>
    private lateinit var keyboardButtons: MutableMap<Char, MaterialButton>

    private var currentWord = ""
    private var currentGuess = ""
    private var attempts = 0
    private var gameWon = false
    private var gameOver = false

    private val MAX_ATTEMPTS = 6
    private val WORD_LENGTH = 5 // Consistent word length

    private lateinit var words: List<String> // This will hold the chosen word list
    private val usedWords = mutableSetOf<String>() // To track already guessed words in the current game

    // Your extensive word list (as previously defined)
    // For brevity in this response, I'm assuming defaultWordList is defined here
    // e.g., private val defaultWordList = listOf("APPLE", "TABLE", ...)
    private val defaultWordList = listOf(
        "ABACK", "ABASE", "ABATE", "ABBEY", // ... (include your full list here, ensure they are uppercase)
        // Add all your words here, ensuring they are uppercase
        "HELLO", "EAGLE", "FLAME", "SPARK", "SNARK", "START", "CRATE", "BINGO", "PLAID", "FREAK", "LEERY", "BERRY"
    ).map { it.uppercase() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        words = loadWords() // Load/prepare the word list

        setupUI()          // Creates game board and keyboard visuals
        initializeGame()   // Sets up initial game state (picks word, resets attempts etc.)
        setupClickListeners() // Attaches listeners to buttons
    }

    private fun loadWords(): List<String> {
        // For now, using the hardcoded list.
        // Later, you could load from assets:
        // return assets.open("words.txt").bufferedReader().readLines().map { it.uppercase() }
        if (defaultWordList.isEmpty()) {
            // Fallback if the list is somehow empty, though it's hardcoded here
            return listOf("ERROR", "WORDS", "EMPTY")
        }
        return defaultWordList.filter { it.length == WORD_LENGTH } // Ensure all words are of correct length
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP) // Retained from your original code
    private fun setupUI() {
        supportActionBar?.hide() // Retained from your original code
        // Consider theming for status bar color if using modern themes, or keep explicit:
        // window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent) // Example

        createGameBoard()
        createKeyboard()
    }

    private fun createGameBoard() {
        gameBoard = Array(MAX_ATTEMPTS) { row ->
            Array(WORD_LENGTH) { col ->
                TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0 // Use weights for distribution
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4)) // Use dp for margins
                    }
                    setTextColor(ContextCompat.getColor(context, R.color.white)) // Ensure R.color.white exists
                    textSize = 24f
                    gravity = Gravity.CENTER
                    //setBackgroundResource(R.drawable.ic_launcher) // Initial placeholder, will be game_cell_default
                    // Set a default background for empty cells
                    setBackgroundResource(R.drawable.game_cell_default) // **ACTION: Create this drawable**
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

        keyRows.forEach { rowString ->
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    val marginVertical = dpToPx(2) // Smaller margin for keyboard rows
                    setMargins(0, marginVertical, 0, marginVertical)
                }
            }

            rowString.forEach { char ->
                val keyButton = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                    text = char.toString()
                    // More dynamic layout params for keys
                    val keyLayoutParams = LinearLayout.LayoutParams(
                        0, // Use 0 for width with weight
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f // Assign equal weight to each key in a row
                    ).apply {
                        val marginHorizontal = dpToPx(2)
                        setMargins(marginHorizontal, 0, marginHorizontal, 0)
                    }
                    layoutParams = keyLayoutParams

                    // Ensure R.color.keyboard_key_background and R.color.white exist
                    setBackgroundColor(ContextCompat.getColor(context, R.color.keyboard_key_background))
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 16f
                    // minWidth = 0 // Not needed with weighted layout
                    // minHeight = 0
                    setPadding(0, dpToPx(12), 0, dpToPx(12)) // Adjust padding as needed
                    // cornerRadius = dpToPx(4).toFloat() // MaterialButton uses shapeAppearance for corners

                    setOnClickListener { onKeyPress(char) }
                }
                keyboardButtons[char] = keyButton
                rowLayout.addView(keyButton)
            }
            binding.keyboardContainer.addView(rowLayout)
        }
    }


    private fun initializeGame() {
        if (words.isEmpty()) {
            showMessage("Error: Word list is empty. Cannot start game.", Snackbar.LENGTH_INDEFINITE)
            gameOver = true // Prevent further interaction
            return
        }

        currentWord = words.random().uppercase() // Ensure currentWord is also uppercase
        Log.d("GameActivity", "Current word: $currentWord") // For debugging
        currentGuess = ""
        attempts = 0
        gameWon = false
        gameOver = false
        usedWords.clear()

        clearGameBoardDisplay()
        resetKeyboardColors()
        updateGuessCounter()

        binding.btnPlayAgain.visibility = View.GONE

        // showMessage("New game started!", Snackbar.LENGTH_SHORT) // Optional: message to user
    }

    private fun setupClickListeners() {
        // Keyboard keys are set up in createKeyboard
        binding.btnPlayAgain.setOnClickListener { startNewGame() }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun onKeyPress(char: Char) {
        if (gameOver || currentGuess.length >= WORD_LENGTH) return

        currentGuess += char.uppercaseChar() // Store guess in uppercase
        updateGameBoardDisplay() // Show the typed character
    }

    private fun onBackspace() {
        if (currentGuess.isNotEmpty() && !gameOver) {
            currentGuess = currentGuess.dropLast(1)
            updateGameBoardDisplay() // Update the display to remove the char
        }
    }

    private fun submitGuess() {
        if (gameOver) return
        if (currentGuess.length != WORD_LENGTH) {
            showMessage("Word must be $WORD_LENGTH letters long", Snackbar.LENGTH_SHORT)
            return
        }

        // Word validation (check against the loaded 'words' list)
        if (!words.contains(currentGuess)) { // currentGuess is already uppercase
            showMessage("Not in word list", Snackbar.LENGTH_SHORT)
            // currentGuess = "" // Optionally clear invalid guess
            // updateGameBoardDisplay()
            return
        }

        if (usedWords.contains(currentGuess)) {
            showMessage("Word already guessed", Snackbar.LENGTH_SHORT)
            return
        }
        usedWords.add(currentGuess)

        val feedback = generateFeedback(currentGuess, currentWord)
        applyFeedbackToBoard(attempts, currentGuess, feedback)

        updateKeyboardColors(currentGuess, feedback)

        if (currentGuess == currentWord) {
            gameWon = true
            gameOver = true
            showMessage("Congratulations! You guessed it: $currentWord", Snackbar.LENGTH_INDEFINITE)
            binding.btnPlayAgain.visibility = View.VISIBLE
        } else {
            attempts++
            if (attempts >= MAX_ATTEMPTS) {
                gameOver = true
                showMessage("Game Over. The word was: $currentWord", Snackbar.LENGTH_INDEFINITE)
                binding.btnPlayAgain.visibility = View.VISIBLE
            } else {
                // showMessage("Try again!", Snackbar.LENGTH_SHORT) // Optional feedback
            }
        }
        currentGuess = "" // Clear for next guess, AFTER processing current one
        // updateGameBoardDisplay() // Call this if you want the next input row to immediately clear (but it's handled by next key press)
        updateGuessCounter()
    }


    private fun generateFeedback(guess: String, actualWord: String): List<CharFeedback> {
        val feedback = MutableList(WORD_LENGTH) { CharFeedback.NOT_IN_WORD }
        val actualWordCharCounts = actualWord.groupingBy { it }.eachCount().toMutableMap()

        // First pass: Correct position (Green)
        for (i in guess.indices) {
            if (guess[i] == actualWord[i]) {
                feedback[i] = CharFeedback.CORRECT_POSITION
                actualWordCharCounts[actualWord[i]] = (actualWordCharCounts[actualWord[i]] ?: 0) - 1
            }
        }

        // Second pass: Wrong position (Yellow)
        for (i in guess.indices) {
            if (feedback[i] == CharFeedback.NOT_IN_WORD) { // Only check if not already green
                if (actualWord.contains(guess[i]) && (actualWordCharCounts[guess[i]] ?: 0) > 0) {
                    feedback[i] = CharFeedback.WRONG_POSITION
                    actualWordCharCounts[guess[i]] = (actualWordCharCounts[guess[i]] ?: 0) - 1
                }
            }
        }
        return feedback
    }

    private fun applyFeedbackToBoard(attemptRow: Int, guess: String, feedback: List<CharFeedback>) {
        if (attemptRow >= MAX_ATTEMPTS) return

        for (i in guess.indices) {
            val cell = gameBoard[attemptRow][i]
            cell.text = guess[i].toString() // Already uppercase
            when (feedback[i]) {
                CharFeedback.CORRECT_POSITION -> cell.setBackgroundResource(R.drawable.cell_correct_position)
                CharFeedback.WRONG_POSITION -> cell.setBackgroundResource(R.drawable.game_cell_present)
                CharFeedback.NOT_IN_WORD -> cell.setBackgroundResource(R.drawable.game_cell_absent)
            }
        }
    }

    private fun updateKeyboardColors(guess: String, feedback: List<CharFeedback>) {
        for (i in guess.indices) {
            val char = guess[i] // Already uppercase
            val button = keyboardButtons[char]

            button?.let {
                // Determine the best color: Green > Yellow > Gray (for already NOT_IN_WORD)
                // This logic needs to be careful not to downgrade a green key if a letter appears again and is gray
                val currentKeyFeedback = getKeyFeedbackState(it) // You'd need a helper to get this if not just using background color
                val newCharFeedback = feedback[i]

                // Only upgrade the key color, or set it if it's default
                if (newCharFeedback == CharFeedback.CORRECT_POSITION) {
                    it.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                } else if (newCharFeedback == CharFeedback.WRONG_POSITION && currentKeyFeedback != CharFeedback.CORRECT_POSITION) {
                    it.setBackgroundColor(ContextCompat.getColor(this, R.color.present))
                } else if (newCharFeedback == CharFeedback.NOT_IN_WORD && currentKeyFeedback == null) {
                    it.setBackgroundColor(ContextCompat.getColor(this, R.color.absent))
                }
                }
            }
        }
    }
    // Helper to get current feedback state of a key (simplified)
    // You might need a more robust way, e.g., by tagging buttons or checking background color if set uniquely.
    private fun getKeyFeedbackState(button: MaterialButton): CharFeedback? {
        // This is a simplified example. Comparing drawable resources directly can be tricky.
        // It's better to manage state via tags or a separate map if this becomes complex.
        // For now, this is a placeholder idea.
        return null // Placeholder: Assume default unless explicitly set
    }


    private fun clearGameBoardDisplay() {
        for (rowIndex in gameBoard.indices) {
            for (colIndex in gameBoard[rowIndex].indices) {
                val cell = gameBoard[rowIndex][colIndex]
                cell.text = ""
                cell.setBackgroundResource(R.drawable.game_cell_default) // **ACTION: Create this drawable**
            }
        }
    }

    private fun resetKeyboardColors() {
        keyboardButtons.forEach { (_, button) ->
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.keyboard_key_background))
            // button.isEnabled = true // Ensure keys are re-enabled
        }
    }

    private fun updateGuessCounter() {
        // Update the header guess counter TextView
        binding.guessCounter.text = "Guess ${attempts + 1}/$MAX_ATTEMPTS"
    }

    private fun updateGameBoardDisplay() {
        // Display current guess in the active row
        val currentRow = attempts
        if (currentRow < MAX_ATTEMPTS) {
            for (i in 0 until WORD_LENGTH) {
                val cell = gameBoard[currentRow][i]
                if (i < currentGuess.length) {
                    cell.text = currentGuess[i].toString() // currentGuess is already uppercase
                } else {
                    cell.text = ""
                }
                // Only reset background for current input row if it was previously colored by feedback (unlikely here)
                // cell.setBackgroundResource(R.drawable.game_cell_default)
            }
        }
    }

    private fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(binding.root, message, duration).show()
    }

    private fun startNewGame() {
        initializeGame()
    }

    // Helper for dp to pixel conversion
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
