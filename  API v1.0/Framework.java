package hw5a;

import java.util.List;

/**
 * The interface by which DataPlugin and DisplayPlugin instances can directly
 * interact with the framework.
 */
public interface Framework {
	/**
	 * Set the filePath to inputPath.
	 */
	void setPath(String inputPath);

	/**
	 * Set this.datp to datp.
	 * 
	 * @param datp
	 *            input data plug-in
	 */
	void loadDataPlugin(DataPlugin datp);

	/**
	 * Set current display plug-in to visp.
	 * 
	 * @param visp
	 *            input display plug-in
	 */
	void loadVisualPlugin(DisplayPlugin visp);

	/**
	 * Called every time the user clicked display button.
	 */
	void createVisualThread();

	/**
	 * Register data plug-in to framework.
	 */
	void registerDataPlugin(DataPlugin datp);

	/**
	 * Register display plug-in to framework.
	 */
	void registerVisualPlugin(DisplayPlugin visp);

	/**
	 * Sort selected columns of data according to cmp.
	 * 
	 * @param cols
	 *            index of the columns that sort is applied
	 * @param cmp
	 *            a comparator used for sorting.
	 */
	void sort(List<Integer> cols, Comparator cmp);

	/**
	 * Filter selected columns of data according to cmp.
	 * 
	 * @param cols
	 *            index of the columns that filter is applied
	 * @param flt
	 *            a flt used for filtering
	 */
	void filter(List<Integer> cols, Filter flt);

}
