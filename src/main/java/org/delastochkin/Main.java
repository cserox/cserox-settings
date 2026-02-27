package org.delastochkin;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        String theme = "one_dark.theme";
        FlatLafSettings settings = new FlatLafSettings(
                Settings.fromJsonString(
                        Files.readString(
                                Path.of("/home/delastochkin/TX/misc/themes/" + theme + ".json"
                                )
                        )
                )
        );
        Files.writeString(Path.of("/home/delastochkin/TX/misc/themes/" + theme + ".properties"), settings.toPreferences());
    }
}