package org.example.chroniclequeue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeEventTest {
    @Test
    public void testToString() {
        final TradeEvent tradeEvent = new TradeEvent("GOOG", 100.0, 1000);
        assertEquals("""
!org.example.chroniclequeue.TradeEvent {
  symbol: GOOG,
  price: 100.0,
  quantity: 1000
}
""", tradeEvent.toString());
    }
}
