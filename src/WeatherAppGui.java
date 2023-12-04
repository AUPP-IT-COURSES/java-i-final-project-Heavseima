import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;
    private JButton searchButton;
    private JLabel errorMessageLabel;
    public class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Define the two colors for the gradient
            Color topColor = new Color(26, 82, 118); // Navy Blue
            Color bottomColor = new Color(204, 229, 255); // Light Blue

            // Create a gradient paint object from top to bottom
            GradientPaint gradientPaint = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);

            // Set the paint to the gradient
            g2d.setPaint(gradientPaint);

            // Fill the entire panel with the gradient color
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    public WeatherAppGui(){
        // setup our gui and add a title
        super("Weather Forecast");

        // configure gui to end the program's process once it has been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the size of our gui (in pixels)
        setSize(450, 650);

        // load our gui at the center of the screen
        setLocationRelativeTo(null);

        // make our layout manager null to manually position our components within the gui
        setLayout(null);

        // prevent any resize of our gui
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents(){
        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(null);
        setContentPane(gradientPanel);

        JLabel weatherLabel = new JLabel("Weather Forecast");

        // Configure the size and position of the JLabel
        weatherLabel.setBounds(0, 0, getWidth(), 50);
        weatherLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Set white color for the text
        weatherLabel.setForeground(Color.WHITE);

        // Change the font style and size
        weatherLabel.setFont(new Font("Roboto", Font.BOLD, 36));

        // Add the JLabel to the content pane of the JFrame
        gradientPanel.add(weatherLabel);

        // Create a JLabel component to hold the date and time
        JLabel dateTimeLabel = new JLabel();

        // Configure the size and position of the JLabel
        dateTimeLabel.setBounds(0, 60, getWidth(), 20);
        dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Set white color for the text
        dateTimeLabel.setForeground(Color.WHITE);

        // Change the font style and size
        dateTimeLabel.setFont(new Font("Roboto", Font.PLAIN, 17));

        // Add a Timer to update the date and time every second
        Timer timer = new Timer(1000, e -> {
            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Format the date and time as a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\uD83D\uDCC5  E, MMMM d, h:mm a");
            String formattedDateTime = now.format(formatter);

            // Set the formatted date and time as the text of the label
            dateTimeLabel.setText(formattedDateTime);
        });
        timer.start();

        // Add the JLabel to the content pane of the JFrame
        gradientPanel.add(dateTimeLabel);

        JTextField searchTextField = new JTextField();

        // set the location and size of our component
        searchTextField.setBounds(13, 105, 355, 50);

        // change the font style and size
        searchTextField.setFont(new Font("Montserrat", Font.PLAIN, 24));


        add(searchTextField);

        // minimum temperature text
        JLabel minTemperatureText = new JLabel("Min: 5 °C");
        minTemperatureText.setBounds(10, 160, 250, 40);
        minTemperatureText.setFont(new Font("Dialog", Font.BOLD, 16));
        minTemperatureText.setHorizontalAlignment(SwingConstants.LEFT);
        add(minTemperatureText);

// maximum temperature text
        JLabel maxTemperatureText = new JLabel("Max: 15 °C");
        maxTemperatureText.setBounds(10, 180, 250, 40);
        maxTemperatureText.setFont(new Font("Dialog", Font.BOLD, 16));
        maxTemperatureText.setHorizontalAlignment(SwingConstants.LEFT);
        add(maxTemperatureText);

        String locationName = "New York"; // Replace "New York" with the desired location name


        // weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 290);
        add(weatherConditionImage);

        // temperature text
        JLabel temperatureText = new JLabel("10 °C");
        temperatureText.setBounds(0, 350, 450, 90);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));

        // center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);
        // weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405, 450, 85);
        weatherConditionDesc.setFont(new Font("Montserrat", Font.BOLD, 38));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // humidity image
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 81);
        add(humidityImage);

        // humidity text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 70);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // windspeed image
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 95);
        add(windspeedImage);

        // windspeed text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310, 500, 85, 80);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);

        // search button
        searchButton = new JButton(loadImage("src/assets/search.png"));

        errorMessageLabel = new JLabel("City is invalid");
        errorMessageLabel.setBounds(0, 100, getWidth(), 50);
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessageLabel.setVisible(false);
        gradientPanel.add(errorMessageLabel);


        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 105, 47, 50);

// Add the label to the main content pane of the JFrame
        add(errorMessageLabel);

        gradientPanel.add(errorMessageLabel);  // Remove this line to avoid adding the label twice

// ...

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset error message visibility
                errorMessageLabel.setVisible(false);

                // get location from user
                String userInput = searchTextField.getText();

                // validate input - remove whitespace to ensure non-empty text
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    // Show error message
                    errorMessageLabel.setText("Please enter a city");
                    errorMessageLabel.setVisible(true);
                    return;
                }

                // retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                // If the weather data is null, it means the city was not found
                if (weatherData == null) {
                    // Show error message
                    errorMessageLabel.setText("City not found");
                    errorMessageLabel.setVisible(true);
                    return;
                }

                // update gui

                // Hide error message if the input is valid
                errorMessageLabel.setVisible(false);

                // update gui

                // Hide error message if the input is valid
                errorMessageLabel.setVisible(false);
                // update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                // depending on the condition, we will update the weather image that corresponds with the condition
                switch(weatherCondition){
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " °C");

                // update weather condition text
                weatherConditionDesc.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");

                // update min and max temperature
                double minTemperature = (double) weatherData.get("min_temperature");
                double maxTemperature = (double) weatherData.get("max_temperature");
                minTemperatureText.setText("Min: " + minTemperature + " °C");
                maxTemperatureText.setText("Max: " + maxTemperature + " °C");
            }
        });
        add(searchButton);
    }


    // used to create images in our gui components
    private ImageIcon loadImage(String resourcePath){
        try{
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns an image icon so that our component can render it
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }
}









