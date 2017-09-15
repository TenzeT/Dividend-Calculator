package dividendcalculator;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

public class DividendCalculator {
    // Create fields
    static double monthlyTotalDiv = 0;
    static final int arraySize = 20;
    static URLObject urlObject = new URLObject(arraySize);
    static DivURLObject divObject = new DivURLObject(arraySize);
    static String[] symbolArray = new String[arraySize];
    static String[] priceArray = new String[arraySize];
    static String[] monthlyDivArray = new String[arraySize];
    
    // Populate symbol array
    public static void setSymbols() {
        symbolArray[0] = "VDE";
        symbolArray[1] = "VWO";
        symbolArray[2] = "NEWT";
        symbolArray[3] = "STAG";
        symbolArray[4] = "ACP";
        symbolArray[5] = "SPYD";
        symbolArray[6] = "VYM";
        symbolArray[7] = "CLDT";
        symbolArray[8] = "USB";
        symbolArray[9] = "BNS";
        symbolArray[10] = "CEFL";
        symbolArray[11] = "BDCL";
        symbolArray[12] = "CHI";
        symbolArray[13] = "EMG";
        symbolArray[14] = "AHGP";
        symbolArray[15] = "MAIN";
        symbolArray[16] = "BPT";
        symbolArray[17] = "ORC";
        symbolArray[18] = "SCHB";
        symbolArray[19] = "TFI";
    }
    
    // Populate stock price array
    public static void setPrices() {
        for(int i = 0; i < 3; i++) {
            GoogleStockReader stockReader = new GoogleStockReader(symbolArray[i], urlObject.getURL(i));
            priceArray[i] = stockReader.s;
            System.out.println(priceArray[i]);
        }
    }
    
    // Populate dividend array
    public static void setDividends() {
        for(int i = 0; i < 3; i++) {
            if(i == 2) {
                monthlyDivArray[i] = "0.12"; // Because ACP div not on Nasdaq
                continue;
            }
            NasdaqDivReader divReader = new NasdaqDivReader(symbolArray[i], divObject.getDivURL(i));
            monthlyDivArray[i] = divReader.div;
            System.out.println(monthlyDivArray[i]);
        }
    }
    
    // Overloaded for Calculate Button action
    public static double calculateTotalMonthlyDiv(JTextField[] jtf) {
        monthlyTotalDiv = 0;  
        try {
            for(int i = 0; i < 3; i++) {
                double numShare = Double.parseDouble(jtf[i].getText());
                double divAmount = Double.parseDouble(monthlyDivArray[i]);
                monthlyTotalDiv += (numShare * divAmount);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e);
        }
        System.out.println("Total: " + monthlyTotalDiv);
        return monthlyTotalDiv;
    }
    
    public static double getTotalIndividualMonthlyDiv(String div, int index, JTextField[] jtf) {
        double totalIndividualMonthlyDiv = 0;
        double numShare = 0;
        
        try {
            numShare = Double.parseDouble(jtf[index].getText());
            double divAmount = Double.parseDouble(div);
            totalIndividualMonthlyDiv = numShare * divAmount;
        } catch (NumberFormatException e) {
            System.out.println("error: " + e);
        }
        return totalIndividualMonthlyDiv;
    }
    
    public DividendCalculator() { 
      
      ImageIcon icon = new ImageIcon(this.getClass().getResource("\\images\\if_Money_206469.png")); // Get icon from src/images
      JFrame frame = new JFrame("Dividend Calculator");
      frame.setIconImage(icon.getImage());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new FlowLayout());
      
      // Create columns of JLabels/Textfields
      JLabel[] jlabSymbolArray = new JLabel[3];
      JTextField[] jtfShareArray = new JTextField[3];
      JLabel[] jlabPriceArray = new JLabel[3];
      JLabel[] jlabMonthlyDivArray = new JLabel[3];
      
      for(int i = 0; i < 3; i++) {
          jtfShareArray[i] = new JTextField("1", 5);
          jtfShareArray[i].setHorizontalAlignment(SwingConstants.CENTER);
      }
      
      // Initialize 'column' labels
      Border border = BorderFactory.createEtchedBorder(); // Generic border
      JLabel jlabSymbols = new JLabel("Symbols");
      jlabSymbols.setHorizontalAlignment(SwingConstants.CENTER);
      jlabSymbols.setBorder(border);
      jlabSymbols.setBackground(new Color(204, 255, 204));
      jlabSymbols.setOpaque(true);
      jlabSymbols.setToolTipText("Your stock symbols");
      JLabel jlabShares = new JLabel("Number of Shares");
      jlabShares.setHorizontalAlignment(SwingConstants.CENTER);
      jlabShares.setBorder(border);
      jlabShares.setBackground(new Color(204, 255, 204));
      jlabShares.setOpaque(true);
      jlabShares.setToolTipText("Number of stocks you own");
      JLabel jlabPrice = new JLabel("Stock Price / Total");
      jlabPrice.setHorizontalAlignment(SwingConstants.CENTER);
      jlabPrice.setBorder(border);
      jlabPrice.setBackground(new Color(204, 255, 204));
      jlabPrice.setOpaque(true);
      jlabPrice.setToolTipText("Stock price / Total cost of your stocks");
      JLabel jlabMonthlyDiv = new JLabel("Monthly Div / Total: " + calculateTotalMonthlyDiv(jtfShareArray));
      jlabMonthlyDiv.setHorizontalAlignment(SwingConstants.CENTER);
      jlabMonthlyDiv.setBorder(border);
      jlabMonthlyDiv.setBackground(new Color(204, 255, 204));
      jlabMonthlyDiv.setOpaque(true);
      jlabMonthlyDiv.setToolTipText("Stock monthly dividend / Total dividends from stock");

