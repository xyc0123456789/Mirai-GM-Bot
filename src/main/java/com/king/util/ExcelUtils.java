package com.king.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import java.awt.Color;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Slf4j
public class ExcelUtils {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    private static Logger logger = Logger.getLogger(String.valueOf(ExcelUtils.class));


//    public static void exportExcel(HttpServletResponse response, String fileName, ExcelData data) throws Exception {
//        // 告诉浏览器用什么软件可以打开此文件
//        response.setHeader("content-Type", "application/vnd.ms-excel");
//        // 下载文件的默认名称
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
//        exportExcel(data, response.getOutputStream());
//    }


//    public static void exportExcel(HttpServletResponse response, String fileName, List<ExcelData> data) throws Exception {
//        // 告诉浏览器用什么软件可以打开此文件
//        response.setHeader("content-Type", "application/vnd.ms-excel");
//        // 下载文件的默认名称
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
//        exportExcel(data, response.getOutputStream());
//    }

    public static File generateExcel(ExcelData excelData, String path) throws Exception {
        File f = new File(path);
        if (!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        FileOutputStream out = new FileOutputStream(f);
        exportExcel(excelData, out);
        return f;
    }

    public static int generateExcel(List<ExcelData> excelData, String path) throws Exception {
        File f = new File(path);
        FileOutputStream out = new FileOutputStream(f);
        return exportExcel(excelData, out);
    }

    public static InputStream getStream(ExcelData data) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data);
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //此处需要关闭 wb 变量
            out.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static InputStream getStream(List<ExcelData> list) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            int i = 1;
            for (ExcelData data : list) {
                String sheetName = data.getName();
                if (null == sheetName) {
                    sheetName = "Sheet" + i++;
                }
                XSSFSheet sheet = wb.createSheet(sheetName);
                writeExcel(wb, sheet, data);
            }
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //此处需要关闭 wb 变量
            out.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static int exportExcel(List<ExcelData> excelData, OutputStream out) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        int rowIndex = 0;
        try {
            int i = 1;
            for (ExcelData data : excelData) {
                String sheetName = data.getName();
                if (null == sheetName) {
                    sheetName = "Sheet" + i++;
                }
                XSSFSheet sheet = wb.createSheet(sheetName);
                rowIndex = writeExcel(wb, sheet, data);
            }
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //此处需要关闭 wb 变量
            out.close();
        }
        return rowIndex;
    }

    private static int exportExcel(ExcelData data, OutputStream out) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        int rowIndex = 0;
        try {
            String sheetName = data.getName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            rowIndex = writeExcel(wb, sheet, data);
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //此处需要关闭 wb 变量
            out.close();
        }
        return rowIndex;
    }

    /**
     * 表不显示字段
     * @param wb
     * @param sheet
     * @param data
     * @return
     */
//    private static int writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data) {
//        int rowIndex = 0;
//        writeTitlesToExcel(wb, sheet, data.getTitles());
//        rowIndex = writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
//        autoSizeColumns(sheet, data.getTitles().size() + 1);
//        return rowIndex;
//    }

    /**
     * 表显示字段
     *
     * @param wb
     * @param sheet
     * @param data
     * @return
     */
    private static int writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data) {
        int rowIndex = 0;
        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles(), data.getMergeName());
        rowIndex = writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
        autoSizeColumns(sheet, data.getTitles().size() + 1);
        return rowIndex;
    }

    /**
     * 设置表头
     *
     * @param wb
     * @param sheet
     * @param titles
     * @return
     */
    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles, String mergeContent) {
        int rowIndex = 0;
        int colIndex = 0;
        Font titleFont = wb.createFont();
        //设置字体
        titleFont.setFontName("simsun");
        //设置粗体
//        titleFont.setBoldweight(Short.MAX_VALUE);
        titleFont.setBold(true);
        //设置字号
        titleFont.setFontHeightInPoints((short) 14);
        //设置颜色
        titleFont.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle titleStyle = wb.createCellStyle();
        //水平居中
//        titleStyle.setAlignment(XSSFCellStyle/);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        //垂直居中
//        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置图案颜色
        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
        //设置图案样式
//        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        titleStyle.setFont(titleFont);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        if (mergeContent != null && mergeContent.length()>1) {
            mergerStyle(wb,sheet,mergeContent,titles.size()-1);
            rowIndex++;
        }
        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeightInPoints(25);
        colIndex = 0;
        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }
        rowIndex++;
        return rowIndex;
    }

    /**
     * 头部合并样式
     * @param wb
     * @param sheet
     * @param mergeContent
     * @param size
     */
    private static void mergerStyle(XSSFWorkbook wb, Sheet sheet,String mergeContent,int size){
        Font titleFont = wb.createFont();
        //设置字体
        titleFont.setFontName("simsun");
        //设置粗体
//        titleFont.setBoldweight(Short.MAX_VALUE);
        titleFont.setBold(true);
        //设置字号
        titleFont.setFontHeightInPoints((short) 14);
        //设置颜色
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle mergerStyle = wb.createCellStyle();
        //水平居中
        mergerStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        mergerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置图案颜色
        mergerStyle.setFillForegroundColor(new XSSFColor(new Color(212, 212, 212)));
        //设置图案样式

//        mergerStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        mergerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        mergerStyle.setFont(titleFont);
        setBorder(mergerStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        Row row0 = sheet.createRow(0);//第一行
        row0.setHeight((short) ((short) 30 * 20));//设置行高
        Cell cell = row0.createCell(0);
        cell.setCellValue(mergeContent);//设置单元格内容
        cell.setCellStyle(mergerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, size));//起始行，截至行，起始列， 截至列
    }

    /**
     * 设置内容
     *
     * @param wb
     * @param sheet
     * @param rows
     * @param rowIndex
     * @return
     */
    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex;
        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
