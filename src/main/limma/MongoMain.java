package limma;

import javax.swing.*;

public class MongoMain {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);

        JDesktopPane desktop = new JDesktopPane();
        JLabel label = new JLabel("Murkla");
        label.setSize(200, 20);
        desktop.add(label, new Integer(Integer.MIN_VALUE));
        frame.setContentPane(desktop);

        JInternalFrame internalFrame = new JInternalFrame("Hej hopp gummisnopp");
        desktop.add(internalFrame);

        internalFrame.setSize(200, 200);
        internalFrame.add(new JLabel("Purkla..."));
        internalFrame.show();

//        desktop.repaint();
    }
}
