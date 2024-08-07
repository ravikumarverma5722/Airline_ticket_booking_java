
public class Booking {
    private String passengerName;
    private String ticketClass;
    private int numberOfTickets;
    private double totalCost;

    public Booking(String passengerName, String ticketClass, int numberOfTickets, double totalCost) {
        this.passengerName = passengerName;
        this.ticketClass = ticketClass;
        this.numberOfTickets = numberOfTickets;
        this.totalCost = totalCost;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "passengerName='" + passengerName + '\'' +
                ", ticketClass='" + ticketClass + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                ", totalCost=" + totalCost +
                '}';
    }
}
