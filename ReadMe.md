# SimpleTasks

## Summary

SimpleTasks is a task managing app. Tasks are split up into two categories, `Tasks` and `Actions`.

## Objects

### WorkspaceNode

The basic starting block for any object that is to be managed by the [Workspace Manager](#workspacemanager). It contains all the required fields that a node may need, name, description, due date etc.

### Task

Extends [WorkspaceNode](#workspacenode). This object will maintain a list of other WorkspaceNodes. It can move them and create other nodes. The main use of these types of nodes is for grouping similar tasks together. For example, todo, in progress, assignment 1, sprint 3 etc.

### Action

Extends [WorkspaceNode](#workspacenode). This object represents a singular action that cannot be broken down into more sub tasks. It is a very simple implementation of a node that does not maintain any tasks.

### WorkspaceManager

Manages the workspace. The workspace consists of a root [WorkspaceNode](#workspacenode), generally a [Task](#task), but it does not need to be. All interaction by GUI's or other external processes are done through the manager. It maintains two nodes, the root node and the current node. The root node is the top level node and should not change (I think). While the current node represents the node that the manager is currently managing. i.e. if you add another node, it will be added to the current node.
