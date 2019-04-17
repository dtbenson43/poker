package com.poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.stream.Stream;

public class Evaluator {
	
	public Evaluator() {
		
	}
	
	public int highCard(ArrayList<Card> hand) {
		int highestCard = -1;

		for(Card c: hand) {
			if(c.getRank() > highestCard ) {
				highestCard = c.getRank();
			}
		}
		
		return highestCard;
	}
	
	public boolean pairCheck(ArrayList<Card> hand) {	
		for(Card c: hand) {
			for(Card v: hand) {
				if(c.getRank() == v.getRank() && c.getSuit() != v.getSuit()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean twoPairCheck(ArrayList<Card> hand) {	
		int pairOneRank = -1;
		int pairTwoRank = -1;
		
		for(Card c: hand) {
			for(Card v: hand) {
				if(c.getRank() == v.getRank() && c.getSuit() != v.getSuit()) {
					pairOneRank = c.getRank();
				}
			}
		}
		if(pairOneRank == -1) {
			return false;
		}
		
		for(Card c: hand) {
			for(Card v: hand) {
				if(c.getRank() == v.getRank() && c.getSuit() != v.getSuit() && c.getRank() != pairOneRank) {
					pairTwoRank = c.getRank();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean threeOfAKind(ArrayList<Card> hand) {	
		int rankToCheck = -1;
		
		for(Card c: hand) {
			for(Card v: hand) {
				for(Card b: hand) {
					if(c.getRank() == v.getRank() && c.getSuit() != v.getSuit() && b.getRank() == c.getRank() && b.getSuit() != c.getSuit() && b.getSuit() != v.getSuit() )  {
						return true;
					}
				}
			}
		}
		return false;
	}
}
