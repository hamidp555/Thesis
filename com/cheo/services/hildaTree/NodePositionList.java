package com.cheo.services.hildaTree;

import java.util.Iterator;

public class NodePositionList<E> implements PositionList<E> {

	protected int numElements;
	protected DNode<E> header, trailer;

	public NodePositionList(){
		numElements=0;
		header = new DNode<E>(null, null, null);
		trailer = new DNode<E>(header, null, null);
		header.setNext(trailer);	
	}

	@Override
	public int size() {
		return numElements;
	}

	@Override
	public boolean isEmpty() {
		return numElements==0;
	}

	@Override
	public Position<E> first(){
		return header.getNext();
	}

	@Override
	public Position<E> last() {
		return trailer.getPrev();
	}

	@Override
	public Position<E> next(Position<E> p) {
		DNode<E> v = checkPosition(p);
		DNode<E> next = v.getNext();
		return next;
	}

	@Override
	public Position<E> prev(Position<E> p) {
		DNode<E> v = checkPosition(p);
		DNode<E> prev = v.getPrev();
		return prev;
	}

	@Override
	public void addFirst(E e) {
		numElements++;
		DNode<E> newNode = new DNode<E>(header, header.getNext(), e);
		header.getNext().setPrev(newNode);
		header.setNext(newNode);

	}

	@Override
	public void addLast(E e) {
		numElements++;
		DNode<E> newNode = new DNode<E>(trailer.getPrev(), trailer, e);
		trailer.getPrev().setNext(newNode);
		trailer.setPrev(newNode);
	}

	@Override
	public void addAfter(Position<E> p, E e) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		numElements++;
		DNode<E> newNode = new DNode<E>(v, v.getNext(), e);
		v.getNext().setPrev(newNode);
		v.setNext(newNode);

	}

	@Override
	public void addBefore(Position<E> p, E e) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		numElements++;
		DNode<E> newNode = new DNode<E>(v.getPrev(), v, e);
		v.getPrev().setNext(newNode);
		v.setPrev(newNode);

	}

	@Override
	public E remove(Position<E> p) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		numElements--;
		DNode<E> vPrev = v.getPrev();
		DNode<E> vNext = v.getNext();
		vPrev.setNext(vNext);
		vNext.setPrev(vPrev);
		E vElem = v.element();
		v.setNext(null);
		v.setPrev(null);
		return vElem;
	}

	@Override
	public E set(Position<E> p, E e) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		E oldElem = v.element();
		v.setElement(e);
		return oldElem;
	}

	protected DNode<E> checkPosition(Position<E> p){
		DNode<E> temp = (DNode<E>)p;
		return temp;
	}

	@Override
	public Iterable<Position<E>> positions() throws EmptyTreeException, BoundaryViolationException, InvalidPositionException {
		PositionList<Position<E>> p = new NodePositionList<Position<E>>();
		if(!isEmpty()){
			Position<E> fp = first();
			while(true){
				p.addLast(fp);
				if(fp == last())
					break;
				fp = next(fp);
			}
		}
		return p;
	}

	public static <E> String toString(PositionList<E> list) throws EmptyTreeException, InvalidPositionException, BoundaryViolationException{
		Iterator<E> iter = list.iterator();
		String s = "[";
		while(iter.hasNext()){
			s+=iter.next();
			if(iter.hasNext())
				s+=", ";
		}
		s+="]";
		return s;
	}

	@Override
	public Iterator<E> iterator() {
		return new ElementIterator<E>(this);
	}


}
