package org.example.chroniclequeue;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MarketDataChronicleQueueTest {
    @Test
    public void testMarketDataQueue() {
        final File queuePath = new File(OS.getTarget(), "testMarketDataQueue-" + System.nanoTime());

        try {
            // publisher
            try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(queuePath).build()) {
                // create chronicle-queue MarketDataListener
                final MarketDataListener marketDataListener = queue.createAppender().methodWriter(MarketDataListener.class);

                // create TradeStatsCalculator which will publish events to chronicle-queue MarketDataListener
                final TradeStatsCalculator tradeStatsCalculator = new TradeStatsCalculator(marketDataListener);

                // raw events
                tradeStatsCalculator.onTradeEvent(new TradeEvent("GOOG", 100.0, 1000));
                tradeStatsCalculator.onTradeEvent(new TradeEvent("GOOG", 100.0, 2000));
            }

            // consumer
            try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(queuePath).build()) {
                // create chronicle-queue MarketDataListener consumer
                final DefaultMarketDataListener marketDataListener = new DefaultMarketDataListener();
                final MethodReader methodReader = queue.createTailer().methodReader(marketDataListener);

                // consume events
                for (int i = 0; i < 2; i++) {
                    assertTrue(methodReader.readOne());
                }
                assertFalse(methodReader.readOne());

                final Map<String, TradeEventWithStats> tradeStatsMap = marketDataListener.getTradeStatsMap();
                assertEquals(1, tradeStatsMap.size());
                final TradeEventWithStats tradeEventWithStats = tradeStatsMap.get("GOOG");
                assertEquals("GOOG", tradeEventWithStats.getSymbol());
                assertEquals(100.0, tradeEventWithStats.getPrice());
                assertEquals(2000, tradeEventWithStats.getQuantity());
                assertEquals(2, tradeEventWithStats.getTotalTrades());

                // inspect the queue
                System.out.println(queue.dump());
            }
        } finally {
            IOTools.shallowDeleteDirWithFiles(queuePath);
        }
    }
}
