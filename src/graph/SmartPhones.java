package graph;

import java.util.HashMap;
import java.util.Random;

/**
 * This class is used to represent the smart phones that will be used to
 * determine next generation owners of smart phones.
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class SmartPhones {

	// the array that will hold the smart phones
	private SmartPhone[] phones;


	/**
	 * The only constructor of this class.
	 * It will start by initialize the data and store a "N/A" phone in it.
	 */
	public SmartPhones() {
		this.phones = new SmartPhone[3];

		// initialize three phones at "N/A
		for (int i = 0; i < 3; i++) {
			this.phones[i] = new SmartPhone("N/A", 0, 0);
		}

		// map input and output ranges
		mapValueRanges();
	}

	/*
	 * The input and output ranges must no be discontinued.
	 * 0$ -> 2$ : 0% -> 5%
	 * 2$ -> 5$ : 5% -> 20%
	 * 5$ -> 10$ : 20% -> 50%
	 * 10$ -> +oo : 50% -> 100%
	 */
	private static final float[] inputValueRange = {0, 2, 2, 5, 5, 10, 10, 20};
	private static final float[] outputValueRange = {0, 5, 5, 20, 20, 50, 50, 100};
	private static HashMap<Integer, float[]> inputRangeMap;
	private static HashMap<Integer, float[]> outputRangeMap;


	/**
	 * This method will return a random phone name between the available ones.
	 * @return the name of a smart phone.
	 */
	public String getRandomPhone() {

		// initialize random
		Random random = new Random();

		return phones[random.nextInt(3)].getName();
	}


	/**
	 * This method will add a smart phone.
	 * @param name name of the added smart phone.
	 * @param price its price.
	 * @param numberFeatures its number of features.
	 */
	public void addSmartPhone(String name, float price, int numberFeatures) {

		// can't have the same name of phone
		if (phones[1].getName().equals(name) || phones[2].getName().equals(name)) {
			throw new IllegalArgumentException("Name of the phone already exists");
		}

		if ("N/A".equals(phones[1].getName())) {
			phones[1].setName(name);
			phones[1].setPrice(price);
			phones[1].setNumberFeatures(numberFeatures);
		}
		else if ("N/A".equals(phones[2].getName())) {
			phones[2].setName(name);
			phones[2].setPrice(price);
			phones[2].setNumberFeatures(numberFeatures);
		}
		else {
			throw new IndexOutOfBoundsException("Can't add another phone.\nOnly two phones are accepted.");
		}


	}

	/**
	 * This method will return the smart phone given an index.
	 * @param i the index must be between 0 and 2.
	 * 0 corresponds to the first phone -> "N/A"
	 * 2 corresponds to the last phone added.
	 * @return the smart phone.
	 */
	public SmartPhone getSmartPhone(int i) {

		if (i < 0 || i > 3)
			throw new IllegalArgumentException("Index of the phone out of bound.");

		return phones[i];
	}

	/**
	 * This method will return the smart phone given its name.
	 * @param name the name of the smart phone.
	 * @return the smart phone.
	 */
	public SmartPhone getSmartPhone(String name) {
		if (name.equals(phones[1])) {
			return phones[1];
		}
		else if (name.equals(phones[2])) {
			return phones[2];
		}

		// if the name of the phone does not exist in the array
		return null;
	}


	/**
	 * This method will return an array containing the names of all available 
	 * smart phones.
	 * @return an array containing the 3 names of the available phones.
	 */
	public String[] getNamesSmartPhone() {
		String[] phonesName = new String[3];

		for (int i = 0; i < 3; i++) {
			phonesName[i] = phones[i].getName();
		}

		return phonesName;
	}


	/**
	 * This method will calculate the ratio price/number of features.
	 *
	 * We will reason on the first given smart phone. 
	 * ie, if the first phone has less ratio than the second one, 
	 * the probability of the first phone will increase. 
	 * Reversely, the reasoning is true. 
	 * 
	 * @param phone1
	 * @param phone2
	 * @return the probability 
	 */
	public float ratioFeaturePrice(SmartPhone phone1, SmartPhone phone2) {

		// we can't compare a "N/A" phone
		if ("N/A".equals(phone1.getName()) || 
				"N/A".equals(phone2.getName())) {
			return 0;
		}		

		// get the price of single feature for both phones
		float priceSingFeat1 = phone1.getPrice() / phone1.getNumberFeatures();
		float priceSingFeat2 = phone2.getPrice() / phone2.getNumberFeatures();

		// calculate the difference
		float difference = priceSingFeat1 - priceSingFeat2;

		// if the difference is negative, then the current phone has 
		// less ratio than the specified phone.
		// Reversely, true.

		// check the sign of the difference
		boolean notNegative = false;
		if (difference < 0) {
			notNegative = true;
			difference = Math.abs(difference);
		}

		// if the difference is already bigger than the max range of input
		if (difference >= inputValueRange[inputValueRange.length - 1]) {

			float eightyPerc = 0.8f;

			if (!notNegative) {
				return -1 * eightyPerc;
			}
			else {
				return eightyPerc;
			}
		}


		// get the mapped value of difference
		int mapedDifference = getMapedInput(difference);



		// something went wrong
		if (mapedDifference == -1) {
			return -1;
		}

		// get the input and output range
		float input_start = inputRangeMap.get(mapedDifference)[0];
		float input_end = inputRangeMap.get(mapedDifference)[1];
		float output_start = outputRangeMap.get(mapedDifference)[0];
		float output_end = outputRangeMap.get(mapedDifference)[1];

		// calculate percentage
		float probPercentage = mapRangeIntoRange(
				input_start, input_end,
				output_start, output_end,
				difference) / 100;


		if (!notNegative) {
			return -1 * probPercentage;
		}

		// need to check 
		return probPercentage;
	}



	
	/**
	 * Given an input that's inside the input range, this function will calculate the output 
	 * that's inside the the output range. 
	 * @param input_start lower bound of input range.
	 * @param input_end higher bound of input range.
	 * @param output_start lower bound of output range.
	 * @param output_end higher bound of output range.
	 * @param input the input value.
	 * @return the output value.
	 */
	public static float mapRangeIntoRange(float input_start, float input_end, 
			float output_start, float output_end, 
			float input) {


		// test if the input is in range of the input values
		if (input > input_end || input < input_start) {
			throw new IllegalArgumentException("Input values must be in the range of input values");
		}


		// Transform the [input_start, input_end] -> [0, r]
		float r = input_end - input_start;

		// Transform the [output_start, output_end] -> [0, R]
		float R = output_end - output_start;

		// mapped input
		float mInput = input - input_start;

		// calculate slope
		float slope = R / r; 

		// calculate output
		float output = output_start + slope * mInput;

		// That's all folks
		return output;

	}



	/**
	 * This method determines the input and output range out of the specified difference.
	 * @param difference
	 * @return
	 */
	private static int getMapedInput(float difference) {

		// if the difference is lower than the input value range
		if (difference < inputValueRange[0])
			return -1;

		int goTo = 1;
		for (int i = 1; i < inputValueRange.length; i+=2) {
			if (difference > inputValueRange[i]) {
				goTo++;
			}
			else if (difference < inputValueRange[i]) {
				return goTo;
			}
		}


		return goTo - 1;
	}


	/**
	 * basically we need to calculate this only one time and not every call to ratio.
	 * So, we will call it in the constructor. Thus we need a Global maps to map the 
	 * set of values.
	 */ 
	private static void mapValueRanges() {

		// initialize map
		inputRangeMap = new HashMap<Integer, float[]>();
		outputRangeMap = new HashMap<Integer, float[]>();

		// iterate over the output range values and put them 2 by 2 in the map
		for (int i = 0, j = 1; i < outputValueRange.length; i += 2, j++) {
			float[] currentOutRange = {outputValueRange[i], outputValueRange[i + 1]};
			float[] currentInRange = {inputValueRange[i], inputValueRange[i + 1]};

			outputRangeMap.put(j, currentOutRange);
			inputRangeMap.put(j, currentInRange);
		}

	}
	
	
	// Getters
	
	public float[] getInputRange(int i) {

		// throws exception if the map does not contain key
		if (!inputRangeMap.containsKey(i)) {
			throw new IndexOutOfBoundsException("Map does not contain entered key");
		}



		return inputRangeMap.get(i);
	}

	public float[] getOutputRange(int i) {

		// throws exception if the map does not contain key
		if (!outputRangeMap.containsKey(i)) {
			throw new IndexOutOfBoundsException("Map does not contain entered key");
		}

		return outputRangeMap.get(i);
	}


	public class SmartPhone {

		private String name;
		private float price;
		private int numberFeatures;

		public SmartPhone(String name, float price, int numberFeatures) {
			this.name = name;
			this.price = price;
			this.numberFeatures = numberFeatures;
		}


		public String getName() {
			return this.name;
		}

		public float getPrice() {
			return this.price;
		}

		public int getNumberFeatures() {
			return this.numberFeatures;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public void setNumberFeatures(int numberFeatures) {
			this.numberFeatures = numberFeatures;
		}

	}

}
