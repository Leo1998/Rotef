package com.rotef.game.world.entity;

public class Foot {

	private final Entity owner;
	private int numContacts = 0;

	public Foot(Entity owner) {
		this.owner = owner;
	}

	public Entity getOwner() {
		return owner;
	}

	public int getNumContacts() {
		return numContacts;
	}

	public void increaseNumContacts() {
		numContacts++;
	}

	public void decreaseNumContacts() {
		numContacts--;
	}

}
