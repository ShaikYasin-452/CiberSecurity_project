import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @file SecurityIncident.java
 * @brief Core class for managing cybersecurity incidents with comprehensive threat analysis
 * 
 * @details This class represents a security incident with comprehensive threat analysis
 * and response capabilities. It includes risk scoring, status management,
 * and automated response suggestions based on threat type and severity.
 * 
 * The SecurityIncident class is the central data model for the incident response system.
 * It encapsulates all incident-related information and provides methods for:
 * - Risk assessment and scoring
 * - Status management and transitions
 * - Response action generation
 * - Incident lifecycle tracking
 * 
 * Each incident is uniquely identified and contains information about the source
 * and target systems, threat characteristics, and current handling status.
 * 
 * @author Cybersecurity Team
 * @version 1.0
 * @date 2024
 * @see ThreatType
 * @see SeverityLevel
 * @see IncidentStatus
 * @see SecurityConsole
 */
public class SecurityIncident {
    
    /** Unique identifier for the incident */
    private String incidentId;
    /** IP address where the threat originated (attacker/source) */
    private String sourceIP;
    /** IP address of the targeted system (victim/destination) */
    private String targetIP;
    /** Type of security threat detected */
    private ThreatType threatType;
    /** Severity level of the incident */
    private SeverityLevel severity;
    /** Timestamp when the incident was created or last updated */
    private LocalDateTime timestamp;
    /** Current status of the incident in the response lifecycle */
    private IncidentStatus status;
    
    /** Calculated risk score (0.0 to 10.0) based on threat type and severity */
    private double riskScore;
    /** List of recommended response actions for this incident */
    private List<String> responseActions;
    /** Detailed description of the incident */
    private String description;
    /** Flag indicating if immediate response is required */
    private boolean requiresImmediateAction;
    
