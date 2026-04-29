import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

/**
 * This class is used to navigate between the various panels (screens) of the app
 */
public class NavigationController implements PanelNames{

    // singleton instance; this will be used as static instance across all app panel objects (screen objects)
    private static NavigationController navigationControllerInstance;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // will be used to validate whether all app screen panel objects have been added to the main panel object, and to navigate to different app screens
    private final Map<String, JPanel> panelCache = new HashMap<>();

    // used to store user credentials stored and shared between all screens
    private final Map<String, String> userCredentials = new HashMap<>();

    /**
     * Private constructor - prevents direct instantiation
     */
    private NavigationController() {}

    /**
     * Creates a static private instance of navigation controller that can only be accessed by this getter method
     * @return private static 'NavigationController' instance
     */
    public static NavigationController getInstance() {
        if (navigationControllerInstance == null) {
            navigationControllerInstance = new NavigationController();
        }
        return navigationControllerInstance;
    }

    /**
     * Store shared user credentials between panels
     * @param key a particular user credential
     * @param value that user credential's value
     */
    public void putData(String key, String value) {
        userCredentials.put(key, value);
    }

    /**
     * Setrieve user credential data in any panel
     * @param key a particular user credential
     * @return the value of the 'key' user credential
     */
    public String getData(String key) {
        return userCredentials.get(key);
    }

    /**
     * This method is called this once during app startup
     * @param cardLayout a type of swing ui layout manager used for apps with multiple screens
     * @param mainPanel the main/parent JPanel object containing all panels (screens) of the app
     */
    public void init(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    /**
     * Adds panels to main panel and store panels with the associated panel names in panel cache
     * @param panelName name of the panel
     * @param panel JPanel object for a particular panel
     */
    public void addPanelToMainPanel(String panelName, JPanel panel) {
        panelCache.put(panelName, panel);
        mainPanel.add(panel, panelName);
    }

    /**
     * Navigates to a panel by its name
     * @param panelName name of the panel
     */
    public void navigateTo(String panelName) {
        if (!panelCache.containsKey(panelName)) {
            throw new IllegalArgumentException("Panel '" + panelName + "' not added to Main Panel");
        }
        cardLayout.show(mainPanel, panelName);

        /*
        '(BasePanel)' casts the panel object returned by panelCache from JPanel to BasePanel.
        This cast is needed to access onNavigatedTo(), which is defined in BasePanel class but not in JPanel
         */
        BasePanel panel = (BasePanel) panelCache.get(panelName);
        panel.onNavigatedTo();
    }

}
