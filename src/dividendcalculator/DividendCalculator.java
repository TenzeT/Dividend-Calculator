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
    static final int arraySize = 10;
    static URLObject urlObject = new URLObject(arraySize);
    static DivURLObject divObject = new DivURLObject(arraySize);
    static String[] symbolArray = new String[arraySize];
    static String[] priceArray = new String[arraySize];
    static String[] monthlyDivArray = new String[arraySize];
    
    // Populate symbol array
    public static void setSymbols() {
        symbolArray[0] = "CEFL";
        symbolArray[1] = "ORC";
        symbolArray[2] = "ACP";
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
    
    public DividendCalculator() { 
      
      ImageIcon icon = new ImageIcon(this.getClass().getResource("\\images\\if_Money_206469.png")); // Get icon from src/images
      JFrame frame = new JFrame("Dividend Calculator");
      frame.setIconImage(icon.getImage());
      //frame.setSize(400, 300);
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
      Border border = BorderFactory.createLineBorder(Color.BLUE, 1); // Generic border
      JLabel jlabSymbols = new JLabel("Symbols");
      jlabSymbols.setHorizontalAlignment(SwingConstants.CENTER);
      jlabSymbols.setBorder(border);
      JLabel jlabShares = new JLabel("Number of Shares");
      jlabShares.setHorizontalAlignment(SwingConstants.CENTER);
      jlabShares.setBorder(border);
      JLabel jlabPrice = new JLabel("Price");
      jlabPrice.setHorizontalAlignment(SwingConstants.CENTER);
      jlabPrice.setBorder(border);
      JLabel jlabMonthlyDiv = new JLabel("Monthly Div / Total: " + calculateTotalMonthlyDiv(jtfShareArray));
      jlabMonthlyDiv.setHorizontalAlignment(SwingConstants.CENTER);
      jlabMonthlyDiv.setBorder(border);

      // Initialize arrays and center text in labels; create textfields
      for(int i = 0; i < 3; i++) {
          jlabSymbolArray[i] = new JLabel(symbolArray[i]);
          jlabSymbolArray[i].setHorizontalAlignment(SwingConstants.CENTER);
          jlabPriceArray[i] = new JLabel(priceArray[i]);
          jlabPriceArray[i].setHorizontalAlignment(SwingConstants.CENTER);
          jlabMonthlyDivArray[i] = new JLabel(monthlyDivArray[i]);
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
      jbuttonCalculateTotal.addActionListener((ae) -> { // Calculate total monthly dividend using overloaded method
        double totalMonthly = calculateTotalMonthlyDiv(jtfShareArray);
        BigDecimal bd = new BigDecimal(totalMonthly).setScale(2, RoundingMode.HALF_EVEN);
        totalMonthly = bd.doubleValue();
        jlabMonthlyDiv.setText("Monthly Div / Total: " + totalMonthly); 
        frame.pack();
      });
      jpanMain.add(jbuttonCalculateTotal);
      jpanMain.add(new JPanel());
      
      frame.add(jpanMain);
      frame.pack();
      frame.setVisible(true);
    }
    
    public static void main(String[] args) {
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
        urlArray[0] = "https://www.google.com/finance?q=CEFL&ei=Paa5WZnNN8XXjAHrsZ_IDg";
        urlArray[1] = "https://www.google.com/finance?q=ORC&ei=u565WZGyM8GqjAGcoaBg";
        urlArray[2] = "https://www.google.com/finance?q=ACP&ei=Oqa5WfnjJdaS2Aaq8a5o"; 
    }
    
    public String getURL(int index) {
        return urlArray[index];
    }
}

class DivURLObject {
    int arraySize = 10;
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