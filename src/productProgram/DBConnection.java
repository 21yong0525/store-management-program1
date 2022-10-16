package productProgram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	private Connection connection = null;

	/** connection */
	public void connect() {

		
		Properties properties = new Properties();

		try {
			FileInputStream fis = new FileInputStream("C:/java_test/shopProgram/src/productProgram/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Fproperties.load error" + e.getMessage());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("connerction error" + e.getMessage());
		}
		
	}

	/** insert statement */
	public int insert(Product product) {
		PreparedStatement ps = null;
		String insertQuery = "insert into product(no,name,kind,price,stock,date) values(?,?,?,?,?,?)";
		
		int insertReturnValue = -1;
		
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, product.getNo());
			ps.setString(2, product.getName());
			ps.setString(3, product.getKind());
			ps.setInt(4, product.getPrice());
			ps.setInt(5, product.getStock());
			ps.setString(6, product.getDate());
			insertReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			if(e.getMessage().equals("Duplicate entry '"+product.getNo()+"' for key 'PRIMARY'")) {
				System.out.println("동일한 품번의 상품이 존재합니다.");
			} else {
				System.out.println("SQLException error"+e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return insertReturnValue;
	}
	
	/** update statement */
	public int update(Product product) {
		PreparedStatement ps = null;
		String insertQuery = "update product set name = ?,kind = ?, price = ?, stock = ?, date = ? where no = ?";
		int updateReturnValue = -1;

		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, product.getName());
			ps.setString(2, product.getKind());
			ps.setInt(3, product.getPrice());
			ps.setInt(4, product.getStock());
			ps.setString(5, product.getDate());
			ps.setString(6, product.getNo());
			updateReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("update error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return updateReturnValue;
	}
	
	/** search statement */
	public List<Product> selectSearch(String data, int type) {
		PreparedStatement ps = null;

		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		String selectSearchQuery = "select * from product where ";

		try {
			switch (type) {
			case 1:
				selectSearchQuery += "no like ? ;";
				break;
			case 2:
				selectSearchQuery += "name like ? ;";
				break;
			default:
				System.out.println("잘못 입력하였습니다.");
				return list;
			}

			ps = connection.prepareStatement(selectSearchQuery);

			String namePattern = "%" + data + "%";
			ps.setString(1, namePattern);
			rs = ps.executeQuery();


			if (!(rs != null || rs.isBeforeFirst())) return list;


			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(no, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("search 에러" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}

	/** delete statement */
	public int delete(String no) {
		PreparedStatement ps = null;
		String dleleteQuery = "delete from product where no = ?;";
		int deleteReturnValue = -1;

		try {
			ps = connection.prepareStatement(dleleteQuery);
			ps.setString(1, no);

			deleteReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return deleteReturnValue;
	}

	/** select statement */
	public List<Product> select() {
		PreparedStatement ps = null;
		String selectQuery = "select * from product;";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			ps = connection.prepareStatement(selectQuery);

			rs = ps.executeQuery(selectQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(no, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** selectOrderBy statement */
	public List<Product> selectOrderBy(int type) {
		PreparedStatement ps = null;
		String selectOrderByQuery = "select * from product order by ";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			switch (type) {
				case 1: selectOrderByQuery += "no asc"; break;
				case 2: selectOrderByQuery += "price desc"; break;
				case 3: selectOrderByQuery += "stock desc"; break;
				default : System.out.println("다시 입력하세요"); return list;
			}
			ps = connection.prepareStatement(selectOrderByQuery);


			rs = ps.executeQuery(selectOrderByQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(no, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("select error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** selectMaxMin statement */
	public List<Product> selectMaxMin(int type) {
		Statement st = null;
		String selectMaxMinQuery = "select * from product where price = ";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			st = connection.createStatement();
			
			switch (type) {
				case 1: selectMaxMinQuery += "(select MAX(price) from product);"; break;
				case 2: selectMaxMinQuery += "(select MIN(price) from product);"; break;
				default : System.out.println("다시 입력하세요"); return list;
			}

			rs = st.executeQuery(selectMaxMinQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(no, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("Statement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** Connection close */
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Connection close error" + e.getMessage());
		}
	}
}
