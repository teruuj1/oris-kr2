package ru.kpfu.itis304.chat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kpfu.itis304.chat.service.CurrencyService;
import ru.kpfu.itis304.chat.service.WeatherService;

public class ChatBotApplication extends Application {

    private TextArea chatArea;
    private TextField inputField;
    private WeatherService weatherService;
    private CurrencyService currencyService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        chatArea = new TextArea();
        chatArea.setEditable(false);
        inputField = new TextField();
        Button sendButton = new Button("send");

        weatherService = new WeatherService();
        currencyService = new CurrencyService();

        sendButton.setOnAction(e -> processInput());

        VBox layout = new VBox(10, chatArea, inputField, sendButton);
        Scene scene = new Scene(layout, 600, 300);

        primaryStage.setScene(scene);
        primaryStage.setTitle("chat Bot");
        primaryStage.show();
    }

    private void processInput() {
        String input = inputField.getText().trim();
        chatArea.appendText("you: " + input + "\n");
        inputField.clear();

        switch (input.toLowerCase()) {
            case "list":
                chatArea.appendText("""
                        botik: available commands:
                           list - shows a list of available commands;
                           weather <city> - shows the weather in the city;
                           exchange <currency> - shows the exchange rate of the specified currency to the RUB;
                           quite - closes the application.
                        """);

                break;
            case "quit":
                chatArea.appendText("botik: goodbye!\n");
                break;
            default:
                if (input.startsWith("weather ")) {
                    String city = input.substring(8);
                    String weatherInfo = weatherService.getWeather(city);
                    chatArea.appendText("botik: " + weatherInfo + "\n");
                } else if (input.startsWith("exchange ")) {
                    String currency = input.substring(9);
                    String exchangeRate = currencyService.getExchangeRate(currency);
                    chatArea.appendText("botik: " + exchangeRate + "\n");
                } else {
                    chatArea.appendText("botik: unknown command. type 'list' for available commands.\n");
                }
                break;
        }
    }
}
