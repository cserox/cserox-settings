import org.delastochkin.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTests {
    @Test
    void testReadAttribute() throws IOException {
        String input = "{\n\"name\": Darcula,\n\"dark\": true}";
        Settings settings = Settings.fromJsonString(input);
        assertEquals("Darcula", settings.attribute("name").orElse(null));
        assertEquals("true", settings.attribute("dark").orElse(null));
        assertTrue(settings.attribute("does-not-exist").isEmpty());
        assertTrue(settings.element("name").isEmpty());
    }

    @Test
    void testReadElement() throws IOException {
        String input = "{\n\"colors\": {\"accentColor\": \"#ff79c6\"}}";
        Settings settings = Settings.fromJsonString(input);
        assertTrue(settings.attribute("colors").isEmpty());
        assertTrue(settings.attribute("accentColor").isEmpty());
        assertTrue(settings.element("colors").isPresent());
        Settings colors = settings.element("colors").get();
        assertEquals("#ff79c6", colors.attribute("accentColor").orElse(null));
        assertNull(colors.attribute("does-not-exist").orElse(null));
    }

    @Test
    void testReadSeveralElements() throws IOException {
        String input = "{\n" +
                "\"parent-attribute\": \"1\",\n" +
                "\"first-parent-element\": {\n" +
                "\"child-attribute\": \"2\",\n" +
                "\"first-child-element\": {\n" +
                "\"grand-child-attribute\": \"3\"\n" +
                "},\n" +
                "\"second-child-element\": {\n" +
                "\"grand-child-element\": {\n" +
                "\"grand-grand-child-attribute\": \"4\"\n" +
                "}\n" +
                "}\n" +
                "},\n" +
                "\"second-parent-element\": {\n" +
                "\"child-attribute\": \"5\"\n" +
                "}\n" +
                "}";
        assertEquals(input, Settings.fromJsonString(input).toJsonString());
    }

    @Test
    void testReadRealSettings() throws IOException {
        String input = "{\n" +
                "\"name\": \"Dracula\",\n" +
                "\"dark\": \"true\",\n" +
                "\"author\": \"Zihan Ma\",\n" +
                "\"editorScheme\": \"/themes/Dracula.xml\",\n" +
                "\"colors\": {\n" +
                "\"accentColor\": \"#ff79c6\",\n" +
                "\"secondaryAccentColor\": \"#bd93f9\",\n" +
                "\"primaryForeground\": \"#f8f8f2\",\n" +
                "\"primaryBackground\": \"#414450\",\n" +
                "\"secondaryBackground\": \"#3a3d4c\",\n" +
                "\"hoverBackground\": \"#282a36\",\n" +
                "\"selectionBackground\": \"#6272a4\",\n" +
                "\"selectionInactiveBackground\": \"#4e5a82\",\n" +
                "\"borderColor\": \"#282a36\",\n" +
                "\"separatorColor\": \"#5d5e66\"\n" +
                "},\n" +
                "\"ui\": {\n" +
                "\"*\": {\n" +
                "\"arc\": \"7\",\n" +
                "\"background\": \"primaryBackground\",\n" +
                "\"selectionForeground\": \"primaryForeground\",\n" +
                "\"selectionInactiveForeground\": \"primaryForeground\",\n" +
                "\"selectionBackground\": \"selectionBackground\",\n" +
                "\"selectionInactiveBackground\": \"selectionInactiveBackground\",\n" +
                "\"inactiveBackground\": \"primaryBackground\",\n" +
                "\"disabledBackground\": \"primaryBackground\",\n" +
                "\"borderColor\": \"borderColor\",\n" +
                "\"separatorColor\": \"separatorColor\"\n" +
                "},\n" +
                "\"Borders\": {\n" +
                "\"color\": \"borderColor\",\n" +
                "\"ContrastBorderColor\": \"borderColor\"\n" +
                "},\n" +
                "\"ActionButton\": {\n" +
                "\"hoverBackground\": \"hoverBackground\",\n" +
                "\"hoverBorderColor\": \"hoverBackground\",\n" +
                "\"pressedBackground\": \"hoverBackground\",\n" +
                "\"pressedBorderColor\": \"hoverBackground\"\n" +
                "},\n" +
                "\"Bookmark\": {\n" +
                "\"iconBackground\": \"accentColor\",\n" +
                "\"Mnemonic\": {\n" +
                "\"iconForeground\": \"primaryForeground\"\n" +
                "},\n" +
                "\"MnemonicAssigned\": {\n" +
                "\"foreground\": \"primaryForeground\",\n" +
                "\"background\": \"#786299\"\n" +
                "},\n" +
                "\"MnemonicCurrent\": {\n" +
                "\"foreground\": \"primaryForeground\",\n" +
                "\"background\": \"#8d6b81\"\n" +
                "}\n" +
                "},\n" +
                "\"Button\": {\n" +
                "\"foreground\": \"primaryForeground\",\n" +
                "\"startBorderColor\": \"selectionBackground\",\n" +
                "\"endBorderColor\": \"selectionBackground\",\n" +
                "\"startBackground\": \"#4f566d\",\n" +
                "\"endBackground\": \"#4f566d\",\n" +
                "\"focusedBorderColor\": \"secondaryAccentColor\",\n" +
                "\"disabledBorderColor\": \"#4f566d\",\n" +
                "\"default\": {\n" +
                "\"foreground\": \"primaryForeground\",\n" +
                "\"startBackground\": \"selectionBackground\",\n" +
                "\"endBackground\": \"selectionBackground\",\n" +
                "\"startBorderColor\": \"secondaryAccentColor\",\n" +
                "\"endBorderColor\": \"secondaryAccentColor\",\n" +
                "\"focusColor\": \"secondaryAccentColor\",\n" +
                "\"focusedBorderColor\": \"secondaryAccentColor\"\n" +
                "}\n" +
                "},\n" +
                "\"Counter\": {\n" +
                "\"foreground\": \"primaryBackground\",\n" +
                "\"background\": \"#2fc864\"\n" +
                "},\n" +
                "\"CheckBoxMenuItem\": {\n" +
                "\"acceleratorSelectionForeground\": \"accentColor\"\n" +
                "},\n" +
                "\"ComboBox\": {\n" +
                "\"modifiedItemForeground\": \"accentColor\",\n" +
                "\"ArrowButton\": {\n" +
                "\"background\": \"secondaryBackground\",\n" +
                "\"nonEditableBackground\": \"secondaryBackground\",\n" +
                "\"disabledIconColor\": \"#576285\",\n" +
                "\"iconColor\": \"secondaryAccentColor\"\n" +
                "},\n" +
                "\"selectionBackground\": \"secondaryAccentColor\",\n" +
                "\"nonEditableBackground\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"CompletionPopup\": {\n" +
                "\"selectionBackground\": \"selectionInactiveBackground\",\n" +
                "\"selectionInactiveBackground\": \"selectionInactiveBackground\",\n" +
                "\"matchForeground\": \"accentColor\"\n" +
                "},\n" +
                "\"Component\": {\n" +
                "\"focusColor\": \"secondaryAccentColor\",\n" +
                "\"borderColor\": \"selectionBackground\",\n" +
                "\"focusedBorderColor\": \"selectionBackground\",\n" +
                "\"disabledBorderColor\": \"selectionBackground\",\n" +
                "\"errorFocusColor\": \"#ff5554\",\n" +
                "\"inactiveErrorFocusColor\": \"#ff5554\",\n" +
                "\"warningFocusColor\": \"#f1fa8c\",\n" +
                "\"inactiveWarningFocusColor\": \"#f1fa8c\"\n" +
                "},\n" +
                "\"DragAndDrop\": {\n" +
                "\"borderColor\": \"selectionBackground\"\n" +
                "},\n" +
                "\"Editor\": {\n" +
                "\"background\": \"secondaryBackground\",\n" +
                "\"shortcutForeground\": \"accentColor\"\n" +
                "},\n" +
                "\"EditorTabs\": {\n" +
                "\"background\": \"secondaryBackground\",\n" +
                "\"underlinedTabBackground\": \"#292b38\",\n" +
                "\"underlineColor\": \"secondaryAccentColor\",\n" +
                "\"underlineHeight\": \"2\"\n" +
                "},\n" +
                "\"FileColor\": {\n" +
                "\"Blue\": \"#344f54\",\n" +
                "\"Green\": \"#344535\",\n" +
                "\"Orange\": \"#533f30\",\n" +
                "\"Yellow\": \"#4f4b41\",\n" +
                "\"Rose\": \"#4c273c\",\n" +
                "\"Violet\": \"#382b4a\"\n" +
                "},\n" +
                "\"Label\": {\n" +
                "\"errorForeground\": \"#ff5554\"\n" +
                "},\n" +
                "\"Link\": {\n" +
                "\"activeForeground\": \"accentColor\",\n" +
                "\"hoverForeground\": \"accentColor\",\n" +
                "\"visitedForeground\": \"secondaryAccentColor\",\n" +
                "\"pressedForeground\": \"secondaryAccentColor\"\n" +
                "},\n" +
                "\"Notification\": {\n" +
                "\"borderColor\": \"selectionBackground\",\n" +
                "\"errorBorderColor\": \"#ff5554\",\n" +
                "\"errorBackground\": \"primaryBackground\",\n" +
                "\"errorForeground\": \"primaryForeground\",\n" +
                "\"ToolWindow\": {\n" +
                "\"warningForeground\": \"primaryForeground\",\n" +
                "\"warningBackground\": \"primaryBackground\",\n" +
                "\"warningBorderColor\": \"#ffb86c\",\n" +
                "\"errorForeground\": \"primaryForeground\",\n" +
                "\"errorBorderColor\": \"#ff5554\",\n" +
                "\"errorBackground\": \"primaryBackground\",\n" +
                "\"informativeForeground\": \"primaryForeground\",\n" +
                "\"informativeBackground\": \"primaryBackground\",\n" +
                "\"informativeBorderColor\": \"#50fa7b\"\n" +
                "}\n" +
                "},\n" +
                "\"NotificationsToolwindow\": {\n" +
                "\"newNotification\": {\n" +
                "\"hoverBackground\": \"secondaryBackground\",\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"Notification\": {\n" +
                "\"hoverBackground\": \"secondaryBackground\"\n" +
                "}\n" +
                "},\n" +
                "\"PasswordField\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"Plugins\": {\n" +
                "\"SearchField\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"SectionHeader\": {\n" +
                "\"foreground\": \"primaryForeground\"\n" +
                "},\n" +
                "\"hoverBackground\": \"hoverBackground\",\n" +
                "\"lightSelectionBackground\": \"secondaryBackground\",\n" +
                "\"Button\": {\n" +
                "\"installBorderColor\": \"secondaryAccentColor\",\n" +
                "\"installForeground\": \"secondaryAccentColor\",\n" +
                "\"installBackground\": \"primaryBackground\",\n" +
                "\"installFillForeground\": \"primaryBackground\",\n" +
                "\"installFillBackground\": \"secondaryAccentColor\",\n" +
                "\"installFocusedBackground\": \"primaryBackground\",\n" +
                "\"updateBorderColor\": \"#5da3f4\",\n" +
                "\"updateForeground\": \"primaryForeground\",\n" +
                "\"updateBackground\": \"#5da3f4\"\n" +
                "},\n" +
                "\"Tab\": {\n" +
                "\"selectedBackground\": \"hoverBackground\",\n" +
                "\"selectedForeground\": \"primaryForeground\",\n" +
                "\"hoverBackground\": \"hoverBackground\"\n" +
                "}\n" +
                "},\n" +
                "\"ProgressBar\": {\n" +
                "\"failedColor\": \"#ff5554\",\n" +
                "\"failedEndColor\": \"#ff5554\",\n" +
                "\"trackColor\": \"selectionBackground\",\n" +
                "\"progressColor\": \"accentColor\",\n" +
                "\"indeterminateStartColor\": \"#93b8f9\",\n" +
                "\"indeterminateEndColor\": \"secondaryAccentColor\",\n" +
                "\"passedColor\": \"#50fa7b\",\n" +
                "\"passedEndColor\": \"#50fa7b\"\n" +
                "},\n" +
                "\"Popup\": {\n" +
                "\"Header\": {\n" +
                "\"activeBackground\": \"secondaryBackground\",\n" +
                "\"inactiveBackground\": \"secondaryBackground\"\n" +
                "}\n" +
                "},\n" +
                "\"ScrollBar\": {\n" +
                "\"Mac\": {\n" +
                "\"hoverThumbColor\": \"secondaryAccentColor\",\n" +
                "\"Transparent\": {\n" +
                "\"hoverThumbColor\": \"secondaryAccentColor\"\n" +
                "}\n" +
                "}\n" +
                "},\n" +
                "\"SearchEverywhere\": {\n" +
                "\"SearchField\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"Tab\": {\n" +
                "\"selectedBackground\": \"#313341\",\n" +
                "\"selectedForeground\": \"primaryForeground\"\n" +
                "}\n" +
                "},\n" +
                "\"SearchMatch\": {\n" +
                "\"startBackground\": \"accentColor\",\n" +
                "\"endBackground\": \"accentColor\"\n" +
                "},\n" +
                "\"Separator\": {\n" +
                "\"separatorColor\": \"separatorColor\"\n" +
                "},\n" +
                "\"SidePanel\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"TabbedPane\": {\n" +
                "\"tabSelectionHeight\": \"1\",\n" +
                "\"focusColor\": \"hoverBackground\",\n" +
                "\"hoverColor\": \"hoverBackground\",\n" +
                "\"underlineColor\": \"secondaryAccentColor\",\n" +
                "\"contentAreaColor\": \"hoverBackground\"\n" +
                "},\n" +
                "\"Table\": {\n" +
                "\"gridColor\": \"#5d617a\",\n" +
                "\"hoverBackground\": \"selectionBackground\",\n" +
                "\"lightSelectionBackground\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"TableHeader\": {\n" +
                "\"bottomSeparatorColor\": \"separatorColor\"\n" +
                "},\n" +
                "\"TextField\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"ToggleButton\": {\n" +
                "\"onBackground\": \"#50fa7b\",\n" +
                "\"onForeground\": \"hoverBackground\",\n" +
                "\"offBackground\": \"selectionBackground\",\n" +
                "\"offForeground\": \"hoverBackground\",\n" +
                "\"buttonColor\": \"primaryForeground\"\n" +
                "},\n" +
                "\"ToolWindow\": {\n" +
                "\"Button\": {\n" +
                "\"selectedBackground\": \"secondaryAccentColor\",\n" +
                "\"hoverBackground\": \"hoverBackground\"\n" +
                "},\n" +
                "\"Header\": {\n" +
                "\"background\": \"#484c60\",\n" +
                "\"inactiveBackground\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"HeaderTab\": {\n" +
                "\"underlineColor\": \"secondaryAccentColor\",\n" +
                "\"underlineHeight\": \"2\",\n" +
                "\"underlinedTabBackground\": \"#292b38\",\n" +
                "\"underlinedTabInactiveBackground\": \"#313341\"\n" +
                "}\n" +
                "},\n" +
                "\"Tree\": {\n" +
                "\"modifiedItemForeground\": \"accentColor\",\n" +
                "\"selectionBackground\": \"selectionBackground\",\n" +
                "\"selectionInactiveBackground\": \"selectionInactiveBackground\",\n" +
                "\"hash\": \"hoverBackground\"\n" +
                "},\n" +
                "\"ValidationTooltip\": {\n" +
                "\"errorBackground\": \"#4c273c\",\n" +
                "\"warningBackground\": \"#4f4b41\"\n" +
                "},\n" +
                "\"VersionControl\": {\n" +
                "\"FileHistory\": {\n" +
                "\"Commit\": {\n" +
                "\"selectedBranchBackground\": \"secondaryBackground\"\n" +
                "}\n" +
                "},\n" +
                "\"GitLog\": {\n" +
                "\"headIconColor\": \"#f1fa8c\",\n" +
                "\"localBranchIconColor\": \"#50fa7b\",\n" +
                "\"remoteBranchIconColor\": \"secondaryAccentColor\",\n" +
                "\"tagIconColor\": \"accentColor\",\n" +
                "\"otherIconColor\": \"#8be9fd\"\n" +
                "},\n" +
                "\"Log\": {\n" +
                "\"Commit\": {\n" +
                "\"hoveredBackground\": \"selectionBackground\",\n" +
                "\"currentBranchBackground\": \"secondaryBackground\"\n" +
                "}\n" +
                "},\n" +
                "\"RefLabel\": {\n" +
                "\"foreground\": \"primaryForeground\"\n" +
                "}\n" +
                "},\n" +
                "\"WelcomeScreen\": {\n" +
                "\"SidePanel\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "},\n" +
                "\"separatorColor\": \"separatorColor\",\n" +
                "\"Projects\": {\n" +
                "\"background\": \"hoverBackground\",\n" +
                "\"selectionBackground\": \"secondaryBackground\",\n" +
                "\"selectionInactiveBackground\": \"secondaryBackground\",\n" +
                "\"actions\": {\n" +
                "\"background\": \"secondaryBackground\"\n" +
                "}\n" +
                "},\n" +
                "\"Details\": {\n" +
                "\"background\": \"hoverBackground\"\n" +
                "}\n" +
                "}\n" +
                "},\n" +
                "\"icons\": {\n" +
                "\"ColorPalette\": {\n" +
                "\"Actions.Blue\": \"#5da3f4\",\n" +
                "\"Actions.Green\": \"#2fc864\",\n" +
                "\"Actions.Grey\": \"#858994\",\n" +
                "\"Actions.GreyInline.Dark\": \"#2fc864\",\n" +
                "\"Actions.GreyInline\": \"#2fc864\",\n" +
                "\"Actions.Red\": \"#ff5554\",\n" +
                "\"Actions.Yellow\": \"#f1fa8c\",\n" +
                "\"Objects.Blue\": \"#5da3f4\",\n" +
                "\"Objects.Green\": \"#2fc864\",\n" +
                "\"Objects.GreenAndroid\": \"#2fc864\",\n" +
                "\"Objects.Grey\": \"#858994\",\n" +
                "\"Objects.Pink\": \"#ff79c6\",\n" +
                "\"Objects.Purple\": \"#bd93f9\",\n" +
                "\"Objects.Red\": \"#ff5554\",\n" +
                "\"Objects.RedStatus\": \"#ff5554\",\n" +
                "\"Objects.Yellow\": \"#f1fa8c\",\n" +
                "\"Objects.YellowDark\": \"#f1fa8c\",\n" +
                "\"Objects.BlackText\": \"#282a35\",\n" +
                "\"Checkbox.Foreground.Selected.Dark\": \"#f8f8f2\",\n" +
                "\"Checkbox.Border.Default.Dark\": \"#bd93f9\",\n" +
                "\"Checkbox.Border.Selected.Dark\": \"#bd93f9\",\n" +
                "\"Checkbox.Border.Disabled.Dark\": \"#6272a4\",\n" +
                "\"Checkbox.Background.Default.Dark\": \"#6272a4\",\n" +
                "\"Checkbox.Background.Disabled.Dark\": \"#414450\",\n" +
                "\"Checkbox.Focus.Wide.Dark\": \"#bd93f9\",\n" +
                "\"Checkbox.Focus.Thin.Selected.Dark\": \"#bd93f9\",\n" +
                "\"Checkbox.Focus.Thin.Default.Dark\": \"#bd93f9\"\n" +
                "}\n" +
                "}\n" +
                "}";
        assertEquals(input, Settings.fromJsonString(input).toJsonString());
    }
}
