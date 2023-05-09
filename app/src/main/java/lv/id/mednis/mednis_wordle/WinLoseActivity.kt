package lv.id.mednis.mednis_wordle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

class WinLoseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win_lose)

        val OverTitle = findViewById<TextView>(R.id.GameOverTitle)
        val OverText = findViewById<TextView>(R.id.GameOverText)
        var word = "mednis"

        val extras = intent.extras
        if (extras != null) {
            val win = extras.getBoolean("win")
            Log.d("WinLoseActivity", "win: $win")

            val attempts = extras.getInt("attempts")
            Log.d("WinLoseActivity", "attempts: $attempts")

            word = extras.getString("word") ?: "mednis"
            Log.d("WinLoseActivity", "word: $word")

            if (win) {
                OverTitle.text = getString(R.string.gameover_title_win)
                OverText.text = getString(R.string.gameover_text_win).format(attempts)
            } else {
                OverTitle.text = getString(R.string.gameover_title_lose)
                OverText.text = getString(R.string.gameover_text_lose).format(attempts)
            }
        }

        // Letter View Blocks
        val letter1 = findViewById<TextView>(R.id.OverLetter1)
        val letter2 = findViewById<TextView>(R.id.OverLetter2)
        val letter3 = findViewById<TextView>(R.id.OverLetter3)
        val letter4 = findViewById<TextView>(R.id.OverLetter4)
        val letter5 = findViewById<TextView>(R.id.OverLetter5)

        // Set the background color
        letter1.background = AppCompatResources.getDrawable(this, R.drawable.letter_good)
        letter2.background = AppCompatResources.getDrawable(this, R.drawable.letter_good)
        letter3.background = AppCompatResources.getDrawable(this, R.drawable.letter_good)
        letter4.background = AppCompatResources.getDrawable(this, R.drawable.letter_good)
        letter5.background = AppCompatResources.getDrawable(this, R.drawable.letter_good)

        // Set the letter
        letter1.text = word[0].toString()
        letter2.text = word[1].toString()
        letter3.text = word[2].toString()
        letter4.text = word[3].toString()
        letter5.text = word[4].toString()


        // Button to go back to the game
        findViewById<Button>(R.id.GameOverPoga).setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            ContextCompat.startActivity(this, intent, null)
            this.finish()
        }

        // Settings Button
        findViewById<ImageButton>(R.id.SettingsBackButton)
            .setOnClickListener {
                ContextCompat.startActivity(this, Intent(this, SettingsActivity::class.java), null)
                this.finish()
            }

    }
}