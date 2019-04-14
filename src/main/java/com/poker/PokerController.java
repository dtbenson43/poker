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
    	public static int PLAYER = 1;
    	public static int AGENT = 0;
    	
    	private ArrayList<Card> AgentHand;
    	private ArrayList<Card> PlayerHand;
    	private ArrayList<Card> Board;
    	private ArrayList<Card> Deck;
    	private int playerChips = 1000;
    	private int agentChips = 1000;
    	private int smallBlind = 25;
    	private int bigBlind = 50;
    	private int pot = 0;
    	private Scanner scnr = new Scanner(System.in);
    	
    	private int turn;
    	
    	public Game() {
    		
    		this.turn = 0;
    		if(Math.random() < 0.5) {
    			this.turn = 1;
    		}
    		
        	this.newHand();
    	}
    	
    	private void newHand() {
        	AgentHand = new ArrayList<Card>();
        	PlayerHand = new ArrayList<Card>();
        	Board = new ArrayList<Card>();
        	
    		this.buildDeck();
    		this.dealCards();
    	}
    	
    	private void buildDeck() {
    		Deck = new ArrayList<Card>();
    		
    		for(int i = 0; i < 4; i++) {
    			for(int j = 0; j < 13; j++) {
    				Deck.add(new Card(i, j));
    			}
    		}
    		System.out.println(Deck.size());
    	}
    	
    	private void dealCards() {
 
    		PlayerHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		PlayerHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		
    		AgentHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		AgentHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		
    	}
    	
    	private void bettingLoop() {
    		
    		playerBet();
    		botBet();
    	}
    	
    	private void playerBet(int chipsToCall) {
    		System.out.println("Your Cards:");
    		System.out.println("Enter your choice (case sensitive):");
    		if(chipsToCall > 0) {
	    		System.out.println("1. Call " + chipsToCall + " chips");
	    		System.out.println("2. Raise");
	    		System.out.println("3. Fold");
	    		bettingRules(scnr.nextInt(), chipsToCall);
    		}
    	}
    	
    	private void bettingRules(int choice, int chipsToCall) {
    		int bet = 0;
    		
    		if(choice == 1) {
    			pot += chipsToCall;
    			playerChips -= chipsToCall;
    		}
    		if(choice == 2) {
    			System.out.println("Enter the amount you want to raise: ");
    			bet = scnr.nextInt();
    			while(bet < bigBlind) {
    				System.out.println("Raise must be at least" + bigBlind + " chips");
    				bet = scnr.nextInt();
    			}
    			if((chipsToCall + bet) > playerChips) {
    				bet = playerChips - chipsToCall;
    			}
    			pot += chipsToCall + bet;
    			playerChips -= chipsToCall + bet;
    		}
    		if(choice == 3) {
    			agentChips += pot;
    			pot = 0;
    		}
    	}

		public int getTurn() {
			return turn;
		}

		public void setTurn(int turn) {
			this.turn = turn;
		}
    	
    	
    }
}
