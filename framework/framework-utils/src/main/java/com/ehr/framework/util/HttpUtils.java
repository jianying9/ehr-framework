package com.ehr.framework.util;

import com.ehr.framework.excel.ExcelWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http中request,response操作辅助类
 * @author zoe
 */
public class HttpUtils {

    private HttpUtils() {
    }

    /**
     * 将JSON字符串输出到前台
     * @param request
     * @param response
     * @param jsonStr
     */
    public static void toWrite(HttpServletRequest request, HttpServletResponse response, String jsonStr) {
        //获取跨域请求标志，并对输出内容做跨域处理
        String jsoncallback = request.getParameter("jsoncallback");
        if (jsoncallback == null || jsoncallback.equals("?")) {
            toWrite(response, jsonStr);
        } else {
            StringBuilder stringBuilder = new StringBuilder(jsoncallback.length() + jsonStr.length() + 2);
            stringBuilder.append(jsoncallback).append('(').append(jsonStr).append(')');
            toWrite(response, stringBuilder.toString());
        }
    }

    /**
     * 将JSON字符串输出到前台
     * @param request
     * @param response
     * @param jsonStr
     */
    private static void toWrite(HttpServletResponse response, String jsonStr) {
        response.setContentType("application/x-javascript");
        response.setCharacterEncoding("utf-8");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(jsonStr);
            printWriter.flush();
        } catch (IOException e) {
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 输出Excel文件
     * @param request
     * @param response
     * @param fileName
     * @param excelWriter
     */
    public static void toWirteExcel(HttpServletRequest request, HttpServletResponse response, String fileName, ExcelWriter excelWriter) {
        String agent = request.getHeader("USER-AGENT");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        ServletOutputStream out = null;
        try {
            String codedfilename;
            if (null != agent && -1 != agent.indexOf("MSIE")) {
                codedfilename = URLEncoder.encode(fileName, "UTF8");
                response.setHeader("Content-Disposition", "attachment;filename=".concat(codedfilename));
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                codedfilename = MimeUtility.encodeText(fileName, "UTF8", "B");
                response.setHeader("Content-Disposition", "attachment;filename=".concat(codedfilename));
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=".concat(fileName));
            }
            out = response.getOutputStream();
            excelWriter.write(out);
            out.flush();
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    /**
     * 输出CSS
     * @param request
     * @param response
     * @param cssString 
     */
    public static void toWirteCss(HttpServletRequest request, HttpServletResponse response, String cssString) {
        response.setContentType("text/css");
        response.setCharacterEncoding("utf-8");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(cssString);
            printWriter.flush();
        } catch (IOException e) {
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 过滤前后全角半角空格
     * @param value
     * @return 
     */
    public static String trim(String value) {
        String result = "";
        int len = value.length();
        if (len > 0) {
            int st = 0;
            int end = len;      /* avoid getfield opcode */
            char[] val = value.toCharArray();    /* avoid getfield opcode */
            while ((st < end) && (val[st] == ' ' || val[st] == '　')) {
                st++;
            }
            while ((st < end) && (val[end - 1] == ' ' || val[end - 1] == '　')) {
                end--;
            }
            result = ((st > 0) || (end < len)) ? value.substring(st, end) : value;
        }
        return result;
    }
}
