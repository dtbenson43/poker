package com.poker;

import org.apache.commons.math3.util.CombinatoricsUtils;
import java.util.ArrayList;
import java.util.Iterator;

/* --------------------------------------------------------------
Contains method to evaluate the strength of Poker hands
I made them as STATIC (class) methods, because they 
are like methods such as "sin(x)", "cos(x)", that
evaulate the sine, cosine of a value x
Input of each method:
  ArrayList<Card> h;  (5 Cards)
Output of each method:
  An integer value represent the strength
  The higher the integer, the stronger the hand
-------------------------------------------------------------- */

public class Evaluator
{
	public static final int STRAIGHT_FLUSH = 8000000; 
	                                          // + valueHighCard()
	public static final int FOUR_OF_A_KIND = 7000000; 
	                                          // + Quads Card Rank
	public static final int FULL_HOUSE     = 6000000; 
	                                          // + SET card rank
	public static final int FLUSH          = 5000000;  
	                                          // + valueHighCard()
	public static final int STRAIGHT       = 4000000;   
	                                          // + valueHighCard()
	public static final int SET            = 3000000;    
	                                          // + Set card value
	public static final int TWO_PAIRS      = 2000000;     
	                                          // + High2*14^4+ Low2*14^2 + card
	public static final int ONE_PAIR       = 1000000;      
	                                          // + high*14^2 + high2*14^1 + low
	public static final int[][] HOLE_CARDS = {
			 // 1   2  s  o    1=first card; 2=second card; s=suited; o=off suit;
			 { 12, 12, 0, 0 },
			 { 11, 11, 0, 0 },
			 { 10, 10, 0, 0 },
			 { 12, 11, 1, 0 },
			 {  9,  9, 0, 0 },
			 { 12, 10, 1, 0 },
			 { 11, 10, 1, 0 },
			 { 12,  9, 1, 0 },
			 { 11,  9, 1, 0 },
			 {  8,  8, 0, 0 },
			 { 12, 11, 0, 1 },
			 { 12,  8, 1, 0 },
			 { 10,  9, 1, 0 },
			 { 11,  8, 1, 0 },
			 { 10,  8, 1, 0 },
			 {  9,  8, 1, 0 },
			 {  7,  7, 0, 0 },
			 { 12, 10, 0, 1 },
			 { 12,  7, 1, 0 },
			 { 11, 10, 0, 1 },
			 {  6,  6, 0, 0 },
			 { 11,  7, 1, 0 },
			 {  8,  7, 1, 0 },
			 { 12,  6, 1, 0 },
			 { 10,  7, 1, 0 },
			 {  9,  7, 1, 0 },
			 { 12,  9, 0, 1 },
			 { 12,  3, 1, 0 },
			 {  5,  5, 0, 0 },
			 { 12,  5, 1, 0 },
			 { 11,  9, 0, 1 },
			 { 12,  2, 1, 0 },
			 { 12,  1, 1, 0 },
			 { 12,  4, 1, 0 },
			 { 10,  9, 0, 1 },
			 {  4,  4, 0, 0 },
			 { 11,  6, 1, 0 },
			 {  8,  6, 1, 0 },
			 { 12,  0, 1, 0 },
			 {  7,  6, 1, 0 },
			 {  9,  6, 1, 0 },
			 { 12,  8, 0, 1 },
			 { 10,  6, 1, 0 },
			 { 11,  5, 1, 0 },
			 { 11,  8, 0, 1 },
			 {  3,  3, 0, 0 },
			 {  9,  8, 0, 1 },
			 {  6,  5, 1, 0 },
			 { 10,  8, 0, 1 },
			 {  2,  2, 0, 0 },
			 {  1,  1, 0, 0 },
			 {  0,  0, 0, 0 },
			 { 11,  4, 1, 0 },
			 {  7,  5, 1, 0 },
			 { 11,  3, 1, 0 },
			 {  5,  4, 1, 0 },
			 {  8,  5, 1, 0 },
			 { 11,  2, 1, 0 },
			 { 11,  1, 1, 0 },
			 { 11,  0, 1, 0 },
			 { 10,  5, 1, 0 },
			 {  6,  4, 1, 0 },
			 {  4,  3, 1, 0 },
			 {  9,  5, 1, 0 },
			 {  3,  2, 1, 0 },
			 { 10,  4, 1, 0 },
			 {  5,  3, 1, 0 },
			 {  7,  4, 1, 0 },
			 { 10,  3, 1, 0 },
			 {  4,  2, 1, 0 },
			 { 10,  2, 1, 0 },
			 { 10,  1, 1, 0 },
			 {  8,  7, 0, 1 },
			 {  8,  4, 1, 0 },
			 { 10,  0, 1, 0 },
			 { 12,  7, 0, 1 },
			 {  3,  1, 1, 0 },
			 {  6,  3, 1, 0 },
			 {  9,  4, 1, 0 },
			 {  9,  7, 0, 1 },
			 { 11,  7, 0, 1 },
			 {  9,  3, 1, 0 },
			 { 10,  7, 0, 1 },
			 {  2,  1, 1, 0 },
			 {  5,  2, 1, 0 },
			 {  9,  2, 1, 0 },
			 {  9,  1, 1, 0 },
			 {  7,  3, 1, 0 },
			 {  9,  0, 1, 0 },
			 {  4,  1, 1, 0 },
			 { 12,  6, 0, 1 },
			 {  3,  0, 1, 0 },
			 {  8,  3, 1, 0 },
			 {  6,  2, 1, 0 },
			 {  8,  2, 1, 0 },
			 {  8,  1, 1, 0 },
			 {  2,  0, 1, 0 },
			 {  8,  0, 1, 0 },
			 {  7,  6, 0, 1 },
			 {  8,  6, 0, 1 },
			 { 12,  3, 0, 1 },
			 { 12,  5, 0, 1 },
			 {  5,  1, 1, 0 },
			 { 12,  2, 0, 1 },
			 {  1,  0, 1, 0 },
			 {  7,  2, 1, 0 },
			 {  7,  1, 1, 0 },
			 {  9,  6, 0, 1 },
			 { 12,  1, 0, 1 },
			 {  4,  0, 1, 0 },
			 {  7,  0, 1, 0 },
			 { 11,  6, 0, 1 },
			 { 12,  4, 0, 1 },
			 {  6,  5, 0, 1 },
			 { 10,  6, 0, 1 },
			 {  6,  1, 1, 0 },
			 { 12,  0, 0, 1 },
			 {  6,  0, 1, 0 },
			 {  7,  5, 0, 1 },
			 {  5,  0, 1, 0 },
			 {  5,  4, 0, 1 },
			 { 11,  5, 0, 1 },
			 {  4,  3, 0, 1 },
			 {  8,  5, 0, 1 },
			 { 11,  4, 0, 1 },
			 {  6,  4, 0, 1 },
			 {  3,  2, 0, 1 },
			 { 11,  3, 0, 1 },
			 {  9,  5, 0, 1 },
			 {  5,  3, 0, 1 },
			 { 10,  5, 0, 1 },
			 { 11,  2, 0, 1 },
			 { 11,  1, 0, 1 },
			 { 7,   4, 0, 1 },
			 { 11,  0, 0, 1 },
			 { 4,   2, 0, 1 },
			 { 10,  4, 0, 1 },
			 {  3,  1, 0, 1 },
			 {  6,  3, 0, 1 },
			 {  8,  4, 0, 1 },
			 { 10,  3, 0, 1 },
			 {  2,  1, 0, 1 },
			 { 10,  2, 0, 1 },
			 { 10,  1, 0, 1 },
			 {  5,  2, 0, 1 },
			 { 10,  0, 0, 1 },
			 {  9,  4, 0, 1 },
			 {  4,  1, 0, 1 },
			 {  9,  3, 0, 1 },
			 {  7,  3, 0, 1 },
			 {  3,  0, 0, 1 },
			 {  9,  2, 0, 1 },
			 {  9,  1, 0, 1 },
			 {  2,  0, 0, 1 },
			 {  9,  0, 0, 1 },
			 {  6,  2, 0, 1 },
			 {  8,  3, 0, 1 },
			 {  8,  2, 0, 1 },
			 {  1,  0, 0, 1 },
			 {  8,  1, 0, 1 },
			 {  5,  1, 0, 1 },
			 {  8,  0, 0, 1 },
			 {  4,  0, 0, 1 },
			 {  7,  2, 0, 1 },
			 {  7,  1, 0, 1 },
			 {  7,  0, 0, 1 },
			 {  6,  1, 0, 1 },
			 {  6,  0 ,0, 1 },
			 {  5,  0, 0, 1 }
		};

