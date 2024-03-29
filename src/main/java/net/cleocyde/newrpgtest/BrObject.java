package net.cleocyde.newrpgtest;


public abstract class BrObject {

    public interface RunnableFloat {
        void run(float value);
    }

    public interface RunnableFloat2 {
        void run(float value1, float value2);
    }

    public interface RunnableFloat3 {
        void run(float value1, float value2, float value3);
    }

    public static void print(Object... args) {
        var output = "";
        for (var arg : args) {
            output += arg.toString() + " ";
        }
        System.out.println(output);
    }

    public static void Test(Object... args) {

    }
}