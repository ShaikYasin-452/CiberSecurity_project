@echo off
REM ========================================
REM    CYBERSECURITY INCIDENT RESPONSE
REM    CONSOLE - COMPILATION & RUN SCRIPT
REM ========================================
REM
REM @file run.bat
REM @brief Automated build and execution script for Cybersecurity Incident Response Console
REM
REM @details This batch script provides a complete automation solution for compiling,
REM testing, and running the Cybersecurity Incident Response Console application.
REM The script performs a four-stage process to ensure the application is properly
REM built and tested before execution.
REM
REM The script executes the following stages:
REM 1. Compilation: Compiles all Java source files in the current directory
REM 2. Testing: Runs the SimpleTestRunner to validate core functionality
REM 3. Application Launch: Starts the main SecurityConsole application
REM 4. Cleanup: Provides user feedback and waits for user input before exit
REM
REM @author Cybersecurity Team
REM @version 1.0
REM @date 2024
REM
REM @usage Double-click run.bat or execute from command line
REM @prerequisites Java Development Kit (JDK) must be installed and in PATH
REM @output Console output showing compilation status, test results, and application interface
REM
REM @example
REM     run.bat
REM     REM Output: ========================================
REM     REM         CYBERSECURITY INCIDENT RESPONSE
REM     REM         CONSOLE - COMPILATION & RUN SCRIPT
REM     REM         ========================================
REM     REM         [1/4] Compiling Java files...
REM     REM         ✅ Compilation successful!
REM     REM         [2/4] Running tests...
REM     REM         🧪 RUNNING SECURITY INCIDENT TESTS
REM     REM         =====================================
REM     REM         [3/4] Starting main application...
REM     REM         🚀 Launching Cybersecurity Incident Response Console...
REM     REM         [4/4] Application finished.
REM
echo ========================================
echo    CYBERSECURITY INCIDENT RESPONSE
echo    CONSOLE - COMPILATION & RUN SCRIPT
echo ========================================
echo.

echo [1/4] Compiling Java files...
REM @brief Compile all Java source files in the current directory
REM @details Uses javac to compile all .java files. Checks for compilation errors
REM and provides clear feedback on success or failure. If compilation fails,
REM the script pauses and exits with error code 1.
javac *.java
if %errorlevel% neq 0 (
    echo ❌ Compilation failed! Please check your Java installation.
    pause
    exit /b 1
)
echo ✅ Compilation successful!

echo.
echo [2/4] Running tests...
REM @brief Execute the SimpleTestRunner to validate application functionality
REM @details Runs comprehensive tests to ensure all core features work correctly
REM before launching the main application. Tests include incident creation,
REM validation, risk scoring, and response generation.
java SimpleTestRunner
echo.

echo [3/4] Starting main application...
echo.
echo 🚀 Launching Cybersecurity Incident Response Console...
echo.
REM @brief Launch the main SecurityConsole application
REM @details Starts the interactive console application that provides the
REM user interface for incident management, reporting, and analysis.
REM The application runs until the user chooses to quit.
java SecurityConsole

echo.
echo [4/4] Application finished.
echo.
REM @brief Provide user feedback and wait for input before exit
REM @details Pauses execution to allow the user to review any output
REM or error messages before the console window closes.
pause


