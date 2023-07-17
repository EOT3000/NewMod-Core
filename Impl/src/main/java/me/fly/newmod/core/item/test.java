package me.fly.newmod.core.item;

public class test {
    private final String thing;

    public static void main(String[] args) {
        System.out.println(new test().thing);
    }

    public test() {
        this.thing = doTheThing();
    }

    private String doTheThing() {
        if(thing == null) {
            return "lol";
        }

        return "what?";
    }
}
