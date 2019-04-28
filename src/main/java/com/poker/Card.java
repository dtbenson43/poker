package com.poker;

public class Card {
	private int suit;
	private int rank;
	
	Card(Card clone) {
		this.suit = clone.getSuit();
		this.rank = clone.getRank();
	}

	Card(int suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public int getSuit() {
		return suit; 
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public boolean equals(Card comp) {
		if (comp.getRank() == this.rank && comp.getSuit() == this.suit) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return this.suit + "0" + this.rank;
		
	}
}
