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
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import simplechess.board.ChessBoard;
import simplechess.board.ChessPiece;
import simplechess.rating.Chessuser;
import simplechess.rating.Userlist;
import simplechess.sound.Sound;

/**
 *
 * @author Maudal
 */
public class GamePanel extends JPanel {

    private static final int MS_PER_UPDATE = 1000;
    private static final int PROGRESSBAR_RED = 25;
    private static final int MAX = 2147483647;
    private static final int MINUTE = 60;
    private static final String LOGOPATH = "simplechess/art/logo.png";
    private static final String WHITE = "White";
    private static final String BLACK = "Black";
    private static final String PATH_SND_WARNING = "simplechess/sound/warning.wav";
    private static final String pausesymbol = " || ";
    private static final String resumesymbol = " ► ";
    private static final String INFINITE = " ∞ ";
    private int seconds, pauseresumecounter, turncounter,
            turnduration, blackturntime, whiteturntime, currentturnseconds, drawbuttoncounter;
    private Timer maintimer, turntimer;
    private Timerlistener timerlistener;
    private String white = "White";
    private String black = "Black";
    private JLabel lblWhite_gamestate, lblBlack_gamestate, timerlabel, lblCurrentTurnTime,
            lblPlayersTurn, lblWhite_turn, lblBlack_turn, lblWhitetotalturnleft,
            lblTurnTimeLeft, lblBlacktotalturnleft;
    private String currentplayerstring;
    private JButton btnPauseResume, btnResign, btnDraw;
    private ButtonListener buttonlistener;
    private JPanel chesspanel;
    private Canvas gamecanvas;
    private ChessBoard chessboard;
    private ImageIcon logoimage;
    private JProgressBar timerbar;
    private Userlist userlist;
    private Chessuser whiteplayer, blackplayer;
    private Sound sndWarning;
    private EndGamePanel endgamepanel;
    private Boolean infinitetime = false;

