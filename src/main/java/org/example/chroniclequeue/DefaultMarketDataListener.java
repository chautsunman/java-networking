package org.example.chroniclequeue;

import java.util.HashMap;
import java.util.Map;

public class DefaultMarketDataListener implements MarketDataListener {
    private final Map<String, TradeEventWithStats> tradeStatsMap = new HashMap<>();

    @Override
    public void onTradeEventWithStats(TradeEventWithStats tradeEventWithStats) {
        tradeStatsMap.put(tradeEventWithStats.getSymbol(), tradeEventWithStats);
    }

    public Map<String, TradeEventWithStats> getTradeStatsMap() {
        return tradeStatsMap;
    }
}
