package com.binance.api.examples;

import java.util.List;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.PriceFilter;
import com.binance.api.client.domain.general.SymbolInfo;

/**
 * Examples on how to use the general endpoints.
 */
public class GeneralEndpointsExample {

  public static void main(String[] args) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    BinanceApiRestClient client = factory.newRestClient();

    // Test connectivity
    client.ping();

    // Check server time
    long serverTime = client.getServerTime();
    System.out.println(serverTime);

    // Exchange info
    ExchangeInfo exchangeInfo = client.getExchangeInfo();
    System.out.println(exchangeInfo.getTimezone());
    System.out.println(exchangeInfo.getSymbols());

    // Obtain symbol information
    SymbolInfo symbolInfo = exchangeInfo.getSymbolInfo("ETHBTC");
    System.out.println(symbolInfo.getStatus());

    PriceFilter priceFilter = (PriceFilter) symbolInfo.getSymbolFilter(FilterType.PRICE_FILTER);
    System.out.println(priceFilter.getMinPrice());
    System.out.println(priceFilter.getTickSize());

    // Obtain asset information
    List<Asset> allAssets = client.getAllAssets();
    System.out.println(allAssets.stream().filter(asset -> asset.getAssetCode().equals("BNB")).findFirst().get());
  }
}
