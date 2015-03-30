/**
 * 
 */
package org.eapp.oa.system.util;

import java.io.Serializable;
import java.util.Stack;
import java.util.TreeMap;

/**
 * 将有父子关系的Element数组转化成有顺序的树结构模型
 * @author zsy
 * @version Jun 19, 2009
 */
public class Tree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1542152026970651687L;
	//当前节点
	private Node node;
	//子结点的可排序Map
	private TreeMap<Node, Tree> childNodeMap = new TreeMap<Node, Tree>();
	
	public Tree() {
	}
	
	public Tree(Node node) {
		this.node = node;
	}
	
	/**
	 * @return the module
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @return the subModule
	 */
	public TreeMap<Node, Tree> getChildNodeMap() {
		return childNodeMap;
	}
	
	/**
	 * 是否为相同节点
	 * Element为空或其ID为空做为根节点
	 * @return
	 */
	public boolean equalsNode(String nodeId) {
		if (node == null || node.getId() == null) {
			return nodeId == null;
		} else {
			return node.getId().equals(nodeId);
		}
	}
	
	/**
	 * 平级的Node数组通过父ID组成树模型
	 * @param nodes
	 * @return
	 */
	public static Tree createTree(Node[] nodes) {
		Tree rootTree = new Tree();
		rootTree.addNodes(nodes);
		return rootTree;
	}
	
	/**
	 * 平级的Node数组添加到树模型
	 * @param nodes
	 */
	public void addNodes(Node[] nodes) {
		if (nodes != null) {
			for (Node e : nodes) {
				addNodeToTree(e);
			}
		}
	}

	/**
	 * 添加元素，从树的当前节点往下查找，找到该元素的父节点再添加进去
	 * @param m
	 */
	private void addNode(Node e) {
		if (e == null) {
			return;
		}
		//找到父节点
		String parentId = null;
		if(e.getParentNode() != null){
			parentId = e.getParentNode().getId();
		}
		//当前树是根节点或等于元素的父节点，直接把该元素加入当前树的下级Map中
		if(equalsNode(parentId)) {
			if (!childNodeMap.keySet().contains(e)) {
				childNodeMap.put(e, new Tree(e));
			}
		} else {
			//否则添加到下级进行判断
			for(Tree cTree : childNodeMap.values()) {
				cTree.addNode(e);
			}
		}
	}
	
	/**
	 * 先找出该元素的所有父元素直到跟元素（即父元素为空）为止
	 * 从跟元素开始逐一添加到树上
	 * @param e
	 * @param tree
	 */
	private void addNodeToTree(Node e) {
		Stack<Node> s = new Stack<Node>();
		s.push(e);
		Node parentEle = e.getParentNode();
		while (parentEle != null) {
			s.push(parentEle);
			parentEle = parentEle.getParentNode();
		}
		//从跟元素开始逐一添加到树上
		while(!s.empty()) {
			parentEle = s.pop();
			addNode(parentEle);
		}
	}
	
	/**
	 * 根据给定的ID查找节点元素
	 * @param id
	 * @return
	 */
	public Tree findNode(String id) {
		if(equalsNode(id)) {
			return this;
		} else {
			Tree finded = null;
			for(Tree cTree : childNodeMap.values()) {
				finded = cTree.findNode(id);
				if (finded != null) {
					return finded;
				}
			}
		}
		return null;
	}
	
	/**
	 * 树节点的元素
	 * @author zsy
	 * @version Jun 19, 2009
	 */
	public static interface Node extends Serializable, Comparable<Node> {
		String getId();
		Node getParentNode();
	}
}
