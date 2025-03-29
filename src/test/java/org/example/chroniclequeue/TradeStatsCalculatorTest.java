package org.example.chroniclequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TradeStatsCalculatorTest {
    private TradeStatsCalculator tradeStatsCalculator;

    @Mock
    private MarketDataListener marketDataListener;

    @Captor
    private ArgumentCaptor<TradeEventWithStats> tradeEventWithStatsCaptor;

    @BeforeEach
    public void setUp() {
        tradeStatsCalculator = new TradeStatsCalculator(marketDataListener);
    }

    @Test
    public void testOnTradeEvent() {
        final TradeEvent tradeEvent = new TradeEvent("GOOG", 100.0, 1000);
        tradeStatsCalculator.onTradeEvent(tradeEvent);

        Mockito.verify(marketDataListener, Mockito.times(1))
                .onTradeEventWithStats(tradeEventWithStatsCaptor.capture());
        final TradeEventWithStats tradeEventWithStats = tradeEventWithStatsCaptor.getValue();
        assertEquals("GOOG", tradeEventWithStats.getSymbol());
        assertEquals(100.0, tradeEventWithStats.getPrice());
        assertEquals(1000, tradeEventWithStats.getQuantity());
        assertEquals(1, tradeEventWithStats.getTotalTrades());
    }
}
