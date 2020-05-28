package me.mo;

import java.util.ArrayList;

public class Cryptor {
    // int value of characters:                0    1    2    3    4    5    6    7    8    9
    static final char[] allowed_characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};

    private static String filter_cleartext_message(String unfiltered) {
        unfiltered = unfiltered.toLowerCase();
        StringBuilder message_filtered = new StringBuilder();
        for (char message_character : unfiltered.toCharArray()) {
            if(is_valid_character(message_character)){
                message_filtered.append(message_character);
            }
        }
        return message_filtered.toString();
    }

    private static boolean is_valid_character(char c){
        for (char allowed_character : allowed_characters) {
            if (c == allowed_character) {
                return true;
            }
        }
        return false;
    }

    private static boolean is_cleartext_message_valid(String message, boolean throwErrors) {
        if (!message.contains("aaa")) {
            if(throwErrors){
                throw new IllegalArgumentException("Die Nachricht muss 'aaa' beinhalten!");
            }
            return false;
        }

        for (char message_character : message.toCharArray()) {
            if(!is_valid_character(message_character)){
                if(throwErrors){
                    throw new IllegalArgumentException("Die Nachricht enthält unzulässige Buchstaben.");
                }
                return false;
            }
        }
        return true;
    }

    private static boolean is_only_number(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static void validate_code(String code) {
        if (code.length() != 4) {
            throw new IllegalArgumentException("Der Code ist keine 4 Zahlen lang!");
        }
        if (!is_only_number(code)) {
            throw new IllegalArgumentException("Der Code darf nur Zahlen beinhalten.");
        }
    }

    private static void validate_key(int key) {
        if (key > 9 || key < 0) {
            throw new IllegalArgumentException("Der Key muss zwischen 0 und 9 liegen!");
        }
    }

    private static int get_int_value_from_index(String code, int index) {
        return (int) code.toCharArray()[index] - '0';
    }

    // toDo: umlaute umwandeln
    public static String encrypt(String message_cleartext, int key, String code) {
        String input_message_filtered = filter_cleartext_message(message_cleartext);
        is_cleartext_message_valid(input_message_filtered, true);
        validate_key(key);
        validate_code(code);
        return encrypt_string(input_message_filtered, key, code);
    }

    private static String encrypt_string(String message_cleartext, int key, String code){
        char[] message_chararray = message_cleartext.toCharArray();

        StringBuilder message_encrypted = new StringBuilder();

        for (int i = 0; i < message_cleartext.length(); i += 1) {
            int int_value = (int) message_chararray[i] - 'a';
            int encrypted_int_value = (int_value + key + get_int_value_from_index(code, i % 4)) % allowed_characters.length;
            message_encrypted.append((char) (encrypted_int_value + 'a'));
        }

        return message_encrypted.toString();
    }

    private static String decrypt_string(String message_encrypted, int key, String code){
        char[] message_chararray = message_encrypted.toCharArray();
        StringBuilder decrypted_message = new StringBuilder();

        for(int i = 0; i < message_chararray.length; i++){
            int int_value = (int) message_chararray[i] - 'a';
            int decrypted_int_value = (int_value - key - get_int_value_from_index(code, i%4)) % allowed_characters.length;
            decrypted_int_value = decrypted_int_value >= 0 ? decrypted_int_value : allowed_characters.length + decrypted_int_value;

            decrypted_message.append((char) (decrypted_int_value + 'a'));
        }
        return decrypted_message.toString();
    }

    // this decrypt method knows all the secret parameters: the key & the code
    public static String decrypt(String message_encrypted, int key, String code){
        validate_code(code);
        validate_key(key);

        return decrypt_string(message_encrypted, key, code);
    }

    // this decrypt method only knows 1 secret parameter: the code
    // for that reason, we have to brute force the key, which has 10 different possibilities
    public static String[] decrypt(String message_encrypted, String code){
        validate_code(code);

        ArrayList<StringBuilder> possible_decrypted_messages = new ArrayList<>();
        for(int i = 0; i <= 9; i++){
            String decrypted_possibility = decrypt_string(message_encrypted, i, code);
            if(is_cleartext_message_valid(decrypted_possibility, false)){
                possible_decrypted_messages.add(new StringBuilder(decrypted_possibility));
            }
        }

        // convert the StringBuilder ArrayList to an String array.
        return stringbuilder_arraylist_to_string_array(possible_decrypted_messages);
    }

    // this decrypt method also only knows 1 secret parameter: the code
    // the code is 4 digit, so we have 9999 different possibilities
    public static String[] decrypt(String message_encrypted, int key){
        validate_key(key);

        ArrayList<StringBuilder> possible_decrypted_messages = new ArrayList<>();
        for(int i = 0; i <= 9999; i++){
            String code_with_zeroes = String.format("%04d", i);
            String decrypted_possibility = decrypt_string(message_encrypted, key, code_with_zeroes);
            if(is_cleartext_message_valid(decrypted_possibility, false)){
                possible_decrypted_messages.add(new StringBuilder(decrypted_possibility));
            }
        }

        return stringbuilder_arraylist_to_string_array(possible_decrypted_messages);
    }

    // this decrypt method does not know any secret parameters
    // it has to brute force the key and also the code, so we have 10 * 9999 different possibilities
    public static String[] decrypt(String message_encrypted){
        ArrayList<StringBuilder> possible_decrypted_messages = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            for(String s : decrypt(message_encrypted, i)){
                possible_decrypted_messages.add(new StringBuilder(s));
            }
        }
        return stringbuilder_arraylist_to_string_array(possible_decrypted_messages);
    }

    private static String[] stringbuilder_arraylist_to_string_array(ArrayList<StringBuilder> input){
        String[] output = new String[input.size()];
        for(int i = 0; i < output.length; i++) {
            output[i] = input.get(i).toString();
        }
        return output;
    }
}

