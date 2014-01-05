package com.example.chickeninvaders;

public class BonusList {

	Bonus root;
	Bonus tail;
	int length;

	public BonusList(Bonus root) {
		this.root = root;
		this.tail = root;
		tail.next = null;
		length = 1;
	}

	public BonusList() {
		// TODO Auto-generated constructor stub
		root = null;
		tail = null;
		length = 0;
	}

	public void addBonus(Bonus bonus) {
		if (length == 0) {
			root = bonus;
			root.next = null;
			tail = bonus;
			tail.next = null;
			++length;
		} else if (length == 1) {
			root.next = bonus;
			tail = bonus;
			tail.next = null;
			++length;
		} else {
			tail.next = bonus;
			tail = bonus;
			tail.next = null;
			++length;
		}
	}

	public int removeBonus(Bonus bonus) {
		if (length == 0) {
			return -1;
		}
		--length;
		if (bonus == root) {
			root = root.next;
			return 1;
		}
		Bonus previous = root;
		Bonus current = root.next;
		for (int i = 1; i < length; ++i) {
			if (current == bonus) {
				previous.next = current.next;
				if (current == tail) {
					tail = previous;
				}
				return 1;
			}
		}
		++length;
		return -2;
	}

}
