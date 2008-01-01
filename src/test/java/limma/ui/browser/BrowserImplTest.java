package limma.ui.browser;

import com.agical.rmock.extension.junit.RMockTestCase;
import limma.ui.UIProperties;
import limma.ui.browser.model.BrowserModel;
import limma.ui.dialogs.DialogManager;

public class BrowserImplTest extends RMockTestCase {
    public void testStartingSelectedNode() throws Exception {
        BrowserModel model = (BrowserModel) mock(BrowserModel.class);
        UIProperties ui = (UIProperties) mock(UIProperties.class);
        BrowserImpl browser = new BrowserImpl(model, ui, (DialogManager) mock(DialogManager.class));

        

        model.getBaseNode();

        startVerification();

        browser.getSelectedNode();
    }

}
