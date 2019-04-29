package com.poker;
/**
 * Card
 * A class that creates the card object that stores information such as suit and rank
 * 
 * 
 * @author Daniel Benson, and Allen Austen Riffee
 * @version 1.0
 * 
 * CAP4601   Project #: 2
 * File Name: Card.java
 */
public class Card {
	/**
	 * Integer variable that represents the suit of a card
	 */
	private int suit;
	/**
	 * Integer variable that represents the rank of a card
	 */
	private int rank;
	
	/**
	 * Parameterized constructor that clones a card
	 * 
	 * @param clone - A card object
	 */
	Card(Card clone) {
		this.suit = clone.getSuit();
		this.rank = clone.getRank();
	}

	/**
	 * Parameterized constructor that creates a card using integer values for the suit and rank
	 * 
	 * @param clone - A card object
	 */
	Card(int suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}
	/**
	 * Returns the suit of the card
	 * @return - the suit of the card
	 */
	public int getSuit() {
		return suit; 
	}
	/**
	 * Sets the suit of the card
	 * @param suit - the suit of the card
	 */
	public void setSuit(int suit) {
		this.suit = suit;
	}
	/**
	 * Returns the rank of the card
	 * @return - the rank of the card
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * Sets the rank of the card
	 * @param rank - the rank of the card
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * Compares a card to this card to see if they are the same
	 * @param comp - card to compare
	 * @return - false if cards are not the same, true if cards are the same
	 */
	public boolean equals(Card comp) {
		if (comp.getRank() == this.rank && comp.getSuit() == this.suit) {
			return true;
		}
		return false;
	}
}
