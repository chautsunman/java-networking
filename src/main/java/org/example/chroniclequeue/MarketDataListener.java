package org.example.chroniclequeue;

public interface MarketDataListener {
    void onTradeEventWithStats(TradeEventWithStats tradeEventWithStats);
}
