package com.pythonta.pytapycharmplugin.extensions

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import com.pythonta.pytapycharmplugin.services.MyProjectService
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.swing.JLabel
import javax.swing.SwingConstants
import kotlin.concurrent.thread

/**
 * Project Configurable using Kotlin DSL to create a new page in the Settings menu
 *
 * @property configService the Project service which contains the Project SDK path.
 * @property pythonPath the property linked to the DSL field, delegating the setter and getter to [configService]
 */
class PythonTAConfigurable(project: Project) : BoundSearchableConfigurable("PythonTA", "base") {

    private var configService: MyProjectService = project.service()

    var pythonPath: String
        get() = configService.getPythonSDKPath()
        set(value) {
            configService.setPythonSDKPath(value)
        }

    private val detectionResultPanel = panel {}

    override fun createPanel(): DialogPanel {
        return panel {
            titledRow("PythonTA Plugin Settings") {
                row {
                    label("Python SDK Installation Location")
                }
                row {
                    val textFieldBuilder = this.textFieldWithBrowseButton(::pythonPath)
                        .withValidationOnInput {
                            validateOnInput(this)
                        }

                    textFieldBuilder.component
                }
                row {
                    detectionResultPanel()
                }
            }
        }
    }

    override fun reset() {
        super.reset()
        redetectPythonTA()
    }

    override fun apply() {
        super.apply()
        redetectPythonTA()
    }

    private fun redetectPythonTA() {
        detectionResultPanel.removeAll()
        detectionResultPanel.add(JLabel("Checking for PythonTA...", AnimatedIcon.Default(), SwingConstants.LEFT))
        thread {
            // Need to manually repaint and revalidate as threads are run asynchronously.
            // Normally client framework code would repaint and revalidate post overrode method call.
            val detectionResult = detectPythonTA()
            println(detectionResult)
            detectionResultPanel.removeAll()
            detectionResultPanel.add(JLabel(detectionResult))
            detectionResultPanel.repaint()
            detectionResultPanel.revalidate()
        }
    }

    private fun detectPythonTA(): String {
        val executableStatus = checkPythonPath(pythonPath)
        val potentialSdk = executableStatus in listOf(PythonExecutableStatus.IS_PYTHON_SDK, PythonExecutableStatus.MAYBE_PYTHON_SDK)

        if (!potentialSdk) return "No Python executable detected"

        val builder = ProcessBuilder(pythonPath, "-m", "python_ta", "-h")
        val process: Process
        try {
            process = builder.start()
        } catch (e: IOException) {
            return "Specified file was not executable"
        }
        val completed = process.waitFor(5, TimeUnit.SECONDS)
        if (!completed) {
            return "Error: Process timed out (may be bug or lag)"
        }

        val errorCode = process.exitValue()
        return if (errorCode == 0) {
            "PythonTA detected"
        } else {
            "PythonTA not detected"
        }
    }

    private fun validateOnInput(validationBuilder: ValidationInfoBuilder): ValidationInfo? {
        val fieldContent = (validationBuilder.component as ExtendableTextField).text

        return when (checkPythonPath(fieldContent)) {
            PythonExecutableStatus.NOT_FILE -> {
                validationBuilder.error("File does not exist")
            }
            PythonExecutableStatus.MAYBE_PYTHON_SDK -> {
                validationBuilder.error("File might not be a Python SDK")
            }
            PythonExecutableStatus.NOT_PYTHON_SDK -> {
                validationBuilder.error("File is not a Python SDK.")
            }
            PythonExecutableStatus.NOT_EXECUTABLE -> {
                validationBuilder.error("File is not executable")
            }
            else -> null
        }
    }
}

/**
 * Check whether the path refers to a Python executable.
 * Approach is dangerous as path MIGHT be an SDK, but it might be an irrelevant executable.
 * @param path the path to check
 * @return integer values enumerating the above options
 */
private fun checkPythonPath(path: String): PythonExecutableStatus {
    if (path.isEmpty()) return PythonExecutableStatus.NOT_FILE

    val file = File(path)

    return if (!file.isFile) {
        PythonExecutableStatus.NOT_FILE
    } else if (!file.canExecute()) {
        PythonExecutableStatus.NOT_EXECUTABLE
    } else if (file.nameWithoutExtension == "python") {
        PythonExecutableStatus.IS_PYTHON_SDK
    } else if (file.name.startsWith("python")) {
        PythonExecutableStatus.MAYBE_PYTHON_SDK
    } else {
        PythonExecutableStatus.NOT_PYTHON_SDK
    }
}

/**
 * Possible statuses for whether a given path refers to a Python executable.
 */
private enum class PythonExecutableStatus {
    NOT_FILE,
    IS_PYTHON_SDK,
    MAYBE_PYTHON_SDK,
    NOT_PYTHON_SDK,
    NOT_EXECUTABLE
}
