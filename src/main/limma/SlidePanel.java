package limma;

import javax.swing.*;
import java.awt.*;

public class SlidePanel extends JPanel {
    private JComponent slideComponent;

    public SlidePanel() {
        setLayout(new SlideLayout());
        setOpaque(false);
    }

    public void slideIn(JComponent component) {
        if (component != slideComponent) {
            this.slideComponent = component;
            slideComponent.setVisible(false);
            removeAll();
            add(slideComponent);
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
        if (slideComponent == null) {
            return;
        }
        slideComponent.setVisible(!out);
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

        JPanel glassPane = (JPanel) frame.getGlassPane();
        glassPane.setLayout(new SlideLayout());
        glassPane.setVisible(true);

        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.gridx = 1;
        glassPane.add(slidePanel, gbc);
        gbc.gridx = 0;
        gbc.weighty = Integer.MAX_VALUE;
        gbc.weightx = Integer.MAX_VALUE;
        glassPane.add(Box.createGlue(), gbc);

        frame.setSize(400, 400);
        frame.validate();
        frame.setVisible(true);

        Thread.sleep(2000);
        System.out.println("Sliding in");
        slidePanel.slideIn(new JLabel("<html>Hej heasdadasdaasj<br>Shiasdadssdsat!<br>dfssdfsdsdsdfdsfsfsdfsdf</html>"));
        Thread.sleep(2000);
        System.out.println("Sliding out");
        slidePanel.slideOut();
        Thread.sleep(2000);
        System.out.println("Sliding in");
        slidePanel.slideIn();
        Thread.sleep(2000);
        System.out.println("Sliding out");
        slidePanel.slideOut();
    }


    public static class SlideLayout implements LayoutManager2 {
        private Component component;

        public SlideLayout() {
        }

        public void addLayoutComponent(Component comp, Object constraints) {
            synchronized (comp.getTreeLock()) {
                addLayoutComponent((String) constraints, comp);
            }
        }

        public void addLayoutComponent(String name, Component comp) {
            synchronized (comp.getTreeLock()) {
                component = comp;
            }
        }

        public void removeLayoutComponent(Component comp) {
            synchronized (comp.getTreeLock()) {
                if (comp == component) {
                    component = null;
                }
            }
        }

        public Component getLayoutComponent(Object constraints) {
            if (constraints == null) {
                return component;
            } else {
                throw new IllegalArgumentException("cannot get component: unknown constraint: " + constraints);
            }
        }

        public Component getLayoutComponent(Container target, Object constraints) {
            if (constraints == null) {
                return component;
            } else {
                throw new IllegalArgumentException("cannot get component: invalid constraint: " + constraints);
            }
        }

        public Object getConstraints(Component comp) {
            return null;
        }

        public Dimension minimumLayoutSize(Container target) {
            synchronized (target.getTreeLock()) {
                Dimension dim = new Dimension(0, 0);

                if (component != null) {
                    Dimension d = component.getMinimumSize();
                    dim.width += d.width;
                    dim.height = Math.max(d.height, dim.height);
                }

                Insets insets = target.getInsets();
                dim.width += insets.left + insets.right;
                dim.height += insets.top + insets.bottom;

                return dim;
            }
        }

        public Dimension preferredLayoutSize(Container target) {
            synchronized (target.getTreeLock()) {
                Dimension dim = new Dimension(0, 0);

                if (component != null) {
                    Dimension d = component.getPreferredSize();
                    dim.width += d.width;
                    dim.height = Math.max(d.height, dim.height);
                }

                Insets insets = target.getInsets();
                dim.width += insets.left + insets.right;
                dim.height += insets.top + insets.bottom;

                return dim;
            }
        }

        public Dimension maximumLayoutSize(Container target) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public float getLayoutAlignmentX(Container parent) {
            return 0.5f;
        }

        public float getLayoutAlignmentY(Container parent) {
            return 0.5f;
        }

        public void invalidateLayout(Container target) {
        }

        public void layoutContainer(Container target) {
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                int top = insets.top;
                int bottom = target.getHeight() - insets.bottom;
                int left = insets.left;
                int right = target.getWidth() - insets.right;


                if (component != null) {
                    component.setBounds(left, top, right - left, bottom - top);
                }
            }
        }
    }
}
