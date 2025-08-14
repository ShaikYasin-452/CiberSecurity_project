import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @file SecurityConsole.java
 * @brief Main console application for Cybersecurity Incident Response System
 * 
 * @details This is the main entry point for the cybersecurity incident response system.
 * It provides a comprehensive console interface for managing security incidents,
 * monitoring threats, and generating reports. The console features real-time
 * system monitoring, interactive incident management, and automated background
 * processes for threat detection and alerting.
 * 
 * The SecurityConsole implements a menu-driven interface with the following
 * main features:
 * - Incident reporting and creation
 * - Incident analysis and status management
 * - Security reporting and statistics
 * - Real-time system status monitoring
 * - Background threat monitoring and alerting
 * - Interactive help system
 * 
 * The application maintains system metrics including active incidents,
 * threat levels, network health, and resolution statistics. Background
 * monitoring runs continuously to update these metrics and generate
 * alerts for suspicious activities.
 * 
 * @author Cybersecurity Team
 * @version 1.0
 * @date 2024
 * @see SecurityIncident
 * @see ThreatType
 * @see SeverityLevel
 * @see IncidentStatus
 */
public class SecurityConsole {
    
    /** Scanner for reading user input from console */
    private static final Scanner scanner = new Scanner(System.in);
    /** List to store all security incidents */
    private static final List<SecurityIncident> incidents = new ArrayList<>();
    /** Random number generator for simulation purposes */
    private static final Random random = new Random();
    /** Scheduler for background monitoring tasks */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // System monitoring variables
    /** Number of currently open incidents */
    private static int activeIncidents = 0;
    /** Number of incidents resolved today */
    private static int resolvedToday = 0;
    /** Overall threat level based on active incidents (0.0 to 10.0) */
    private static double overallThreatLevel = 0.0;
    /** Network health percentage (100% = healthy, decreases with incidents) */
    private static int networkHealth = 100;
    /** Timestamp of the last system scan */
    private static LocalDateTime lastScan = LocalDateTime.now();
    /** List of current system alerts */
    private static List<String> alerts = new ArrayList<>();
    
    /**
     * @brief Main entry point for the Security Console application
     * 
     * @details Initializes the system, displays the welcome screen, starts
     * background monitoring, and runs the main application loop. The method
     * handles cleanup when the application exits.
     * 
     * @param args Command line arguments (not used)
     * 
     * @example
     * public static void main(String[] args) {
     *     // Application starts automatically
     *     // User interacts with console menu
     *     // Application exits when user selects 'Q'
     * }
     */
    public static void main(String[] args) {
        initializeSystem();
        displayWelcomeScreen();
        
        // Start background monitoring
        startBackgroundMonitoring();
        
        // Main application loop
        runMainLoop();
        
        // Cleanup
        scheduler.shutdown();
        scanner.close();
    }
    
    /**
     * @brief Initialize the system without any preloaded sample data
     * 
     * @details Clears all incidents and alerts to ensure a clean, user-driven
     * experience. Updates system metrics to reflect the initial state.
     * 
     * @example
     * initializeSystem();
     * // System is now ready for user interaction
     */
    private static void initializeSystem() {
        incidents.clear();
        alerts.clear();
        updateSystemMetrics();
    }
    
    /**
     * @brief Display the welcome screen with system status
     * 
     * @details Shows the main console interface including system status,
     * recent threats, current alerts, and the main menu. This method
     * provides the primary user interface for the application.
     * 
     * @example
     * displayWelcomeScreen();
     * // Console displays current system state and menu options
     */
    private static void displayWelcomeScreen() {
        clearScreen();
        System.out.println("=== CYBERSECURITY INCIDENT RESPONSE CONSOLE ===");
        System.out.println();
        displaySystemStatus();
        System.out.println();
        displayRecentThreats();
        System.out.println();
        displayAlerts();
        System.out.println();
        displayMenu();
    }
    
