import java.util.ArrayList;

public class Order {

	private ArrayList<String>content;
	private ArrayList<Integer>orderQ;
	private ArrayList<Double>price;
	private int idx;
	private int total;
	
	public Order(int idx) {
		this.idx = idx;
		content = new ArrayList<String>();
		orderQ = new ArrayList<Integer>();
		price = new ArrayList<Double>();
		total = 0;
	}
	
	public void addOrder(String food,int quantity,double price) { // 把已選取的食材加到訂單內容的array儲存
		content.add(food);
		orderQ.add(quantity);
		this.price.add(price);
	}
	
	public void findAndUpdate(String foodName,int num) {
		int index = 0;
		for(String s:content) {
			if(s.equals(foodName)) {
				index = content.indexOf(s);
			}
		}
		orderQ.add(index, orderQ.get(index)+num); //更新點餐內容
		
	}
	
	public double calTotal() {
		double Total = 0;
		for(int i = 0;i<content.size();i++) {
			Total += orderQ.get(i)*price.get(i);
		}
		return Total;
	}

	public ArrayList<String> getContent() {
		return content;
	}

	public ArrayList<Integer> getOrderQ() {
		return orderQ;
	}

	public ArrayList<Double> getPrice() {
		return price;
	}
	
	
}
