package module1.TheaterTicket;

/**
 * @class Participant
 * @brief Represents a person (thread) attempting to buy a theater ticket.
 * Each participant runs as an independent thread that interacts with
 * the shared Ticket resource to claim a seat.
 * @author Your Name
 * @version 1.0
 */
public class Participant extends Thread {

    /** @brief Reference to the shared Ticket resource. */
    private final Ticket count;

    /** @brief The ticket number successfully acquired by this participant. */
    private int number;

    /**
     * @brief Constructor for the Participant class.
     * @param count The shared Ticket object to interact with.
     * @param name The name of the thread (participant identifier).
     */
    public Participant (Ticket count, String name) {
        super(name);
        this.count = count;
    }

    /**
     * @brief Execution entry point for the participant thread.
     * This method calls the shared resource to retrieve a ticket number.
     * Since it is not synchronized, multiple participants might
     * occasionally retrieve the same number.
     */
    @Override
    public void run () {
        this.number = this.count.getTicket();
    }

    /**
     * @brief Gets the ticket number assigned to this participant.
     * @return The integer value of the acquired ticket.
     */
    public int getNumber() {
        return number;
    }
}