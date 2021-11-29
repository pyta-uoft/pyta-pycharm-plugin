package com.github.davidyzliu.pytapycharmplugin.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PytaPluginUtilsTest {

    // A set of constants that will be used in the tests
    companion object {
        private const val SAMPLE_FILE_ONE = "sample_file_one.py"
        private const val SAMPLE_FILE_TWO = "sample_file_two.py"

        private const val SAMPLE_MESSAGE_ONE_FILE_ONE =
            """
             {
                 "msg_id": "E0000",
                 "symbol": "some-symbol",
                 "msg": "This is a message",
                 "C": "C",
                 "category": "convention",
                 "confidence": [
                     "HIGH",
                     "No false positive possible."
                 ],
                 "abspath": "$SAMPLE_FILE_ONE",
                 "path": "$SAMPLE_FILE_ONE",
                 "module": "some_module",
                 "obj": "",
                 "line": 1,
                 "column": 0,
                 "node": null,
                 "snippet": "some_code_snippet",
                 "line_end": 11,
                 "column_end": 12
            }
        """

        private val SAMPLE_MESSAGE_ONE_FILE_ONE_OBJECT =
            PytaIssue(
                SAMPLE_FILE_ONE,
                arrayOf(
                    PytaMessage(
                        "some-symbol", "This is a message", "convention",
                        "sample_file_one.py", "some_module", 1, 0
                    )
                )
            )

        private const val SAMPLE_MESSAGE_TWO_FILE_ONE =
            """
             {
                 "msg_id": "E0001",
                 "symbol": "some-other-symbol",
                 "msg": "This is another message",
                 "C": "E",
                 "category": "error",
                 "confidence": [
                     "HIGH",
                     "No false positive possible."
                 ],
                 "abspath": "$SAMPLE_FILE_ONE",
                 "path": "$SAMPLE_FILE_ONE",
                 "module": "some_module",
                 "obj": "",
                 "line": 5,
                 "column": 4,
                 "node": null,
                 "snippet": "another_code_snipped",
                 "line_end": 5,
                 "column_end": 20
            }
            """

        const val SAMPLE_MESSAGE_ONE_FILE_TWO =
            """
             {
                 "msg_id": "E0000",
                 "symbol": "some-symbol",
                 "msg": "This is a message",
                 "C": "C",
                 "category": "convention",
                 "confidence": [
                     "HIGH",
                     "No false positive possible."
                 ],
                 "abspath": "$SAMPLE_FILE_TWO",
                 "path": "$SAMPLE_FILE_TWO",
                 "module": "some_module",
                 "obj": "",
                 "line": 1,
                 "column": 0,
                 "node": null,
                 "snippet": "some_code_snippet",
                 "line_end": 11,
                 "column_end": 12
            }
            """
    }

    @Test
    fun `PyTA Json string with single issue converted to Kotlin object`() {
        val samplePytaOutput =
            """
                [
                    {
                        "filename": "$SAMPLE_FILE_ONE",
                        "msgs": [$SAMPLE_MESSAGE_ONE_FILE_ONE]
                    }
                ]
            """
        val issues: List<PytaIssue> = PytaPluginUtils.parsePytaOutputString(samplePytaOutput)
        assertEquals(listOf(SAMPLE_MESSAGE_ONE_FILE_ONE_OBJECT), issues)
    }

    @Test
    fun `All messages for an issue present in Kotlin object`() {
        val samplePytaOutput =
            """
               [
                    {
                        "filename": "$SAMPLE_FILE_ONE",
                        "msgs": [$SAMPLE_MESSAGE_ONE_FILE_ONE, $SAMPLE_MESSAGE_ONE_FILE_TWO]
                    }
               ]
            """
        val issues: List<PytaIssue> = PytaPluginUtils.parsePytaOutputString(samplePytaOutput)

        assertEquals(1, issues.size)
        assertEquals(2, issues[0].msgs.size)
    }

    @Test
    fun `Multiple issues present in Kotlin object`() {
        val samplePytaOutput =
            """
               [
                    {
                        "filename": "$SAMPLE_FILE_ONE",
                        "msgs": [$SAMPLE_MESSAGE_ONE_FILE_ONE, $SAMPLE_MESSAGE_ONE_FILE_TWO]
                    },
                    {
                        "filename": "$SAMPLE_FILE_TWO",
                        "msgs": [$SAMPLE_MESSAGE_ONE_FILE_TWO]
                    }
               ]
            """
        val issues: List<PytaIssue> = PytaPluginUtils.parsePytaOutputString(samplePytaOutput)

        assertEquals(2, issues.size)
    }
}
