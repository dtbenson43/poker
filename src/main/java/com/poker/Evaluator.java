package com.poker;

import java.util.ArrayList;

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
	
	
	
	/***********************************************************
	  Methods used to determine a certain Poker hand
	 ***********************************************************/
	
	/* --------------------------------------------------------
	   valueHand(): return value of a hand
	   -------------------------------------------------------- */
	public static int valueHandPreFlop( ArrayList<Card> h ) {
		int[][] holes = {
				//1   2  s  o
				{12, 12, 0, 0}, // 1
				{11, 11, 0, 0}, // 2
				{10, 10, 0, 0}, // 3
				{12, 11, 1, 0}, // 4
				{ 9,  9, 0, 0}, // 5
				{12, 10, 1, 0}, // 6
				{11, 10, 1, 0}, // 7
				{12,  9, 1, 0}, // 8
				{}, // 9
				{}, // 10
				{}, // 11
				{}, // 12
				{}, // 13
				{}, // 14
				{}, // 15
				{}, // 16
				{}, // 17
				{}, // 18
				{}, // 19
				{}, // 20
				{}, // 21
				{}, // 22
				{}, // 23
				{}, // 24
				{}, // 25
				{}, // 26
				{}, // 27
				{}, // 28
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9
				{}, // 9	
		};
		
		return 0;
	}
	
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