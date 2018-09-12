package simplechess.board;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import simplechess.GUI.Callback;
import simplechess.sound.Sound;

/*
 * Alf-Andre Walla, 2012
 * 
 * This class fully manages a chessboard hooked to a canvas.
 * External access is managed through resize() for setting new bounds,
 * newTurn() for both starting combat phase and alternating black/white,
 * and newBoard() which initializes a new empty game with combat phase off.
 * 
 * The latter is done so that you can spawn a chessboard anywhere, and not
 * necessarily play on it. It could be used to view previously played games.
 * 
 */

public class ChessBoard {
    
    private final int ANIMATION_SPEED = 16;
    
    private int wndW, wndH, imgW, imgH;
    private final int chessW = 512, chessH = 512;
    
    private int boardX, boardY; // chessboard position
    
    private final int MOUSE_MOVTRESH = 5;
    private boolean mouseDown = false;
    private int mouseX, mouseY, mouseCamX, mouseCamY;
    
    private ChessPiece board[];         // the chessboard
    private boolean movementmap[];      // valid move array
    private final String SPRITE_IMAGE = "simplechess/art/chess.png";
    private float F_POWER = 1f / 12f; // chessboard color ramp
    private final int CHESS_SHR = 3; // right shift value for 2^3 = 8 board axis
    
    // Mouse and lastmove positions
    private ChessPosition mouseSel, lastmoveSrc, lastmoveDst;
    // Absolute positions of kings to avoid O(n^2) for incheck()
    private ChessPosition kingWhite, kingBlack;
    
    // sound clips
    private Sound sndBoard, sndAttack;
    private final String SOUND_BOARD  = "simplechess/sound/board.wav";
    private final String SOUND_ATTACK = "simplechess/sound/attack.wav";
    
    // color == 0 indicates no turn, and respectively no player is in check
    // color == C_constant indicates color Ns turn and incheck status
    private byte turnColor, checkColor;
    private boolean paused;
    
    // graphics containers
    private BufferedImage buffer, chessboard, background;
    private Canvas canv;
    private Callback callbackEndturn;
    
    // chessboard sprites
    private Sprite spritesheet, spiece[];
    
    // class that manages starry background
    private Stars stars;
    
    public void setBoard(ChessPiece[] board, byte turn) {
        
        turnColor = turn;
        this.board = board;
        renderBoard();
        render();
        update();
        
    }
    
    private void setCheck(byte c) {
        
        checkColor = c;
        // if a player is under attack, play a warning sound
        if (c != 0) sndAttack.play();
        
    }
    
    // starts a new turn for a specific player(color),
    // and enables (if not already enabled) turn-based combat
    // color = 0 disables turn-based combat
    
    public void setTurn(byte color) {
        turnColor = color;
    }
    
    // set & get for pause flag, which when enabled display "Pause" at
    // center of gamescreen
    
    public void setPause(boolean p) {
        paused = p;
        render(); update();
    }
    
    public boolean getPause() { return paused; }
    
    
    // returns playercolor of player in check, 0 if no player is in check
    
    public byte incheck() { return checkColor; }
    
    // returns true if the game is currently a stalemate and should be stopped
    
    public boolean stalemate() { return ChessPiece.stalemate(board); }
    
    // extension for chessboard canvas containing update and paint functions
    // whenever canvas needs to repaint itself, these events is appropriately fired
    
    private class MainCanvas extends Canvas {
        
        @Override
        public void paint(Graphics g) {
            
            // super.paint() is unnecessary because of fullscreen rendering
            
            g.drawImage(buffer, 0, 0, wndW, wndH, 0, 0, imgW, imgH, this);
            g.dispose();
        }
        
        @Override
        public void update(Graphics g) { paint(g); }
        
    }
    
    public void resize(Rectangle bounds) {
        
        canv.setBounds(bounds);
        
        wndW = (int)bounds.getWidth();
        wndH = (int)bounds.getHeight();
        imgW = Math.max(640, wndW);
        imgH = Math.max(480, wndH);
        
        // reposition stars according to new bounds
        stars.initStars(imgW, imgH);
        
        // create background buffer
        background = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
        renderBackground();
        
        // center camera position:
        boardX = (imgW - chessW) >> 1;
        boardY = (imgH - chessH) >> 1;
        
        // create image backbuffer
        buffer = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
        
        render();
        update();
        
    }
    
