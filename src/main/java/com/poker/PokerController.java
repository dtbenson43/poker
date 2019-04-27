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
            KnowledgeBase kbase = readKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            
            Game message = new Game();
            
            ksession.insert(message);
            ksession.fireAllRules();
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
    	public static final int FLOPDEAL = 3;
    	public static final int FLOP = 4;
    	public static final int TURNDEAL = 5;
    	public static final int TURN = 6;
    	public static final int RIVERDEAL = 7;
    	public static final int RIVER = 8;
    	public static final int SHOWDOWN = 9;
    	
    	public static final int SMALL_BLIND = 25;
    	public static final int BIG_BLIND = 50;
    	public static final int CHIPS = 1000;
    	
    	private int gameState; 
        private int smallBlind;
    	private int bigBlind;
    	private int pot;  	
    	
        private ArrayList<Card> deck;
    	private ArrayList<Card> agentHand;
    	private ArrayList<Card> playerHand;
    	private ArrayList<Card> board;

    	
    	private boolean playerAllIn;
    	private boolean agentAllIn;
    	private int playerChipsStartOfHand;
    	private int agentChipsStartOfHand;
		private int playerChips;
    	private int agentChips;
    	
    	private int currentBet;
    	private boolean prevBetOrCheck;
    	private int dealer;
    	private int turn;
    	
		private String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"}; 
    	private String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    	
    	private Scanner scnr = new Scanner(System.in);
    	
    	public Game() {
    		this.turn = 0;
    		this.dealer = 0;
    		if(Math.random() < 0.5) {
    			this.turn = 1;
    			this.dealer = 1;
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
    		buildDeck();
    		dealHandCards();
    		this.currentBet = 0;
        	this.pot = 0;
    		dealer = dealer == PLAYER ? AGENT : PLAYER;
    		turn = dealer;
    		agentChipsStartOfHand = agentChips;
    		playerChipsStartOfHand = playerChips;
    		setAgentAllIn(false);
    		setPlayerAllIn(false);
    		postBlinds();
    	}
    	
    	public void buildDeck() {
    		for(int i = 0; i < 4; i++) {
    			for(int j = 0; j < 13; j++) {
    				deck.add(new Card(i, j));
    			}
    		}
    	}
    	
    	public void dealBoardCard() {
    		board.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    	}
    	
    	public void printBoardCards() {
    		System.out.println("Board Cards:");
    		for(Card c: board) {
    			System.out.println(ranks[c.getRank()] + " of " + suits[c.getSuit()]);
    		}
    		System.out.println("");
    	}
    	

    	public void dealHandCards() {
    		playerHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		playerHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		
    		agentHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		agentHand.add(deck.remove((int)(Math.random() * 1000000) % deck.size()));
    		
    	}
    	
    	public void printPlayerHand() {
    		System.out.println("Your Cards:");
    		for(Card c: playerHand) {
    			System.out.println(ranks[c.getRank()] + " of " + suits[c.getSuit()]);
    		}
    		System.out.println("");
    	}
    	
    	public void playerBet(int chipsToCall) {
    		printPlayerHand();
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
    	
    	public void agentBet(int chipsToCall) {
    		if(chipsToCall >= agentChips) {
    			agentGoAllIn();
    		} 
    		else {
        		pot += chipsToCall;
    			agentChips -= chipsToCall;
    			
    			System.out.println("Agent Chips = " + agentChips);
    			if (prevBetOrCheck) {
    				gameState += 1;
    				currentBet = 0;
    				turn = dealer == PLAYER ? AGENT : PLAYER;
    				prevBetOrCheck = false;
    			} 
    			else {
    				currentBet = 0;
    				prevBetOrCheck = true;
    				turn = PLAYER;
    			}
    		}
			return;
    	}
    	
    	public void bettingRules(int choice, int chipsToCall) {
    		int bet = 0;
    		
    		if(choice == 1) {
    			if(chipsToCall >= playerChips) {
        			playerGoAllIn();
        		} 
    			else {
	    			pot += chipsToCall;
	    			playerChips -= chipsToCall;
	    			System.out.println("Player Chips = " + playerChips);
	    			currentBet = 0;
	    			turn = AGENT;
	   
	    			if(isPrevBetOrCheck()) {
	    				gameState += 1;
	    				turn = dealer == PLAYER ? AGENT : PLAYER;
	    				prevBetOrCheck = false;
	    			}
	    			else {
	    				setPrevBetOrCheck(true);
	    			}
    			}
    			return;
    		}
    		if(choice == 2) {
    			System.out.println("Enter the amount you want to raise by: ");
    			bet = scnr.nextInt();
    			
    			if(chipsToCall > bigBlind) {
    				while(bet < chipsToCall) {
        				System.out.println("Raise must be at least" + chipsToCall + " chips");
        				bet = scnr.nextInt();
        			}
    			}
    			else {
    				while(bet < bigBlind) {
        				System.out.println("Raise must be at least" + bigBlind + " chips");
        				bet = scnr.nextInt();
        			}
    			}
    			currentBet = bet;
    			if((bet + chipsToCall) >= playerChips) {
    				playerGoAllIn();
    			}
    			else {
	    			pot += bet + chipsToCall;
	    			playerChips -= bet + chipsToCall;
	    			
    			}
    			System.out.println("Player Chips = " + playerChips);
    			setPrevBetOrCheck(true);
    			
    			turn = AGENT;
    		}
    		else {
    			agentChips += pot;
    			pot = 0;
    			System.out.println("Player Chips = " + playerChips);
    			System.out.println("Agent Chips = " + agentChips);
    			gameState = NEW_HAND;
    			return;
    		}
    	}
    	
    	public void playerGoAllIn() {
    		pot += playerChips;
    		playerChips = 0;
    		setPlayerAllIn(true);
    		if(pot > playerChipsStartOfHand * 2) {
    			agentChips += (pot - playerChipsStartOfHand * 2);
    			pot = playerChipsStartOfHand * 2;
    		}
    		else {
    			currentBet = (agentChipsStartOfHand * 2) - pot;
    		}
    	}
    	
    	public void agentGoAllIn() {
    		pot += agentChips;
    		agentChips = 0;
    		setAgentAllIn(true);
    		if(pot > agentChipsStartOfHand * 2) {
    			playerChips += (pot - agentChipsStartOfHand * 2);
    			pot = agentChipsStartOfHand * 2;
    		}
    		else {
    			currentBet = (agentChipsStartOfHand * 2) - pot;
    		}
    	}
    	
    	
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

		public int getCurrentBet() {
			return currentBet;
		}

		public void setCurrentBet(int currentBet) {
			this.currentBet = currentBet;
		}

		public int getTurn() {
			return turn;
		}

		public void setTurn(int turn) {
			this.turn = turn;
		}

		public boolean isPrevBetOrCheck() {
			return prevBetOrCheck;
		}

		public void setPrevBetOrCheck(boolean prevBetOrCheck) {
			this.prevBetOrCheck = prevBetOrCheck;
		}
		
    	public int getSmallBlind() {
			return smallBlind;
		}

		public void setSmallBlind(int smallBlind) {
			this.smallBlind = smallBlind;
		}

		public int getBigBlind() {
			return bigBlind;
		}

		public void setBigBlind(int bigBlind) {
			this.bigBlind = bigBlind;
		}
		
    	public int getDealer() {
			return dealer;
		}

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
    }
}
