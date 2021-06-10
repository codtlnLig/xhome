//
//package net.swa.util.io;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import net.swa.util.bean.Model;
//import net.swa.util.io.enums.MsOfficeVersion;
//import org.apache.commons.beanutils.BeanMap;
//import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFDataFormat;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//
///**
// * Excel工具类
// * Excel工具类，通过POI操作<p>
// * [功能详细描述]<p>
// */
//public class ExcelUtil
//{
//    /**
//    * Logger for this class
//    */
//    private static final Logger log = Logger.getLogger(ExcelUtil.class);
//
//    private static final String DEFAULT_SHEET_NAME = "Sheet";
//
//    private static final String SEPARATOR = ".";
//
//    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
//
//    /**
//     * 多重对象的属性分割符
//     */
//    public static final String REGEX_FIELD_SEPARATOR = "\\u002E";
//
//    private static String getFileName(String fileName , MsOfficeVersion officeVersion)
//    {
//        return fileName + SEPARATOR + officeVersion.getSuffix();
//    }
//
//    /**
//     * 设置单元格的值，以及单元格格式（只有日期才设置格式）
//     * @param cell 单元格
//     * @param dateformatStyle 日期单元格格式
//     * @param alignStyle 水平位置格式
//     * @param value 值
//     */
//    private static void setCell(HSSFCell cell , HSSFCellStyle dateformatStyle , HSSFCellStyle alignStyle , Object value)
//    {
//        if (null == value)
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue("");
//        }
//        else if (value instanceof String)
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue(value.toString());
//        }
//        else if (value instanceof Integer)
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue(((Integer) value).intValue());
//        }
//        else if (value instanceof Double)
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue(((Double) value).doubleValue());
//        }
//        else if (value instanceof Short)
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue(((Short) value).shortValue());
//        }
//        else if (value instanceof Date)
//        {
//            cell.setCellStyle(dateformatStyle);
//            cell.setCellValue(((Date) value));
//        }
//        else if (value instanceof Long)
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue(((Long) value).longValue());
//        }
//        else
//        {
//            cell.setCellStyle(alignStyle);
//            cell.setCellValue(value.toString());
//        }
//    }
//
//    /**
//     * 支持多个sheet页和模版
//     * 将信息导出为Excel文件
//     * @param input 需要写入Excel文件的信息
//     * @param rowBegin 开始行数，从0开始
//     * @param colBegin 开始列数，从0开始
//     * @param outExcelFileName 导出目标Excel文件，带路径但不带后缀名
//     * @param sheetName Excel的Sheet名字，如果为空，则采用默认的Sheet名，即Sheet1
//     * @param replaceFile 如果导出目标文件已经存在，是否替换，true表示替换原有文件，false表示不替换原有文件
//     * @param templateExcelFileName Excel文件模板，带路径但不带后缀名。如果为空，则表示不使用模板。当目标文件已经存在，且replaceFile为false时，此参数不起作用。
//     * @param officeVersion Office版本，目前只支持2003版本
//     * @param datePattern 日期格式字符串，如果为空，则默认为yyyy-MM-dd
//     * @return 写入的文件名
//     * @throws Exception 
//     */
//    public static String writeToExcelFile(Object[][][] input , int[] rowBegin , int[] colBegin ,
//            String outExcelFileName , String[] sheetName , boolean[] replaceFile , String[] templateExcelFileName ,
//            MsOfficeVersion officeVersion , String datePattern) throws Exception
//    {
//        if ((null == outExcelFileName) || (0 == outExcelFileName.trim().length()))
//        {
//            //抛出文件名为空的系统异常
//            throw new Exception("文件名为空");
//        }
//        String outFileName = getFileName(outExcelFileName.trim(), officeVersion);
//        File outFile = new File(outFileName);
//
//        boolean templateExcelFileExist = false;
//        File templateFile = null;
//        String templateName = null;
//
//        FileOutputStream output = null;
//        FileInputStream fs = null;
//        //HSSFWorkbook对象
//        HSSFSheet sheet = null;
//        HSSFWorkbook wb = null;
//        try
//        {
//            for (int i = 0; i < input.length; i++)
//            {
//                if ((null != templateExcelFileName[i]) && (0 < templateExcelFileName[i].trim().length()))
//                {
//
//                    templateName = getFileName(templateExcelFileName[i].trim(), officeVersion);
//                    templateFile = new File(templateName.trim());
//                    if (templateFile.exists())
//                    {
//                        templateExcelFileExist = true;
//                    }
//                }
//
//                String tempSheetName = DEFAULT_SHEET_NAME + (i + 1);//默认sheet名称
//                if ((null != sheetName[i]) && (0 < sheetName[i].trim().length()))
//                {
//                    tempSheetName = sheetName[i];
//                }
//                if ((false == replaceFile[i]) && (outFile.exists()))
//                {
//                    //如果不需要替换且输出目标文件已经存在，使用输出目标文件创建POI文件对象
//                    fs = new FileInputStream(outFile);
//                    POIFSFileSystem ps = new POIFSFileSystem(fs);
//                    wb = new HSSFWorkbook(ps);
//                    sheet = wb.getSheetAt(i);
//                }
//                else if (templateExcelFileExist)
//                {
//                    //如果目标文件不存在，或者需要替换，且模板文件存在，则使用模板文件创建POI文件对象
//                    IOUitl.copy(templateName, outFileName, true);
//                    fs = new FileInputStream(outFileName);
//                    POIFSFileSystem ps = new POIFSFileSystem(fs);
//                    wb = new HSSFWorkbook(ps);
//                    sheet = wb.getSheetAt(i);
//                }
//                else
//                {
//                    //除上述情况外，直接创建HSSFWorkbook对象
//                    wb = new HSSFWorkbook();
//                    sheet = wb.createSheet(tempSheetName);
//                }
//                HSSFCellStyle dateCellStyle = wb.createCellStyle();
//                dateCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//                HSSFDataFormat format = wb.createDataFormat();
//                String tempPattern = datePattern;
//                if (null == tempPattern)
//                {
//                    tempPattern = DEFAULT_DATE_PATTERN;
//                }
//                dateCellStyle.setDataFormat(format.getFormat(tempPattern));
//                HSSFCellStyle alignStyle = wb.createCellStyle();
//                alignStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//                //如果input[i]为空，则在创建Excel文件中不写入任何内容
//                if ((null != input[i]) && (0 < input[i].length) && (0 < input[i][0].length))
//                {
//                    int rowLength = input[i].length;
//                    int columnLength = input[i][0].length;
//                    for (int x = 0; x < rowLength; x++)
//                    {
//                        HSSFRow row = sheet.getRow(x + rowBegin[i]);
//                        if (null == row)
//                        {
//                            //建立新行
//                            row = sheet.createRow(x + rowBegin[i]);
//                        }
//                        if (null == input[i][x])
//                        {
//                            continue;
//                        }
//                        for (int j = 0; j < columnLength; j++)
//                        {
//                            //设置cell的值
//                            HSSFCell createCell = row.createCell(j + colBegin[i]);
//                            setCell(createCell, dateCellStyle, alignStyle, input[i][x][j]);
//                        }
//                    }
//                }
//
//            }
//            output = new FileOutputStream(outFile);
//            output.flush();
//            wb.write(output);
//            if (log.isDebugEnabled())
//            {
//                log.debug("成功将条记录写入Excel文件");
//            }
//            return outFileName;
//        }
//        finally
//        {
//            IOUitl.closeWithWarnLog(output);
//            IOUitl.closeWithWarnLog(fs);
//        }
//    }
//
//    /**
//     * 将信息导出为Excel文件
//     * @param list 需要写入Excel文件的对象列表
//     * @param fieldNames 需要从对象中获取的属性名称
//     * @param rowBegin 开始行数，从0开始
//     * @param colBegin 开始列数，从0开始
//     * @param outExcelFileName 导出目标Excel文件，带路径但不带后缀名
//     * @param sheetName Excel的Sheet名字，如果为空，则采用默认的Sheet名，即Sheet1
//     * @param replaceFile 如果导出目标文件已经存在，是否替换，true表示替换原有文件，false表示不替换原有文件
//     * @param templateExcelFileName Excel文件模板，带路径但不带后缀名。如果为空，则表示不使用模板。当目标文件已经存在，且replaceFile为false时，此参数不起作用。
//     * @param officeVersion Office版本，目前只支持2003版本
//     * @param datePattern 日期格式字符串，如果为空，则默认为yyyy-MM-dd
//     * @return 写入的文件名
//     * @throws Exception 
//     */
//    public static String writeToExcelFile(List<List<Object>> list , String[][] fieldNames , int[] rowBegin ,
//            int[] colBegin , String outExcelFileName , String[] sheetName , boolean[] replaceFile ,
//            String[] templateExcelFileName , MsOfficeVersion officeVersion , String datePattern) throws Exception
//    {
//        if ((null == list) || (0 == list.size()) || (null == fieldNames) || (0 == fieldNames.length))
//        {
//            return writeToExcelFile(null, rowBegin, colBegin, outExcelFileName, sheetName, replaceFile,
//                    templateExcelFileName, officeVersion, datePattern);
//        }
//        else
//        {
//            Object[][][] input = getObjArr(list, fieldNames);
//            for (int f = 0; f < input.length; f++)
//            {
//                for (int i = 0; i < input[f].length; i++)
//                {
//                    Map<String, Object> fieldsMap = getPropertyObjectMap(list.get(f).get(i), true);
//                    if (null != fieldsMap)
//                    {
//                        for (int j = 0; j < fieldNames[f].length; j++)
//                        {
//                            input[f][i][j] = fieldsMap.get(fieldNames[f][j]);
//                        }
//                    }
//                }
//            }
//            return writeToExcelFile(input, rowBegin, colBegin, outExcelFileName, sheetName, replaceFile,
//                    templateExcelFileName, officeVersion, datePattern);
//        }
//    }
//
//    private static Object[][][] getObjArr(List<List<Object>> list , String[][] fn)
//    {
//        int k = 0;
//        int j = 0;
//        for (int m = 0; m < list.size(); m++)
//        {
//            if (j < list.get(m).size())
//            {
//                j = list.get(m).size();
//            }
//        }
//        for (int n = 0; n < fn.length; n++)
//        {
//            if (k < fn[n].length)
//            {
//                k = fn[n].length;
//            }
//        }
//        return new Object[list.size()][j][k];
//    }
//
//    public static Map<String, Object> getPropertyObjectMap(Object obj , boolean ignoreNull , String prefix)
//    {
//        Map<String, Object> resultMap = null;
//        if (null != obj)
//        {
//            String tempPrefix = prefix;
//            if (null == tempPrefix)
//            {
//                tempPrefix = "";
//            }
//            resultMap = new HashMap<String, Object>();
//            BeanMap beanMap = new BeanMap(obj);
//            Set<?> keySet = beanMap.keySet();
//            if ((null != keySet) && (0 < keySet.size()))
//            {
//                Iterator<?> it = keySet.iterator();
//                while (it.hasNext())
//                {
//                    String key = (String) it.next();
//                    String className = beanMap.getType(key).getName();
//                    //过滤掉Class属性
//                    if (Class.class.getName().equals(className))
//                    {
//                        continue;
//                    }
//                    Object value = beanMap.get(key);
//                    if (null == value)
//                    {
//                        if (ignoreNull)
//                        {
//                            continue;
//                        }
//                        else
//                        {
//                            resultMap.put(tempPrefix + key, null);
//                        }
//                    }
//                    else if ((Byte.class.getName().equals(className)) || (byte.class.getName().equals(className))
//                            || (Short.class.getName().equals(className)) || (short.class.getName().equals(className))
//                            || (Integer.class.getName().equals(className)) || (int.class.getName().equals(className))
//                            || (Long.class.getName().equals(className)) || (long.class.getName().equals(className))
//                            || (Float.class.getName().equals(className)) || (float.class.getName().equals(className))
//                            || (Double.class.getName().equals(className)) || (double.class.getName().equals(className))
//                            || (String.class.getName().equals(className))
//                            || (StringBuffer.class.getName().equals(className)))
//                    {
//                        resultMap.put(tempPrefix + key, value);
//                    }
//                    else if (value instanceof Date)
//                    {
//                        resultMap.put(tempPrefix + key, value);
//                    }
//                    else if (value instanceof Enum<?>)
//                    {
//                        resultMap.put(tempPrefix + key, value);
//                    }
//                    else if (value instanceof Model)
//                    {
//                        resultMap.putAll(getPropertyMap(value, ignoreNull, tempPrefix + key + "."));
//                    }
//                    else if (value instanceof Map<?, ?>)
//                    {
//                        Map<?, ?> tempMap = (Map<?, ?>) value;
//                        if (0 < tempMap.size())
//                        {
//                            for (Object objKey : tempMap.keySet())
//                            {
//                                if (null != objKey)
//                                {
//
//                                    resultMap.put(tempPrefix + key + "." + objKey.toString(), tempMap.get(objKey));
//                                }
//                            }
//                        }
//                        resultMap.putAll(getPropertyMap(value, ignoreNull, tempPrefix + key + "."));
//                    }
//                    else
//                    {
//                        continue;
//                    }
//                }
//            }
//            if (0 > resultMap.size())
//            {
//                return null;
//            }
//        }
//        return resultMap;
//    }
//
//    public static Map<String, Object> getPropertyObjectMap(Object obj , boolean ignoreNull)
//    {
//        return getPropertyObjectMap(obj, ignoreNull, null);
//    }
//
//    /**
//     * 获取属性值名和属性值，作为Map的键和值，如果是基本类型，则转为对应的包装类
//     * 支持属性为基本类型、基本类型对应的包装类、String类型、Date类型、枚举类型、属性对象的属性通过点连接
//     * 暂时不支持数组、List、Map、Set等集合类型
//     * @param obj
//     * @param ignoreNull 是否忽略null值
//     * @param prefix 前缀
//     * @return
//     */
//    public static Map<String, String> getPropertyMap(Object obj , boolean ignoreNull , String prefix)
//    {
//        Map<String, String> resultMap = null;
//        if (null != obj)
//        {
//            String tempPrefix = prefix;
//            if (null == tempPrefix)
//            {
//                tempPrefix = "";
//            }
//            resultMap = new HashMap<String, String>();
//            BeanMap beanMap = new BeanMap(obj);
//            Set<?> keySet = beanMap.keySet();
//            if ((null != keySet) && (0 < keySet.size()))
//            {
//                Iterator<?> it = keySet.iterator();
//                while (it.hasNext())
//                {
//                    String key = (String) it.next();
//                    String className = beanMap.getType(key).getName();
//                    //过滤掉Class属性
//                    if (Class.class.getName().equals(className))
//                    {
//                        continue;
//                    }
//                    Object value = beanMap.get(key);
//                    if (null == value)
//                    {
//                        if (ignoreNull)
//                        {
//                            continue;
//                        }
//                        else
//                        {
//                            resultMap.put(tempPrefix + key, null);
//                        }
//                    }
//                    else if ((Byte.class.getName().equals(className)) || (byte.class.getName().equals(className))
//                            || (Short.class.getName().equals(className)) || (short.class.getName().equals(className))
//                            || (Integer.class.getName().equals(className)) || (int.class.getName().equals(className))
//                            || (Long.class.getName().equals(className)) || (long.class.getName().equals(className))
//                            || (Float.class.getName().equals(className)) || (float.class.getName().equals(className))
//                            || (Double.class.getName().equals(className)) || (double.class.getName().equals(className))
//                            || (String.class.getName().equals(className))
//                            || (StringBuffer.class.getName().equals(className)))
//                    {
//                        resultMap.put(tempPrefix + key, value.toString());
//                    }
//                    else if (value instanceof Date)
//                    {
//                        resultMap
//                                .put(tempPrefix + key, new SimpleDateFormat(DEFAULT_DATE_PATTERN).format((Date) value));
//                    }
//                    else if (value instanceof Enum<?>)
//                    {
//                        resultMap.put(tempPrefix + key, value.toString());
//                    }
//                    //                    else if (value instanceof Model)
//                    //                    {
//                    //                        resultMap.putAll(getPropertyMap(value, ignoreNull, tempPrefix + key + "."));
//                    //                    }
//                    else if (value instanceof Map<?, ?>)
//                    {
//                        Map<?, ?> tempMap = (Map<?, ?>) value;
//                        Object objValue = null;
//                        if (0 < tempMap.size())
//                        {
//                            for (Object objKey : tempMap.keySet())
//                            {
//                                if (null != objKey)
//                                {
//                                    objValue = tempMap.get(objKey);
//                                    if (null != objValue)
//                                    {
//                                        resultMap.put(tempPrefix + key + "." + objKey.toString(), objValue.toString());
//                                    }
//                                }
//                            }
//                        }
//                        resultMap.putAll(getPropertyMap(value, ignoreNull, tempPrefix + key + "."));
//                    }
//                    else
//                    {
//                        continue;
//                    }
//                }
//            }
//            if (0 > resultMap.size())
//            {
//                return null;
//            }
//        }
//        return resultMap;
//    }
//
//    /**
//     * 获取属性值名和属性值，作为Map的键和值，如果是基本类型，则转为对应的包装类
//     * 支持属性为基本类型、基本类型对应的包装类、String类型、Date类型、枚举类型、属性对象的属性通过点连接
//     * 暂时不支持数组、List、Map、Set等集合类型
//     * @param obj
//     * @param ignoreNull 是否忽略null值
//     * @return
//     */
//    public static Map<String, String> getPropertyMap(Object obj , boolean ignoreNull)
//    {
//        return getPropertyMap(obj, ignoreNull, null);
//    }
//
//    /**
//     * 将信息导出为Excel文件
//     * @param list 需要写入Excel文件的对象列表
//     * @param fieldNames 需要从对象中获取的属性名称
//     * @param rowBegin 开始行数，从0开始
//     * @param colBegin 开始列数，从0开始
//     * @param outExcelFileName 导出目标Excel文件，带路径但不带后缀名
//     * @param sheetName Excel的Sheet名字，如果为空，则采用默认的Sheet名，即Sheet1
//     * @param replaceFile 如果导出目标文件已经存在，是否替换，true表示替换原有文件，false表示不替换原有文件
//     * @param templateExcelFileName Excel文件模板，带路径但不带后缀名。如果为空，则表示不使用模板。当目标文件已经存在，且replaceFile为false时，此参数不起作用。
//     * @param officeVersion Office版本，目前只支持2003版本
//     * @param datePattern 日期格式字符串，如果为空，则默认为yyyy-MM-dd
//     * @return 写入的文件名
//     * @throws IOException
//     * @throws SysException
//     */
//    public static HSSFWorkbook writeToExcelFile(List<Object> list , String[] fieldNames , int rowBegin , int colBegin ,
//            String outExcelFileName , String sheetName , boolean replaceFile , String templateExcelFileName ,
//            MsOfficeVersion officeVersion , String datePattern) throws Exception
//    {
//        if ((null == list) || (0 == list.size()) || (null == fieldNames) || (0 == fieldNames.length))
//        {
//            return writeToExcelFile(null, rowBegin, colBegin, outExcelFileName, sheetName, replaceFile,
//                    templateExcelFileName, officeVersion, datePattern);
//        }
//        else
//        {
//            Object[][] input = new Object[list.size()][fieldNames.length];
//            for (int i = 0; i < input.length; i++)
//            {
//                Map<String, Object> fieldsMap = getPropertyObjectMap(list.get(i), true);
//                if (null != fieldsMap)
//                {
//                    for (int j = 0; j < fieldNames.length; j++)
//                    {
//                        input[i][j] = fieldsMap.get(fieldNames[j]);
//                    }
//                }
//            }
//            return writeToExcelFile(input, rowBegin, colBegin, outExcelFileName, sheetName, replaceFile,
//                    templateExcelFileName, officeVersion, datePattern);
//        }
//    }
//
//    /**
//     * 将信息导出为Excel文件
//     * @param input 需要写入Excel文件的信息
//     * @param rowBegin 开始行数，从0开始
//     * @param colBegin 开始列数，从0开始
//     * @param outExcelFileName 导出目标Excel文件，带路径但不带后缀名
//     * @param sheetName Excel的Sheet名字，如果为空，则采用默认的Sheet名，即Sheet1
//     * @param replaceFile 如果导出目标文件已经存在，是否替换，true表示替换原有文件，false表示不替换原有文件
//     * @param templateExcelFileName Excel文件模板，带路径但不带后缀名。如果为空，则表示不使用模板。当目标文件已经存在，且replaceFile为false时，此参数不起作用。
//     * @param officeVersion Office版本，目前只支持2003版本
//     * @param datePattern 日期格式字符串，如果为空，则默认为yyyy-MM-dd
//     * @return 写入的文件名
//     * @throws IOException
//     * @throws SysException
//     */
//    public static HSSFWorkbook writeToExcelFile(Object[][] input , int rowBegin , int colBegin ,
//            String outExcelFileName , String sheetName , boolean replaceFile , String templateExcelFileName ,
//            MsOfficeVersion officeVersion , String datePattern) throws Exception
//    {
//        if ((null == outExcelFileName) || (0 == outExcelFileName.trim().length()))
//        {
//            //抛出文件名为空的系统异常
//            throw new Exception("");
//        }
//        if (log.isDebugEnabled())
//        {
//            log.debug("开始将记录写入Excel文件，共条");
//        }
//        String outFileName = getFileName(outExcelFileName.trim(), officeVersion);
//
//        File outFile = new File(outFileName);
//
//        boolean templateExcelFileExist = false;
//        File templateFile = null;
//        String templateFileName = templateExcelFileName;
//        if ((null != templateExcelFileName) && (0 < templateExcelFileName.trim().length()))
//        {
//            templateFileName = getFileName(templateExcelFileName.trim(), officeVersion);
//            templateFile = new File(templateFileName.trim());
//            if (templateFile.exists())
//            {
//                templateExcelFileExist = true;
//            }
//        }
//        //HSSFWorkbook对象
//        HSSFSheet sheet = null;
//        HSSFWorkbook wb = null;
//        String tempSheetName = DEFAULT_SHEET_NAME;
//        if ((null != sheetName) && (0 < sheetName.trim().length()))
//        {
//            tempSheetName = sheetName;
//        }
//        FileOutputStream output = null;
//        FileInputStream fs = null;
//        try
//        {
//            if ((false == replaceFile) && (outFile.exists()))
//            {
//                //如果不需要替换且输出目标文件已经存在，使用输出目标文件创建POI文件对象
//                fs = new FileInputStream(outFile);
//                POIFSFileSystem ps = new POIFSFileSystem(fs);
//                wb = new HSSFWorkbook(ps);
//                sheet = wb.getSheetAt(0);
//            }
//            else if (templateExcelFileExist)
//            {
//                //如果目标文件不存在，或者需要替换，且模板文件存在，则使用模板文件创建POI文件对象
//                IOUitl.copy(templateFileName, outFileName, true);
//                fs = new FileInputStream(outFileName);
//                POIFSFileSystem ps = new POIFSFileSystem(fs);
//                wb = new HSSFWorkbook(ps);
//                sheet = wb.getSheetAt(0);
//            }
//            else
//            {
//                //除上述情况外，直接创建HSSFWorkbook对象
//                wb = new HSSFWorkbook();
//                sheet = wb.createSheet(tempSheetName);
//            }
//            HSSFCellStyle dateCellStyle = wb.createCellStyle();
//            dateCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            HSSFDataFormat format = wb.createDataFormat();
//            String tempPattern = datePattern;
//            if (null == tempPattern)
//            {
//                tempPattern = DEFAULT_DATE_PATTERN;
//            }
//            dateCellStyle.setDataFormat(format.getFormat(tempPattern));
//            HSSFCellStyle alignStyle = wb.createCellStyle();
//            alignStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            //如果input为空，则在创建Excel文件中不写入任何内容
//            if ((null != input) && (0 < input.length) && (0 < input[0].length))
//            {
//                int rowLength = input.length;
//                int columnLength = input[0].length;
//                for (int i = 0; i < rowLength; i++)
//                {
//                    HSSFRow row = sheet.getRow(i + rowBegin);
//                    if (null == row)
//                    {
//                        //建立新行
//                        row = sheet.createRow(i + rowBegin);
//                    }
//                    if (null == input[i])
//                    {
//                        continue;
//                    }
//                    for (int j = 0; j < columnLength; j++)
//                    {
//                        //设置cell的值
//                        HSSFCell createCell = row.createCell(j + colBegin);
//                        setCell(createCell, dateCellStyle, alignStyle, input[i][j]);
//                    }
//                }
//            }
//            if (log.isDebugEnabled())
//            {
//                log.debug("成功将" + input.length + "条记录写入Excel文件");
//            }
//            return wb;
//        }
//        finally
//        {
//            IOUitl.closeWithWarnLog(output);
//            IOUitl.closeWithWarnLog(fs);
//        }
//    }
//
//    public static HSSFWorkbook writeToExcelFile(List<?> list , String[] fieldNames , String[] titleNames , int rowBegin ,
//            int colBegin , String outExcelFileName , String sheetName , boolean replaceFile ,
//            String templateExcelFileName , MsOfficeVersion officeVersion , String datePattern) throws Exception
//    {
//        if ((null == list) || (0 == list.size()) || (null == fieldNames) || (0 == fieldNames.length))
//        {
//            return writeToExcelFile(null, rowBegin, colBegin, outExcelFileName, sheetName, replaceFile,
//                    templateExcelFileName, officeVersion, datePattern);
//        }
//        else
//        {
//            Object[][] input = new Object[list.size() + 1][fieldNames.length];
//            for (int j = 0; j < fieldNames.length; j++)
//            {
//                input[0][j] = titleNames[j];
//            }
//            for (int i = 1; i < input.length; i++)
//            {
//                Map<String, Object> fieldsMap = getPropertyObjectMap(list.get(i - 1), true);
//                if (null != fieldsMap)
//                {
//                    for (int j = 0; j < fieldNames.length; j++)
//                    {
//                        input[i][j] = fieldsMap.get(fieldNames[j]);
//                    }
//                }
//            }
//            return writeToExcelFile(input, rowBegin, colBegin, outExcelFileName, sheetName, replaceFile,
//                    templateExcelFileName, officeVersion, datePattern);
//        }
//    }
//
//}
