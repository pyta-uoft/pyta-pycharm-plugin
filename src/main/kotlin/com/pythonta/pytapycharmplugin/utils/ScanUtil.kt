package com.pythonta.pytapycharmplugin.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.stream.Collectors

/**
 * Contains utility methods that help with the scanning of files in the ScanFileAction
 * */
object ScanUtil {

    /**
     * Performs a scan on the provided file and returns a string representing the JSON
     * object that contains the results of the scan.
     * @param pythonSDKPath Path to PythonTA executable
     * @param filePath The path of the file to be scanned
     * @return A string represneting the JSON object containing results of the scan
     * @throws IOException
     * */
    @Throws(IOException::class)
    fun scan(pythonSDKPath: String, filePath: String): String {
        val builder = ProcessBuilder(pythonSDKPath, "-m", "python_ta", filePath, "--output-format", "python_ta.reporters.JSONReporter")
        var p: Process? = null
        try {
            p = builder.start()
        } catch (e: IOException) {
            println(e)
        }

        val stdInput = BufferedReader(InputStreamReader(p!!.inputStream))
        return stdInput.lines().collect(Collectors.joining(System.lineSeparator()))
    }
}
