package com.cheo.services.hildaTree;

public interface BinaryTree<E> extends Tree<E> {

	public Position<E> left(Position<E> v);
	public Position<E> right(Position<E> v);
	public boolean hasLeft(Position<E> v);
	public boolean hasRight(Position<E> v);
	
}
