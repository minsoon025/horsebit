package com.a406.horsebit.service;

import java.time.LocalDateTime;
import java.util.List;

import com.a406.horsebit.aop.DistributedLock;
import com.a406.horsebit.constant.OrderConstant;
import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.domain.redis.OrderSummary;
import com.a406.horsebit.domain.redis.VolumePage;
import com.a406.horsebit.repository.redis.OrderRepository;
import com.a406.horsebit.repository.redis.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.OrderDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final PriceRepository priceRepository;
	private final OrderAsyncService orderAsyncService;

	private final double TENTH_MINIMUM_ORDER_QUANTITY = 0.0001;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, PriceRepository priceRepository, OrderAsyncService orderAsyncService) {
		this.orderRepository = orderRepository;
		this.priceRepository = priceRepository;
		this.orderAsyncService = orderAsyncService;
	}

	@Override
	public List<OrderDTO> getOrders(Long userNo, Long tokenNo) {
		log.info("OrderServiceImpl::getOrders() START");
		return orderRepository.findAllOrder(userNo, tokenNo, "A");
	}

	@Override
	@DistributedLock(key = "'TOKEN_ORDER_LOCK:' + #tokenNo.toString()")
	public String processBuyOrder(Long userNo, Long tokenNo, Order order) {
		String orderStatus = OrderConstant.RESPONSE_EXECUTED;
		// Capture order time.
		LocalDateTime orderCaptureTime = LocalDateTime.now();
		order.setOrderTime(orderCaptureTime);
		// Generate new orderNo.
		Long orderNo = generateOrderNo();
		// Get minimum sell volume page.
		VolumePage minSellVolumePage = orderRepository.findMinSellVolumePage(tokenNo);
		// Check if order is executable.
		long price = order.getPrice();
		long lastPrice = priceRepository.findCurrentPrice(tokenNo).getPrice();
		double quantity = order.getQuantity();
		double remain = quantity;
		while (minSellVolumePage.getPrice() <= price && TENTH_MINIMUM_ORDER_QUANTITY < remain) {
			// Execute trade.
			OrderSummary orderSummary = orderRepository.findSellOrderSummary(tokenNo, minSellVolumePage.getPrice());
			// No sell order summary available.
			if (orderSummary == null) {
				orderRepository.deleteSellVolumePage(tokenNo);
			}
			// Sell order remain is less than order remain.
			else if (orderSummary.getRemain() < remain - TENTH_MINIMUM_ORDER_QUANTITY) {
				double orderSummaryRemain = orderSummary.getRemain();
				// Update order page at order book.
				orderRepository.deleteSellOrderSummary(tokenNo, minSellVolumePage.getPrice());
				// Update volume page at volume book.
				VolumePage volumePage = new VolumePage(minSellVolumePage.getPrice(), orderRepository.findSellVolumeByPriceAtOrderBook(tokenNo, minSellVolumePage.getPrice()));
				orderRepository.saveSellVolumePage(tokenNo, volumePage);
				// Update total volume.
				orderRepository.changeSellTotalVolume(tokenNo, orderRepository.findSellTotalVolume(tokenNo) - orderSummaryRemain);
				// Save trade execution.
				Order sellOrder = orderRepository.findOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo());
				orderAsyncService.buyExecuteTrade(minSellVolumePage.getPrice(), remain, tokenNo, orderNo, userNo, orderSummary.getOrderNo(), orderSummary.getUserNo(), sellOrder, orderCaptureTime);
				// Delete buy order.
				orderRepository.deleteOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo());
				// Update current price.
				lastPrice = minSellVolumePage.getPrice();
				// Update remain.
				remain -= orderSummaryRemain;
			}
			// Order remain is less than sell order remain.
			else {
				orderSummary.setRemain(orderSummary.getRemain() - remain);
				// Update order page at order book.
				orderRepository.changeSellOrderSummary(tokenNo, minSellVolumePage.getPrice(), orderSummary);
				// Update volume page at volume book.
				VolumePage volumePage = new VolumePage(minSellVolumePage.getPrice(), orderRepository.findSellVolumeByPriceAtOrderBook(tokenNo, minSellVolumePage.getPrice()));
				orderRepository.saveSellVolumePage(tokenNo, volumePage);
				// Update total volume.
				orderRepository.changeSellTotalVolume(tokenNo, orderRepository.findSellTotalVolume(tokenNo) - remain);
				// Save trade execution.
				Order sellOrder = orderRepository.findOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo());
				orderAsyncService.buyExecuteTrade(minSellVolumePage.getPrice(), remain, tokenNo, orderNo, userNo, orderSummary.getOrderNo(), orderSummary.getUserNo(), sellOrder, orderCaptureTime);
				// Change sell order.
				sellOrder.setRemain(orderSummary.getRemain());
				orderRepository.saveOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo(), sellOrder);
				// Update current price.
				lastPrice = minSellVolumePage.getPrice();
				// Update remain.
				remain = 0.0;
			}
			// Get minimum sell value page.
			minSellVolumePage = orderRepository.findMinSellVolumePage(tokenNo);
		}
		// Add order volume to volume book and order summary to order book.
		if (TENTH_MINIMUM_ORDER_QUANTITY < remain) {
			addBuyOrder(userNo, tokenNo, order, orderNo, quantity, remain, price);
			orderStatus = OrderConstant.RESPONSE_ORDERED;
		}
		// Update current price.
		priceRepository.saveCurrentPrice(tokenNo, lastPrice);
		return orderStatus;
	}

	@Override
	public String processSellOrder(Long userNo, Long tokenNo, Order order) {
		String orderStatus = OrderConstant.RESPONSE_EXECUTED;
		// Capture order time.
		LocalDateTime orderCaptureTime = LocalDateTime.now();
		order.setOrderTime(orderCaptureTime);
		// Generate new orderNo.
		Long orderNo = generateOrderNo();
		// Get maximum buy volume page.
		VolumePage maxBuyVolumePage = orderRepository.findMaxBuyVolumePage(tokenNo);
		// Check if order is executable.
		long price = order.getPrice();
		long lastPrice = priceRepository.findCurrentPrice(tokenNo).getPrice();
		double quantity = order.getQuantity();
		double remain = quantity;
		while (price <= maxBuyVolumePage.getPrice() && TENTH_MINIMUM_ORDER_QUANTITY < remain) {
			// Execute trade.
			OrderSummary orderSummary = orderRepository.findBuyOrderSummary(tokenNo, maxBuyVolumePage.getPrice());
			// No buy order summary available.
			if (orderSummary == null) {
				orderRepository.deleteBuyVolumePage(tokenNo);
			}
			// Sell order remain is less than order remain.
			else if (orderSummary.getRemain() < remain  - TENTH_MINIMUM_ORDER_QUANTITY) {
				double orderSummaryRemain = orderSummary.getRemain();
				// Update order page at order book.
				orderRepository.deleteBuyOrderSummary(tokenNo, maxBuyVolumePage.getPrice());
				// Update volume page at volume book.
				VolumePage volumePage = new VolumePage(maxBuyVolumePage.getPrice(), orderRepository.findBuyVolumeByPriceAtOrderBook(tokenNo, maxBuyVolumePage.getPrice()));
				orderRepository.saveBuyVolumePage(tokenNo, volumePage);
				// Update total volume.
				orderRepository.changeBuyTotalVolume(tokenNo, orderRepository.findBuyTotalVolume(tokenNo) - orderSummaryRemain);
				// Save trade execution.
				Order buyOrder = orderRepository.findOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo());
				orderAsyncService.sellExecuteTrade(maxBuyVolumePage.getPrice(), orderSummaryRemain, tokenNo, orderSummary.getOrderNo(), orderSummary.getUserNo(), buyOrder, orderNo, userNo, orderCaptureTime);
				// Delete buy order.
				orderRepository.deleteOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo());
				// Update current price.
				lastPrice = maxBuyVolumePage.getPrice();
				// Update remain.
				remain -= orderSummaryRemain;
			}
			// Order remain is less than sell order remain.
			else {
				orderSummary.setRemain(orderSummary.getRemain() - remain);
				// Update order page at order book.
				orderRepository.changeBuyOrderSummary(tokenNo, maxBuyVolumePage.getPrice(), orderSummary);
				// Update volume page at volume book.
				VolumePage volumePage = new VolumePage(maxBuyVolumePage.getPrice(), orderRepository.findBuyVolumeByPriceAtOrderBook(tokenNo, maxBuyVolumePage.getPrice()));
				orderRepository.saveBuyVolumePage(tokenNo, volumePage);
				// Update total volume.
				orderRepository.changeBuyTotalVolume(tokenNo, orderRepository.findBuyTotalVolume(tokenNo) - remain);
				// Save trade execution.
				Order buyOrder = orderRepository.findOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo());
				orderAsyncService.sellExecuteTrade(maxBuyVolumePage.getPrice(), remain, tokenNo, orderSummary.getOrderNo(), orderSummary.getUserNo(), buyOrder, orderNo, userNo, orderCaptureTime);
				// Change buy order.
				buyOrder.setRemain(orderSummary.getRemain());
				orderRepository.saveOrder(orderSummary.getUserNo(), tokenNo, orderSummary.getOrderNo(), buyOrder);
				// Update current price.
				lastPrice = maxBuyVolumePage.getPrice();
				// Update remain.
				remain = 0.0;
				break;
			}
			// Get maximum buy volume page.
			maxBuyVolumePage = orderRepository.findMaxBuyVolumePage(tokenNo);
		}
		// Add order volume to volume book and order summary to order book.
		if (TENTH_MINIMUM_ORDER_QUANTITY < remain) {
			addSellOrder(userNo, tokenNo, order, orderNo, quantity, remain, price);
			orderStatus = OrderConstant.RESPONSE_ORDERED;
		}
		// Update current price.
		priceRepository.saveCurrentPrice(tokenNo, lastPrice);
		return orderStatus;
	}

	private void addBuyOrder(Long userNo, Long tokenNo, Order order, Long orderNo, double quantity, double remain, long price) {
		// Add order summary to order book.
		OrderSummary orderSummary = new OrderSummary(orderNo, userNo, quantity, remain);
//		double buyVolume = orderRepository.findBuyVolumeByPriceAtOrderBook(tokenNo, price) + remain;
		orderRepository.saveBuyOrderSummary(tokenNo, price, orderSummary);
		// Update volume page at volume book.
		double buyVolume = orderRepository.findBuyVolumeByPriceAtOrderBook(tokenNo, price);
		VolumePage volumePage = new VolumePage(price, buyVolume);
		orderRepository.saveBuyVolumePage(tokenNo, volumePage);
		// Update total volume.
		orderRepository.changeBuyTotalVolume(tokenNo, quantity);
		// Update user order list.
		orderRepository.saveOrder(userNo, tokenNo, orderNo, order);
	}

	private void addSellOrder(Long userNo, Long tokenNo, Order order, Long orderNo, double quantity, double remain, long price) {
		// Add order summary to order book.
		OrderSummary orderSummary = new OrderSummary(orderNo, userNo, quantity, remain);
		orderRepository.saveSellOrderSummary(tokenNo, price, orderSummary);
		// Update volume page at volume book.
		double sellVolume = orderRepository.findSellVolumeByPriceAtOrderBook(tokenNo, price);
		VolumePage volumePage = new VolumePage(price, sellVolume);
		orderRepository.saveSellVolumePage(tokenNo, volumePage);
		// Update total volume.
		orderRepository.changeSellTotalVolume(tokenNo, quantity);
		// Update user order list.
		orderRepository.saveOrder(userNo, tokenNo, orderNo, order);
	}

	@DistributedLock(key = "'ORDER_NO_LOCK'")
	private Long generateOrderNo() {
		return orderRepository.increaseOrderNo();
	}
}
