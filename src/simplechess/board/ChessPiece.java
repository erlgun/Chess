package simplechess.board;

/*
 * Alf-Andre Walla, 2012
 * 
 * This class manages chesspieces on a chessboard.
 * 
 * All chesspiece classes are stored in a hierarchy starting with the
 * abstract class ChessPiece (this).
 * It also contains static functions to determine the state of the board,
 * such as wether or not the kings are in check, or if the game is currently
 * a stalemate.
 * 
 * This class attempts to follow the official chess rules. See:
 * http://www.chessvariants.org/fidelaws.html
 * 
 */

// TODO: castling, stalemate

public abstract class ChessPiece {
    
    public static final byte T_W_PAWN   = 1;
    public static final byte T_W_ROOK   = 2;
    public static final byte T_W_KING   = 3;
    public static final byte T_W_QUEEN  = 4;
    public static final byte T_W_KNIGHT = 5;
    public static final byte T_W_BISHOP = 6;
    
    public static final byte T_B_PAWN   = 7;
    public static final byte T_B_ROOK   = 8;
    public static final byte T_B_KING   = 9;
    public static final byte T_B_QUEEN  = 10;
    public static final byte T_B_KNIGHT = 11;
    public static final byte T_B_BISHOP = 12;
    
    public static final byte C_WHITE = 1;
    public static final byte C_BLACK = 2;
    
    private byte chessid;     // sprite and chess token ID
    private boolean hasmoved; // used in (for example) castling
    
    public ChessPiece(byte cid) {
        chessid = cid;
    }
    
    // returns the constant (sprite) id for this chesspiece
    
    public byte getID() { return chessid; }
    
    // setter & getter for hasmoved datafield
    
    public void setMoved(boolean b) { hasmoved = b; }
    public boolean getMoved() { return hasmoved; }
    
    // returns the color constant for this chesspiece
    
    public byte getColor() {
        if (chessid < 7) return C_WHITE;
        return C_BLACK;
    }
    
    //    check if piece can move from (x,y) to (x+dx, y+dy)
    //    attack determines if theres an opposition piece on this spot
    public abstract boolean move(int x, int y, int testx, int testy, final ChessPiece[] board);
    
    // compute boolean for specific tile for piece
    
    public boolean moveTile(int x, int y, int tx, int ty, final ChessPiece[] board) {
        
        ChessPiece piece = board[ (y << 3) + x ];
        if (piece == null) return false;
        
        return move(x, y, tx, ty, board);
        
    }
    
    // compute boolean movement map for piece
    // returns null if the chesspiece used in test is null
    
    public boolean[] moveMap(int x, int y, final ChessPiece[] board) {
        
        ChessPiece piece = board[(y << 3) + x];
        if (piece == null) return null;
        
        boolean white = piece.getID() < 7;
        
        boolean[] mmap = new boolean[64];
        
        for (int bx = 0; bx < 8; bx++)
        for (int by = 0; by < 8; by++)
            if(!(x == bx && y == by)) // avoid self-test
                mmap[ (by << 3) + bx ] = move(x, y, bx, by, board);
        
        return mmap;
        
    }
    
    
    // returns true only if a chessboard position can be attacked
    
    public boolean attack(final ChessPiece dst) {
        
        return dst != null && dst.getColor() != getColor();
        
    }
    
    // returns true if a chessboard position can be moved to,
    // or can be attacked
    
    public boolean attackormove(final ChessPiece dst) {
        
        return dst == null || dst.getColor() != getColor();
        
    }
    
    // returns true if the chesspiece at (testx, testy) is under attack
    // color is used to distinguish friend or foe
    
    public static boolean incheck(byte color, int testx, int testy, ChessPiece[] board) {
        
        // iterate entire board for differing colored pieces
        // check move value towards position (testx, testy)
        
        ChessPiece selected;
        ChessPiece oldtile = board[ (testy << 3) + testx ];
        
        // create temporary pawn for where king could move to
        board[ (testy << 3) + testx ] = new ChessPieceBlackPawn((byte)((color-1) * 7));
        
        for (int y = 0; y < 8; y++)
        for (int x = 0; x < 8; x++) {
            
            selected = board[ (y << 3) + x ];
            if (selected != null) {
                
                if (selected.getColor() != color)
                    if (selected.moveTile(x, y, testx, testy, board)) {
                        board[ (testy << 3) + testx ] = oldtile;
                        return true;
                    }
                
            }
            
        }
        
        board[ (testy << 3) + testx ] = oldtile;
        return false;
        
    }
    
