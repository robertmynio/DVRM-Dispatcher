package misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Sep 30, 2010
 * Time: 2:39:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DecisionTreeNode {
    private String nodeName;
    private String actionName;
    private String entropy;
    private List<DecisionTreeNode> children;

    public DecisionTreeNode(String nodeName, String actionName, String entropy) {
        this.nodeName = nodeName;
        this.actionName = actionName;
        this.entropy = entropy;
        children = new ArrayList<DecisionTreeNode>();
    }

    public void addChild(DecisionTreeNode node) {
        children.add(node);
    }

    public void removeChild(DecisionTreeNode node) {
        children.remove(node);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getEntropy() {
        return entropy;
    }

    public void setEntropy(String entropy) {
        this.entropy = entropy;
    }

    public List<DecisionTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<DecisionTreeNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DecisionTreeNode that = (DecisionTreeNode) o;

        if (nodeName != null ? !nodeName.equals(that.nodeName) : that.nodeName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nodeName != null ? nodeName.hashCode() : 0;
    }
}