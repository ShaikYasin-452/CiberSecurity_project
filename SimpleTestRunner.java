/**
 * @file SimpleTestRunner.java
 * @brief Basic test runner for SecurityIncident class without external dependencies
 * 
 * @details This class provides basic testing functionality without requiring JUnit dependencies.
 * It demonstrates testing concepts and validates the core functionality of the
 * cybersecurity incident response system. The test runner includes comprehensive
 * test cases covering all major aspects of the SecurityIncident class.
 * 
 * The SimpleTestRunner performs the following test categories:
 * - Valid incident creation and data integrity
 * - Input validation and error handling
 * - Risk score calculation accuracy
 * - Immediate response logic validation
 * - Response suggestion generation
 * - Status update functionality
 * - Builder pattern usage
 * - Encapsulation and data protection
 * 
 * Each test method includes detailed assertions and provides clear feedback
 * on test results. The runner maintains test statistics and provides a
 * comprehensive summary at the end.
 * 
 * @author Cybersecurity Team
 * @version 1.0
 * @date 2024
 * @see SecurityIncident
 * @see ThreatType
 * @see SeverityLevel
 * @see IncidentStatus
 */
public class SimpleTestRunner {
    
    /** Total number of tests executed */
    private static int totalTests = 0;
    /** Number of tests that passed successfully */
    private static int passedTests = 0;
    /** Number of tests that failed */
    private static int failedTests = 0;
    
    /**
     * @brief Main entry point for the test runner
     * 
     * @details Executes all test methods in sequence and displays comprehensive
     * test results. The method runs tests for all major functionality and
     * provides a summary of test outcomes including pass/fail statistics
     * and success rate.
     * 
     * @param args Command line arguments (not used)
     * 
     * @example
     * public static void main(String[] args) {
     *     // Runs all tests and displays results
     *     // Output includes test details and summary statistics
     * }
     */
    public static void main(String[] args) {
        System.out.println("🧪 RUNNING SECURITY INCIDENT TESTS");
        System.out.println("=====================================");
        
        // Run all test methods
        testValidIncidentCreation();
        testNullValidation();
        testRiskScoreCalculation();
        testImmediateResponseLogic();
        testResponseSuggestions();
        testStatusUpdates();
        testBuilderPattern();
        testEncapsulation();
        
        // Print test results
        printTestResults();
    }
    
    /**
     * @brief Test valid incident creation and data integrity
     * 
     * @details Verifies that SecurityIncident objects can be created successfully
     * with valid parameters and that all data is stored correctly. Tests
     * the constructor, getter methods, and initial state values.
     * 
     * @example
     * testValidIncidentCreation();
     * // Tests incident creation with valid parameters
     * // Verifies all fields are set correctly
     */
    private static void testValidIncidentCreation() {
        System.out.println("\n📋 Test: Valid Incident Creation");
        try {
            SecurityIncident incident = SecurityIncident.builder()
                .incidentId("TEST-001")
                .sourceIP("192.168.1.100")
                .targetIP("192.168.1.200")
                .threatType(ThreatType.MALWARE)
                .severity(SeverityLevel.HIGH)
                .description("Test malware incident")
                .build();
                
            assertNotNull(incident, "Incident should not be null");
            assertEquals("TEST-001", incident.getIncidentId(), "Incident ID should match");
            assertEquals("192.168.1.100", incident.getSourceIP(), "Source IP should match");
            assertEquals(ThreatType.MALWARE, incident.getThreatType(), "Threat type should match");
            assertEquals(SeverityLevel.HIGH, incident.getSeverity(), "Severity should match");
            assertEquals(IncidentStatus.OPEN, incident.getStatus(), "Status should be OPEN");
            
            System.out.println("✅ PASSED: Valid incident creation");
            passedTests++;
        } catch (Exception e) {
            System.out.println("❌ FAILED: Valid incident creation - " + e.getMessage());
            failedTests++;
        }
        totalTests++;
    }
    
