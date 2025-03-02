package space.gavinklfong.demo.streamapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@DataJpaTest
public class MyStreamApiTest {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ProductRepo productRepo;

	@Test
	@DisplayName("Obtain a list of product with category = \"Books\" and price > 100")
	public void exercise1() {
		//productRepo.findAll().stream().filter(p -> p.getCategory()=="Books").filter(p -> p.getPrice()>100).collect(Collectors.toList()); do not use ==
		productRepo.findAll().stream().filter( p -> p.getCategory().equalsIgnoreCase("Books"))
				.filter(p -> p.getPrice()>100).collect(Collectors.toList());
	}

	@Test
	@DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using Predicate chaining for filter)")
	public void exercise1a() {
		Predicate<Product> allBooks = (Product p) -> p.getCategory().equalsIgnoreCase("Books");
		Predicate<Product> greater = (Product p) -> p.getPrice()>100;

		productRepo.findAll().stream().filter(p -> allBooks.and(greater).test(p)).collect(Collectors.toList());
	}

	@Test
	@DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using BiPredicate for filter)")
	public void exercise1b() {
		BiPredicate<Product, String> categoryFilter = (Product product, String  category) ->product.getCategory().equalsIgnoreCase(category);
		productRepo.findAll().stream()
				.filter( product -> categoryFilter.test(product, "Books") && product.getPrice()> 100)
				.collect(Collectors.toList());
	}

	@Test
	@DisplayName("Obtain a list of order with product category = \"Baby\"")
	public void exercise2() {
		orderRepo.findAll()
				.stream().filter(order -> order.getProducts().stream()
						.anyMatch(product -> product.equals("Baby"))).collect(Collectors.toList());

	}

	@Test
	@DisplayName("Obtain a list of product with category = “Toys” and then apply 10% discount\"")
	public void exercise3() {
		productRepo.findAll().stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Toys"))
				.map(product -> product.getPrice()*0.90).collect(Collectors.toList());
	}

	@Test
	@DisplayName("Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021")
	public void exercise4() {
		LocalDate startDate = LocalDate.of(2021,2, 01);
		LocalDate endDate = LocalDate.of(2021,4, 01);
//		productRepo.findAll().stream().filter(product -> product.getOrders()
//				.stream().filter(order -> order.getCustomer().getTier().equals(2))
//				.filter(order -> order.getOrderDate().isAfter(startDate) && order.getOrderDate().isBefore(endDate))).collect(Collectors.toList());
		orderRepo.findAll().stream()
				.filter(order -> order.getCustomer().getTier().equals(2))
				.filter(order -> order.getOrderDate().isAfter(startDate))
				.filter(order -> order.getOrderDate().isBefore(endDate))
				.flatMap(order -> order.getProducts().stream())
				.distinct()
				.collect(Collectors.toList());
	}

	@Test
	@DisplayName("Get the 3 cheapest products of \"Books\" category")
	public void exercise5() {
		productRepo.findAll().stream()
				.filter(product -> product.getCategory().equalsIgnoreCase("Books"))
				.map(product -> product.getPrice())
				.sorted()
				.limit(3)
				.collect(Collectors.toList());
	}

	@Test
	@DisplayName("Get the 3 most recent placed order")
	public void exercise6() {
		orderRepo.findAll().stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3);
	}

	@Test
	@DisplayName("Get a list of products which was ordered on 15-Mar-2021")
	public void exercise7() {
		orderRepo.findAll().stream()
				.filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
				.flatMap(order -> order.getProducts().stream())
				.distinct()
				.collect(Collectors.toList());
	}

	@Test
	@DisplayName("Calculate the total lump of all orders placed in Feb 2021")
	public void exercise8() {

	}

	@Test
	@DisplayName("Calculate the total lump of all orders placed in Feb 2021 (using reduce with BiFunction)")
	public void exercise8a() {
	}

	@Test
	@DisplayName("Calculate the average price of all orders placed on 15-Mar-2021")
	public void exercise9() {
	}

	@Test
	@DisplayName("Obtain statistics summary of all products belong to \"Books\" category")
	public void exercise10() {
	}

	@Test
	@DisplayName("Obtain a mapping of order id and the order's product count")
	public void exercise11() {
	}

	@Test
	@DisplayName("Obtain a data map of customer and list of orders")
	public void exercise12() {
	}

	@Test
	@DisplayName("Obtain a data map of customer_id and list of order_id(s)")
	public void exercise12a() {
	}

	@Test
	@DisplayName("Obtain a data map with order and its total price")
	public void exercise13() {
	}

	@Test
	@DisplayName("Obtain a data map with order and its total price (using reduce)")
	public void exercise13a() {
	}

	@Test
	@DisplayName("Obtain a data map of product name by category")
	public void exercise14() {
	}

	@Test
	@DisplayName("Get the most expensive product per category")
	void exercise15() {
	}
	
	@Test
	@DisplayName("Get the most expensive product (by name) per category")
	void exercise15a() {
	}

}
