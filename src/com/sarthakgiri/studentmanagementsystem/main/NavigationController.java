import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class NavigationController implements PanelNames{

    // singleton instance
    private static NavigationController navigationControllerInstance;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, JPanel> panelCache = new HashMap<>();
    private Map<String, String> userCredentials = new HashMap<>();
    private String currentPanel;

    // private constructor - prevents direct instantiation
    private NavigationController() {}

    public static NavigationController getInstance() {
        if (navigationControllerInstance == null) {
            navigationControllerInstance = new NavigationController();
        }
        return navigationControllerInstance;
    }

    // store shared values between panels
    public void putData(String key, String value) {
        userCredentials.put(key, value);
    }

    // retrieve data
    public String getData(String key) {
        return userCredentials.get(key);
    }
    
    // call this once during app startup
    public void init(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    // add panels to main panel
    public void addPanelToMainPanel(String panelName, JPanel panel) {
        panelCache.put(panelName, panel);
        mainPanel.add(panel, panelName);
    }

    // navigate to a panel by name
    public void navigateTo(String panelName) {
        if (!panelCache.containsKey(panelName)) {
            throw new IllegalArgumentException("Panel '" + panelName + "' not added to Main Panel");
        }
        currentPanel = panelName;
        cardLayout.show(mainPanel, panelName);

        BasePanel panel = (BasePanel) panelCache.get(panelName);
        panel.onNavigatedTo();
    }

    public String getCurrentPage() {
        return currentPanel;
    }

}
