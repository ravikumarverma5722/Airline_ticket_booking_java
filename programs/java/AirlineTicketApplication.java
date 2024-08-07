
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AirlineTicketApplication {

    public static void main(String[] args) {
        AirlineTicketApplication app = new AirlineTicketApplication();
        app.processTicketBooking("John Doe", "Economy", 2);
    }

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
        
        if ("Economy".equalsIgnoreCase(ticketClass)) {
            pricePerTicket = 100.00;
        } else if ("Business".equalsIgnoreCase(ticketClass)) {
            pricePerTicket = 200.00;
        } else if ("FirstClass".equalsIgnoreCase(ticketClass)) {
            pricePerTicket = 300.00;
        } else {
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
        String jdbcUrl = "jdbc:mysql://localhost:3306/airline";
        String jdbcUsername = "root";
        String jdbcPassword = "password";
        String sql = "INSERT INTO bookings (passenger_name, ticket_class, number_of_tickets, total_cost) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
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
        String apiUrl = "https://api.example.com/confirmBooking";
        String requestPayload = String.format("{\"name\":\"%s\",\"totalCost\":%.2f}", booking.getPassengerName(), booking.getTotalCost());
        
        // Simulating an API call
        System.out.println("Calling API: " + apiUrl);
        System.out.println("Payload: " + requestPayload);
        
        // In a real-world application, you would use a library like HttpClient to make the API call.
    }
}
