function visit(externs, root) {
  plugin.traverse(root, findTraverse);
  plugin.traverse(root, removeTraverse);
}

var allFunctions = [];
var usedFunctions = {};
var dataMap = {};

function remember(key, priority, templateName) {
  allFunctions.push(templateName);
  dataMap[key] = priority;
  usedFunctions[key] = templateName;
}

function findTraverse(t, node, parentNode) {
  if (isDelegateCallNode(node)) {
    var priority = getPriority(node);
    var key = getDelegateId(node);
    var currentPriorityInMap = dataMap.get(key);
    if (currentPriorityInMap == null || currentPriorityInMap < priority) {
      remember(key, priority, getTemplateName(node));
    }
  }
}

function removeTraverse(t, node, parentNode) {
  switch (node.getType()) {
    case com.google.javascript.rhino.Token.CALL:
      if (isDelegateCallNode(node)) {
        var priority = getPriority(node);
        var key = getDelegateId(node);
        var highestPriorityInMap = dataMap[key];
        if (priority < highestPriorityInMap) {
          parentNode.detachFromParent();
          compiler.reportCodeChange();
        }
      }
      break;
    case com.google.javascript.rhino.Token.FUNCTION:
      var property = node.getProp(com.google.javascript.rhino.Node.ORIGINALNAME_PROP);
      if (property != null) {
        var qualifiedName = property.toString();
        if (allFunctions.indexOf(qualifiedName) != -1 &&
            usedFunctions.indexOf(qualifiedName) == -1) {
          parentNode.getParent().detachFromParent();
          compiler.reportCodeChange();
        }
      }
      break;
  }
}

var DELEGATE_FN_NAME = "soy.$$registerDelegateFn";

function isDelegateCallNode(node) {
  return node.getType() == com.google.javascript.rhino.Token.CALL &&
         DELEGATE_FN_NAME == node.getFirstChild().getQualifiedName();
}

function getPriority(node) {
  return node.getChildAtIndex(3).getDouble();
}

function getDelegateId(node) {
  return getKey(getName(node), getVariant(node));
}

function getKey(name, variant) {
  return name + ":" + variant;
}

function getName(node) {
  return node.getFirstChild().getNext().getLastChild()
      .getString();
};

function getVariant(node) {
  return node.getChildAtIndex(1).getNext().getString();
};

function getTemplateName(node) {
  return node.getChildAtIndex(4).getQualifiedName();
};