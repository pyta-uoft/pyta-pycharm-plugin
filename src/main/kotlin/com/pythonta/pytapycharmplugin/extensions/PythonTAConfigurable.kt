package com.pythonta.pytapycharmplugin.extensions

import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.layout.Row
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
 * @property pathFieldContent the property linked to the DSL field, delegating the setter and getter to [configService]
 */
class PythonTAConfigurable(_project: Project) : BoundSearchableConfigurable("PythonTA Plugin Settings", "base") {

    private var configService: MyProjectService = _project.service()

    var pathFieldContent: String
        get() = configService.getPythonSDKPath()
        set(value) {
            configService.setPythonSDKPath(value)
        }

    private val detectionResultPanel = panel {}

    override fun createPanel(): DialogPanel {

        val settingsPagePanel = panel {
            titledRow("PythonTA Plugin Configuration") {
                row {
                    label("Python SDK Installation Location")
                }
                row {
                    buildTextField(this)
                }
                row {
                    detectionResultPanel()
                }
            }
        }
        return settingsPagePanel
    }

    override fun reset() {
        super.reset()
        redetectPythonTA()
    }

    override fun apply() {
        super.apply()
        redetectPythonTA()
    }

    private fun buildTextField(row: Row): TextFieldWithBrowseButton {

        val textFieldBuilder = row.textFieldWithBrowseButton(::pathFieldContent)
            .withValidationOnInput {
                validateOnInput(this)
            }

        return textFieldBuilder.component
    }

    private fun redetectPythonTA() {
        detectionResultPanel.removeAll()
        detectionResultPanel.add(JLabel("Looking for PythonTA Installation...", AnimatedIcon.Default(), SwingConstants.LEFT))
        thread {
            val detectionResult = detectPythonTA()
            detectionResultPanel.removeAll()
            detectionResultPanel.add(JLabel(detectionResult))
            detectionResultPanel.repaint()
            detectionResultPanel.revalidate()
            // need to manually repaint and revalidate as threads are run asynchronously.
            // normally client framework code would repaint and revalidate post overrode method call.
        }
    }

    private fun detectPythonTA(): String {
        val pathProperties = analyzePath(pathFieldContent)

        val potentialSdk = pathProperties.any {
            property ->
            property in listOf(PathProperties.IS_PYTHON_SDK, PathProperties.MAYBE_PYTHON_SDK)
        }
        // approach is dangerous as path MIGHT be an SDK, but it might be an irrelevant executable
        if (potentialSdk) {
            val builder = ProcessBuilder(pathFieldContent, "-m", "python_ta", "-h")
            val process: Process
            try {
                process = builder.start()
            } catch (e: IOException) {
                return "Specified file was not executable"
            }
            val completed = process.waitFor(5, TimeUnit.SECONDS)
            if (!completed)
                return "Error: Process timed out (may be bug or hardware lag)"

            val errorCode = process.exitValue()
            if (errorCode == 0)
                return "PythonTA detected"
        }

        return "No PythonTA detected"
    }

    private fun validateOnInput(validationBuilder: ValidationInfoBuilder): ValidationInfo? {
        val fieldContent = (validationBuilder.component as ExtendableTextField).text
        if (fieldContent.isEmpty())
            return null

        val pathProperties = analyzePath(fieldContent)

        if (pathProperties.contains(PathProperties.NOT_FILE))
            return validationBuilder.error("Path is not file")
        else if (pathProperties.contains(PathProperties.MAYBE_PYTHON_SDK))
            return validationBuilder.warning("File might not be a Python SDK")
        else if (pathProperties.contains(PathProperties.NOT_PYTHON_SDK))
            return validationBuilder.error(
                "File is not a Python SDK. If it should be," +
                    "please rename it to follow the standard naming convention (file name starting with 'python')"
            )
        else if (pathProperties.contains(PathProperties.NOT_EXECUTABLE))
            return validationBuilder.error("File is not executable")

        return null
    }
}

/**
 * Analyze a path to infer properties of the target
 * @return integer values enumerating the above options
 */
private fun analyzePath(path: String): Set<PathProperties> {
    val file = File(path)

    val properties = HashSet<PathProperties>()

    if (!file.isFile)
        properties.add(PathProperties.NOT_FILE)
    else {
        if (file.canExecute()) {
            if (file.nameWithoutExtension == "python")
                properties.add(PathProperties.IS_PYTHON_SDK)
            else if (file.name.startsWith("python"))
                properties.add(PathProperties.MAYBE_PYTHON_SDK)
            else
                properties.add(PathProperties.NOT_PYTHON_SDK)
        } else
            properties.add(PathProperties.NOT_EXECUTABLE)
    }

    return properties
}

private enum class PathProperties {
    NOT_FILE,
    IS_PYTHON_SDK,
    MAYBE_PYTHON_SDK,
    NOT_PYTHON_SDK,
    NOT_EXECUTABLE
}
