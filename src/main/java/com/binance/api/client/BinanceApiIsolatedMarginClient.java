package com.binance.api.client;

import java.util.List;
import java.util.Optional;

import com.binance.api.client.domain.account.MarginNewOrder;
import com.binance.api.client.domain.account.MarginNewOrderResponse;
import com.binance.api.client.domain.account.MarginTransaction;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.account.isolated.IsolatedMarginAccountInfo;
import com.binance.api.client.domain.account.isolated.IsolatedMarginSymbol;
import com.binance.api.client.domain.account.isolated.IsolatedMarginTransfer;
import com.binance.api.client.domain.account.isolated.IsolatedMarginTransferResult;
import com.binance.api.client.domain.account.isolated.NewIsolatedAccountResponse;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.CancelOrderResponse;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.event.ListenKey;

public interface BinanceApiIsolatedMarginClient {

  NewIsolatedAccountResponse createAccount(String base, String quote, String apiKey, String secret);

  IsolatedMarginAccountInfo queryAccount(Optional<List<String>> symbols, String apiKey, String secret);

  /**
   * Get all open orders on margin account for a symbol.
   *
   * @param orderRequest order request parameters
   */
  List<Order> getOpenOrders(OrderRequest orderRequest, String apiKey, String secret);

  /**
   * Send in a new margin order.
   *
   * @param order the new order to submit.
   * @return a response containing details about the newly placed order.
   */
  MarginNewOrderResponse newOrder(MarginNewOrder order, String apiKey, String secret);

  /**
   * Cancel an active margin order.
   *
   * @param cancelOrderRequest order status request parameters
   */
  CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest, String apiKey, String secret);

  /**
   * Check margin order's status.
   *
   * @param orderStatusRequest order status request options/filters
   * @return an order
   */
  Order getOrderStatus(OrderStatusRequest orderStatusRequest, String apiKey, String secret);

  /**
   * Get margin trades for a specific symbol.
   *
   * @param symbol symbol to get trades from
   * @return a list of trades
   */
  List<Trade> getMyTrades(String symbol, String apiKey, String secret);

  /**
   * Transfers funds between spot and isolated margin account (account must be
   * first created).
   *
   * @param transfer
   */
  IsolatedMarginTransferResult transfer(IsolatedMarginTransfer transfer, String apiKey, String secret);

  /**
   * Apply for a loan
   *
   * @param asset  asset to repay
   * @param amount amount to repay
   * @return transaction id
   */
  MarginTransaction borrow(String asset, String symbol, String amount, String apiKey, String secret);

  /**
   * Repay loan for margin account
   *
   * @param asset  asset to repay
   * @param amount amount to repay
   * @return transaction id
   */
  MarginTransaction repay(String asset, String symbol, String amount, String apiKey, String secret);

  IsolatedMarginSymbol getSymbol(String symbol, String apiKey, String secret);

  List<IsolatedMarginSymbol> getSymbols(String apiKey, String secret);

  /**
   * Start a new user data stream.
   *
   * @param symbol the isolated account symbol you want to receive events for
   */
  ListenKey startUserDataStream(String symbol, String apiKey);

  /**
   * PING a user data stream to prevent a time out.
   *
   * @param symbol    the isolated account symbol you want to receive events for
   * @param listenKey listen key that identifies a data stream
   */
  void keepAliveUserDataStream(String symbol, String listenKey, String apiKey);

  /**
   * Close out a new user data stream.
   *
   * @param symbol    the isolated account symbol you want to stop receiving
   *                  events for
   * @param listenKey listen key that identifies a data stream
   */
  void closeUserDataStream(String symbol, String listenKey, String apiKey);

}
