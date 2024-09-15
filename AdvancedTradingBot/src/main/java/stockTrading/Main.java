package stockTrading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    //declare layout
    private CardLayout cardLayout;
    //declare panels
    private JPanel menuPanel;
    private JPanel investigateStockPanel;
    private JPanel viewStockPanel;
    private JPanel tickerStockPanel;

    //declare jtextfield variables that help the bot decide if buy, sell, or hold
    private JTextField tickerField;
    private JTextField currentPriceField;
    private JTextField price5DaysAgoField;
    private JTextField price1MonthAgoField;
    private JTextField price1YearAgoField;
    private JTextField peRatioField;
    private JTextField betaField;
    private JTextField dividendYieldField;
    private JLabel decisionLabel;

    //background image
    private Image backgroundImage = new ImageIcon("img.png").getImage();

    //font and colour for buttons
    private Font buttonFont = new Font("Arial", Font.BOLD, 16);
    private Color fontColor = new Color(0, 102, 204);

    //declare objects
    private Portfolio portfolio;

    public Main() {
        //set size and title
        setTitle("Advanced Trading Bot");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set up close operation
        setLocationRelativeTo(null); // Center the frame

        // Initialize objects
        portfolio = new Portfolio();

        //Intialize layout
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        //Initialize panels
        menuPanel = createMenuPanel();
        viewStockPanel = viewStocks();
        tickerStockPanel = createTickerStockPanel();

        //create panels
        getContentPane().add(menuPanel, "Menu");
        getContentPane().add(tickerStockPanel, "TickerStock");
        getContentPane().add(viewStockPanel, "ViewStock");
    }

    //main menu panel that has buttons to go to different panels
    private JPanel createMenuPanel() {
        //set the background to an image previously declared
        BackgroundPanel menuPanel = new BackgroundPanel(backgroundImage);

        //set layout of menu panel
        menuPanel.setLayout(new GridBagLayout());
        BorderFactory.createEmptyBorder();
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //set up contraints for grid layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between each button
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make sure buttons fill horizontally

        // Create and style investigate button
        MenuButtons investigateStockButton = new MenuButtons("Investigate Stock", new Color(70, 130, 180), 20);
        investigateStockButton.setFont(buttonFont); //set font
        investigateStockButton.setForeground(fontColor); //set font colour
        investigateStockButton.addActionListener(e -> cardLayout.show(getContentPane(), "TickerStock")); //open ticker window on press
        //set to first row on grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(investigateStockButton, gbc); //add button to menu panel

        // Create and style view stock list button
        MenuButtons viewStockButton = new MenuButtons("View Stock List", new Color(70, 130, 180), 20);
        viewStockButton.setFont(buttonFont); //set font
        viewStockButton.setForeground(fontColor); //set font colour
        viewStockButton.addActionListener(e -> {
            viewStockPanel.removeAll(); //clears previous content pane
            viewStockPanel.add(viewStocks()); //add newest stocks
            cardLayout.show(getContentPane(), "ViewStock"); //show pane
        });
        //set to second row on grid
        gbc.gridy = 1;
        menuPanel.add(viewStockButton, gbc); //add button to menu panel

        // Create and style exit button
        MenuButtons exitButton = new MenuButtons("Exit", new Color(70, 130, 180), 20);
        exitButton.setFont(buttonFont); //set font
        exitButton.setForeground(fontColor); //set font colour
        exitButton.addActionListener(e -> {
            Portfolio.savePortfolio(); //save portfolio
            System.exit(0);
        }); //exit program when clicked
        //set to second row on grid
        gbc.gridy = 2;
        menuPanel.add(exitButton, gbc); //add button to menu panel

        return menuPanel;
    }

    //ticker panel that prompts the user to enter a ticker symbol to then either go to modify or investigate
    private JPanel createTickerStockPanel() {
        //set up panel with a background image
        BackgroundPanel tickerPanel = new BackgroundPanel(backgroundImage);
        tickerPanel.setLayout(new GridBagLayout()); //set layout as a grid layout
        tickerPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100)); //set up borders

        //set up contraints for grid layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10); // Add padding between each button
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make sure buttons fill horizontally

        //set up title to prompt user to input ticker symbol
        JLabel tickerPromptLabel = new JLabel("Enter Ticker Symbol", JLabel.CENTER);
        tickerPromptLabel.setForeground(Color.black); //set font colour
        tickerPromptLabel.setOpaque(false); //make sure background of text is transparent
        Font font = tickerPromptLabel.getFont();
        tickerPromptLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD)); //set font and make font bold
        //set to row 1 on screen
        gbc.gridx = 0;
        gbc.gridy = 0;
        tickerPanel.add(tickerPromptLabel, gbc); //add label to ticker panel

        //add panel to insert ticker symbol
        JPanel tickerInputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        tickerInputPanel.setOpaque(false); //set background to be transparent
        //set up tickerfield for value of ticker
        JTextField tickerField = new JTextField();
        tickerField.setOpaque(false); //set background of field to be transparent
        tickerInputPanel.add(new JLabel("", JLabel.RIGHT)); //add space about insert area
        tickerInputPanel.add(tickerField); //add ticker value
        gbc.gridy = 1; // Move the text field to the next row
        tickerPanel.add(tickerInputPanel, gbc); //add to ticker panel

        //add enter button
        EnterButtons enterButton = new EnterButtons("Enter", Color.WHITE, 10);
        enterButton.setFont(buttonFont); //set font
        enterButton.setForeground(fontColor); //set font colour
        gbc.gridy = 2; // place on row 3
        tickerPanel.add(enterButton, gbc); //add to panel

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StockData stock = portfolio.getStockByTicker(tickerField.getText());
                if(stock == null){
                    investigateStockPanel = createInvestigateStockPanel(tickerField.getText());
                }else{
                    investigateStockPanel = createInvestigateStockPanel(stock.getTicker(),stock.getCurrentPrice(),stock.getPrice5DaysAgo(),stock.getPrice1MonthAgo(), stock.getPrice1YearAgo(), stock.getPeRatio(),stock.getBeta(), stock.getDividendYield());
                }
                getContentPane().add(investigateStockPanel, "InvestigateStock"); // Update the instance variable
                cardLayout.show(getContentPane(), "InvestigateStock");
            }
        });

        return tickerPanel; // Return the background panel with components
    }

    private JPanel createInvestigateStockPanel(String ticker) {
        webScraper scraper = new webScraper();
        StockData stock = scraper.scrape(ticker);

        //Container mainContainer = null;
        BackgroundPanel investigatePanel = new BackgroundPanel(backgroundImage);
        investigatePanel.setLayout(new BorderLayout(10, 10));
        investigatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
// Input panel with GridLayout
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        inputPanel.setOpaque(false);
        // Initialize the input fields
        tickerField = new JTextField(ticker);
        currentPriceField = new JTextField(doubleToString(stock.getCurrentPrice()));
        price5DaysAgoField = new JTextField(doubleToString(stock.getPrice5DaysAgo()));
        price1MonthAgoField = new JTextField(doubleToString(stock.getPrice1MonthAgo()));
        price1YearAgoField = new JTextField(doubleToString(stock.getPrice1YearAgo()));
        peRatioField = new JTextField(doubleToString(stock.getPeRatio()));
        betaField = new JTextField(doubleToString(stock.getBeta()));
        dividendYieldField = new JTextField(doubleToString(stock.getDividendYield()));

        // Add components to the input panel
        inputPanel.add(new JLabel("Ticker Symbol:", JLabel.RIGHT));
        inputPanel.add(tickerField);

        inputPanel.add(new JLabel("Current Price:", JLabel.RIGHT));
        inputPanel.add(currentPriceField);

        inputPanel.add(new JLabel("Price 5 Days Ago:", JLabel.RIGHT));
        inputPanel.add(price5DaysAgoField);

        inputPanel.add(new JLabel("Price 1 Month Ago:", JLabel.RIGHT));
        inputPanel.add(price1MonthAgoField);

        inputPanel.add(new JLabel("Price 1 Year Ago:", JLabel.RIGHT));
        inputPanel.add(price1YearAgoField);

        inputPanel.add(new JLabel("P/E Ratio:", JLabel.RIGHT));
        inputPanel.add(peRatioField);

        inputPanel.add(new JLabel("Beta:", JLabel.RIGHT));
        inputPanel.add(betaField);

        inputPanel.add(new JLabel("Dividend Yield (%):", JLabel.RIGHT));
        inputPanel.add(dividendYieldField);
        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(Color.black);
                Font font = label.getFont();
                label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
            }
        }

        // Button panel with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton calculate1Button = new JButton("Calculate");
        calculate1Button.setPreferredSize(new Dimension(100, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.add(calculate1Button);

        JButton addButton = new JButton("Buy");
        addButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(addButton);

        JButton removeButton = new JButton("Sell");
        removeButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(removeButton);

        JButton menuButton = new JButton("Menu");
        menuButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(menuButton);

        // Decision label
        decisionLabel = new JLabel("The bot recommends you to: ", JLabel.CENTER);
        decisionLabel.setForeground(Color.black);
        Font font = decisionLabel.getFont();
        decisionLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

        // Add components to the main panel
        investigatePanel.add(inputPanel, BorderLayout.CENTER);
        investigatePanel.add(buttonPanel, BorderLayout.SOUTH);
        investigatePanel.add(decisionLabel, BorderLayout.NORTH);

        // Add main panel to the frame
        add(investigatePanel);
        menuButton.addActionListener(e -> cardLayout.show(getContentPane(), "Menu"));
        calculate1Button.addActionListener(e -> calculateDecision());
        addButton.addActionListener(e -> addStock());
        removeButton.addActionListener(e -> removeStock());
        return investigatePanel;
    }

    private JPanel createInvestigateStockPanel(String ticker, double currentPrice, double price5daysAgo, double price1MonthAgo, double price1YearAgo, double peRatio, double beta, double dividend) {
        //Container mainContainer = null;
        BackgroundPanel investigatePanel = new BackgroundPanel(backgroundImage);
        investigatePanel.setLayout(new BorderLayout(10, 10));
        investigatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
// Input panel with GridLayout
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        inputPanel.setOpaque(false);
        // Initialize the input fields
        tickerField = new JTextField(ticker);
        currentPriceField = new JTextField(doubleToString(currentPrice));
        price5DaysAgoField = new JTextField(doubleToString(price5daysAgo));
        price1MonthAgoField = new JTextField(doubleToString(price1MonthAgo));
        price1YearAgoField = new JTextField(doubleToString(price1YearAgo));
        peRatioField = new JTextField(doubleToString(peRatio));
        betaField = new JTextField(doubleToString(beta));
        dividendYieldField = new JTextField(doubleToString(dividend));

        // Add components to the input panel
        inputPanel.add(new JLabel("Ticker Symbol:", JLabel.RIGHT));
        inputPanel.add(tickerField);
        inputPanel.add(new JLabel("Current Price:", JLabel.RIGHT));
        inputPanel.add(currentPriceField);
        inputPanel.add(new JLabel("Price 5 Days Ago:", JLabel.RIGHT));
        inputPanel.add(price5DaysAgoField);
        inputPanel.add(new JLabel("Price 1 Month Ago:", JLabel.RIGHT));
        inputPanel.add(price1MonthAgoField);
        inputPanel.add(new JLabel("Price 1 Year Ago:", JLabel.RIGHT));
        inputPanel.add(price1YearAgoField);
        inputPanel.add(new JLabel("P/E Ratio:", JLabel.RIGHT));
        inputPanel.add(peRatioField);
        inputPanel.add(new JLabel("Beta:", JLabel.RIGHT));
        inputPanel.add(betaField);
        inputPanel.add(new JLabel("Dividend Yield (%):", JLabel.RIGHT));
        inputPanel.add(dividendYieldField);
        for (Component component : inputPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(Color.black);
                Font font = label.getFont();
                label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
            }
        }
        // Button panel with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.add(calculateButton);

        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(updateButton);

        JButton removeButton = new JButton("Sell");
        removeButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(removeButton);

        JButton menuButton = new JButton("Menu");
        menuButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(menuButton);

        // Decision label
        decisionLabel = new JLabel("The bot recommends you to: ", JLabel.CENTER);
        decisionLabel.setForeground(Color.black);
        Font font = decisionLabel.getFont();
        decisionLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));

        // Add components to the main panel
        investigatePanel.add(inputPanel, BorderLayout.CENTER);
        investigatePanel.add(buttonPanel, BorderLayout.SOUTH);
        investigatePanel.add(decisionLabel, BorderLayout.NORTH);

        // Add main panel to the frame
        add(investigatePanel);

        menuButton.addActionListener(e -> cardLayout.show(getContentPane(), "Menu"));
        calculateButton.addActionListener(e -> calculateDecision());
        updateButton.addActionListener(e -> updateStock());
        removeButton.addActionListener(e -> removeStock());
        return investigatePanel;
    }

    private JPanel viewStocks() {
        JPanel viewStocksPanel = new JPanel(new BorderLayout());
        viewStocksPanel.setLayout(new BorderLayout(10, 10));
        viewStocksPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Scrollable panel for stocks
        JPanel stocksPanel = new JPanel();
        stocksPanel.setLayout(new BoxLayout(stocksPanel, BoxLayout.Y_AXIS));
        stocksPanel.setOpaque(false);

        // Add stocks to the panel
        for (StockData stock : portfolio.getPortfolio()) {
            JPanel stockPanel = createStockPanel(stock);
            stocksPanel.add(stockPanel);
        }

        // Create a scroll pane
        JScrollPane scrollPane = new JScrollPane(stocksPanel);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        viewStocksPanel.add(scrollPane, BorderLayout.CENTER);

        // Menu button to go back
        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(e -> cardLayout.show(getContentPane(), "Menu"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(menuButton);
        buttonPanel.setOpaque(false);
        viewStocksPanel.add(buttonPanel, BorderLayout.SOUTH);

        return viewStocksPanel;
    }
    private JPanel createStockPanel(StockData stock) {
        JPanel stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        stockPanel.setOpaque(false);

        // Create labels for each line of information
        JLabel tickerLabel = new JLabel("Ticker: " + stock.getTicker());
        JLabel currentPriceLabel = new JLabel("Current Price: " + stock.getCurrentPrice());
        JLabel pricesLabel = new JLabel("5 Days Ago: " + stock.getPrice5DaysAgo() +
                ", 1 Month Ago: " + stock.getPrice1MonthAgo() +
                ", 1 Year Ago: " + stock.getPrice1YearAgo());
        JLabel ratiosLabel = new JLabel("P/E Ratio: " + stock.getPeRatio() +
                ", Beta: " + stock.getBeta() +
                ", Dividend Yield: " + stock.getDividendYield());

        // Set font and color for the labels
        Font baseFont = new Font("Arial", Font.PLAIN, 12);
        Font largerFont = baseFont.deriveFont(baseFont.getSize2D() * 1.25f);

        tickerLabel.setFont(largerFont);
        currentPriceLabel.setFont(largerFont);
        pricesLabel.setFont(largerFont);
        ratiosLabel.setFont(largerFont);

        tickerLabel.setForeground(Color.black);
        currentPriceLabel.setForeground(Color.black);
        pricesLabel.setForeground(Color.black);
        ratiosLabel.setForeground(Color.black);

        // Create buttons
        JButton calculateButton = new JButton("Calculate");
        JButton updateButton = new JButton("Update");
        JButton modifyButton = new JButton("Modify");

        // Set font for buttons
        calculateButton.setFont(baseFont);
        updateButton.setFont(baseFont);
        modifyButton.setFont(baseFont);

        // Panel for the top line (ticker and buttons)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(tickerLabel);
        topPanel.add(Box.createHorizontalStrut(10)); // Add space between label and buttons
        topPanel.add(calculateButton);
        topPanel.add(updateButton);
        topPanel.add(modifyButton);

        // Panel for the second line (current price)
        JPanel currentPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        currentPricePanel.setOpaque(false);
        currentPricePanel.add(currentPriceLabel);

        // Panel for the third line (prices)
        JPanel pricesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricesPanel.setOpaque(false);
        pricesPanel.add(pricesLabel);

        // Panel for the fourth line (ratios)
        JPanel ratiosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratiosPanel.setOpaque(false);
        ratiosPanel.add(ratiosLabel);

        // Add panels to the stockPanel
        stockPanel.add(topPanel);
        stockPanel.add(currentPricePanel);
        stockPanel.add(pricesPanel);
        stockPanel.add(ratiosPanel);

        // Calculate the preferred height and set it
        int minHeight = (int) ((topPanel.getPreferredSize().height + currentPricePanel.getPreferredSize().height +
                pricesPanel.getPreferredSize().height + ratiosPanel.getPreferredSize().height));
        stockPanel.setPreferredSize(new Dimension(stockPanel.getPreferredSize().width, minHeight));

        // Add action listeners for the buttons
        //NEED TO MODIFY THE FUNCTIONS TO WORK WITH THIS AS WELL
        //calculateButton.addActionListener(e -> calculateAction(stock));
        //updateButton.addActionListener(e -> updateAction(stock));
        //modifyButton.addActionListener(e -> modifyAction(stock));

        return stockPanel;
    }










    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
    private void calculateDecision() {
        try {
            // Get the input values
            double currentPrice = Double.parseDouble(currentPriceField.getText());
            double price5DaysAgo = Double.parseDouble(price5DaysAgoField.getText());
            double price1MonthAgo = Double.parseDouble(price1MonthAgoField.getText());
            double price1YearAgo = Double.parseDouble(price1YearAgoField.getText());
            double peRatio = Double.parseDouble(peRatioField.getText());
            double beta = Double.parseDouble(betaField.getText());
            double dividendYield = Double.parseDouble(dividendYieldField.getText());

            // Calculate SMA and RSI
            double sma = calculateSMA(price5DaysAgo, price1MonthAgo, price1YearAgo);
            double rsi = calculateRSI(price5DaysAgo, price1MonthAgo, price1YearAgo, currentPrice);

            // Make a decision based on the calculated values and input metrics
            String decision = makeDecision(currentPrice, sma, rsi, peRatio, beta, dividendYield);

            // Display the decision
            System.out.println("The bot recommends you to: " + decision);
            decisionLabel.setText("The bot recommends you to: " + decision);


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateStock() {
        try {
            // Get the input values
            String ticker = tickerField.getText();
            double currentPrice = Double.parseDouble(currentPriceField.getText());
            double price5DaysAgo = Double.parseDouble(price5DaysAgoField.getText());
            double price1MonthAgo = Double.parseDouble(price1MonthAgoField.getText());
            double price1YearAgo = Double.parseDouble(price1YearAgoField.getText());
            double peRatio = Double.parseDouble(peRatioField.getText());
            double beta = Double.parseDouble(betaField.getText());
            double dividendYield = Double.parseDouble(dividendYieldField.getText());

            // Update the stock information in the portfolio
            portfolio.updateStock(ticker, currentPrice, price5DaysAgo, price1MonthAgo, price1YearAgo, peRatio, beta, dividendYield);

            // Optionally, you can display a success message
            JOptionPane.showMessageDialog(this, "Stock information updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addStock() {
        try {
            // Get the input values
            String ticker = tickerField.getText();
            double currentPrice = Double.parseDouble(currentPriceField.getText());
            double price5DaysAgo = Double.parseDouble(price5DaysAgoField.getText());
            double price1MonthAgo = Double.parseDouble(price1MonthAgoField.getText());
            double price1YearAgo = Double.parseDouble(price1YearAgoField.getText());
            double peRatio = Double.parseDouble(peRatioField.getText());
            double beta = Double.parseDouble(betaField.getText());
            double dividendYield = Double.parseDouble(dividendYieldField.getText());

            portfolio.addStock(new StockData(ticker, currentPrice, price5DaysAgo, price1MonthAgo, price1YearAgo, peRatio, beta, dividendYield));
            JOptionPane.showMessageDialog(this, "Stock added to portfolio.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void removeStock() {
        String ticker = tickerField.getText();
        portfolio.removeStock(ticker);
    }
    private double calculateSMA(double price5DaysAgo, double price1MonthAgo, double price1YearAgo) {
        return (price5DaysAgo + price1MonthAgo + price1YearAgo) / 3;
    }

    private double calculateRSI(double price5DaysAgo, double price1MonthAgo, double price1YearAgo, double currentPrice) {
        double avgGain = 0;
        double avgLoss = 0;
        double[] priceChanges = {currentPrice - price5DaysAgo, price5DaysAgo - price1MonthAgo, price1MonthAgo - price1YearAgo};

        for (double change : priceChanges) {
            if (change > 0) {
                avgGain += change;
            } else {
                avgLoss -= change;
            }
        }

        avgGain /= priceChanges.length;
        avgLoss /= priceChanges.length;

        if (avgLoss == 0) {
            return 100;
        }

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
    private String makeDecision(double currentPrice, double sma, double rsi, double peRatio, double beta, double dividendYield) {
        // Sample decision logic combining various metrics
        if (currentPrice > sma && rsi < 70 && peRatio < 20 && beta < 1.5 && dividendYield > 2) {
            return "Buy";
        } else if (currentPrice < sma && rsi > 30 && peRatio > 25 && beta > 1.5 && dividendYield < 2) {
            return "Sell";
        } else {
            return "Hold";
        }
    }
    private String doubleToString(double num){
        if(Double.isNaN(num)) return "";
        else return Double.toString(num);
    }
}
