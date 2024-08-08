import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.*;

public class AirlineTicketApplication {

    private static final String DATABASE_URL = "jdbc:sqlite:airline.db";
    private static final String API_URL = "https://api.example.com/confirmBooking";

    public void processTicketBooking(String passengerName, String ticketClass, int numberOfTickets) {
        double totalCost = calculateTotalCost(ticketClass, numberOfTickets);
        boolean isEligibleForDiscount = checkDiscountEligibility(totalCost);
        
        if (isEligibleForDiscount) {
            totalCost = applyDiscount(totalCost);
        }
        
        Booking booking = new Booking(passengerName, ticketClass, numberOfTickets, totalCost);
        boolean isBookingSuccessful = saveBookingToDatabase(booking);
        
        if (isBookingSuccessful) {
            callConfirmationAPI(booking);
        } else {
            System.out.println("Booking failed. Please try again.");
        }
    }

    public double calculateTotalCost(String ticketClass, int numberOfTickets) {
        double pricePerTicket;
        
        switch (ticketClass.toLowerCase()) {
            case "economy":
                pricePerTicket = 100.00;
                break;
            case "business":
                pricePerTicket = 200.00;
                break;
            case "firstclass":
                pricePerTicket = 300.00;
                break;
            default:
                throw new IllegalArgumentException("Invalid ticket class: " + ticketClass);
        }
        
        return pricePerTicket * numberOfTickets;
    }

    public boolean checkDiscountEligibility(double totalCost) {
        return totalCost > 500.00;
    }

    public double applyDiscount(double totalCost) {
        return totalCost * 0.9; // 10% discount
    }

    public boolean saveBookingToDatabase(Booking booking) {
        String sql = "INSERT INTO bookings (passenger_name, ticket_class, number_of_tickets, total_cost) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getPassengerName());
            pstmt.setString(2, booking.getTicketClass());
            pstmt.setInt(3, booking.getNumberOfTickets());
            pstmt.setDouble(4, booking.getTotalCost());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void callConfirmationAPI(Booking booking) {
        OkHttpClient client = new OkHttpClient();

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("name", booking.getPassengerName());
        requestPayload.put("totalCost", booking.getTotalCost());

        String jsonPayload = new org.json.JSONObject(requestPayload).toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonPayload
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Booking confirmed.");
            } else {
                System.out.println("Failed to confirm booking.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during API call.");
        }
    }
}


public class Main {
    public static void main(String[] args) {
        // This is where your program starts
        System.out.println("Welcome to the Airline Ticket Application!");

        // Create an instance of AirlineTicketApplication
        AirlineTicketApplication app = new AirlineTicketApplication();
        
        // Process a ticket booking
        app.processTicketBooking("John Doe", "Economy", 2);

    }
}
