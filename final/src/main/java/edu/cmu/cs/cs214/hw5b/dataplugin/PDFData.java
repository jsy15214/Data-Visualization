package edu.cmu.cs.cs214.hw5b.dataplugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.Framework;


/**
 * A data plugin for the weather data visualization framework.
 * @author YanningMao1
 *
 */
public final class PDFData implements DataPlugin {
	
	/* ---------- class constants ---------- */

	private static final String NAME = "PDF Reader";
	private static final String DESCRIPTION = "Please enter the path of the PDF file";
	
	/* ---------- instance methods ---------- */
	
	/**
	 * Reads the data from the given PDF file, and returns a data set representing the
	 * data read form the file, with the data set's name set to the name given in the
	 * argument.
	 * 
	 * @param fileName, the name of the PDF file
	 * @param dataSetName, the name of the data set to be generated
	 * @return dataSet, the data set representing the data read from the PDF file
	 * @throws IOException, any IO exception while retrieving data from the PDF file
	 */
	@Override
	public DataSet processData(String filePath, String dataSetName) throws IOException {
		
		// delegate job to the PDF reader
		PDFReader pdfReader = new PDFReader();
		String text = pdfReader.read(filePath);
		
		// create a new data set to store extracted data
		DataSet dataSet = new DataSet(dataSetName);
		
		// split into lines on new line character
		String[] lines = text.split("\n");
		
		// first row is title row
		boolean isTitleRow = true;
		
		// split into words on space
		for (String line : lines) {
			List<String> row = new ArrayList<>();
			String[] entries = line.split(" ");
			// strip white spaces
			for (String entry : entries) {
				row.add(entry.trim());
			}
			// append the row to data set
			if (isTitleRow) {
				isTitleRow = false;
				dataSet.addTitleRow(row);
			} else {
				dataSet.addDataRow(row);
			}
		}
		
		return dataSet;
	}

	@Override
	public void onRegister(Framework framework) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Returns the name of the data plugin.
	 * 
	 * @return name, name of the data plugin
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Gets the description of the data plugin, which describes what to input for the path.
	 * 
	 * @return description, description of the data plugin
	 */
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}

/**
 * A PDF reader that interacts with a PDF file.
 * Can take a file path, and extracts the text in that file.
 * 
 * @author YanningMao
 */
class PDFReader {

	/* ---------- instance variables ---------- */
	
	private PDFParser parser;
	private PDFTextStripper pdfStripper;
	private PDDocument pdDoc;
	private COSDocument cosDoc;

	/* ----------- instance methods ----------- */

	/**
	 * Reads any text in the PDF file.
	 * 
	 * @param filePath, the path of the PDF file.
	 * @return text, the text read from the file.
	 * @throws IOException
	 */
	public String read(String filePath) throws IOException {
		
		// initialize
		pdfStripper = null;
		pdDoc = null;
		cosDoc = null;

		// set the parser
		File file = new File(filePath);
		
		parser = new PDFParser(new RandomAccessFile(file, "r"));


		// parse the file
		parser.parse();
		cosDoc = parser.getDocument();
		pdfStripper = new PDFTextStripper();
		
		pdDoc = new PDDocument(cosDoc);
		
		// set start and end pages
		int pages = pdDoc.getNumberOfPages();
		pdfStripper.setStartPage(1);
		pdfStripper.setEndPage(pages);

		// read the text
		String text = pdfStripper.getText(pdDoc);
		
		// close files
		pdDoc.close();
		cosDoc.close();
		return text;
	}

}