package hw5a;

import java.util.List;

/** The representation of extracted data */
public interface Dataset {
	/**
	 * Retrieve data from row i.
	 * 
	 * @param i
	 *            row index of the row that needed to be retrieved
	 * @return all the data in row i
	 */
	List<? extends Object> getRow(int i);

	/**
	 * Retrieve data from column i.
	 * 
	 * @param i
	 *            column index of the column that needed to be retrieved
	 * @return all the data in column i
	 */
	List<? extends Object> getCol(int i);

	/**
	 * Retrieve data from row i,column j.
	 * 
	 * @param i
	 *            row index
	 * @param j
	 *            column index
	 * @return the data from row i, col j
	 */
	String getCell(int i, int j);

	/**
	 * Return column of the data set.
	 * 
	 * @return title of the data set
	 */
	String getTitle();

	/**
	 * Return name of the indicated row.
	 * 
	 * @param i
	 *            row index
	 * @return name of the indicated row
	 */
	String getRowTitle(int i);

	/**
	 * Return name of the indicated column.
	 * 
	 * @param i
	 *            column index
	 * @return name of the indicated column
	 */
	String getColTitle(int i);
}
