package simplechess.rating;

public class Chessuser {

    protected String user;
    private int rating, wins, remis, losses, matches;
    public Chessuser next;
    private String [] lastfive;

    private final int START_RATING = 1500;

    // Constructor
    public Chessuser(String user, Chessuser next) {

        this.next = next;
        this.user = user;
        rating = START_RATING;
        wins = 0;
        remis = 0;
        losses = 0;
        matches = 0;

    }
    // Metode which returns the username of the user

    public String getUserName() {
        return user;
    }
    // Metode which returns the rating of ther user

    public int getRating() {
        return rating;
    }

    // Tostring metod
    @Override
    public String toString() {
        String output = " ";
        output += "User Name: " + user + "\n";
        output += "Rating: " + rating + "\n";
        output += "Wins: " + wins + "\n";
        output += "Remis: " + remis + "\n";
        output += "Loss: " + losses + "\n";
        output += "Matches: " + matches;
        return output;
    }

        // Method for putting all Chessuser in an array row
	    public String[] toRow() {
	    String [] sort = { user, Integer.toString(wins), Integer.toString(remis), Integer.toString(losses), Integer.toString(matches), Integer.toString(rating) };
	            return sort;
       }


    // Increases the amount of victories for the user by one

    public void addWin() {
        wins += 1;
    }
    // Increases the amount of losses for the user by one

    public void addLoss() {
        losses += 1;
    }
    // Increases the amount of draws for the user by one

    public void addRemis() {
        remis++;
    }
    // Increases or lowers the rating

    public void changeRating(int change) {
        rating += change;

    }
    // Increases the amount of games for the user by one

    public void addMatch() {
        matches++;

    }
    public boolean changefive(String a) {
        if (lastfive[4] != null) {
            String [] lastfivecopy = lastfive;
            for (int i = 1; i < lastfive.length; i++) {
                lastfive[i] = lastfivecopy[i-1];
            }
            lastfive[0] = a;
            return true;
        }
        else {
            for (int i = 0; i < lastfive.length; i++) {
            if (lastfive[i] == null){
                lastfive[i] = a;
                return true;
            }
                
        }
        }
        return false;
    }
}
