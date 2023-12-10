import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class FasttagScan {

	public static void main(String[] args) {

		try {
			
			FileInputStream fis = new FileInputStream(args[0]);
			Scanner sc = new Scanner(fis); 

			CalculateToll newToll = new CalculateToll();

	
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] inputArray = line.split(" ");

				if (inputArray[0].equalsIgnoreCase("FASTAG"))
					newToll.setFastag(inputArray[1], Integer.parseInt(inputArray[2]));

				else if (inputArray[0].equalsIgnoreCase("COLLECT_TOLL"))
					newToll.setCollectToll(inputArray[1], inputArray[2]);

				else {
					newToll.setPrintCollection();
				}
			}
			sc.close(); 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
