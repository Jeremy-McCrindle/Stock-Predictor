package stockTrading;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class webScraper {
    public StockData scrape(String ticker) {
        String encodedTicker = URLEncoder.encode(ticker, StandardCharsets.UTF_8);
        String url = "https://finance.yahoo.com/quote/" + encodedTicker;
        String stockHistoryUrl = url + "/history?p=" + encodedTicker;
        String stockStatisticsUrl = url + "/key-statistics?p=" + encodedTicker;

        try {
            // Fetch the HTML content for the financial metrics
            Document statsDoc = fetchDocument(stockStatisticsUrl);
            String peRatio = getMetric(statsDoc, "Trailing P/E");
            String beta = getMetric(statsDoc, "Beta (5Y Monthly)");
            String dividendYield = getMetric(statsDoc, "Forward Annual Dividend Yield");

            Document historicalDoc = fetchDocument(stockHistoryUrl);
            LocalDate today = LocalDate.now();
            String currentPrice = getClosestPriceOnDate(historicalDoc, today);
            String priceOneYearAgo = getClosestPriceOnDate(historicalDoc, today.minusYears(1));
            String priceOneMonthAgo = getClosestPriceOnDate(historicalDoc, today.minusMonths(1));
            String priceFiveDaysAgo = getClosestPriceOnDate(historicalDoc, today.minusDays(5));

            return new StockData(
                    ticker,
                    parseDoubleSafe(currentPrice),
                    parseDoubleSafe(priceFiveDaysAgo),
                    parseDoubleSafe(priceOneMonthAgo),
                    parseDoubleSafe(priceOneYearAgo),
                    parseDoubleSafe(peRatio),
                    parseDoubleSafe(beta),
                    parseDoubleSafe(dividendYield)
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to " + url);
            return new StockData(ticker);
        }
    }

    private static Document fetchDocument(String url) throws IOException {
        System.out.println("Connecting to: " + url);
        return Jsoup.connect(url).get();
    }

    private static String getMetric(Document doc, String metricName) {
        Elements rows = doc.select("table tbody tr");
        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.size() > 1 && cols.get(0).text().contains(metricName)) {
                return cols.get(1).text().replaceAll("[^\\d.]", ""); // Extract only the numeric part
            }
        }
        return "N/A";
    }

    private static String getClosestPriceOnDate(Document historicalDoc, LocalDate targetDate) {
        Elements rows = historicalDoc.select("table tbody tr");
        String closestPrice = "N/A";
        long minDaysDifference = Long.MAX_VALUE;

        for (Element row : rows) {
            Elements columns = row.select("td");
            if (columns.size() > 1) {
                String rowDateStr = columns.get(0).text();
                try {
                    LocalDate rowDate = LocalDate.parse(rowDateStr, DateTimeFormatter.ofPattern("MMM d, yyyy"));
                    long daysDifference = Math.abs(ChronoUnit.DAYS.between(targetDate, rowDate));
                    if (daysDifference < minDaysDifference) {
                        minDaysDifference = daysDifference;
                        closestPrice = columns.get(4).text().replaceAll("[^\\d.]", ""); // Extract only the numeric part
                    }
                } catch (Exception e) {
                    // Ignore parsing exceptions and continue
                }
            }
        }
        return closestPrice;
    }

    private static double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return Double.NaN; // or any other default value
        }
    }
}
