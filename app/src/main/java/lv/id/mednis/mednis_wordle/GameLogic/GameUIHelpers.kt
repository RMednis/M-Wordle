package lv.id.mednis.mednis_wordle.GameLogic

import GameSettings
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import lv.id.mednis.mednis_wordle.MainActivity
import lv.id.mednis.mednis_wordle.R
import lv.id.mednis.mednis_wordle.SettingsActivity
import lv.id.mednis.mednis_wordle.WinLoseActivity
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit


class GameUIHelpers {

    class GameOver {
        // Game Over Confetti
        private fun doConfetti(ConfettiView: KonfettiView) {

            // Make the confetti animation start from the top of the view
            ConfettiView.start(
                Party(
                    Angle.BOTTOM, 1000, 0F, 15F, 0.9f,
                    emitter = Emitter(duration = 15, TimeUnit.SECONDS).perSecond(100),
                    position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
                )
            )
            ConfettiView.bringToFront()
        }

        private fun gameOver(context: Context) {
            val TextInput = (context as MainActivity).findViewById<TextInputLayout>(R.id.WordInputLayout)

            // Hide the keyboard
            // WHY IS THIS SO HARD TO DO IN ANDROID?
            // I tried like 4 different ways to do this, and this is the only one that worked
            context.currentFocus?.let { view ->
                // Get the input manager
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                // Hide the keyboard
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }

            // Hide the input view
            TextInput.visibility = View.GONE

        }

        fun gameOverWon(context: Context, word: String, attempts: Int) {
            val ConfettiView = (context as MainActivity).findViewById<KonfettiView>(R.id.confettiView)
            gameOver(context)
            // Show the confetti
            doConfetti(ConfettiView)

            val gameOverIntent = Intent(context, WinLoseActivity::class.java)
            gameOverIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            gameOverIntent.putExtra("win", true)
            gameOverIntent.putExtra("word", word)
            gameOverIntent.putExtra("attempts", attempts)

            // Launch the game over activity after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(context, gameOverIntent, null)
                context.finish()
            }, 5000)
        }

        fun gameOverLost(context: Context, word: String, attempts: Int) {
            gameOver(context)
            val gameOverIntent = Intent(context, WinLoseActivity::class.java)
            gameOverIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

            gameOverIntent.putExtra("win", false)
            gameOverIntent.putExtra("word", word)
            gameOverIntent.putExtra("attempts", attempts)

            // Launch the game over activity
            startActivity(context, gameOverIntent, null)

            // End the activity
            (context as MainActivity).finish()
        }
    }

    fun showSettings(context: Context) {
        startActivity(context, Intent(context, SettingsActivity::class.java), null)
    }

    fun updateInputHint(context: Context) {
        // Rediģejam hint tekstu
        val WordInputLayout: TextInputLayout = (context as MainActivity).findViewById(R.id.WordInputLayout)
        val WordInput: TextInputEditText = context.findViewById(R.id.WordInput)
        val burti = 5 - (WordInput.text?.length ?: 0)
        WordInputLayout.hint = context.getString(R.string.text_entry_info).format(burti)
    }


    class WordList {

        private fun AddSpacer(context: Context, layout: LinearLayout) {
            val Spacer = Space(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.25f
                )
            }
            layout.addView(Spacer)
        }

        fun addWordToBoard(context: Context, word: String, colors: Array<String>, number: Int, empty: Boolean = false) {

            val row = LinearLayout(context)
            val WordTable: LinearLayout = (context as MainActivity).findViewById(R.id.WordList)

            row.orientation = LinearLayout.HORIZONTAL
            row.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)

            row.setPadding(10, 10, 10, 10)

            var letter_id = 0
            for (letter in word) {
                val letterColor = colors[letter_id++]
                addLetterToWord(letter, context, row, letterColor)
            }

            if (!empty) {
                if (number == 0) {
                    WordTable.removeViewAt(0)
                } else {
                    WordTable.removeViewAt(WordTable.childCount - 1)
                }
            }

            WordTable.addView(row, number )
        }

        private fun addLetterToWord(letter: Char, context: Context, row: LinearLayout, color: String) {
            // Letter View

            Log.d("GameUIHelpers", "Adding letter: $letter Color: $color")
            val letterView = TextView(context).apply {
                text = letter.toString()
                textSize = 45F
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                gravity = View.TEXT_ALIGNMENT_CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5f
                )
            }

            val drawableResId = when (color) {
                "green" -> R.drawable.letter_good
                "yellow" -> R.drawable.letter_medium
                "red" -> R.drawable.letter_bad
                "empty" -> R.drawable.letter_empty
                else -> throw IllegalArgumentException("Invalid color: $color")
            }
            letterView.background = ContextCompat.getDrawable(context, drawableResId)
            letterView.setTextColor(ContextCompat.getColor(context, R.color.white))

            // Letter View
            val letterFadeInAnimation = AlphaAnimation(0f, 1f).apply {
                interpolator = DecelerateInterpolator()
                duration = 500
            }
            letterView.animation = letterFadeInAnimation
            AddSpacer(context, row)
            row.addView(letterView)
        }

        fun clearBoard(context: Context, empty_rows: Int, firstletter: Char = ' ') {
            // Loop through the empty rows
            for (i in 0 until empty_rows) {
                // Empty colors
                val colors = arrayOf("empty", "empty", "empty", "empty", "empty")

                if (GameSettings(context).showFirstLetter && i == 0) {
                    // If the option to show the first letter is enabled, show it
                    colors[0] = "green"
                    addWordToBoard(context, "%s    ".format(firstletter), colors, i, true)

                } else {
                    // Add the empty row
                    addWordToBoard(context, "     ", colors, i, true)
                }
            }
        }

    }
}
