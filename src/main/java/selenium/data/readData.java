package selenium.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;

public class readData {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String excelPath = System.getProperty("user.dir")+"\\src\\main\\java\\selenium\\data\\data.xlsx";
		
		
			FileInputStream fis = new FileInputStream(new File(excelPath));
			
			 Workbook workbook = new XSSFWorkbook(fis);
			 Sheet sh = workbook.getSheet("Sheet1");
			 int rowCount = sh.getPhysicalNumberOfRows();
			 
			 for(int i=0;i<rowCount;i++) {
				 Row row = sh.getRow(i);
				 int colCount = row.getPhysicalNumberOfCells();
				 
				 for(int j=0;j< colCount;j++) {
					 Cell cell = row.getCell(j);
					 
					 switch (cell.getCellType()) {
					 
					 case STRING:
						 System.out.print(cell.getStringCellValue()+ "\t");
						 break;
					 case NUMERIC:
						 System.out.print(cell.getNumericCellValue()+"\t");
						 break;
					 case BOOLEAN:
						 System.out.print(cell.getBooleanCellValue()+"\t");
						 break;
					default:
						System.out.print("unknown\t");
						 
					 }
			 }
				 System.out.println();
			 
			 
			 }
			 workbook.close();
			 fis.close();
		
		
		
		String FromStn = sh.getRow(1).getCell(0).getStringCellValue();
		String ToStn = sh.getRow(1).getCell(1).getStringCellValue();
		
		System.out.println("fromStn: "+ FromStn+" to Stn: "+ ToStn);
		
	}
	public 
}