	/***********************************************************
	  Methods used to determine a certain Poker hand
	 ***********************************************************/
	
	public static double handStrength( ArrayList<Card> agentCards, ArrayList<Card> boardCards) {
		if (boardCards.size() == 0) {
			return valueHandPreFlop(agentCards);
		}
		
		int ahead =  0;
		int tied = 0;
		int behind = 0;
		int ourRank = (valueHand(bestHand(agentCards, boardCards)));
		
		ArrayList<Card> newDeck = PokerController.Game.buildDeck();
		ArrayList<Card> tempDeck = new ArrayList<Card>(agentCards);
		tempDeck.addAll(boardCards);
		for(Card card : tempDeck) {
			newDeck.remove(newDeck.indexOf(card));
		}
		
		Iterator<int[]> twoCardCombos = org.apache.commons
						.math3.util.CombinatoricsUtils.
						combinationsIterator(tempDeck.size(), 2);
		
		while(twoCardCombos.hasNext()) {
			int[] combinations = twoCardCombos.next();
			ArrayList<Card> tempHand = new ArrayList<Card>();
			for(int idx : combinations) {
				tempHand.add(tempDeck.get(idx));
			}
			int oppRank = valueHand(bestHand(tempHand, boardCards));
			if (ourRank > oppRank) ahead += 1;
			else if (ourRank == oppRank) tied +=1;
			else behind += 1;
		}
		
		double handStrength = ((double)(ahead + tied) / (double)2)
							/ (double)(ahead + tied + behind);
		
		return handStrength;
	}
	
