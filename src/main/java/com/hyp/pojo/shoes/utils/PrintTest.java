package com.hyp.pojo.shoes.utils;

import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.pojo.shoes.vo.ShoesItemAndProduct;
import com.hyp.pojo.shoes.vo.ShoesTicketVO;
import com.hyp.service.shoes.ShoesProductService;
import com.hyp.service.shoes.impl.ShoesProductServiceImpl;

import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 打印机测试类（58mm）
 * 1、目标打印机必须设置为默认打印机
 * 2、打印页面的宽度和具体的打印机有关，一般为打印纸的宽度，需要配置成系统参数
 * 3、一个汉字的宽度大概是12点
 */
public class PrintTest {


    ShoesProductService shoesProductService = new ShoesProductServiceImpl();

    public static void main(String[] args) {
        print2();
    }

    public void print3(ShoesTicketVO shoesTicketVO) {
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
                                //drawString(graphics2D, "//////////////////////////////", 10, 17, 119, 8);
                                font = new Font("宋体", Font.PLAIN, 7);
                                graphics2D.setFont(font);
                                int yIndex = 20;
                                int lineHeight = 10;
                                int lineWidth = 120;
                                Color defaultColor = graphics2D.getColor();
                                Color grey = new Color(145, 145, 145);

                                Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0);
                                graphics2D.setStroke(stroke);
                                lineWidth = 129;
                                lineHeight = 8;
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 8));
                                graphics2D.setColor(defaultColor);
                                yIndex = drawString(graphics2D, "丹 比 奴 DAMBOLO", 20, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "形象代言人：胡可", 50, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                graphics2D.setColor(grey);
                                yIndex = drawString(graphics2D, "操作员：和*", 5, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                                String nowStr = now.format(format);
                                yIndex = drawString(graphics2D, "日期：" + nowStr, 5 + lineWidth / 2, yIndex, lineWidth, lineHeight);

                                yIndex = drawString(graphics2D, "名称", 5, yIndex + lineHeight * 2 - 5, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "颜色", (lineWidth / 10) * 58 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "规格", (lineWidth / 10) * 72 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "数量", (lineWidth / 10) * 85 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "单价", (lineWidth / 10) * 95 / 10, yIndex, lineWidth, lineHeight);
                                /*订单详细页*/
                                List<ShoesItemAndProduct> shoesItemAndProductList = shoesTicketVO.getShoesItemAndProductList();
                                for (ShoesItemAndProduct shoesItemAndProduct : shoesItemAndProductList) {
                                    System.out.println(shoesItemAndProduct.toString());
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getName(), 5, yIndex + 15, (lineWidth / 10) * 6, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getColor(), (lineWidth / 10) * 58 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getSize().toString(), (lineWidth / 10) * 72 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getNumber().toString(), (lineWidth / 10) * 85 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getPrice().toString(), (lineWidth / 10) * 95 / 10, yIndex, lineWidth, lineHeight);
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    yIndex = yIndex + 6;
                                    graphics2D.drawLine(5, yIndex, 5 + lineWidth, yIndex);
                                }

                                graphics2D.setColor(defaultColor);

                                /*订单总信息*/
                                ShoesOrder shoesOrder = shoesTicketVO.getShoesOrder();
                                yIndex = drawString(graphics2D, "订单号：" + shoesOrder.getId(), 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总  数：" + shoesItemAndProductList.size(), 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总  计：" + shoesOrder.getMoney(), 5, yIndex + lineHeight, lineWidth, lineHeight);
                                if (shoesOrder.getReduction() > 0) {
                                    yIndex = drawString(graphics2D, "减  免：" + shoesOrder.getReduction(), 5, yIndex + lineHeight, lineWidth, lineHeight);
                                }
                                yIndex = drawString(graphics2D, "实  收：" + (shoesOrder.getMoney().subtract(new BigDecimal(String.valueOf(shoesOrder.getReduction())))), 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "积  分：" + (shoesOrder.getMoney().subtract(new BigDecimal(String.valueOf(shoesOrder.getReduction())))), 5, yIndex + lineHeight, lineWidth, lineHeight);

                                /*用户信息*/
                                ShoesUser shoesUser = shoesTicketVO.getShoesUser();
                                yIndex = drawString(graphics2D, "会员名称：" + shoesUser.getRealName(), 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "会员编号：" + shoesUser.getPhoneNum(), 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总积分：" + shoesUser.getEmpirical(), 5, yIndex + lineHeight, lineWidth, lineHeight);

                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                graphics2D.setColor(grey);
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 6));
                                yIndex = drawString(graphics2D, "温馨提示：", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                yIndex = drawString(graphics2D, "    1、本店所售商品严格按照国家“三包”规定执行；", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "    2、积分规则和使用方法详见店内海报。", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 6));
                                yIndex = drawString(graphics2D, "店面地址：", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                yIndex = drawString(graphics2D, "    息县谦楼街西段（华联超市西150米路北）", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "电话：", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                yIndex = drawString(graphics2D, "    137 8299 6182", 5, yIndex + lineHeight, lineWidth, lineHeight);
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


    public static void print2() {
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
                                //drawString(graphics2D, "//////////////////////////////", 10, 17, 119, 8);
                                font = new Font("宋体", Font.PLAIN, 7);
                                graphics2D.setFont(font);
                                int yIndex = 20;
                                int lineHeight = 10;
                                int lineWidth = 120;
                                Color defaultColor = graphics2D.getColor();
                                Color grey = new Color(145, 145, 145);
                                //收货信息
                                //yIndex = drawString(graphics2D, "收货人：路人甲", 10, yIndex, lineWidth, lineHeight);
                                //yIndex = drawString(graphics2D, "收货地址：北京市海淀区上地十街10号百度大厦", 10, yIndex + lineHeight, lineWidth, lineHeight);
                                //收货信息边框
                                Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0);
                                graphics2D.setStroke(stroke);
                                //graphics2D.drawRect(5, 10, 129, yIndex);
                                //药店名称
                                lineWidth = 129;
                                lineHeight = 8;
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 8));
                                graphics2D.setColor(defaultColor);
                                //yIndex = drawString(graphics2D, "北京药店零售小票", 5, yIndex + lineHeight + 20, lineWidth, 12);
                                yIndex = drawString(graphics2D, "丹 比 奴 DAMBOLO", 20, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "形象代言人：胡可", 50, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                graphics2D.setColor(grey);
                                yIndex = drawString(graphics2D, "操作员：和*", 5, yIndex + lineHeight + 2, lineWidth, lineHeight);
                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                                String nowStr = now.format(format);
                                yIndex = drawString(graphics2D, "日期：" + nowStr, 5 + lineWidth / 2, yIndex, lineWidth, lineHeight);

                                yIndex = drawString(graphics2D, "名称", 5, yIndex + lineHeight * 2 - 5, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "颜色", (lineWidth / 10) * 65 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "规格", (lineWidth / 10) * 79 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "数量", (lineWidth / 10) * 9, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "单价", (lineWidth / 10) * 10, yIndex, lineWidth, lineHeight);
                                /*订单详细页*/
                                for (int i = 0; i < 5; i++) {
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    //yIndex = drawString(graphics2D, "空军一号空军一号空军一号空军一号空军一号", 5, yIndex + 15, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, "空军一号", 5, yIndex + 15, (lineWidth / 10) * 6, lineHeight);
                                    yIndex = drawString(graphics2D, "红色", (lineWidth / 10) * 65 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, "42", (lineWidth / 10) * 79 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, "1", (lineWidth / 10) * 9, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, "799", (lineWidth / 10) * 10, yIndex, lineWidth, lineHeight);
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    yIndex = yIndex + 6;
                                    graphics2D.drawLine(5, yIndex, 5 + lineWidth, yIndex);
                                }
                                graphics2D.setColor(defaultColor);

                                /*订单总信息*/
                                yIndex = drawString(graphics2D, "订单号：1006", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总  数：6", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总  计：55.30", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "减  免：55.30", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "抵  免：100.00", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "实  收：44.70", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "积  分：70", 5, yIndex + lineHeight, lineWidth, lineHeight);

                                /*用户信息*/
                                yIndex = drawString(graphics2D, "会员名称：默认用户", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "会员编号：15518901416", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "总积分：700", 5, yIndex + lineHeight, lineWidth, lineHeight);

                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                graphics2D.setColor(grey);
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 6));
                                yIndex = drawString(graphics2D, "温馨提示：", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                yIndex = drawString(graphics2D, "    1、本店所售商品严格按照国家“三包”规定执行；", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "    2、积分规则和使用方法详见店内海报。", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.BOLD, 6));
                                yIndex = drawString(graphics2D, "店面地址：", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                yIndex = drawString(graphics2D, "    息县谦楼街西段（华联超市西150米路北）", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "电话：", 5, yIndex + lineHeight, lineWidth, lineHeight);
                                graphics2D.setFont(new Font("宋体", Font.PLAIN, 6));
                                yIndex = drawString(graphics2D, "    137 8299 6182", 5, yIndex + lineHeight, lineWidth, lineHeight);
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






    /*public static void print1() {
        if (PrinterJob.lookupPrintServices().length > 0) {
            *//*
        打印格式
       *//*
            PageFormat pageFormat = new PageFormat();
            //设置打印起点从左上角开始，从左到右，从上到下打印
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            *//*
        打印页面格式设置
       *//*
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
                                yIndex = drawString(graphics2D, "电话：15518901416", 5, yIndex + lineHeight * 2, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "地址：政府路100号鞋店", 5, yIndex + lineHeight, lineWidth, lineHeight);
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
    }*/

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