    public GamePanel(String w, String b, int t, Userlist u, Boolean i) {


        if ((w).toUpperCase().equals((b).toUpperCase())) {
            b = BLACK;
        }

        whiteplayer = new Chessuser(w, null);
        blackplayer = new Chessuser(b, null);
        buttonlistener = new ButtonListener();
        timerlistener = new Timerlistener();
        Turnender turn = new Turnender();
        userlist = u;
        infinitetime = i;
        if (i == true) {
            t = MAX;
        }


        userlist.insert(whiteplayer);
        userlist.insert(blackplayer);

        white = whiteplayer.getUserName();
        black = blackplayer.getUserName();

        turnduration = t;
        blackturntime = t;
        whiteturntime = t;
        logoimage = GUI.resourceIcon(LOGOPATH);

        setLayout(new BorderLayout());
        //setBackground(new Color(64, 32, 32));

        chesspanel = new JPanel(new BorderLayout());
        add(chesspanel, "Center");
        chessboard = new ChessBoard(turn);
        chessboard.newBoard();
        gamecanvas = chessboard.getCanvas();
        chesspanel.add(gamecanvas, "Center");
        chesspanel.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                chessboard.resize(gamecanvas.getBounds());
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });


        //<editor-fold defaultstate="collapsed" desc="Sidepanel">
        JPanel Sidepanel = new JPanel();
        add(Sidepanel, BorderLayout.EAST);
        GridBagLayout gbl_Sidepanel = new GridBagLayout();
        gbl_Sidepanel.columnWidths = new int[]{7, 0, 2, 0};
        gbl_Sidepanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gbl_Sidepanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_Sidepanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        Sidepanel.setLayout(gbl_Sidepanel);

        JLabel lblLogolabel = new JLabel("");
        lblLogolabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogolabel.setIcon(logoimage);
        GridBagConstraints gbc_lblLogolabel = new GridBagConstraints();
        gbc_lblLogolabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblLogolabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblLogolabel.gridx = 1;
        gbc_lblLogolabel.gridy = 0;
        Sidepanel.add(lblLogolabel, gbc_lblLogolabel);

        JPanel gamestatepanel = new JPanel();
        GridBagConstraints gbc_gamestatepanel = new GridBagConstraints();
        gbc_gamestatepanel.insets = new Insets(0, 0, 5, 5);
        gbc_gamestatepanel.fill = GridBagConstraints.BOTH;
        gbc_gamestatepanel.gridx = 1;
        gbc_gamestatepanel.gridy = 1;
        Sidepanel.add(gamestatepanel, gbc_gamestatepanel);
        GridBagLayout gbl_gamestatepanel = new GridBagLayout();
        gbl_gamestatepanel.columnWidths = new int[]{0, 0};
        gbl_gamestatepanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        gbl_gamestatepanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_gamestatepanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gamestatepanel.setLayout(gbl_gamestatepanel);

        JLabel lblSimpleChess = new JLabel("Simple Chess:");
        lblSimpleChess.setFont(new Font("Tahoma", Font.BOLD, 14));
        GridBagConstraints gbc_lblSimpleChess = new GridBagConstraints();
        gbc_lblSimpleChess.insets = new Insets(0, 0, 5, 0);
        gbc_lblSimpleChess.gridx = 0;
        gbc_lblSimpleChess.gridy = 0;
        gamestatepanel.add(lblSimpleChess, gbc_lblSimpleChess);

        lblWhite_gamestate = new JLabel("Player 1");
        GridBagConstraints gbc_lblWhite = new GridBagConstraints();
        gbc_lblWhite.insets = new Insets(0, 0, 5, 0);
        gbc_lblWhite.gridx = 0;
        gbc_lblWhite.gridy = 1;
        gamestatepanel.add(lblWhite_gamestate, gbc_lblWhite);

        JLabel lblVs = new JLabel("vs.");
        GridBagConstraints gbc_lblVs = new GridBagConstraints();
        gbc_lblVs.insets = new Insets(0, 0, 5, 0);
        gbc_lblVs.gridx = 0;
        gbc_lblVs.gridy = 2;
        gamestatepanel.add(lblVs, gbc_lblVs);

        lblBlack_gamestate = new JLabel("Player 2");
        GridBagConstraints gbc_lblBlack = new GridBagConstraints();
        gbc_lblBlack.insets = new Insets(0, 0, 5, 0);
        gbc_lblBlack.gridx = 0;
        gbc_lblBlack.gridy = 3;
        gamestatepanel.add(lblBlack_gamestate, gbc_lblBlack);

        JLabel lblTotalGameTime = new JLabel("Total game time:");
        lblTotalGameTime.setFont(new Font("Tahoma", Font.BOLD, 11));
        GridBagConstraints gbc_lblTotalGameTime = new GridBagConstraints();
        gbc_lblTotalGameTime.insets = new Insets(0, 0, 5, 0);
        gbc_lblTotalGameTime.gridx = 0;
        gbc_lblTotalGameTime.gridy = 4;
        gamestatepanel.add(lblTotalGameTime, gbc_lblTotalGameTime);

        timerlabel = new JLabel();
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.gridx = 0;
        gbc_label.gridy = 5;
        gamestatepanel.add(timerlabel, gbc_label);

        JLabel lblTotalTurnTime = new JLabel("Total player turn time left:");
        lblTotalTurnTime.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblTotalTurnTime = new GridBagConstraints();
        gbc_lblTotalTurnTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblTotalTurnTime.gridx = 1;
        gbc_lblTotalTurnTime.gridy = 2;
        Sidepanel.add(lblTotalTurnTime, gbc_lblTotalTurnTime);

        JPanel totalturntimeleft = new JPanel();
        GridBagConstraints gbc_totalturntimeleft = new GridBagConstraints();
        gbc_totalturntimeleft.insets = new Insets(0, 0, 5, 5);
        gbc_totalturntimeleft.fill = GridBagConstraints.BOTH;
        gbc_totalturntimeleft.gridx = 1;
        gbc_totalturntimeleft.gridy = 3;
        Sidepanel.add(totalturntimeleft, gbc_totalturntimeleft);
        GridBagLayout gbl_totalturntimeleft = new GridBagLayout();
        gbl_totalturntimeleft.columnWidths = new int[]{0, 0, 0};
        gbl_totalturntimeleft.rowHeights = new int[]{0, 0, 0};
        gbl_totalturntimeleft.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gbl_totalturntimeleft.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        totalturntimeleft.setLayout(gbl_totalturntimeleft);

        lblWhite_turn = new JLabel("Player 1");
        lblWhite_turn.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblWhite1 = new GridBagConstraints();
        gbc_lblWhite1.insets = new Insets(0, 0, 5, 5);
        gbc_lblWhite1.gridx = 0;
        gbc_lblWhite1.gridy = 0;
        totalturntimeleft.add(lblWhite_turn, gbc_lblWhite1);

        lblBlack_turn = new JLabel("Player 2");
        lblBlack_turn.setFont(new Font("Tahoma", Font.BOLD, 12));
        GridBagConstraints gbc_lblBlack2 = new GridBagConstraints();
        gbc_lblBlack2.insets = new Insets(0, 0, 5, 0);
        gbc_lblBlack2.gridx = 1;
        gbc_lblBlack2.gridy = 0;
        totalturntimeleft.add(lblBlack_turn, gbc_lblBlack2);

        lblWhitetotalturnleft = new JLabel("13:37");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        totalturntimeleft.add(lblWhitetotalturnleft, gbc_lblNewLabel);

        lblBlacktotalturnleft = new JLabel("13:37");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.gridx = 1;
        gbc_label_1.gridy = 1;
        totalturntimeleft.add(lblBlacktotalturnleft, gbc_label_1);

        JPanel gametimepanel = new JPanel();
        gametimepanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagConstraints gbc_gametimepanel = new GridBagConstraints();
        gbc_gametimepanel.insets = new Insets(0, 0, 5, 5);
        gbc_gametimepanel.fill = GridBagConstraints.BOTH;
        gbc_gametimepanel.gridx = 1;
        gbc_gametimepanel.gridy = 4;
        Sidepanel.add(gametimepanel, gbc_gametimepanel);
        GridBagLayout gbl_gametimepanel = new GridBagLayout();
        gbl_gametimepanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_gametimepanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_gametimepanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_gametimepanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gametimepanel.setLayout(gbl_gametimepanel);

        lblTurnTimeLeft = new JLabel("Turn time left:");
        lblTurnTimeLeft.setFont(new Font("Tahoma", Font.BOLD, 14));
        GridBagConstraints gbc_lblTurnTimeLeft = new GridBagConstraints();
        gbc_lblTurnTimeLeft.insets = new Insets(0, 0, 5, 5);
        gbc_lblTurnTimeLeft.gridx = 1;
        gbc_lblTurnTimeLeft.gridy = 0;
        gametimepanel.add(lblTurnTimeLeft, gbc_lblTurnTimeLeft);

        lblCurrentTurnTime = new JLabel();
        lblCurrentTurnTime.setFont(new Font("Tahoma", Font.PLAIN, 13));
        GridBagConstraints gbc_lblTimeleft = new GridBagConstraints();
        gbc_lblTimeleft.insets = new Insets(0, 0, 5, 5);
        gbc_lblTimeleft.gridx = 1;
        gbc_lblTimeleft.gridy = 1;
        gametimepanel.add(lblCurrentTurnTime, gbc_lblTimeleft);

        timerbar = new JProgressBar(0, turnduration);
        GridBagConstraints gbc_progressBar = new GridBagConstraints();
        gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_progressBar.insets = new Insets(0, 0, 5, 5);
        gbc_progressBar.gridx = 1;
        gbc_progressBar.gridy = 2;
        gametimepanel.add(timerbar, gbc_progressBar);

        lblPlayersTurn = new JLabel("Player 1's turn");
        lblPlayersTurn.setFont(new Font("Tahoma", Font.BOLD, 14));
        GridBagConstraints gbc_lblPlayersTurn = new GridBagConstraints();
        gbc_lblPlayersTurn.insets = new Insets(0, 0, 5, 5);
        gbc_lblPlayersTurn.gridx = 1;
        gbc_lblPlayersTurn.gridy = 3;
        gametimepanel.add(lblPlayersTurn, gbc_lblPlayersTurn);

        btnPauseResume = new JButton(pausesymbol);
        GridBagConstraints gbc_btnPause = new GridBagConstraints();
        gbc_btnPause.insets = new Insets(0, 0, 0, 5);
        gbc_btnPause.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnPause.gridx = 1;
        gbc_btnPause.gridy = 4;
        gametimepanel.add(btnPauseResume, gbc_btnPause);

        JPanel movespanel = new JPanel();
        GridBagConstraints gbc_movespanel = new GridBagConstraints();
        gbc_movespanel.insets = new Insets(0, 0, 5, 5);
        gbc_movespanel.fill = GridBagConstraints.BOTH;
        gbc_movespanel.gridx = 1;
        gbc_movespanel.gridy = 5;
        Sidepanel.add(movespanel, gbc_movespanel);
        GridBagLayout gbl_movespanel = new GridBagLayout();
        gbl_movespanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_movespanel.rowHeights = new int[]{0, 0, 0, 0};
        gbl_movespanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_movespanel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        movespanel.setLayout(gbl_movespanel);

        JLabel lblMoves = new JLabel("Moves:");
        GridBagConstraints gbc_lblMoves = new GridBagConstraints();
        gbc_lblMoves.insets = new Insets(0, 0, 5, 5);
        gbc_lblMoves.gridx = 1;
        gbc_lblMoves.gridy = 0;
        movespanel.add(lblMoves, gbc_lblMoves);

        JList list = new JList();
        list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        GridBagConstraints gbc_list = new GridBagConstraints();
        gbc_list.gridheight = 2;
        gbc_list.insets = new Insets(0, 0, 0, 5);
        gbc_list.fill = GridBagConstraints.BOTH;
        gbc_list.gridx = 1;
        gbc_list.gridy = 1;
        movespanel.add(list, gbc_list);

        JPanel buttonpanel = new JPanel();
        GridBagConstraints gbc_buttonpanel = new GridBagConstraints();
        gbc_buttonpanel.insets = new Insets(0, 0, 0, 5);
        gbc_buttonpanel.fill = GridBagConstraints.BOTH;
        gbc_buttonpanel.gridx = 1;
        gbc_buttonpanel.gridy = 6;
        Sidepanel.add(buttonpanel, gbc_buttonpanel);
        GridBagLayout gbl_buttonpanel = new GridBagLayout();
        gbl_buttonpanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
        gbl_buttonpanel.rowHeights = new int[]{0, 0, 0, 0};
        gbl_buttonpanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_buttonpanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        buttonpanel.setLayout(gbl_buttonpanel);

        btnResign = new JButton("Resign");
        GridBagConstraints gbc_btnSurrender = new GridBagConstraints();
        gbc_btnSurrender.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSurrender.insets = new Insets(0, 0, 5, 5);
        gbc_btnSurrender.gridx = 2;
        gbc_btnSurrender.gridy = 0;
        buttonpanel.add(btnResign, gbc_btnSurrender);

        btnDraw = new JButton("Propose draw");
        GridBagConstraints gbc_btnOptions = new GridBagConstraints();
        gbc_btnOptions.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnOptions.insets = new Insets(0, 0, 5, 5);
        gbc_btnOptions.gridx = 2;
        gbc_btnOptions.gridy = 1;
        buttonpanel.add(btnDraw, gbc_btnOptions);

        btnPauseResume.addActionListener(buttonlistener);
        btnResign.addActionListener(buttonlistener);
        btnDraw.addActionListener(buttonlistener);
        add(Sidepanel, "East");
        //</editor-fold>


        setPlayerNames(w, b);
        timerAndGameSetup();

        Sidepanel.setOpaque(false);
        gamestatepanel.setOpaque(false);
        totalturntimeleft.setOpaque(false);
        gametimepanel.setOpaque(false);
        movespanel.setOpaque(false);
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
    // Updates the current player label

    private void updatePlayerTurnLabel(String s) {

        currentplayerstring = s;
        lblPlayersTurn.setText(currentplayerstring + "'s turn");

    }

    // Returns the int as string in clock format for jlabels that indicate time
    // Stringbuilder might be more efficient than "+="
    private String getClockFormat(int i) {

        int hours = i / 3600;
        int minutes = (i % 3600) / 60;
        int secs =  ((i % 3600) % 60);
        String parsed = "";
        
        
        
        if (minutes > 0) {
            parsed += minutes + " : ";
        } else {
            parsed += "00 : ";
        }
        if (secs != 0) {
            parsed += secs;
        } else {
            parsed += "00";
        }
        return parsed;
    }

    // Creates timers which call the actionlistener every 1 second (MS_PER_UPDATE)
    // Sets initial values for labels, passes the turn to white and starts the timers
    // TODO: Separate infinite time gui creation and noninfinite
    private void timerAndGameSetup() {

        maintimer = new Timer(MS_PER_UPDATE, timerlistener);
        sndWarning = new Sound();
        sndWarning.load(PATH_SND_WARNING);
        drawbuttoncounter = 0;
        chessboard.setTurn(ChessPiece.C_WHITE);

        if (infinitetime == false) {

            turntimer = new Timer(MS_PER_UPDATE, timerlistener);
            seconds = 0;

            updatePlayerTurnLabel(white);
            lblBlacktotalturnleft.setText(Integer.toString(blackturntime));
            lblWhitetotalturnleft.setText(Integer.toString(whiteturntime));

            turntimer.start();

        } else {
            btnPauseResume.setVisible(false);
            lblTurnTimeLeft.setVisible(false);
            timerbar.setVisible(false);
            lblBlacktotalturnleft.setText(INFINITE);
            lblWhitetotalturnleft.setText(INFINITE);
            timerlabel.setText(INFINITE);
            lblCurrentTurnTime = new JLabel(INFINITE);
            lblWhite_turn = new JLabel(INFINITE);
            lblWhitetotalturnleft = new JLabel(INFINITE);
            lblBlacktotalturnleft = new JLabel(INFINITE);

        }

        updatePlayerTurnLabel(white);
        lblWhite_turn.setText(white);
        lblBlack_turn.setText(black);

        maintimer.start();
    }
    // Pauses the game

    private void pauseGame() {
        maintimer.stop();
        btnPauseResume.setText(resumesymbol);
        chessboard.setPause(true);
    }
    // Resumes the game

    private void resumeGame() {
        maintimer.start();
        btnPauseResume.setText(pausesymbol);
        chessboard.setPause(false);
    }

    private void setPlayerNames(String p1, String p2) {
        if (isStringNullOrEmpty(p1)) {
            white = WHITE;
        }
        if (isStringNullOrEmpty(p2)) {
            black = BLACK;
        }
        updatePlayerTurnLabel(white);
        lblWhite_gamestate.setText(white);
        lblBlack_gamestate.setText(black);

    }

    private boolean isStringNullOrEmpty(String s) {
        if ((s.length() == 0) || s == null) {
            return true;
        } else {
            return false;
        }
    }

    // Called when a new turn begins
    // Updates GUI-labels, number of turns
    // Sends 
    public void newTurn() {

        turncounter++;

        if (turncounter % 2 == 0) {
            updatePlayerTurnLabel(white);
            chessboard.setTurn(ChessPiece.C_WHITE);
        } else {
            updatePlayerTurnLabel(black);
            chessboard.setTurn(ChessPiece.C_BLACK);
        }
        if (drawbuttoncounter == 1) {
            btnDraw.setText("Draw proposed");
        }

    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnPauseResume) {
                if (pauseresumecounter % 2 == 0) {
                    pauseGame();
                } else {
                    resumeGame();
                }

                pauseresumecounter++;

            } else if (e.getSource() == btnResign) {
                if (turncounter % 2 == 0) {
                    gameEnded(black);
                } else {
                    gameEnded(white);
                }

            } else if (e.getSource() == btnDraw) {

                drawbuttoncounter++;

                if (drawbuttoncounter == 2) {
                    gameEnded(null);
                }
            }
        }
    }

    private void timerComponentUpdates() {

        seconds++;

        if (turncounter % 2 == 0) {

            whiteturntime--;
            lblWhitetotalturnleft.setText(getClockFormat(whiteturntime));
            timerbar.setValue(whiteturntime);
            lblCurrentTurnTime.setText(getClockFormat(whiteturntime));
            currentturnseconds = whiteturntime;

        } else {

            blackturntime--;
            lblBlacktotalturnleft.setText(getClockFormat(blackturntime));
            timerbar.setValue(blackturntime);
            lblCurrentTurnTime.setText(getClockFormat(blackturntime));
            currentturnseconds = blackturntime;
        }


        if (currentturnseconds < PROGRESSBAR_RED) {
            timerbar.setForeground(Color.red);
            sndWarning.play();
        } else {
            timerbar.setForeground(Color.green);
        }

        timerlabel.setText(getClockFormat(seconds));


    }

    //Receives the winning player and adds info to the registry as needed
    private void gameEnded(String w) {

        String winner = w;

        if (white.equals(winner)) {
            userlist.addLoss(black);
            userlist.addMatch(white, black);
            userlist.addWin(white);
            userlist.changeRatingLose(white, black);
            userlist.changeRatingWin(white, black);

        } else if (black.equals(winner)) {
            userlist.addLoss(white);
            userlist.addMatch(white, black);
            userlist.addWin(black);
            userlist.changeRatingLose(black, white);
            userlist.changeRatingWin(black, white);

        } else {

            userlist.addRemis(white, black);
            userlist.sortRatingRemis(white, black);

        }
        endgamepanel = new EndGamePanel(whiteplayer, blackplayer, winner);
        setVisible(false);
    }

    // Method for the GUI (JFrame) to receive the endgamepanel
    public EndGamePanel getEndGamePanel() {

        return endgamepanel;

    }

    // The main listening class which is notified every second (or as the MS_PER_UPDATE specifies)
    private class Timerlistener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            currentturnseconds++;// Place in component update might cause problem


            if (e.getSource() == maintimer && (infinitetime == false)) {
                timerComponentUpdates();
            }

            if (currentturnseconds == 0 && (infinitetime == false)) {
                if (turncounter % 2 == 0) {
                    gameEnded(black);
                } else {
                    gameEnded(white);
                }
            }

            if (chessboard.stalemate() == true) {
                gameEnded(null);
            }

        }
    }

    private class Turnender implements Callback {

        public void callback() {
            newTurn();
        }
    }
}
