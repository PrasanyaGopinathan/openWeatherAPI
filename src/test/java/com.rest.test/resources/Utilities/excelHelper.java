package com.rest.test.resources.Utilities;
import java.util.*;
import java.io.*;
import jxl.*;
import java.lang.*;
import jxl.read.biff.BiffException;

public class excelHelper {

        public void readExcel() throws IOException, BiffException
        {
            String fileLocation;
            System.out.println("@@@@@@@@@@@@@");
            List<Map<String,String>> data = new ArrayList<Map<String, String>>();
 fileLocation="/Users/prasanyagopinathan/Documents/Dev/openWeatherStation/src/test/java/com.rest.test/resources/testdata/postRequest.xlsx";
                   Workbook workbook = Workbook.getWorkbook(new File(fileLocation));
            Sheet  sheet = workbook.getSheet(0);
                   int rows = sheet.getRows();
System.out.println("Rows"+rows);

                   //int colummns = sheet.getColumns();
                   for (int i=1; i <= rows; i++){

            Map<String,String> fields = new HashMap<String, String>();
                   for (int j = 1; j < 6; j++) {

                  fields.put(sheet.getCell(i,j).toString(), sheet.getCell(i,1).toString());

                   }
            data.add(fields);}

    }
}


