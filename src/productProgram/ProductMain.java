package productProgram;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ProductMain {
	
	public static final int INPUT = 1, UPDATE = 2, SEARHCH = 3, DELETE = 4, OUTPUT = 5, SORT = 6, STATS = 7, EXIT = 8;
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {


		boolean mainFlag = false;
		int select = 0;

		while (!mainFlag) {
			switch (displayMenu(select)) {
			case INPUT :
				insertProduct();
				break;
			case UPDATE :
				upateProduct();
				break;
			case SEARHCH :
				searchProduct();
				break;
			case DELETE :
				deleteProduct();
				break;
			case OUTPUT :
				selectProduct();
				break;
			case SORT :
				sortProduct();
				break;
			case STATS :
				productStats();
				break;
			case EXIT :
				System.out.println("종료합니다.");
				mainFlag = true;
				break;
			}
		}
	}
	

	/** 1. 상품 정보 입력 */
	public static void insertProduct() {
		boolean value = false;
		try {
			System.out.println("000~099상의 || 100~199하의 || 200~299신발 || 300~399가방 || 400~499잡화 ");
			System.out.print("품번를 입력하세요 : ");
			String no = sc.nextLine();
			value = checkInputPattern(no,1);
			if (!value) return;
	
			System.out.print("품명을 입력하세요 : ");
			String name = sc.nextLine();
			value = checkInputPattern(name,2);
			if (!value) return;
			
			System.out.print("가격을 입력하세요 : ");
			int price = sc.nextInt();
			value = checkInputPattern(String.valueOf(price),3);
			if (!value) return;
			
			System.out.print("재고를 입력하세요 : ");
			int stock = sc.nextInt();
			value = checkInputPattern(String.valueOf(stock),4);
			if (!value) return;
		

			Product product = new Product(no, name, price, stock);
		
			product.calProductKind();
			product.calDate();

			DBConnection dbc = new DBConnection();
			
			dbc.connect();
			
			int insertReturnValue = dbc.insert(product);
			
			if (insertReturnValue == -1 || insertReturnValue == 0) {
				System.out.println("삽입 실패입니다.");
			} else {
				System.out.println("삽입 성공입니다.");
			}
	
			dbc.close();
	
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}

	/** 2. 상품 정보 수정 */
	public static void upateProduct() {
		List<Product> list = new ArrayList<Product>();
		try {
			System.out.println("1.품번검색 || 2.품명검색");
			int select = sc.nextInt();
			sc.nextLine();
			String data = null;
			
			switch (select) {
				case 1: 
					System.out.println("품번를 입력하세요 : ");
					data = sc.nextLine();
					break;
				case 2:
					System.out.println("품명를 입력하세요 : ");
					data = sc.nextLine();
					break;
				default : System.out.println("다시 입력하세요 (1,2)"); return;
			}
			
			
			boolean value = checkInputPattern(data, select);
			if (!value) return;
			
			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectSearch(data,select);
			
			if (list.size() <= 0) {
				System.out.println("입력된 정보가 없습니다.");
			}
			
			for (Product product : list) {
				System.out.println(product);
			}

			Product updateProduct = list.get(0);
			System.out.print("품명을 입력하세요 : ");
			String name = sc.nextLine();
			value = checkInputPattern(String.valueOf(name), 2);
			if (!value) return;
			updateProduct.setName(name);
			
			System.out.print("가격을 입력하세요 : ");
			int price = sc.nextInt();
			value = checkInputPattern(String.valueOf(price),3);
			if (!value) return;
			updateProduct.setPrice(price);
			
			System.out.print("재고를 입력하세요 : ");
			int stock = sc.nextInt();
			value = checkInputPattern(String.valueOf(stock),4);
			if (!value) return;
			updateProduct.setStock(stock);
			
			updateProduct.calProductKind();
			updateProduct.calDate();

			int returnUpdateValue = dbc.update(updateProduct);
			if (returnUpdateValue == -1) {
				System.out.println("상품 수정 정보 없음");
				return;
			}
			System.out.println("상품 수정 완료하였습니다.");
			
			dbc.close();
		
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}
	
	/** 3. 상품 정보 검색 */
	public static void searchProduct() {
		List<Product> list = new ArrayList<Product>();
		try {
			
			System.out.println("1.품번검색 || 2.품명검색");
			int select = sc.nextInt();
			sc.nextLine();
			String data = null;
			
			switch (select) {
				case 1: 
					System.out.println("품번를 입력하세요 (000~499) : ");
					data = sc.nextLine();
					break;
				case 2:
					System.out.println("품명를 입력하세요 : ");
					data = sc.nextLine();
					break;
				default : System.out.println("다시 입력하세요 (1,2)"); return;
			}
			
			
			boolean value = checkInputPattern(data, select);
			if (!value) return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectSearch(data,select);
			
			if (list.size() <= 0) {
				System.out.println("검색한 상품의 정보가 없습니다.");
			}
			
			for (Product product : list) {
				System.out.println(product);
			}

			dbc.close();
			
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}
	
	/** 4. 상품 정보 삭제 */
	public static void deleteProduct() {

		try {
			System.out.println("삭제할 상품 품번을 입력하세요 (000~499) : ");
			String no = sc.nextLine();
			
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

		
			DBConnection dbc = new DBConnection();
			dbc.connect();

			int deleteReturnValue = dbc.delete(no);
			if (deleteReturnValue == -1) {
				System.out.println("삭제 실패입니다.");
			}
			if (deleteReturnValue == 0) {
				System.out.println("해당 품번의 상품이 없습니다.");
			} else {
				System.out.println(no + "번의 상품 정보를 삭제하였습니다.");
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}
	
	/** 5. 상품 정보 출력 */
	public static void selectProduct() {
		List<Product> list = new ArrayList<Product>();
		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.select();
			
			if (list.size() <= 0) {
				System.out.println("상품 정보가 없습니다.");
			}

			for (Product product : list) {
				System.out.println(product);
			}

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}

	/** 6. 상품 정보 정렬 */
	public static void sortProduct() {
		List<Product> list = new ArrayList<Product>();

		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();
			

			System.out.print("정렬 방식을 선택하세요 (1.품번 || 2.가격 || 3.재고 >> ");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value) return;
	

			list = dbc.selectOrderBy(type);
			if (list.size() <= 0) {
				System.out.println("상품 정보가 없습니다.");
			}

			for (Product product : list) {
				System.out.println(product);
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}
	
	/** 7. 상품 정보 통계 */
	public static void productStats() {
		List<Product> list = new ArrayList<Product>();

		try {
			System.out.println("1.최고가 || 2.최저가");
			int type = sc.nextInt();
			
			boolean value = checkInputPattern(String.valueOf(type),6);
			if (!value) return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectMaxMin(type);
			
			if (list.size() <= 0) {
				System.out.println("상품의 정보가 없습니다.");
			}

			for (Product product : list) {
				System.out.println(product);
			}
			
			dbc.close();
			
		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException " + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return;
		}
	}

	/** 검색 패턴 체크 */
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;

		switch (patternType) {
		case 1:
			pattern = "^[0-4][0-9][0-9]$";
			message = "품번을 다시 입력하세요 (000~499)";
			break;
		case 2:
			pattern = "^[가-힝]{1,9}$";
			message = "품명을 다시 입력하세요";
			break;
		case 3:
			pattern = "^[0-9]{1,7}$";
			message = "가격을 다시 입력하세요 (0~9999999)";
			break;
		case 4:
			pattern = "^[0-9]{1,5}$";
			message = "재고를 다시 입력하세요 (0~99999)";
			break;
		case 5:
			pattern = "^[1-3]$";
			message = "다시 입력하세요 (1~3)";
			break;
		case 6:
			pattern = "^[1-2]$";
			message = "다시 입력하세요 (1~2)";
			break;
		case 7:
			pattern = "^[1-8]*$";
			message = "다시 입력하세요 (1~8)";
			break;
		}

		regex = Pattern.matches(pattern, data);

		if (!regex) {
			System.out.println(message);
			return false;
		}

		return regex;
	}

	/** 메뉴 선택 */
	public static int displayMenu(int select) {
		try {
			System.out.println("〓".repeat(70));
			System.out.println("1.입력 || 2.수정 || 3.검색 || 4.삭제 || 5.출력 || 6.정렬 || 7.통계 || 8.종료");
			System.out.println("〓".repeat(70));
			select = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(select),7);
			
		} catch (InputMismatchException e) {
			System.out.println("다시 입력하세요 (1~8)");
		} finally {
			sc.nextLine();
		}
		return select;
	}
}
