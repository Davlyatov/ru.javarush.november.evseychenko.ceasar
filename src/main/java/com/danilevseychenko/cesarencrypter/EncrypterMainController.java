package com.danilevseychenko.cesarencrypter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class EncrypterMainController extends Window {

    public static final String ALPHABET = "абвгдеёжзиклмнопрстуфхцчшщъыьэюя.,\":-!? ";

    @FXML
    private CheckBox bruteForceCB;

    @FXML
    private TextField ROTTextField;
    @FXML
    private Button fileChooseBtn;

    @FXML
    private Label choosenFileLbl;

    @FXML
    private Button startBtn;

    @FXML
    private RadioButton decryptRadioBtn;

    @FXML
    private RadioButton encryptRadioBtn;

    @FXML
    public TextArea textFromFileTA;

    File file;

    @FXML
    public void chooseFile() throws IOException {
        textFromFileTA.setText("");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Текстовые файлы (.txt)","*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Выберите файл");
        file = fileChooser.showOpenDialog(this);
        choosenFileLbl.setText("Выбран файл: "+ file.getName());
        choosenFileLbl.setVisible(true);
        FileReader fileReader = new FileReader(file);
        Scanner scanner = new Scanner(fileReader);
        while (scanner.hasNext()){
            textFromFileTA.appendText(scanner.nextLine()+"\n");
        }
        fileReader.close();
        scanner.close();
    }

    @FXML
    public void checkDecryptRB(){
        if (decryptRadioBtn.isSelected()){
            encryptRadioBtn.setSelected(false);
            bruteForceCB.setDisable(false);
        }
    }

    @FXML
    public void checkEncryptRB(){
        if (encryptRadioBtn.isSelected()){
            decryptRadioBtn.setSelected(false);
            bruteForceCB.setDisable(true);
            ROTTextField.setEditable(true);
        }
    }

    @FXML
    public void checkBruteForceCB(){
        if (bruteForceCB.isSelected()){
            ROTTextField.setEditable(false);
        }else {
            ROTTextField.setEditable(true);
        }
    }

    @FXML
    public void start() throws IOException {
        String temp = ROTTextField.getText();
        if (file!=null) {
            if (encryptRadioBtn.isSelected()) {
                String done="";
                if (temp!=null&& !temp.equals("")) {
                    if (!temp.matches("\\d*")) {
                        ROTTextField.setText(temp.replaceAll("[^\\d]", ""));
                    }
                    int rot = Integer.parseInt(ROTTextField.getText()); // Считывание ключа сдвига с TextField
                    if (rot >= 1 && rot <= 41) {
                        Encrypt encrypt = new Encrypt();
                        done = encrypt.encryption(file, rot);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("");
                        alert.setHeaderText("Неверный сдвиг");
                        alert.setContentText("Введите сдвиг от 1 до "+ ALPHABET.length()+"!");
                        alert.show();
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Готово!");
                    alert.setContentText("Файл зашифрован! Его путь: "+done);
                    alert.show();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("");
                    alert.setHeaderText("Неверный сдвиг");
                    alert.setContentText("Введите сдвиг от 1 до "+ ALPHABET.length()+"!");
                    alert.show();
                }
            } else if (decryptRadioBtn.isSelected()) {
                String done="";
                if (temp!=null&&!temp.equals("")||bruteForceCB.isSelected()) {
                    if (!temp.matches("\\d*")) {
                        ROTTextField.setText(temp.replaceAll("[^\\d]", ""));
                    }
                    int ROT=0;
                    if (!bruteForceCB.isSelected()) {
                        ROT = Integer.parseInt(ROTTextField.getText());
                    }
                    if (bruteForceCB.isSelected()) {
                        ROT = 0;
                        Decrypt decrypt = new Decrypt();
                        done = decrypt.decryptionStart(file, ROT);
                    } else if (!bruteForceCB.isSelected() && ROT >= 1 && ROT <= 41) {
                        Decrypt decrypt = new Decrypt();
                        done = decrypt.decryptionStart(file, ROT);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("");
                        alert.setHeaderText("Неверный параметры");
                        alert.setContentText("Введите сдвиг от 1 до "+ ALPHABET.length()+"! или выберите Brute Force!");
                        alert.show();
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Готово!");
                    alert.setContentText("Файл расшифрован! Его путь: "+done);
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("");
                    alert.setHeaderText("Неверный параметры");
                    alert.setContentText("Введите сдвиг от 1 до "+ ALPHABET.length()+"! или выберите Brute Force!");
                    alert.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("Не выбран метод работы!");
                alert.setContentText("Сначала выберите метод работы!");
                alert.show();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setContentText("Сначала выберите файл!");
            alert.setHeaderText("Не выбран файл!");
            alert.show();
        }
    }
}