package com.github.davidyzliu.pytapycharmplugin.extensions

import com.github.davidyzliu.pytapycharmplugin.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel

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

    override fun createPanel(): DialogPanel {

        val settingsPagePanel = panel {
            titledRow("PythonTA Plugin Configuration") {
                row {
                    label("PythonTA Installation Folder")
                }
                row {
                    textFieldWithBrowseButton(::pathFieldContent)
                }
            }
        }
        return settingsPagePanel
    }
}
