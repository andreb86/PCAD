package carPark;

import java.security.InvalidParameterException;

public class Main {

	public static void main(String[] args) throws InterruptedException {
        int gates = 0, cars = 0, slots = 0;
        String errorString = "Usage:\n" +
                "-g    Number of gates\n" +
                "-c    Number of cars\n" +
                "-p    Number of slots\n";
		try {
			
			// Check if the correct input has been given.
		    if (args.length != 6) {
				throw new InvalidParameterException(errorString);
			}
            
		    // Assign the input received from command line
		    for (int i = 0; i < args.length; i++) {
                switch (args[i++]) {
                    case "-g": gates =  Integer.parseInt(args[i]); System.out.println("Gates:" + gates);break;
                    case "-c": cars = Integer.parseInt(args[i]); System.out.println("Cars:" + cars); break;
                    case "-p": slots = Integer.parseInt(args[i]); System.out.println("Slots:" + slots); break;
                    default: throw new InvalidParameterException(String.format("Invalid parameter %s: ", args[i]));
                }
            }
            
            // Construct the parkMonitor
		    parkMonitor mon = new parkMonitor(slots, gates);
            
		    // Start the car park simulation
		    for (int j = 0; j < cars; j++) {
		    	(new Thread(new Car(j))).start();
		    }
            
		} catch (InvalidParameterException ipe) {
            System.err.println("Wrong parameters\n" + ipe.getMessage());
        } catch (NumberFormatException nfe) {
            System.err.println("Only integer arguments accepted: " + nfe.getMessage());
        }
	}

}
