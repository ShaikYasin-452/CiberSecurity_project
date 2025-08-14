/**
 * @file SeverityLevel.java
 * @brief Enumeration of incident severity levels for cybersecurity incident classification
 * 
 * @details This enum defines the severity levels used to classify security incidents
 * and determine appropriate response actions. Each severity level has a numeric
 * value for comparison and a human-readable display name.
 * 
 * The severity levels are used in conjunction with threat types to calculate
 * risk scores and determine whether immediate action is required.
 * 
 * @author Cybersecurity Team
 * @version 1.0
 * @date 2024
 * @see SecurityIncident
 * @see ThreatType
 */
public enum SeverityLevel {
    /** Low severity - minimal impact, routine handling */
    LOW("Low", 1),
    /** Medium severity - moderate impact, standard response */
    MEDIUM("Medium", 2),
    /** High severity - significant impact, urgent response */
    HIGH("High", 3),
    /** Critical severity - severe impact, immediate response required */
    CRITICAL("Critical", 4);
    
    /** Human-readable display name for the severity level */
    private final String displayName;
    /** Numeric level for comparison operations */
    private final int level;
    
    /**
     * @brief Constructor for SeverityLevel enum
     * 
     * @param displayName The human-readable name for this severity level
     * @param level The numeric level for comparison (higher = more severe)
     */
    SeverityLevel(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    /**
     * @brief Get the human-readable display name
     * 
     * @return String representation of the severity level for UI display
     * 
     * @example
     * SeverityLevel.HIGH.getDisplayName(); // Returns "High"
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @brief Get the numeric level for comparison
     * 
     * @return Integer value representing the severity level (1-4)
     * 
     * @example
     * SeverityLevel.CRITICAL.getLevel(); // Returns 4
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * @brief Compare this severity level with another
     * 
     * @param other The SeverityLevel to compare against
     * @return true if this level is higher than the other, false otherwise
     * 
     * @example
     * SeverityLevel.HIGH.isHigherThan(SeverityLevel.MEDIUM); // Returns true
     * SeverityLevel.LOW.isHigherThan(SeverityLevel.CRITICAL); // Returns false
     */
    public boolean isHigherThan(SeverityLevel other) {
        return this.level > other.level;
    }
    
    /**
     * @brief Get string representation of the severity level
     * 
     * @return The display name of the severity level
     * 
     * @example
     * SeverityLevel.CRITICAL.toString(); // Returns "Critical"
     */
    @Override
    public String toString() {
        return displayName;
    }
}
