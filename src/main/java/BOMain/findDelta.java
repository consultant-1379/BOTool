package BOMain;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;
import java.util.Date;

//import org.apache.poi.hssf.util.HSSFColor.AQUA;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jxl.Workbook;
import jxl.format.Colour;
//import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;

//import com.sybase.jdbc4.jdbc.SybConnectionPoolDataSource;

//import jxl.*;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@SuppressWarnings("unused")

public class findDelta {

	 

	
	XSSFWorkbook oldWorkbook = null, newWorkbook = null;
	String[] requiredSheetList = {"Contexts","Objects","Classes","Conditions","Joins","Incompatibilities"};
	String ow,c4,d4,cl1,ow1,nw1,o9;
	
	LinkedHashMap<String, LinkedHashMap<String, String>> oldWorkbookData = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	LinkedHashMap<String, LinkedHashMap<String, String>> newWorkbookData = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	LinkedHashMap<String, LinkedHashMap<String, String>> deltaOldWorkbookData = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	LinkedHashMap<String, LinkedHashMap<String, String>> deltaNewWorkbookData = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	
	private String oldInput;
	private String newInput;
	private String outputPath;
	
	
   
	
	@SuppressWarnings("unchecked")
	public void fileInput(String oldInput, String newInput, String outputPath) {
	
		LinkedHashMap<String, String> h = new LinkedHashMap<String, String>();
		try {
			this.oldInput=oldInput;
			this.newInput=newInput;
			this.outputPath=outputPath;

			System.out.println("Loading Input Files");

			oldWorkbook = new XSSFWorkbook(oldInput);
			newWorkbook = new XSSFWorkbook(newInput);
			
			System.out.println("Input Files Loading Complete \n");

			System.out.println("Extracting Sheets Data");
			XSSFSheet sheet = null;
			LinkedHashMap<String, String> sheetData = new LinkedHashMap<>();
			System.out.println("Reading Individual Sheets Data ");
			for (String sheetName : requiredSheetList) {
				sheet = oldWorkbook.getSheet(sheetName);
				System.out.println("old sheet name: " + sheet.getSheetName());
				sheetData = getData(sheet);
				oldWorkbookData.put(sheetName, sheetData);

				sheet = newWorkbook.getSheet(sheetName);
				System.out.println("new sheet name: " + sheet.getSheetName());
				sheetData = getData(sheet); 
				newWorkbookData.put(sheetName, sheetData);

				deltaOldWorkbookData = (LinkedHashMap<String, LinkedHashMap<String, String>>) oldWorkbookData.clone();
				deltaNewWorkbookData = (LinkedHashMap<String, LinkedHashMap<String, String>>) newWorkbookData.clone();
			}
			
			System.out.println("Extraction Done \n");
			// finding Delta
			System.out.println("Finding Delta");

			System.out.println("Delta Complete");
			writeDelta();
		}
		catch (Exception e) {
			System.out.println("\tException in accessing fd:" + e + "\n");
			e.printStackTrace();
			System.exit(0);
		}

	}

	public boolean isEmptyCell(Cell cell) {
		if (cell == null || String.valueOf(cell).isEmpty())
			return true;
		else
			return false;

	}

	public String getCellValue(Cell cell) {
		String s = null;
		if (cell == null || String.valueOf(cell).isEmpty())
			return s;
		switch (cell.getCellType()) {
		case 0:
			Double d = cell.getNumericCellValue();
			Integer i = d.intValue();
			s = i.toString();
			break;
		case 1:
			s = cell.getStringCellValue();
			break;
		default:
			break;
		}
		return s;
	}

