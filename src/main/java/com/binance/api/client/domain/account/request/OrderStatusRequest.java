package com.binance.api.client.domain.account.request;

import lombok.ToString;

/**
 * A specialized order request with additional filters.
 */
@ToString
public class OrderStatusRequest extends OrderRequest {

  private Long orderId;

  private String origClientOrderId;

  public OrderStatusRequest(String symbol, Long orderId) {
    super(symbol);
    this.orderId = orderId;
  }

  public OrderStatusRequest(String symbol, String origClientOrderId) {
    super(symbol);
    this.origClientOrderId = origClientOrderId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public OrderStatusRequest orderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public String getOrigClientOrderId() {
    return origClientOrderId;
  }

  public OrderStatusRequest origClientOrderId(String origClientOrderId) {
    this.origClientOrderId = origClientOrderId;
    return this;
  }

}
