package com.hyp.pojo.shoes.utils;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.Calendar;

 
//实现Printable接口 用于创建打印内容
public class SalesTicket implements Printable {



	private ArrayList<Commodity> list;
	private String cashier;
	private Font font;
	private String sale_num;
	private String sale_sum;
	private String practical;
	private String changes;
	private String orders;




 
	// 构造函数
	public SalesTicket(ArrayList<Commodity> list, String cashier, String orders, String sale_num, String sale_sum,
			String practical, String changes) {
		this.list = list;
		// 收银员编号
		this.cashier = cashier;
		// 订单标号
		this.orders = orders;
		// 商品总数
		this.sale_num = sale_num;
		// 总金额
		this.sale_sum = sale_sum;
		// 实收
		this.practical = practical;
		// 找零
		this.changes = changes;
	}


	/**
	 *
	 * @param graphics 指明打印的图形环境
	 * @param pageFormat 指明打印页格式（页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）
	 * @param pageIndex 指明页号
	 * @return
	 * @throws PrinterException
	 */
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Component c = null;
		// 转换成Graphics2D 拿到画笔
		Graphics2D g2 = (Graphics2D) graphics;
		// 设置打印颜色为黑色
		g2.setColor(Color.black);
 
		// 打印起点坐标
		double x = pageFormat.getImageableX();
		double y = pageFormat.getImageableY();
 
		// 虚线
		float[] dash1 = { 4.0f };
		// width - 此 BasicStroke 的宽度。此宽度必须大于或等于 0.0f。如果将宽度设置为
		// 0.0f，则将笔划呈现为可用于目标设备和抗锯齿提示设置的最细线条。
		// cap - BasicStroke 端点的装饰
		// join - 应用在路径线段交汇处的装饰
		// miterlimit - 斜接处的剪裁限制。miterlimit 必须大于或等于 1.0f。
		// dash - 表示虚线模式的数组
		// dash_phase - 开始虚线模式的偏移量
 
		// 设置画虚线
		g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, dash1, 0.0f));
 
		// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
		font = new Font("宋体", Font.PLAIN, 11);
		g2.setFont(font);// 设置字体
		float heigth = font.getSize2D();// 字体高度
		// 标题
		g2.drawString("广西机电工程学校智能超市", (float) x, (float) y + heigth);
		float line = 2 * heigth;
 
		font = new Font("宋体", Font.PLAIN, 8);
		g2.setFont(font);// 设置字体
		heigth = font.getSize2D();// 字体高度
 
		// 显示收银员
		g2.drawString("收银员:" + cashier, (float) x, (float) y + line);
		// 显示订单号
		g2.drawString("订单号:" + orders, (float) x + 100, (float) y + line);
 
		line += heigth;
		// 显示标题
		g2.drawString("名称", (float) x + 20, (float) y + line);
		g2.drawString("单价", (float) x + 60, (float) y + line);
		g2.drawString("数量", (float) x + 85, (float) y + line);
		g2.drawString("总额", (float) x + 115, (float) y + line);
		line += heigth;
		g2.drawLine((int) x, (int) (y + line), (int) x + 158, (int) (y + line));
 
		// 第4行
		line += heigth;
 
		// 显示内容
		for (int i = 0; i < list.size(); i++) {
 
			Commodity commodity = list.get(i);
 
			g2.drawString(commodity.getName(), (float) x, (float) y + line);
			line += heigth;
 
			g2.drawString(commodity.getBarcode(), (float) x, (float) y + line);
			g2.drawString(commodity.getUnit_price(), (float) x + 60, (float) y + line);
			g2.drawString(commodity.getNum(), (float) x + 90, (float) y + line);
			g2.drawString(commodity.getSum(), (float) x + 120, (float) y + line);
			line += heigth;
 
		}
		line += heigth;
 
		g2.drawLine((int) x, (int) (y + line), (int) x + 158, (int) (y + line));
		line += heigth;
 
		g2.drawString("售出商品数:" + sale_num + "件", (float) x, (float) y + line);
		g2.drawString("合计:" + sale_sum + "元", (float) x + 70, (float) y + line);
		line += heigth;
		g2.drawString("实收:" + practical + "元", (float) x, (float) y + line);
		g2.drawString("找零:" + changes + "元", (float) x + 70, (float) y + line);
		line += heigth;
		g2.drawString("时间:" + Calendar.getInstance().getTime().toLocaleString(), (float) x, (float) y + line);
 
		line += heigth;
		g2.drawString("天天平价,日日新鲜", (float) x + 20, (float) y + line);
 
		line += heigth;
		g2.drawString("钱票请当面点清，离开柜台恕不负责", (float) x, (float) y + line);
		switch (pageIndex) {
		case 0:
 
			return PAGE_EXISTS;
		default:
			return NO_SUCH_PAGE;
 
		}
 
	}


	/**
	 * 打印销售小票
	 *
	 * @param order
	 *            订单号
	 * @param num
	 *            数量
	 * @param sum
	 *            总金额
	 * @param practical
	 *            实收
	 * @param change
	 *            找零
	 */
	public void PrintSale(String order, String num, String sum, String practical, String change) {
		try {
			// 通俗理解就是书、文档
			Book book = new Book();
			// 设置成竖打
			PageFormat pf = new PageFormat();
			pf.setOrientation(PageFormat.PORTRAIT);

			ArrayList<Commodity> cmd_list = new ArrayList<Commodity>();
			// 取出数据
			for (int i = 0; i < list.size(); i++) {
				Commodity c = list.get(i);
				Commodity cd = new Commodity(c.getName(), String.valueOf(c.getUnit_price()),c.getNum(),c.getSum(),c.getBarcode());
				cmd_list.add(cd);
			}

			// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
			Paper paper = new Paper();
			paper.setSize(158, 30000);// 纸张大小
			paper.setImageableArea(0, 0, 158, 30000);// A4(595 X
			// 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
			pf.setPaper(paper);

			book.append(new SalesTicket(cmd_list, "何亚培", order, num, sum, practical, change), pf);

			// 获取打印服务对象
			PrinterJob job = PrinterJob.getPrinterJob();
			// 设置打印类
			job.setPageable(book);

			job.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}




 
}