	public static double[] handPotential( ArrayList<Card> agentCards, ArrayList<Card> boardCards) {
		int HP[][] = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
		int HPTotal[] = {0, 0, 0};
		
		int ahead =  0;
		int tied = 0;
		int behind = 0;
		
		int aheadIdx = 0;
		int tiedIdx = 1;
		int behindIdx = 2;
		
		int ourRank = (valueHand(bestHand(agentCards, boardCards)));
		
		ArrayList<Card> newDeck = PokerController.Game.buildDeck();
		ArrayList<Card> tempDeck = new ArrayList<Card>(newDeck);
		ArrayList<Card> usedCards = new ArrayList<Card>(agentCards); 
		usedCards.addAll(boardCards);
		
		for(Card card : usedCards) {
			tempDeck.remove(tempDeck.indexOf(card));
		}
		
		Iterator<int[]> twoCardCombos = org.apache.commons
						.math3.util.CombinatoricsUtils.
						combinationsIterator(tempDeck.size(), 2);
		
		while(twoCardCombos.hasNext()) {
			int index = 0;
			int[] combinations = twoCardCombos.next();
			ArrayList<Card> tempHand = new ArrayList<Card>();
			Card tempCard1 = tempDeck.get(combinations[0]);
			Card tempCard2 = tempDeck.get(combinations[1]);
			tempHand.add(tempCard1);
			tempHand.add(tempCard2);
			tempDeck.remove(tempDeck.indexOf(tempCard1));
			tempDeck.remove(tempDeck.indexOf(tempCard2));

			int oppRank = valueHand(bestHand(tempHand, boardCards));
			if (ourRank > oppRank) {
				index = aheadIdx;
				ahead += 1;
			}
			else if (ourRank == oppRank) {
				index = tiedIdx;
				tied +=1;
			}
			else {
				index = behindIdx;
				behind += 1;
			}
			HPTotal[index] += 1;
			
			Iterator<int[]> potBoardCards = org.apache.commons
					.math3.util.CombinatoricsUtils.
					combinationsIterator(tempDeck.size(), 5 - boardCards.size());
			
			while(potBoardCards.hasNext()) {
				ArrayList<Card> tempBoard = new ArrayList<Card>(boardCards);
				int[] picks = potBoardCards.next();
				for(int idx : picks) {
					tempBoard.add(tempDeck.get(idx));
				}
				
				int ourBest = valueHand(bestHand(agentCards, tempBoard));
				int oppBest = valueHand(bestHand(agentCards, tempBoard));
				if (ourBest > oppBest) HP[index][aheadIdx] += 1;
				else if (ourBest == oppBest) HP[index][tiedIdx] += 1;
				else HP[index][behindIdx] += 1;
			}
		}
		
		double handStrength = ((double)(ahead + tied) / (double)2)
							/ (double)(ahead + tied + behind);
		
		double Ppot = ((double)HP[behindIdx][aheadIdx] 
					+ ((double)HP[behindIdx][tiedIdx] / (double)2)
					+ ((double)HP[tiedIdx][aheadIdx] / (double)2))
					/ ((double)HPTotal[behindIdx] + (double)HPTotal[tiedIdx]);
		
		double Npot = ((double)HP[aheadIdx][behindIdx] 
					+ ((double)HP[tiedIdx][behindIdx] / (double)2)
					+ ((double)HP[aheadIdx][tiedIdx] / (double)2))
					/ ((double)HPTotal[aheadIdx] + (double)HPTotal[tiedIdx]);
		
		double[] returnArray = { handStrength, Ppot, Npot };
		
		return returnArray;
	}
	