//        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
//        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        dataStyle.setFont(dataFont);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            dataRow.setHeightInPoints(25);
            colIndex = 0;
            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    /**
     * 自动调整列宽
     *
     * @param sheet
     * @param columnNumber
     */
    private static void autoSizeColumns(Sheet sheet, int columnNumber) {
        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    /**
     * 设置边框
     *
     * @param style
     * @param border
     * @param color
     */
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(BorderSide.TOP, color);
        style.setBorderColor(BorderSide.LEFT, color);
        style.setBorderColor(BorderSide.RIGHT, color);
        style.setBorderColor(BorderSide.BOTTOM, color);
    }

//    public static List<String[]> readExcel(MultipartFile file) throws IOException {
//        //检查文件
//        checkFile(file);
//        //获得Workbook工作薄对象
//        Workbook workbook = getWorkBook(file);
//
//        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
//        List<String[]> list = new ArrayList<String[]>();
//        if (workbook != null) {
//            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
//                //获得当前sheet工作表
//                Sheet sheet = workbook.getSheetAt(sheetNum);
//                if (sheet == null) {
//                    continue;
//                }
//                //获得当前sheet的开始行
//                int firstRowNum = sheet.getFirstRowNum();
//                //获得当前sheet的结束行
//                int lastRowNum = sheet.getLastRowNum();
//                //循环除了第一行的所有行
//                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
//                    //获得当前行
//                    Row row = sheet.getRow(rowNum);
//                    if (row == null) {
//                        continue;
//                    }
//                    //获得当前行的开始列
//                    int firstCellNum = row.getFirstCellNum();
//                    //获得当前行的列数
//                    int lastCellNum = row.getPhysicalNumberOfCells();
//                    String[] cells = new String[row.getPhysicalNumberOfCells()];
//                    //循环当前行
//                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
//                        Cell cell = row.getCell(cellNum);
//                        cells[cellNum] = getCellValue(cell);
//                    }
//                    list.add(cells);
//                }
//            }
//        }
//        return list;
//    }

    public static List<String[]> readExcel(InputStream is, String fileType) throws IOException {

        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(is, fileType);

        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[row.getLastCellNum()];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
        }
        return list;
    }

//    public static void checkFile(MultipartFile file) throws IOException {
//        //判断文件是否存在
//        if (null == file) {
//            log.info("文件不存在！");
//            throw new FileNotFoundException("文件不存在！");
//        }
//        //获得文件名
//        String fileName = file.getOriginalFilename();
//        //判断文件是否是excel文件
//        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
//            log.info(fileName + "不是excel文件");
//            throw new IOException(fileName + "不是excel文件");
//        }
//    }
//
//    public static Workbook getWorkBook(MultipartFile file) {
//        //获得文件名
//        String fileName = file.getOriginalFilename();
//        //创建Workbook工作薄对象，表示整个excel
//        Workbook workbook = null;
//        try {
//            //获取excel文件的io流
//            InputStream is = file.getInputStream();
//            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
//            if (fileName.endsWith(xls)) {
//                //2003
//                workbook = new HSSFWorkbook(is);
//            } else if (fileName.endsWith(xlsx)) {
//                //2007
//                workbook = new XSSFWorkbook(is);
//            }
//            is.close();
//
//        } catch (IOException e) {
//            logger.info(e.getMessage());
//        }
//        return workbook;
//    }

    public static Workbook getWorkBook(InputStream is, String fileType) {

        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {


            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileType.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileType.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
            is.close();

        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况

        if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
            cell.setCellType(CellType.STRING);

        }
        //判断数据的类型
        String name = cell.getCellType().name();
        switch (name) {

            case "NUMERIC": //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case "STRING": //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case "BOOLEAN": //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case "FORMULA": //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case "BLANK": //空值
                cellValue = "";
                break;
            case "ERROR": //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static void main(String args[]) {


    }
}
