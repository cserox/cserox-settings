import org.delastochkin.JsonParser;
import org.delastochkin.Setting;
import org.delastochkin.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO better tests
public class SettingsTest {

    private Settings settings;

    @BeforeEach
    void setup() throws IOException {
        settings = JsonParser.parseSettings(new StringReader("""
                {
                    "parent-attribute": "1",
                    "first-parent-element": {
                        "child-attribute": "2",
                        "first-child-element": {
                            "grand-child-attribute": "3"
                        },
                        "second-child-element": {
                            "grand-child-element": {
                                "grand-grand-child-attribute": "4"
                            }
                        }
                    },
                    "second-parent-element": {
                        "child-attribute": "5"
                    }
                }
                """
        ));
    }

    @Test
    public void testFetch() {
        Collection<String> attributeNames = settings.fetch(Setting::isAttribute, Setting::name).toList();
        assertEquals(5, attributeNames.size());
        assertTrue(List.of("parent-attribute",
                "child-attribute",
                "grand-child-attribute",
                "grand-grand-child-attribute",
                "child-attribute").containsAll(attributeNames));
    }
}
