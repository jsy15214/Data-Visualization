package edu.cmu.cs.cs214.hw5b.framework.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DataSet class
 * 
 * Tests the internal data representation of the data extracted form sources.
 * Tests correct functionalities of successfully adding rows & columns, and
 * retrieving rows & columns from the data set.
 * 
 * @author YanningMao
 *
 */
public final class TestDataSet {

	/* ----------- class constants ------------ */
	
	private static final double EPSILON = 1e-10;
	private static final int NUMBER_OF_COLS = 3;
	private static final int NUMBER_OF_ROWS = 3;

	/* ---------- instance variables ----------- */

	DataSet data;

	/* ---------------- set up ----------------- */

	@Before
	public void setUp() {
		data = new DataSet();
	}

	/* ---------------- tests ----------------- */

	@Test
	public void testAddCol() {

		addCols();

		// check data set size
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);

		// add an invalid column
		List<String> invalidIntList = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ROWS + 1; i++) {
			invalidIntList.add(Integer.toString(i));
		}

		assertFalse(data.addDataCol(invalidIntList));

		// check data set size
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);
	}

	@Test
	public void testAddRow() {

		addRows();

		// check data set size
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);

		// add an invalid row
		List<String> invalidRow = new ArrayList<>();
		invalidRow.add("1");
		invalidRow.add("b");
		invalidRow.add("1.1");
		invalidRow.add("happy");

		assertFalse(data.addDataRow(invalidRow));

		// check data set size
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);

	}

	@Test
	public void testGetColByAddCol() {

		addCols();

		// check data set size
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);
		
		checkCols();

	}



	@Test
	public void testGetColByAddRow() {

		addRows();

		// check data set size
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);

		checkCols();

	}


	@Test
	public void testGetRowByAddRow() {

		addRows();

		// check data set size
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);

		checkRows();

	}

	@Test
	public void testGetRowByAddCol() {

		addCols();

		// check data set size
		assertEquals(data.getNumberOfRows(), NUMBER_OF_ROWS);
		assertEquals(data.getNumberOfCols(), NUMBER_OF_COLS);

		checkRows();

	}
	
	/* --------------- private helpers --------------- */
	
	private void addCols() {

		// add 1st column of integers
		List<String> intCol = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			intCol.add(Integer.toString(i + 1));
		}
		assertTrue(data.addDataCol(intCol));

		// add second column of strings
		List<String> strCol = new ArrayList<>();
		strCol.add("one");
		strCol.add("two");
		strCol.add("three");
		assertTrue(data.addDataCol(strCol));

		// add third column of doubles
		List<String> doubleCol = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			double value = i + 1.1;
			doubleCol.add(Double.toString(value));
		}
		assertTrue(data.addDataCol(doubleCol));
	}

	private void addRows() {

		// add 1st row
		List<String> rowOne = new ArrayList<>();
		rowOne.add("1");
		rowOne.add("one");
		rowOne.add("1.1");
		assertTrue(data.addDataRow(rowOne));

		// add 2nd row
		List<String> rowTwo = new ArrayList<>();
		rowTwo.add("2");
		rowTwo.add("two");
		rowTwo.add("2.1");
		assertTrue(data.addDataRow(rowTwo));

		// add 3rd row
		List<String> rowThree = new ArrayList<>();
		rowThree.add("3");
		rowThree.add("three");
		rowThree.add("3.1");
		assertTrue(data.addDataRow(rowThree));
	}
	
	private void checkCols() {
		
		// get integer column
		List<DataEntry> intList = data.getCol(0);
		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			DataEntry data = intList.get(i);
			assertTrue(data.isInteger());
			Integer intObj = data.convertToInteger();
			assertEquals(intObj.intValue(), i + 1);
		}

		// get string column
		List<DataEntry> strList = data.getCol(1);
		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			DataEntry data = strList.get(i);
			assertTrue(data.isString());
			String strObj = data.convertToString();
			if (i == 0) {
				assertEquals(strObj, "one");
			} else if (i == 1) {
				assertEquals(strObj, "two");
			} else {
				assertEquals(strObj, "three");
			}
		}

		// get boolean column
		List<DataEntry> doubleList = data.getCol(2);
		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			DataEntry data = doubleList.get(i);
			assertTrue(data.isDouble());
			Double doubleObj = data.convertToDouble();
			assertEquals(doubleObj.doubleValue(), i + 1.1, EPSILON);
		}
	}
	
	private void checkRows() {

		// get 1st row
		List<DataEntry> firstRow = data.getRow(0);
		// check types
		assertTrue(firstRow.get(0).isInteger());
		assertTrue(firstRow.get(1).isString());
		assertTrue(firstRow.get(2).isDouble());
		// convert to specific types
		Integer intObj = firstRow.get(0).convertToInteger();
		String strObj = firstRow.get(1).convertToString();
		Double doubleObj = firstRow.get(2).convertToDouble();
		// check value
		assertEquals(intObj.intValue(), 1);
		assertEquals(strObj, "one");
		assertEquals(doubleObj, 1.1, EPSILON);

		// get 2nd row
		List<DataEntry> secondRow = data.getRow(1);
		// check types
		assertTrue(secondRow.get(0).isInteger());
		assertTrue(secondRow.get(1).isString());
		assertTrue(secondRow.get(2).isDouble());
		// convert to specific types
		intObj = secondRow.get(0).convertToInteger();
		strObj = secondRow.get(1).convertToString();
		doubleObj = secondRow.get(2).convertToDouble();
		// check value
		assertEquals(intObj.intValue(), 2);
		assertEquals(strObj, "two");
		assertEquals(doubleObj, 2.1, EPSILON);

		// get 3rd row
		List<DataEntry> thirdRow = data.getRow(2);
		// check types
		assertTrue(thirdRow.get(0).isInteger());
		assertTrue(thirdRow.get(1).isString());
		assertTrue(thirdRow.get(2).isDouble());
		// convert to specific types
		intObj = thirdRow.get(0).convertToInteger();
		strObj = thirdRow.get(1).convertToString();
		doubleObj = thirdRow.get(2).convertToDouble();
		// check value
		assertEquals(intObj.intValue(), 3);
		assertEquals(strObj, "three");
		assertEquals(doubleObj, 3.1, EPSILON);
	}


}
