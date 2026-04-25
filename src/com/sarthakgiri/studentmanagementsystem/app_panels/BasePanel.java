import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class BasePanel extends JPanel implements PanelNames {

    protected NavigationController navigationController = NavigationController.getInstance();

    public BasePanel() {
        setLayout(new BorderLayout(10, 10));
        add(build(), BorderLayout.CENTER);
    }

    // Each panel implements its own UI
    protected abstract JPanel build();

    // called every time a page is shown (optional)
    public void onNavigatedTo() {}
}
