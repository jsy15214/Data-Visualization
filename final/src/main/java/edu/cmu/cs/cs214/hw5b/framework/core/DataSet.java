package edu.cmu.cs.cs214.hw5b.framework.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An internal representation of the extracted data to be represented.
 * assume tabular.
 * Class invariant : rectangular data, comparatorList.size() = dataSet.size(),
 * 			title list size = number of columns
 * 
 * @author YanningMao
 *
 */
public final class DataSet {
	
	/* ------------- class constants --------------- */

	private static final double EPSILON = 1e-10;
	
	/* ------------- instance variables --------------- */
	
	private List<List<DataEntry>> dataSet;
	private List<String> titleRow;
	private String name = "data";		// default name of the data set
	
	/* ----------------- constructor ------------------ */
	
	public DataSet(String name) {
		this.name = name;
		dataSet = new ArrayList<>();
		titleRow = new ArrayList<>();
	}
	
	/* ----------------- class methods ------------------ */

	/**
	 * Inner joins all given data sets into one single data set. Data sets with
	 * extra rows are truncated to fit with the shortest one.
	 * Requires : argument is not null.
	 * Ensures : the returned data set is a rectangular data set with corresponding
	 * 	     titles.
	 * 
	 * @param dataSets, the set of data sets to be inner joined
	 * @return newDataSet, the resulting data set after inner joining
	 */
	public static final DataSet innerJoinDataSets(List<DataSet> dataSets) {
		
		// create a new data set
		DataSet newDataSet = new DataSet("data");
		List<String> newTitleRow = new ArrayList<>();
		
		// join all data sets
		for (DataSet dataSet : dataSets) {
			// join each column
			for (int j = 0; j < dataSet.getNumberOfCols(); j ++) {
				if (newDataSet.innerJoinCol(dataSet.getCol(j))) {
					// join the title
					newTitleRow.add(dataSet.getTitleRow().get(j));
				}
			}
		}
				
		// give duplicate titles different names
		Map<String, Integer> titleOccurences = new HashMap<>();
		
		for (int i = 0; i < newTitleRow.size(); i ++) {
			
			// get occurrence of the title
			String title = newTitleRow.get(i);
			Integer occur = titleOccurences.get(title);
			
			// if appeared before
			if (occur != null) {
				// reset the title and occurrence
				newTitleRow.set(i, title + occur.intValue());
				titleOccurences.put(title, occur.intValue() + 1);
			}
			
			// if didn't appear before
			else {
				// update occurrence
				titleOccurences.put(title, 1);
			}
		}
		
		newDataSet.addTitleRow(newTitleRow);
		return newDataSet;
	}
	
	/**
	 * Inner joins all given data sets into one single data set. Data sets with
	 * extra rows are truncated to fit with the shortest one.
	 * Requires : argument is not null.
	 * Ensures : the returned data set is a rectangular data set with corresponding
	 * 	     titles.
	 * 
	 * @param dataSets, the map representing data sets to be inner joined. The key is
	 * 	            the data set, and the value is the list of indices of columns
	 * 		    to be selected from the data set.
	 * @return newDataSet, the data set after inner joining
	 */
	public static final DataSet innerJoinDataSets(Map<DataSet, List<Integer>> dataSets) {
		
		List<DataSet> dataSetList = new ArrayList<>();
		
		Iterator<Entry<DataSet, List<Integer>>> iter = dataSets.entrySet().iterator();
		// extract selected columns from each data set
		while (iter.hasNext()) {
			Map.Entry<DataSet, List<Integer>> pair = iter.next();
			dataSetList.add(pair.getKey().selectCols(pair.getValue()));
		}
		
		// join all data sets
		return innerJoinDataSets(dataSetList);
		
	}
	
	/* --------------- private helpers ---------------- */

