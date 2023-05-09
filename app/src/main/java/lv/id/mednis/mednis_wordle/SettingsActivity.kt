package lv.id.mednis.mednis_wordle

import GameSettings
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import lv.id.mednis.mednis_wordle.GameLogic.Dictionary

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Atpakaļ pogas listeneris
        findViewById<ImageButton>(R.id.SettingsBackButton)
            .setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)

                // Remove all previous activities from the stack
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                ContextCompat.startActivity(this, intent, null)

                // End this activity
                this.finish()
            }

        val gameSettings = GameSettings(this)

        // Atrodam slēdžus
        val switch_first_letter = findViewById<SwitchCompat>(R.id.switch_first_letter)
        val switch_require_word = findViewById<SwitchCompat>(R.id.switch_require_word)
        val switch_allow_duplicates = findViewById<SwitchCompat>(R.id.switch_allow_duplicates)
        val switch_allow_cheats = findViewById<SwitchCompat>(R.id.switch_cheat_mode)
        val number_max_guesses = findViewById<EditText>(R.id.number_max_guesses)


        // Uzstādām slēdžu stāvokļus
        switch_first_letter.isChecked = gameSettings.showFirstLetter
        switch_require_word.isChecked = gameSettings.allowNonWords
        switch_allow_duplicates.isChecked = gameSettings.allowDubleWords
        number_max_guesses.setText(gameSettings.maximumGuesses.toString())

        // Uzstādām teksta datus
        val infotext = HtmlCompat.fromHtml(getString(R.string.text_about), HtmlCompat.FROM_HTML_MODE_LEGACY)
        findViewById<TextView>(R.id.GuideText).text = infotext

        val aboutext = HtmlCompat.fromHtml(getString(R.string.text_about), HtmlCompat.FROM_HTML_MODE_LEGACY)
        findViewById<TextView>(R.id.AboutText).text = aboutext

        val data = Dictionary().getDictionaryStats(this)
        val choosableWords = getString(R.string.choosable_words)
        val totalWords = getString(R.string.total_words)

        findViewById<TextView>(R.id.option_choosableWords).text = choosableWords.format(data.chosenWords.toString())
        findViewById<TextView>(R.id.option_totalWords).text = totalWords.format(data.maxWords.toString())


        // Atjaunojam iestatījumus pēc tam, kad tie tiek mainīti

        switch_first_letter.setOnCheckedChangeListener { _, isChecked ->
            gameSettings.showFirstLetter = isChecked }

        switch_require_word.setOnCheckedChangeListener { _, isChecked ->
            gameSettings.allowNonWords = isChecked }

        switch_allow_duplicates.setOnCheckedChangeListener { _, isChecked ->
            gameSettings.allowDubleWords = isChecked }

        // Teksta ievade maksimālajam minējumu skaitam
        number_max_guesses.setOnEditorActionListener { _, _, _ ->
            var number = number_max_guesses.text.toString().toInt()

            // Pārbaudām vai skaitlis ir starp 1 un 10
            number = number.coerceIn(1, 10)
            number_max_guesses.setText("")
            number_max_guesses.append(number.toString())

            gameSettings.maximumGuesses = number
            false
        }

        if (gameSettings.isDebug) {
            // Šmaukšanās testējot
            switch_allow_cheats.setOnCheckedChangeListener { _, isChecked ->
                gameSettings.allowCheats = isChecked
            }
            switch_allow_cheats.isChecked = gameSettings.allowCheats
            switch_allow_cheats.visibility = SwitchCompat.VISIBLE
        }

    }

}
