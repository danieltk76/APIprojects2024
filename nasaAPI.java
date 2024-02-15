import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.swing.*;

public class nasaAPI {

    public static void main(String[] args) 
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter a date in YYYY-MM-DD format for the Astronomy Picture of the Day:");
        String userInput = keyboard.nextLine();

        try {
            String jsonResponse = getAstronomyPictureOfTheDay(userInput);
            String imageUrl = extractImageUrl(jsonResponse);
            displayImage(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAstronomyPictureOfTheDay(String date) throws IOException 
    {
        String apiKey = "eiGMQNYZmubhnIIqFd6Iq3jZYCn2h2cTbPwZJrI6"; 
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey + "&date=" + date;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) 
        {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static String extractImageUrl(String json) 
    {
        try (Scanner scanner = new Scanner(json)) 
        {
            scanner.useDelimiter("[,\"]+");
            while (scanner.hasNext()) 
            {
                String nextToken = scanner.next();
                if ("url".equals(nextToken) && scanner.hasNext()) 
                {
                    scanner.next();
                    if (scanner.hasNext()) 
                    {
                        String imageUrl = scanner.next();
                        
                        if (imageUrl.startsWith("http")) 
                        {
                            return imageUrl;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void displayImage(String imageUrl) 
    {
    System.out.println("Image URL: " + imageUrl); 

    JFrame frame = new JFrame("NASA Image of the Day");
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel label = new JLabel("Loading...", JLabel.CENTER);
    frame.getContentPane().add(label, BorderLayout.CENTER);
    frame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() 
        {
           
            public void run() {
                try 
                {
                    ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                    label.setIcon(imageIcon);
                    label.setText(""); 
                } catch (IOException e) 
                {
                    e.printStackTrace();
                    label.setText("Failed to load image");
                }
            }
        });
    }
}