	public static ArrayList<Card> bestHand( ArrayList<Card> agentCards, ArrayList<Card> boardCards ) {
		ArrayList<Card> tempHand = new ArrayList<Card>(agentCards);
		tempHand.addAll(boardCards);
		
		Iterator<int[]> temp = org.apache.commons.math3.util.CombinatoricsUtils.combinationsIterator(tempHand.size(), tempHand.size() - 5);
		
		int bestScore = 0;
		ArrayList<Card> bestHand = null;
		while(temp.hasNext()) {
			ArrayList<Card> testHand = new ArrayList<Card>();
			testHand.addAll(tempHand);
			int[] combinations = temp.next();

			for(int i = 0; i < combinations.length; i++) {
				testHand.remove(combinations[i]-i);
			}
			
			int newScore = valueHand(testHand);
			
			if( newScore > bestScore) {
				bestHand = testHand;
				bestScore = newScore;
			}
		}
		return bestHand;
	}
	
	public static double valueHandPreFlop( ArrayList<Card> h ) {
		int handArray[] = { 
				h.get(0).getRank(),
				h.get(1).getRank(),
				h.get(0).getSuit() == h.get(1).getSuit() ? 1 : 0,
				h.get(0).getSuit() != h.get(1).getSuit() ? 1 : 0	
		};
		
		int count = 0;
		for(int[] hand : HOLE_CARDS) {
			if(handArray[0] == hand[0] && handArray[1] == hand[1]) {
				if(hand[2] == 1 && handArray[2] == 1) {
					return (double)(((double)169 - (double)count) / (double)169);
				}
				
				if(hand[3] == 1 && handArray[3] == 1) {
					return (double)(((double)169 - (double)count) / (double)169);
				}
				
				if(hand[2] == 0 && hand[3] == 0) {
					return (double)(((double)169 - (double)count) / (double)169);
				}
			}
			count++;
		}
		
		return (double)0;
	}
	
