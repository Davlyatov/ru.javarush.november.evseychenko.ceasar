package com.danilevseychenko.cesarencrypter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Scanner;

public class Decrypt extends EncrypterMainController {


    public String decryptionStart(File file,int rot) throws IOException{
        Scanner scanner = new Scanner(file);
        LinkedList<String> encryptedTextList = new LinkedList<>();
        LinkedList<String> decryptedTextList;
        while (scanner.hasNextLine()) {
            encryptedTextList.add(scanner.nextLine());
        }
        scanner.close();
        String tempPath="";
        if (rot==0){
            decryptedTextList = bruteForce(encryptedTextList);
            tempPath = writeFile(decryptedTextList,file);
        }else {
            decryptedTextList = decryption(encryptedTextList,rot);
            tempPath = writeFile(decryptedTextList,file);
        }
        return tempPath;
    }

    private String writeFile(LinkedList<String> decryptedTextList, File file) throws IOException {
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

    private LinkedList<String> decryption(LinkedList<String> encryptedTextList, int rot) {
        rot = -rot;
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
                            if (ALPHABET.indexOf(symbol) != -1){
                                char temp;
                                if (ALPHABET.indexOf(symbol)+rot<0){
                                    temp = ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot + ALPHABET.length()) % ALPHABET.length());
                                } else {
                                    temp = ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot) % ALPHABET.length());
                                }
                                decryptedLine += Character.toUpperCase(temp);
                            } else {
                                decryptedLine += symbol;
                            }
                        } else {
                            if (ALPHABET.indexOf(symbol) != -1){
                                char temp;
                                if (ALPHABET.indexOf(symbol)+rot<0){
                                    temp = ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot + ALPHABET.length()) % ALPHABET.length());
                                } else {
                                    temp = ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot) % ALPHABET.length());
                                }
                                decryptedLine+=temp;
                            } else {
                                decryptedLine += symbol;
                            }
                        }
                    } else {
                        if (ALPHABET.indexOf(symbol) != -1){
                            char temp;
                            if (ALPHABET.indexOf(symbol)+rot<0){
                                temp = ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot + ALPHABET.length()) % ALPHABET.length());
                            } else {
                                temp = ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot) % ALPHABET.length());
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
        while (!c&&i!=ALPHABET.length()){
            decryptedTextList.clear();
            decryptedTextList = decryption(encryptedTextList,i);
            c = chectText(decryptedTextList);
            if (!c){
                i+=1;
            }
        }
        return decryptedTextList;
    }

    private boolean chectText(LinkedList<String> decryptedTextList) {
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