    /**
     * @brief Test null parameter validation
     * 
     * @details Verifies that the SecurityIncident constructor properly validates
     * input parameters and throws appropriate exceptions for null or empty
     * values. Tests validation for incident ID, threat type, and other
     * required fields.
     * 
     * @example
     * testNullValidation();
     * // Tests that null parameters are properly rejected
     * // Verifies appropriate exception messages
     */
    private static void testNullValidation() {
        System.out.println("\n📋 Test: Null Parameter Validation");
        
        // Test null incident ID
        try {
            SecurityIncident.builder()
                .incidentId(null)
                .sourceIP("192.168.1.100")
                .targetIP("192.168.1.200")
                .threatType(ThreatType.MALWARE)
                .severity(SeverityLevel.HIGH)
                .description("Test incident")
                .build();
            System.out.println("❌ FAILED: Should throw exception for null incident ID");
            failedTests++;
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: Null incident ID validation");
            passedTests++;
        }
        totalTests++;
        
        // Test null threat type
        try {
            SecurityIncident.builder()
                .incidentId("TEST-002")
                .sourceIP("192.168.1.100")
                .targetIP("192.168.1.200")
                .threatType(null)
                .severity(SeverityLevel.HIGH)
                .description("Test incident")
                .build();
            System.out.println("❌ FAILED: Should throw exception for null threat type");
            failedTests++;
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSED: Null threat type validation");
            passedTests++;
        }
        totalTests++;
    }
    
    /**
     * @brief Test risk score calculation accuracy
     * 
     * @details Verifies that risk scores are calculated correctly based on
     * threat type and severity combinations. Tests multiple scenarios to
     * ensure the calculation algorithm works as expected and produces
     * scores within the valid range (0.0 to 10.0).
     * 
     * @example
     * testRiskScoreCalculation();
     * // Tests risk score calculation for different threat/severity combinations
     * // Verifies scores are within valid range and calculated correctly
     */
    private static void testRiskScoreCalculation() {
        System.out.println("\n📋 Test: Risk Score Calculation");
        
        // Test malware with high severity
        SecurityIncident malwareIncident = SecurityIncident.builder()
            .incidentId("MALWARE-001")
            .sourceIP("192.168.1.100")
            .targetIP("192.168.1.200")
            .threatType(ThreatType.MALWARE)
            .severity(SeverityLevel.HIGH)
            .description("Malware detection")
            .build();
            
        double malwareRisk = malwareIncident.getRiskScore();
        assertTrue(malwareRisk >= 0.0 && malwareRisk <= 10.0, "Risk score should be between 0-10");
        System.out.println("✅ PASSED: Malware risk score calculation: " + malwareRisk);
        passedTests++;
        totalTests++;
        
        // Test unauthorized access with critical severity
        SecurityIncident accessIncident = SecurityIncident.builder()
            .incidentId("ACCESS-001")
            .sourceIP("45.123.67.89")
            .targetIP("192.168.1.100")
            .threatType(ThreatType.UNAUTHORIZED_ACCESS)
            .severity(SeverityLevel.CRITICAL)
            .description("Unauthorized access attempt")
            .build();
            
        double accessRisk = accessIncident.getRiskScore();
        assertTrue(accessRisk >= 0.0 && accessRisk <= 10.0, "Risk score should be between 0-10");
        System.out.println("✅ PASSED: Unauthorized access risk score calculation: " + accessRisk);
        passedTests++;
        totalTests++;
    }
    
    /**
     * @brief Test immediate response logic validation
     * 
     * @details Verifies that the immediate response flag is set correctly
     * based on severity level, risk score, and threat type. Tests various
     * scenarios to ensure the logic correctly identifies incidents that
     * require immediate attention.
     * 
     * @example
     * testImmediateResponseLogic();
     * // Tests immediate response logic for different incident types
     * // Verifies critical incidents and high-risk threats are flagged correctly
     */
    private static void testImmediateResponseLogic() {
        System.out.println("\n📋 Test: Immediate Response Logic");
        
        // Test critical incident requires immediate response
        SecurityIncident criticalIncident = SecurityIncident.builder()
            .incidentId("CRITICAL-001")
            .sourceIP("192.168.1.100")
            .targetIP("192.168.1.200")
            .threatType(ThreatType.PORT_SCAN)
            .severity(SeverityLevel.CRITICAL)
            .description("Critical port scan")
            .build();
            
        assertTrue(criticalIncident.isRequiresImmediateAction(), "Critical incidents should require immediate response");
        System.out.println("✅ PASSED: Critical incident requires immediate response");
        passedTests++;
        totalTests++;
        
        // Test data breach requires immediate response
        SecurityIncident dataBreachIncident = SecurityIncident.builder()
            .incidentId("BREACH-001")
            .sourceIP("192.168.1.100")
            .targetIP("192.168.1.200")
            .threatType(ThreatType.DATA_BREACH)
            .severity(SeverityLevel.MEDIUM)
            .description("Data breach incident")
            .build();
            
        assertTrue(dataBreachIncident.isRequiresImmediateAction(), "Data breaches should require immediate response");
        System.out.println("✅ PASSED: Data breach requires immediate response");
        passedTests++;
        totalTests++;
    }
    
