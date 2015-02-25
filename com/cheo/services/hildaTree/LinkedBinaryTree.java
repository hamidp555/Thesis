package com.cheo.services.hildaTree;

import java.util.Iterator;

public class LinkedBinaryTree<E> implements BinaryTree<E> {

	protected BTPosition<E> root;
	protected int size;

	public LinkedBinaryTree(){
		root=null;
		size=0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		Iterable<Position<E>> positions = positions();
		PositionList<E> elements = new NodePositionList<E>();
		for(Position<E> pos: positions){
			elements.addLast(pos.element());
		}
		return elements.iterator();
	}

	@Override
	public Iterable<Position<E>> positions(){

		PositionList<Position<E>> positions = new NodePositionList<Position<E>>();
		if(size != 0){
			preorderPositions(root(), positions);
		}
		return positions;
	}

	@Override
	public E replace(Position<E> v, E e) {
		BTPosition<E> w = checkPosition(v);
		E temp = v.element();
		w.setElement(e);
		return temp;
	}

	@Override
	public Position<E> root(){
		return root;
	}

	@Override
	public Position<E> parent(Position<E> v) {
		BTPosition<E> w = checkPosition(v);
		Position<E> parentPos = w.getParent();
		return parentPos;
	}

	@Override
	public Iterable<Position<E>> children(Position<E> v) {
		PositionList<Position<E>> children = new NodePositionList<Position<E>>(); 
		if(hasLeft(v))
			children.addLast(left(v));
		if(hasRight(v))
			children.addLast(right(v));
		return children;
	}

	@Override
	public boolean isInternal(Position<E> v)  {
		checkPosition(v);
		return (hasLeft(v) || hasRight(v));
	}

	@Override
	public boolean isExternal(Position<E> v) {
		checkPosition(v);
		return (!hasLeft(v) && !hasRight(v));
	}

	@Override
	public boolean isRoot(Position<E> v) {
		checkPosition(v);
		return (v == root());
	}

	@Override
	public Position<E> left(Position<E> v) {
		BTPosition<E> w = checkPosition(v);
		Position<E> leftPos = w.getLeft();
		return leftPos;
	}

	@Override
	public Position<E> right(Position<E> v) {
		BTPosition<E> w = checkPosition(v);
		Position<E> rightPos = w.getRight();
		return rightPos;
	}

	@Override
	public boolean hasLeft(Position<E> v){
		BTPosition<E> w = checkPosition(v);
		return(w.getLeft() != null);
	}

	@Override
	public boolean hasRight(Position<E> v){
		BTPosition<E> w = checkPosition(v);
		return(w.getRight() != null);
	}

	protected BTPosition<E> checkPosition(Position<E> v){
		return (BTPosition<E>)v;
	}

	protected void preorderPositions(Position<E> v, PositionList<Position<E>> pos){
		pos.addLast(v);
		if(hasLeft(v))
			preorderPositions(left(v), pos);
		if(hasRight(v))
			preorderPositions(right(v), pos);
	}

	protected BTPosition<E> createNode(E element, 
			BTPosition<E> parent, BTPosition<E> left, BTPosition<E> right){
		return new BTNode<E>(element, parent, left, right);
	}

	public Position<E> sibling(Position<E> v){
		BTPosition<E> w = checkPosition(v);
		BTPosition<E> parent = w.getParent();
		if(parent != null){
			BTPosition<E> sibPos;
			BTPosition<E> leftPos = parent.getLeft();
			if(leftPos == w){
				sibPos = parent.getRight();
			}else{
				sibPos = parent.getLeft();
			}
			if(sibPos!=null){
				return sibPos;
			}
		}
		return null;
	}

	public Position<E> addRoot(E e){
		size=1;
		root=createNode(e, null, null, null);
		return root;
	}

	public Position<E> insertLeft(Position<E> v, E e){
		BTPosition<E> w = checkPosition(v);
		BTPosition<E> left = w.getLeft();
		if(left == null){
			BTPosition<E> newNode = createNode(e, w, null, null);
			w.setLeft(newNode);
			size++;
			return newNode;
		}
		return null;
	}

	public Position<E> insertRight(Position<E> v, E e){
		BTPosition<E> w = checkPosition(v);
		BTPosition<E> right = w.getRight();
		if(right == null){
			BTPosition<E> newNode = createNode(e, w, null, null);
			w.setRight(newNode);
			size++;
			return newNode;
		}
		return null;
	}

	public E remove(Position<E> v){
		
		BTPosition<E> w = checkPosition(v);
		BTPosition<E> left = w.getLeft();
		BTPosition<E> right = w.getRight();
		
		if(left == null || right == null){
			BTPosition<E> child;
			if(left !=null){
				child=left;
			}else if(right !=null){
				child=right;	
			}else{
				child=null;
			}

			if(w == root){
				if(w!=null){
					w.setParent(null);
				}
				root=w;
			}else{
				BTPosition<E> parent = w.getParent();
				if(w == parent.getLeft()){
					parent.setLeft(child);
				}else{
					parent.setRight(child);
				}
				if(child != null){
					child.setParent(parent);
				}
			}
			size--;
			return v.element();
		}
		return null;
	}
	
	public void attach(Position<E> v, BinaryTree<E> bt1, BinaryTree<E> bt2) throws InvalidPositionException{
		BTPosition<E> w = checkPosition(v);
		if(isInternal(v))
			throw new InvalidPositionException("Cannot attch from internal node");
		if(!bt1.isEmpty()){
			BTPosition<E> r1 = checkPosition(bt1.root());
			w.setLeft(r1);
			r1.setParent(w);
		}
		if(!bt2.isEmpty()){
			BTPosition<E> r2= checkPosition(bt2.root());
			w.setRight(r2);
			r2.setParent(w);		
		}
	}

}
