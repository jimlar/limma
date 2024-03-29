package limma.ui;

import org.jdesktop.animation.timing.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SlidePanel extends JPanel {
    private boolean isOut = false;
    private JComponent slideComponent;
    private TimingController lastTimer;

    public SlidePanel() {
        setLayout(new SlideLayout());
        setOpaque(false);
    }

    public void slideIn(JComponent component) {
        if (component != slideComponent) {
            this.slideComponent = component;
            removeAll();
            slideComponent.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.black, 1), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            slideComponent.setBackground(new Color(255, 255, 255, 128));
            slideComponent.setOpaque(true);
            slideComponent.doLayout();
            slideComponent.setLocation(0, (int) slideComponent.getPreferredSize().getHeight());
            add(slideComponent);
        }
        slideIn();
    }

    public void slideOut() {
        if (!isOut) {
            isOut = true;
            slide(true);
        }
    }

    public void slideIn() {
        if (isOut) {
            isOut = false;
            slide(false);
        }
    }

    private void slide(boolean out) {
        if (slideComponent == null) {
            return;
        }

        if (lastTimer != null) {
            lastTimer.stop();
        }

        Cycle cycle = new Cycle(500, 10);
        Envelope envelope = new Envelope(1,
                                         0,
                                         Envelope.RepeatBehavior.FORWARD,
                                         Envelope.EndBehavior.HOLD);
        Point p0 = new Point(0, 0);
        int height = getHeight();
        Point p1 = new Point(0, height);
        PropertyRange range;
        if (out) {
            range = PropertyRange.createPropertyRangePoint("location", p0, p1);
        } else {
            range = PropertyRange.createPropertyRangePoint("location", p1, p0);
        }
        lastTimer = new TimingController(cycle, envelope, new ObjectModifier(slideComponent, range));
        lastTimer.setAcceleration(1.0f);
        lastTimer.start();
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
                if (component != null) {
                    component.setSize(target.getWidth(), target.getHeight());
                }
            }
        }
    }
}