    /**
     * @brief Constructor - Initialize incident with comprehensive validation
     * 
     * @details Creates a new security incident with all required fields and performs
     * comprehensive validation to ensure data integrity. The constructor automatically
     * calculates risk scores, determines immediate action requirements, and generates
     * appropriate response suggestions.
     * 
     * @param incidentId Unique identifier for the incident (must not be null or empty)
     * @param sourceIP Source IP address where the threat originated (must not be null or empty)
     * @param targetIP Target IP address of the affected system (must not be null or empty)
     * @param threatType Type of threat detected (must not be null)
     * @param severity Severity level of the incident (must not be null)
     * @param description Detailed description of the incident (must not be null or empty)
     * @throws IllegalArgumentException if any validation fails
     * 
     * @example
     * SecurityIncident incident = new SecurityIncident(
     *     "INC-2024-001",
     *     "192.168.1.100",
     *     "10.0.0.5",
     *     ThreatType.MALWARE,
     *     SeverityLevel.HIGH,
     *     "Malware detected on endpoint WIN-07"
     * );
     */
    public SecurityIncident(String incidentId, String sourceIP, String targetIP, 
                           ThreatType threatType, SeverityLevel severity, String description) {
        // Validation to prevent null IP addresses and invalid threat types
        if (incidentId == null || incidentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Incident ID cannot be null or empty");
        }
        if (sourceIP == null || sourceIP.trim().isEmpty()) {
            throw new IllegalArgumentException("Source IP cannot be null or empty");
        }
        if (targetIP == null || targetIP.trim().isEmpty()) {
            throw new IllegalArgumentException("Target IP cannot be null or empty");
        }
        if (threatType == null) {
            throw new IllegalArgumentException("Threat type cannot be null");
        }
        if (severity == null) {
            throw new IllegalArgumentException("Severity level cannot be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        
        this.incidentId = incidentId;
        this.sourceIP = sourceIP;
        this.targetIP = targetIP;
        this.threatType = threatType;
        this.severity = severity;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.status = IncidentStatus.OPEN;
        this.responseActions = new ArrayList<>();
        
        // Calculate initial risk score and determine if immediate response is needed
        this.riskScore = calculateRiskScore();
        this.requiresImmediateAction = requiresImmediateResponse();
        
        // Generate initial response suggestions
        this.responseActions = generateResponseSuggestions();
    }
    
    /**
     * @brief Calculate threat risk score on a 0-10 scale
     * 
     * @details Calculates a risk score based on the threat type's base risk and
     * the incident's severity level. The calculation uses a multiplier system
     * where different threat types have different base scores, and severity
     * levels provide multipliers to adjust the final score.
     * 
     * Base scores by threat type:
     * - Malware: 8.0
     * - Unauthorized Access: 9.0
     * - DoS Attack: 7.0
     * - Phishing: 6.0
     * - Data Breach: 9.5
     * - Port Scan: 4.0
     * - Suspicious Activity: 5.0
     * 
     * Severity multipliers:
     * - Low: 0.5x
     * - Medium: 1.0x
     * - High: 1.5x
     * - Critical: 2.0x
     * 
     * @return Risk score between 0.0 and 10.0, with higher scores indicating greater risk
     * 
     * @example
     * SecurityIncident incident = new SecurityIncident(...);
     * double risk = incident.calculateRiskScore(); // Returns value between 0.0-10.0
     */
    public double calculateRiskScore() {
        double baseScore = 0.0;
        
        // Base score from threat type
        switch (threatType) {
            case MALWARE:
                baseScore = 8.0;
                break;
            case UNAUTHORIZED_ACCESS:
                baseScore = 9.0;
                break;
            case DOS_ATTACK:
                baseScore = 7.0;
                break;
            case PHISHING:
                baseScore = 6.0;
                break;
            case DATA_BREACH:
                baseScore = 9.5;
                break;
            case PORT_SCAN:
                baseScore = 4.0;
                break;
            case SUSPICIOUS_ACTIVITY:
                baseScore = 5.0;
                break;
        }
        
        // Adjust based on severity level
        double severityMultiplier = 1.0;
        switch (severity) {
            case LOW:
                severityMultiplier = 0.5;
                break;
            case MEDIUM:
                severityMultiplier = 1.0;
                break;
            case HIGH:
                severityMultiplier = 1.5;
                break;
            case CRITICAL:
                severityMultiplier = 2.0;
                break;
        }
        
        // Prevent division by zero and ensure score is within bounds
        double finalScore = baseScore * severityMultiplier;
        return Math.min(10.0, Math.max(0.0, finalScore));
    }
    
    /**
     * @brief Determine if critical action is needed based on risk factors
     * 
     * @details Evaluates whether this incident requires immediate response based on
     * severity level, risk score, and threat type. Immediate action is required if:
     * - Severity is CRITICAL
     * - Risk score is 8.0 or higher
     * - Threat type is UNAUTHORIZED_ACCESS, DATA_BREACH, or MALWARE
     * 
     * @return true if immediate response is required, false otherwise
     * 
     * @example
     * SecurityIncident incident = new SecurityIncident(...);
     * if (incident.requiresImmediateResponse()) {
     *     // Activate emergency response procedures
     *     activateEmergencyResponse();
     * }
     */
    public boolean requiresImmediateResponse() {
        // Critical conditions that require immediate response
        if (severity == SeverityLevel.CRITICAL) {
            return true;
        }
        
        if (riskScore >= 8.0) {
            return true;
        }
        
        // Specific threat types that always need immediate attention
        return threatType == ThreatType.UNAUTHORIZED_ACCESS || 
               threatType == ThreatType.DATA_BREACH ||
               threatType == ThreatType.MALWARE;
    }
    
    /**
     * @brief Generate list of recommended actions based on threat type and severity
     * 
     * @details Creates a list of recommended response actions tailored to the specific
     * threat type and severity level. Each threat type has a predefined set of
     * appropriate response actions. For critical severity incidents, additional
     * emergency procedures are added to the beginning of the list.
     * 
     * @return List of recommended response actions as strings
     * 
     * @example
     * SecurityIncident incident = new SecurityIncident(...);
     * List<String> actions = incident.generateResponseSuggestions();
     * for (String action : actions) {
     *     System.out.println("Recommended: " + action);
     * }
     */
    public List<String> generateResponseSuggestions() {
        List<String> suggestions = new ArrayList<>();
        
        switch (threatType) {
            case MALWARE:
                suggestions.add("Isolate affected system immediately");
                suggestions.add("Initiate full system scan");
                suggestions.add("Update antivirus signatures");
                suggestions.add("Notify IT security team");
                break;
                
            case UNAUTHORIZED_ACCESS:
                suggestions.add("Lock compromised accounts");
                suggestions.add("Change affected passwords");
                suggestions.add("Review access logs");
                suggestions.add("Enable additional authentication");
                break;
                
            case DOS_ATTACK:
                suggestions.add("Activate DDoS protection");
                suggestions.add("Block source IP addresses");
                suggestions.add("Scale up server resources");
                suggestions.add("Monitor network traffic");
                break;
                
            case PHISHING:
                suggestions.add("Block phishing URLs");
                suggestions.add("Send security awareness alert");
                suggestions.add("Review email security settings");
                suggestions.add("Monitor for credential theft");
                break;
                
            case DATA_BREACH:
                suggestions.add("Secure affected systems");
                suggestions.add("Assess data exposure scope");
                suggestions.add("Notify legal and compliance teams");
                suggestions.add("Prepare incident report");
                break;
                
            case PORT_SCAN:
                suggestions.add("Monitor for follow-up attacks");
                suggestions.add("Review firewall rules");
                suggestions.add("Block scanning IP if persistent");
                suggestions.add("Update intrusion detection rules");
                break;
                
            case SUSPICIOUS_ACTIVITY:
                suggestions.add("Investigate activity patterns");
                suggestions.add("Review user permissions");
                suggestions.add("Monitor for escalation");
                suggestions.add("Update security policies");
                break;
        }
        
        // Add severity-based actions
        if (severity == SeverityLevel.CRITICAL) {
            suggestions.add(0, "ACTIVATE INCIDENT RESPONSE TEAM");
            suggestions.add(1, "Initiate emergency procedures");
        }
        
        return suggestions;
    }
    
    /**
     * @brief Update incident status with validation
     * 
     * @details Updates the incident status and performs validation to ensure
     * status transitions are logical. The method prevents invalid transitions
     * such as reopening a resolved incident. When status is updated, the
     * timestamp is refreshed and risk calculations are updated.
     * 
     * @param newStatus New status to set for the incident
     * @throws IllegalArgumentException if status transition is invalid
     * 
     * @example
     * SecurityIncident incident = new SecurityIncident(...);
     * incident.updateIncidentStatus(IncidentStatus.IN_PROGRESS);
     * incident.updateIncidentStatus(IncidentStatus.RESOLVED);
     */
    public void updateIncidentStatus(IncidentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        
        // Validate status transitions
        if (status == IncidentStatus.RESOLVED && newStatus == IncidentStatus.OPEN) {
            throw new IllegalArgumentException("Cannot reopen a resolved incident");
        }
        
        this.status = newStatus;
        
        // Update timestamp for status change
        this.timestamp = LocalDateTime.now();
        
        // Recalculate risk score and response suggestions
        this.riskScore = calculateRiskScore();
        this.responseActions = generateResponseSuggestions();
    }
    
    /**
     * @brief Display formatted incident details
     * 
     * @details Prints a comprehensive, formatted view of all incident information
     * including basic details, risk assessment, and recommended actions. The output
     * is designed for console display with clear section separators and organized
     * information presentation.
     * 
     * @example
     * SecurityIncident incident = new SecurityIncident(...);
     * incident.displayIncidentDetails();
     * // Output includes formatted incident information
     */
    public void displayIncidentDetails() {
        System.out.println("\n=== SECURITY INCIDENT DETAILS ===");
        System.out.println("ID: " + incidentId);
        System.out.println("Status: " + status);
        System.out.println("Severity: " + severity);
        System.out.println("Threat Type: " + threatType);
        System.out.println("Risk Score: " + String.format("%.1f/10", riskScore));
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Source IP: " + sourceIP);
        System.out.println("Target IP: " + targetIP);
        System.out.println("Description: " + description);
        System.out.println("Requires Immediate Action: " + (requiresImmediateAction ? "YES" : "NO"));
        
        System.out.println("\nRecommended Actions:");
        for (int i = 0; i < responseActions.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + responseActions.get(i));
        }
        System.out.println("================================\n");
    }
    
    // Getters and setters with proper encapsulation
    /**
     * @brief Get the incident ID
     * @return Unique identifier for the incident
     */
    public String getIncidentId() { return incidentId; }
    
    /**
     * @brief Get the source IP address
     * @return IP address where the threat originated
     */
    public String getSourceIP() { return sourceIP; }
    
    /**
     * @brief Get the target IP address
     * @return IP address of the targeted system
     */
    public String getTargetIP() { return targetIP; }
    
    /**
     * @brief Get the threat type
     * @return Type of security threat detected
     */
    public ThreatType getThreatType() { return threatType; }
    
    /**
     * @brief Get the severity level
     * @return Severity level of the incident
     */
    public SeverityLevel getSeverity() { return severity; }
    
    /**
     * @brief Get the timestamp
     * @return When the incident was created or last updated
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    
    /**
     * @brief Get the current status
     * @return Current status of the incident
     */
    public IncidentStatus getStatus() { return status; }
    
    /**
     * @brief Get the calculated risk score
     * @return Risk score between 0.0 and 10.0
     */
    public double getRiskScore() { return riskScore; }
    
    /**
     * @brief Check if immediate action is required
     * @return true if immediate response is needed
     */
    public boolean isRequiresImmediateAction() { return requiresImmediateAction; }
    
    /**
     * @brief Get recommended response actions
     * @return Defensive copy of the response actions list
     */
    public List<String> getResponseActions() { return new ArrayList<>(responseActions); }
    
    /**
     * @brief Get the incident description
     * @return Detailed description of the incident
     */
    public String getDescription() { return description; }
    
    /**
     * @brief Builder pattern for complex incident creation
     * 
     * @details Provides a fluent interface for creating SecurityIncident objects
     * with optional parameters. The builder pattern allows for more readable
     * and flexible object construction, especially when dealing with many
     * optional parameters.
     * 
     * @author Cybersecurity Team
     * @version 1.0
     */
    public static class SecurityIncidentBuilder {
        private String incidentId;
        private String sourceIP;
        private String targetIP;
        private ThreatType threatType;
        private SeverityLevel severity;
        private String description;
        
        /**
         * @brief Set the incident ID
         * @param incidentId Unique identifier for the incident
         * @return This builder instance for method chaining
         */
        public SecurityIncidentBuilder incidentId(String incidentId) {
            this.incidentId = incidentId;
            return this;
        }
        
        /**
         * @brief Set the source IP address
         * @param sourceIP IP address where the threat originated
         * @return This builder instance for method chaining
         */
        public SecurityIncidentBuilder sourceIP(String sourceIP) {
            this.sourceIP = sourceIP;
            return this;
        }
        
        /**
         * @brief Set the target IP address
         * @param targetIP IP address of the targeted system
         * @return This builder instance for method chaining
         */
        public SecurityIncidentBuilder targetIP(String targetIP) {
            this.targetIP = targetIP;
            return this;
        }
        
        /**
         * @brief Set the threat type
         * @param threatType Type of security threat detected
         * @return This builder instance for method chaining
         */
        public SecurityIncidentBuilder threatType(ThreatType threatType) {
            this.threatType = threatType;
            return this;
        }
        
        /**
         * @brief Set the severity level
         * @param severity Severity level of the incident
         * @return This builder instance for method chaining
         */
        public SecurityIncidentBuilder severity(SeverityLevel severity) {
            this.severity = severity;
            return this;
        }
        
        /**
         * @brief Set the incident description
         * @param description Detailed description of the incident
         * @return This builder instance for method chaining
         */
        public SecurityIncidentBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * @brief Build the SecurityIncident object
         * 
         * @details Creates a new SecurityIncident instance with all the parameters
         * set in the builder. This method will throw an IllegalArgumentException
         * if any required parameters are missing or invalid.
         * 
         * @return A new SecurityIncident instance
         * @throws IllegalArgumentException if validation fails
         * 
         * @example
         * SecurityIncident incident = SecurityIncident.builder()
         *     .incidentId("INC-2024-001")
         *     .sourceIP("192.168.1.100")
         *     .targetIP("10.0.0.5")
         *     .threatType(ThreatType.MALWARE)
         *     .severity(SeverityLevel.HIGH)
         *     .description("Malware detected on endpoint")
         *     .build();
         */
        public SecurityIncident build() {
            return new SecurityIncident(incidentId, sourceIP, targetIP, threatType, severity, description);
        }
    }
    
    /**
     * @brief Create a new builder instance
     * @return A new SecurityIncidentBuilder for fluent construction
     */
    public static SecurityIncidentBuilder builder() {
        return new SecurityIncidentBuilder();
    }
    
    /**
     * @brief Check equality based on incident ID
     * @param obj Object to compare with
     * @return true if incidents have the same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecurityIncident that = (SecurityIncident) obj;
        return Objects.equals(incidentId, that.incidentId);
    }
    
    /**
     * @brief Generate hash code based on incident ID
     * @return Hash code for the incident
     */
    @Override
    public int hashCode() {
        return Objects.hash(incidentId);
    }
    
    /**
     * @brief Get string representation of the incident
     * @return Formatted string with incident ID, type, severity, and risk score
     */
    @Override
    public String toString() {
        return String.format("SecurityIncident{id='%s', type=%s, severity=%s, risk=%.1f}", 
                           incidentId, threatType, severity, riskScore);
    }
}
