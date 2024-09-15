package stockTrading;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Scanner;

public class Portfolio extends Component implements Serializable {

    private static final long serialVersionUID = 1L;
    private static ArrayList<StockData> myPortfolio;

    public Portfolio() {
        myPortfolio = new ArrayList<>();
        loadPortfolio();
    }

    public void addStock(StockData stockInfo) {
        myPortfolio.add(stockInfo);
        for (int i = 0; i < myPortfolio.size(); i++)
            System.out.println(myPortfolio.get(i));

    }
    public void updateStock(String ticker, double currentPrice, double price5DaysAgo, double price1MonthAgo, double price1YearAgo, double peRatio, double beta, double dividendYield) {
        for (StockData stock : myPortfolio) {
            if (stock.getTicker().equals(ticker)) {
                // Update the stock information
                stock.setCurrentPrice(currentPrice);
                stock.setPrice5DaysAgo(price5DaysAgo);
                stock.setPrice1MonthAgo(price1MonthAgo);
                stock.setPrice1YearAgo(price1YearAgo);
                stock.setPeRatio(peRatio);
                stock.setBeta(beta);
                stock.setDividendYield(dividendYield);
                return; // Exit the method once the stock is updated
            }
        }
        // If the stock is not found, you may choose to handle it accordingly (e.g., display an error message)
    }

    // Remove method removed to ensure stocks stay in the portfolio
    public void removeStock(String ticker) {
        boolean removed = myPortfolio.removeIf(stock -> stock.getTicker().equalsIgnoreCase(ticker));
        if (removed) {
            JOptionPane.showMessageDialog(this, "Stock removed from portfolio.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Stock not found in portfolio.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public StockData getStockByTicker(String ticker) {
        for(int i = 0; i < myPortfolio.size(); i++) {
            StockData stock = myPortfolio.get(i);
            if (stock.getTicker().equalsIgnoreCase(ticker)) {
                if ( i > 0) {
                    myPortfolio.set(i, myPortfolio.get(i - 1));
                    myPortfolio.set(i - 1, stock);
                }
                return stock;
            }
        }
        return null;
    }
    public ArrayList<StockData> getPortfolio() {
        return myPortfolio;
    }

    public static void savePortfolio() {
        try {
            PrintWriter output = new PrintWriter("saveState.txt");
            for (StockData stock : myPortfolio) {
                output.print(stock.getTicker() + " ");
                output.print(stock.getCurrentPrice() + " ");
                output.print(stock.getPrice5DaysAgo() + " ");
                output.print(stock.getPrice1MonthAgo() + " ");
                output.print(stock.getPrice1YearAgo() + " ");
                output.print(stock.getPeRatio() + " ");
                output.print(stock.getBeta() + " ");
                output.println(stock.getDividendYield());
            }
            output.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Was unable to save file");
        }
    }

    public void loadPortfolio() {
        myPortfolio.clear(); // Clear the current portfolio to avoid duplicates
        try (Scanner load = new Scanner(new File("saveState.txt"))) {
            while (load.hasNext()) {
                String ticker = load.next();
                if (load.hasNextDouble()) {
                    double currentPrice = load.nextDouble();
                    double price5DaysAgo = load.nextDouble();
                    double price1MonthAgo = load.nextDouble();
                    double price1YearAgo = load.nextDouble();
                    double peRatio = load.nextDouble();
                    double beta = load.nextDouble();
                    double dividendYield = load.nextDouble();
                    myPortfolio.add(new StockData(ticker, currentPrice, price5DaysAgo, price1MonthAgo, price1YearAgo, peRatio, beta, dividendYield));
                } else {
                    JOptionPane.showMessageDialog(null, "File format is incorrect");
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File could not load");
        }
    }

}

