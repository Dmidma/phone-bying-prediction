package graph.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import graph.SmartPhones;


public class SmartPhoneTester {

	SmartPhones smartPhones;
	int a = 5;
	int b = 6;

	@Before
	public void setUp() {
		smartPhones = new SmartPhones();
		smartPhones.addSmartPhone("IPhone", 600.3f, 50);
		smartPhones.addSmartPhone("SamsungG", 550.10f, 53);
		
		int a = 5;
		int b = 6;
	}
	
	
	@Test
	public void testSomme() {
		assertEquals("Somme de deux entiers", (a + b), 15);
	}
	
	@Test
	public void testMapping() {

		float[] inputValueRange = {0, 2, 2, 5, 5, 10, 10, 20};
		float[] outputValueRange = {0, 5, 5, 20, 20, 50, 50, 100};

		// wrong input
		try {

			smartPhones.getInputRange(0);
			fail("Did not throw an exception!");

		} catch (IndexOutOfBoundsException e) {

		}
		try {

			smartPhones.getOutputRange(5);
			fail("Did not throw an exception!");

		} catch (IndexOutOfBoundsException e) {

		}

		// test input range
		for (int i = 0, j = 1; i < inputValueRange.length; i += 2, j++) {
			assertEquals(inputValueRange[i], smartPhones.getInputRange(j)[0], 2);
			assertEquals(inputValueRange[i + 1], smartPhones.getInputRange(j)[1], 2);
		}

		// test output range
		for (int i = 0, j = 1; i < outputValueRange.length; i += 2, j++) {
			assertEquals(outputValueRange[i], smartPhones.getOutputRange(j)[0], 2);
			assertEquals(outputValueRange[i + 1], smartPhones.getOutputRange(j)[1], 2);
		}



	}

	/*
	 // To test this you need to make getMapedInput method in SmartPhone class public. 
	@Test
	public void testInputMaping() {

		assertEquals("Testing maped input values", 2, sam.getMapedInput(3.2f));

		assertEquals("Testing maped input values", 1, sam.getMapedInput(1.5f));

		assertEquals("Testing maped input values", 3, sam.getMapedInput(6.8f));

		// larger than max bound values
		assertEquals("Testing maped input values", 4, sam.getMapedInput(50));
		assertEquals("Testing maped input values", 4, sam.getMapedInput(60));
	}

	 */

	@Test
	public void testRangeToRange() {


		float input_start = 0;
		float input_end = 2;
		float output_start = 0;
		float output_end = 10;
		float input = 5;

		// tests wrong inputs and exception throwing
		try {
			SmartPhones.mapRangeIntoRange(input_start, 
					input_end, 
					output_start,
					output_end,
					input);
			fail("Wrong Input");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
		}

		input_start = 5;
		input_end = 20;
		output_start = 2;
		output_end = 8;
		input = 2;

		try {
			SmartPhones.mapRangeIntoRange(input_start, 
					input_end, 
					output_start,
					output_end,
					input);
			fail("Wrong Input");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
		}
		
		
		// testing output
		
		input_start = 5;
		input_end = 20;
		output_start = 2;
		output_end = 8;
		input = 5;
		assertEquals(2, 
				SmartPhones.mapRangeIntoRange(input_start, 
						input_end, 
						output_start, 
						output_end, 
						input), 
				2);

		input_start = 0;
		input_end = 2;
		output_start = 0;
		output_end = 5;
		input = 1;
		assertEquals(2.5f, 
				SmartPhones.mapRangeIntoRange(input_start, 
						input_end, 
						output_start, 
						output_end, 
						input), 
				2);

		input_start = 0;
		input_end = 10;
		output_start = 0;
		output_end = 100;
		input = 6.5f;
		assertEquals(65, 
				SmartPhones.mapRangeIntoRange(input_start, 
						input_end, 
						output_start, 
						output_end, 
						input), 
				2);
		
	}
	
	
	// TODO: run more tests on ratioFeaturePrice
	@Test
	public void testRatio() {
		
		
        float f1 = smartPhones.ratioFeaturePrice(
        		smartPhones.getSmartPhone(1), 
        		smartPhones.getSmartPhone(2));
        float f2 = smartPhones.ratioFeaturePrice(
        		smartPhones.getSmartPhone(2), 
        		smartPhones.getSmartPhone(1));
		assertEquals(f1, 
				-1 * f2, 
				2);
		
		float f3 = smartPhones.ratioFeaturePrice(
        		smartPhones.getSmartPhone(0), 
        		smartPhones.getSmartPhone(2));
		assertEquals(f3, 
				0, 
				2);
		f3 = smartPhones.ratioFeaturePrice(
        		smartPhones.getSmartPhone(2), 
        		smartPhones.getSmartPhone(0));
		assertEquals(f3, 
				0, 
				2);
		
	}

}
