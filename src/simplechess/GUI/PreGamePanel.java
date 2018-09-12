/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplechess.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Maudal
 */
public class PreGamePanel extends JPanel {

    JTextField whitetxtfield;
    JTextField blacktxtfield;
    JTextField txtsecondsperturn;
    JButton btnSendInfoAndStart;
    JCheckBox chckbxUnlimitedtimebox;

    public PreGamePanel(int dt) {

        int turnduration = dt;


        setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gbl_contentPane);

        JLabel lblWelcomeTo = new JLabel("Choose your destiny!");
        lblWelcomeTo.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblWelcomeTo = new GridBagConstraints();
        gbc_lblWelcomeTo.insets = new Insets(0, 0, 5, 0);
        gbc_lblWelcomeTo.gridx = 0;
        gbc_lblWelcomeTo.gridy = 1;
        add(lblWelcomeTo, gbc_lblWelcomeTo);

        JPanel player1info = new JPanel();
        GridBagConstraints gbc_player1info = new GridBagConstraints();
        gbc_player1info.insets = new Insets(0, 0, 5, 0);
        gbc_player1info.fill = GridBagConstraints.BOTH;
        gbc_player1info.gridx = 0;
        gbc_player1info.gridy = 2;
        add(player1info, gbc_player1info);
        GridBagLayout gbl_player1info = new GridBagLayout();
        gbl_player1info.columnWidths = new int[]{0, 0, 0};
        gbl_player1info.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_player1info.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_player1info.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        player1info.setLayout(gbl_player1info);

        JLabel lblPlayerName = new JLabel("Player 1 name (white):");
        GridBagConstraints gbc_lblPlayerName = new GridBagConstraints();
        gbc_lblPlayerName.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlayerName.anchor = GridBagConstraints.EAST;
        gbc_lblPlayerName.gridx = 0;
        gbc_lblPlayerName.gridy = 0;
        player1info.add(lblPlayerName, gbc_lblPlayerName);

        whitetxtfield = new JTextField();
        GridBagConstraints gbc_whitetxtfield = new GridBagConstraints();
        gbc_whitetxtfield.anchor = GridBagConstraints.WEST;
        gbc_whitetxtfield.insets = new Insets(0, 0, 5, 0);
        gbc_whitetxtfield.gridx = 1;
        gbc_whitetxtfield.gridy = 0;
        player1info.add(whitetxtfield, gbc_whitetxtfield);
        whitetxtfield.setColumns(10);

        JLabel lblPlayerName_1 = new JLabel("Player 2 name (black):");
        GridBagConstraints gbc_lblPlayerName_1 = new GridBagConstraints();
        gbc_lblPlayerName_1.anchor = GridBagConstraints.EAST;
        gbc_lblPlayerName_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlayerName_1.gridx = 0;
        gbc_lblPlayerName_1.gridy = 1;
        player1info.add(lblPlayerName_1, gbc_lblPlayerName_1);

        blacktxtfield = new JTextField();
        GridBagConstraints gbc_blacktextfield = new GridBagConstraints();
        gbc_blacktextfield.anchor = GridBagConstraints.WEST;
        gbc_blacktextfield.insets = new Insets(0, 0, 5, 0);
        gbc_blacktextfield.gridx = 1;
        gbc_blacktextfield.gridy = 1;
        player1info.add(blacktxtfield, gbc_blacktextfield);
        blacktxtfield.setColumns(10);

        JLabel lblGameOptions = new JLabel("Game options:");
        lblGameOptions.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblGameOptions = new GridBagConstraints();
        gbc_lblGameOptions.anchor = GridBagConstraints.EAST;
        gbc_lblGameOptions.insets = new Insets(0, 0, 5, 5);
        gbc_lblGameOptions.gridx = 0;
        gbc_lblGameOptions.gridy = 2;
        player1info.add(lblGameOptions, gbc_lblGameOptions);

        JLabel lblSecondsPerTurn = new JLabel("Seconds per turn:");
        GridBagConstraints gbc_lblSecondsPerTurn = new GridBagConstraints();
        gbc_lblSecondsPerTurn.insets = new Insets(0, 0, 5, 5);
        gbc_lblSecondsPerTurn.anchor = GridBagConstraints.EAST;
        gbc_lblSecondsPerTurn.gridx = 0;
        gbc_lblSecondsPerTurn.gridy = 3;
        player1info.add(lblSecondsPerTurn, gbc_lblSecondsPerTurn);

        txtsecondsperturn = new JTextField();
        txtsecondsperturn.setText("e.g "+turnduration+" (default)");
        GridBagConstraints gbc_txtsecondsperturn = new GridBagConstraints();
        gbc_txtsecondsperturn.anchor = GridBagConstraints.WEST;
        gbc_txtsecondsperturn.insets = new Insets(0, 0, 5, 0);
        gbc_txtsecondsperturn.gridx = 1;
        gbc_txtsecondsperturn.gridy = 3;
        player1info.add(txtsecondsperturn, gbc_txtsecondsperturn);
        txtsecondsperturn.setColumns(10);

        JLabel lblUnlimitedTimePer = new JLabel("Unlimited time per turn:");
        GridBagConstraints gbc_lblUnlimitedTimePer = new GridBagConstraints();
        gbc_lblUnlimitedTimePer.anchor = GridBagConstraints.EAST;
        gbc_lblUnlimitedTimePer.insets = new Insets(0, 0, 0, 5);
        gbc_lblUnlimitedTimePer.gridx = 0;
        gbc_lblUnlimitedTimePer.gridy = 4;
        player1info.add(lblUnlimitedTimePer, gbc_lblUnlimitedTimePer);

        chckbxUnlimitedtimebox = new JCheckBox("");
        GridBagConstraints gbc_chckbxUnlimitedtimebox = new GridBagConstraints();
        gbc_chckbxUnlimitedtimebox.anchor = GridBagConstraints.WEST;
        gbc_chckbxUnlimitedtimebox.gridx = 1;
        gbc_chckbxUnlimitedtimebox.gridy = 4;
        player1info.add(chckbxUnlimitedtimebox, gbc_chckbxUnlimitedtimebox);

        btnSendInfoAndStart = new JButton("Fight!");
        GridBagConstraints gbc_btnSendInfoAndStart = new GridBagConstraints();
        gbc_btnSendInfoAndStart.gridx = 0;
        gbc_btnSendInfoAndStart.gridy = 3;
        add(btnSendInfoAndStart, gbc_btnSendInfoAndStart);
        
        chckbxUnlimitedtimebox.setOpaque(false);
        player1info.setOpaque(false);
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
