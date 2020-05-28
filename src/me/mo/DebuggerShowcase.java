package me.mo;

public class DebuggerShowcase {
    public static void main(String[] args){
        int counter = 0;
        String[] names = {"Peter", "Franz", "Max"};

        for(int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
            counter += 2;
        }
    }
}