    // cross movement generalized procedure
    // used by rook and queen
    
    public boolean cross(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        // is in movement vectors
        if (x == nx) {
            
            // possible move vertically
            int dist = ny - y;
            int sign = sgn(dist);
            
            // test for blocked path (start at sign, to avoid self-test)
            for (int dy = sign; Math.abs(dy) < Math.abs(dist); dy += sign ) {
                
                // if something is blocking return false
                if ( board[ ((y + dy) << 3) + nx ] != null ) return false;
                
            }
            
        }
        else
        if (y == ny) {
            
            // possible move horizontally
            int dist = nx - x;
            int sign = sgn(dist);
            
            // test for blocked path (start at sign, to avoid self-test)
            for (int dx = sign; Math.abs(dx) < Math.abs(dist); dx += sign ) {
                
                // if something is blocking return false
                if ( board[ (ny << 3) + (x + dx) ] != null ) return false;
                
            }
            
        } else
            // if not on either axis, return immediately
            return false;
        
        // unblocked until final position
        // true if free or attackable
        if ( attackormove(board[ (ny << 3) + nx ]) ) return true;
        
        return false;
        
    }
    
    // diagonal movement generalized procedure
    // used by bishop and queen
    
    public boolean diagonal(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        int distx = Math.abs(nx-x);
        int disty = Math.abs(ny-y);
        
        if ( distx == disty ) {
            
            // trace diagonally
            
            int sgnx = sgn(nx-x);
            int sgny = sgn(ny-y);
            
            for (int i = 1; i < distx; i++)
                if ( board[ ((y + sgny * i) << 3) + (x + sgnx * i) ] != null )
                    return false;
            
            // check final position
            if (attackormove(board[ (ny << 3) + nx ]))
                return true;
        }
        
        return false;
    }
    
    // returns true if the game is currently a stalemate
    // a stalemate is a draw according to FIDE chess laws
    // currently only tests for king vs king or king vs king
    // with one bishop or knight on only one side.
    
    public static boolean stalemate(ChessPiece[] board) {
        
        if (board == null) return false;
        
        ChessPiece selected;
        
        boolean stalemate = true;
        byte id;
        int counter = 0;
        
        for (int y = 0; y < 8; y++)
        for (int x = 0; x < 8; x++) {
            
            selected = board[ (y << 3) + x ];
            if (selected != null) {
                
                id = selected.getID();
                
                if (id != T_B_KING && id != T_W_KING) {
                
                    if (id == T_B_BISHOP || id == T_W_BISHOP || id == T_B_KNIGHT || id == T_W_KNIGHT)
                        counter++;
                    else
                        stalemate = false;
                }
            }
        }
        
        return (stalemate && counter < 2);
        
    }
    
    // the sign function returns the sign value of an integer
    // sgn returns 0 if n = 0, 1 if n > 0 and -1 is n < 0
    
    public static int sgn(final int n) {
        return (n == 0) ? 0 : (n > 0) ? 1 : -1;
    }
    
    
}

// pawn, black

class ChessPieceBlackPawn extends ChessPiece {
    
    public ChessPieceBlackPawn(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        // TODO: en-passant
        
        // attack right
        if (nx == x+1 && ny == y+1)
            if (attack(board[(ny << 3) + nx])) return true;
        
        // attack left
        if (nx == x-1 && ny == y+1)
            if (attack(board[(ny << 3) + nx])) return true;
        
        if (x != nx) return false;
        
        if (y == 1) {
            
            // one step forward
            if (ny == 2)
                if (board[(ny << 3) + nx] == null) return true;
                
            // two-step (cannot step over any pieces)
            if (ny == 3)
                if (board[(2 << 3) + nx] == null)
                if (board[(3 << 3) + nx] == null)
                    return true;
            
            
        } else {
            
            // one step up
            if (ny == y+1)
                if (board[(ny << 3) + nx] == null) return true;
            
        }
        
        return false;
        
    }
    
}