	private DataSet rearrangeRows(List<Integer> rowIndices) {
		
		DataSet newDataSet = new DataSet(getName());
		
		if (rowIndices == null || rowIndices.size() == 0) {
			return newDataSet;
		}
		
		// set title row
		newDataSet.addTitleRow(new ArrayList<>(getTitleRow()));
		
		// rearrange all rows in the data set
		for (List<DataEntry> col : dataSet) {
			// create a new column
			List<String> newCol = new ArrayList<>();
			// add entries in rearranged order
			for (int rowIdx : rowIndices) {
				newCol.add(col.get(rowIdx).toString());
			}
			// append the new column to new data set
			newDataSet.addDataCol(newCol);
		}
		
		return newDataSet;
	}
	
	/**
	 * Rearrange the columns of the data set, according to the order of the indices
	 * given as the argument.
	 * Returns the empty data set, if the given indices are null or empty; returns
	 * the reordered data set otherwise.
	 * Out of bound indices are ignored.
	 * 
	 * @param colIndices, the new order of the columns
	 * @return dataSet, the new data set after rearranging columns
	 */
	private DataSet rearrangeCols(List<Integer> colIndices) {
		
		int numberOfCols = getNumberOfCols();
		int numberOfRows = getNumberOfRows();
		
		// create a new data set and new title row
		DataSet newDataSet = new DataSet(getName());
		List<String> newTitleRow = new ArrayList<>();
		
		// check null and empty
		if (colIndices == null || colIndices.size() == 0) {
			return newDataSet; 
		}
		
		// rearrange the columns and titles
		for (int j : colIndices) {
			List<String> newCol = new ArrayList<>();
			List<DataEntry> oldCol = getCol(j);
			// copy each entry in the column
			if (j >= 0 && j < numberOfCols) {
				for (int i = 0; i < numberOfRows; i ++) {
					newCol.add(oldCol.get(i).toString());
				}
			}
			// append the new column
			if (newCol.size() > 0) {
				newTitleRow.add(titleRow.get(j));
				newDataSet.addDataCol(newCol);
			}
		}
		
		newDataSet.addTitleRow(newTitleRow);
		return newDataSet;
	}
	