    public void newBoard() {
        
        // reset stats
        turnColor = 0;
        checkColor = 0;
        movementmap = null;
        mouseSel.select(false);
        paused = false;
        
        // black officers
        board[0] = new ChessPieceRook(ChessPiece.T_B_ROOK);
        board[1] = new ChessPieceKnight(ChessPiece.T_B_KNIGHT);
        board[2] = new ChessPieceBishop(ChessPiece.T_B_BISHOP);
        board[3] = new ChessPieceQueen(ChessPiece.T_B_QUEEN);
        board[4] = new ChessPieceKing(ChessPiece.T_B_KING);
        board[5] = new ChessPieceBishop(ChessPiece.T_B_BISHOP);
        board[6] = new ChessPieceKnight(ChessPiece.T_B_KNIGHT);
        board[7] = new ChessPieceRook(ChessPiece.T_B_ROOK);
        
        // white officers
        board[64-8] = new ChessPieceRook(ChessPiece.T_W_ROOK);
        board[64-7] = new ChessPieceKnight(ChessPiece.T_W_KNIGHT);
        board[64-6] = new ChessPieceBishop(ChessPiece.T_W_BISHOP);
        board[64-5] = new ChessPieceQueen(ChessPiece.T_W_QUEEN);
        board[64-4] = new ChessPieceKing(ChessPiece.T_W_KING);
        board[64-3] = new ChessPieceBishop(ChessPiece.T_W_BISHOP);
        board[64-2] = new ChessPieceKnight(ChessPiece.T_W_KNIGHT);
        board[64-1] = new ChessPieceRook(ChessPiece.T_W_ROOK);
        
        // chess positions updated on kingmoves used for reduction to O(n)
        // for incheck() test
        kingWhite = new ChessPosition(4, 7);
        kingBlack = new ChessPosition(4, 0);
        
        for (int i = 0; i < 8; i++) {
            
            // black player pawns
            board[8     + i] = new ChessPieceBlackPawn(ChessPiece.T_B_PAWN);
            
            // white player pawns
            board[64-16 + i] = new ChessPieceWhitePawn(ChessPiece.T_W_PAWN);
            
        }
        
        renderBoard();
        
    }
    
    public Canvas getCanvas() {
        return canv;
    }
    
    public ChessBoard(Callback cb) {
        
        wndW = 0; wndH = 0;
        callbackEndturn = cb;
        
        board = new ChessPiece[64];
        mouseSel  = new ChessPosition();
        kingWhite = new ChessPosition();
        kingBlack = new ChessPosition();
        
        canv = new MainCanvas();
        canv.addMouseListener(new MouseTriggers() );
        canv.addMouseMotionListener(new MouseMotions() );
        //canv.addMouseWheelListener(null);
        canv.addKeyListener(new Keyboard());
        
        // initialize graphics
        spritesheet = new Sprite();
        
        if (!spritesheet.load(SPRITE_IMAGE)) {
            
            JOptionPane.showMessageDialog(null, "Finner ikke fil: " + SPRITE_IMAGE);
            System.exit(0);
        }
        
        spiece = new Sprite[12];
        {
            int x, y;
            for (int i = 0; i < 12; i++) {
                
                x = 1 + (i % 6) * 34;
                y = 1 + (i / 6) * 34;
                
                spiece[i] = new Sprite(spritesheet.sprite, x, y, 30, 30);
                
            }
        }
        
        // center camera position:
        boardX = -(1 << 31);
        boardY = -(1 << 31);
        
        // create stars
        stars = new Stars();
        
        // create chessboard buffer
        chessboard = new BufferedImage(chessW, chessH, BufferedImage.TYPE_INT_RGB);
        renderBoard();
        
        // initialize sounds
        sndBoard = new Sound();
        sndBoard.load(SOUND_BOARD);
        sndAttack = new Sound();
        sndAttack.load(SOUND_ATTACK);
        
    }
    
    // checks if a king was moved, and updates chesspositions accordingly
    
    private void kingTest(ChessPiece selected, byte color, int x, int y) {
        
        if (selected instanceof ChessPieceKing) {

            // if king was moved, update king positions

            if (color == ChessPiece.C_BLACK) {
                kingBlack.x = x; kingBlack.y = y;
            } else {
                kingWhite.x = x; kingWhite.y = y;
            }

        }
        
    }
    
    private class MouseTriggers implements MouseListener {
        
        @Override
        public void mouseClicked(MouseEvent me) {
            
            // possible piece pickup / release
            
        }
        
        public void mouseEntered(MouseEvent me) {}
        public void mouseExited(MouseEvent me) {}
        