    /**
     * @brief Display current system status
     * 
     * @details Shows key system metrics including active incidents,
     * threat level, network health, and last scan time. The information
     * is formatted for easy reading with icons and clear labels.
     * 
     * @example
     * displaySystemStatus();
     * // Output: Active Incidents: 2 Open, 5 Resolved Today
     * //         Threat Level: HIGH (Score: 7.5/10)
     * //         Network Health: 96% (4 anomalies detected)
     * //         Last Scan: 30 seconds ago
     */
    private static void displaySystemStatus() {
        System.out.println("🛡️  SYSTEM STATUS:");
        System.out.println("   ├─ Active Incidents: " + activeIncidents + " Open, " + resolvedToday + " Resolved Today");
        System.out.println("   ├─ Threat Level: " + getThreatLevelString() + " (Score: " + String.format("%.1f/10", overallThreatLevel) + ")");
        System.out.println("   ├─ Network Health: " + networkHealth + "% (" + (100 - networkHealth) + " anomalies detected)");
        System.out.println("   └─ Last Scan: " + getTimeSinceLastScan() + " ago");
    }
    
    /**
     * @brief Display recent threats in an impressive format
     * 
     * @details Shows the top 3 most critical open incidents sorted by risk score.
     * For each incident, displays severity icon, threat type, source/target IPs,
     * risk score, timestamp, and primary recommended action.
     * 
     * @example
     * displayRecentThreats();
     * // Output: 🔴[HIGH] Malware Detection
     * //         │ Source: 192.168.1.100 → Target: 10.0.0.5
     * //         │ Risk Score: 10.0/10 | Time: 15:30:45
     * //         │ Actions: Isolate affected system immediately
     */
    private static void displayRecentThreats() {
        System.out.println("📊 RECENT THREATS:");
        
        List<SecurityIncident> openIncidents = incidents.stream()
            .filter(incident -> incident.getStatus() == IncidentStatus.OPEN)
            .sorted((a, b) -> Double.compare(b.getRiskScore(), a.getRiskScore()))
            .limit(3)
            .toList();
            
        if (openIncidents.isEmpty()) {
            System.out.println("   No active threats detected");
            return;
        }
        
        for (SecurityIncident incident : openIncidents) {
            String severityIcon = getSeverityIcon(incident.getSeverity());
            String severityText = "[" + incident.getSeverity().toString().toUpperCase() + "]";
            
            System.out.println("   " + severityIcon + severityText + " " + incident.getThreatType().getDisplayName());
            System.out.println("   │ Source: " + incident.getSourceIP() + " → Target: " + incident.getTargetIP());
            System.out.println("   │ Risk Score: " + String.format("%.1f/10", incident.getRiskScore()) + 
                         " | Time: " + incident.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            
            List<String> actions = incident.getResponseActions();
            if (!actions.isEmpty()) {
                System.out.println("   │ Actions: " + actions.get(0));
            }
            System.out.println();
        }
    }
    
    /**
     * @brief Display current alerts
     * 
     * @details Shows any active system alerts generated by background monitoring.
     * Alerts are displayed in a simple list format. If no alerts are present,
     * nothing is displayed.
     * 
     * @example
     * displayAlerts();
     * // Output: 🚨 ALERTS:
     * //         Suspicious file activity detected
     * //         Network traffic anomaly detected
     */
    private static void displayAlerts() {
        if (alerts.isEmpty()) {
            return;
        }
        
        System.out.println("🚨 ALERTS:");
        for (String alert : alerts) {
            System.out.println("         " + alert);
        }
    }
    
    /**
     * @brief Display the main menu
     * 
     * @details Shows the available menu options with single-letter shortcuts.
     * The menu provides access to all major application functions.
     * 
     * @example
     * displayMenu();
     * // Output: Options: [R]eport Incident [A]nalyze [S]ecurity Report [H]elp [Q]uit
     * //         >
     */
    private static void displayMenu() {
        System.out.println("Options: [R]eport Incident [A]nalyze [S]ecurity Report [H]elp [Q]uit");
        System.out.print("> ");
    }
    
    /**
     * @brief Main application loop
     * 
     * @details Continuously reads user input and processes menu selections.
     * The loop handles all user interactions and maintains the application
     * state. After each operation, the welcome screen is redisplayed.
     * 
     * @example
     * runMainLoop();
     * // Application runs until user selects 'Q' to quit
     */
    private static void runMainLoop() {
        boolean running = true;
        
        while (running) {
            try {
                String input = scanner.nextLine().trim().toUpperCase();
                
                switch (input) {
                    case "R":
                        reportNewIncident();
                        break;
                    case "A":
                        analyzeIncidents();
                        break;
                    case "S":
                        generateSecurityReport();
                        break;
                    case "H":
                        showHelp();
                        break;
                    case "Q":
                        running = false;
                        System.out.println("Shutting down security console...");
                        break;
                    default:
                        System.out.println("Invalid option. Type 'H' for help.");
                        break;
                }
                
                if (running) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    displayWelcomeScreen();
                }
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * @brief Report a new security incident
     * 
     * @details Prompts the user for all required incident information and creates
     * a new SecurityIncident object. The method validates input and provides
     * clear prompts for each field. After creation, the incident details are
     * displayed and system metrics are updated.
     * 
     * @example
     * reportNewIncident();
     * // Prompts for: Incident ID, Source IP, Target IP, Threat Type, Severity, Description
     * // Creates incident and displays details
     */
    private static void reportNewIncident() {
        clearScreen();
        System.out.println("=== REPORT NEW SECURITY INCIDENT ===");
        System.out.println();
        
        try {
            System.out.print("Enter Incident ID: ");
            String incidentId = scanner.nextLine().trim();
            
            System.out.print("Enter Source IP: ");
            String sourceIP = scanner.nextLine().trim();
            
            System.out.print("Enter Target IP: ");
            String targetIP = scanner.nextLine().trim();
            
            System.out.println("\nSelect Threat Type:");
            ThreatType[] threatTypes = ThreatType.values();
            for (int i = 0; i < threatTypes.length; i++) {
                System.out.println((i + 1) + ". " + threatTypes[i].getDisplayName());
            }
            System.out.print("Enter choice (1-" + threatTypes.length + "): ");
            int threatChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            System.out.println("\nSelect Severity Level:");
            SeverityLevel[] severityLevels = SeverityLevel.values();
            for (int i = 0; i < severityLevels.length; i++) {
                System.out.println((i + 1) + ". " + severityLevels[i].getDisplayName());
            }
            System.out.print("Enter choice (1-" + severityLevels.length + "): ");
            int severityChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            System.out.print("Enter Description: ");
            String description = scanner.nextLine().trim();
            
            SecurityIncident newIncident = SecurityIncident.builder()
                .incidentId(incidentId)
                .sourceIP(sourceIP)
                .targetIP(targetIP)
                .threatType(threatTypes[threatChoice])
                .severity(severityLevels[severityChoice])
                .description(description)
                .build();
                
            incidents.add(newIncident);
            updateSystemMetrics();
            
            System.out.println("\n✅ Incident reported successfully!");
            newIncident.displayIncidentDetails();
            
        } catch (Exception e) {
            System.err.println("❌ Error creating incident: " + e.getMessage());
        }
    }
    
    /**
     * @brief Analyze existing incidents
     * 
     * @details Lists all incidents and allows the user to select one for detailed
     * analysis. The user can view incident details and optionally update the
     * incident status. The method supports selection by list number or incident ID.
     * 
     * @example
     * analyzeIncidents();
     * // Lists incidents: 1. INC-001 - Malware (Open) - Risk: 8.0/10
     * // User selects incident and can update status
     */
    private static void analyzeIncidents() {
        clearScreen();
        System.out.println("=== INCIDENT ANALYSIS ===");
        System.out.println();
        
        if (incidents.isEmpty()) {
            System.out.println("No incidents to analyze.");
            return;
        }
        
        System.out.println("Select incident to analyze:");
        for (int i = 0; i < incidents.size(); i++) {
            SecurityIncident incident = incidents.get(i);
            System.out.printf("%d. %s - %s (%s) - Risk: %.1f/10\n", 
                i + 1, incident.getIncidentId(), incident.getThreatType(), 
                incident.getStatus(), incident.getRiskScore());
        }
        
        System.out.print("\nEnter incident number or ID: ");
        String input = scanner.nextLine().trim();

        SecurityIncident selected = null;
        // Try by list number
        if (input.matches("\\d+")) {
            try {
                int choice = Integer.parseInt(input) - 1;
                if (choice >= 0 && choice < incidents.size()) {
                    selected = incidents.get(choice);
                }
            } catch (Exception ignored) { }
        }
        // Try by incident ID if not found by number
        if (selected == null) {
            for (SecurityIncident inc : incidents) {
                if (inc.getIncidentId().equalsIgnoreCase(input)) {
                    selected = inc;
                    break;
                }
            }
        }

        if (selected != null) {
            selected.displayIncidentDetails();

            // Allow status update
            System.out.println("Update status? (y/n): ");
            if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                updateIncidentStatus(selected);
            }
        } else {
            System.out.println("Incident not found. Please enter a valid number or ID.");
        }
    }
    
    /**
     * @brief Update incident status
     * 
     * @details Allows the user to change the status of a selected incident.
     * The method displays all available status options and validates the
     * selection. After updating, system metrics are recalculated.
     * 
     * @param incident The incident to update
     * 
     * @example
     * updateIncidentStatus(selectedIncident);
     * // Shows status options: 1. Open, 2. In Progress, 3. Escalated, 4. Resolved, 5. Closed
     * // User selects new status and system updates
     */
    private static void updateIncidentStatus(SecurityIncident incident) {
        System.out.println("\nSelect new status:");
        IncidentStatus[] statuses = IncidentStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
        }
        
        System.out.print("Enter choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice >= 0 && choice < statuses.length) {
                incident.updateIncidentStatus(statuses[choice]);
                updateSystemMetrics();
                System.out.println("✅ Status updated successfully!");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
    }
    
    /**
     * @brief Generate comprehensive security report
     * 
     * @details Creates a detailed security report including incident statistics,
     * threat type breakdown, severity breakdown, and high-risk incidents.
     * The report provides a comprehensive view of the current security posture.
     * 
     * @example
     * generateSecurityReport();
     * // Output: === SECURITY REPORT ===
     * //         Generated: 2024-01-15 14:30:25
     * //         📈 INCIDENT STATISTICS:
     * //         Total Incidents: 10
     * //         Open Incidents: 3
     * //         Resolved Incidents: 7
     * //         Resolution Rate: 70.0%
     */
    private static void generateSecurityReport() {
        clearScreen();
        System.out.println("=== SECURITY REPORT ===");
        System.out.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println();
        
        // Overall statistics
        long totalIncidents = incidents.size();
        long openIncidents = incidents.stream().filter(i -> i.getStatus() == IncidentStatus.OPEN).count();
        long resolvedIncidents = incidents.stream().filter(i -> i.getStatus() == IncidentStatus.RESOLVED).count();
        
        System.out.println("📈 INCIDENT STATISTICS:");
        System.out.println("   Total Incidents: " + totalIncidents);
        System.out.println("   Open Incidents: " + openIncidents);
        System.out.println("   Resolved Incidents: " + resolvedIncidents);
        System.out.println("   Resolution Rate: " + String.format("%.1f%%", (double) resolvedIncidents / totalIncidents * 100));
        System.out.println();
        
        // Threat type breakdown
        System.out.println("🎯 THREAT TYPE BREAKDOWN:");
        Map<ThreatType, Long> threatCounts = incidents.stream()
            .collect(java.util.stream.Collectors.groupingBy(SecurityIncident::getThreatType, java.util.stream.Collectors.counting()));
        
        for (ThreatType type : ThreatType.values()) {
            long count = threatCounts.getOrDefault(type, 0L);
            System.out.println("   " + type.getDisplayName() + ": " + count);
        }
        System.out.println();
        
        // Severity breakdown
        System.out.println("⚠️  SEVERITY BREAKDOWN:");
        Map<SeverityLevel, Long> severityCounts = incidents.stream()
            .collect(java.util.stream.Collectors.groupingBy(SecurityIncident::getSeverity, java.util.stream.Collectors.counting()));
        
        for (SeverityLevel level : SeverityLevel.values()) {
            long count = severityCounts.getOrDefault(level, 0L);
            System.out.println("   " + level.getDisplayName() + ": " + count);
        }
        System.out.println();
        
        // High-risk incidents
        System.out.println("🚨 HIGH-RISK INCIDENTS (Risk Score >= 8.0):");
        List<SecurityIncident> highRisk = incidents.stream()
            .filter(i -> i.getRiskScore() >= 8.0)
            .sorted((a, b) -> Double.compare(b.getRiskScore(), a.getRiskScore()))
            .toList();
            
        if (highRisk.isEmpty()) {
            System.out.println("   No high-risk incidents found.");
        } else {
            for (SecurityIncident incident : highRisk) {
                System.out.printf("   %s - %s - Risk: %.1f/10\n", 
                    incident.getIncidentId(), incident.getThreatType(), incident.getRiskScore());
            }
        }
    }
    
    /**
     * @brief Show help information
     * 
     * @details Displays comprehensive help information including available
     * commands, system features, and supported threat types. The help
     * system provides guidance for new users and command reference.
     * 
     * @example
     * showHelp();
     * // Output: === HELP - CYBERSECURITY INCIDENT RESPONSE CONSOLE ===
     * //         Available Commands:
     * //         R - Report Incident: Create a new security incident
     * //         A - Analyze: View and manage existing incidents
     * //         S - Security Report: Generate comprehensive security statistics
     * //         H - Help: Show this help information
     * //         Q - Quit: Exit the application
     */
    private static void showHelp() {
        clearScreen();
        System.out.println("=== HELP - CYBERSECURITY INCIDENT RESPONSE CONSOLE ===");
        System.out.println();
        System.out.println("Available Commands:");
        System.out.println("  R - Report Incident: Create a new security incident");
        System.out.println("  A - Analyze: View and manage existing incidents");
        System.out.println("  S - Security Report: Generate comprehensive security statistics");
        System.out.println("  H - Help: Show this help information");
        System.out.println("  Q - Quit: Exit the application");
        System.out.println();
        System.out.println("System Features:");
        System.out.println("  • Real-time threat monitoring");
        System.out.println("  • Automated risk scoring (0-10 scale)");
        System.out.println("  • Intelligent response suggestions");
        System.out.println("  • Comprehensive incident tracking");
        System.out.println("  • Security metrics and reporting");
        System.out.println();
        System.out.println("Threat Types Supported:");
        for (ThreatType type : ThreatType.values()) {
            System.out.println("  • " + type.getDisplayName());
        }
    }
    
    /**
     * @brief Start background monitoring tasks
     * 
     * @details Initializes background monitoring that runs every 30 seconds.
     * The monitoring updates system metrics and generates random alerts
     * for demonstration purposes.
     * 
     * @example
     * startBackgroundMonitoring();
     * // Background tasks start running automatically
     */
    private static void startBackgroundMonitoring() {
        // Update system metrics every 30 seconds
        scheduler.scheduleAtFixedRate(() -> {
            updateSystemMetrics();
            generateRandomAlerts();
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * @brief Update system metrics
     * 
     * @details Recalculates all system metrics based on current incident data.
     * Updates active incident count, resolved count, threat level, network
     * health, and last scan timestamp.
     * 
     * @example
     * updateSystemMetrics();
     * // Metrics are updated based on current incident state
     */
    private static void updateSystemMetrics() {
        activeIncidents = (int) incidents.stream()
            .filter(i -> i.getStatus() == IncidentStatus.OPEN)
            .count();
            
        resolvedToday = (int) incidents.stream()
            .filter(i -> i.getStatus() == IncidentStatus.RESOLVED)
            .count();
            
        overallThreatLevel = incidents.stream()
            .filter(i -> i.getStatus() == IncidentStatus.OPEN)
            .mapToDouble(SecurityIncident::getRiskScore)
            .average()
            .orElse(0.0);
            
        networkHealth = Math.max(50, 100 - (activeIncidents * 2));
        lastScan = LocalDateTime.now();
    }
    
    /**
     * @brief Generate random alerts for demonstration
     * 
     * @details Creates random security alerts for demonstration purposes.
     * Alerts are generated with a 30% probability and include various
     * types of security events. The method maintains a maximum of 3
     * active alerts.
     * 
     * @example
     * generateRandomAlerts();
     * // May add alerts like "Port scan detected from external IP"
     */
    private static void generateRandomAlerts() {
        if (random.nextInt(10) < 3) { // 30% chance
            String[] alertTypes = {
                "Port scan detected from external IP",
                "Unusual data transfer on port 443",
                "Failed authentication attempts increased",
                "Suspicious file activity detected",
                "Network traffic anomaly detected"
            };
            
            String alert = alertTypes[random.nextInt(alertTypes.length)];
            if (!alerts.contains(alert)) {
                alerts.add(alert);
                if (alerts.size() > 3) {
                    alerts.remove(0);
                }
            }
        }
    }
    
    // Utility methods
    /**
     * @brief Clear the console screen
     * 
     * @details Uses ANSI escape codes to clear the console screen and
     * reset the cursor position to the top-left corner.
     * 
     * @example
     * clearScreen();
     * // Console is cleared and cursor positioned at top
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    /**
     * @brief Get threat level string representation
     * 
     * @details Converts the numeric threat level to a human-readable string.
     * The conversion is based on predefined thresholds.
     * 
     * @return String representation of the threat level (LOW, MEDIUM, HIGH, CRITICAL)
     * 
     * @example
     * String level = getThreatLevelString(); // Returns "HIGH" for threat level 7.5
     */
    private static String getThreatLevelString() {
        if (overallThreatLevel >= 8.0) return "CRITICAL";
        if (overallThreatLevel >= 6.0) return "HIGH";
        if (overallThreatLevel >= 4.0) return "MEDIUM";
        return "LOW";
    }
    
    /**
     * @brief Get time since last scan in human-readable format
     * 
     * @details Calculates the time difference between now and the last scan
     * and formats it as a human-readable string with appropriate units.
     * 
     * @return Formatted time string (e.g., "30 seconds", "5 minutes", "2 hours")
     * 
     * @example
     * String time = getTimeSinceLastScan(); // Returns "45 seconds" or "2 minutes"
     */
    private static String getTimeSinceLastScan() {
        long seconds = java.time.Duration.between(lastScan, LocalDateTime.now()).getSeconds();
        if (seconds < 60) return seconds + " seconds";
        if (seconds < 3600) return (seconds / 60) + " minutes";
        return (seconds / 3600) + " hours";
    }
    
    /**
     * @brief Get severity icon for display
     * 
     * @details Returns an appropriate emoji icon based on the severity level
     * for use in console output formatting.
     * 
     * @param severity The severity level to get an icon for
     * @return Emoji string representing the severity level
     * 
     * @example
     * String icon = getSeverityIcon(SeverityLevel.CRITICAL); // Returns "🔴"
     */
    private static String getSeverityIcon(SeverityLevel severity) {
        switch (severity) {
            case CRITICAL: return "🔴";
            case HIGH: return "🟠";
            case MEDIUM: return "🟡";
            case LOW: return "🟢";
            default: return "⚪";
        }
    }
}
