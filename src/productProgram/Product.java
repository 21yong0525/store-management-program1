package productProgram;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//상품 관리 프로그램
public class Product implements Comparable<Product>, Serializable {
	
	private String no;	 // 번호
	private String name; // 이름
	private String kind; // 종류
	private int price;	 // 가격
	private int stock;	 // 재고
	private String date; // 상품등록날짜
	

	public Product(String no, String name, String kind, int price, int stock, String date) {
		this(no,name,price,stock);
		this.kind = kind;
		this.date = date;
	}
	
	public Product(String no, String name, int price, int stock) {
		this.no = no;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}


	public String getNo() {return no;}
	public void setNo(String no) {this.no = no;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getKind() {return kind;}
	public void setKind(String kind) {this.kind = kind;}

	public int getPrice() {return price;}
	public void setPrice(int price) {this.price = price;}

	public int getStock() {return stock;}
	public void setStock(int stock) {this.stock = stock;}

	public String getDate() {return date;}
	public void setDate(String date) {this.date = date;}
	
	public void calProductKind() {
		switch (Integer.parseInt(no)/100) {
			case 4: this.kind = "잡화"; break;//400~499 잡화
			case 3: this.kind = "가방"; break;//300~399 가방
			case 2: this.kind = "신발"; break;//200~299 신발
			case 1: this.kind = "하의"; break;//100~199 하의
			case 0: this.kind = "상의"; break;//0~99 상의
		}
	}
	
	public void calDate() {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		this.date = sdformat.format(new Date());
	}
	
	@Override
	public int hashCode() {
		return this.no.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Product)) return false;
		return this.no.equals(((Product)obj).no);
	}
	
	@Override
	public int compareTo(Product product) {
		return this.no.compareToIgnoreCase(product.no);
	}
	
	@Override
	public String toString() {
		System.out.println("〓".repeat(70));
		System.out.println("상품 정보\n품번\t품명\t종류\t가격\t재고\t등록일");
		return no + "\t" + name + "\t" + kind + "\t" + price +"\t"+ stock + "\t" + date;
	}
}
