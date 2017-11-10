package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SaveData {
    public static void savedata(String[] newdata, String filename) throws IOException, InvalidFormatException {

	if (new File(filename).exists()) {
	    FileInputStream inp = new FileInputStream(new File(filename));
	    XSSFWorkbook workbook = new XSSFWorkbook(inp);

	    inp.close();
	    XSSFSheet sheet = workbook.getSheetAt(0);
	    int rowN = sheet.getLastRowNum();
	    Row lastrow = sheet.getRow(rowN);
	    // System.out.println(lastrow.getCell(1).getStringCellValue().toString());
	    // System.out.println(newdata[1].toString());
	    if (!lastrow.getCell(0).getStringCellValue().toString().equals(newdata[0].toString())) {
		Row row = sheet.createRow(rowN + 1);
		for (int i = 0; i < newdata.length; i++) {
		    Cell cell = row.createCell(i);
		    if (newdata[i] == null) {
			cell.setCellValue("null");
		    } else {
			cell.setCellValue(newdata[i].toString());
		    }

		}

		FileOutputStream out = new FileOutputStream(new File(filename));
		workbook.write(out);
		workbook.close();
		out.close();
	    }

	} else {
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet();
	    Row row0 = sheet.createRow(0);

	    String columnname[] = { "时间", "水位", "水势", "比昨日涨落", "流量", "设防水位", "警戒水位", "最高水位" };
	    for (int i = 0; i < columnname.length; i++) {
		Cell cell = row0.createCell(i);
		cell.setCellValue(columnname[i]);
	    }

	    Row row1 = sheet.createRow(1);
	    for (int i = 0; i < newdata.length; i++) {
		Cell cell = row1.createCell(i);
		cell.setCellValue(newdata[i]);
	    }
	    FileOutputStream out = new FileOutputStream(new File(filename));
	    workbook.write(out);
	    workbook.close();
	    out.close();
	}


    }

}
