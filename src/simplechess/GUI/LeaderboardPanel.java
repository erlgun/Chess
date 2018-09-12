/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplechess.GUI;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import simplechess.rating.Userlist;

public class LeaderboardPanel extends JPanel {

    private JTable rankingtable;
    JButton btnBack,btnSaveAsHtml;


    /**
     * Create the panel.
     */
    public LeaderboardPanel(Userlist userlist) {
        
        
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{5, 0, 5, 0};
        gridBagLayout.rowHeights = new int[]{5, 0, 0, 0, 5, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JLabel lblRankings = new JLabel("Rankings");
        lblRankings.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblRankings = new GridBagConstraints();
        gbc_lblRankings.insets = new Insets(0, 0, 5, 5);
        gbc_lblRankings.gridx = 1;
        gbc_lblRankings.gridy = 1;
        add(lblRankings, gbc_lblRankings);

        rankingtable = new JTable(userlist.createScoreBoard(null),userlist.getColumnNames());
        rankingtable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagConstraints gbc_rankingstable = new GridBagConstraints();
        gbc_rankingstable.insets = new Insets(0, 0, 5, 5);
        gbc_rankingstable.fill = GridBagConstraints.BOTH;
        gbc_rankingstable.gridx = 1;
        gbc_rankingstable.gridy = 2;
        add(rankingtable, gbc_rankingstable);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 3;
        add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panel.rowHeights = new int[]{0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        btnBack = new JButton("Back to main menu");
        GridBagConstraints gbc_btnBack = new GridBagConstraints();
        gbc_btnBack.insets = new Insets(0, 0, 0, 5);
        gbc_btnBack.gridx = 0;
        gbc_btnBack.gridy = 0;
        panel.add(btnBack, gbc_btnBack);

        btnSaveAsHtml = new JButton("Export as HTML...");
        GridBagConstraints gbc_btnSaveAsHtml = new GridBagConstraints();
        gbc_btnSaveAsHtml.gridx = 2;
        gbc_btnSaveAsHtml.gridy = 0;
        panel.add(btnSaveAsHtml, gbc_btnSaveAsHtml);

    }
}
