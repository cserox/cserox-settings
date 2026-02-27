import org.delastochkin.Setting;
import org.delastochkin.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SettingsTest {

    private Settings settings;

    @BeforeEach
    void setup() throws IOException {
        settings = Settings.fromJsonString("{\n\"parent-attribute\": \"1\",\n\"first-parent-element\": {\n\"child-attribute\": \"2\",\n\"first-child-element\": {\n\"grand-child-attribute\": \"3\"\n},\n\"second-child-element\": {\n\"grand-child-element\": {\n\"grand-grand-child-attribute\": \"4\"\n}\n}\n},\n\"second-parent-element\": {\n\"child-attribute\": \"5\"\n}\n}");
    }

/*
    {
      "parent-attribute" : "1",
      "first-parent-element" : {
        "child-attribute" : "2",
        "first-child-element" : {
          "grand-child-attribute" : "3"
        },
        "second-child-element" : {
          "grand-child-element" : {
            "grand-grand-child-attribute" : "4"
          }
        }
      },
      "second-parent-element" : {
        "child-attribute" : "5"
      }
    }
*/
    @Test
    public void testFetch() {
        Collection<String> attributeNames = settings.fetchAll(Setting::isAttribute, Setting::name);
        assertEquals(5, attributeNames.size());
        assertTrue(List.of("parent-attribute",
                "child-attribute",
                "grand-child-attribute",
                "grand-grand-child-attribute",
                "child-attribute").containsAll(attributeNames));
    }
}
