package simpletask.test.entities;

import simpletask.main.entities.Task;
import simpletask.main.entities.Workspace;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class TaskTest {
    
    @Test
    public void testIsFinished() {
        Task t1 = new Task("Task One");
        boolean res = false;
        try {
            Method privateIsFinished = Task.class.getDeclaredMethod("isFinished", Workspace.class);
            privateIsFinished.setAccessible(true);
            res = (boolean) privateIsFinished.invoke(t1, t1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(res);
    }

}