        public void mousePressed(MouseEvent me) {
            
            if (paused) return;
            
            // mouse press
            // panning
            mouseX = me.getX();
            mouseY = me.getY();
            mouseCamX = boardX; mouseCamY = boardY;
            mouseDown = true;
            
        }
        
        public void mouseReleased(MouseEvent me) {
            
            // mouse release
            // stop panning
            
            if (Math.abs(mouseX - me.getX()) < MOUSE_MOVTRESH && 
                Math.abs(mouseY - me.getY()) < MOUSE_MOVTRESH) {
                
                // normalize and scale to image
                int fx = (int) ((float)mouseX / (float)wndW * (float)imgW);
                int fy = (int) ((float)mouseY / (float)wndH * (float)imgH);
                
                // chessboard tile coordinates
                int x = (fx - boardX) / (chessW >> CHESS_SHR);
                int y = (fy - boardY) / (chessH >> CHESS_SHR);
                
                if (x >= 0 && x < 8 &&
                    y >= 0 && y < 8) {
                    
                    // tile id
                    int tid = (y << CHESS_SHR) + x;
                    
                    // check if a move can be made
                    if (movementmap != null && movementmap[tid]) {
                        
                        int pid = mouseSel.getTile();
                        
                        // attempt to move chesspiece
                        
                        ChessPiece oldpiece = board[tid];
                        ChessPiece curpiece = board[pid];
                        board[tid] = board[pid]; // new position
                        board[pid] = null;       // old position
                        
                        // --
                        // check if any king is in check
                        // --
                        
                        byte color = board[tid].getColor();
                        checkColor = 0; // start with no player in check
                        // update positions if kings was moved
                        kingTest(board[tid], color, x, y);
                        
                        if (color == ChessPiece.C_BLACK) {
                            
                            // validate move: check if own king is in check
                            if (ChessPiece.incheck(ChessPiece.C_BLACK, kingBlack.x, kingBlack.y, board)) {
                                
                                JOptionPane.showMessageDialog(null, 
                                        "Illegal move: Black king would be in check.", 
                                        "Stop!", 
                                        JOptionPane.WARNING_MESSAGE);
                                
                                // revert and ignore move
                                setCheck(ChessPiece.C_BLACK);
                                board[tid] = oldpiece;
                                board[pid] = curpiece;
                                // move king back if king was moved
                                kingTest(board[pid], color, mouseSel.x, mouseSel.y);
                                return;
                            }
                            
                            // check if white king is in check
                            if (ChessPiece.incheck(ChessPiece.C_WHITE, kingWhite.x, kingWhite.y, board)) {
                                setCheck(ChessPiece.C_WHITE);
                            }
                            
                        } else {
                            
                            // validate move: check if own king is in check
                            if (ChessPiece.incheck(ChessPiece.C_WHITE, kingWhite.x, kingWhite.y, board)) {
                                
                                JOptionPane.showMessageDialog(null, 
                                        "Illegal move: White king would be in check.", 
                                        "Stop!", 
                                        JOptionPane.WARNING_MESSAGE);
                                
                                // revert and ignore move
                                setCheck(ChessPiece.C_WHITE);
                                board[tid] = oldpiece;
                                board[pid] = curpiece;
                                // move king back if king was moved
                                kingTest(board[pid], color, mouseSel.x, mouseSel.y);
                                return;
                            }
                            
                            // check if black king is in check
                            if (ChessPiece.incheck(ChessPiece.C_BLACK, kingBlack.x, kingBlack.y, board)) {
                                setCheck(ChessPiece.C_BLACK);
                            }
                            
                        }
                        
                        // --
                        // move was successful, perform appropriate changes to board
                        // --
                        
                        if (oldpiece != null) {
                            
                            // check if we arent animating a special move
                            
                            if (oldpiece.getColor() == color) {
                                
                                // likely a castling move
                                if (oldpiece instanceof ChessPieceRook) {
                                    
                                    // move king and rook to the designated places
                                    if (x == 0) {
                                        
                                        board[ (y << CHESS_SHR) + 2 ] = oldpiece;
                                        board[ (y << CHESS_SHR) + 1 ] = curpiece;
                                        
                                    } else {
                                        
                                        board[ (y << CHESS_SHR) + 5 ] = oldpiece;
                                        board[ (y << CHESS_SHR) + 6 ] = curpiece;
                                        
                                    }
                                    board[tid] = null; // clear new position
                                    if (curpiece.getColor() == ChessPiece.C_WHITE)
                                        kingWhite.x = (x == 0) ? 1 : 6;
                                    else
                                        kingBlack.x = (x == 0) ? 1 : 6;
                                    
                                }
                                
                            } else {
                            
                                // a piece is being captured
                                // make cheap animation

                                ActionListener a = new AnimationTimer( oldpiece.getID(), me.getX(), me.getY() );
                                Timer t = new Timer(ANIMATION_SPEED, a);
                                t.start();
                                
                            }
                        }
                        
                        // check if promote rule applies
                        
                        if (y == 7 && board[tid] instanceof ChessPieceBlackPawn)
                            board[tid] = new ChessPieceQueen(ChessPiece.T_B_QUEEN);
                        
                        if (y == 0 && board[tid] instanceof ChessPieceWhitePawn)
                            board[tid] = new ChessPieceQueen(ChessPiece.T_W_QUEEN);
                        
                        // unselect selection
                        mouseSel.select(false);
                        movementmap = null; // invalidate movementmap
                        
                        // set last moves for rectangles
                        lastmoveSrc = new ChessPosition(x, y);
                        lastmoveDst = new ChessPosition(mouseSel);
                        
                        // play sound
                        sndBoard.play();
                        
                        // end of turn
                        callbackEndturn.callback();
                        
                    } else {
                        
                        // check if we have selected a chesspiece
                        if (board[tid] != null && board[tid].getColor() == turnColor) {
                            
                            if (mouseSel.selected && mouseSel.x == x && mouseSel.y == y) {
                                
                                mouseSel.select(false);
                                movementmap = null;
                                
                            } else {
                                
                                mouseSel.setpos(x, y);
                                mouseSel.select(true);

                                // make movementmap for chesspiece
                                movementmap = board[tid].moveMap(x, y, board);
                                sndBoard.play();
                                
                            }
                            
                        } else {

                            mouseSel.select(false); // unselect
                            movementmap = null; // nullify movementmap
                            
                        }
                        
                    }
                    
                    renderBoard();
                    render();
                    update();
                    
                } else {
                    
                    mouseSel.select(false);
                }
                
            }
            
            mouseDown = false;
        }
        
    }
    
