package com.company;

import java.util.*;

public class Main {

    public static char evaluateFunction(String function, String order, String entries) {
        for(int i = 0; i<order.length(); i++) {
            String letter = String.valueOf(order.charAt(i));
            if(entries.charAt(i) == '1') {
                function = function.replaceAll(letter, "1");
                function = function.replaceAll(letter.toLowerCase(Locale.ROOT), "0");
            } else if(entries.charAt(i) == '0') {
                function = function.replaceAll(letter, "0");
                function = function.replaceAll(letter.toLowerCase(Locale.ROOT), "1");
            }
        }
        for(String str : function.split("\\+")) {
            if(!str.contains("0") && !str.isEmpty()) {
                return '1';
            }
        }
        return '0';
    }

    public static String convertInput(String input) {
        String result = "";
        boolean skipNext = false;
        for(int i = 0; i<input.length(); i++) {
            if(input.charAt(i) == '!' && i != input.length()-1) {
                String letter = String.valueOf(input.charAt(i+1)).toLowerCase(Locale.ROOT);
                result += letter;
                i++;
                skipNext = true;
            }
            if(!skipNext) {
                result += input.charAt(i);
            }
            skipNext = false;
        }
        return result;
    }

    public static BDD bdd_create(String bFunction, String order) {
        return new BDD(bFunction, order);
    }

    public static char BDD_use(BDD bdd, String entries) {
        if(bdd.root == null) {
            return '-';
        }
        int counter = 0;
        Node curr = bdd.root;
        while(curr.value == -1) {
            if(counter > entries.length()-1) {
                return '-';
            }
            if(entries.charAt(counter) == '0'){
                curr = curr.left;
            }
            if(entries.charAt(counter) == '1'){
                curr = curr.right;
            }
            counter++;
        }
        return (curr.value == 1) ? '1' : '0';
    }


    public static String createFunction(String vars){
        Random rand = new Random();
        StringBuilder function = new StringBuilder();
        int conjunctions = 10;
        for (int i = 0; i < conjunctions; i++) {
            int count = 0;
            if(vars.length() <= 3) {
                count = rand.nextInt(vars.length()) + 1;
            } else {
                count = rand.nextInt(3) + 2;
            }

            vars = shuffle(vars);
            for(int j = 0; j<count; j++){
                String letter = String.valueOf(vars.charAt(j));
                if(Math.random() > 0.5) { // 50% negations
                    letter = "!" + letter;
                }
                function.append(letter);
            }
            if(i != conjunctions-1){
                function.append("+");
            }
        }
        return function.toString();
    }

    public static String generateRandomWord(int length) {
        StringBuilder randomWord = new StringBuilder();
        for(int i = 0; i < length; i++) {
            String newLetter = String.valueOf((char) (Math.random() * 26 + 65));
            if(!randomWord.toString().contains(newLetter)){
                randomWord.append(newLetter);
            }
        }
        return randomWord.toString();
    }

    public static String shuffle(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        StringBuilder shuffled = new StringBuilder();
        for (String letter : letters) {
            shuffled.append(letter);
        }
        return shuffled.toString();
    }

    public static void comparison(char result, char realResult){
        if(result == realResult) {
            //System.out.println("Congrats! The result is " + result);
        } else {
            System.out.println("The result should be this: " + realResult + " but you got this: " + result);
        }
    }

    public static String[] entryCreate(int length) {
        int size = (int)Math.pow(2, length);
        String[] array = new String[size];
        for(int i = 0; i<size; i++) {
            String bin = Integer.toBinaryString(i);
            while(bin.length() < length) {
                bin = "0" + bin;
            }
            array[i] = bin;
        }
        return array;
    }


    public static void main(String[] args) {

        // test
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the amount of variables for a function: ");
        int length = sc.nextInt(); // number of variables
        sc.nextLine();

        double averageRate = 0;
        long createTime = 0;
        long bddTime = 0;

        // using 100 randomly generated functions for testing
        for(int i = 0; i < 100; i++) {
            String order = generateRandomWord(length);
            String bFunction = createFunction(order);
            order = shuffle(order);
            long t1 = System.nanoTime();
            BDD bdd = bdd_create(bFunction, order);
            long t2 = System.nanoTime();
            createTime += (t2 - t1);
            double ratio = ((Math.pow(2, length+1) -1) - (double) bdd.getTotalNodes()) / (Math.pow(2, length+1) -1);
            averageRate += ratio;
            String[] entries = entryCreate(length);

            long t3 = System.nanoTime();
            for(int j = 0; j<(int) Math.pow(2,length); j++) {
                char result = BDD_use(bdd, entries[j]);
                char realResult = evaluateFunction(convertInput(bFunction), order, entries[j]);
                comparison(result, realResult);
            }
            long t4 = System.nanoTime();
            bddTime += (t4 - t3);
        }

        // print out results
        System.out.println("Time of bdd create: " + (createTime / 100) + " nanoseconds or " + ((double) createTime / 100000000) + " millis");
        System.out.println("Time of bdd use: " + (bddTime / 100) + " nanoseconds or " + ((double) bddTime / 100000000) + " millis");
        System.out.println("Average reduction rate: " + (Math.round(averageRate * 100.0) / 100.0) + "%");
    }
}
