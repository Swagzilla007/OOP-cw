package config;

public class Configuration {
    private final int releaseRate, retrievalRate, maxCapacity, totalTickets;

    public Configuration(int releaseRate, int retrievalRate, int maxCapacity, int totalTickets) {
        this.releaseRate = releaseRate;
        this.retrievalRate = retrievalRate;
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

    public int getReleaseRate() { return releaseRate; }
    public int getRetrievalRate() { return retrievalRate; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getTotalTickets() { return totalTickets; }
}