    // events fired on mousemovement or a dragging operation
    // used to move chessboard around
    
    private class MouseMotions implements MouseMotionListener {
        
        @Override
        public void mouseMoved(MouseEvent me) { }
        @Override
        public void mouseDragged(MouseEvent me) {
            
            if (mouseDown) {
                
                if (Math.abs(mouseX - me.getX()) >= MOUSE_MOVTRESH || 
                    Math.abs(mouseY - me.getY()) >= MOUSE_MOVTRESH) {
                    
                    boardX = mouseCamX + (me.getX() - mouseX);
                    boardY = mouseCamY + (me.getY() - mouseY);

                    render();
                    update();
                }
                
            }
            
        }
        
    }
    
    // renders the chessboard onto an imagebuffer
    
    private void renderBoard() {
        
        WritableRaster wr = chessboard.getRaster();
        
        rgb3 rgb = new rgb3();
        
        int strength = 200;
        
        int w = chessboard.getWidth();
        int h = chessboard.getHeight();
        
        int t, tc;
        int tx, ty;
        final int tsizeX = w >> CHESS_SHR;
        final int tsizeY = h >> CHESS_SHR;
        
        int edgeX, edgeY;
        float crossX, crossY;
        
        for (int y = 0; y < h; y++)
        for (int x = 0; x < w; x++) {
            
            tx = x / tsizeX; // could be power of 2
            ty = y / tsizeY;
            
            t = ty * 8 + tx;      // tile id
            tc = (t / 8 + t) & 1; // tile color
            
            if (tc == 0) rgb.white();
            else         rgb.black();
            
            edgeX = x % tsizeX;
            edgeY = y % tsizeY;
            crossX = (float)Math.abs( edgeX - tsizeX / 2);
            crossY = (float)Math.abs( edgeY - tsizeY / 2);
            
            rgb.corona(crossX, crossY, (tsizeX + tsizeY) / 4, F_POWER, strength);
            
            // lastmove rectangles
            if (lastmoveSrc != null) {
                if (lastmoveSrc.x == tx && lastmoveSrc.y == ty || lastmoveDst.x == tx && lastmoveDst.y == ty) {
                    if (edgeX < 3 || edgeY < 3 || edgeX > tsizeX-4 || edgeY > tsizeY-4) {
                        rgb.colorize(127, 0, 127, 0.5f);
                    }
                }
            }
            
            // blue selection
            if (mouseSel.selected) {
                
                if (mouseSel.x == tx && mouseSel.y == ty)
                    rgb.colorize(0, 80, 80, 1.25f);
                
            }
            
            // green can-move
            
            if (movementmap != null) {
                
                if (movementmap[t])
                    
                    // occupied tile
                    if (board[t] != null)
                        rgb.colorize(127, 16, 16, 1.25f);
                    // free tile
                    else
                        rgb.colorize(16, 127, 16, 2.0f);
                
            
            }
            
            wr.setPixel(x, y, rgb.getInts());
            
        }
        
        Graphics g = chessboard.getGraphics();
        
        // TODO: fjerne magic numbers på brikkeblit
        
        for (int i = 0; i < 64; i++) {
            
            tx = i & 7;
            ty = i >> CHESS_SHR;
            
            
            if (board[i] != null) {
                
                if (board[i].getID() != 0)
                    g.drawImage(spiece[ board[i].getID()-1 ].sprite, 8 + tx * 64, 8 + ty * 64, 48, 48, null);
                
            }
            
            
        }
        
    }
    
