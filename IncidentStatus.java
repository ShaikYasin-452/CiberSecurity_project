/**
 * @file IncidentStatus.java
 * @brief Enumeration of incident status values for tracking incident lifecycle
 * 
 * @details This enum defines the various status values that an incident can have
 * throughout its lifecycle in the incident response system. The status indicates
 * the current state of incident handling and determines how it affects system
 * metrics and reporting.
 * 
 * Status transitions follow a logical flow from OPEN through resolution.
 * Only OPEN incidents contribute to active threat levels and network health
 * calculations.
 * 
 * @author Cybersecurity Team
 * @version 1.0
 * @date 2024
 * @see SecurityIncident
 * @see SecurityConsole
 */
public enum IncidentStatus {
    /** Incident is newly created and awaiting initial response */
    OPEN("Open"),
    /** Incident is being actively investigated and handled */
    IN_PROGRESS("In Progress"),
    /** Incident has been escalated to higher authority or specialized team */
    ESCALATED("Escalated"),
    /** Incident has been resolved but not yet closed */
    RESOLVED("Resolved"),
    /** Incident is fully closed and archived */
    CLOSED("Closed");
    
    /** Human-readable display name for the status */
    private final String displayName;
    
    /**
     * @brief Constructor for IncidentStatus enum
     * 
     * @param displayName The human-readable name for this status
     */
    IncidentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * @brief Get the human-readable display name
     * 
     * @return String representation of the status for UI display
     * 
     * @example
     * IncidentStatus.OPEN.getDisplayName(); // Returns "Open"
     * IncidentStatus.IN_PROGRESS.getDisplayName(); // Returns "In Progress"
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @brief Get string representation of the incident status
     * 
     * @return The display name of the status
     * 
     * @example
     * IncidentStatus.RESOLVED.toString(); // Returns "Resolved"
     */
    @Override
    public String toString() {
        return displayName;
    }
}
