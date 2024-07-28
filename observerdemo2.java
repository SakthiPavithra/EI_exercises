import java.util.ArrayList;
import java.util.List;

// Observer interface
interface StockObserver {
    void update(String stockSymbol, double stockPrice);
}

// Concrete Observers
class StockBroker implements StockObserver {
    private String brokerName;

    public StockBroker(String brokerName) {
        this.brokerName = brokerName;
    }

    @Override
    public void update(String stockSymbol, double stockPrice) {
        System.out.println(brokerName + " received update: " + stockSymbol + " is now $" + stockPrice);
    }
}

class TradingSystem implements StockObserver {
    @Override
    public void update(String stockSymbol, double stockPrice) {
        System.out.println("Trading system received update: " + stockSymbol + " is now $" + stockPrice);
    }
}

// Subject class
class StockMarket {
    private List<StockObserver> observers;
    private String stockSymbol;
    private double stockPrice;

    public StockMarket(String stockSymbol, double stockPrice) {
        this.stockSymbol = stockSymbol;
        this.stockPrice = stockPrice;
        this.observers = new ArrayList<>();
    }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
        notifyObservers();
    }

    private void notifyObservers() {
        for (StockObserver observer : observers) {
            observer.update(stockSymbol, stockPrice);
        }
    }
}

// Main class to demonstrate the Observer pattern
public class observerdemo2 {
    public static void main(String[] args) {
        StockMarket appleStock = new StockMarket("AAPL", 150.00);

        StockBroker broker1 = new StockBroker("Broker 1");
        StockBroker broker2 = new StockBroker("Broker 2");
        TradingSystem tradingSystem = new TradingSystem();

        appleStock.addObserver(broker1);
        appleStock.addObserver(broker2);
        appleStock.addObserver(tradingSystem);

        // Changing stock price
        System.out.println("Apple stock price updated:");
        appleStock.setStockPrice(155.00);

        System.out.println("\nApple stock price updated again:");
        appleStock.setStockPrice(160.00);
    }
}