    private void renderBackground() {
        
        WritableRaster wr =  background.getRaster();
        
        int[] black = new int[] {0, 0, 0};
        
        int w = imgW;
        int h = imgH;
        
        for (int y = 0; y < h; y++)
        for (int x = 0; x < w; x++)
            wr.setPixel(x, y, black );
        
        // draw stars
        stars.drawStars(wr);
        
    }
    
    private int renderOutline(Graphics2D g, Font f, String s, int x, int y, Color inside, Color outside) {
        
        FontRenderContext frc = g.getFontRenderContext();
        FontMetrics fm = g.getFontMetrics();
        
        g.setColor(inside);
        
        // load identity matrix, to reset position
        g.setTransform(new AffineTransform());
        
        // draw text at specified position
        g.drawString(s, x, y + fm.getHeight());
        
        TextLayout tl = new TextLayout(s, f, frc);
        Shape outline = tl.getOutline(null);
        
        AffineTransform transform = g.getTransform();
        transform.translate(x, y + fm.getHeight());
        g.transform(transform);
        
        g.setColor(outside);
        g.draw(outline);
        
        return fm.stringWidth(s);
    }
    
    private void render() {
        
        Graphics2D g = (Graphics2D)buffer.getGraphics();
        
        // background image
        g.drawImage(background, 0, 0, imgW, imgH, null);
        
        // chessboard unscaled
        int cx, cy, csx, csy, csw, csh;
        
        if (boardX < 0) {
            cx = 0; csx = -boardX; csw = imgW - csx;
        } else {
            cx = boardX; csx = 0; csw = imgW - cx;
        }
        if (boardY < 0) {
            cy = 0; csy = -boardY; csh = imgH - csy;
        } else {
            cy = boardY; csy = 0; csh = imgH - cy;
        }
        
        if (csx < 0 || csy < 0 || csw <= 0 || csh <= 0) return;
        
        g.drawImage(chessboard, cx, cy, cx + csw, cy+csh, csx, csy, csx + csw, csy + csh, null);
        
        // text on backbuffer
        
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        Color gold = new Color(160, 127, 64, 127);
        Color white = new Color(200, 200, 200, 220);
        Color black = new Color(48, 48, 48, 255);
        
        Font font = new Font("Times New Roman", Font.TRUETYPE_FONT + Font.BOLD, 20);
        g.setFont(font);
        
        int dw, dh;
        
        for (int i = 0; i < 8; i++) {
            
            dw = boardX + 24 + i * (chessW >> 3);
            
            renderOutline(g, font, "" + (char)('A' + i), dw, boardY - 32, white, gold);
            renderOutline(g, font, "" + (char)('A' + i), dw, chessH + boardY - 4, white, gold);
            
            dh = boardY + 16 + i * (chessW >> 3);
            
            renderOutline(g, font, "" + (char)('8' - i), boardX - 24, dh, white, gold);
            renderOutline(g, font, "" + (char)('8' - i), boardX + chessW + 16, dh, white, gold);
            
        }
        
        font = new Font("Times New Roman", Font.TRUETYPE_FONT + Font.BOLD, 30);
        g.setFont(font);
        
        // display which player has the current turn
        
        if (turnColor != 0) {
            
            Color color_a;
            String text_a, text_b = " players turn";
            
            if (turnColor == ChessPiece.C_WHITE) {
                
                text_a = "White"; color_a = white;
                
            } else {
                
                text_a = "Black"; color_a = black;
            }

            FontMetrics fm = g.getFontMetrics();
            int totalwidth = fm.stringWidth(text_a + text_b);

            // rectangle covering text
            g.setColor(new Color(64, 64, 64, 80));
            g.setTransform(new AffineTransform());
            g.fill(new Rectangle2D.Float(0, 8, totalwidth + 16, fm.getHeight()));
            // 

            int outw = renderOutline(g, font, text_a, 8, 0, color_a, gold);
                    renderOutline(g, font, text_b, 8 + outw, 0, Color.WHITE, gold);
            
        }
        
        // display in-check text if someones king is in check
        
        if (checkColor != 0) {
            
            // someones king is in check
            Color color_a;
            String text_a, text_b = " player in check!";
            
            if (checkColor == ChessPiece.C_WHITE) {
                
                text_a = "White"; color_a = white;
                
            } else {
                
                text_a = "Black"; color_a = black;
            }
            
            int outw = renderOutline(g, font, text_a, 8, 26, color_a, gold);
                       renderOutline(g, font, text_b, 8 + outw, 26, Color.WHITE, gold);
            
        }
        
        if (paused) {
            
            final String TXT_PAUSED = "Paused";
            
            font = new Font("Times New Roman", Font.TRUETYPE_FONT + Font.BOLD, 54);
            g.setFont(font);
            
            FontMetrics fm = g.getFontMetrics();
            int totalwidth = fm.stringWidth(TXT_PAUSED);
            int height = fm.getHeight();
            
            int x = (imgW >> 1) - (totalwidth >> 1);
            int y = (imgH >> 1) - (height >> 1);
            
            // rectangle behind text
            g.setColor(new Color(70, 70, 70, 160));
            g.setTransform(new AffineTransform());
            g.fill(new RoundRectangle2D.Float(x-8, y+12, totalwidth + 16, height, 12.0f, 12.0f));
            //
            
            renderOutline(g, font, TXT_PAUSED, x, y, Color.white, black);
            
        }
        
        g.dispose();
        
    }
    
