package aplikacja.projekt;

import javax.swing.*;
import java.awt.*;

public class Window{

    private JFrame jFrame;




    public Window() {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jFrame.add(getJPanel());
        jFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        jFrame.setVisible(true);
    }

    private JPanel jPanel;
    private JPanel jPanelConvo;
    private TextArea textArea;
    private JScrollPane jScrollPane;

    private  JPanel getJPanel(){
        jPanel = new JPanel();

        jPanel.add(getJScrollPane());

        return jPanel;
    }

    private JScrollPane getJScrollPane(){
        jScrollPane = new JScrollPane(getJPanelConvo());

        return jScrollPane;
    }

    private  JPanel getJPanelConvo(){
        jPanelConvo = new JPanel();
        jPanelConvo.add(getTextArea());


        return jPanelConvo;
    }

    private TextArea getTextArea(){
        textArea = new TextArea("enter text here");
        textArea.setRows(1);

        return textArea;
    }
}
