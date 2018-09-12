/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplechess.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import javax.swing.*;
import simplechess.rating.Userlist;

/**
 *
 * @author Maudal
 */
public class GUI extends JFrame {

    private static final String TITLE = "Simple Chess";
    private static final int STARTUPFRAME_WIDTH = 248;
    private static final int STARTUPFRAME_HEIGHT = 390;
    private static final int PREGAMEFRAME_WIDTH = 368;
    private static final int PREGAMEFRAME_HEIGHT = 248;
    private static final int GAMEFRAME_WIDTH = 800;
    private static final int GAMEFRAME_HEIGHT = 700;
    private static final int ENDFRAME_WIDTH = 600;
    private static final int ENDFRAME_HEIGHT = 300;
    private static final int LEADERFRAME_WIDTH = 500;
    private static final int LEADERFRAME_HEIGHT = 400;
    private static final String TEXTFIELD_ERROR = "Please enter a number";
    private static final int DEFAULT_TURNDURATION = 30;
    private GamePanel gamepanel;
    private Boolean infinite_time = false;
    private PreGamePanel preGamePanel;
    private int turnduration = 0;
    protected MainMenuListener mainMenuListener;
    private EndGamePanel endGamePanel;
    private MainMenuPanel mainMenuPanel;
    protected Userlist userlist;
    private EndGamePanelListener endGamePanelListener;
    private LeaderboardPanelListener leaderboardPanelListener;
    private LeaderboardPanel leaderboardPanel;
    private PreGamePanelListener preGamePanelListener;

    public GUI() {

        super(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Make closing procedure instead of this

        userlist = new Userlist();
        endGamePanelListener = new EndGamePanelListener();
        mainMenuListener = new MainMenuListener();
        preGamePanelListener = new PreGamePanelListener();
        leaderboardPanelListener = new LeaderboardPanelListener();
        newMainMenu();


        createAndSetImages();
    }

    

    // Initialises images and sets images
    private void createAndSetImages() {

        /*
         * iconimage = new ImageIcon(getClass().getResource(ICONPATH)); Image
         * icon = iconimage.getImage(); setIconImage(icon);
         */
    }

    // Receives userinput, creates the gamepanel
    // Sets JTextfield texts to "" for startup
    // Adds listener to the panel to listen for when the game is ended
    public void newGame(String p1, String p2, int t, Boolean inf) {

        gamepanel = new GamePanel(p1, p2, t, userlist, inf);

        Component comp[] = gamepanel.getComponents();
        for (Component c : comp) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            }

            setContentPane(gamepanel);
            setSize(GAMEFRAME_WIDTH, GAMEFRAME_HEIGHT);
            setLocationRelativeTo(null);
            setResizable(true);

            addPanelListeners();


        }
    }
    // Creates and shows the main menu

    private void newMainMenu() {

        mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.btnNewGame.addActionListener(mainMenuListener);
        mainMenuPanel.btnLeaderboard.addActionListener(mainMenuListener);
        mainMenuPanel.btnQuit.addActionListener(mainMenuListener);
        setSize(STARTUPFRAME_WIDTH, STARTUPFRAME_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setContentPane(mainMenuPanel);

    }
    // Creates and show the end game screen

    private void newEndGame() {

        endGamePanel = gamepanel.getEndGamePanel();
        endGamePanel.btnNewGame.addActionListener(endGamePanelListener);
        endGamePanel.btnMainMenu.addActionListener(endGamePanelListener);
        endGamePanel.btnQuitSimpleChess.addActionListener(endGamePanelListener);

        setContentPane(endGamePanel);
        setLocationRelativeTo(null);
        setSize(ENDFRAME_WIDTH, ENDFRAME_HEIGHT);

    }

    // Creates and sets the contentpane to the pregamepanel
    private void newPreGame() {

        preGamePanel = new PreGamePanel(DEFAULT_TURNDURATION);
        preGamePanel.btnSendInfoAndStart.addActionListener(preGamePanelListener);
        preGamePanel.txtsecondsperturn.addActionListener(preGamePanelListener);
        setContentPane(preGamePanel);
        setSize(PREGAMEFRAME_WIDTH, PREGAMEFRAME_HEIGHT);
        setLocationRelativeTo(null);

    }

    private void newLeaderboard() {

        leaderboardPanel = new LeaderboardPanel(userlist);
        leaderboardPanel.btnBack.addActionListener(leaderboardPanelListener);
        leaderboardPanel.btnSaveAsHtml.addActionListener(leaderboardPanelListener);
        setSize(LEADERFRAME_WIDTH, LEADERFRAME_HEIGHT);
        setContentPane(leaderboardPanel);

    }

    // Fetches userinput info and passes it to newGame()
    private void sendInfoAndStart() {

        String w = preGamePanel.whitetxtfield.getText();
        String b = preGamePanel.blacktxtfield.getText();

        if (preGamePanel.chckbxUnlimitedtimebox.isSelected() == false) {
            if (preGamePanel.txtsecondsperturn.getText().equals("e.g " + DEFAULT_TURNDURATION + " (default)")) {
                turnduration = DEFAULT_TURNDURATION;
            } else {
                try {
                    turnduration = Integer.parseInt(preGamePanel.txtsecondsperturn.getText());
                } catch (Exception ex) {
                    preGamePanel.txtsecondsperturn.setText(TEXTFIELD_ERROR);
                }
            }

        } else {
            infinite_time = true;

        }
        newGame(w, b, turnduration, infinite_time);

    }

    // Listener class for the main menu
    private class MainMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == mainMenuPanel.btnNewGame) {

                newPreGame();

            } else if (e.getSource() == mainMenuPanel.btnLeaderboard) {

                newLeaderboard();

            } else if (e.getSource() == mainMenuPanel.btnQuit) {
                System.exit(0);
            }


        }
    }

    private class PreGamePanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == preGamePanel.btnSendInfoAndStart) {
                sendInfoAndStart();

                if (preGamePanel.chckbxUnlimitedtimebox.isSelected() == true) {
                    preGamePanel.txtsecondsperturn.setEnabled(false);
                }


            }
        }
    }

    // Listener class for the leaderboard
    private class LeaderboardPanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == leaderboardPanel.btnBack) {

                setContentPane(mainMenuPanel);

            } else if (e.getSource() == leaderboardPanel.btnSaveAsHtml) {
                // TODO: add procedure
            }
        }
    }

    // Adds listeners to the endgame panel 
    private class EndGamePanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {



            if (e.getSource() == endGamePanel.btnNewGame) {

                newPreGame();

            } else if (e.getSource() == endGamePanel.btnMainMenu) {

                setContentPane(mainMenuPanel);

            } else if (e.getSource() == endGamePanel.btnQuitSimpleChess) {
                System.exit(0);
            } else if (e.getSource() == preGamePanel.txtsecondsperturn) {
                if (preGamePanel.chckbxUnlimitedtimebox.isSelected() == true) {
                    preGamePanel.txtsecondsperturn.setEnabled(false);
                }
            }
        }
    }
    // Adds listener to the gamepanel to listen for when the game is ended

    private void addPanelListeners() {

        gamepanel.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
                newEndGame();

            }
        });

    }

    // loads icon from resources
    // returns null if no image was found
    public static ImageIcon resourceIcon(String fname) {

        URL url = Thread.currentThread().getContextClassLoader().getResource(fname);
        if (url == null) {
            JOptionPane.showMessageDialog(null, "Fant ikke fil: " + fname);
            return null;
        }
        return new ImageIcon(url);

    }
}
