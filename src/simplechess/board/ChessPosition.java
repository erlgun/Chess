package simplechess.board;
/**
 *
 * @author GONZO
 */
public class ChessPosition {
    
    protected boolean selected;
    protected int x, y;
    
    public ChessPosition() {}
    public ChessPosition(int x, int y) { this.x = x; this.y = y; }
    public ChessPosition(ChessPosition p) { x = p.x; y = p.y; }
    
    public void setpos(int x, int y) { this.x = x; this.y = y; }
    public void setpos(ChessPosition p) { x = p.x; y = p.y; }
    public void select(boolean b) { selected = b; }
    
    public int getTile() { return (y << 3) + x; }
    
}
