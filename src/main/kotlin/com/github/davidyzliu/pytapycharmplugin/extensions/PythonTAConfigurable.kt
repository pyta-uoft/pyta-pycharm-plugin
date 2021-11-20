package com.github.davidyzliu.pytapycharmplugin.extensions

import com.github.davidyzliu.pytapycharmplugin.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel


class PythonTAConfigurable(_project: Project) : BoundSearchableConfigurable("PythonTA Plugin Settings", "base") {

    private var configService: MyProjectService = _project.service()

    var pathFieldContent: String
        get() = configService.getPythonTAPath()
        set(value) {
            configService.setPythonTAPath(value)
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