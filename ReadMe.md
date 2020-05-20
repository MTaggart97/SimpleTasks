# SimpleTasks

## Summary

SimpleTasks is a task managing app. Tasks are split up into two categories, `Tasks` and `Actions`.

## Dev Notes

Remove `Workspace` interface. I don't want a public interface exposing important information to the Application. i.e. the App doesn't need to be able to change a Task object directly. This can be done through a `WorkspaceManager`. This manager can maintain any meta data associated with the workspace (i.e. hashmaps - nyi).

~~The interface is to be replaced with the `Action` class. With the `Task` class extending it and implementing the concept of a list of tasks, which the `Action` class does not know about.~~

Actually, it should be the other way around. `Task` will be the base class with `Action` extending it. If we think of the Workspace as a tree structure, then each node is actually a `Task` with the `Action` being a special case of a `Task`.
