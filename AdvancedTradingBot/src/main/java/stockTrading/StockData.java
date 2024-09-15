package stockTrading;

public class StockData {
    private String ticker;
    private double currentPrice;
    private double price5DaysAgo;
    private double monthPrice;
    private double yearPrice;
    private double beta;
    private double peRatio;
    private double dividendYield;

    public StockData(String ticker, double currentPrice, double price5DaysAgo, double monthPrice, double yearPrice, double beta, double peRatio, double dividendYield) {
        this.ticker = ticker;
        this.currentPrice = currentPrice;
        this.price5DaysAgo = price5DaysAgo;
        this.monthPrice = monthPrice;
        this.yearPrice = yearPrice;
        this.beta = beta;
        this.peRatio = peRatio;
        this.dividendYield = dividendYield;
    }
    public StockData(String ticker) {
        this.ticker = ticker;
        this.currentPrice = Double.NaN;
        this.price5DaysAgo = Double.NaN;
        this.monthPrice = Double.NaN;
        this.yearPrice = Double.NaN;
        this.beta = Double.NaN;
        this.peRatio = Double.NaN;
        this.dividendYield = Double.NaN;
    }

    public String getTicker() {
        return ticker;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getPrice5DaysAgo() {
        return price5DaysAgo;
    }

    public double getPrice1MonthAgo() {
        return monthPrice;
    }

    public double getPrice1YearAgo() {
        return yearPrice;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public double getBeta() {
        return beta;
    }

    public double getDividendYield() {
        return dividendYield;
    }
    // Setter methods
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setPrice5DaysAgo(double price5DaysAgo) {
        this.price5DaysAgo = price5DaysAgo;
    }

    public void setPrice1MonthAgo(double monthPrice) {
        this.monthPrice = monthPrice;
    }

    public void setPrice1YearAgo(double yearPrice) {
        this.yearPrice = yearPrice;
    }

    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setDividendYield(double dividendYield) {
        this.dividendYield = dividendYield;
    }

    public String toString() {
        return "Stock: " + ticker + ", Current Price: " + currentPrice + ", Price 5 Days Ago: " + price5DaysAgo + ", Price 1 Month Ago: " + monthPrice + ", Price 1 Year Ago: " + yearPrice + ", Beta: " + beta + ", P/E Ratio: " + peRatio;
    }
}
