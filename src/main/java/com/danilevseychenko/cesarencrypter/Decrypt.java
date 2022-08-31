package com.danilevseychenko.cesarencrypter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Scanner;

public class Decrypt extends encrypterMainController{

    private static final String alphabet = "абвгдеёжзиклмнопрстуфхцчшщъыьэюя.,\":-!? ";

    public String decryptionStart(File file,int ROT) throws IOException{
        Scanner scanner = new Scanner(file);
        LinkedList<String> encryptedTextList = new LinkedList<>();
        LinkedList<String> decryptedTextList;
        while (scanner.hasNextLine()) {
            encryptedTextList.add(scanner.nextLine());
        }
        scanner.close();
        String tempPath="";
        if (ROT==0){
            decryptedTextList = bruteForce(encryptedTextList);
            tempPath = WriteFile(decryptedTextList,file);
        }else {
            decryptedTextList = Decryption(encryptedTextList,ROT);
            tempPath = WriteFile(decryptedTextList,file);
        }
        return tempPath;
    }

    private String WriteFile(LinkedList<String> decryptedTextList, File file) throws IOException {
        Path path = Path.of(file.toURI());
        String[] tempArray = path.toString().split(".txt");
        String tempPath = tempArray[0]+"Decrypted.txt";
        Files.createFile(Path.of(tempPath));
        FileWriter fileWriter = new FileWriter(tempPath);
        fileWriter.write("");
        while (decryptedTextList.size()>0){
            fileWriter.append(decryptedTextList.getFirst()).append("\n");
            decryptedTextList.removeFirst();
        }
        fileWriter.close();
        return tempPath;
    }

    private LinkedList<String> Decryption(LinkedList<String> encryptedTextList, int ROT) {
        ROT = -ROT;
        LinkedList<String> decryptedTextList = new LinkedList<>();
        for (int i = 0; i < encryptedTextList.size(); i++) {
            String tempLine = encryptedTextList.get(i);
            String decryptedLine = "";
            if (tempLine.length()>0){
                for (int j = 0; j < tempLine.length(); j++) {
                    char symbol = tempLine.charAt(j);
                    if (Character.isLetter(symbol)){
                        if (Character.isUpperCase(symbol)){
                            symbol = Character.toLowerCase(symbol);
                            if (alphabet.indexOf(symbol) != -1){
                                char temp;
                                if (alphabet.indexOf(symbol)+ROT<0){
                                    temp = alphabet.charAt((alphabet.indexOf(symbol) + ROT + alphabet.length()) % alphabet.length());
                                } else {
                                    temp = alphabet.charAt((alphabet.indexOf(symbol) + ROT) % alphabet.length());
                                }
                                decryptedLine += Character.toUpperCase(temp);
                            } else {
                                decryptedLine += symbol;
                            }
                        } else {
                            if (alphabet.indexOf(symbol) != -1){
                                char temp;
                                if (alphabet.indexOf(symbol)+ROT<0){
                                    temp = alphabet.charAt((alphabet.indexOf(symbol) + ROT + alphabet.length()) % alphabet.length());
                                } else {
                                    temp = alphabet.charAt((alphabet.indexOf(symbol) + ROT) % alphabet.length());
                                }
                                decryptedLine+=temp;
                            } else {
                                decryptedLine += symbol;
                            }
                        }
                    } else {
                        if (alphabet.indexOf(symbol) != -1){
                            char temp;
                            if (alphabet.indexOf(symbol)+ROT<0){
                                temp = alphabet.charAt((alphabet.indexOf(symbol) + ROT + alphabet.length()) % alphabet.length());
                            } else {
                                temp = alphabet.charAt((alphabet.indexOf(symbol) + ROT) % alphabet.length());
                            }
                            decryptedLine+=temp;
                        } else {
                            decryptedLine += symbol;
                        }
                    }
                }
            }
            decryptedTextList.add(decryptedLine);
        }
        return decryptedTextList;
    }

    private LinkedList<String> bruteForce(LinkedList<String> encryptedTextList) {
        boolean c = false;
        LinkedList<String> decryptedTextList = new LinkedList<>();
        int i=0;
        while (!c&&i!=alphabet.length()){
            decryptedTextList.clear();
            decryptedTextList = Decryption(encryptedTextList,i);
            c = ChectText(decryptedTextList);
            if (!c){
                i+=1;
            }
        }
        return decryptedTextList;
    }

    private boolean ChectText(LinkedList<String> decryptedTextList) {
        String s = decryptedTextList.getFirst();
        char[] chars = s.toCharArray();
        if (chars.length!=0) {
            for (int i = 0; i < chars.length; i++) {
                String char1 = String.valueOf(chars[i]);
                if (char1.equals(".")) {
                    String char2 = String.valueOf(chars[i + 1]);
                    char character = chars[i + 2];
                    if ((char2.equals(" ") && Character.isUpperCase(character))) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

}
