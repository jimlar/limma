package limma.plugins.video;

import limma.swing.AntialiasLabel;
import limma.Configuration;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class VideoListCellRenderer extends JPanel implements ListCellRenderer {
    private static final Color SELECTED_BACKGROUND = Color.blue.darker().darker();
    private AntialiasLabel titleLabel;
    private AntialiasLabel directorLabel;
    private JTextArea plotLabel;
    private AntialiasLabel runtimeLabel;
    private AntialiasLabel ratingLabel;
    private JLabel cover;
    private Configuration configuration;

    public VideoListCellRenderer(Configuration configuration) {
        super(new GridBagLayout());
        this.configuration = configuration;
        int width = 5;
        setBorder(BorderFactory.createEmptyBorder(width, width, width, width));

        cover = new JLabel();
        add(cover, new GridBagConstraints(0, 0, 1, 4, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));

        titleLabel = new AntialiasLabel();
        titleLabel.setForeground(Color.yellow);
        add(titleLabel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        plotLabel = new JTextArea();
        plotLabel.setFont(AntialiasLabel.DEFAULT_FONT);
        plotLabel.setForeground(Color.white);
        plotLabel.setOpaque(false);
        plotLabel.setEditable(false);
        plotLabel.setLineWrap(true);
        plotLabel.setWrapStyleWord(true);

        add(plotLabel, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        JPanel directorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        directorPanel.setOpaque(false);

        directorPanel.add(new AntialiasLabel("Directed by: "));
        directorLabel = new AntialiasLabel();
        directorPanel.add(directorLabel);

        add(directorPanel, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        JPanel runtimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        runtimePanel.setOpaque(false);
        
        runtimePanel.add(new AntialiasLabel("Runtime: "));
        runtimeLabel = new AntialiasLabel();
        runtimePanel.add(runtimeLabel);

        runtimePanel.add(new AntialiasLabel("Rating: "));
        ratingLabel = new AntialiasLabel();
        runtimePanel.add(ratingLabel);

        add(runtimePanel, new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Video video = (Video) value;

        titleLabel.setText(video.getTitle());
        directorLabel.setText(video.getDirector() + " (" + video.getYear() + ")");
        runtimeLabel.setText(video.getRuntime());
        ratingLabel.setText(video.getRating());
        plotLabel.setText(video.getPlot());

        File posterFile = new File(configuration.getFile("video.posterdir"), String.valueOf(video.getImdbNumber()));
        cover.setIcon(new ImageIcon(posterFile.getAbsolutePath()));

        setComponentOrientation(list.getComponentOrientation());
        setForeground(Color.white);
        setBackground(isSelected ? SELECTED_BACKGROUND : list.getBackground());
        setOpaque(isSelected);
        return this;
    }
}
