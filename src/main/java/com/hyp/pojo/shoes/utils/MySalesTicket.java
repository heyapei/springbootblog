package com.hyp.pojo.shoes.utils;

import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.pojo.shoes.dataobject.ShoesUser;
import com.hyp.pojo.shoes.vo.ShoesItemAndProduct;
import com.hyp.pojo.shoes.vo.ShoesTicketVO;

import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/14 15:19
 * @Description: TODO
 */
public class MySalesTicket {

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
                                yIndex = drawString(graphics2D, "颜色", (lineWidth / 10) * 65 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "规格", (lineWidth / 10) * 79 / 10, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "数量", (lineWidth / 10) * 9, yIndex, lineWidth, lineHeight);
                                yIndex = drawString(graphics2D, "单价", (lineWidth / 10) * 10, yIndex, lineWidth, lineHeight);
                                /*订单详细页*/
                                List<ShoesItemAndProduct> shoesItemAndProductList = shoesTicketVO.getShoesItemAndProductList();
                                for (ShoesItemAndProduct shoesItemAndProduct : shoesItemAndProductList) {
                                    System.out.println(shoesItemAndProduct.toString());
                                    graphics2D.setFont(new Font("宋体", Font.PLAIN, 7));
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getName(), 5, yIndex + 15, (lineWidth / 10) * 6, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getColor(), (lineWidth / 10) * 65 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getSize().toString(), (lineWidth / 10) * 79 / 10, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getNumber().toString(), (lineWidth / 10) * 9, yIndex, lineWidth, lineHeight);
                                    yIndex = drawString(graphics2D, shoesItemAndProduct.getPrice().toString(), (lineWidth / 10) * 10, yIndex, lineWidth, lineHeight);
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
