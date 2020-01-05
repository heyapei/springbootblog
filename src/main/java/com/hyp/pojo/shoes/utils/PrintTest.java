package com.hyp.pojo.shoes.utils;

import java.awt.*;
import java.awt.print.*;
import java.util.ArrayList;

/**
 * 打印机测试类（58mm）
 * 1、目标打印机必须设置为默认打印机
 * 2、打印页面的宽度和具体的打印机有关，一般为打印纸的宽度，需要配置成系统参数
 * 3、一个汉字的宽度大概是12点
 */
public class PrintTest {
    public static void main(String[] args) {
        PageFormat pageFormat = new PageFormat();
        //设置打印起点从左上角开始，从左到右，从上到下打印
        pageFormat.setOrientation(PageFormat.PORTRAIT);
            /*
        打印页面格式设置
       */
        Paper paper = new Paper();
        //设置打印宽度（固定，和具体的打印机有关）和高度（跟实际打印内容的多少有关）
        paper.setSize(140, 450);
        //设置打印区域 打印起点坐标、打印的宽度和高度
        paper.setImageableArea(0, 0, 135, 450);
        pageFormat.setPaper(paper);
        Commodity commodity = new Commodity("11", "11", "11", "11", "11");
        ArrayList<Commodity> commodities = new ArrayList<>();
        commodities.add(commodity);


        String order = "1";
        String num = "1";
        String sum1 = "1";
        String ptc = "1";
        String change = "1";

        new SalesTicket(commodities, "11", "123", "11", "11", "11", "11").
                PrintSale(order, num, sum1, ptc, change);

    }
    public static void print1() {
        if (PrinterJob.lookupPrintServices().length > 0) {
            /*
        打印格式
       */
            PageFormat pageFormat = new PageFormat();
            //设置打印起点从左上角开始，从左到右，从上到下打印
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            /*
        打印页面格式设置
       */
            Paper paper = new Paper();
            //设置打印宽度（固定，和具体的打印机有关）和高度（跟实际打印内容的多少有关）
            paper.setSize(140, 450);
            //设置打印区域 打印起点坐标、打印的宽度和高度
            paper.setImageableArea(0, 0, 135, 450);
            pageFormat.setPaper(paper);
            //创建打印文档
            Book book = new Book();
            book.append(new Printable() {
                            @Override
                            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                                if (pageIndex > 0) {
                                    return NO_SUCH_PAGE;
                                }
                                Graphics2D graphics2D = (Graphics2D) graphics;
                                Font font = new Font("宋体", Font.PLAIN, 5);
                                graphics2D.setFont(font);
                                drawString(graphics2D, "//////////////////////////////", 10, 17, 119, 8);
                                font = new Font("宋体", Font.PLAIN, 7);
                                graphics2D.setFont(font);
                                int yIndex = 30;
                                int lineHeight = 10;
                                int lineWidth = 120;
                                Color defaultColor = graphics2D.getColor();
                                Color grey = new Color(145, 145, 145);
                                //收货信息
                                yIndex = drawString(graphics2D, "收货人：路人甲", 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "收货地址：北京市海淀区上地十街10号百度大厦", 10, yIndex + lineHeight, lineWidth, lineHeight);
                                //收货信息边框
                                Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0);
                                graphics2D.setStroke(stroke);
                                graphics2D.drawRect(5, 10, 129, yIndex);
                                //药店名称
                                lineWidth = 129;
                                lineHeight = 8;
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 8));
                                graphics2D.setColor(defaultColor);
                                yIndex = drawString(graphics2D, "北京药店零售小票", 5, yIndex + lineHeight + 20, lineWidth, 12);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                graphics2D.setColor(grey);
                                yIndex = drawString(graphics2D, "操作员：小清新", 5, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "日期：2017-01-05", 5 + lineWidth / 2, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "品名", 5, yIndex + lineHeight * 2 - 5, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "规格", (lineWidth / 10) * 4, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "单价", (lineWidth / 10) * 8, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "数量", (lineWidth / 10) * 10, yIndex, lineWidth, lineHeight);
                                for (int i = 0; i < 5; i++) {
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    yIndex = drawString(graphics2D, "E复合维生素B片100片E复合维生素B片100片", 5, yIndex + 15, (lineWidth / 10) * 7, 10);
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                    graphics2D.setColor(grey);
                                    yIndex = drawString(graphics2D, "100片/盒", 5, yIndex + 11, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, "14.50", (lineWidth / 10) * 8, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, "2", (lineWidth / 10) * 10, yIndex, lineWidth, lineHeight);
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    yIndex = yIndex + 2;
                                    graphics2D.drawLine(5, yIndex, 5 + lineWidth, yIndex);
                                }
                                graphics2D.setColor(defaultColor);
                                yIndex = drawString(graphics2D, "会员名称：小清新", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总  数：6", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总  计：55.30", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "收  款：100.00", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "找  零：44.70", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                graphics2D.setColor(grey);
                                yIndex = drawString(graphics2D, "电话：020-123456", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "地址：北京市海淀区上地十街10号百度大厦", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = yIndex + 20;
                                graphics2D.drawLine(0, yIndex, 140, yIndex);
                                return PAGE_EXISTS;
                            }
                        }
                    , pageFormat);
            //获取默认打印机
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPageable(book);
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
                System.out.println("打印异常");
            }
        } else {
            System.out.println("没法发现打印机服务");
        }
    }

    /**
     * 字符串输出
     *
     * @param graphics2D 画笔
     * @param text       打印文本
     * @param x          打印起点 x 坐标
     * @param y          打印起点 y 坐标
     * @param lineWidth  行宽
     * @param lineHeight 行高
     * @return 返回终点 y 坐标
     */
    private static int drawString(Graphics2D graphics2D, String text, int x, int y, int lineWidth, int lineHeight) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        if (fontMetrics.stringWidth(text) < lineWidth) {
            graphics2D.drawString(text, x, y);
            return y;
        } else {
            char[] chars = text.toCharArray();
            int charsWidth = 0;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                if ((charsWidth + fontMetrics.charWidth(chars[i])) > lineWidth) {
                    graphics2D.drawString(sb.toString(), x, y);
                    sb.setLength(0);
                    y = y + lineHeight;
                    charsWidth = fontMetrics.charWidth(chars[i]);
                    sb.append(chars[i]);
                } else {
                    charsWidth = charsWidth + fontMetrics.charWidth(chars[i]);
                    sb.append(chars[i]);
                }
            }
            if (sb.length() > 0) {
                graphics2D.drawString(sb.toString(), x, y);
                y = y + lineHeight;
            }
            return y - lineHeight;
        }
    }
}