      // Initialize arrays and center text in labels; create textfields
      for(int i = 0; i < 3; i++) {
          jlabSymbolArray[i] = new JLabel(symbolArray[i]);
          jlabSymbolArray[i].setHorizontalAlignment(SwingConstants.CENTER);
          jlabSymbolArray[i].setBorder(BorderFactory.createEtchedBorder());
          
          jlabPriceArray[i] = new JLabel(priceArray[i] + " / " + priceArray[i]);
          jlabPriceArray[i].setHorizontalAlignment(SwingConstants.CENTER);
          
          String div = monthlyDivArray[i];
          jlabMonthlyDivArray[i] = new JLabel(div+ " / " + getTotalIndividualMonthlyDiv(div, i, jtfShareArray));
          jlabMonthlyDivArray[i].setHorizontalAlignment(SwingConstants.CENTER);
      }
      
      JPanel jpanMain = new JPanel();
      jpanMain.setLayout(new GridLayout(5, 4, 10, 10));
      jpanMain.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      
      jpanMain.add(jlabSymbols);
      jpanMain.add(jlabShares);
      jpanMain.add(jlabPrice);
      jpanMain.add(jlabMonthlyDiv);
      
      // Add arrays to columns in GridLayout
      for(int j = 0; j < 3; j++) {
          jpanMain.add(jlabSymbolArray[j]);
          jpanMain.add(jtfShareArray[j]);
          jpanMain.add(jlabPriceArray[j]);
          jpanMain.add(jlabMonthlyDivArray[j]);
      }
      
      jpanMain.add(new JPanel());
      jpanMain.add(new JPanel());
      JButton jbuttonCalculateTotal = new JButton("Calculate");
      jbuttonCalculateTotal.setHorizontalAlignment(SwingConstants.CENTER);
      jbuttonCalculateTotal.addActionListener((ae) -> { 
        // Calculate total div amount
        double totalMonthly = calculateTotalMonthlyDiv(jtfShareArray);
        BigDecimal bd = new BigDecimal(totalMonthly).setScale(2, RoundingMode.HALF_EVEN); // Need to make into method
        totalMonthly = bd.doubleValue();
        jlabMonthlyDiv.setText("Monthly Div / Total: " + totalMonthly);
        
        // Calculate total individual div amounts, set stock price labels
        for(int i = 0; i < 3; i++) {
            double totalIndiv = 0;
            String div = monthlyDivArray[i];
            totalIndiv = getTotalIndividualMonthlyDiv(div, i, jtfShareArray);
            BigDecimal bd2 = new BigDecimal(totalIndiv).setScale(2, RoundingMode.HALF_EVEN);
            totalIndiv = bd2.doubleValue();
            jlabMonthlyDivArray[i].setText(div + " / " + totalIndiv);
            
            double stockPrice = Double.parseDouble(priceArray[i]);
            double numShares = Double.parseDouble(jtfShareArray[i].getText());
            double totalStockPrice = stockPrice * numShares;
            BigDecimal bd3 = new BigDecimal(totalStockPrice).setScale(2, RoundingMode.HALF_EVEN);
            totalStockPrice = bd3.doubleValue();
            jlabPriceArray[i].setText(stockPrice + " / " + totalStockPrice);
        }
        
        frame.pack(); // Repacks frame when total gets too large
      });
      jpanMain.add(jbuttonCalculateTotal);
      jpanMain.add(new JPanel());
      
      frame.add(jpanMain);
      frame.pack();
      frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        // Set Nimbus look and feel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        setSymbols();
        setPrices();
        setDividends();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DividendCalculator();
            }
        });
    }    
}

class GoogleStockReader {
    public String s;
    
    public GoogleStockReader(String symbol, String symURL) {
        try{
            s = getPrice(symbol, symURL);
        } catch(IOException e) {
            System.out.println("Error:" + e);
        }
    }
        
    public String getPrice(String sym, String stringURL) throws IOException {
        URL url = new URL(stringURL);
        URLConnection urlConn = url.openConnection();
        InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
        BufferedReader buff = new BufferedReader(inStream);
        
        String price = "Not found";
        String lineFind = (":[\"" + sym + "\",\"");
        System.out.println(sym); // Delete
        String line = buff.readLine();
        while(line != null) {
            if(line.contains(lineFind)){
                int target = line.indexOf(lineFind);
                int deci = line.indexOf(".", target);
                int start = deci;
                while(line.charAt(start) != '\"') {
                    start--;
                }
                price = line.substring(start + 1, deci + 3);
            }
            line = buff.readLine();
        }
        return price;
    }
}