	/**
	 * Checks if two double values are approximately equal, within the epsilon.
	 * 
	 * @param d1, the first double value
	 * @param d2, the second double value
	 * @return equality, true if approximately equal; false otherwise
	 */
	private boolean approxEqual(Double d1, Double d2) {
		
		// check null
		if (d1 == null || d2 == null) {
			return false;
		}
		
		// check value
		if ((d1.doubleValue() - EPSILON <= d2) && (d1.doubleValue() + EPSILON >= d2)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if a double is approximately within the range of two given ranges.
	 * 
	 * @param d, the value to be checked
	 * @param lo, the lower bound
	 * @param hi, the higher bound
	 * @return isWithin, true if d is within low and high; false otherwise
	 */
	private boolean approxWithin(Double d, Double lo, Double hi) {
		
		// check null
		if (d == null || lo == null || hi == null) {
			return false;
		}
		
		// check value
		if ((lo.doubleValue() - EPSILON <= d.doubleValue())
				&& (hi.doubleValue() + EPSILON >= d.doubleValue())) {
			return true;
		} else {
			return false;
		}
	}

	
	/* --------------- instance methods - filter a column ---------------- */
	
	/**
	 * Filters out rows in the data set which have data entry with the given prefix and suffix
	 * at the given column.
	 * Returns the original data set if the column index is not valid, or the prefix and suffix
	 * are both empty;
	 * returns the empty data set if the original data set is empty, or no such rows exits;
	 * returns the filtered data otherwise.
	 * 
	 * @param colIdx, the index of the column being looked at
	 * @param prefix, the prefix of the string to be selected
	 * @param suffix, the suffix of the string to be selected
	 * @return newDataSet, the data set after filter
	 */
	public DataSet filterRowsByString(int colIdx, String prefix, String suffix) {
		
		// return empty data set
		if (getNumberOfRows() == 0 || getNumberOfCols() == 0) {
			DataSet newDataSet =  new DataSet(getName());
			newDataSet.addTitleRow(new ArrayList<>(getTitleRow()));
			return newDataSet;
		}
		
		// return original data set
		if ((colIdx < 0) || (colIdx >= getNumberOfCols())
				 || (prefix.length() == 0 && suffix.length() == 0)) {
			List<Integer> rowIndices = new ArrayList<>();
			for (int i = 0; i < getNumberOfRows(); i ++) {
				rowIndices.add(i);
			}
			return rearrangeRows(rowIndices);
		}
		
		// select rows
		List<DataEntry> col = getCol(colIdx);
		List<Integer> rowIndices = new ArrayList<>();
		
		// filter using prefix and suffix
		for (int i = 0; i < getNumberOfRows(); i ++) {
			String dataStr = col.get(i).toString();
			// check prefix and suffix
			if (prefix.length() == 0 && dataStr.endsWith(suffix)) {
				rowIndices.add(i);
			} else if (suffix.length() == 0 && dataStr.startsWith(prefix)) {
				rowIndices.add(i);
			} else if (dataStr.startsWith(prefix) && dataStr.endsWith(suffix)) {
				rowIndices.add(i);
			} 
		}
		
		// return selected rows
		return rearrangeRows(rowIndices);
	}
	
	/**
	 * Filters out rows in the data set whose data entry is contained in given target strings.
	 * Returns original data set if the column index is not valid, or target strings empty;
	 * returns the empty data set if the original data set is empty, or no such rows exits;
	 * returns the filtered data otherwise.
	 * 
	 * @param colIdx, the index of the column being looked at
	 * @param targets, the list of strings to be kept
	 * @return newDataSet, the data set after filter
	 */
	public DataSet filterRowsByString(int colIdx, List<String> targets) {
		
		// return empty data set
		if (getNumberOfRows() == 0 || getNumberOfCols() == 0) {
			DataSet newDataSet =  new DataSet(getName());
			newDataSet.addTitleRow(new ArrayList<>(getTitleRow()));
			return newDataSet;
		}
		
		// return original data set
		if ((colIdx < 0) || (colIdx >= getNumberOfCols()) || (targets.size() == 0)) {
			List<Integer> rowIndices = new ArrayList<>();
			for (int i = 0; i < getNumberOfRows(); i ++) {
				rowIndices.add(i);
			}
			return rearrangeRows(rowIndices);
		}

		// select rows
		List<DataEntry> col = getCol(colIdx);
		List<Integer> rowIndices = new ArrayList<>();
		
		// filter out target strings
		for (int i = 0; i < getNumberOfRows(); i ++) {
			String dataStr = col.get(i).toString();
			// check prefix and suffix
			if (targets.contains(dataStr)) {
				rowIndices.add(i);
			}
		}
		
		return rearrangeRows(rowIndices);
	}
	
	/**
	 * Filters out rows in the data set which have data entry within the given lower bound and
	 * upper bound at the given column.
	 * If the column has string values, the bounds are compared as string; if the column has
	 * numeric values, the bounds are compared in their numeric values.
	 * Returns the original data set if the column index is not valid, or the lower bound is
	 * higher than the upper bound; returns the empty data set if the original data set is
	 * empty, or no such rows exits; returns the filtered data otherwise.
	 * 
	 * @param colIdx, the index of the column being looked at
	 * @param lo, the lower bound value
	 * @param hi, the upper bound value
	 * @return newDataSet, the data set after filter
	 */
	public DataSet filterRowsByDouble(int colIdx, double lo, double hi) {
				
		// return empty data set
		if (getNumberOfRows() == 0 || getNumberOfCols() == 0) {
			DataSet newDataSet =  new DataSet(getName());
			newDataSet.addTitleRow(new ArrayList<>(getTitleRow()));
			return newDataSet;
		}
		
		// return original data set
		if ((colIdx < 0) || (colIdx >= getNumberOfCols()) || (lo > hi)) {
			List<Integer> rowIndices = new ArrayList<>();
			for (int i = 0; i < getNumberOfRows(); i ++) {
				rowIndices.add(i);
			}
			return rearrangeRows(rowIndices);
		}
		
		// select rows
		List<DataEntry> col = getCol(colIdx);
		List<Integer> rowIndices = new ArrayList<>();
		// filter each data entry
		for (int i = 0; i < getNumberOfRows(); i ++) {
			DataEntry data = col.get(i);
			// if double data, compare double value
			if (data.isDouble()) {
				Double dData = data.convertToDouble();
				if (dData.doubleValue() >= lo && dData.doubleValue() <= hi) {
					rowIndices.add(i);
				}
			}
			// if integer data, compare integer value
			else {
				Double dData = Double.POSITIVE_INFINITY;
				if (data.isInteger()) {
					dData = data.convertToInteger().doubleValue();
				} else if (data.isDouble()) {
					dData = data.convertToDouble();
				}
				if (approxWithin(dData, lo, hi)) {
					rowIndices.add(i);
				}
			}
		}
		
		// return selected rows
		return rearrangeRows(rowIndices);
	}
	
	/**
	 * Filters out rows in the data set whose data entry is contained in given target strings.
	 * Returns original data set if the column index is not valid, or target strings empty;
	 * returns the empty data set if the original data set is empty, or no such rows exits;
	 * returns the filtered data otherwise.
	 * 
	 * @param colIdx, the index of the column being looked at
	 * @param targets, the list of strings to be kept
	 * @return newDataSet, the data set after filter
	 */
	public DataSet filterRowsByDouble(int colIdx, List<Double> targets) {
		
		// return empty data set
		if (getNumberOfRows() == 0 || getNumberOfCols() == 0) {
			DataSet newDataSet =  new DataSet(getName());
			newDataSet.addTitleRow(new ArrayList<>(getTitleRow()));
			return newDataSet;
		}
		
		// return original data set
		if ((colIdx < 0) || (colIdx >= getNumberOfCols()) || (targets.size() == 0)) {
			List<Integer> rowIndices = new ArrayList<>();
			for (int i = 0; i < getNumberOfRows(); i ++) {
				rowIndices.add(i);
			}
			return rearrangeRows(rowIndices);
		}

		// select rows
		List<DataEntry> col = getCol(colIdx);
		List<Integer> rowIndices = new ArrayList<>();
		
		// filter out target strings
		for (int i = 0; i < getNumberOfRows(); i ++) {
			DataEntry data = col.get(i);
			// if data entry is string
			if (data.isString()) {
				if (targets.contains(data.toString())) {
					rowIndices.add(i);
				}
			}
			// if data entry is numeric
			else {
				Double dData = Double.POSITIVE_INFINITY;
				// get the double value
				if (data.isDouble()) {
					dData = col.get(i).convertToDouble();
				} else if (data.isInteger()) {
					dData = col.get(i).convertToInteger().doubleValue();
				}
				// check if appears in the target list
				for (Double d : targets) {
					if (approxEqual(d, dData)) {
						rowIndices.add(i);
						break;
					}
				}
			}
		}
		
		return rearrangeRows(rowIndices);
	}
	
	/* -------------- instance methods - sorting by columns -------------- */

	/**
	 * Sorts the records in the data set based on selected columns and indicated order.
	 * Requires : columns list is not null
	 * 
	 * @param cols, indices of the columns based on which to sort, order indicates priority
	 * @param order, the order of sorting, true means increasing and false means decreasing
	 * @return newDataSet, the sorted data set
	 */
	public DataSet sortRows(List<Integer> cols, boolean order) {
		
		// create a list of row indices
		List<Integer> rows = new ArrayList<>();
		for (int i = 0; i < getNumberOfRows(); i ++) {
			rows.add(i);
		}
		
		// create the comparator
		Comparator<Integer> cmp = new RowComparator(cols, order);
		
		// sort the rows
		List<Integer> rowIndices = sortRows(rows, cmp);
		
		// rearrange the data set
		return rearrangeRows(rowIndices);
	}
	
	
	private List<Integer> sortRows(List<Integer> rows, Comparator<Integer> cmp) {
		
		int size = rows.size();
		List<Integer> lst = new ArrayList<>();
		
		// empty row list
		if (size == 0) {
			return lst;
		}
		
		// singleton row list
		if (size == 1) {
			lst.add(rows.get(0));
			return lst;
		}
		
		// merge sort row list
		int midIdx = (size + 1) / 2;
		List<Integer> lst1 = sortRows(rows.subList(0, midIdx), cmp);
		List<Integer> lst2 = sortRows(rows.subList(midIdx, size), cmp);
		return merge(lst1, lst2, cmp);
	}
	
	private List<Integer> merge(List<Integer> lst1, List<Integer> lst2, Comparator<Integer> cmp) {
		
		// create a new list to store merge result
		List<Integer> lst = new ArrayList<>();
		
		// sizes of two lists to be merged
		int len1 = lst1.size();
		int len2 = lst2.size();
		
		// flags of index of the next element to be merged
		int idx1 = 0;
		int idx2 = 0;
		
		// merge into one list of indices
		while (idx1 < len1 && idx2 < len2) {
			Integer int1 = lst1.get(idx1);
			Integer int2 = lst2.get(idx2);
			// compare two rows
			if (cmp.compare(int1, int2) != 1) {
				lst.add(int1);
				idx1 ++;
			} else {
				lst.add(int2);
				idx2 ++;
			}
		}
		
		// add remaining arguments
		for (int i = idx1; i < len1; i ++) {
			lst.add(lst1.get(i));
		}
		for (int i = idx2; i < len2; i ++) {
			lst.add(lst2.get(i));
		}	
		
		return lst;
		
	}
	
	/**
	 * Represents a comparator, which compares two rows in the data set.
	 * Takes a list of ordered indices, which indicate the priority of
	 * fields, and a flag indicating whether the comparison is in increasing
	 * order or decreasing order.
	 *
	 */
	private class RowComparator implements Comparator<Integer> {
		
		private final List<Integer> cols;
		private final boolean isIncreasing;
		
		/**
		 * Constructs a comparator object.
		 * 
		 * @param cols, indices of a list of columns to be sorted
		 * @param order, sort order, true - rising, false - decreasing
		 */
		public RowComparator(List<Integer> cols, boolean isIncreasing) {
			this.cols = cols;
			this.isIncreasing = isIncreasing;
		}
		
		/**
		 * Compares the two rows at given indices. Returns an integer indicating
		 * the comparison result. Returns 0 if two rows are equal; a negative value
		 * if the first row should come before; a positive value if the second row
		 * should come before
		 * 
		 * @param row1, the index of the first row in the data set
		 * @param row2, the index of the second row in the data set
		 * @return relationship, the integer indicating the comparison result
		 */
		public int compare(Integer row1, Integer row2) {
			
			// compare two rows based on indicated column priority
			for (int i : cols) {
				DataEntry data1 = dataSet.get(i).get(row1);
				DataEntry data2 = dataSet.get(i).get(row2);
				// compare two data
				int result = data1.compareTo(data2);
				if (result != 0) {
					return isIncreasing ? result : -result;
				}
			}
			
			// two rows are equal
			return 0;
		}
		
	}
	
	/* -------------- selecting columns & rows ---------------- */
	
	/**
	 * Selects columns from the data set, and returns the selected columns as a
	 * new data set.
	 * Empty data set is returned if no columns are selected.
	 * 
	 * @param colIndices, the indices of the selected columns
	 * @return newDataSet, the data set of selected columns
	 */
	public final DataSet selectCols(List<Integer> colIndices) {
		return rearrangeCols(colIndices);
	}
	
	/**
	 * Selects rows from the data set, and returns the selected rows as a
	 * new data set.
	 * Empty data set is returned if no rows are selected.
	 * 
	 * @param rowIndices, the indices of the selected rows
	 * @return newDataSet, the data set of selected rows
	 */
	public final DataSet selectRows(List<Integer> rowIndices) {
		return rearrangeRows(rowIndices);
	}
	

	/* -------------- setters ---------------- */
	
	/**
	 * Sets the name of the data set.
	 * Requires : input name is not null.
	 * 
	 * @param name, the name of the data set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Requires : 1. row != null, dataSet != null
	 * 	      2. data set is empty, or the data set contains rectangular data
	 * 	      2. data set is empty, or the row has same number of elements as existing rows
	 * Ensures : 1. the input data record(row) is appended at the end of data set
	 * 	     2. the data set contains rectangular data
	 * 
	 * @param row, the new row of data to be added to the data set
	 * @return succeed, true if adding the record succeeded; false otherwise
	 */
	public boolean addDataRow(List<String> row) {
				
		// check null
		if (row == null || dataSet == null) {
			return false;
		}
		
		int dataSetSize = dataSet.size();

		// check size
		if ((dataSetSize != 0) && (dataSetSize != row.size())) {
			return false;
		}
		
		// append the row
		for (int j = 0; j < row.size(); j ++) {
			// create new data entry
			DataEntry data = new DataEntry(row.get(j));
			// if data set is not empty
			if (dataSetSize != 0) {
				dataSet.get(j).add(data);
			} else {
				List<DataEntry> col = new ArrayList<DataEntry>();
				col.add(data);
				dataSet.add(col);
			}	
		}
		
		return true;
	}

	
	/**
	 * Appends a column to the right of the data set. The column will be ignored if
	 * its size does not agree with the pre-existing columns.
	 * 
	 * Requires : 1. row != null, dataSet != null
	 * 	      2. data set is empty, or the data set contains rectangular data
	 * 	      2. data set is empty, or the col has same number of elements as existing cols
	 * Ensures : 1. the input data field(col) is appended at the right most column of data set
	 * 	     2. the data set contains rectangular data
	 * 
	 * @param col, the new column of data to be added to the data set
	 * @return succeed, true if adding the column succeeded; false otherwise
	 */
	public boolean addDataCol(List<String> col) {
		
		// check null
		if (dataSet == null || col == null) {
			return false;
		}
		
		int dataSetSize = dataSet.size();

		// check size
		if ((dataSetSize != 0) && (dataSet.get(0).size() != col.size())) {
			return false;
		}
		
		// append the column
		List<DataEntry> dataEntryCol = new ArrayList<>();
		for (String data : col) {
			dataEntryCol.add(new DataEntry(data));
		}
		dataSet.add(dataEntryCol);
		return true;
	}
	
	/**
	 * Concatenate a column to the current data set. After concatenation, the data
	 * set will be resized such that all column will be truncated according to the
	 * shortest column.
	 * 
	 * Requires : the original data set is rectangular
	 * Ensures : the data set is rectangular after joining the new column
	 * 
	 * @param col, the column to be concatenated to the current data set
	 */
	public boolean innerJoinCol(List<DataEntry> col) {
		
		// if column is null or empty, do nothing
		if (col == null || col.size() == 0) {
			return false;
		}
		
		// if data set is empty, directly add the col
		if (getNumberOfCols() == 0) {
			List<String> newCol = new ArrayList<>();
			for (DataEntry entry : col) {
				newCol.add(entry.toString());
			}
			addDataCol(newCol);
			return true;
		}
		
		// if data set is not empty, column is not empty, join the column
		else {
			int dataSetNumberOfRows = getNumberOfRows();
			int colNumberOfRows = col.size();
			// find the smaller one
			int numberOfRows = Math.min(dataSetNumberOfRows, colNumberOfRows);
			dataSet = dataSet.subList(0, numberOfRows);
			col = col.subList(0, numberOfRows);
			// append the column
			List<String> newCol = new ArrayList<>();
			for (DataEntry entry : col) {
				newCol.add(entry.toString());
			}
			addDataCol(newCol);
			return true;
		}
	}
	
	
	/**
	 * Add the row of column titles.
	 * Requires : 1. titleList != null 2. titleList stores title for each field in order
	 * Ensures : 1. titleRow instance variable is set
	 * 
	 * @param titleList, the list of titles of fields in order
	 * @return succeed, true if preconditions are met and add succeeded; false otherwise
	 */
	public boolean addTitleRow(List<String> titleList) {
		
		// check null
		if (dataSet == null || titleList == null) {
			return false;
		}
		
		// check size
		if (dataSet.size() != 0 && dataSet.size() != titleList.size()) {
			return false;
		}
		
		// add the title row
		titleRow = new ArrayList<>();
		for (String title : titleList) {
			titleRow.add(title);
		}
		
		// postcondition
		assert(titleRow != null);
		assert(dataSet.size() == 0 || dataSet.size() == titleRow.size());
		
		return true;
	}

	/* ------------- getters ------------- */
	
	/**
	 * Gets the name of the data set.
	 * Requires : name is not null.
	 * 
	 * @return name, name of the data set.
	 */
	public String getName() {
		assert(name != null);
		return name;
	}
	
	/**
	 * Gets a column(field) data from the indicated index column.
	 * Requires : 1. the dataSet stores rectangular data 2. 0 <= i and i < number of fields
	 * Ensures : data set is not changed
	 * 
	 * @param idx, the index of the column
	 * @return column, the column of data retrieved.
	 */
	public List<DataEntry> getCol (int idx) {
		
		List<DataEntry> colCopy = new ArrayList<>();

		// check range
		if (idx < 0 || idx >= getNumberOfCols()) {
			return colCopy;
		}
		
		// create a new list to store the column
		List<DataEntry> col = dataSet.get(idx);
		for (DataEntry data : col) {
			colCopy.add(data.copy());
		}
		
		return colCopy;
	}
	
	/**
	 * Gets a row(record) data from the indicated index row.
	 * Requires : 1. the dataSet stores rectangular data 2. 0 <= i and i < number of records
	 * Ensures : data set is not changed
	 * 
	 * @param idx, the index of the row
	 * @return row, the row of data retrieved.
	 */
	public List<DataEntry> getRow (int idx) {
		
		List<DataEntry> rowCopy = new ArrayList<>();
		
		// check range
		if (idx < 0 || idx >= getNumberOfRows()) {
			return rowCopy;
		}
		
		// add all cells on row index
		for (List<DataEntry> col: dataSet) {
			rowCopy.add(col.get(idx).copy());
		}
		
		return rowCopy;
	}
	
	/**
	 * Gets the title row of the data set.
	 * 
	 * @return titleRowCopy, a copy of the title row.
	 */
	public List<String> getTitleRow() {
		// create a new list
		List<String> titleRowCopy = new ArrayList<>();
		
		// copy the title row
		for (String title : titleRow) {
			titleRowCopy.add(title);
		}
		
		return titleRowCopy;
	}
	
	
	/**
	 * Gets the number of fields (columns) in the data set
	 * 
	 * @return numberOfCols, the number of fields in the data set.
	 */
	public int getNumberOfCols() {
		// check null
		if (dataSet == null) {
			return 0;
		} else {
			return dataSet.size();
		}
	}
	
	/**
	 * Gets the number of records (rows) in the data set
	 * 
	 * @return numberOfRows, the number of rows in the data set.
	 */
	public int getNumberOfRows() {
		if (dataSet == null || dataSet.size() == 0) {
			return 0;
		} else {
			return dataSet.get(0).size();
		}
	}
	
	/* -------------- override object methods ---------------- */
	
	/**
	 * Checks equality between two data sets.
	 * Two data sets are considered equal if they have the same name.
	 * Two data sets have the same name if they are exactly the same data set object.
	 * 
	 * @param other, the object to be compared
	 * @return equality, true if equal; false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		
		// check type
		if (!(other instanceof DataSet)) {
			return false;
		}
		
		// convert
		DataSet otherDataSet = (DataSet) other;
		
		// check name equality
		return getName().equals(otherDataSet.getName());
	}
	
	/**
	 * Returns the hash code of the data set.
	 * 
	 * @return hashCode, the hash code of the data set
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}