// pawn, white

class ChessPieceWhitePawn extends ChessPiece {
    
    public ChessPieceWhitePawn(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        // TODO: en-passant
        
        // attack right
        if (nx == x+1 && ny == y-1)
            if (attack(board[(ny << 3) + nx])) return true;
        // attack left
        if (nx == x-1 && ny == y-1)
            if (attack(board[(ny << 3) + nx])) return true;
        
        if (x != nx) return false;
        
        if (y == 6) {
            
            // one step forward
            if (ny == 5)
                if (board[(ny << 3) + nx] == null) return true;
            
            // two-step (cannot step over any pieces)
            if (ny == 4)
                if (board[4 * 8 + nx] == null)
                if (board[5 * 8 + nx] == null)
                    return true;
            
        } else {
            
            // one step up
            if (ny == y-1)
                if (board[ (ny << 3) + nx] == null) return true;
            
        }
        
        return false;
        
    }
    
}

// castle / rook, black & white

class ChessPieceRook extends ChessPiece {
    
    public ChessPieceRook(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        // TODO: castling
        
        return cross(x, y, nx, ny, board);
        
    }
    
}

// king, black & white

class ChessPieceKing extends ChessPiece {
    
    public ChessPieceKing(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        byte mycolor = getColor();
        
        // castling
        // check if king hasnt been moved, (nx,ny) is appropriate value and the dest is not null
        if (getMoved() == false && (nx == 0 || nx == 7) && ny == y && board[ (ny << 3) + nx ] != null) {
            
            if ((board[ (ny << 3) + nx ].getID() == T_B_ROOK && mycolor == C_BLACK) ||
                (board[ (ny << 3) + nx ].getID() == T_W_ROOK && mycolor == C_WHITE)) {
                
                // test if the rook in question has been moved
                if (board[ (ny << 3) + nx ].getMoved() == false) {

                    int dist = nx - x;
                    int sign = sgn(dist);

                    // test for blocked path (start at sign, to avoid self-test)
                    for (int dx = sign; Math.abs(dx) < Math.abs(dist); dx += sign ) {

                        // if something is blocking return false
                        if ( board[ (y << 3) + (x + dx) ] != null ) return false;
                        // test for incheck at position
                        if (incheck(mycolor, x + dx, y, board)) return false;

                    }
                    return true;
                    
                }
            }
            
        }
        
        // king uses chebyshev distance formula
        // http://en.wikipedia.org/wiki/Chebyshev_distance
        
        // if chebyshev distance is not 1, incorrect distance
        if (Math.max( Math.abs(y-ny), Math.abs(x-nx) ) != 1) return false;
        
        // move if not blocked, or attackable
        if (attackormove(board[ (ny << 3) + nx])) 
            if (!incheck(mycolor, nx, ny, board)) return true;
        
        return false;
        
    }
    
}

// knight, black & white

class ChessPieceKnight extends ChessPiece {
    
    public ChessPieceKnight(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        if ( Math.abs(y-ny) == 2 && Math.abs(x-nx) == 1 ||
             Math.abs(y-ny) == 1 && Math.abs(x-nx) == 2 )
            return attackormove( board[ (ny << 3) + nx ] );
        
        return false;
        
    }
    
}

// bishop, black & white

class ChessPieceBishop extends ChessPiece {
    
    public ChessPieceBishop(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        return diagonal(x, y, nx, ny, board);
        
    }
    
}

// queen, black & white

class ChessPieceQueen extends ChessPiece {
    
    public ChessPieceQueen(byte cid) { super(cid); }
    
    @Override
    public boolean move(int x, int y, int nx, int ny, final ChessPiece[] board) {
        
        // diagonal movement
        if (diagonal(x, y, nx, ny, board)) return true;
        
        // cross movement
        return cross(x, y, nx, ny, board);
        
    }
    
}