    // manual update of rendered graphics
    
    private void update() {
        Graphics g = canv.getGraphics();
        g.drawImage(buffer, 0, 0, wndW, wndH, 0, 0, imgW, imgH, null);
        g.dispose();
    }
    
    private class Keyboard implements KeyListener {
        
        @Override
        public void keyPressed(KeyEvent evt) {
            int keyCode = evt.getKeyCode();

            //if (evt.isShiftDown())

            if (keyCode == KeyEvent.VK_ESCAPE) {
                
                System.exit(0);
            }
            else if (keyCode == KeyEvent.VK_ADD) {

                if (F_POWER < 1.0f)
                    F_POWER *= 2.0f;
                else
                    F_POWER += 0.5f;
                render();
                update();
            }
            else if (keyCode == KeyEvent.VK_SUBTRACT) {

                if (F_POWER < 1.0f)
                    F_POWER /= 2.0f;
                else
                    F_POWER -= 0.5f;
                render();
                update();
            }
            
        }
        
        @Override
        public void keyReleased(KeyEvent evt) {

        }
        
        @Override
        public void keyTyped(KeyEvent evt) {

        }
        
    }
    
    private String coord(int x, int y) {
        return (char)('A' + x) + "" + (char)('1' + y);
    }
    
    private class AnimationTimer implements ActionListener {
        
        private final int MAX_TTL = 16;
        private final int FLOAT_PER_TICK = 6;
        private final float SPRITE_SIZE = 64.0f;
        
        private int x, y;
        private byte id, ttl;
        
        public AnimationTimer(byte id, int x, int y) {
            
            this.x = x; this.y = y; this.id = id;
            ttl = MAX_TTL;
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            render();
            
            if (ttl > 0) {
                
                // blit
                
                Graphics2D g = (Graphics2D)(buffer.getGraphics());
                
                float f = (float)ttl / (float)MAX_TTL;
                int size = (int)(f * f * f * SPRITE_SIZE);
                
                g.drawImage(spiece[id-1].sprite, x - (size >> 1), y - (size >> 1), size, size, null); 
                
                y -= FLOAT_PER_TICK; ttl--;
                
                g.dispose();
                
            } else {
                ((Timer)e.getSource()).stop();
            }
            
            update();
            
        }
        
    }
    
}
