package services;

import model.Car;
import model.Motorcycle;
import model.Van;
import model.Client;
import model.LoyaltyTier;
import model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuggestionEngine {

    private Random random = new Random();

    public Vehicle recommendVehicle(Client client) {
        System.out.println("Analyzing client's rental history...");
    
        List<Vehicle> vehicleOptions = new ArrayList<>();
    
        if (client.getLoyaltyTier() == LoyaltyTier.GOLD) {
            vehicleOptions.add(new Car("V002", "Tesla", "Model S", "ECO123", "Electric", 5));
            vehicleOptions.add(new Car("V005", "Audi", "A8", "AUD456", "Hybrid", 4));
            vehicleOptions.add(new Van("V006", "Mercedes", "Sprinter", "SPR789", 10.0, 2));
            vehicleOptions.add(new Motorcycle("M001", "Harley Davidson", "Street 750", "HD750", 750, false));
        } else if (client.getLoyaltyTier() == LoyaltyTier.SILVER) {
            vehicleOptions.add(new Car("V003", "BMW", "3 Series", "BMW789", "Petrol", 4));
            vehicleOptions.add(new Car("V007", "Lexus", "IS", "LEX123", "Hybrid", 4));
            vehicleOptions.add(new Van("V008", "Ford", "Transit", "FOR456", 12.0, 3));
            vehicleOptions.add(new Motorcycle("M002", "Ducati", "Panigale V2", "DUC123", 955, false));
        } else {
            vehicleOptions.add(new Car("V004", "Honda", "Civic", "HON456", "Petrol", 4));
            vehicleOptions.add(new Car("V009", "Toyota", "Camry", "TOY123", "Hybrid", 4));
            vehicleOptions.add(new Van("V010", "Nissan", "NV350", "NISS123", 8.0, 2));
            vehicleOptions.add(new Motorcycle("M003", "Yamaha", "MT-07", "YAM789", 689, false));
        }
    
        System.out.println("Available vehicles for suggestion:");
        for (Vehicle vehicle : vehicleOptions) {
            System.out.println(vehicle.getBrand() + " " + vehicle.getModel());
        }
    
        return vehicleOptions.get(random.nextInt(vehicleOptions.size()));
    }
}
