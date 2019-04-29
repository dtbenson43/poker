package com.poker;

import java.util.ArrayList;
import java.util.Scanner;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * PokerController
 * A class that uses a jboss drools rule engine to run the poker game
 * 
 * 
 * @author Daniel Benson, and Allen Austen Riffee
 * @version 1.0
 * 
 * CAP4601   Project #: 2
 * File Name: PokerController.java
 */
public class PokerController {
	/**
	 * Main class that creates the knowledge base and starts the game
	 * @param args - empty
	 */
    public static final void main(String[] args) {
        try {
            KnowledgeBase kbase = readKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            
            Game message = new Game();
            
            ksession.insert(message);
            ksession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Initializes the knowledge base
     * @return - returns the knowledge base
     * @throws Exception - when the knowledge base cannot compute the information
     */
    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("AgentHoldEm.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }

    /**
     * Game
     * An inner class that uses a jboss drools rule engine to run the poker game and keep track of/updates game information
     * 
     * 
     * @author Daniel Benson, and Allen Austen Riffee
     * @version 1.0
     * 
     * CAP4601   Project #: 2
     * File Name: PokerController.java
     */
    public static class Game {
    	/**
    	 * Integer representing the player
    	 */
    	public static final int PLAYER = 0;
    	/**
    	 * Integer representing the agent
    	 */
    	public static final int AGENT = 1;
    	
    	/**
    	 * Integer representing the new game game state
    	 */
    	public static final int NEW_GAME = 0;
    	/**
    	 * Integer representing the new hand game state
    	 */
    	public static final int NEW_HAND = 1;
    	/**
    	 * Integer representing the preflop betting round game state
    	 */
    	public static final int PREFLOP = 2;
    	/**
    	 * Integer representing the game state where the flop cards are dealt
    	 */
    	public static final int FLOPDEAL = 3;
    	/**
    	 * Integer representing the flop betting round game state
    	 */
    	public static final int FLOP = 4;
    	/**
    	 * Integer representing the game state where the turn card is dealt
    	 */
    	public static final int TURNDEAL = 5;
    	/**
    	 * Integer representing the turn betting round game state
    	 */
    	public static final int TURN = 6;
    	/**
    	 * Integer representing the game state where the river card is dealt
    	 */
    	public static final int RIVERDEAL = 7;
    	/**
    	 * Integer representing the river betting round game state
    	 */
    	public static final int RIVER = 8;
    	/**
    	 * Integer representing the showdown game state
    	 */
    	public static final int SHOWDOWN = 9;
    	/**
    	 * Integer representing the end of the game game state
    	 */
    	public static final int END = 10;
    	
    	/**
    	 * Boolean variable that keeps track of whether the agent evaluated their current hand already
    	 */
    	public boolean dataUpdated = false;
    	
    	/**
    	 * Small blind bet amount
    	 */
    	public static final int SMALL_BLIND = 25;
    	/**
    	 * Big blind bet amount
    	 */
    	public static final int BIG_BLIND = 50;
    	/**
    	 * Starting chip amount
    	 */
    	public static final int CHIPS = 1000;
    	
    	/**
    	 * Integer that keeps track of the current game state
    	 */
    	private int gameState; 
    	/**
    	 * Small blind bet amount
    	 */
        private int smallBlind;
        /**
    	 * Big blind bet amount
    	 */
    	private int bigBlind;
    	/**
    	 * The current pot
    	 */
    	private int pot;  	
    	
    	/**
    	 * Array list of cards that represents the deck
    	 */
        private ArrayList<Card> deck;
        /**
    	 * Array list of cards that represents the agent's hand
    	 */
    	private ArrayList<Card> agentHand;
    	/**
    	 * Array list of cards that represents the player's hand
    	 */
    	private ArrayList<Card> playerHand;
    	/**
    	 * Array list of cards that represents the board cards
    	 */
    	private ArrayList<Card> board;

    	/**
    	 * true if the player is all in
    	 */
    	private boolean playerAllIn;
    	/**
    	 * true if the agent is all in
    	 */
    	private boolean agentAllIn;
    	/**
    	 * the amount of chips the player has at the start of a hand
    	 */
    	private int playerChipsStartOfHand;
    	/**
    	 * the amount of chips the agent has at the start of a hand
    	 */
    	private int agentChipsStartOfHand;
    	/**
    	 * the current amount of player chips
    	 */
		private int playerChips;
		/**
    	 * the current amount of agent chips
    	 */
    	private int agentChips;
    	
    	/**
    	 * the last bet placed by either player
    	 */
    	private int currentBet;
    	/**
    	 * Ensures both players have an opportunity to go
    	 */
    	private boolean prevBetOrCheck;
    	/**
    	 * Keeps track of which player is the dealer
    	 */
    	private int dealer;
    	/**
    	 * Keeps track of who's turn it is
    	 */
    	private int turn;
    	
    	/**
    	 * Array of characters representing the 4 possible suits
    	 */
		private char[] suits = {(char)'\u2665', (char)'\u2666', (char)'\u2663', (char)'\u2660'}; 
		/**
    	 * Array of Strings representing the 13 possible ranks
    	 */
    	private String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    	
    	/**
    	 * Scanner for user input
    	 */
    	private Scanner scnr = new Scanner(System.in);
    	/**
    	 * Keeps track of whether the agent has evaluated it's hand in the current game state
    	 */
		private boolean agentEvaluation;
		/**
		 * The current hand strength of the agent
		 */
		private double currentHS;
		/**
		 * The current effective hand strength of the agent
		 */
		private double currentEHS;
		/**
		 * The current potential of the agent's hand to increase in strength
		 */
		private double currentPPot;
		/**
		 * The current potential of the agent's hand to increase in strength
		 */
		private double currentNPot;
    	
		/**
		 * Default constructor that initializes the game
		 */
    	public Game() {
    		this.turn = 0;
    		this.dealer = 0;
    		if(Math.random() < 0.5) {
    			this.turn = 1;
    			this.dealer = 1;
    		}
    	}
    	
    	/**
    	 * Creates a new game, initializes variables with default values
    	 */
    	public void newGame() {
    		this.gameState = Game.NEW_GAME;
        	this.playerChips = Game.CHIPS;
        	this.agentChips = Game.CHIPS;
        	this.smallBlind = Game.SMALL_BLIND;
        	this.bigBlind = Game.BIG_BLIND;
        	this.pot = 0;
        	this.agentEvaluation = false;
        	this.currentHS = 0;
        	this.currentEHS = 0;
        	this.currentPPot = 0;
        	this.currentNPot = 0;
    	}
    	
    	/**
    	 * Creates a new hand
    	 */
		public void newHand() {
        	agentHand = new ArrayList<Card>();
        	playerHand = new ArrayList<Card>();
        	board = new ArrayList<Card>();
    		deck = buildDeck();
    		dealHandCards();
    		this.currentBet = 0;
        	this.pot = 0;
    		dealer = dealer == PLAYER ? AGENT : PLAYER; // passes the dealer "chip"
    		turn = dealer;
    		dataUpdated = false;
    		agentChipsStartOfHand = agentChips;
    		playerChipsStartOfHand = playerChips;
    		setAgentAllIn(false);
    		setPlayerAllIn(false);
    		postBlinds();
    	}
    	
		/**
		 * Builds the deck of cards
		 * @return - the deck of cards
		 */
    	public static ArrayList<Card> buildDeck() {
    		ArrayList<Card> temp = new ArrayList<Card>();
    		for(int i = 0; i < 4; i++) {
    			for(int j = 0; j < 13; j++) {
    				temp.add(new Card(i, j));
    			}
    		}
    		return temp;
    	}
    	
    	/**
    	 * Deals a card to the board
    	 */
    	public void dealBoardCard() {
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    	}
    	
    	/**
    	 * Prints the current board cards
    	 */
    	public void printBoardCards() {
    		System.out.println("Board Cards:");
    		for(Card c: board) {
    			System.out.print(ranks[c.getRank()] + suits[c.getSuit()] + " ");
    		}
    		System.out.println("");
    	}
    	
    	/**
    	 * Deals cards to the player and the agent
    	 */
    	public void dealHandCards() {
    		playerHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		playerHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		
    		agentHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		agentHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		
    	}
    	
    	/**
    	 * Prints the player's cards
    	 */
    	public void printPlayerHand() {
    		System.out.println("Your Cards:");
    		for(Card c: playerHand) {
    			System.out.print(ranks[c.getRank()] + suits[c.getSuit()] + " ");
    		}
    		System.out.println("");
    	}
    	
    	/**
    	 * Prints the agent's cards
    	 */
    	public void printAgentHand() {
    		System.out.println("Agent Cards:");
    		for(Card c: agentHand) {
    			System.out.print(ranks[c.getRank()] + suits[c.getSuit()] + " ");
    			System.out.println("");
    		}
    	}
    	
    	/**
    	 * Returns the index of a specific card in any arraylist of cards
    	 * @param cards - arraylist of cards
    	 * @param card - card to search for
    	 * @return - the index of the card to search for in the arraylist of cards
    	 */
    	public static int indexOfCard(ArrayList<Card> cards, Card card) {
    		int count = 0;
    		for(Card comp : cards) {
    			if (card.equals(comp)) {
    				return count;
    			}
    			count++;
    		}
    		
    		return -1;
    	}
    	
    	/**
    	 * Allows player to bet
    	 * @param chipsToCall - agent's previous bet
    	 */
    	public void playerBet(int chipsToCall) {
    		if(playerAllIn == true) { //if the player is already all in, return
    			return;
    		}
    		printPlayerHand();
    		System.out.println("");
    		
    		// Player's choices
    		System.out.println("Enter your choice:");
    		if(chipsToCall > 0) {
	    		System.out.println("1. Call " + chipsToCall + " chips");
	    		System.out.println("2. Raise");
	    		System.out.println("3. Fold");
	    		bettingRules(scnr.nextInt(), chipsToCall);
    		}
    		else {
    			System.out.println("1. Check");
	    		System.out.println("2. Bet - Must be greater than " + bigBlind + " chips");
	    		System.out.println("3. Fold");
	    		bettingRules(scnr.nextInt(), chipsToCall);
    		}
    	}
    	
    	/**
    	 * Enforces betting rules for player
    	 * @param choice - player's choice of action
    	 * @param chipsToCall - agent's previous bet
    	 */
    	public void bettingRules(int choice, int chipsToCall) {
    		int bet = 0;
    		
    		if(choice == 1) { // if call, call
    			if(chipsToCall >= playerChips) { // if player doesnt have enough chips, go all in
        			playerGoAllIn();
        		} 
    			else { // call
	    			pot += chipsToCall;
	    			playerChips -= chipsToCall;
	    			currentBet = 0;
	    			turn = AGENT;
	   
	    			if(isPrevBetOrCheck()) { // if both players have gone, advance game state
	    				gameState += 1;
	    				turn = dealer == PLAYER ? AGENT : PLAYER;
	    				prevBetOrCheck = false;
	    			}
	    			else { // if both players haven't had the opportunity to bet, don't advance game state
	    				setPrevBetOrCheck(true);
	    			}
    			}
    			return;
    		}
    		if(choice == 2) { // Bet/ Raise
    			System.out.println("Enter the amount you want to raise by: ");
    			bet = scnr.nextInt();
    			
    			if(chipsToCall > bigBlind) { // If there is a bet to call, check raise amount
    				while(bet < chipsToCall) {// If bet is too low ask again
        				System.out.println("Raise must be at least" + chipsToCall + " chips");
        				bet = scnr.nextInt();
        			}
    			}
    			else { // If there is not a bet to call, check raise amount
    				while(bet < bigBlind) { // If bet is too low ask again
        				System.out.println("Raise must be at least" + bigBlind + " chips");
        				bet = scnr.nextInt();
        			}
    			}
    			
    			currentBet = bet;
    			
    			if((bet + chipsToCall) >= playerChips) { // if raise is more than player chips, go all in
    				playerGoAllIn();
    			}
    			else { // else, raise
	    			pot += bet + chipsToCall;
	    			playerChips -= bet + chipsToCall;
	    			
    			}
    			setPrevBetOrCheck(true);
    			
    			turn = AGENT;
    		}
    		else { // fold
    			agentChips += pot;
    			pot = 0;
    			gameState = NEW_HAND;
    			return;
    		}
    	}
    	
    	/**
    	 * Allows agent to bet
    	 * @param chipsToCall - player's previous bet
    	 */
    	public void agentBet(int chipsToBet, int chipsToCall) {
    		if(agentAllIn == true) { //if the agent is already all in, return
    			return;
    		}
    		
    		// Lets the player know the agent went all in
    		if(chipsToBet + chipsToCall >= agentChips) {
    			agentGoAllIn();
    			System.out.println("Agent went All-In");
    		} 
    		
    		else {
    			System.out.println("Agent raises by " + chipsToBet + " chips");
        		pot += chipsToBet + chipsToCall;
    			agentChips -= chipsToBet + chipsToCall;
    			
    			currentBet = chipsToBet;
    			prevBetOrCheck = true;
    			turn = PLAYER;
    		}
			return;
    	}
    	/**
    	 * Agent calls the player's previous bet
    	 * @param chipsToCall - the player's previous bet
    	 */
    	public void agentCall(int chipsToCall) {
    		// Lets the player know the agent went all in
    		if(chipsToCall >= agentChips) {
    			agentGoAllIn();
    			System.out.println("Agent went All-In");
    		} 
			else {
				if(chipsToCall !=0) { // if chips to call, call
					System.out.println("Agent calls " + chipsToCall + " chips");
				}	
				else { // if no chips to call, check
					System.out.println("Agent checks");
				}
    			pot += chipsToCall;
    			agentChips -= chipsToCall;
    			currentBet = 0;
    			turn = PLAYER;
   
    			if(isPrevBetOrCheck()) { // if both players had the opportunity to go, advance game state
    				gameState += 1;
    				turn = dealer == PLAYER ? AGENT : PLAYER;
    				prevBetOrCheck = false;
    			}
    			else { // if both players haven't the opportunity to go, don't advance game state, update game info
    				setPrevBetOrCheck(true);
    			}
			}
    	}
    	/**
    	 * Agent folds the hand
    	 */
    	public void agentFold() {
    		System.out.println("Agent has folded");
    		playerWin();
    		setAgentEvaluation(false);
    		gameState = NEW_HAND;
  
    	}
    	
    	/**
    	 * Player goes all in
    	 */
    	public void playerGoAllIn() {
    		pot += playerChips;
    		playerChips = 0;
    		setPlayerAllIn(true);
    		if(pot > playerChipsStartOfHand * 2) {
    			agentChips += (pot - (playerChipsStartOfHand * 2));
    			pot = playerChipsStartOfHand * 2;
    		}
    		else {
    			currentBet = (playerChipsStartOfHand * 2) - pot;
    			if(currentEHS > 0.90) {
    				agentBet(0, currentBet);
    			}
    			else if(currentEHS == 0 && currentHS > 0.90) {
    				agentBet(0, currentBet);
    			}
    			else {
    				agentFold();
    			}
    		}
    	}
    	/**
    	 * Agent goes all in
    	 */
    	public void agentGoAllIn() {
    		pot += agentChips;
    		agentChips = 0;
    		setAgentAllIn(true);
    		if(pot > agentChipsStartOfHand * 2) {
    			playerChips += (pot - (agentChipsStartOfHand * 2));
    			pot = agentChipsStartOfHand * 2;
    		}
    		else {
    			currentBet = (agentChipsStartOfHand * 2) - pot;
    			playerBet(currentBet);
    		}
    	}
    	
    	/**
    	 * Both players post blinds, amounts depending on who's dealer
    	 */
    	public void postBlinds() {
    		if (dealer == PLAYER) {
    			if (playerChips < smallBlind) {
    				pot += playerChips;
    				playerChips = 0;
    			}
    			else
    			{
    				pot += smallBlind;
    				playerChips -= smallBlind;
    			}
    			if (agentChips < bigBlind) {
    				pot += agentChips;
    				agentChips = 0;
    			}
    			else
    			{
    				pot += bigBlind;
    				agentChips -= bigBlind;
    			}
    		} 
    		else {
    			if (playerChips < bigBlind) {
    				pot += playerChips;
    				playerChips = 0;
    			}
    			else
    			{
    				pot += bigBlind;
    				playerChips -= bigBlind;
    			}
    			if (agentChips < smallBlind) {
    				pot += agentChips;
    				agentChips = 0;
    			}
    			else
    			{
    				pot += smallBlind;
    				agentChips -= smallBlind;
    			}
    		}
    	}
    	/**
    	 * Prints a hand of cards
    	 * @param hand - hand of cards
    	 */
    	public void printHand(ArrayList<Card> hand) {
    		for(Card c: hand) {
    			System.out.print(ranks[c.getRank()] + suits[c.getSuit()] + " ");
    		}
    		System.out.println("");
    	}
    	
    	/**
    	 * Transfers pot to agent when agent wins 
    	 */
    	public void agentWin() {
    		agentChips += pot;
    		pot = 0;
    	}
    	
    	/**
    	 * Transfers pot to player when player wins 
    	 */
    	public void playerWin() {
    		playerChips += pot;
    		pot = 0;
    	}
    	
    	/**
    	 * Splits pot between agent and player
    	 */
    	public void splitPot() {
    		playerChips += pot/2;
    		agentChips += pot/2;
    		pot = 0;
    	}
    	
    	/**
    	 * Updates the agent's evaluation
    	 */
    	public void updateData() {
    		this.currentHS = Evaluator.handStrength(this.agentHand, this.board);
    		if (this.gameState > Game.PREFLOP) {
        		double[] temp = Evaluator.handPotential(this.agentHand, this.board);
        		this.currentPPot = temp[1];
        		this.currentNPot = temp[2];
        		this.currentEHS = Evaluator.effectiveHandStrength(this.currentEHS, this.currentPPot, this.currentNPot);
        		dataUpdated = true;
    		}
    	}
    	
    	/**
    	 * Returns current game state
    	 * @return - current game state
    	 */
		public int getGameState() {
			return gameState;
		}
		/**
		 * Sets current game state
		 * @param gameState - current game state
		 */
		public void setGameState(int gameState) {
			this.gameState = gameState;
		}
		/**
		 * Returns agent's hand
		 * @return - agent's hand
		 */
		public ArrayList<Card> getAgentHand() {
			return agentHand;
		}
		/**
		 * Sets agent's hand
		 * @param agentHand - agent's hand
		 */
		public void setAgentHand(ArrayList<Card> agentHand) {
			this.agentHand = agentHand;
		}
		/**
		 * Returns player's hand
		 * @return - player's hand
		 */
		public ArrayList<Card> getPlayerHand() {
			return playerHand;
		}
		/**
		 * Sets player's hand
		 * @param playerHand - player's hand
		 */
		public void setPlayerHand(ArrayList<Card> playerHand) {
			this.playerHand = playerHand;
		}
		/**
		 * Returns board cards
		 * @return - board cards
		 */
		public ArrayList<Card> getBoard() {
			return board;
		}
		/**
		 * Sets board cards
		 * @param board - board cards
		 */
		public void setBoard(ArrayList<Card> board) {
			this.board = board;
		}
		/**
		 * Returns deck
		 * @return - deck
		 */
		public ArrayList<Card> getDeck() {
			return deck;
		}
		/**
		 * Sets deck
		 * @param deck - deck
		 */
		public void setDeck(ArrayList<Card> deck) {
			this.deck = deck;
		}
		/**
		 * Returns player chips
		 * @return - player chips
		 */
		public int getPlayerChips() {
			return playerChips;
		}
		/**
		 * Sets player chips
		 * @param playerChips - player chips
		 */
		public void setPlayerChips(int playerChips) {
			this.playerChips = playerChips;
		}
		/**
		 * Returns Agent Chips
		 * @return - agent chips
		 */
		public int getAgentChips() {
			return agentChips;
		}
		/**
		 * Sets agent chips
		 * @param agentChips - agent chips
		 */
		public void setAgentChips(int agentChips) {
			this.agentChips = agentChips;
		}
		/**
		 * Returns pot
		 * @return - pot
		 */
		public int getPot() {
			return pot;
		}
		/**
		 * Sets pot
		 * @param pot - pot
		 */
		public void setPot(int pot) {
			this.pot = pot;
		}
		/**
		 * Returns previous bet
		 * @return - previous bet
		 */
		public int getCurrentBet() {
			return currentBet;
		}
		/**
		 * Sets current bet
		 * @param currentBet - current bet
		 */
		public void setCurrentBet(int currentBet) {
			this.currentBet = currentBet;
		}
		/**
		 * Returns who's turn it is
		 * @return - who's turn it is
		 */
		public int getTurn() {
			return turn;
		}
		/**
		 * Sets who's turn it is
		 * @param turn - who's turn it is
		 */
		public void setTurn(int turn) {
			this.turn = turn;
		}
		/**
		 * Returns whether both players had an opportunity to bet
		 * @return - whether both players had an opportunity to bet
		 */
		public boolean isPrevBetOrCheck() {
			return prevBetOrCheck;
		}
		/**
		 * Sets whether both players had an opportunity to bet
		 * @param prevBetOrCheck - whether both players had an opportunity to bet
		 */
		public void setPrevBetOrCheck(boolean prevBetOrCheck) {
			this.prevBetOrCheck = prevBetOrCheck;
		}
		/**
		 * Returns small blind
		 * @return - small blind
		 */
    	public int getSmallBlind() {
			return smallBlind;
		}
    	/**
    	 * Sets small blind
    	 * @param smallBlind - small blind
    	 */
		public void setSmallBlind(int smallBlind) {
			this.smallBlind = smallBlind;
		}
		/**
		 * Returns big blind
		 * @return - big blind
		 */
		public int getBigBlind() {
			return bigBlind;
		}
		/**
    	 * Sets big blind
    	 * @param bigBlind - big blind
    	 */
		public void setBigBlind(int bigBlind) {
			this.bigBlind = bigBlind;
		}
		/**
		 * Returns who is the dealer
		 * @return - who is the dealer
		 */
    	public int getDealer() {
			return dealer;
		}
    	/**
		 * Sets who is the dealer
		 * @param dealer - who is the dealer
		 */
		public void setDealer(int dealer) {
			this.dealer = dealer;
		}

		public boolean getPlayerAllIn() {
			return playerAllIn;
		}

		public void setPlayerAllIn(boolean playerAllIn) {
			this.playerAllIn = playerAllIn;
		}

		public boolean getAgentAllIn() {
			return agentAllIn;
		}

		public void setAgentAllIn(boolean agentAllIn) {
			this.agentAllIn = agentAllIn;
		}
		
		public int getBoardSize() {
			return board.size();
		}
		
    	public boolean isAgentEvaluation() {
			return agentEvaluation;
		}

		public void setAgentEvaluation(boolean agentEvaluation) {
			if(agentEvaluation == true) {
				if(!dataUpdated) {
					this.updateData();
				}
			}
			this.agentEvaluation = agentEvaluation;
		}
		
		public double getCurrentHS() {
			return currentHS;
		}

		public void setCurrentHS(double currentHS) {
			this.currentHS = currentHS;
		}

		public double getCurrentEHS() {
			return currentEHS;
		}

		public void setCurrentEHS(double currentEHS) {
			this.currentEHS = currentEHS;
		}

		public double getCurrentPPot() {
			return currentPPot;
		}

		public void setCurrentPPot(double currentPPot) {
			this.currentPPot = currentPPot;
		}

		public double getCurrentNPot() {
			return currentNPot;
		}

		public void setCurrentNPot(double currentNPot) {
			this.currentNPot = currentNPot;
		}

		public boolean isDataUpdated() {
			return dataUpdated;
		}

		public void setDataUpdated(boolean dataUpdated) {
			this.dataUpdated = dataUpdated;
		}
    }
}
