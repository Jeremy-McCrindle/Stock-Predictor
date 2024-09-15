package stockTrading;

public class test {
    public static void main(String[] args) {
        webScraper scraper = new webScraper();
        StockData stock = scraper.scrape("AAPL");
        System.out.print(stock);
    }
}