    /**
     * @brief Test response suggestions generation
     * 
     * @details Verifies that appropriate response suggestions are generated
     * for different threat types. Tests that the suggestions list is not
     * empty and contains relevant actions for the specific threat type.
     * 
     * @example
     * testResponseSuggestions();
     * // Tests response suggestion generation for malware incidents
     * // Verifies suggestions are relevant and non-empty
     */
    private static void testResponseSuggestions() {
        System.out.println("\n📋 Test: Response Suggestions");
        
        SecurityIncident malwareIncident = SecurityIncident.builder()
            .incidentId("MALWARE-002")
            .sourceIP("192.168.1.100")
            .targetIP("192.168.1.200")
            .threatType(ThreatType.MALWARE)
            .severity(SeverityLevel.HIGH)
            .description("Malware detection")
            .build();
            
        var suggestions = malwareIncident.getResponseActions();
        assertNotNull(suggestions, "Response suggestions should not be null");
        assertTrue(suggestions.size() > 0, "Should have at least one suggestion");
        assertTrue(suggestions.contains("Isolate affected system immediately"), "Should contain isolation suggestion");
        
        System.out.println("✅ PASSED: Malware response suggestions generated");
        System.out.println("   Suggestions: " + suggestions.size() + " items");
        passedTests++;
        totalTests++;
    }
    
    /**
     * @brief Test status updates functionality
     * 
     * @details Verifies that incident status can be updated correctly and
     * that status transitions work as expected. Tests the status update
     * method and ensures the status field is properly modified.
     * 
     * @example
     * testStatusUpdates();
     * // Tests status update functionality
     * // Verifies status transitions work correctly
     */
    private static void testStatusUpdates() {
        System.out.println("\n📋 Test: Status Updates");
        
        SecurityIncident incident = SecurityIncident.builder()
            .incidentId("STATUS-001")
            .sourceIP("192.168.1.100")
            .targetIP("192.168.1.200")
            .threatType(ThreatType.SUSPICIOUS_ACTIVITY)
            .severity(SeverityLevel.MEDIUM)
            .description("Status test incident")
            .build();
            
        assertEquals(IncidentStatus.OPEN, incident.getStatus(), "Initial status should be OPEN");
        
        incident.updateIncidentStatus(IncidentStatus.IN_PROGRESS);
        assertEquals(IncidentStatus.IN_PROGRESS, incident.getStatus(), "Status should update to IN_PROGRESS");
        
        incident.updateIncidentStatus(IncidentStatus.RESOLVED);
        assertEquals(IncidentStatus.RESOLVED, incident.getStatus(), "Status should update to RESOLVED");
        
        System.out.println("✅ PASSED: Status updates work correctly");
        passedTests++;
        totalTests++;
    }
    
