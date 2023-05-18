import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
public class OrderPage extends JFrame{
	
	private JPanel menuPanel,calPanel,orderPanel,operatePanel,resultPanel,overallPanel;
	private JTextField quantity;
	private JTextArea orderList;
	private JScrollPane scrollPane,scrollPane2;
	private JComboBox items;
	private JButton addOrder,confirm;
	private JLabel ingredientsToday,choose,quantityLabel,totalLabel,confirmed;
	private JTable table,ordered;
	private static ArrayList<String>names;
	private int totalP;	
	private DefaultTableModel tblModel;
	private Order order;
	private int indx;
	int index = 0;
	
	Statement stat;
	public OrderPage() throws SQLException {
		this.setSize(600, 500);
		this.setTitle("點餐頁面");
		this.setBackground(Color.orange);
		indx = 1;
		order = new Order(indx);
		//orderQ = new ArrayList<Integer>();
		names = new ArrayList<String>();
		
		//tblModel = (DefaultTableModel)ordered.getModel();
		createItemCombo();
		createTextArea();
		createButton();
		createPanel();
		
		
	}
	
	public void createPanel() { // 排版用
		menuPanel = new JPanel(new FlowLayout());
		ingredientsToday = new JLabel("今日食材");
		scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		menuPanel.add(ingredientsToday);
		menuPanel.add(scrollPane);
		
		orderPanel = new JPanel(new FlowLayout());
		choose = new JLabel("選擇品項");
		quantityLabel = new JLabel("數量");
		quantity = new JTextField("0",10);
		orderPanel.add(choose);
		orderPanel.add(items);
		orderPanel.add(quantityLabel);
		orderPanel.add(quantity);
		orderPanel.add(addOrder);
		
		operatePanel = new JPanel(new GridLayout(2,1));
		scrollPane2 = new JScrollPane(orderList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		operatePanel.add(orderPanel);
		operatePanel.add(scrollPane2);
		
		calPanel = new JPanel(new FlowLayout());
		totalLabel = new JLabel(String.format("%s  %d","總計",0));
		calPanel.add(totalLabel);
		calPanel.add(confirm);
		
		resultPanel = new JPanel(new FlowLayout());
		resultPanel.add(calPanel);
		resultPanel.add(confirmed);
		
		overallPanel = new JPanel(new GridLayout(3,1));
		overallPanel.add(menuPanel);
		overallPanel.add(operatePanel);
		overallPanel.add(resultPanel);
		this.add(overallPanel);
	}
	
	
	public void createItemCombo() { // 創建品項選單
		items = new JComboBox();
		PreparedStatement pst;
		ResultSet rs;
		DBConnectionFood con = new DBConnectionFood();
		String query = "SELECT `f_name` FROM `food`;";
		
		try {
			pst = con.mkDataBase().prepareStatement(query);
			rs = pst.executeQuery();
			while(rs.next()) {
				items.addItem(rs.getString("f_name"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void createTextArea() throws SQLException { // 創建菜單顯示區域
		
		String[] columnNames = {"序號","名稱","價錢","數量"};
		Object[][] data = new Object[100][4];
		PreparedStatement pst;
		ResultSet rs;
		DBConnectionFood con = new DBConnectionFood();
		String query = "SELECT * FROM `food`;";
		int i = 0;
		try {
			pst = con.mkDataBase().prepareStatement(query);
			rs = pst.executeQuery();
			while(rs.next()) {
				data[i][0] = rs.getInt("f_ID");
				data[i][1] = rs.getString("f_name");
				data[i][2] = rs.getDouble("f_prize");
				data[i][3] = rs.getInt("f_quantity");
				i++;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		table = new JTable(data,columnNames);
		table.setGridColor(Color.white);
		
		
		orderList = new JTextArea(70,50); // 創建已選區域
		orderList.setEditable(false);
		
		
	}
	
	public void createButton() throws SQLException{
		addOrder = new JButton("加入");
		addOrder.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				//System.out.println(items.getSelectedItem().toString()+quantity.getText());
				PreparedStatement pst;
				ResultSet rs;
				DBConnectionFood con = new DBConnectionFood();
				//int i = 0;
				try {
					
					
					if(quantity.getText().equals("0")) {
						JOptionPane.showMessageDialog(null,"請選擇數量", "error",JOptionPane.ERROR_MESSAGE);
					}
					
					String query1 = String.format("SELECT `f_quantity` FROM `food` WHERE `f_name` = '%s';",items.getSelectedItem().toString());
					pst = con.mkDataBase().prepareStatement(query1);
					rs = pst.executeQuery();
					
					while(rs.next()) {
						if(rs.getInt("f_quantity")<Integer.valueOf(quantity.getText())) {
							JOptionPane.showMessageDialog(null,"食材供應不足", "error",JOptionPane.ERROR_MESSAGE);
						}
					}
				
					
						//orderList.append(String.format("%15s%15s\n",items.getSelectedItem().toString(),quantity.getText()));
					
					else {	
						String query2 = String.format("SELECT `f_prize` FROM `food` WHERE `f_name` = '%s';",items.getSelectedItem().toString());
						pst = con.mkDataBase().prepareStatement(query2);
						rs = pst.executeQuery();
						while(rs.next()) {
							order.addOrder(items.getSelectedItem().toString(),Integer.valueOf(quantity.getText()),rs.getDouble("f_prize"));
							/*String[] columnNames = {"名稱","數量","價錢"};
							Object[][] data = new Object[100][3];
							data[i][0] = items.getSelectedItem().toString();
							data[i][1] = Integer.valueOf(quantity.getText());
							data[i][2] = rs.getDouble("f_prize");*/
						
							orderList.append(String.format("%10s%10s%10.2f\n",items.getSelectedItem().toString(),Integer.valueOf(quantity.getText()),rs.getDouble("f_prize")));
												
							/*DefaultTableModel model = (DefaultTableModel)ordered.getModel();
							model.setRowCount(0);
							model.addRow(data);
							ordered = new JTable(model);
							ordered.setGridColor(Color.white);*/
						
//							System.out.print(order.getContent().get(index)+" "+order.getOrderQ().get(index)+" "+order.getPrice().get(index));
//							index++;
						}
						totalLabel.setText(String.format("%s  %.1f","總計",order.calTotal()));
						
						
					}
				}
					
				
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				
			
			});
		confirm = new JButton("確認");
		confirmed = new JLabel("請點擊確認");
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				confirmed.setText("餐點已送出");
				indx++;
			}
		});
	}


	
}





