import java.util.*;

import static java.lang.System.*;

/*
 * The Pig game
 * See http://en.wikipedia.org/wiki/Pig_%28dice_game%29
 *
 */
public class Pig {

    public static void main(String[] args) {
        new Pig().program();
    }

    // The only allowed instance variables (i.e. declared outside any method)
    // Accessible from any method
    final Scanner sc = new Scanner(in);
    final Random rand = new Random();

    void program() {
        //test();                 // <-------------- Uncomment to run tests!

        final int winPts = 100;    // Points to win
        List<Player> players; // List of players
        Player actual;            // Actual player for round (must use)
        boolean aborted = false;  // Game aborted by player?
        boolean gameOver = false;


        welcomeMsg(winPts); // Welcome the user and inform them of the win condition
        players = getPlayers(); // Initialize the list of players with IO method prompting user to enter amount and name of players
        int nPlayers = players.size();
        statusMsg(players); // Print the current score (0 for everyone)
        actual = players.get(0); // Set the actual player to the first one in the list


        while (!gameOver && !aborted) {
            boolean turnOver = false;

            while (!turnOver) {
                String choiceAsString = getPlayerChoice(actual, players); // Ask the user what they want their action to be
                char choice = choiceAsString.charAt(0); // Get the first letter typed by the user as a char
                switch (choice) { // Interpret the letter typed and act accordingly
                    case 'r':
                        int roll = rollDice();
                        if (roll != 1) {
                            actual.roundPts += roll;
                        } else {
                            actual.roundPts = 0;
                            turnOver = true;
                        }
                        roundMsg(roll, actual);
                        break;
                    case 'n':
                        actual.totalPts += actual.roundPts;
                        actual.roundPts = 0;
                        turnOver = true;
                        break;
                    case 'q':
                        turnOver = true;
                        aborted = true;
                        break;
                }
            }
            if (actual.totalPts >= winPts) { // After the player is finished with their turn, check if they have won
                gameOver = true;
            } else { // If the game isn't over...
                statusMsg(players); // Print the score
                int actualIndex = players.indexOf(actual); // Get the index of the current player in the list
                actual = players.get((actualIndex + 1) % nPlayers); // Change "current" player to the next one in the list
            }

        }


        gameOverMsg(actual, aborted);
    }

    // ---- Game logic methods --------------

    int rollDice() {
        int diceRoll = rand.nextInt(5) + 1; // Get a random integer between 1 and 6 (inclusive)
        return diceRoll;
    }

    // ---- IO methods ------------------

    void welcomeMsg(int winPoints) {
        out.println("Welcome to PIG!");
        out.println("First player to get " + winPoints + " points will win!");
        out.println("Commands are: r = roll , n = next, q = quit");
        out.println();
    }

    void statusMsg(List<Player> players) {
        out.print("Points: ");
        for (int i = 0; i < players.size(); i++) {
            out.print(players.get(i).name + " = " + players.get(i).totalPts + " ");
        }
        out.println();
    }

    void roundMsg(int result, Player actual) {
        if (result > 1) {
            out.println("Got " + result + " running total are " + actual.roundPts);
        } else {
            out.println("Got 1 lost it all!");
        }
    }

    void gameOverMsg(Player player, boolean aborted) {
        if (aborted) {
            out.println("Aborted");
        } else {
            out.println("Game over! Winner is player " + player.name + " with "
                    + (player.totalPts + player.roundPts) + " points");
        }
    }

    String getPlayerChoice(Player player, List<Player> players) {
        out.print("Player is " + player.name + " > ");
        String choice;
        if (!player.isComputer) {
            choice = sc.nextLine();
        } else {
            choice = player.getComputedChoice(players);
        }
        return choice;
    }

    List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();

        out.print("How many players? > ");
        int numberOfPlayers = sc.nextInt();
        sc.nextLine();

        out.print("How many computers? > ");
        int numberOfComputers = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < numberOfPlayers; i++) {
            out.print("Name for player " + (i + 1) + " > ");
            String name = sc.nextLine();
            players.add(new Player(name, false));
        }
        for (int i = 0; i < numberOfComputers; i++) {
            //out.print("Name for computer " + (i+1) + " > ");
            String name = "Computer " + i;
            players.add(new Player(name, true));
        }

        return players;
    }

    // ---------- Class -------------
    // Class representing the concept of a player
    // Use the class to create (instantiate) Player objects
    class Player {
        String name;     // Default null
        int totalPts;    // Total points for all rounds, default 0
        int roundPts;    // Points for a single round, default 0
        boolean isComputer = false;

        Player(String name, boolean isComputer) {
            this.name = name;
            this.isComputer = isComputer;
        }

        public String getComputedChoice(List<Player> players) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String choice = "n";
            if (this.roundPts < 20) {
                choice = "r";
            }
            out.println("\n" + this.name + " chose: " + choice);
            return choice;
        }
    }


    // ----- Testing -----------------
    // Here you run your tests i.e. call your game logic methods
    // to see that they really work (IO methods not tested here)
    void test() {
        // This is hard coded test data, an array of Players 
        //Player[] players = {new Player("a"), new Player("b"), new Player("c")};


        exit(0);   // End program
    }
}



