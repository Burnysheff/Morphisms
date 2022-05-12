To start the program, download the JAR file in directory Desktop.

There are 2 JARs, one of them require you to have JavaFX on your device, other doesn't.

To run the one that doen't require it, simply download it and run the following command in terminal:
java -jar "%PATH_TO_JAR%", where "%PATH_TO_JAR%" is the place where you store the downloaded file.
This JAR is heavy (~50Mb), as it stores some JavaFX library inside it. Moreover, it doesn't matter what OS you use, as in that JAR JavaFX for different OS is stored.

The other JAR is only 4Mb, but you need to have JavaFX on your device to run it. To run it, execute the following command in Terminal:
java --module-path "$PATH_TO_LIB$" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar "%PATH_TO_JAR%",
where "%PATH_TO_JAR%" is the place where you store the downloaded file and "$PATH_TO_LIB$" is the place of directory lib of JavaFX.
