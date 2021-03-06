import java.util.*;

/**
 * A class to Implement the other classes, creating a Blackjack game.
 * 
 * @author Rahul, Vivek
 *
 */
public class Game {

	/**
	 * Driver program for the Blackjack Game.
	 * @param args - default param for public static void main
	 */
	public static void main(String[] args) {
		Player player = new Player();
		Dealer dealer = new Dealer();

		while (player.money != 0) {
			Deck deck = new Deck();
//			deck.shuffle();
		    deck.stack();
			int bet = 0;

			System.out.println("You have " + player.money + ". How much do you want to bet?");
			Scanner sc = new Scanner(System.in);
			int betAmount = sc.nextInt();

			boolean betIsValid = false;
			while (betIsValid == false) { 
				if (betAmount <= 0) {
					System.out.println("Please enter a bet value more than $0");
					System.out.println("How much do you want to bet?");
					sc = new Scanner(System.in);
				} else if(betAmount > player.money) {
					System.out.println("Please enter a bet value less than your total money");
					System.out.println("How much do you want to bet?");
					sc = new Scanner(System.in);
				} else {
					bet = betAmount;
					betIsValid = true;
					break;
				}
				betAmount = sc.nextInt();
			}

			//initial hand is dealt
			player.dealCard(deck);
			dealer.dealCard(deck);
			player.dealCard(deck);

			//check for blackjack
			if (player.checkBlackjack() == true) {
				player.money += 1.5 * bet;
				System.out.println("Blackjack! You WIN!");
				System.out.println();
				resetGame(player, dealer);
			} else {
				//should never be true, but resets possible errors
				if (player.isBusted() == true) {
					System.out.println("GAME IS BROKEN");
					resetGame(player, dealer);
				}

				System.out.println("Showing: " + player.cardsValue);
				System.out.println();
				System.out.println("Would you like to HIT or STAY?");

				sc = new Scanner(System.in);
				String userInput = sc.next().toLowerCase();

				//handles user's hit options
				while(userInput.equals("stay") == false) {
					if (userInput.equals("hit")) {
						System.out.println();
						player.dealCard(deck);
						if (player.isBusted() == true) { break; }
					}
					System.out.println("Showing: " + player.cardsValue);
					System.out.println();
					System.out.println("Would you like to HIT or STAY?");
					sc = new Scanner(System.in);
					userInput = sc.next().toLowerCase();
				}

				//dealer logic proceeds according to specs in the dealer class
				while (dealer.limitReached != true) { dealer.action(deck); }

				//once all actions are done, determines a winner and readies next hand
				winner(player, dealer, bet);
			}
		}
		System.out.println("You have $0. GAME OVER!");
	}

	/**
	 * Prepares the game for the next hand, updating money in the process.
	 * @param player - the user playing the game
	 * @param dealer - the dealer A.I. created to play against
	 */
	public static void resetGame(Player player, Dealer dealer) {
		player.cardCount = 0;
		dealer.cardCount = 0;
		Deck deck = new Deck();
		deck.shuffle();

		player.cardCount = 0;
		dealer.cardCount = 0;
		player.cardsValue = 0;
		dealer.cardsValue = 0;
		player.aceCount = 0;
		dealer.aceCount = 0;
		dealer.limitReached = false;
		player.hand = new Card[12];
		dealer.hand = new Card[12];
	}

	/**
	 * Checks for a winner, displays a message, then calls resetGame().
	 * @param player - the player playing Blackjack
	 * @param dealer - the dealer
	 * @param bet - the amount that was wagered on this particular hand
	 */
	public static void winner(Player player, Dealer dealer, int bet) {
		if (player.isBusted()) {
			System.out.println("Player is showing " + player.cardsValue + ".");
			System.out.println("You BUSTED!");
			player.money -= bet;
		} else if (dealer.isBusted()) {
			System.out.print("Player is showing " + player.cardsValue + ": ");
			player.printHand();
			System.out.print("Dealer is showing " + dealer.cardsValue + ": ");
			dealer.printHand();
			System.out.println("Dealer busted. You WIN!");
			player.money += bet;
		} else {
			if (player.cardsValue > dealer.cardsValue) {
				System.out.print("Player is showing " + player.cardsValue + ": ");
				player.printHand();
				System.out.print("Dealer is showing " + dealer.cardsValue + ": ");
				dealer.printHand();
				System.out.println("You WIN!");
				player.money += bet;
			} else if (player.cardsValue < dealer.cardsValue) {
				System.out.print("Player is showing " + player.cardsValue + ": ");
				player.printHand();
				System.out.print("Dealer is showing " + dealer.cardsValue + ": ");
				dealer.printHand();
				System.out.println("You LOST!");
				player.money -= bet;
			} else if (player.cardsValue == dealer.cardsValue) {
				if (player.cardCount < dealer.cardCount) {
					System.out.print("Player is showing " + player.cardsValue + ": ");
					player.printHand();
					System.out.print("Dealer is showing " + dealer.cardsValue + ": ");
					dealer.printHand();
					System.out.println("You WIN!");
					player.money += bet;
				} else if (player.cardCount > dealer.cardCount){
					System.out.print("Player is showing " + player.cardsValue + ": ");
					player.printHand();
					System.out.print("Dealer is showing " + dealer.cardsValue + ": ");
					dealer.printHand();
					System.out.println("You LOST!");
					player.money -= bet;
				} else {
					System.out.print("Player is showing " + player.cardsValue + ": ");
					player.printHand();
					System.out.print("Dealer is showing " + dealer.cardsValue + ": ");
					dealer.printHand();
					System.out.println("It's a tie.");
				}
			}
		}
		System.out.println();
		resetGame(player, dealer);
	}
}


