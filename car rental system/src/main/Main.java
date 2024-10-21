package main;

import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;
import model.Car;
import model.Motorcycle;
import model.Van;
import model.VIPClient;
import model.Vehicle;
import services.Rental;
import services.SuggestionEngine;
import services.VehicleHealthMonitor;
import services.CreditCardPayment;
import services.BankTransferPayment;

public class Main {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SuggestionEngine suggestionEngine = new SuggestionEngine();

        // Input the client information
        VIPClient client = getClientInformation(scanner);

        // Suggest vehicle
        System.out.println(ANSI_CYAN + "\n--- Vehicle Suggestion for VIP Client ---" + ANSI_RESET);
        Vehicle suggestedVehicle = suggestionEngine.recommendVehicle(client);
        System.out.println(ANSI_YELLOW + "Suggested Vehicle: " + suggestedVehicle.getModel() + suggestedVehicle.getBrand()  + ANSI_RESET);

        // Vehicle selection
        Vehicle chosenVehicle = chooseVehicle(scanner);

        // Input rental the duration and process dates
        int rentalDays = getRentalDuration(scanner);
        Date[] rentalDates = calculateRentalDates(rentalDays);

        // Process rental
        System.out.println(ANSI_CYAN + "\n--- Rental Process ---" + ANSI_RESET);
        Rental rental = new Rental("R001", chosenVehicle, client, rentalDates[0], rentalDates[1], false);
        rental.generateInvoice();

        // Payment process
        processPayment(scanner, rental);

        // Update loyalty points and will display loyalty tier
        updateAndDisplayLoyalty(client, rental);

        // Monitor the vehicle health
        monitorVehicleHealth(chosenVehicle);

        scanner.close();
    }

    // Get client information
    private static VIPClient getClientInformation(Scanner scanner) {
        System.out.print("Enter client ID: ");
        String clientId = scanner.nextLine();
        System.out.print("Enter client name: ");
        String clientName = scanner.nextLine();
        System.out.print("Enter client email: ");
        String clientEmail = scanner.nextLine();
        return new VIPClient(clientId, clientName, clientEmail);
    }

    // Vehicle selection 
    private static Vehicle chooseVehicle(Scanner scanner) {
        System.out.println("Choose vehicle type (1: Car, 2: Motorcycle, 3: Van): ");
        int vehicleTypeChoice = getValidChoice(scanner, 1, 3);

        Vehicle chosenVehicle = null;
        switch (vehicleTypeChoice) {
            case 1:
                chosenVehicle = chooseCar(scanner);
                break;
            case 2:
                chosenVehicle = chooseMotorcycle(scanner);
                break;
            case 3:
                chosenVehicle = chooseVan(scanner);
                break;
        }
        return chosenVehicle;
    }

    // Car selection 
    private static Vehicle chooseCar(Scanner scanner) {
        System.out.println("Choose a car model: ");
        System.out.println("1: Toyota Corolla");
        System.out.println("2: Honda Civic");
        System.out.println("3: Tesla Model 3");

        int carChoice = getValidChoice(scanner, 1, 3);
        switch (carChoice) {
            case 1:
                return new Car("V001", "Toyota", "Corolla", "ABC123", "Petrol", 4);
            case 2:
                return new Car("V002", "Honda", "Civic", "HDC456", "Petrol", 4);
            case 3:
                return new Car("V003", "Tesla", "Model 3", "TSL789", "Electric", 4);
            default:
                return null; 
        }
    }

    // Motorcycle 
    private static Vehicle chooseMotorcycle(Scanner scanner) {
        System.out.println("Choose a motorcycle model: ");
        System.out.println("1: Harley Davidson Street 750");
        System.out.println("2: Yamaha MT-07");
        System.out.println("3: Ducati Panigale V4");

        int motorcycleChoice = getValidChoice(scanner, 1, 3);
        switch (motorcycleChoice) {
            case 1:
                return new Motorcycle("M001", "Harley Davidson", "Street 750", "HD750", 750, false);
            case 2:
                return new Motorcycle("M002", "Yamaha", "MT-07", "YMT07", 689, true);
            case 3:
                return new Motorcycle("M003", "Ducati", "Panigale V4", "DCPV4", 1103, true);
            default:
                return null; 
        }
    }

    // Van
    private static Vehicle chooseVan(Scanner scanner) {
        System.out.println("Choose a van model: ");
        System.out.println("1: Mercedes Sprinter");
        System.out.println("2: Ford Transit");
        System.out.println("3: Volkswagen Transporter");

        int vanChoice = getValidChoice(scanner, 1, 3);
        switch (vanChoice) {
            case 1:
                return new Van("VAN001", "Mercedes", "Sprinter", "SPR456", 10.0, 2);
            case 2:
                return new Van("VAN002", "Ford", "Transit", "FTR789", 12.0, 3);
            case 3:
                return new Van("VAN003", "Volkswagen", "Transporter", "VW123", 9.5, 2);
            default:
                return null; 
        }
    }

    // rental duration 
    private static int getRentalDuration(Scanner scanner) {
        System.out.print("Enter rental duration in days: ");
        return getValidChoice(scanner, 1, 365);  
    }

    // Calculate rental start and end dates
    private static Date[] calculateRentalDates(int rentalDays) {
        Date rentalStartDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rentalStartDate);
        calendar.add(Calendar.DAY_OF_MONTH, rentalDays);
        Date rentalEndDate = calendar.getTime();
        return new Date[]{rentalStartDate, rentalEndDate};
    }

    // Process payment
    private static void processPayment(Scanner scanner, Rental rental) {
        System.out.println("Choose payment method (1: Credit Card, 2: Bank Transfer): ");
        int paymentChoice = getValidChoice(scanner, 1, 2);

        switch (paymentChoice) {
            case 1:
                rental.processPayment(new CreditCardPayment("P001", rental.applyDynamicPricing()));
                break;
            case 2:
                rental.processPayment(new BankTransferPayment("P002", rental.applyDynamicPricing()));
                break;
        }
    }

    // Update and display client loyalty points
    private static void updateAndDisplayLoyalty(VIPClient client, Rental rental) {
        client.accumulateLoyaltyPoints(rental.applyDynamicPricing());
        System.out.println(ANSI_GREEN + "Client's Loyalty Tier: " + client.getLoyaltyTier() + ANSI_RESET);
    }

    // Monitor vehicle health
    private static void monitorVehicleHealth(Vehicle chosenVehicle) {
        System.out.println(ANSI_CYAN + "\n--- IoT-Based Vehicle Health Monitoring ---" + ANSI_RESET);
        VehicleHealthMonitor monitor = new VehicleHealthMonitor(chosenVehicle);
        monitor.checkVehicleHealth();
        monitor.sendRealTimeAlert("Vehicle needs attention!");
    }

    private static int getValidChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println(ANSI_RED + "Invalid choice! Please enter a number between " + min + " and " + max + "." + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_RED + "Invalid input! Please enter a valid number." + ANSI_RESET);
                scanner.next(); 
            }
        }
        return choice;
    }
}
