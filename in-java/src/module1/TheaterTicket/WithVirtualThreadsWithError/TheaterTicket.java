package module1.TheaterTicket.WithVirtualThreadsWithError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class TheaterTicket
 * @brief Main execution class for the theater ticket simulation.
 * * This class initializes the shared ticket resource, manages the creation
 * and synchronization of participant threads, and generates a final report.
 * @author Vitor Emanuel
 * @version 1.0
 */
public class TheaterTicket {

    /**
     * @brief Application entry point.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        run();
    }

    /**
     * @brief Orchestrates the simulation logic and reporting.
     * * This method retrieves the processed list of participants,
     * compiles their ticket numbers into a frequency map, and
     * displays the final occupancy report.
     */
    private static void run() {
        List<Participant> participants = getParticipants();

        // Map to count how many participants acquired each ticket number
        Map<Integer, Integer> ticketCountMap = new HashMap<>();

        for (Participant p : participants) {
            int numTicket = p.getNumber();
            // Count occurrences of each ticket ID
            ticketCountMap.put(numTicket, ticketCountMap.getOrDefault(numTicket, 0) + 1);
        }

        // Print final occupancy report
        System.out.println("\n--- Occupancy Report ---");
        ticketCountMap.forEach((num, qtd) -> {
            System.out.println("Ticket #" + num + ": " + qtd + " participant(s)");
        });

        System.out.println("\nTotal unique tickets generated: " + ticketCountMap.size());
        System.out.println("Total participants processed: " + participants.size());
    }

    /**
     * @brief Creates, starts, and synchronizes participant threads using the Fluent API for Virtual Threads.
     * This method demonstrates the use of Thread.ofVirtual() to configure each thread.
     * @return A list of all Participant instances after execution.
     */
    private static List<Participant> getParticipants() {
        final int TOTAL_TICKETS = 100000;
        Ticket ticket = new Ticket(TOTAL_TICKETS);
        List<Participant> participants = new ArrayList<>();

        for (int i = 0; i < TOTAL_TICKETS; i++) {
            Participant p = new Participant(ticket, "Participant " + i);
            participants.add(p);

            Thread t = Thread.ofVirtual()
                    .name("Participant-" + i)
                    .unstarted(p);

            t.start();
        }

        participants.forEach(p -> {
            try {
                p.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        });

        return participants;
    }
}