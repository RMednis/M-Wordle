package lv.id.mednis.mednis_wordle.GameLogic

import GameSettings
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lv.id.mednis.mednis_wordle.R
import java.util.Locale




class GameManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    class inputError(error: String): Exception(error)

    fun inputIssues(context: Context, entered_word: String, correct_word: String, guesses: Int, guessed_words: MutableList<String>) {

        val gameSettings = GameSettings(context)


        if (gameSettings.showFirstLetter && guesses == 0) {
            if (entered_word[0] != correct_word[0]) {

                // Vārds nesākas ar pareizo burtu!
                val errormessage = context.getString(R.string.error_word_doesnt_start_with_letter)
                    .format(correct_word[0].toString().uppercase(Locale.ROOT))

                throw inputError(errormessage)
            }
        }

        if (gameSettings.allowNonWords) {
            if (!Dictionary().isWord(entered_word, context)) {
                throw inputError(context.getString(R.string.error_word_not_in_dictionary))
            }
        }

        if (!gameSettings.allowDubleWords) {
            // Check if word in list
            if (entered_word in guessed_words) {
                // Vārds jau ir minēts!
                throw inputError(context.getString(R.string.error_word_guessed))
            }
        }
    }


    fun getWordColors(correctWord: String, guessedWord: String): Array<String> {
        var colors = Array(correctWord.length) { "" }
        var letter_position = 0

        for (letter in guessedWord) {
            // Is the letter even in the word?
            if (correctWord.contains(letter)) {

                // Is the letter in the correct position?
                if (correctWord[letter_position] == guessedWord[letter_position]) {
                    // Green Letter
                    colors[letter_position] = "green"

                } else {
                    // Yellow Letter
                    colors[letter_position] = "yellow"
                }
            } else {
                // Red Letter
                colors[letter_position] = "red"
            }
            letter_position++
        }

        return colors
    }
}