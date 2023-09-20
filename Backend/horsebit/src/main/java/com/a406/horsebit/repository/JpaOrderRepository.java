//package com.a406.horsebit.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import com.a406.horsebit.domain.Order;
//
//import jakarta.persistence.EntityManager;
//
//public class JpaOrderRepository implements OrderRepository{
//	private final EntityManager em;
//
//	public JpaOrderRepository(EntityManager em) {
//		this.em = em;
//	}
//
//	@Override
//	public Order save(Order order) {
//		em.persist(order);
//		return order;
//	}
//
//	@Override
//	public List<Order> findByUserNo(int userNo) {
//		return null;
//	}
//
//	@Override
//	public List<Order> findByTokenNo(int tokenNo) {
//		return null;
//	}
//
//	@Override
//	public List<Order> findAll() {
//		return em.createQuery("select o from Order o", Order.class)
//			.getResultList();
//	}
//}
