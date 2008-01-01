package limma.ui.video;

import limma.application.video.VideoConfig;
import limma.domain.video.Video;
import limma.ui.AntialiasLabel;
import limma.ui.UIProperties;
import limma.ui.browser.BrowserList;
import limma.ui.browser.BrowserNodeRenderer;
import limma.ui.browser.model.BrowserModelNode;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class VideoBrowserNodeRenderer extends JPanel implements BrowserNodeRenderer {
    private AntialiasLabel titleLabel;
    private AntialiasLabel directorLabel;
    private JTextArea plotLabel;
    private AntialiasLabel runtimeLabel;
    private AntialiasLabel ratingLabel;
    private JLabel cover;
    private VideoConfig videoConfig;
    private boolean selected;
    private UIProperties uiProperties;

    public VideoBrowserNodeRenderer(VideoConfig videoConfig, UIProperties uiProperties) {
        super(new GridBagLayout());
        this.uiProperties = uiProperties;
        this.videoConfig = videoConfig;

        setOpaque(false);
        int width = 5;
        setBorder(BorderFactory.createEmptyBorder(width, width, width, width));

        cover = new JLabel();
        add(cover, new GridBagConstraints(0, 0, 1, 5, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));

        titleLabel = new AntialiasLabel(uiProperties);
        titleLabel.setForeground(Color.black);
        titleLabel.setFont(uiProperties.getMediumFont());
        add(titleLabel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        plotLabel = new JTextArea();
        plotLabel.setFont(uiProperties.getSmallFont());
        plotLabel.setForeground(Color.black);
        plotLabel.setOpaque(false);
        plotLabel.setEditable(false);
        plotLabel.setLineWrap(true);
        plotLabel.setWrapStyleWord(true);

        add(plotLabel, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        JPanel directorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        directorPanel.setOpaque(false);

        directorPanel.add(createLabel("Directed by: "));
        directorLabel = new AntialiasLabel(uiProperties);
        directorLabel.setForeground(Color.black);
        directorPanel.add(directorLabel);

        add(directorPanel, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        JPanel runtimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        runtimePanel.setOpaque(false);

        runtimePanel.add(createLabel("Runtime: "));
        runtimeLabel = new AntialiasLabel(uiProperties);
        runtimeLabel.setForeground(Color.black);
        runtimePanel.add(runtimeLabel);

        add(runtimePanel, new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ratingPanel.setOpaque(false);
        ratingPanel.add(createLabel("Rating: "));
        ratingLabel = new AntialiasLabel(uiProperties);
        ratingLabel.setForeground(Color.black);
        ratingPanel.add(ratingLabel);

        add(ratingPanel, new GridBagConstraints(1, 4, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    }

    public boolean supportsRendering(BrowserModelNode value) {
        return value instanceof MovieBrowserNode;
    }

    private AntialiasLabel createLabel(String text) {
        AntialiasLabel antialiasLabel = new AntialiasLabel(text, uiProperties);
        antialiasLabel.setForeground(Color.white);
        return antialiasLabel;
    }

    public Component getNodeRendererComponent(BrowserList browserList, BrowserModelNode value, int index, boolean isSelected, boolean cellHasFocus) {
        this.selected = isSelected;
        Video video = ((MovieBrowserNode) value).getVideo();

        titleLabel.setText(video.getTitle());
        String director = "Unknown";
        if (StringUtils.isNotBlank(video.getDirector())) {
            director = video.getDirector();
        }
        String year = "";
        if (video.getYear() != 0) {
            year = " (" + video.getYear() + ")";
        }
        directorLabel.setText(director + year);
        runtimeLabel.setText(video.getRuntime());
        ratingLabel.setText(video.getRating());

        File posterFile = new File(videoConfig.getPosterDir(), String.valueOf(video.getImdbNumber()));
        ImageIcon poster = new ImageIcon(posterFile.getAbsolutePath());
        cover.setIcon(poster);

        plotLabel.setText(video.getPlot());
        plotLabel.setSize(browserList.getWidth() - poster.getIconWidth() - 15, 100000);

        setComponentOrientation(browserList.getComponentOrientation());
        setForeground(Color.white);
        setOpaque(false);
        return this;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        Composite oldComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, uiProperties.getMenuTransparency()));

        if (selected) {
            paintGradient(graphics);
            graphics.setColor(new Color(0x127ec7));
            graphics.drawLine(0, 0, graphics.getClipBounds().width, 0);
            graphics.setColor(new Color(0x428ac5));
            graphics.drawLine(0, graphics.getClipBounds().height - 1, graphics.getClipBounds().width, graphics.getClipBounds().height - 1);
        }
        super.paintComponent(g);
        graphics.setComposite(oldComposite);

    }

    private void paintGradient(Graphics2D graphics) {
        Color topColor = new Color(0x209bd6);
        Color bottomColor = new Color(0x0177bf);
        int height = graphics.getClipBounds().height;
        int yPos = 0;
        graphics.setPaint(new GradientPaint(0, yPos, topColor, 0, yPos + height, bottomColor));
        graphics.fillRect(0, yPos, graphics.getClipBounds().width, height);
    }

}