    /**
     * @brief Test builder pattern functionality
     * 
     * @details Verifies that the builder pattern works correctly for creating
     * SecurityIncident objects. Tests that all builder methods function
     * properly and that the built object contains the expected values.
     * 
     * @example
     * testBuilderPattern();
     * // Tests builder pattern usage
     * // Verifies fluent interface works correctly
     */
    private static void testBuilderPattern() {
        System.out.println("\n📋 Test: Builder Pattern");
        
        SecurityIncident builtIncident = SecurityIncident.builder()
            .incidentId("BUILDER-001")
            .sourceIP("10.0.0.1")
            .targetIP("10.0.0.2")
            .threatType(ThreatType.PHISHING)
            .severity(SeverityLevel.MEDIUM)
            .description("Phishing attempt")
            .build();
            
        assertNotNull(builtIncident, "Built incident should not be null");
        assertEquals("BUILDER-001", builtIncident.getIncidentId(), "Builder should set incident ID correctly");
        assertEquals(ThreatType.PHISHING, builtIncident.getThreatType(), "Builder should set threat type correctly");
        
        System.out.println("✅ PASSED: Builder pattern works correctly");
        passedTests++;
        totalTests++;
    }
    
    /**
     * @brief Test encapsulation and data protection
     * 
     * @details Verifies that the SecurityIncident class properly encapsulates
     * its data and prevents external modification of internal collections.
     * Tests that returned lists are defensive copies and cannot be
     * modified to affect the original object.
     * 
     * @example
     * testEncapsulation();
     * // Tests data encapsulation
     * // Verifies defensive copying prevents external modification
     */
    private static void testEncapsulation() {
        System.out.println("\n📋 Test: Encapsulation");
        
        SecurityIncident incident = SecurityIncident.builder()
            .incidentId("ENCAP-001")
            .sourceIP("192.168.1.100")
            .targetIP("192.168.1.200")
            .threatType(ThreatType.MALWARE)
            .severity(SeverityLevel.HIGH)
            .description("Encapsulation test")
            .build();
            
        var originalActions = incident.getResponseActions();
        assertNotNull(originalActions, "Response actions should not be null");
        
        // Try to modify the returned list (should not affect the original)
        originalActions.add("Test action");
        
        var newActions = incident.getResponseActions();
        assertNotEquals(originalActions.size(), newActions.size(), "Encapsulation should prevent external modification");
        
        System.out.println("✅ PASSED: Proper encapsulation maintained");
        passedTests++;
        totalTests++;
    }
    
    /**
     * @brief Print comprehensive test results summary
     * 
     * @details Displays a formatted summary of all test results including
     * total tests run, number of passes and failures, success rate,
     * and overall test status. Provides clear feedback on the health
     * of the codebase.
     * 
     * @example
     * printTestResults();
     * // Output: ==================================================
     * //         📊 TEST RESULTS SUMMARY
     * //         ==================================================
     * //         Total Tests: 8
     * //         Passed: 8 ✅
     * //         Failed: 0 ❌
     * //         Success Rate: 100.0%
     * //         🎉 ALL TESTS PASSED! System is working correctly.
     */
    private static void printTestResults() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 TEST RESULTS SUMMARY");
        System.out.println("=".repeat(50));
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests + " ✅");
        System.out.println("Failed: " + failedTests + " ❌");
        System.out.println("Success Rate: " + String.format("%.1f%%", (double) passedTests / totalTests * 100));
        
        if (failedTests == 0) {
            System.out.println("\n🎉 ALL TESTS PASSED! System is working correctly.");
        } else {
            System.out.println("\n⚠️  Some tests failed. Please review the implementation.");
        }
    }
    
    // Simple assertion methods
    /**
     * @brief Assert that an object is not null
     * 
     * @param obj Object to check for null
     * @param message Error message to display if assertion fails
     * @throws AssertionError if the object is null
     */
    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }
    
    /**
     * @brief Assert that two objects are equal
     * 
     * @param expected Expected value
     * @param actual Actual value to compare
     * @param message Error message to display if assertion fails
     * @throws AssertionError if the objects are not equal
     */
    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }
    
    /**
     * @brief Assert that a condition is true
     * 
     * @param condition Boolean condition to check
     * @param message Error message to display if assertion fails
     * @throws AssertionError if the condition is false
     */
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    /**
     * @brief Assert that two objects are not equal
     * 
     * @param expected Expected value
     * @param actual Actual value to compare
     * @param message Error message to display if assertion fails
     * @throws AssertionError if the objects are equal
     */
    private static void assertNotEquals(Object expected, Object actual, String message) {
        if (expected.equals(actual)) {
            throw new AssertionError(message + " - Values should not be equal: " + expected);
        }
    }
}
