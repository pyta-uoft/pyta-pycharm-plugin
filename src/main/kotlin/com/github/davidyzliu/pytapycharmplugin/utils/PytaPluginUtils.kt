package com.github.davidyzliu.pytapycharmplugin.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Contains utility methods used in the plugin.
 *
 * This class has a private constructor because the utility methods have *no state*.
 * */
class PytaPluginUtils private constructor () {

    companion object {

        /**
         * Parse the JSON string containing the results of a scan and return a list of Kotlin objects representing the
         * issues.
         * @param messageString The JSON string returned by PyTA after a scan.
         * @return A list of PytaIssue containing results for each file that was used in the scan.
         * */
        fun parsePytaOutputString(messageString: String): List<PytaIssue> {
            val gson = Gson()
            val issueListType: Type = object : TypeToken<List<PytaIssue>>() {}.type
            return gson.fromJson(messageString, issueListType)
        }
    }


}
