package org.example.chroniclequeue;

import net.openhft.chronicle.wire.AbstractMarshallableCfg;

public class TradeEvent extends AbstractMarshallableCfg {
    private final String symbol;
    private final double price;
    private final int quantity;

    public TradeEvent(String symbol, double price, int quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
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
}
