package com.poker;

import java.util.ArrayList;

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
    	
    	private int turn;
    	
    	public Game() {
    		this.turn = 0;
    		if(Math.random() < 0.5) {
    			this.turn = 1;
    		}
    		
        	this.newHand();
    	}
    	
    	private void newHand() {
        	ArrayList<Card> AgentHand = new ArrayList<Card>();
        	ArrayList<Card> PlayerHand = new ArrayList<Card>();
        	ArrayList<Card> Board = new ArrayList<Card>();
        	
    		this.buildDeck();
    		this.dealCards();
    	}
    	
    	private void buildDeck() {
    		ArrayList<Card> Deck = new ArrayList<Card>();
    		
    		for(int i = 0; i < 3; i++) {
    			for(int j = 0; j < 12; j++) {
    				Deck.add(new Card(i, j));
    			}
    		}
    	}
    	
    	private void dealCards() {
    		PlayerHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		PlayerHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		
    		AgentHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		AgentHand.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		
    		Board.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		Board.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    		Board.add(Deck.remove((int)(Math.random() * 1000000) % Deck.size()));
    	}

		public int getTurn() {
			return turn;
		}

		public void setTurn(int turn) {
			this.turn = turn;
		}
    	
    	
    }
}
