package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.example.demo.entity.OrderList;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExcelGenerator {

    public void generateExcel(List<OrderList> orders, HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Orders");


        HSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Order ID");
        headerRow.createCell(1).setCellValue("Product Name");
        headerRow.createCell(2).setCellValue("Price");
        headerRow.createCell(3).setCellValue("Quantity");

 
        int rowIndex = 1;
        for (OrderList order : orders) {
            Row dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(0).setCellValue(order.getOrderId());
            dataRow.createCell(1).setCellValue(order.getProductName());
            dataRow.createCell(2).setCellValue(order.getPrice());
            dataRow.createCell(3).setCellValue(order.getQuantity());
        }


        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=orders.xls");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    
    private byte[] getInvoiceAsByteArray(OrderList order) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Invoice");


            HSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Product Name");
            headerRow.createCell(1).setCellValue("Price");
            headerRow.createCell(2).setCellValue("Quantity");

            
            HSSFRow dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(order.getProductName());
            dataRow.createCell(1).setCellValue(order.getPrice());
            dataRow.createCell(2).setCellValue(order.getQuantity());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }
}

