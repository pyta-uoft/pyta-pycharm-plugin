package com.github.davidyzliu.pytapycharmplugin.utils

/**
 * Represents all issues identified by PyTA with a given file
 * @property filename The name of the file which was scanned
 * @property msgs An array of PytaMessage which contains information on each issue identified by PyTA with the file.
 * */
data class PytaIssue(val filename: String, val msgs: Array<PytaMessage>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PytaIssue

        if (filename != other.filename) return false
        if (!msgs.contentEquals(other.msgs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filename.hashCode()
        result = 31 * result + msgs.contentHashCode()
        return result
    }
}

/**
 * Represents a single PyTA issue and containing information about the issue identified by PyTA with a file
 * @property symbol The symbol associated with the PyTA message which uniquely identifies the issue
 * @property msg The string representing the issue with the file in human-readable form
 * @property category The category of this issue identified by PyTA
 * @property path The path with the file which was scanned (not absolute)
 * @property module The module of the file
 * @property line The line for which this message highlights the issue
 * @property column The column for which this message highlights the issue
 * */
data class PytaMessage(
    val symbol: String,
    val msg: String,
    val category: String,
    val path: String,
    val module: String,
    val line: Int,
    val column: Int
)
