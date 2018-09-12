package simplechess.rating;

public class Userlist {

    private Chessuser first;
    private int winrate = 1500;
    private int loserate = 1500;
    private String default1 = "Black";
    private String default2 = "White";


    // Adds a new user to the Chessuser system and checks if the user already exits
    public boolean insert(Chessuser insert) {
        if ((insert == null) || (("").equals(insert.getUserName())) || (findUser(insert.getUserName()) == true) || ((default1).toUpperCase().equals((insert.getUserName().toUpperCase())) || ((default2).toUpperCase().equals((insert.getUserName()).toUpperCase())))) {
            return false;


        } else {

            if (first == null) {
                first = insert;
                return true;
            } else {
                Chessuser current = first;

                while (current.next != null) {
                    current = current.next;


                }

                current.next = insert;
                return true;
            }
        }
    }

    //Finds a user and returns true if it finds the user

    public boolean findUser(String user) {
        Chessuser current = first;
        if (((user).toUpperCase().equals((default1).toUpperCase())) || ((user).toUpperCase().equals((default2).toUpperCase()))) {
            return false;
        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((user).toUpperCase())) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }

    }
    // Increases the amount of matches both players have played with 1

    public boolean addMatch(String w, String l) {
        if ((w == null || l == null)) {
            return false;
        }
        else
        {
        Chessuser current = first;
        if (((w).toUpperCase().equals((default1).toUpperCase())) || ((w).toUpperCase().equals((default2).toUpperCase()))) {

        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((w).toUpperCase())) {
                    current.addMatch();

                }
                current = current.next;
            }
        }
        current = first;
        if (((l).toUpperCase().equals((default1).toUpperCase())) || ((l).toUpperCase().equals((default2).toUpperCase()))) {

        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((l).toUpperCase())) {
                    current.addMatch();

                }
                current = current.next;
            }
        }
        return true;
        }

    }
    // Increases the amount of wins a player have with one

    public boolean addWin(String user) {
        Chessuser current = first;
        if (((user).toUpperCase().equals((default1).toUpperCase())) || ((user).toUpperCase().equals((default2).toUpperCase()))) {
            return false;
        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((user).toUpperCase())) {
                    current.addWin();
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }
    // Increases the amount of remis the players have with one

    public boolean addRemis(String w, String l) {
        if ((w == null || l == null)) {
            return false;
        }
        else
        {
        Chessuser current = first;
        if (((w).toUpperCase().equals((default1).toUpperCase())) || ((w).toUpperCase().equals((default2).toUpperCase()))) {

        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((w).toUpperCase())) {
                    current.addRemis();

                }
                current = current.next;
            }
        }
        current = first;
        if (((l).toUpperCase().equals((default1).toUpperCase())) || ((l).toUpperCase().equals((default2).toUpperCase()))) {

        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((l).toUpperCase())) {
                    current.addRemis();

                }
                current = current.next;
            }
        }
        return true;
        }

    }
    // Increases the amount of losses a player have with one

    public boolean addLoss(String user) {
        Chessuser current = first;
        if (((user).toUpperCase().equals(default1)) || ((user).toUpperCase().equals((default2).toUpperCase()))) {
            return false;
        } else {
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((user).toUpperCase())) {
                    current.addLoss();
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }


    // Metode for finding out which player has the highest rating during a remis
    public boolean sortRatingRemis(String w, String l)
            {
         if ((w == null) || (l == null ) || ( ((w).toUpperCase().equals((default1).toUpperCase())) && ((l).toUpperCase().equals((default2).toUpperCase())))) {
            return false;
        } else  {
             Chessuser current = first;
                while (current != null) {
                    if ((current.getUserName()).toUpperCase().equals((w).toUpperCase())) {
                        winrate = current.getRating();

                    }
                    current = current.next;
                }
             current = first;
                while (current != null) {
                    if ((current.getUserName()).toUpperCase().equals((l).toUpperCase())) {
                        loserate = current.getRating();

                    }
                    current = current.next;
                }
                if (winrate == loserate)
                {
                    return false;
                }
                if (winrate < loserate)
                        {
                            changeRatingRemis(l, w);
                            return true;
                        }
                if (winrate > loserate)
                        {
                            changeRatingRemis(w, l);
                            return true;
                        }
                return false;
         }
            }
// Increases the rating of the player with the lowest rating and decreases the rating of the player with the highest rating during a remis
    public boolean changeRatingRemis(String w, String l) {


            Chessuser current = first;

            int winChange = ratingCalcRemisHigh(winrate, loserate);
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((w).toUpperCase())) {
                    current.changeRating(winChange);

                }
                current = current.next;
            }
            current = first;
            int loseChange = ratingCalcRemisLow(winrate, loserate);
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((l).toUpperCase())) {
                    current.changeRating(loseChange);

                }
                current = current.next;
            }
            winrate = 1500;
            loserate = 1500;
            return true;

    }

    // Changes the rating for the winner based on the players rating

    public boolean changeRatingWin(String winner, String loser) {
        Chessuser current = first;
        if (((winner).toUpperCase().equals((default1).toUpperCase())) || ((winner).toUpperCase().equals((default2).toUpperCase()))) {
            return false;
        } else {

            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((winner).toUpperCase())) {
                    winrate = current.getRating();

                }
                current = current.next;
            }
            current = first;
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((loser).toUpperCase())) {
                    loserate = current.getRating();
                  }
                current = current.next;
            }
            current = first;
            int winChange = ratingCalcWinner(winrate, loserate);
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((winner).toUpperCase())) {
                    current.changeRating(winChange);

                }
                current = current.next;
            }
            winrate = 1500;
            loserate = 1500;
            return true;
        }


    }
    // Changes the rating of the loser based on the players rating

    public boolean changeRatingLose(String winner, String loser) {
        Chessuser current = first;
        if (((loser).toUpperCase().equals((default1).toUpperCase())) || ((loser).toUpperCase().equals((default2).toUpperCase()))) {
            return false;
        } else {

            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((winner).toUpperCase())) {
                    winrate = current.getRating();

                }
                current = current.next;
            }
            current = first;
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((loser).toUpperCase())) {
                    loserate = current.getRating();

                }
                current = current.next;
            }
            current = first;
            int loseChange = ratingCalcLoser(winrate, loserate);
            while (current != null) {
                if ((current.getUserName()).toUpperCase().equals((loser).toUpperCase())) {
                    current.changeRating(loseChange);
                }
                current = current.next;
            }
            winrate = 1500;
            loserate = 1500;
            return true;
        }
    }
    // Function for deleting a Chessuser

    public boolean deleteUser(String user) {
        if (findUser(user)) {
            Chessuser current = first;

            if ((current.getUserName()).toUpperCase().equals((user).toUpperCase())) {
                first = current.next;
                return true;
            } else {
                while ((current.next.getUserName()).toUpperCase().equals((user).toUpperCase())) {
                    current = current.next;

                }
                current.next = current.next.next;
                return true;
            }

        }
        return false;
    }
    // Tostring metod

    @Override
    public String toString() {
        String output = " ";
        Chessuser current = first;

        while (current != null) {
            output += current + "\n\n";
            current = current.next;
        }
        return output;
    }

    // Metod for calculating how much rating the losing player losses.

    public static int ratingCalcLoser(int a, int b) {
        double winner = a;
        double loser = b;
        double awc = (1 / (1 + (java.lang.Math.pow(10.00000, ((a - b) / 400.00000)))));
        double loserating = 30 * (0 - awc);
        int ratechange = (int) loserating;

        return ratechange;
    }
    // Metod for calculating how much rating the winner gains.

    public static int ratingCalcWinner(int a, int b) {
        double winner = a;
        double loser = b;
        double awc = (1 / (1 + (java.lang.Math.pow(10.00000, ((b - a) / 400.00000)))));
        double winrating = 30 * (1 - awc);
        int ratechange = (int) winrating;

        return ratechange;



    }

    // returns the nodecount of a Chessuser nodelist

    private int nodeCount(Chessuser first) {

        Chessuser n = first;
        int c = 0;

        while(n != null) {
            n = n.next; c++;
        }

        return c;

    }


    // returns the highest rating found directly under ceil

    private int findHighestRating(Chessuser first, int ceil) {

        Chessuser n = first;
        int highest = 0;

        while(n != null) {

            if (n.getRating() < ceil) {

                if (highest < n.getRating())
                    highest = n.getRating();

            }
            n = n.next;

        }
        return highest;

    }

    // returns the number of users with the same rating

    private int findRatingCount(Chessuser first, int rating) {

        Chessuser n = first;
        int count = 0;

        while(n != null) {

            if (n.getRating() == rating)
                count++;

            n = n.next;

        }
        return count;

    }

    // returns an array of users with the same rating

    private Chessuser[] findUsersRating(Chessuser first, int rating) {

        Chessuser n = first;
        int count = findRatingCount(first, rating);
        int counter = 0;

        Chessuser ret[] = new Chessuser[count];

        while(n != null) {

            if (n.getRating() == rating) {

                ret[counter] = n; counter++;

            }
            n = n.next;

        }
        return ret;

    }

    // creates a sorted array of a Chessuser nodelist
    // returns null if the nodelist is empty

    public Chessuser[] sortedArray(Chessuser first) {

        int count = nodeCount(first);

        if (count == 0) return null;

        Chessuser[] sarray = new Chessuser[count];
        Chessuser[] ret;

        int ceil = Integer.MAX_VALUE;

        ceil = findHighestRating(first, ceil);
        int base = 0;

        while(ceil != 0) {

            ret = findUsersRating(first, ceil);

            for (int i = 0; i < ret.length; i++) {
                sarray[base] = ret[i]; base++;
            }

            ceil = findHighestRating(first, ceil);
        }
        return sarray;

    }
    // Metod for calculating how much rating the player with the lowest rating gains during a remis

    public static int ratingCalcRemisLow(int a, int b) {
        double low = a;
        double high = b;
        double awc = (1 / (1 + (java.lang.Math.pow(10.00000, ((high - low) / 400.00000)))));
        double winrating = 15 * (1 - awc);
        int ratechange = (int) winrating;

        return ratechange;



    }
    // Metod for calculating how much rating the player with the highest rating loses during a remis

    public static int ratingCalcRemisHigh(int a, int b) {
        double low = a;
        double high = b;
        double awc = (1 / (1 + (java.lang.Math.pow(10.00000, ((low - high) / 400.00000)))));
        double loserating = 15 * (0 - awc);
        int ratechange = (int) loserating;

        return ratechange;
    }

        // Method for getting the name of the columns for the scoreboard
	    public String[] getColumnNames()
	    {
	        return new String[] {"User Name","Wins","Remis","Losses","Matches","Rating"};

	    }


	    public String[][] createScoreBoard(Chessuser first)
	    {
	        Chessuser[] userinfo = sortedArray(first);

	        if (userinfo == null)
	        {
				return null;
			}
	        String[][] data = new String[userinfo.length][getColumnNames().length];

	        for (int row = 0; row < userinfo.length; row++)
	            data[row] = userinfo[row].toRow() ;

	        return data;
    }

}
