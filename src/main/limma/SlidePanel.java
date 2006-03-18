package limma;

import javax.swing.*;
import java.awt.*;

public class SlidePanel extends JPanel {
    private JComponent slideComponent;

    public SlidePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public void slideIn(JComponent component) {
        if (component != slideComponent) {
            this.slideComponent = component;
            removeAll();
            add(slideComponent, BorderLayout.CENTER);
            slideComponent.setVisible(false);
        }
        slideIn();
    }

    public void slideOut() {
        slide(true);
    }

    public void slideIn() {
        slide(false);
    }

    private void slide(boolean out) {
        if (slideComponent != null) {
            slideComponent.setVisible(!out);
        }
//        Cycle cycle = new Cycle(3000, 50);
//        Envelope envelope = new Envelope(1,
//                                         0,
//                                         Envelope.RepeatBehavior.FORWARD,
//                                         Envelope.EndBehavior.HOLD);
//        Point p0 = new Point(0, 0);
//        Point p1 = new Point(slideComponent.getLocation().x, 0);
//        PropertyRange range;
//        if (out) {
//            range = PropertyRange.createPropertyRangePoint("location", p0, p1);
//        } else {
//            range = PropertyRange.createPropertyRangePoint("location", p1, p0);
//        }
//        TimingController timer = new TimingController(cycle,
//                                                      envelope,
//                                                      new ObjectModifier(slideComponent, range));
//        timer.setAcceleration(1.0f);
//        timer.start();
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SlidePanel slidePanel = new SlidePanel();
        frame.add(slidePanel);

        frame.setSize(400, 400);
        frame.setVisible(true);

        Thread.sleep(2000);
        System.out.println("Sliding");
        slidePanel.slideIn(new JLabel("Hej"));
    }
}
