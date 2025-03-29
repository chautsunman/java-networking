package org.example.chroniclequeue;

import net.openhft.chronicle.wire.AbstractMarshallableCfg;

public class TradeEventWithStats extends AbstractMarshallableCfg {
    private final String symbol;
    private double price;
    private int quantity;
    private int totalTrades;

    public TradeEventWithStats(String symbol) {
        this.symbol = symbol;
        this.price = 0.0;
        this.quantity = 0;
        this.totalTrades = 0;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalTrades() {
        return totalTrades;
    }

    public boolean onTradeEvent(TradeEvent tradeEvent) {
        if (tradeEvent.getSymbol().equals(this.symbol)) {
            this.price = tradeEvent.getPrice();
            this.quantity = tradeEvent.getQuantity();
            this.totalTrades++;
            return true;
        }
        return false;
    }
}
