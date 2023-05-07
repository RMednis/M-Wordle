import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo

class GameSettings(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    var isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    var showFirstLetter: Boolean
        get() = getValue("firstLetter") ?: false
        set(value) = saveValue("firstLetter", value)

    var allowCheats: Boolean
        get() = getValue("allowCheats") ?: false
        set(value) = saveValue("allowCheats", value)

    var allowDubleWords: Boolean
        get() = getValue("allowDoubleWords") ?: false
        set(value) = saveValue("allowDoubleWords", value)

    var allowNonWords: Boolean
        get() = getValue("allowNonWords") ?: false
        set(value) = saveValue("allowNonWords", value)

    var maximumGuesses: Int
        get() = getValue("maximumGuesses") ?: 5
        set(value) = saveValue("maximumGuesses", value)

    private var savedDefaults: Int
        get() = getValue("savedDefaults") ?: 0
        set(value) = saveValue("savedDefaults", value)

    private fun saveValue(key: String, value: Any?) {
        val editor = sharedPreferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
        }
        editor.apply()
    }

    private inline fun <reified T : Any> getValue(key: String): T? {
        return when (T::class) {
            Boolean::class -> sharedPreferences.getBoolean(key, false) as T
            Int::class -> sharedPreferences.getInt(key, 5) as T
            else -> null
        }
    }

    fun loadDefaultSettings() {
        if (savedDefaults == 0) {
            showFirstLetter = true
            allowDubleWords = false
            allowCheats = false
            allowNonWords = false
            maximumGuesses = 5
            savedDefaults = 1
        }
    }
}


