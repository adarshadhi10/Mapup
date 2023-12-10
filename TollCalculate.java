import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class CalculateToll {

	
	private int totalCollection;
	private int totalDiscount;
	private int totalFastTagCollection;
	private int totalCashCollection;
	private int flatFee;

	private Map<String, FastTag> vehicleMap;
	private Map<String, Integer> vehicleAmountMap;
	private Map<String, Integer> vehicleSummaryMapAmount;
	private Map<String, Integer> vehicleSummaryMapFrequency;

	private Toll toll;

	public CalculateToll() {
	
		totalCollection = 0;
		totalDiscount = 0;
		totalFastTagCollection = 0;
		totalCashCollection = 0;
		flatFee = 40;

		vehicleMap = new HashMap<>();
		vehicleAmountMap = new HashMap<>();
		vehicleSummaryMapAmount = new HashMap<>();
		vehicleSummaryMapFrequency = new HashMap<>();

	
		toll = new Toll();

	}

	public void setFastag(String vehicleNumber, Integer amount) {
		vehicleMap.put(vehicleNumber, new FastTag(vehicleNumber, amount));
		vehicleAmountMap.put(vehicleNumber, amount);
	}


	public void setVehicleType(String vehicleType) {

		if (vehicleSummaryMapFrequency.containsKey(vehicleType))
			vehicleSummaryMapFrequency.put(vehicleType, vehicleSummaryMapFrequency.get(vehicleType) + 1);
		else
			vehicleSummaryMapFrequency.put(vehicleType, 1);

	}

	public void setCollectToll(String vehicleType, String vehicleNumber) {

		setVehicleType(vehicleType);

	
		if (vehicleMap.containsKey(vehicleNumber)) {

			int vehicleAmount = vehicleAmountMap.get(vehicleNumber);
			int tollAmount = toll.getAmount(vehicleType);

			if (vehicleAmount >= tollAmount) {
				vehicleAmountMap.put(vehicleNumber, vehicleAmount - tollAmount);
				totalCollection += tollAmount;
				totalFastTagCollection += tollAmount;

				setVehicleSummary(vehicleType, tollAmount);

				setReturnVehicle(vehicleNumber, tollAmount);

			} else {
				setCalAmount(vehicleNumber, tollAmount, vehicleAmount, vehicleType);
			}
		} else {

			Integer tollAmount = toll.getAmount(vehicleType);

			totalCashCollection += tollAmount + flatFee;
			totalCollection += (tollAmount + flatFee);

			setVehicleSummary(vehicleType, tollAmount + flatFee);
		}
	}

	public void setReturnVehicle(String vehicleNumber, Integer tollAmount) {
		if (vehicleAmountMap.containsKey(vehicleNumber + "_RETURN")) {
			totalDiscount += tollAmount / 2;
			totalCollection -= totalDiscount;
			vehicleAmountMap.remove(vehicleNumber + "_RETURN");
		} else {
			vehicleAmountMap.put(vehicleNumber + "_RETURN", null);
		}
	}


	public void setCalAmount(String vehicleNumber, Integer tollAmount, Integer vehicleAmount, String vehicleType) {

		if (vehicleAmountMap.containsKey(vehicleNumber + "_RETURN")) {

			setReturnDiscount(vehicleNumber, tollAmount, vehicleAmount, vehicleType);

		} else {
			vehicleAmountMap.put(vehicleNumber + "_RETURN", null);
			if (vehicleAmount > 0) {
				totalCashCollection += tollAmount - vehicleAmount + flatFee;
				totalFastTagCollection += vehicleAmount;
				vehicleAmountMap.put(vehicleNumber, 0);
				totalCollection += (tollAmount + flatFee);

				setVehicleSummary(vehicleType, tollAmount + flatFee);
			} else {
				totalCashCollection += tollAmount + flatFee;
				totalCollection += (tollAmount + flatFee);

				setVehicleSummary(vehicleType, tollAmount + flatFee);
			}

		}
	}


	public void setReturnDiscount(String vehicleNumber, Integer tollAmount, Integer vehicleAmount, String vehicleType) {

		totalDiscount += tollAmount / 2;
		vehicleAmountMap.remove(vehicleNumber + "_RETURN");

		if (vehicleAmount > 0) {

			setGreaterAmount(vehicleNumber, tollAmount, vehicleAmount, vehicleType);

		} else {
			totalCashCollection += tollAmount / 2 + flatFee;
			totalCollection += (tollAmount / 2 + flatFee);

			setVehicleSummary(vehicleType, tollAmount / 2 + flatFee);
		}
	}


	public void setGreaterAmount(String vehicleNumber, Integer tollAmount, Integer vehicleAmount, String vehicleType) {

		if (vehicleAmount >= tollAmount / 2) {
			vehicleAmountMap.put(vehicleNumber, vehicleAmount - tollAmount / 2);
			totalFastTagCollection += tollAmount / 2;
			totalCollection += tollAmount / 2;

			setVehicleSummary(vehicleType, tollAmount / 2);
		} else {

			totalFastTagCollection += vehicleAmount;
			int remainBalance = tollAmount / 2 - vehicleAmount;
			totalCashCollection += remainBalance + flatFee;
			vehicleAmountMap.put(vehicleNumber, 0);
			totalCollection += (tollAmount / 2 + flatFee);

			setVehicleSummary(vehicleType, tollAmount / 2 + flatFee);
		}

	}

	public void setVehicleSummary(String vehicleType, Integer amount) {

		if (vehicleSummaryMapAmount.containsKey(vehicleType))
			vehicleSummaryMapAmount.put(vehicleType, vehicleSummaryMapAmount.get(vehicleType) + amount);
		else
			vehicleSummaryMapAmount.put(vehicleType, amount);
	}

	public void setPrintCollection() {

		System.out.println("TOTAL_COLLECTION " + totalCollection + " " + totalDiscount);
		System.out.println("PAYMENT_SUMMARY " + (totalFastTagCollection) + " " + totalCashCollection);
		System.out.println("VEHICLE_TYPE_SUMMARY");

		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(
				vehicleSummaryMapAmount.entrySet());


		Collections.sort(list, new Sorting());

		HashMap<String, Integer> newHashMap = new LinkedHashMap<String, Integer>();

		for (Map.Entry<String, Integer> ele : list) {
			newHashMap.put(ele.getKey(), ele.getValue());
		}

		for (String key : newHashMap.keySet()) {
			System.out.println(key + " " + vehicleSummaryMapFrequency.get(key));
		}
	}

}
