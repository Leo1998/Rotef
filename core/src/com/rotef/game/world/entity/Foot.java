package com.rotef.game.world.entity;

public class Foot {

	private final LivingEntity owner;
	private int numContacts = 0;

	public Foot(LivingEntity owner) {
		this.owner = owner;
	}

	public LivingEntity getOwner() {
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
