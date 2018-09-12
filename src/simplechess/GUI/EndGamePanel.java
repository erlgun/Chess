/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplechess.GUI;

import java.awt.*;
import simplechess.rating.Chessuser;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import java.awt.event.*;

public class EndGamePanel extends JPanel {

    /**
     * Create the panel.
     */
    private static final String WINNERTEXT = " has won the game!";
    private static final String DRAWTEXT = "You are equally bad or equally good!";
    private String whiteplayer, blackplayer;
    private JLabel lblWhiteplayer,lblBlackplayer;
    public JButton btnNewGame, btnMainMenu, btnQuitSimpleChess;
    private String winner;
    
    
    public EndGamePanel( Chessuser w, Chessuser b, String wnr) {
        
        whiteplayer = w.getUserName();
        blackplayer = b.getUserName();
        if(wnr == null)
            winner = DRAWTEXT;
        else
            winner = wnr;
        
        
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{10, 0, 10, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JPanel toppanel = new JPanel();
        GridBagConstraints gbc_toppanel = new GridBagConstraints();
        gbc_toppanel.insets = new Insets(0, 0, 5, 5);
        gbc_toppanel.fill = GridBagConstraints.BOTH;
        gbc_toppanel.gridx = 1;
        gbc_toppanel.gridy = 1;
        add(toppanel, gbc_toppanel);
        GridBagLayout gbl_toppanel = new GridBagLayout();
        gbl_toppanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_toppanel.rowHeights = new int[]{0, 0, 0, 0};
        gbl_toppanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_toppanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        toppanel.setLayout(gbl_toppanel);
        
        JLabel lblWinnerlabel = new JLabel("winnerlabel");
        lblWinnerlabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        GridBagConstraints gbc_lblWinnerlabel = new GridBagConstraints();
        gbc_lblWinnerlabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblWinnerlabel.gridx = 1;
        gbc_lblWinnerlabel.gridy = 1;
        toppanel.add(lblWinnerlabel, gbc_lblWinnerlabel);
        
        lblWhiteplayer = new JLabel(whiteplayer);
        lblWhiteplayer.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblWhiteplayer = new GridBagConstraints();
        gbc_lblWhiteplayer.insets = new Insets(0, 0, 0, 5);
        gbc_lblWhiteplayer.gridx = 0;
        gbc_lblWhiteplayer.gridy = 2;
        toppanel.add(lblWhiteplayer, gbc_lblWhiteplayer);
        
        lblBlackplayer = new JLabel(blackplayer);
        lblBlackplayer.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblBlackplayer = new GridBagConstraints();
        gbc_lblBlackplayer.gridx = 2;
        gbc_lblBlackplayer.gridy = 2;
        toppanel.add(lblBlackplayer, gbc_lblBlackplayer);
        
        JPanel bottompanel = new JPanel();
        GridBagConstraints gbc_bottompanel = new GridBagConstraints();
        gbc_bottompanel.insets = new Insets(0, 0, 5, 5);
        gbc_bottompanel.fill = GridBagConstraints.BOTH;
        gbc_bottompanel.gridx = 1;
        gbc_bottompanel.gridy = 2;
        add(bottompanel, gbc_bottompanel);
        GridBagLayout gbl_bottompanel = new GridBagLayout();
        gbl_bottompanel.columnWidths = new int[]{0, 0, 0};
        gbl_bottompanel.rowHeights = new int[]{0, 0};
        gbl_bottompanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_bottompanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        bottompanel.setLayout(gbl_bottompanel);
        
        JPanel whitepanel = new JPanel();
        whitepanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagConstraints gbc_whitepanel = new GridBagConstraints();
        gbc_whitepanel.insets = new Insets(0, 0, 0, 5);
        gbc_whitepanel.fill = GridBagConstraints.BOTH;
        gbc_whitepanel.gridx = 0;
        gbc_whitepanel.gridy = 0;
        bottompanel.add(whitepanel, gbc_whitepanel);
        GridBagLayout gbl_whitepanel = new GridBagLayout();
        gbl_whitepanel.columnWidths = new int[]{0, 0};
        gbl_whitepanel.rowHeights = new int[]{0, 0};
        gbl_whitepanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_whitepanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        whitepanel.setLayout(gbl_whitepanel);
        
        JTextArea txtAreaWhite = new JTextArea();
        GridBagConstraints gbc_txtAreaWhite = new GridBagConstraints();
        gbc_txtAreaWhite.fill = GridBagConstraints.BOTH;
        gbc_txtAreaWhite.gridx = 0;
        gbc_txtAreaWhite.gridy = 0;
        whitepanel.add(txtAreaWhite, gbc_txtAreaWhite);
        
        JPanel blackpanel = new JPanel();
        blackpanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagConstraints gbc_blackpanel = new GridBagConstraints();
        gbc_blackpanel.fill = GridBagConstraints.BOTH;
        gbc_blackpanel.gridx = 1;
        gbc_blackpanel.gridy = 0;
        bottompanel.add(blackpanel, gbc_blackpanel);
        GridBagLayout gbl_blackpanel = new GridBagLayout();
        gbl_blackpanel.columnWidths = new int[]{0, 0};
        gbl_blackpanel.rowHeights = new int[]{0, 0};
        gbl_blackpanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_blackpanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        blackpanel.setLayout(gbl_blackpanel);
        
        JTextArea txtAreaBlack = new JTextArea();
        GridBagConstraints gbc_textAreaBlack = new GridBagConstraints();
        gbc_textAreaBlack.fill = GridBagConstraints.BOTH;
        gbc_textAreaBlack.gridx = 0;
        gbc_textAreaBlack.gridy = 0;
        blackpanel.add(txtAreaBlack, gbc_textAreaBlack);
        
        JPanel buttonpanel = new JPanel();
        GridBagConstraints gbc_buttonpanel = new GridBagConstraints();
        gbc_buttonpanel.insets = new Insets(0, 0, 5, 5);
        gbc_buttonpanel.fill = GridBagConstraints.BOTH;
        gbc_buttonpanel.gridx = 1;
        gbc_buttonpanel.gridy = 3;
        add(buttonpanel, gbc_buttonpanel);
        GridBagLayout gbl_buttonpanel = new GridBagLayout();
        gbl_buttonpanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_buttonpanel.rowHeights = new int[]{0, 0};
        gbl_buttonpanel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_buttonpanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        buttonpanel.setLayout(gbl_buttonpanel);
        
        btnNewGame = new JButton("New game");
        GridBagConstraints gbc_btnNewGame = new GridBagConstraints();
        gbc_btnNewGame.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewGame.gridx = 0;
        gbc_btnNewGame.gridy = 0;
        buttonpanel.add(btnNewGame, gbc_btnNewGame);
        
        btnMainMenu = new JButton("Main menu");
        GridBagConstraints gbc_btnMainMenu = new GridBagConstraints();
        gbc_btnMainMenu.insets = new Insets(0, 0, 0, 5);
        gbc_btnMainMenu.gridx = 1;
        gbc_btnMainMenu.gridy = 0;
        buttonpanel.add(btnMainMenu, gbc_btnMainMenu);
        
        btnQuitSimpleChess = new JButton("Quit Simple Chess");
        GridBagConstraints gbc_btnQuitSimpleChess = new GridBagConstraints();
        gbc_btnQuitSimpleChess.gridx = 2;
        gbc_btnQuitSimpleChess.gridy = 0;
        buttonpanel.add(btnQuitSimpleChess, gbc_btnQuitSimpleChess);
        
        if(wnr == null)
            lblWinnerlabel.setText(winner);
        else
            lblWinnerlabel.setText(winner + WINNERTEXT);
        
        
        
        txtAreaWhite.setText(whiteplayer.toString());
        txtAreaBlack.setText(blackplayer.toString());
        
        whitepanel.setOpaque(false);
        blackpanel.setOpaque(false);
        buttonpanel.setOpaque(false);
        setOpaque(false);
        setVisible(true);
        
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