class NasdaqDivReader {
    public String div;
    
    public NasdaqDivReader(String symbol, String divUrl) {
        try {
            div = getDiv(symbol, divUrl);
        } catch (IOException e) {
           System.out.println("Error:" + e);
        }
    }
    
    public String getDiv(String sym, String divUrl) throws IOException {
        String symbol = sym;
        URL url = new URL(divUrl);
        URLConnection urlConn = url.openConnection();
        InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
        BufferedReader buff = new BufferedReader(inStream);
        
        String div = "Not found";
        String lineFind = ("<span id=\"quotes_content_left_dividendhistoryGrid_CashAmount_0\">");
        String line = buff.readLine();
        while(line != null) {
            if(line.contains(lineFind)) {
                    int target = line.indexOf(lineFind);
                    int deci = line.indexOf(".", target); //search for index of . starting at target
                    int start = deci;
                    while(line.charAt(start) != '>') {
                        start--;
                    }
                    div = line.substring(start + 1, deci + 3);
                }    
            line = buff.readLine();
        }
        return div;
    }
}

class URLObject {
    int arraySize = 10;
    String[] urlArray;
    
    public URLObject(int size) {
        arraySize = size;
        urlArray = new String[arraySize];
        urlArray[0] = "https://finance.google.com/finance?q=VDE&ei=2p65WfGJNsWljAHboo-wDA"; // VDE
        urlArray[1] = "https://finance.google.com/finance?q=VWO&ei=8jW7WZD6G9T82AbHrI-4AQ"; // VWO
        urlArray[2] = "https://finance.google.com/finance?q=NEWT&ei=Dza7WdLcEIKn2Aacy6zACg"; // NEWT
        urlArray[3] = "https://finance.google.com/finance?q=STAG&ei=Iza7WajRJpL3jAHi6o_wAg"; // STAG
        urlArray[4] = "https://finance.google.com/finance?q=ACP&ei=iDa7WeDWLMmPjAHysZxQ"; // ACP
        urlArray[5] = "https://finance.google.com/finance?q=NYSEARCA%3ASPYD&ei=oza7WZPlIpL3jAHi6o_wAg"; // SPYD
        urlArray[6] = "https://finance.google.com/finance?q=NYSEARCA%3AVYM&ei=xTa7WbnDG5KN2Aafm7DICw"; // VYM
        urlArray[7] = "https://finance.google.com/finance?q=NYSE%3ACLDT&ei=6ja7WYnrE4u-jAH7sreYDQ"; // CLDT
        urlArray[8] = "https://finance.google.com/finance?q=NYSE%3AUSB&ei=-Ta7WYj0EJvU2Ab7g6rwCw"; // USB
        urlArray[9] = "https://finance.google.com/finance?q=NYSE%3ABNS&ei=Eze7WePWLIjYjAHs34rYCQ"; // BNS
        urlArray[10] = "https://finance.google.com/finance?q=NYSEARCA%3ACEFL&ei=ITe7WZnQNsT02Abi6IXQDw"; // CEFL
        urlArray[11] = "https://finance.google.com/finance?q=NYSEARCA%3ABDCL&ei=Oje7Wfj1H8TNjAG5r4LYAw"; // BDCL
        urlArray[12] = ""; // nothing for CHI
        urlArray[13] = "https://finance.google.com/finance?q=NYSEARCA%3AIEMG&ei=TDe7WeHLBojYjAHs34rYCQ"; // IEMG
        urlArray[14] = "https://finance.google.com/finance?q=NASDAQ%3AAHGP&ei=gTe7WcmmFYKn2Aacy6zACg"; // AHGP
        urlArray[15] = "https://finance.google.com/finance?q=NYSE%3AMAIN&ei=jje7WcjVL8TNjAG5r4LYAw"; // MAIN
        urlArray[16] = "https://finance.google.com/finance?q=NYSE%3ABPT&ei=oTe7WfHvFIjYjAHs34rYCQ"; // BPT
        urlArray[17] = "https://finance.google.com/finance?q=NYSE%3AORC&ei=qje7WZmwF8T02Abi6IXQDw"; // ORC
        urlArray[18] = "https://finance.google.com/finance?q=NYSEARCA%3ASCHB&ei=tje7WbDmLIn9jAHbjbXoDA"; // SCHB
        urlArray[19] = "https://finance.google.com/finance?q=NYSEARCA%3ATFI&ei=wTe7WZn9J4OejAGy5YLAAQ"; // TFI

    }
    
    public String getURL(int index) {
        return urlArray[index];
    }
}

class DivURLObject {
    int arraySize = 20;
    String[] divUrlArray;
    
    public DivURLObject(int size) {
        arraySize = size;
        divUrlArray = new String[arraySize];
        divUrlArray[0] = "http://www.nasdaq.com/symbol/cefl/dividend-history";
        divUrlArray[1] = "http://www.nasdaq.com/symbol/orc/dividend-history";
        divUrlArray[2] = null; // ACP dividend hisotry not on Nasdaq
    }
    
    public String getDivURL(int index) {
        return divUrlArray[index];
    }
}
