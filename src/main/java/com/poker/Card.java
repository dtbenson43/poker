package com.poker;

public class Card {
	private int suit;
	private int rank;
	
	private String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"}; 
	private String[] nameOfCard = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
	
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
}
