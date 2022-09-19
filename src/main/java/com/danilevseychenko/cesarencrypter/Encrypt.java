package com.danilevseychenko.cesarencrypter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Encrypt extends EncrypterMainController {
    public String encryption(File file, int rot) throws IOException {
        LinkedList<String> textList = new LinkedList<>();
        LinkedList<String> encryptedTextList = new LinkedList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            textList.add(scanner.nextLine());
        }
        scanner.close();
        while (textList.size()>0){
            String tempLine = textList.getFirst();
            String encryptedLine = "";
            if (tempLine.length()>0) {
                for (int i = 0; i < tempLine.length(); i++) {
                    char symbol = tempLine.charAt(i);
                    if (Character.isLetter(symbol)) {
                        if (Character.isLowerCase(symbol)) {
                            if (ALPHABET.indexOf(symbol) != -1) {
                                encryptedLine += ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot) % ALPHABET.length());
                            } else {
                                encryptedLine += symbol;
                            }
                        } else {
                            symbol = Character.toLowerCase(symbol);
                            if (ALPHABET.indexOf(symbol) != -1) {
                                String temp = String.valueOf(ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot) % ALPHABET.length())).toUpperCase();
                                encryptedLine += temp;
                            }else {
                                encryptedLine += symbol;
                            }
                        }
                    } else {
                        if (ALPHABET.indexOf(symbol) != -1) {
                            encryptedLine += ALPHABET.charAt((ALPHABET.indexOf(symbol) + rot) % ALPHABET.length());
                        } else {
                            encryptedLine += symbol;
                        }
                    }
                }
            }
            encryptedTextList.add(encryptedLine);
            textList.removeFirst();
        }
        Path path = Path.of(file.toURI());
        String[] tempArray = path.toString().split(".txt");
        String tempPath = tempArray[0]+"Encrypted.txt";
        Files.createFile(Path.of(tempPath));
        FileWriter fileWriter = new FileWriter(tempPath);
        fileWriter.write("");
        while (encryptedTextList.size()>0){
            fileWriter.append(encryptedTextList.getFirst()).append("\n");
            encryptedTextList.removeFirst();
        }
        fileWriter.close();
        return tempPath;
    }
}