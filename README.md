# PyTA Plugin for Pycharm

![Build](https://github.com/pyta-uoft/pyta-pycharm-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
<!-- This specific section is a source for the [plugin.xml](/src/main/resources/META-INF/plugin.xml) file which will be extracted by the [Gradle](/build.gradle.kts) during the build process. -->
<!-- To keep everything working, do not remove this section. -->

This plugin provides the features of [PyTA](https://github.com/pyta-uoft/pyta) in your Pycharm editor! Currently, it allows you to perform a PyTA scan on a
python file to determine some common coding errors.

<!-- Plugin description end -->
## Development

### Getting Started
If you are working on the development of this plugin:
1. Clone this repository
2. Then, run `pre-commit install` to install the pre-commit hooks (this will automatically check and format your code every time you commit)

### Testing in a Sandbox IDE
During development, you can test the plugin in a sandbox IDE to see how users might interact with the plugin in a realistic environment.
To launch the sandbox, run the `Run Plugin` gradle task. This will launch a sandbox IDE with the plugin already installed.

## Demo
While the plugin is still in development, currently, it can scan a single python file and display the result of the scan to users.
Here is a quick demo of how to use the plugin.

### Configuring PyTA Executable Path
Before performing any scans, the plugin needs to know the location of the PyTA executable (`python_ta.exe`) in your environment. The plugin
first tries to detect this automatically. But, if it is unable to detect `python_ta.exe` file on its own, it can be provided
manually through `File > Settings > Tools > PythonTA Plugin Configuration`.

### Performing the Scan
Once the path for the PyTA executable has been configured, a PyTA scan on a python file can be performed. The action to perform
the scan is available at three locations. The action in all of these locations is only available for python files (those with a `.py` extension)
- **Editor Pop-Up Menu**: This menu can be opened by right-clicking on the text editor ![Editor Popup Menu Action](./src/main/resources/images/readme/editor_popup_action.png?raw=true)
- **Project View Pop-Up Menu**: This menu can be opened by right-clicking on a python file in the Project View ![Project View Popup Menu Action](./src/main/resources/images/readme/project_view_action.png?raw=true)
- **Code Menu**: This menu is present in the topmost toolbar. Note that in the image below, the action is only visible because the file selected (`sample.py`) is a python file.
![Code Menu Action](./src/main/resources/images/readme/code_menu_action.png?raw=true)

A scan can be performed by clicking on **Scan File with PythonTA** in any of these menus.

### Viewing the Results
Once a scan has been performed, the results of the scan can be viewed in the **PythonTA** Tool Window, present at the bottom
of the IDE.  ![Scan Results](./src/main/resources/images/readme/sample_scan_results.png?raw=true)

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
