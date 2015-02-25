package com.cheo.services.hildaTree;

import java.util.Iterator;


public interface Tree<E> {
	public int size();
	public boolean isEmpty();
	public Iterator<E> iterator();
	public Iterable<Position<E>> positions();
	public E replace(Position<E> v, E e);

	public Position<E> root();
	public Position<E> parent(Position<E> v);
	public Iterable<Position<E>> children(Position<E> v);

	public boolean isInternal(Position<E> v);
	public boolean isExternal(Position<E> v);
	public boolean isRoot(Position<E> v);
}
