package cn.com.yusong.yhdg.common.entity.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node<T> {
	private T data;
    private Node<T> parent;
    private List<Node<T>> children = new ArrayList<Node<T>>();

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;

        parent.addChild(this);
    }
    
    public Node(T data, Node<T> parent, Comparator<T> comparator) {
        this.data = data;
        this.parent = parent;

        parent.addChild(this, comparator);
    }    

    public void addChild(Node<T> node) {
        if(node != null) {
            node.parent = this;
            children.add(node);
        }
    }
    
    public void addChild(Node<T> node, Comparator<T> comparator) {
    	if(node != null) {
            node.parent = this;
            
            boolean insert = false;
            for(int i = 0; i < children.size(); i++) {
            	if(comparator.compare(node.getData(), children.get(i).getData()) == -1) {
            		children.add(i, node);
            		insert = true;
            		break;
            	}
            }
            
            if(!insert) {
            	children.add(node);
            }
        }
    }

    public void remove(Node<T> node) {
        if(node != null && node.parent != null && node.parent.getChildren() != null) {
            node.parent.getChildren().remove(node);
            node.parent = null;
        }
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public boolean hasChild() {
        return !isLeaf();
    }

    public boolean isLast() {
        if(parent == null) {
            return true;
        } else {
            if(parent.getChildren() == null || parent.getChildren().isEmpty()) {
                throw new IllegalArgumentException("错误的树结构");
            }
            
            int index = parent.getChildren().indexOf(this);
            if(index == -1) {
                throw new IllegalArgumentException("错误的树结构");
            } else {
                return index == (parent.getChildren().size() - 1);
            }
        }
    }
    
    public boolean containsTheChild(T data) {
    	return getTheChild(data) != null;
    }
    
    public Node<T> getTheChild(T data) {
    	Node<T> child = null;
    	if(hasChild()) {
    		for(Node<T> node : children) {
    			if(data.equals(node.getData())) {
    				child = node;
    				break;
    			}
    		}
    	}
    	
    	return child;
    }
    
    public boolean isFirst() {
    	Node<T> p = getParent();
    	if(p == null || (p.getChildren() != null && p.getChildren().size() == 1)) {
    		return true;
    	} else {
    		return p.getChildren().indexOf(this) == 0;
    	}
    }

    public T getData() {
        return data;
    }
    
    public void setData(T data) {
		this.data = data;
	}

	public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }
}
