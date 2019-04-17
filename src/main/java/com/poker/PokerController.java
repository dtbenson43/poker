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
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This is a sample class to launch a rule.
 */
public class PokerController {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
            KnowledgeBase kbase = readKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
            // go !
            Game message = new Game();
            ksession.insert(message);
            ksession.fireAllRules();
            logger.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

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

    public static class Game {
    	public static final int PLAYER = 0;
    	public static final int AGENT = 1;
    	
    	public static final int NEW_GAME = 0;
    	public static final int NEW_HAND = 1;
    	public static final int PREFLOP = 2;
    	public static final int FLOP = 3;
    	public static final int TURN = 4;
    	public static final int RIVER = 5;
    	public static final int SHOWDOWN = 6;
    	
    	public static final int FOLD = 0;
    	public static final int CALL = 1;
    	public static final int RAISE = 2;
    	
    	public static final int SMALL_BLIND = 25;
    	public static final int BIG_BLIND = 50;
    	public static final int CHIPS = 1000;
    	
        private int gameState;
    	private ArrayList<Card> agentHand;
    	private ArrayList<Card> playerHand;
    	private ArrayList<Card> board;
    	private ArrayList<Card> deck;
    	private int playerChips;
    	private int agentChips;
    	private int smallBlind;
    	private int bigBlind;
    	private int pot;
    	private int minBet;
    	private int currentBet;
    	private int playerAction;
    	private int agentAction;
    	
    	private String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"}; 
    	private String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    	
    	private Scanner scnr = new Scanner(System.in);
    	
    	private int turn;
    	
    	public Game() {
    		this.turn = 0;
    		if(Math.random() < 0.5) {
    			this.turn = 1;
    		}
    	}
    	
    	public void newGame() {
    		this.gameState = Game.NEW_GAME;
        	this.playerChips = Game.CHIPS;
        	this.agentChips = Game.CHIPS;
        	this.smallBlind = Game.SMALL_BLIND;
        	this.bigBlind = Game.BIG_BLIND;
        	this.pot = 0;
    	}
    	
    	public void newHand() {
        	agentHand = new ArrayList<Card>();
        	playerHand = new ArrayList<Card>();
        	board = new ArrayList<Card>();
    		deck = new ArrayList<Card>();
    		this.currentBet = 0;
        	this.pot = 0;
    		this.minBet = Game.BIG_BLIND;
    	}
    	
    	public void gameLoop() {
    		bettingLoop();
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		for(Card c: board) {
    			System.out.println(ranks[c.getRank()] + " of " + suits[c.getSuit()]);
    		}
    		bettingLoop();
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		for(Card c: board) {
    			System.out.println(ranks[c.getRank()] + " of " + suits[c.getSuit()]);
    		}
    		bettingLoop();
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		for(Card c: board) {
    			System.out.println(ranks[c.getRank()] + " of " + suits[c.getSuit()]);
    		}
    		bettingLoop();
    	}
    	
    	public void buildDeck() {
    		for(int i = 0; i < 4; i++) {
    			for(int j = 0; j < 13; j++) {
    				deck.add(new Card(i, j));
    			}
    		}
    		System.out.println(deck.size());
    	}
    	
    	public void dealBoardCard() {
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    	}
    	

    	public void dealHandCards() {
    		playerHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		playerHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		
    		agentHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		agentHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		
    	}
    	
    	public void getPlayerAction() {
    		
    	}
    	
    	public void bettingLoop() {
    		if(pot == 0) {
    			if(turn == 1) {
        			pot += smallBlind + bigBlind;
        			playerChips -= smallBlind;
        			agentChips -= bigBlind;
        			playerBet(25);
        		}
        		else {
        			agentBet(25);
        		}
    		}
    		else {
    			if(turn == 1) {
        			playerBet(0);
        		}
        		else {
        			agentBet(0);
        		}
    		}
    	}
    	
    	public void playerBet(int chipsToCall) {
    		System.out.println("Your Cards:");
    		for(Card c: playerHand) {
    			System.out.println(ranks[c.getRank()] + " of " + suits[c.getSuit()]);
    		}
    		System.out.println("Enter your choice (case sensitive):");
    		if(chipsToCall > 0) {
	    		System.out.println("1. Call " + chipsToCall + " chips");
	    		System.out.println("2. Raise");
	    		System.out.println("3. Fold");
	    		bettingRules(scnr.nextInt(), chipsToCall);
    		}
    	}
    	
    	public void agentBet(int chipsToCall) {
    		pot += chipsToCall;
			agentChips -= chipsToCall;
			return;
    	}
    	
    	public void bettingRules(int choice, int chipsToCall) {
    		int bet = 0;
    		
    		if(choice == 1) {
    			pot += chipsToCall;
    			playerChips -= chipsToCall;
    			if(chipsToCall == smallBlind) {
    				agentBet(0);
    			}
    			return;
    		}
    		if(choice == 2) {
    			System.out.println("Enter the amount you want to raise: ");
    			bet = scnr.nextInt();
    			if(chipsToCall > bigBlind) {
    				while(bet < chipsToCall * 2) {
        				System.out.println("Raise must be at least" + bigBlind + " chips");
        				bet = scnr.nextInt();
        			}
    			}
    			else {
    				while(bet < bigBlind) {
        				System.out.println("Raise must be at least" + bigBlind + " chips");
        				bet = scnr.nextInt();
        			}
    			}
   
    			if((chipsToCall + bet) > playerChips) {
    				bet = playerChips - chipsToCall;
    			}
    			pot += chipsToCall + bet;
    			playerChips -= chipsToCall + bet;
    			
    			agentBet(bet);
    		}
    		else {
    			agentChips += pot;
    			pot = 0;
    			return;
    		}
    	}

		public int getGameState() {
			return gameState;
		}

		public void setGameState(int gameState) {
			this.gameState = gameState;
		}

		public ArrayList<Card> getAgentHand() {
			return agentHand;
		}

		public void setAgentHand(ArrayList<Card> agentHand) {
			this.agentHand = agentHand;
		}

		public ArrayList<Card> getPlayerHand() {
			return playerHand;
		}

		public void setPlayerHand(ArrayList<Card> playerHand) {
			this.playerHand = playerHand;
		}

		public ArrayList<Card> getBoard() {
			return board;
		}

		public void setBoard(ArrayList<Card> board) {
			this.board = board;
		}

		public ArrayList<Card> getDeck() {
			return deck;
		}

		public void setDeck(ArrayList<Card> deck) {
			this.deck = deck;
		}

		public int getPlayerChips() {
			return playerChips;
		}

		public void setPlayerChips(int playerChips) {
			this.playerChips = playerChips;
		}

		public int getAgentChips() {
			return agentChips;
		}

		public void setAgentChips(int agentChips) {
			this.agentChips = agentChips;
		}

		public int getPot() {
			return pot;
		}

		public void setPot(int pot) {
			this.pot = pot;
		}

		public int getMinBet() {
			return minBet;
		}

		public void setMinBet(int minBet) {
			this.minBet = minBet;
		}

		public int getCurrentBet() {
			return currentBet;
		}

		public void setCurrentBet(int currentBet) {
			this.currentBet = currentBet;
		}

		public int getAgentAction() {
			return agentAction;
		}

		public void setAgentAction(int agentAction) {
			this.agentAction = agentAction;
		}

		public int getTurn() {
			return turn;
		}

		public void setTurn(int turn) {
			this.turn = turn;
		}

		public void setPlayerAction(int playerAction) {
			this.playerAction = playerAction;
		}
    }
}
