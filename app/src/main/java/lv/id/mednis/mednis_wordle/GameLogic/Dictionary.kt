package lv.id.mednis.mednis_wordle.GameLogic

import android.content.Context
import android.provider.UserDictionary.Words
import android.util.Log
import com.google.android.material.transition.MaterialSharedAxis
import org.json.JSONObject
import java.io.IOException

class Dictionary {

    class DictionaryStats(max: Int, chosen: Int) {
        val maxWords = max
        val chosenWords = chosen
    }

    // Find a random word from the dictionary
    fun getRandomWord(context: Context): String? {
        // Word dictionary
        Log.d("Dictionary", "-- Ģenerējam vārdu --")

        val dictionary = readDictionary(context, "word-choices.json" )
        val words = dictionary?.getJSONArray("words")

        val randomIndex = (0 until words?.length()!!).random()
        Log.d("Dictionary", "Vārds: ${words.get(randomIndex)}")

        return words.get(randomIndex) as String?
    }

    fun isWord(word: String, context: Context): Boolean {
        // Word dictionary
        val dictionary = readDictionary(context, "word-dictionary.json")
        val words = dictionary?.getJSONArray("words")

        return if (word.length == 5) {
            (0 until words?.length()!!).any { words.getString(it) == word }
        } else false
    }

    fun getDictionaryStats(context: Context): DictionaryStats {
        val wordDictionary = readDictionary(context, "word-dictionary.json")
        val maxWords = wordDictionary?.getInt("total-words")?: 0
        val chosenDictionary = readDictionary(context, "word-choices.json")
        val choiceWords = chosenDictionary?.getInt("total-words")?: 0

        val stats = DictionaryStats(maxWords, choiceWords)
        return stats
    }

    private fun readDictionary(context: Context, filename: String): JSONObject? {
        // Read the dictionary from the json file
        val json: String
        try {
            val inputStream = context.assets.open(filename)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return JSONObject(json)
    }


}