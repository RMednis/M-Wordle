package lv.id.mednis.mednis_wordle

import GameSettings
import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import lv.id.mednis.mednis_wordle.GameLogic.Dictionary
import lv.id.mednis.mednis_wordle.GameLogic.GameManager
import lv.id.mednis.mednis_wordle.GameLogic.GameUIHelpers
import java.util.Locale


class MainActivity : AppCompatActivity() {
    // Game Vars
    var correct_word = ""
    var guesses = 0
    var guessed_words = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        // Ielādējam iestatījumus
        val gameSettings = GameSettings(this)
        gameSettings.loadDefaultSettings()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Izvēlamies nejaušu vārdu!
        correct_word = Dictionary().getRandomWord(this).toString()

        // Iestatām toolbaru
        setSupportActionBar(findViewById(R.id.MainToolbar))
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.actionbar_main)

        // Uzstādām listener priekš pogas
        findViewById<ImageButton>(R.id.SettingsBackButton)
            .setOnClickListener {
                GameUIHelpers().showSettings(this)
            }

        // Uzstādām tīru spēles lauku
        GameUIHelpers.WordList().clearBoard(this, gameSettings.maximumGuesses, correct_word[0])

        val WordInput: TextInputEditText = findViewById(R.id.WordInput)

        // Šmaukšanās testējot
        if (gameSettings.allowCheats && gameSettings.isDebug) {
            showWord()
        }

        // Pievienojam listeneri enter pogai
        WordInput.setOnEditorActionListener { textView, actionId, event ->
            WordInput.requestFocus()
            var used = false
            if (actionId == EditorInfo.IME_ACTION_DONE) used = checkInput(textView)
            used
        }

        WordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                GameUIHelpers().updateInputHint(this@MainActivity)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Atveram klaviatūru
        WordInput.requestFocus()
    }


    fun checkInput(WordInput: TextView): Boolean {
        val entered_word = WordInput.text.toString().lowercase(Locale.ROOT)
        val WordleScroll = findViewById<ScrollView>(R.id.WordleScroll)

        try {
            GameManager().inputIssues(this, entered_word, correct_word, guesses, guessed_words)
        } catch (e: GameManager.inputError) {
            // Izvadam kļūdas paziņojumu
            Snackbar.make(findViewById<LinearLayout>(R.id.WordList), e.message.toString(), Snackbar.LENGTH_LONG).show()
            return true
        }

        // Formatējam vārdu, nosakām krāsas
        val cleanedWord = entered_word.padEnd(5, ' ')
        val colors = GameManager().getWordColors(correct_word, cleanedWord)

        // Pievienojam vārdu tabulā
        GameUIHelpers.WordList().addWordToBoard(this, cleanedWord, colors, guesses)

        // Pavirzam scroll view uz leju
        WordleScroll.post {
            WordleScroll.fullScroll(View.FOCUS_DOWN)
        }

        // Palielinām minējumu skaitu
        guesses += 1
        guessed_words.add(entered_word)

        // Pārbaudām vai vārds ir pareizs
        if (correct_word == cleanedWord) {
            // Spēles beigas
            GameUIHelpers.GameOver().gameOverWon(this, correct_word, guesses)
        }

        // Pārbaudām vai mēģinājumu skaits ir pārsniegts
        if (guesses >= GameSettings(this).maximumGuesses) {
            // Spēles beigas
            GameUIHelpers.GameOver().gameOverLost(this, correct_word, guesses)
        }

        // Notīrām ievades lauku
        WordInput.text = null
        WordInput.requestFocus()
        return true
    }

    fun showWord(){
        if (ApplicationInfo.FLAG_DEBUGGABLE and applicationInfo.flags != 0) {
            Log.d("Mednis-Wordle", "Correct word: $correct_word")
            val RandomView: TextView = findViewById(R.id.RandomWord)
            RandomView.visibility = View.VISIBLE
            RandomView.text = correct_word
        }
    }
}