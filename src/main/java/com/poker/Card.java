package com.poker;

public class Card implements Comparable{
	private int suit;
	private int rank;
	
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

	public int compareTo(Card c) {
		return (this.getRank() < c.getRank() ? -1 : 
            (this.getRank() == c.getRank() ? 0 : 1)); 
	}
}
