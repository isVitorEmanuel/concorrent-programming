package module1.TheaterTicket.NoVirtualThreadsWithError;

/**
 * @class Ticket
 * @brief Represents the shared resource of theater tickets.
 * This class manages the count of available seats and provides
 * methods to retrieve and decrement the ticket stock.
 * @author Vitor Emanuel
 * @version 1.0
 */
public class Ticket {

    /** @brief The current counter value for available tickets. */
    private int value;

    /**
     * @brief Constructor for the Ticket class.
     * @param value The initial capacity of theater tickets.
     */
    public Ticket (int value) {
        this.value = value;
    }

    /**
     * @brief Retrieves the current ticket number and decrements the counter.
     * This method uses the post-decrement operator, meaning it returns
     * the current value before subtracting one.
     * @note This method is currently NOT thread-safe (not synchronized),
     * which may lead to duplicate ticket numbers in concurrent environments.
     * @return The ticket number captured by the participant.
     */
    public int getTicket() {
        return this.value--;
    }
}