	/* --------------------------------------------------------
	   valueHand(): return value of a hand
	   -------------------------------------------------------- */
	public static int valueHand( ArrayList<Card> h )
	{
	   if ( isFlush(h) && isStraight(h) )
	      return valueStraightFlush(h);
	   else if ( is4s(h) )
	      return valueFourOfAKind(h);
	   else if ( isFullHouse(h) )
	      return valueFullHouse(h);
	   else if ( isFlush(h) )
	      return valueFlush(h);
	   else if ( isStraight(h) )
	      return valueStraight(h);
	   else if ( is3s(h) )
	      return valueSet(h);
	   else if ( is22s(h) )
	      return valueTwoPairs(h);
	   else if ( is2s(h) )
	      return valueOnePair(h);
	   else
	      return valueHighCard(h);
	}
	
	
	/* -----------------------------------------------------
	   valueFlush(): return value of a Flush hand
	
	         value = FLUSH + valueHighCard()
	   ----------------------------------------------------- */
	public static int valueStraightFlush(ArrayList<Card> h )
	{
	   return STRAIGHT_FLUSH + valueHighCard(h);
	}
	
	/* -----------------------------------------------------
	   valueFlush(): return value of a Flush hand
	
	         value = FLUSH + valueHighCard()
	   ----------------------------------------------------- */
	public static int valueFlush( ArrayList<Card> h )
	{
	   return FLUSH + valueHighCard(h);
	}
	
	/* -----------------------------------------------------
	   valueStraight(): return value of a Straight hand
	
	         value = STRAIGHT + valueHighCard()
	   ----------------------------------------------------- */
	public static int valueStraight( ArrayList<Card> h )
	{
	   return STRAIGHT + valueHighCard(h);
	}
	
	/* ---------------------------------------------------------
	   valueFourOfAKind(): return value of a 4 of a kind hand
	
	         value = FOUR_OF_A_KIND + 4sCardRank
	
	   Trick: card h.get(2) is always a card that is part of 
	          the 4-of-a-kind hand
		     There is ONLY ONE hand with a quads of a given rank.
	   --------------------------------------------------------- */
	public static int valueFourOfAKind( ArrayList<Card> h )
	{
	   sortByRank(h);
	
	   return FOUR_OF_A_KIND + h.get(2).getRank();
	}
	
	/* -----------------------------------------------------------
	   valueFullHouse(): return value of a Full House hand
	
	         value = FULL_HOUSE + SetCardRank
	
	   Trick: card h.get(2) is always a card that is part of
	          the 3-of-a-kind in the full house hand
		     There is ONLY ONE hand with a FH of a given set.
	   ----------------------------------------------------------- */
	public static int valueFullHouse( ArrayList<Card> h )
	{
	   sortByRank(h);
	
	   return FULL_HOUSE + h.get(2).getRank();
	}
	
	/* ---------------------------------------------------------------
	   valueSet(): return value of a Set hand
	
	         value = SET + SetCardRank
	
	   Trick: card h.get(2) is always a card that is part of the set hand
		     There is ONLY ONE hand with a set of a given rank.
	   --------------------------------------------------------------- */
	public static int valueSet( ArrayList<Card> h )
	{
	   sortByRank(h);
	
	   return SET + h.get(2).getRank();
	}
	
	/* -----------------------------------------------------
	   valueTwoPairs(): return value of a Two-Pairs hand
	
	         value = TWO_PAIRS
	                + 14*14*HighPairCard
	                + 14*LowPairCard
	                + UnmatchedCard
	   ----------------------------------------------------- */
	public static int valueTwoPairs( ArrayList<Card> h )
	{
	   int val = 0;
	
	   sortByRank(h);
	
	   if ( h.get(0).getRank() == h.get(1).getRank() &&
	        h.get(2).getRank() == h.get(3).getRank() )
	      val = 14*14*h.get(2).getRank() + 14*h.get(0).getRank() + h.get(4).getRank();
	   else if ( h.get(0).getRank() == h.get(1).getRank() &&
	             h.get(3).getRank() == h.get(4).getRank() )
	      val = 14*14*h.get(3).getRank() + 14*h.get(0).getRank() + h.get(2).getRank();
	   else 
	      val = 14*14*h.get(3).getRank() + 14*h.get(1).getRank() + h.get(0).getRank();
	
	   return TWO_PAIRS + val;
	}
	
