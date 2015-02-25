 package com.cheo.services.hildaTree;

public class BTNode<E> implements BTPosition<E> {
	
	private E element;
	private BTPosition<E> left, right, parent;
	public BTNode(E element, BTPosition<E> parent, BTPosition<E> left, BTPosition<E> right){
		setElement(element);
		setParent(parent);
		setLeft(left);
		setRight(right);
	}

	@Override
	public E element() {
		return element;
	}

	@Override
	public void setElement(E o) {
		element=o;
	}

	@Override
	public BTPosition<E> getLeft() {
		return left;
	}

	@Override
	public void setLeft(BTPosition<E> v) {
		left=v;
	}

	@Override
	public BTPosition<E> getRight() {
		return right;
	}

	@Override
	public void setRight(BTPosition<E> v) {
		right=v;
	}

	@Override
	public BTPosition<E> getParent() {
		return parent;
	}

	@Override
	public void setParent(BTPosition<E> v) {
		parent=v;
		
	}

}
