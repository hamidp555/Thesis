package com.cheo.services.hildaTree;


public interface PositionList<E> extends Iterable<E>{
	public int size();
	public boolean isEmpty();
	public Position<E> first();
	public Position<E> last();
	public Position<E> next(Position<E> p);
	public Position<E> prev(Position<E> p);
	public void addFirst(E e);
	public void addLast(E e);
	public void addAfter(Position<E> p, E e) throws InvalidPositionException;
	public void addBefore(Position<E> p, E e) throws InvalidPositionException;
	public E remove(Position<E> p) throws  InvalidPositionException;
	public E set(Position<E> p, E e) throws  InvalidPositionException; 
	public Iterable<Position<E>> positions() throws EmptyTreeException, BoundaryViolationException, InvalidPositionException;
}