	/* -----------------------------------------------------
	   valueOnePair(): return value of a One-Pair hand
	
	         value = ONE_PAIR 
	                + 14^3*PairCard
	                + 14^2*HighestCard
	                + 14*MiddleCard
	                + LowestCard
	   ----------------------------------------------------- */
	public static int valueOnePair( ArrayList<Card> h )
	{
	   int val = 0;
	
	   sortByRank(h);
	
	   if ( h.get(0).getRank() == h.get(1).getRank() )
	      val = 14*14*14*h.get(0).getRank() +  
	             + h.get(2).getRank() + 14*h.get(3).getRank() + 14*14*h.get(4).getRank();
	   else if ( h.get(1).getRank() == h.get(2).getRank() )
	      val = 14*14*14*h.get(1).getRank() +  
	             + h.get(0).getRank() + 14*h.get(3).getRank() + 14*14*h.get(4).getRank();
	   else if ( h.get(2).getRank() == h.get(3).getRank() )
	      val = 14*14*14*h.get(2).getRank() +  
	             + h.get(0).getRank() + 14*h.get(1).getRank() + 14*14*h.get(4).getRank();
	   else
	      val = 14*14*14*h.get(3).getRank() +  
	             + h.get(0).getRank() + 14*h.get(1).getRank() + 14*14*h.get(2).getRank();
	
	   return ONE_PAIR + val;
	}
	
	/* -----------------------------------------------------
	   valueHighCard(): return value of a high card hand
	
	         value =  14^4*highestCard 
	                + 14^3*2ndHighestCard
	                + 14^2*3rdHighestCard
	                + 14^1*4thHighestCard
	                + LowestCard
	   ----------------------------------------------------- */
	public static int valueHighCard( ArrayList<Card> h )
	{
	   int val;
	
	   sortByRank(h);
	
	   val = h.get(0).getRank() + 14* h.get(1).getRank() + 14*14* h.get(2).getRank() 
	         + 14*14*14* h.get(3).getRank() + 14*14*14*14* h.get(4).getRank();
	
	   return val;
	}
	
	
	/***********************************************************
	  Methods used to determine a certain Poker hand
	 ***********************************************************/
	
	
	/* ---------------------------------------------
	   is4s(): true if h has 4 of a kind
	           false otherwise
	   --------------------------------------------- */
	public static boolean is4s( ArrayList<Card> h )
	{
	   boolean a1, a2;
	
	   if ( h.size() != 5 )
	      return(false);
	
	   sortByRank(h);
	
	   a1 = h.get(0).getRank() == h.get(1).getRank() &&
	        h.get(1).getRank() == h.get(2).getRank() &&
	        h.get(2).getRank() == h.get(3).getRank() ;
	
	   a2 = h.get(1).getRank() == h.get(2).getRank() &&
	        h.get(2).getRank() == h.get(3).getRank() &&
	        h.get(3).getRank() == h.get(4).getRank() ;
	
	   return( a1 || a2 );
	}
	
	
	/* ----------------------------------------------------
	   isFullHouse(): true if h has Full House
	                  false otherwise
	   ---------------------------------------------------- */
	public static boolean isFullHouse( ArrayList<Card> h )
	{
	   boolean a1, a2;
	
	   if ( h.size() != 5 )
	      return(false);
	
	   sortByRank(h);
	
	   a1 = h.get(0).getRank() == h.get(1).getRank() &&  //  x x x y y
	        h.get(1).getRank() == h.get(2).getRank() &&
	        h.get(3).getRank() == h.get(4).getRank();
	
	   a2 = h.get(0).getRank() == h.get(1).getRank() &&  //  x x y y y
	        h.get(2).getRank() == h.get(3).getRank() &&
	        h.get(3).getRank() == h.get(4).getRank();
	
	   return( a1 || a2 );
	}
	
	
	
