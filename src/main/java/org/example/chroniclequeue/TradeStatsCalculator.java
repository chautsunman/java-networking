package org.example.chroniclequeue;

import java.util.HashMap;
import java.util.Map;

public class TradeStatsCalculator implements TradeEventListener {
    private final MarketDataListener marketDataListener;

    // symbol --> TradeEventWithStats
    private final Map<String, TradeEventWithStats> tradeStatsMap = new HashMap<>();

    public TradeStatsCalculator(MarketDataListener marketDataListener) {
        // can be a regular synchronized MarketDataListener
        // can be a chronicle-queue MarketDataListener that uses chronicle queue to transport events
        this.marketDataListener = marketDataListener;
    }

    @Override
    public void onTradeEvent(TradeEvent event) {
        // get tradeEventWithStats for symbol
        final TradeEventWithStats tradeEventWithStats = tradeStatsMap.computeIfAbsent(
                event.getSymbol(),
                TradeEventWithStats::new
        );
        // update tradeEventWithStats and publish
        if (tradeEventWithStats.onTradeEvent(event)) {
            marketDataListener.onTradeEventWithStats(tradeEventWithStats);
        }
    }
}
