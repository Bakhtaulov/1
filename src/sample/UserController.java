package sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
public class UserController {

    @FXML
    private TableView<Pies> table2;

    @FXML
    private TableColumn<Pies, String> Name2;

    @FXML
    private TableColumn<Pies, Double> Price2;

    @FXML
    private TableColumn<Pies, Integer> Amount2;

    @FXML
    private Button AddButton;

    @FXML
    private TextField amountfield;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button SendButton;

    @FXML
    private Label ExitButton;

    @FXML
    private TableView<Pies> table1;

    @FXML
    private TableColumn<Pies, String> Name1;

    @FXML
    private TableColumn<Pies,Double> Discount1;
    @FXML
    private TableColumn<Pies, Double> discountcol1;
    ObservableList<Pies> ob1 = FXCollections.observableArrayList();//ob1
    ObservableList<Pies> ob2 = FXCollections.observableArrayList();//ob2 хранит заказы кооторые нажимаешь добавить
    Pies p1 = new Pies();
    Pies p2 = new Pies();
    Manager connection = new Manager();
    private static String finalPrice;
    @FXML
    void initialize() {
        ob1.clear();
        fill1();
        show1();
        AddButton.setOnAction(event->{
            String amount = amountfield.getText().trim();
            Pies pies;
            pies = p1;
            double a = p1.getPrice()-p1.getPrice()*p1.getDiscount()*0.02;
            pies.setPrice(a);
            pies.setAmount(Integer.parseInt(amount));
            ob2.add(pies);
            show2();
            amountfield.setText("");
        });
        DeleteButton.setOnAction(event->{
            ob2.remove(p2);
            show2();
        });
        SendButton.setOnAction(event->{
            try {
                BufferedWriter br = new BufferedWriter(new FileWriter("input.txt",true));
                String result = "";
                for(int i=0;i<ob2.size();i++){
                    result += "Название: " + ob2.get(i).getName() + " Цена: " + ob2.get(i).getPrice() +" Количество: " + ob2.get(i).getAmount() + "\r\n";
                }
                result += "-------------------------------------------\r\n";
                result += "Общая сумма: " + finalPrice + "\r\n";
                br.write(result);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            totalPrice();
            ob2.clear();
            show2();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("YOUR TOTAL PRICE: " + finalPrice);
            alert.show();
        });
        ExitButton.setOnMouseClicked(event->{
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Главная страница");
                stage.setScene(new Scene(parent));
                stage.show();
                ExitButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void totalPrice() {
        double total =0;
        for(int i=0 ;i<ob2.size();i++){
            total += ob2.get(i).getPrice()*ob2.get(i).getAmount();
        }
        finalPrice = Double.toString(total);
    }

    public void fill1()  {
        try {
            ob1 = connection.getAllPies();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void show1(){
        Name1.setCellValueFactory(new PropertyValueFactory<>("name"));
        Price2.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountcol1.setCellValueFactory(new PropertyValueFactory<>("discount"));
        table1.setItems(ob1);
    }
    public void show2(){
        Name2.setCellValueFactory(new PropertyValueFactory<>("name"));
        Price2.setCellValueFactory(new PropertyValueFactory<>("price"));
        Amount2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table2.setItems(ob2);
    }

    public void select2(MouseEvent mouseEvent) {
        int row = table2.getSelectionModel().getSelectedCells().get(0).getRow();
        p2 = table2.getItems().get(row);
        p2.p1 = false;
    }

    public void select1(MouseEvent mouseEvent) {
        int row = table1.getSelectionModel().getSelectedCells().get(0).getRow();
        p1 = table1.getItems().get(row);
        p1.p1 = true;
    }
}