	/* ----------------------------------------------------
	   is3s(): true if h has 3 of a kind
	           false otherwise
	
	   **** Note: use is3s() ONLY if you know the hand
	              does not have 4 of a kind 
	   ---------------------------------------------------- */
	public static boolean is3s( ArrayList<Card> h )
	{
	   boolean a1, a2, a3;
	
	   if ( h.size() != 5 )
	      return(false);
	
	   if ( is4s(h) || isFullHouse(h) )
	      return(false);        // The hand is not 3 of a kind (but better)
	
	   /* ----------------------------------------------------------
	      Now we know the hand is not 4 of a kind or a full house !
	      ---------------------------------------------------------- */
	   sortByRank(h);
	
	   a1 = h.get(0).getRank() == h.get(1).getRank() &&
	        h.get(1).getRank() == h.get(2).getRank() ;
	
	   a2 = h.get(1).getRank() == h.get(2).getRank() &&
	        h.get(2).getRank() == h.get(3).getRank() ;
	
	   a3 = h.get(2).getRank() == h.get(3).getRank() &&
	        h.get(3).getRank() == h.get(4).getRank() ;
	
	   return( a1 || a2 || a3 );
	}
	
	/* -----------------------------------------------------
	   is22s(): true if h has 2 pairs
	            false otherwise
	
	   **** Note: use is22s() ONLY if you know the hand
	              does not have 3 of a kind or better
	   ----------------------------------------------------- */
	public static boolean is22s( ArrayList<Card> h )
	{
	   boolean a1, a2, a3;
	
	   if ( h.size() != 5 )
	      return(false);
	
	   if ( is4s(h) || isFullHouse(h) || is3s(h) )
	      return(false);        // The hand is not 2 pairs (but better)
	
	   sortByRank(h);
	
	   a1 = h.get(0).getRank() == h.get(1).getRank() &&
	        h.get(2).getRank() == h.get(3).getRank() ;
	
	   a2 = h.get(0).getRank() == h.get(1).getRank() &&
	        h.get(3).getRank() == h.get(4).getRank() ;
	
	   a3 = h.get(1).getRank() == h.get(2).getRank() &&
	        h.get(3).getRank() == h.get(4).getRank() ;
	
	   return( a1 || a2 || a3 );
	}
	
	
	/* -----------------------------------------------------
	   is2s(): true if h has one pair
	           false otherwise
	
	   **** Note: use is22s() ONLY if you know the hand
	              does not have 2 pairs or better
	   ----------------------------------------------------- */
	public static boolean is2s( ArrayList<Card> h )
	{
	   boolean a1, a2, a3, a4;
	
	   if ( h.size() != 5 )
	      return(false);
	
	   if ( is4s(h) || isFullHouse(h) || is3s(h) || is22s(h) )
	      return(false);        // The hand is not one pair (but better)
	
	   sortByRank(h);
	
	   a1 = h.get(0).getRank() == h.get(1).getRank() ;
	   a2 = h.get(1).getRank() == h.get(2).getRank() ;
	   a3 = h.get(2).getRank() == h.get(3).getRank() ;
	   a4 = h.get(3).getRank() == h.get(4).getRank() ;
	
	   return( a1 || a2 || a3 || a4 );
	}
	
	
	/* ---------------------------------------------
	   isFlush(): true if h has a flush
	              false otherwise
	   --------------------------------------------- */
	public static boolean isFlush( ArrayList<Card> h )
	{
	   if ( h.size() != 5 )
	      return(false);
	
	   sortBySuit(h);
	
	   return( h.get(0).getSuit() == h.get(4).getSuit() );   // All cards has same suit
	}
	
	
	/* ---------------------------------------------
	   isStraight(): true if h is a Straight
	                 false otherwise
	   --------------------------------------------- */
	public static boolean isStraight( ArrayList<Card> h )
	{
	   int i, testRank;
	
	   if ( h.size() != 5 )
	      return(false);
	
	   sortByRank(h);
	
	   /* ===========================
	      Check if hand has an Ace
	      =========================== */
	   if ( h.get(4).getRank() == 14 )
	   {
	      /* =================================
	         Check straight using an Ace
	         ================================= */
	      boolean a = h.get(0).getRank() == 2 && h.get(1).getRank() == 3 &&
	                  h.get(2).getRank() == 4 && h.get(3).getRank() == 5 ;
	      boolean b = h.get(0).getRank() == 10 && h.get(1).getRank() == 11 &&
	                  h.get(2).getRank() == 12 && h.get(3).getRank() == 13 ;
	
	      return ( a || b );
	   }
	   else
	   {
	      /* ===========================================
	         General case: check for increasing values
	         =========================================== */
	      testRank = h.get(0).getRank() + 1;
	
	      for ( i = 1; i < 5; i++ )
	      {
	         if ( h.get(i).getRank() != testRank )
	            return(false);        // Straight failed...
	
	         testRank++;
	      }
	
	      return(true);        // Straight found !
	   }
	}
	
