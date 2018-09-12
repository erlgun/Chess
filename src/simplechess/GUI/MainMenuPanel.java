/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplechess.GUI;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Maudal
 */
public class MainMenuPanel extends JPanel {

    private static final String LOGOPATH = "simplechess/art/logo.png";
    protected JButton btnNewGame, btnLeaderboard, btnQuit;
    private JLabel lblLogolabel;
    
    public MainMenuPanel() {
        
        //<editor-fold defaultstate="collapsed" desc="GUI">
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);


        lblLogolabel = new JLabel("");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 1;
        gbc_label.gridy = 1;
        add(lblLogolabel, gbc_label);

        JLabel lblTitle = new JLabel("Simple Chess");
        GridBagConstraints gbc_lblLogolabel = new GridBagConstraints();
        gbc_lblLogolabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblLogolabel.gridx = 1;
        gbc_lblLogolabel.gridy = 2;
        add(lblTitle, gbc_lblLogolabel);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));

        JPanel buttonpanel = new JPanel();
        GridBagConstraints gbc_buttonpanel = new GridBagConstraints();
        gbc_buttonpanel.insets = new Insets(0, 0, 5, 5);
        gbc_buttonpanel.fill = GridBagConstraints.BOTH;
        gbc_buttonpanel.gridx = 1;
        gbc_buttonpanel.gridy = 3;
        add(buttonpanel, gbc_buttonpanel);
        GridBagLayout gbl_buttonpanel = new GridBagLayout();
        gbl_buttonpanel.columnWidths = new int[]{0, 0};
        gbl_buttonpanel.rowHeights = new int[]{0, 0, 0, 0};
        gbl_buttonpanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_buttonpanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        buttonpanel.setLayout(gbl_buttonpanel);

        btnNewGame = new JButton("New game");
        GridBagConstraints gbc_btnNewGame = new GridBagConstraints();
        gbc_btnNewGame.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewGame.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewGame.gridx = 0;
        gbc_btnNewGame.gridy = 0;
        buttonpanel.add(btnNewGame, gbc_btnNewGame);

        btnLeaderboard = new JButton("Rankings");
        GridBagConstraints gbc_button_1 = new GridBagConstraints();
        gbc_button_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_button_1.insets = new Insets(0, 0, 5, 0);
        gbc_button_1.gridx = 0;
        gbc_button_1.gridy = 1;
        buttonpanel.add(btnLeaderboard, gbc_button_1);



        btnQuit = new JButton("Quit game");
        GridBagConstraints gbc_b_quitgame = new GridBagConstraints();
        gbc_b_quitgame.fill = GridBagConstraints.HORIZONTAL;
        gbc_b_quitgame.gridx = 0;
        gbc_b_quitgame.gridy = 2;
        buttonpanel.add(btnQuit, gbc_b_quitgame);

        //</editor-fold>
        
        //
        
        
        ImageIcon icon = GUI.resourceIcon(LOGOPATH);
        if (icon != null) lblLogolabel.setIcon(icon);
        
        buttonpanel.setOpaque(false);
        setOpaque(false);
        
    }
    
    // Shamelessly stolen and refitted from
    // http://www.javarichclient.com/how-to-add-a-gradient-background-to-any-jcomponent/ 
    @Override
    protected void paintComponent(Graphics grp) {
        Graphics2D g2d = (Graphics2D) grp;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0,
                getBackground().brighter().brighter(), 0, getHeight(),
                getBackground().darker().darker());

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(grp);
    }
    
}
