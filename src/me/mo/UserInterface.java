package me.mo;

public class UserInterface {
    public static void main(String[] args){
        /*if(args.length != 1){
            System.out.println("Bitte gib einen String an, der Verschlüsselt werden soll.");
            return;
        }

        //String input_string = args[0];*/
        String code = "1234";
        int key = 0;
        String input_string = "AAAhaiabcdefghij";
        System.out.println("The starting String: " + input_string);
        String encrypted_string = Cryptor.encrypt(input_string,key, code);
        System.out.println("The encrypted String: " + encrypted_string);
        String decrypted_string = Cryptor.decrypt(encrypted_string, key, code);
        System.out.println("The decrypted String: " + decrypted_string);

        System.out.println("Brute force by guessing key: ");
        for(String s : Cryptor.decrypt(encrypted_string, code)){
            System.out.println("    " + s);
        }
        System.out.println("Brute force by guessing code: ");
        for(String s : Cryptor.decrypt(encrypted_string, key)){
            System.out.println("    " + s);
        }
        System.out.println("Brute force by guessing key AND code: ");
        for(String s : Cryptor.decrypt(encrypted_string)){
            System.out.println("    " + s);
        }

    }
}
