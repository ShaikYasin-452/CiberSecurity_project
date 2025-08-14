/**
 * @file ThreatType.java
 * @brief Enumeration of cybersecurity threat types for incident classification
 * 
 * @details This enum defines the various types of security threats that can be detected
 * and managed by the incident response system. Each threat type has specific characteristics
 * that influence risk scoring, response actions, and incident handling procedures.
 * 
 * Threat types are used in conjunction with severity levels to calculate risk scores
 * and determine appropriate response strategies. Different threat types have different
 * base risk scores and generate different sets of recommended actions.
 * 
 * @author Cybersecurity Team
 * @version 1.0
 * @date 2024
 * @see SecurityIncident
 * @see SeverityLevel
 */
public enum ThreatType {
    /** Malicious software including viruses, trojans, ransomware, and spyware */
    MALWARE("Malware Detection"),
    /** Unauthorized access attempts, credential theft, and privilege escalation */
    UNAUTHORIZED_ACCESS("Unauthorized Access"),
    /** Denial of service attacks including DDoS and resource exhaustion */
    DOS_ATTACK("Denial of Service Attack"),
    /** Phishing emails, malicious links, and social engineering attempts */
    PHISHING("Phishing Attempt"),
    /** Data exfiltration, unauthorized data access, and privacy breaches */
    DATA_BREACH("Data Breach"),
    /** Network reconnaissance and port scanning activities */
    PORT_SCAN("Port Scanning"),
    /** Anomalous behavior that doesn't fit other categories */
    SUSPICIOUS_ACTIVITY("Suspicious Activity");
    
    /** Human-readable display name for the threat type */
    private final String displayName;
    
    /**
     * @brief Constructor for ThreatType enum
     * 
     * @param displayName The human-readable name for this threat type
     */
    ThreatType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * @brief Get the human-readable display name
     * 
     * @return String representation of the threat type for UI display
     * 
     * @example
     * ThreatType.MALWARE.getDisplayName(); // Returns "Malware Detection"
     * ThreatType.PHISHING.getDisplayName(); // Returns "Phishing Attempt"
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @brief Get string representation of the threat type
     * 
     * @return The display name of the threat type
     * 
     * @example
     * ThreatType.DATA_BREACH.toString(); // Returns "Data Breach"
     */
    @Override
    public String toString() {
        return displayName;
    }
}
