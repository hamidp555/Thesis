package com.cheo.services.hildaTree;

public class DNode<E> implements Position<E> {
	
	private DNode<E> prev, next;
	private E element;
	
	public DNode(DNode<E> newPrev, DNode<E> newNext, E elem){
		prev=newPrev;
		next=newNext;
		element=elem;
	}

	@Override
	public E element(){
		return element;
	}

	public DNode<E> getPrev() {
		return prev;
	}

	public void setPrev(DNode<E> prev) {
		this.prev = prev;
	}

	public DNode<E> getNext() {
		return next;
	}

	public void setNext(DNode<E> next) {
		this.next = next;
	}

	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

}