	/* ===========================================================
	   Helper methods
	   =========================================================== */
	
	/* ---------------------------------------------
	   Sort hand by rank:
	
	       smallest ranked card first .... 
	
	   (Finding a straight is eaiser that way)
	   --------------------------------------------- */
	public static void sortByRank( ArrayList<Card> h )
	{
	   int i, j, min_j;
	
	   /* ---------------------------------------------------
	      The selection sort algorithm
	      --------------------------------------------------- */
	   for ( i = 0 ; i < h.size() ; i ++ )
	   {
	      /* ---------------------------------------------------
	         Find array element with min. value among
	         h[i], h[i+1], ..., h[n-1]
	         --------------------------------------------------- */
	      min_j = i;   // Assume elem i (h[i]) is the minimum
	
	      for ( j = i+1 ; j < h.size() ; j++ )
	      {
	         if ( h.get(j).getRank() < h.get(min_j).getRank() )
	         {
	            min_j = j;    // We found a smaller minimum, update min_j     
	         }
	      }
	
	      /* ---------------------------------------------------
	         Swap a[i] and a[min_j]
	         --------------------------------------------------- */
	      Card help = h.get(i);
	      h.set(i, h.get(min_j));
	      h.set(min_j, help);
	   }
	}
	
	/* ---------------------------------------------
	   Sort hand by suit:
	
	       smallest suit card first .... 
	
	   (Finding a flush is eaiser that way)
	   --------------------------------------------- */
	public static void sortBySuit( ArrayList<Card> h )
	{
	   int i, j, min_j;
	
	   /* ---------------------------------------------------
	      The selection sort algorithm
	      --------------------------------------------------- */
	   for ( i = 0 ; i < h.size() ; i ++ )
	   {
	      /* ---------------------------------------------------
	         Find array element with min. value among
	         h[i], h[i+1], ..., h[n-1]
	         --------------------------------------------------- */
	      min_j = i;   // Assume elem i (h[i]) is the minimum
	
	      for ( j = i+1 ; j < h.size() ; j++ )
	      {
	         if ( h.get(j).getSuit() < h.get(min_j).getSuit() )
	         {
	            min_j = j;    // We found a smaller minimum, update min_j     
	         }
	      }
	
	      /* ---------------------------------------------------
	         Swap a[i] and a[min_j]
	         --------------------------------------------------- */
	      Card help = h.get(i);
	      h.set(i, h.get(min_j));
	      h.set(min_j, help);
	   }
	}
}