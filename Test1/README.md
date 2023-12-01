# CSE360_EffortLogger 

## Steps to use Project

1. Clone the repo

2. Import repo:
- File -> Import the project
- Existing Projects into Workspace
- Select root directory and select cloned repo
- Select the Project if it is not already selected
- Click Finish

3. Add libraries to prevent errors
- Go to Window -> Preferences
- Use the search to find User Libraries
- Click New and give the library the name "JavaFX" (it must be exactly what is in the quotes)
- Add External JARs and add all javafx JARs ("javafx.base.jar" "javafx.controls.jar" "javafx.fxml.jar" "javafx.graphics.jar" "javafx.media.jar" "javafx.swing.jar" "javafx.web.jar" "javafx-swt.jar")
  - (For example, Josh's JavaFX is located at `C:\Program Files\Java\openjfx-19.0.2_windows-x64_bin-sdk\javafx-sdk-19.0.2\lib`)

4. Run configuration
- Open run configuration (dropdown next to run button)
- Add the VM arguments  `--module-path "\path\to\javafx-sdk-19.0.2\lib" --add-modules javafx.controls,javafx.fxml`
  - (For example, Josh's JavaFX is located at `C:\Program Files\Java\openjfx-19.0.2_windows-x64_bin-sdk\javafx-sdk-19.0.2\lib`)