	public LinkedHashMap<String, String> getData(XSSFSheet sheet) {

		LinkedHashMap<String, String> sheetData = new LinkedHashMap<>();
		try {
			for (Row row : sheet) {
				String cellValue = null;
				String s = null;
				if (row != null) {
					for (Cell cell : row) {
						if (isEmptyCell(cell))
							s = "EMPTY CELL";
						else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC)
							s = getCellValue(cell);
						else
							s = String.valueOf(cell).toString();
						if (cellValue == null)
							cellValue = s;
						else
							cellValue = cellValue + ":-" + s;
					}
					sheetData.put(cellValue, row.getRowNum() + 1 + "");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetData;
	}

	public void writeDelta() throws IOException, WriteException, RowsExceededException, BiffException {
		String v2;
		String f2;
		ArrayList od  = new ArrayList();
		ArrayList nd  = new ArrayList();
		ArrayList od1 = new ArrayList();
		ArrayList nd1 = new ArrayList();
		ArrayList od2 = new ArrayList();
		ArrayList nd2 = new ArrayList();
		ArrayList od3 = new ArrayList();
		ArrayList nd3 = new ArrayList();
		ArrayList od4 = new ArrayList();
		ArrayList nd4 = new ArrayList();
		ArrayList od5 = new ArrayList();
		ArrayList nd5 = new ArrayList();
		
	    
		WritableCellFormat cellFormats  = new WritableCellFormat();
		WritableCellFormat cellFormats1 = new WritableCellFormat();
		try {
			final File exlFile = new File(outputPath+"/"+"BO_DeltaOutput_"+ (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())) + ".xls");
			
			WritableWorkbook writableWorkbook = null;

			if (!exlFile.exists() && !exlFile.isFile()) {
				writableWorkbook = Workbook.createWorkbook(exlFile);
				System.out.println("workk:");
			}
			else {
				final Workbook workbook = Workbook.getWorkbook(exlFile);
				writableWorkbook = Workbook.createWorkbook(exlFile, workbook);
				System.out.println("work1k:");
			}
			System.out.println("Writing starts");

			final WritableSheet writableSheet1  = writableWorkbook.createSheet("BO_NewConditionsSheet", 0);
			final WritableSheet writableSheet2  = writableWorkbook.createSheet("BO_OldConditionsSheet", 0);
			final WritableSheet writableSheet3  = writableWorkbook.createSheet("BO_NewJoinsSheet", 0);
			final WritableSheet writableSheet4  = writableWorkbook.createSheet("BO_OldJoinsSheet", 0);
			final WritableSheet writableSheet5  = writableWorkbook.createSheet("BO_NewIncompatibilitiesSheet", 0);
			final WritableSheet writableSheet6  = writableWorkbook.createSheet("BO_OldIncompatibilitiesSheet",  0);		
			final WritableSheet writableSheet7  = writableWorkbook.createSheet("BO_NewContextsSheet",0);
			final WritableSheet writableSheet8  = writableWorkbook.createSheet("BO_OldContextsSheet",0);
			final WritableSheet writableSheet9  = writableWorkbook.createSheet("BO_NewObjectsSheet", 0);
			final WritableSheet writableSheet10 = writableWorkbook.createSheet("BO_OldObjectsSheet", 0);
			final WritableSheet writableSheet11 = writableWorkbook.createSheet("BO_NewClassesSheet",0);
			final WritableSheet writableSheet12 = writableWorkbook.createSheet("BO_OldClassesSheet",0);		
			final WritableSheet writableSheet13 = writableWorkbook.createSheet("BO_ContextsDeltaSheet",0);
			final WritableSheet writableSheet14 = writableWorkbook.createSheet("BO_ObjectsDeltaSheet",0);
			final WritableSheet writableSheet15 = writableWorkbook.createSheet("BO_ClassesDeltaSheet",0);
			final WritableSheet writableSheet16 = writableWorkbook.createSheet("BO_ConditionsDeltaSheet",0);
			final WritableSheet writableSheet17 = writableWorkbook.createSheet("BO_JoinsDeltaSheet",0);
			final WritableSheet writableSheet18 = writableWorkbook.createSheet("BO_IncompatibilitiesDeltaSheet",0);
						
			//added
			WritableFont cellFont = new WritableFont(WritableFont.ARIAL,10);
			cellFont.setBoldStyle(WritableFont.BOLD); 
			cellFormats1 = new WritableCellFormat(cellFont);
			cellFormats1.setWrap(true); 
			
			//added
			
			int col1 = 0;
			String[] headers1 = { "SheetName", "Comment", "Input Type Sheet", "Value" };
			for (String s : headers1) {
				Label label = new Label(col1++, 0, s, cellFormats1);
				writableSheet13.addCell(label);
			}
			int col2 = 0;
			for (String s : headers1) {
				Label label = new Label(col2++, 0, s, cellFormats1);
				writableSheet14.addCell(label);
			}
			int col3 = 0;
			
			for (String s : headers1) {
				Label label = new Label(col3++, 0, s, cellFormats1);
				writableSheet15.addCell(label);
			}
			int col4 = 0;
			
			for (String s : headers1) {
				Label label = new Label(col4++, 0, s, cellFormats1);
				writableSheet16.addCell(label);
			}
			int col5 = 0;
			for (String s : headers1) {
				Label label = new Label(col5++, 0, s, cellFormats1);
				writableSheet17.addCell(label);
			}
			int col6 = 0;
			for (String s : headers1) {
				Label label = new Label(col6++, 0, s, cellFormats1);
				writableSheet18.addCell(label);
			}
			

			// Writing Data
	String inputType = null, sheetName = null, comment = null, rowNum = null, rowValue = null;
	int row1=0,row2=0,row3=0,row4=0,row5=0,row6=0,row7=0,row8=0,row9=0,row10=0,row11=0,row12=0,row13=0,row14=0,row15=0,row16=0,row17=0,row18=0;
			 
			cellFormats = new WritableCellFormat();
			cellFormats.setBackground(Colour.LIGHT_GREEN);
			
			for (String sheetNames : deltaNewWorkbookData.keySet()) {
				sheetName = sheetNames;
				
	// New Start
				if(sheetName=="Contexts")
				{
				inputType = "NewInputFile";
				comment = "NEW/MODIFIED";
				for (String s : deltaNewWorkbookData.get(sheetName).keySet()) {
					rowValue = s;
					String[] v = s.split(":-");
					String v1 = Arrays.toString(v);
					String d  = v[2];
					String d1 = v[4];
					String d2 = v[5];
					String d3 = v[6];
					 d4 = d+","+d1+","+d2+","+d3;
					nd.add(d4);
					 
					if(row7==0) {
					Label label1 = new Label(0, row7, d, cellFormats1);
					Label label2 = new Label(1, row7, d1, cellFormats1);
					Label label3 = new Label(2, row7, d2, cellFormats1);
					Label label4 = new Label(3, row7, d3, cellFormats1);
					
					writableSheet7.addCell(label1);
					writableSheet7.addCell(label2);
					writableSheet7.addCell(label3);
					writableSheet7.addCell(label4);
					++row7;
					}
					
					else
					{
						Label label1 = new Label(0, row7, d, cellFormats);
						Label label2 = new Label(1, row7, d1, cellFormats);
						Label label3 = new Label(2, row7, d2, cellFormats);
						Label label4 = new Label(3, row7, d3, cellFormats);
						
						writableSheet7.addCell(label1);
						writableSheet7.addCell(label2);
						writableSheet7.addCell(label3);
						writableSheet7.addCell(label4);
						++row7;
					}

				}
			}
				else if(sheetName=="Objects")
				{
					for (String s : deltaNewWorkbookData.get(sheetName).keySet()) {
						rowValue = s;
						String[] v = s.split(":-");
						String v1 = Arrays.toString(v); 
						String o =  v[3];
						String o1 = v[4];
						String o2 = v[5];
						String o3 = v[6];
						String o4 = v[7];
						String o5 = v[9];
						String o6 = v[10];
						String o7 = v[11];
						String o8 = v[13];
						o9 = o+","+o1+","+o2+","+o3+","+o4+","+o5+","+o6+","+o7+","+o8;
						nd1.add(o9);
						
						 if(row9==0) {
								Label label1 = new Label(0, row9, o,  cellFormats1);
								Label label2 = new Label(1, row9, o1, cellFormats1);
								Label label3 = new Label(2, row9, o2, cellFormats1);
								Label label4 = new Label(3, row9, o3, cellFormats1);
								Label label5 = new Label(4, row9, o4, cellFormats1);
								Label label6 = new Label(5, row9, o5, cellFormats1);
								Label label7 = new Label(6, row9, o6, cellFormats1);
								Label label8 = new Label(7, row9, o7, cellFormats1);
								Label label9 = new Label(8, row9, o8, cellFormats1);
								writableSheet9.addCell(label1);
								writableSheet9.addCell(label2);
								writableSheet9.addCell(label3);
								writableSheet9.addCell(label4);
								writableSheet9.addCell(label5);
								writableSheet9.addCell(label6);
								writableSheet9.addCell(label7);
								writableSheet9.addCell(label8);
								writableSheet9.addCell(label9);
								++row9;
						 }
						 else
						 {
							 Label label1 = new Label(0, row9, o, cellFormats);
							 Label label2 = new Label(1, row9, o1, cellFormats);
							 Label label3 = new Label(2, row9, o2, cellFormats);
							 Label label4 = new Label(3, row9, o3, cellFormats);
							 Label label5 = new Label(4, row9, o4, cellFormats);
							 Label label6 = new Label(5, row9, o5, cellFormats);
							 Label label7 = new Label(6, row9, o6, cellFormats);
							 Label label8 = new Label(7, row9, o7, cellFormats);
							 Label label9 = new Label(8, row9, o8, cellFormats);
							 writableSheet9.addCell(label1);
							 writableSheet9.addCell(label2);
							 writableSheet9.addCell(label3);
							 writableSheet9.addCell(label4);
							 writableSheet9.addCell(label5);
							 writableSheet9.addCell(label6);
							 writableSheet9.addCell(label7);
							 writableSheet9.addCell(label8);
							 writableSheet9.addCell(label9);
							 ++row9; 
						 }
					}
					}
				else if(sheetName=="Classes")
				{
					for (String s : deltaNewWorkbookData.get(sheetName).keySet()) {
						rowValue = s;
						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String d = v[3];
						String d1 = v[4];
						String d2 = v[5];
						cl1 = d+","+d1+","+d2;
						nd2.add(cl1);
							
						 if(row11==0)
						 {
								Label label1 = new Label(0, row11, d, cellFormats1);
								Label label2 = new Label(1, row11, d1, cellFormats1);
								Label label3 = new Label(2, row11, d2, cellFormats1);
								
								writableSheet11.addCell(label1);
								writableSheet11.addCell(label2);
								writableSheet11.addCell(label3);
								 ++row11;
			 }
			 else
			 {
				    Label label1 = new Label(0, row11, d, cellFormats);
					Label label2 = new Label(1, row11, d1, cellFormats);
					Label label3 = new Label(2, row11, d2, cellFormats);
					
					writableSheet11.addCell(label1);
					writableSheet11.addCell(label2);
					writableSheet11.addCell(label3);
					++row11;
			 }
					}
					}
				
				else if(sheetName=="Conditions")
				{
					for (String s : deltaNewWorkbookData.get(sheetName).keySet()) {
						rowValue = s;
						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String d  = v[4];
						String d1 = v[5];
						String d2 = v[6];
						String d3 = v[7];
						String m4 = v[9];
						String co1=v[4]+","+v[5]+","+v[6]+","+v[7]+","+v[9];
						nd3.add(co1);
						
					if(row1==0) 
					{			
								Label label1 = new Label(0, row1, d, cellFormats1);
								Label label2 = new Label(1, row1, d1, cellFormats1);
								Label label3 = new Label(2, row1, d2, cellFormats1);
								Label label4 = new Label(3, row1, d3, cellFormats1);
								Label label5 = new Label(4, row1, m4 , cellFormats1);
								writableSheet1.addCell(label1);
								writableSheet1.addCell(label2);
								writableSheet1.addCell(label3);
								writableSheet1.addCell(label4);
								writableSheet1.addCell(label5);
								++row1;
					}
					else
					{
						Label label1 = new Label(0, row1, d, cellFormats);
						Label label2 = new Label(1, row1, d1, cellFormats);
						Label label3 = new Label(2, row1, d2, cellFormats);
						Label label4 = new Label(3, row1, d3, cellFormats);
						Label label5 = new Label(4, row1, m4 , cellFormats);
						writableSheet1.addCell(label1);
						writableSheet1.addCell(label2);
						writableSheet1.addCell(label3);
						writableSheet1.addCell(label4);
						writableSheet1.addCell(label5);
						++row1;
					}
					}
					}
				
				else if(sheetName=="Joins")
				{
					for (String s : deltaNewWorkbookData.get(sheetName).keySet()) {
						rowValue = s;
						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String d = v[2];
						String d1 = v[3];
						String d2 = v[4];
						String d3 = v[5];
						String m4 = v[7];
						String d5 = v[8];
						String in = d+","+d1+","+d2+","+d3+","+m4+","+d5;
						nd4.add(in);
						
						if(row3==0)
						{
								Label label1 = new Label(0, row3, d, cellFormats1);
								Label label2 = new Label(1, row3, d1, cellFormats1);
								Label label3 = new Label(2, row3, d2, cellFormats1);
								Label label4 = new Label(3, row3, d3, cellFormats1);
								Label label5 = new Label(4, row3, m4, cellFormats1);
								Label label6 = new Label(5, row3, d5, cellFormats1);
								writableSheet3.addCell(label1);
								writableSheet3.addCell(label2);
								writableSheet3.addCell(label3);
								writableSheet3.addCell(label4);
								writableSheet3.addCell(label5);
								writableSheet3.addCell(label6);
								++row3;
						}
						else
						{
							Label label1 = new Label(0, row3, d, cellFormats);
							Label label2 = new Label(1, row3, d1, cellFormats);
							Label label3 = new Label(2, row3, d2, cellFormats);
							Label label4 = new Label(3, row3, d3, cellFormats);
							Label label5 = new Label(4, row3, m4, cellFormats);
							Label label6 = new Label(5, row3, d5, cellFormats);
							writableSheet3.addCell(label1);
							writableSheet3.addCell(label2);
							writableSheet3.addCell(label3);
							writableSheet3.addCell(label4);
							writableSheet3.addCell(label5);
							writableSheet3.addCell(label6);
							++row3;
						}
											}
											}

			else
			{
				for (String s : deltaNewWorkbookData.get(sheetName).keySet()) {
					rowValue = s;
					String[] v = s.split(":-");
					String v1 = Arrays.toString(v);
					String d = v[2];
					String d1 = v[3];
					String d2 = v[4];
					String d3 = v[5];
					String jo = d+","+d1+","+d2+","+d3;
			        nd5.add(jo);		 
			        
					if(row5==0) {
							Label label1 = new Label(0, row5, d, cellFormats1);
							Label label2 = new Label(1, row5, d1, cellFormats1);
							Label label3 = new Label(2, row5, d2, cellFormats1);
							Label label4 = new Label(3, row5, d3, cellFormats1);
						
							writableSheet5.addCell(label1);
							writableSheet5.addCell(label2);
							writableSheet5.addCell(label3);
						    writableSheet5.addCell(label4);
						    ++row5;
				}
				else
				{
					Label label1 = new Label(0, row5, d, cellFormats);
					Label label2 = new Label(1, row5, d1, cellFormats);
					Label label3 = new Label(2, row5, d2, cellFormats);
					Label label4 = new Label(3, row5, d3, cellFormats);
				
					writableSheet5.addCell(label1);
					writableSheet5.addCell(label2);
					writableSheet5.addCell(label3);
				    writableSheet5.addCell(label4);
				    ++row5;
				
				}
							}
					}
				}
			
	//old start
			
			cellFormats = new WritableCellFormat();
			cellFormats.setBackground(Colour.YELLOW);

			for (String sheetNames : deltaOldWorkbookData.keySet()) {
				sheetName = sheetNames;
				if(sheetName=="Contexts")
				{
				inputType = "oldInputFile";
				comment = "MODIFIED/REMOVED";
				for (String s : deltaOldWorkbookData.get(sheetName).keySet()) {

					String[] v = s.split(":-");
					String v1 = Arrays.toString(v);
					String c  = v[2];
					String c1 = v[4];
					String c2 = v[5];
					String c3 = v[6];
					c4 =c+","+c1+","+c2+","+c3;
					od.add(c4);
					 
				if(row8==0) {	
					Label label1 = new Label(0, row8, c, cellFormats1);
					Label label2 = new Label(1, row8, c1, cellFormats1);
					Label label3 = new Label(2, row8, c2, cellFormats1);
					Label label4 = new Label(3, row8, c3, cellFormats1);
					
					writableSheet8.addCell(label1);
					writableSheet8.addCell(label2);
					writableSheet8.addCell(label3);
					writableSheet8.addCell(label4);
					++row8;
				}
				else
				{
					Label label1 = new Label(0, row8, c, cellFormats);
					Label label2 = new Label(1, row8, c1, cellFormats);
					Label label3 = new Label(2, row8, c2, cellFormats);
					Label label4 = new Label(3, row8, c3, cellFormats);
					
					writableSheet8.addCell(label1);
					writableSheet8.addCell(label2);
					writableSheet8.addCell(label3);
					writableSheet8.addCell(label4);
					++row8;
				}
				}
			}
				else if(sheetName=="Objects") {
					for (String s : deltaOldWorkbookData.get(sheetName).keySet()) {

						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String oo =  v[3];
						String oo1 = v[4];
						String oo2 = v[5];
						String oo3 = v[6];
						String oo4 = v[7];
						String oo5 = v[9];
						String oo6 = v[10];
						String oo7 = v[11];
						String oo8 = v[13];
						String oo9 =oo+","+oo1+","+oo2+","+oo3+","+oo4+","+oo5+","+oo6+","+oo7+","+oo8;
						
						od1.add(oo9);
						
						if(row10==0) 
						{
						Label label1 = new Label(0, row10, oo, cellFormats1);
						Label label2 = new Label(1, row10, oo1, cellFormats1);
						Label label3 = new Label(2, row10, oo2, cellFormats1);
						Label label4 = new Label(3, row10, oo3, cellFormats1);
						Label label5 = new Label(4, row10, oo4, cellFormats1);
						Label label6 = new Label(5, row10, oo5, cellFormats1);
						Label label7 = new Label(6, row10, oo6, cellFormats1);
						Label label8 = new Label(7, row10, oo7, cellFormats1);
						Label label9 = new Label(8, row10, oo8, cellFormats1);
						writableSheet10.addCell(label1);
						writableSheet10.addCell(label2);
						writableSheet10.addCell(label3);
						writableSheet10.addCell(label4);
						writableSheet10.addCell(label5);
						writableSheet10.addCell(label6);
						writableSheet10.addCell(label7);
						writableSheet10.addCell(label8);
						writableSheet10.addCell(label9);
						++row10;
						}
						else
						{
							Label label1 = new Label(0, row10, oo, cellFormats);
							Label label2 = new Label(1, row10, oo1, cellFormats);
							Label label3 = new Label(2, row10, oo2, cellFormats);
							Label label4 = new Label(3, row10, oo3, cellFormats);
							Label label5 = new Label(4, row10, oo4, cellFormats);
							Label label6 = new Label(5, row10, oo5, cellFormats);
							Label label7 = new Label(6, row10, oo6, cellFormats);
							Label label8 = new Label(7, row10, oo7, cellFormats);
							Label label9 = new Label(8, row10, oo8, cellFormats);
							writableSheet10.addCell(label1);
							writableSheet10.addCell(label2);
							writableSheet10.addCell(label3);
							writableSheet10.addCell(label4);
							writableSheet10.addCell(label5);
							writableSheet10.addCell(label6);
							writableSheet10.addCell(label7);
							writableSheet10.addCell(label8);
							writableSheet10.addCell(label9);
							++row10;
						}
					}
				}

				else if(sheetName=="Classes") {
					for (String s : deltaOldWorkbookData.get(sheetName).keySet()) {
						// rowValue = s;

						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String oc = v[3];
						String oc1 = v[4];
						String oc2 = v[5];
						String oc4 =oc+","+oc1+","+oc2;
						od2.add(oc4);

					if(row12==0) {
					
						Label label1 = new Label(0, row12, oc, cellFormats1);
						Label label2 = new Label(1, row12, oc1, cellFormats1);
						Label label3 = new Label(2, row12, oc2, cellFormats1);
						
						writableSheet12.addCell(label1);
						writableSheet12.addCell(label2);
						writableSheet12.addCell(label3);
						++row12;
					}
					else
					{
						Label label1 = new Label(0, row12, oc, cellFormats);
						Label label2 = new Label(1, row12, oc1, cellFormats);
						Label label3 = new Label(2, row12, oc2, cellFormats);
						
						writableSheet12.addCell(label1);
						writableSheet12.addCell(label2);
						writableSheet12.addCell(label3);
						++row12;
					}

					}
				}
				else if(sheetName=="Conditions") {
					for (String s : deltaOldWorkbookData.get(sheetName).keySet()) {
						// rowValue = s;

						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String oco  = v[4];
						String oco1 = v[5];
						String oco2 = v[6];
						String oco3 = v[7];
						String oco4 = v[9];
						String oc5 =oco+","+oco1+","+oco2+","+oco3+","+oco4;
						od3.add(oc5);

					if(row2==0) {	
						Label label1 = new Label(0, row2, oco,  cellFormats1);
						Label label2 = new Label(1, row2, oco1, cellFormats1);
						Label label3 = new Label(2, row2, oco2, cellFormats1);
						Label label4 = new Label(3, row2, oco3, cellFormats1);
						Label label5 = new Label(4, row2, oco4, cellFormats1);
						writableSheet2.addCell(label1);
						writableSheet2.addCell(label2);
						writableSheet2.addCell(label3);
						writableSheet2.addCell(label4);
						writableSheet2.addCell(label5);
						++row2;
					}
					else
					{
						Label label1 = new Label(0, row2, oco,  cellFormats);
						Label label2 = new Label(1, row2, oco1, cellFormats);
						Label label3 = new Label(2, row2, oco2, cellFormats);
						Label label4 = new Label(3, row2, oco3, cellFormats);
						Label label5 = new Label(4, row2, oco4, cellFormats);
						writableSheet2.addCell(label1);
						writableSheet2.addCell(label2);
						writableSheet2.addCell(label3);
						writableSheet2.addCell(label4);
						writableSheet2.addCell(label5);
						++row2;
					}
					}
				}

				else if(sheetName=="Joins") {
					for (String s : deltaOldWorkbookData.get(sheetName).keySet()) {
						// rowValue = s;

						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String oi1 = v[2];
						String oi2 = v[3];
						String oi3 = v[4];
						String oi4 = v[5];
						String oi5 = v[7];
						String oi6 = v[8];
						String oi7 =oi1+","+oi2+","+oi3+","+oi4+","+oi5+","+oi6;
						od4.add(oi7);

						if(row4==0) {
						Label label1 = new Label(0, row4, oi1, cellFormats1);
						Label label2 = new Label(1, row4, oi2, cellFormats1);
						Label label3 = new Label(2, row4, oi3, cellFormats1);
						Label label4 = new Label(3, row4, oi4, cellFormats1);
						Label label5 = new Label(4, row4, oi5, cellFormats1);
						Label label6 = new Label(5, row4, oi6, cellFormats1);
						writableSheet4.addCell(label1);
						writableSheet4.addCell(label2);
						writableSheet4.addCell(label3);
						writableSheet4.addCell(label4);
						writableSheet4.addCell(label5);
						writableSheet4.addCell(label6);
						++row4;
						}
						else
						{
							Label label1 = new Label(0, row4, oi1, cellFormats);
							Label label2 = new Label(1, row4, oi2, cellFormats);
							Label label3 = new Label(2, row4, oi3, cellFormats);
							Label label4 = new Label(3, row4, oi4, cellFormats);
							Label label5 = new Label(4, row4, oi5, cellFormats);
							Label label6 = new Label(5, row4, oi6, cellFormats);
							writableSheet4.addCell(label1);
							writableSheet4.addCell(label2);
							writableSheet4.addCell(label3);
							writableSheet4.addCell(label4);
							writableSheet4.addCell(label5);
							writableSheet4.addCell(label6);
							++row4;
						}
					}
				}

				else {
					for (String s : deltaOldWorkbookData.get(sheetName).keySet()) {	

						String[] v = s.split(":-");
						String v1 = Arrays.toString(v);
						String oj = v[2];
						String oj1 = v[3];
						String oj2 = v[4];
						String oj3 = v[5];
						String oj6 =oj+","+oj1+","+oj2+","+oj3;						
						od5.add(oj6);

						if(row6==0) {
						Label label1 = new Label(0, row6, oj, cellFormats1);
						Label label2 = new Label(1, row6, oj1, cellFormats1);
						Label label3 = new Label(2, row6, oj2, cellFormats1);
						Label label4 = new Label(3, row6, oj3, cellFormats1);
						
						writableSheet6.addCell(label1);
						writableSheet6.addCell(label2);
						writableSheet6.addCell(label3);
						writableSheet6.addCell(label4);
						++row6;
						}
						else
						{
							Label label1 = new Label(0, row6, oj, cellFormats);
							Label label2 = new Label(1, row6, oj1, cellFormats);
							Label label3 = new Label(2, row6, oj2, cellFormats);
							Label label4 = new Label(3, row6, oj3, cellFormats);
							
							writableSheet6.addCell(label1);
							writableSheet6.addCell(label2);
							writableSheet6.addCell(label3);
							writableSheet6.addCell(label4);
							++row6;
						}

					}
				}
				}


		//Compare od
			
			for(int x = 0; x < od.size(); x++) {
				Object s1 = od.get(x);
				String ow = s1.toString();
				sheetName="Contexts";
				if (nd.contains(ow)) {
							
					} 
					else {
					
						cellFormats = new WritableCellFormat();
						cellFormats.setBackground(Colour.LIGHT_ORANGE);
					
						String input="old";
						String comment1="Removed";
					    ++row13;
				
						Label label1 = new Label(0, row13,sheetName , cellFormats);
						Label label2 = new Label(1, row13, comment1 , cellFormats);
						Label label3 = new Label(2, row13,input , cellFormats);
						Label label4 = new Label(3, row13, ow, cellFormats);
						
						writableSheet13.addCell(label1);
						writableSheet13.addCell(label2);
						writableSheet13.addCell(label3);
						writableSheet13.addCell(label4);

					}
			}
			
			for (int i = 0; i<nd.size(); i++) {
	
			Object s2 = nd.get(i);
			String nw = s2.toString();
			if (od.contains(nw)) {
					
			} 
			else {
			
				cellFormats = new WritableCellFormat();
				cellFormats.setBackground(Colour.LIGHT_TURQUOISE);			
				String input="new";
				String comment1="New/Added";
				++row13;
				Label label1 = new Label(0, row13,sheetName , cellFormats);
				Label label2 = new Label(1, row13, comment1, cellFormats);
				Label label3 = new Label(2, row13, input, cellFormats);
				Label label4 = new Label(3, row13, nw, cellFormats);

			    writableSheet13.addCell(label1);
			    writableSheet13.addCell(label2);
				writableSheet13.addCell(label3);
				writableSheet13.addCell(label4);
				
			}
			}
			
	//od1

			for(int x = 0; x < od1.size(); x++) {
				Object s1 = od1.get(x);
				String ow1 = s1.toString();
				sheetName="Objects";
				if (nd1.contains(ow1)) {
							
					} 
					else {
					
						cellFormats = new WritableCellFormat();
						cellFormats.setBackground(Colour.LIGHT_ORANGE);
					
						String comment1="Removed";
						String input="old";
					    ++row14;
					
						Label label1 = new Label(0, row14,sheetName , cellFormats);
						Label label2 = new Label(1, row14, comment1 , cellFormats);
						Label label3 = new Label(2, row14,input, cellFormats);
						Label label4 = new Label(3, row14, ow1, cellFormats);

						writableSheet14.addCell(label1);
						writableSheet14.addCell(label2);
						writableSheet14.addCell(label3);
						writableSheet14.addCell(label4);
					
					}
			}
			
			for (int i = 0; i<nd1.size(); i++) {
	
			Object s2 = nd1.get(i);
			String nw1 = s2.toString();
			if (od1.contains(nw1)) {
					
			} 
			else {
				
				cellFormats = new WritableCellFormat();
				cellFormats.setBackground(Colour.LIGHT_TURQUOISE);
				++row14;
				String input="new";
				String comment1="New/Added";

				Label label1 = new Label(0, row14,sheetName , cellFormats);
				Label label2 = new Label(1, row14, comment1 , cellFormats);
				Label label3 = new Label(2, row14, input, cellFormats);
				Label label4 = new Label(3, row14, nw1, cellFormats);

			    writableSheet14.addCell(label1);
				writableSheet14.addCell(label2);
				writableSheet14.addCell(label3);
				writableSheet14.addCell(label4);
				
			}
			}

			//od2
			
			for(int x = 0; x < od2.size(); x++) {
				Object s1 = od2.get(x);
				String ow2 = s1.toString();
				sheetName="Classes";  
				if (nd2.contains(ow2)) {
							
					} 
					else {
						
						cellFormats = new WritableCellFormat();
						cellFormats.setBackground(Colour.LIGHT_ORANGE);

						String comment1="Removed";
						String input="old";
					    ++row15;
					
						Label label1 = new Label(0, row15,sheetName , cellFormats);
						Label label2 = new Label(1, row15, comment1 , cellFormats);
						Label label3 = new Label(2, row15,input , cellFormats);
						Label label4 = new Label(3, row15, ow2, cellFormats);

						writableSheet15.addCell(label1);
						writableSheet15.addCell(label2);
						writableSheet15.addCell(label3);
						writableSheet15.addCell(label4);

					}
			}
			
			for (int i = 0; i<nd2.size(); i++) {
	
			Object s2 = nd2.get(i);
			String nw2 = s2.toString();
			if (od2.contains(nw2)) {
				
			} 
			else {
				
				cellFormats = new WritableCellFormat();
				cellFormats.setBackground(Colour.LIGHT_TURQUOISE);
				
				++row15;
				String input="new";
				String comment1="New/Added";

				Label label1 = new Label(0, row15,sheetName , cellFormats);
				Label label2 = new Label(1, row15, comment1 , cellFormats);
				Label label3 = new Label(2, row15, input, cellFormats);
				Label label4 = new Label(3, row15, nw2, cellFormats);

			    writableSheet15.addCell(label1);
				writableSheet15.addCell(label2);
				writableSheet15.addCell(label3);
				writableSheet15.addCell(label4);
				
			}
			}

	//od3
			
			for(int x = 0; x < od3.size(); x++) {
				Object s1 = od3.get(x);
				String ow3 = s1.toString();
				sheetName="Conditions";
				if (nd3.contains(ow3)) {
							
					} 
					else {
						
						cellFormats = new WritableCellFormat();
						cellFormats.setBackground(Colour.LIGHT_ORANGE);
						String comment1="Removed";
						String input="old";
						++row16;
					
						Label label1 = new Label(0, row16,sheetName , cellFormats);
						Label label2 = new Label(1, row16, comment1 , cellFormats);
						Label label3 = new Label(2, row16, input, cellFormats);
						Label label4 = new Label(3, row16, ow3, cellFormats);

						writableSheet16.addCell(label1);
						writableSheet16.addCell(label2);
						writableSheet16.addCell(label3);
						writableSheet16.addCell(label4);

					}
			}
			
			for (int i = 0; i<nd3.size(); i++) {
	
			Object s2 = nd3.get(i);
			String nw3 = s2.toString();
			if (od3.contains(nw3)) {
	
			} 
			else {
				
				cellFormats = new WritableCellFormat();
				cellFormats.setBackground(Colour.LIGHT_TURQUOISE);
				String input="new";
				String comment1="New/Added";
				++row16;
				
				Label label1 = new Label(0, row16, sheetName , cellFormats);
				Label label2 = new Label(1, row16, comment1 , cellFormats);
				Label label3 = new Label(2, row16, input, cellFormats);
				Label label4 = new Label(3, row16, nw3, cellFormats);

			    writableSheet16.addCell(label1);
				writableSheet16.addCell(label2);
				writableSheet16.addCell(label3);
				writableSheet16.addCell(label4);
				
			}
			}

	//od4
	
			for(int x = 0; x < od4.size(); x++) {
				Object s1 = od4.get(x);
				String ow4 = s1.toString();
				sheetName="Joins";
				if (nd4.contains(ow4)) {
							
					} 
					else {
						
						cellFormats = new WritableCellFormat();
						cellFormats.setBackground(Colour.LIGHT_ORANGE);
						String comment1="Removed";
						String input="old";
						++row17;
					
						Label label1 = new Label(0, row17, sheetName , cellFormats);
						Label label2 = new Label(1, row17, comment1 , cellFormats);
						Label label3 = new Label(2, row17, input, cellFormats);
						Label label4 = new Label(3, row17, ow4, cellFormats);

						writableSheet17.addCell(label1);
						writableSheet17.addCell(label2);
						writableSheet17.addCell(label3);
						writableSheet17.addCell(label4);

					}
			}
			
			for (int i = 0; i<nd4.size(); i++) {
	
			Object s2 = nd4.get(i);
			String nw4 = s2.toString();
			if (od4.contains(nw4)) {
			       } 
			else {
				
				cellFormats = new WritableCellFormat();
				cellFormats.setBackground(Colour.LIGHT_TURQUOISE);
				++row17;
				String input="new";
				String comment1="New/Added";

				Label label1 = new Label(0, row17, sheetName , cellFormats);
				Label label2 = new Label(1, row17, comment1 , cellFormats);
				Label label3 = new Label(2, row17, input, cellFormats);
				Label label4 = new Label(3, row17, nw4, cellFormats);

			    writableSheet17.addCell(label1);
				writableSheet17.addCell(label2);
				writableSheet17.addCell(label3);
				writableSheet17.addCell(label4);
				
			}
			}

			//od5
			
			for(int x = 0; x < od5.size(); x++) {
				Object s1 = od5.get(x);
				String ow5 = s1.toString();
				sheetName="Incompatibilities";
				if (nd5.contains(ow5)) {
							
					} 
					else {
						
						cellFormats = new WritableCellFormat();
						cellFormats.setBackground(Colour.LIGHT_ORANGE);
						
						String comment1="Removed";
						String input="old";
					    ++row18;
					
						Label label1 = new Label(0, row18,sheetName , cellFormats);
						Label label2 = new Label(1, row18, comment1 , cellFormats);
						Label label3 = new Label(2, row18, input, cellFormats);
						Label label4 = new Label(3, row18, ow5, cellFormats);

						writableSheet18.addCell(label1);
						writableSheet18.addCell(label2);
						writableSheet18.addCell(label3);
						writableSheet18.addCell(label4);

					}
			}
			
			for (int i = 0; i<nd5.size(); i++) {
	
			Object s2 = nd5.get(i);
			String nw5 = s2.toString();
			if (od5.contains(nw5)) {
				
			} 
			else {
				
				cellFormats = new WritableCellFormat();
				cellFormats.setBackground(Colour.LIGHT_TURQUOISE);
				
				++row18;
				String input="new";
				String comment1="New/Added";

				Label label1 = new Label(0, row18,sheetName , cellFormats);
				Label label2 = new Label(1, row18, comment1 , cellFormats);
				Label label3 = new Label(2, row18, input, cellFormats);
				Label label4 = new Label(3, row18, nw5, cellFormats);

			    writableSheet18.addCell(label1);
				writableSheet18.addCell(label2);
				writableSheet18.addCell(label3);
				writableSheet18.addCell(label4);
				
			}
			}

					writableWorkbook.write();
					writableWorkbook.close();
	
					System.out.println("Writing Completed");
					
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	}

