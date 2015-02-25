package com.cheo.services.hildaTree;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ElementIterator<E> implements Iterator<E>{

	protected PositionList<E> list;
	protected Position<E> cursor;

	public ElementIterator(PositionList<E> l){
		list=l;
		cursor = (list.isEmpty()) ? null : list.first();
	}

	@Override
	public boolean hasNext() {
		return cursor != null;
	}

	@Override
	public E next(){
		if(cursor == null)
			throw new NoSuchElementException("No next element");
		E toReturn  = cursor.element();
		cursor = (cursor == list.last()) ? null : list.next(cursor);
		return toReturn;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
	}

}
