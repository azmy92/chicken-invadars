package com.example.chickeninvaders;


public class BulletList {

	Bullet root;
	Bullet tail;
	int length;
	
	public BulletList(Bullet root)
	{
		this.root=root;
		this.tail=root;
		tail.next=null;
		length=1;
	}
	public BulletList() {
		// TODO Auto-generated constructor stub
		root=null;
		tail=null;
		length=0;
	}
	public void addBullet(Bullet bullet)
	{
		if(length==0)
		{
			root=bullet;
			root.next=null;
			tail=bullet;
			tail.next=null;
			++length;
		}
		else
			if(length==1)
			{
				root.next=bullet;
				tail=bullet;
				tail.next=null;
				++length;
			}
			else
			{
				tail.next=bullet;
				tail=bullet;
				tail.next=null;
				++length;
			}
	}
	public int removeBullet(Bullet bullet)
	{
		if(length==0)
		{
			return -1;
		}
		--length;
		if(bullet==root)
		{
			root=root.next;
			return 1;
		}
		Bullet previous=root;
		Bullet current=root.next;
		for(int i=1;i<length;++i)
		{
			if(current==bullet)
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
