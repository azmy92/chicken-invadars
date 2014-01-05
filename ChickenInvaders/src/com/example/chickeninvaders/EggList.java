package com.example.chickeninvaders;

public class EggList {

	Egg root;
	Egg tail;
	int length;
	
	public EggList(Egg root)
	{
		this.root=root;
		this.tail=root;
		tail.next=null;
		length=1;
	}
	public EggList() {
		// TODO Auto-generated constructor stub
		root=null;
		tail=null;
		length=0;
	}
	public void addBullet(Egg egg)
	{
		if(length==0)
		{
			root=egg;
			root.next=null;
			tail=egg;
			tail.next=null;
			++length;
		}
		else
			if(length==1)
			{
				root.next=egg;
				tail=egg;
				tail.next=null;
				++length;
			}
			else
			{
				tail.next=egg;
				tail=egg;
				tail.next=null;
				++length;
			}
	}
	public int removeBall(Egg egg)
	{
		if(length==0)
		{
			return -1;
		}
		--length;
		if(egg==root)
		{
			root=root.next;
			return 1;
		}
		Egg previous=root;
		Egg current=root.next;
		for(int i=1;i<length;++i)
		{
			if(current==egg)
			{
				previous.next=current.next;
				if(current==tail)
				{
					tail=previous;
				}
				return 1;
			}
		}
		++length;
		return -2;
	}
	
	
}
