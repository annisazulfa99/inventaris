// ================================================================
// File: src/main/java/com/inventaris/Main.java
// UPDATED: Added auto-refresh cleanup on scene change
// ================================================================
package com.inventaris;

import com.inventaris.config.DatabaseConfig;
import com.inventaris.controller.DashboardController;
import com.inventaris.controller.PeminjamanController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main Application Class
 * Entry point untuk aplikasi Inventaris Barang
 * UPDATED: Added cleanup for auto-refresh timelines
 */
public class Main extends Application {
    
    private static Stage primaryStage;
    private static Scene currentScene;
    private static Object currentController; // Store current controller for cleanup
    
    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            
            // Test database connection
            System.out.println("=================================");
            System.out.println("  SIMAK - INVENTARIS BARANG");
            System.out.println("  SISTEM MANAJEMEN INVENTARIS");
            System.out.println("=================================");
            
            DatabaseConfig dbConfig = DatabaseConfig.getInstance();
            if (dbConfig.testConnection()) {
                System.out.println("✅ Database connection successful");
            } else {
                System.err.println("❌ Database connection failed!");
                showErrorAndExit("Database connection failed!\nPlease check your database configuration.");
                return;
            }
            
            // Load Login Screen
            showLoginScreen();
            
            // Stage configuration
            primaryStage.setTitle("SIMAK - Inventaris Barang");
            primaryStage.setResizable(false);
            
            // Optional: Set application icon
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            } catch (Exception e) {
                System.out.println("⚠️  App icon not found, using default");
            }
            
            // Handle window close event
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("🔴 Application closing...");
                cleanupCurrentController();
            });
            
            primaryStage.show();
            
            System.out.println("✅ Application started successfully");
            System.out.println("=================================\n");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to start application");
            e.printStackTrace();
            showErrorAndExit("Failed to start application!\n" + e.getMessage());
        }
    }
    
    /**
     * Show login screen
     */
    public static void showLoginScreen() {
    try {
        System.out.println("🔄 Loading Login screen...");
        
        // Cleanup previous controller
        cleanupCurrentController();
        
        System.out.println("🔍 Looking for Login.fxml...");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/Login.fxml"));
        
        System.out.println("📂 Loading FXML file...");
        Parent root = loader.load();
        
        System.out.println("🎨 Creating scene...");
        Scene scene = new Scene(root);
        
        System.out.println("🎨 Loading CSS...");
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        
        currentScene = scene;
        currentController = loader.getController();
        
        System.out.println("🖥️  Setting scene to stage...");
        primaryStage.setScene(scene);
        primaryStage.setTitle("SIMAK - Login");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        
        System.out.println("✅ Login screen loaded successfully!");
        
    } catch (Exception e) {
        System.err.println("❌ CRITICAL ERROR loading Login screen:");
        System.err.println("   Error type: " + e.getClass().getName());
        System.err.println("   Error message: " + e.getMessage());
        e.printStackTrace();
        
        // Show error dialog
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Error");
        alert.setHeaderText("Failed to Load Login Screen");
        alert.setContentText("Error: " + e.getMessage() + "\n\nCheck console for details.");
        alert.showAndWait();
    }
}
    
    /**
     * Show dashboard screen
     */
    public static void showDashboard() {
        try {
            // Cleanup previous controller
            cleanupCurrentController();
            
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 1200, 700);
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
            
            currentScene = scene;
            currentController = loader.getController(); // Store controller
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("SIMAK - Dashboard");
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            
            System.out.println("📊 Dashboard loaded");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading Dashboard screen");
            e.printStackTrace();
        }
    }
    
 public static void showScreenWithParam(String fxmlFile, String title, double width, double height, String param) {
    try {
        cleanupCurrentController();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/" + fxmlFile));
        Parent root = loader.load();

        Object controller = loader.getController();

        // cek apakah controller mempunyai method receiveSearchKeyword
        try {
            controller.getClass().getMethod("receiveSearchKeyword", String.class)
                    .invoke(controller, param);
        } catch (NoSuchMethodException e) {
            System.out.println("Controller tidak punya method receiveSearchKeyword");
        }

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());

        currentScene = scene;
        currentController = controller;

        primaryStage.setScene(scene);
        primaryStage.setTitle("SIMAK - " + title);
        primaryStage.centerOnScreen();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    /**
     * Show any screen with custom size
     */
    public static void showScreen(String fxmlFile, String title, double width, double height) {
        try {
            // Cleanup previous controller
            cleanupCurrentController();
            
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
            
            currentScene = scene;
            currentController = loader.getController(); // Store controller
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("SIMAK - " + title);
            primaryStage.centerOnScreen();
            
            System.out.println("📄 Screen loaded: " + fxmlFile);
            
        } catch (Exception e) {
            System.err.println("❌ Error loading screen: " + fxmlFile);
            e.printStackTrace();
        }
    }
    
    /**
     * Cleanup current controller (stop auto-refresh timelines)
     * CRITICAL: Prevents memory leaks from Timeline threads
     */
    private static void cleanupCurrentController() {
        if (currentController != null) {
            try {
                // Stop auto-refresh in DashboardController
                if (currentController instanceof DashboardController) {
                    ((DashboardController) currentController).stopAutoRefresh();
                    System.out.println("🛑 DashboardController auto-refresh stopped");
                }
                
                // Stop auto-refresh in PeminjamanController
                if (currentController instanceof PeminjamanController) {
                    ((PeminjamanController) currentController).stopAutoRefresh();
                    System.out.println("🛑 PeminjamanController auto-refresh stopped");
                }
                
                // Add more controllers here if they have auto-refresh
                
            } catch (Exception e) {
                System.err.println("⚠️  Error cleaning up controller: " + e.getMessage());
            }
            
            currentController = null;
        }
    }
    
    /**
     * Get primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Get current scene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }
    
    /**
     * Get current controller
     */
    public static Object getCurrentController() {
        return currentController;
    }
    
    /**
     * Show error alert and exit
     */
    private void showErrorAndExit(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Error");
        alert.setHeaderText("Application Error");
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(1);
    }
    
    public static void loadContent(String fxmlName) {
    try {
        // Cleanup previous controller BEFORE loading new one
        cleanupCurrentController();
        
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/" + fxmlName));
        Parent content = loader.load();

        // Check if current scene root is BorderPane
        if (currentScene != null && currentScene.getRoot() instanceof BorderPane) {
            BorderPane root = (BorderPane) currentScene.getRoot();
            root.setCenter(content);
            System.out.println("📄 Content loaded into BorderPane: " + fxmlName);
        } else {
            // If not BorderPane, create new scene (for Dashboard case)
            Scene scene = new Scene(content, 1200, 700);
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
            
            currentScene = scene;
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            System.out.println("📄 New scene loaded: " + fxmlName);
        }

        currentController = loader.getController();

    } catch (Exception e) {
        System.err.println("❌ Error loading content: " + fxmlName);
        e.printStackTrace();
    }
}

    /**
     * Application shutdown hook
     */
    @Override
    public void stop() {
        System.out.println("\n=================================");
        System.out.println("  APPLICATION SHUTTING DOWN");
        System.out.println("=================================");
        
        // Cleanup all resources
        cleanupCurrentController();
        
        System.out.println("✅ Cleanup completed");
        System.out.println("👋 Goodbye!");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}