package limma;

import limma.persistence.PersistenceConfigImpl;
import limma.persistence.PersistenceManager;
import limma.persistence.PersistenceManagerImpl;
import limma.plugins.game.GameConfigImpl;
import limma.plugins.game.GamePlugin;
import limma.plugins.music.ExternalMusicPlayer;
import limma.plugins.music.MusicConfigImpl;
import limma.plugins.music.MusicPlugin;
import limma.plugins.video.*;
import limma.swing.*;
import limma.swing.menu.NavigationModel;
import org.hibernate.Session;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        tweakSwing();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DefaultPicoContainer pico = new DefaultPicoContainer();
                pico.registerComponentImplementation(UIPropertiesImpl.class);
                pico.registerComponentImplementation(PlayerManager.class);
                pico.registerComponentImplementation(NavigationModel.class);
                pico.registerComponentImplementation(limma.swing.menu.Navigation.class);
                pico.registerComponentImplementation(PersistenceManagerImpl.class);
                pico.registerComponentImplementation(PersistenceConfigImpl.class);
                pico.registerComponentImplementation(DialogManagerImpl.class);
                pico.registerComponentImplementation(CursorHider.class);

                pico.registerComponentImplementation(VideoPlugin.class);
                pico.registerComponentImplementation(VideoConfigImpl.class);
                pico.registerComponentImplementation(VideoPlayer.class);
                pico.registerComponentImplementation(IMDBServiceImpl.class);

                pico.registerComponentImplementation(MusicPlugin.class);
                pico.registerComponentImplementation(MusicConfigImpl.class);
                pico.registerComponentImplementation(ExternalMusicPlayer.class);

                pico.registerComponentImplementation(GamePlugin.class);
                pico.registerComponentImplementation(GameConfigImpl.class);

                pico.registerComponentImplementation(MainWindow.class);

                pico.start();

                MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);
                mainWindow.setUndecorated(true);
                mainWindow.setResizable(false);

                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                device.setFullScreenWindow(mainWindow);
                mainWindow.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());

//                mainWindow.setSize(800, 600);
//                mainWindow.setVisible(true);

                mainWindow.requestFocus();
                generateSql(pico);
            }
        });
    }

    private static void generateSql(DefaultPicoContainer pico) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DialogManager manager = (DialogManager) pico.getComponentInstanceOfType(DialogManager.class);
        manager.executeInDialog(new TransactionalTask((PersistenceManager) pico.getComponentInstanceOfType(PersistenceManager.class)) {
            public void runInTransaction(Session session) {
                java.util.List videos = session.createQuery("from Video").list();
                for (Iterator i = videos.iterator(); i.hasNext();) {
                    Video video = (Video) i.next();
                    Set files = video.getFiles();
                    VideoFile videoFile = (VideoFile) files.iterator().next();
                    File file = videoFile.getFile();
                    if (!file.getParentFile().getName().startsWith("movies")) {
                        file = file.getParentFile();
                    }
                    Date created = new Date(file.lastModified());
                    System.out.println("update video set created = '" + format.format(created) + "' where id = " + video.getId() + ";");
                }
            }

            public JComponent prepareToRun(TaskInfo taskInfo) {
                return new JLabel("Updateing database");
            }
        });
    }

    private static void tweakSwing() {
        System.setProperty("swing.aatext", "true");
        UIManager.put("TabbedPane.tabsOpaque", "false");
        UIManager.put("TabbedPane.contentOpaque", "false");
    }
}
