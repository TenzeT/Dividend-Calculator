package dividendcalculator;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DividendCalculator {
    // Create fields
    double monthlyTotalDiv = 0;
    static final int arraySize = 10;
    static URLObject urlObject = new URLObject(arraySize);
    static String[] symbolArray = new String[arraySize];
    static String[] priceArray = new String[arraySize];
    static String[] monthlyDivArray = new String[arraySize];
    
    public static void setSymbols() {
        symbolArray[0] = "CEFL";
        symbolArray[1] = "ORC";
        symbolArray[2] = "ACP";
    }
    
    public static void setPrices() {
        for(int i = 0; i < 3; i++) {
            GoogleStockReader stockReader = new GoogleStockReader(symbolArray[i], urlObject.getURL(i));
            priceArray[i] = stockReader.s;
            System.out.println(priceArray[i]);
        }
        
    }
    
    public DividendCalculator() { 
        
      JFrame frame = new JFrame("Dividend Calculator");  
      //frame.setSize(400, 300);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new GridLayout(4, 5, 10, 10));
      
      JLabel jlabSymbols = new JLabel("Symbols");
      JLabel jlabPrice = new JLabel("Price");
      JLabel jlabMonthlyDiv = new JLabel("Monthly Div / Total: " + monthlyTotalDiv);
      
      // Create columns of JLabels
      JLabel[] jlabSymbolArray = new JLabel[3];
      JLabel[] jlabPriceArray = new JLabel[3];
      JLabel[] jlabMonthlyDivArray = new JLabel[3];
      for(int i = 0; i < 3; i++) {
          jlabSymbolArray[i] = new JLabel(symbolArray[i]);
          jlabPriceArray[i] = new JLabel(priceArray[i]);
          jlabMonthlyDivArray[i] = new JLabel("div");
      }
      
      frame.add(jlabSymbols);
      frame.add(jlabPrice);
      frame.add(jlabMonthlyDiv);
      
      // Add arrays to columns in GridLayout
      for(int j = 0; j < 3; j++) {
          frame.add(jlabSymbolArray[j]);
          frame.add(jlabPriceArray[j]);
          frame.add(jlabMonthlyDivArray[j]);
      }
      
      frame.pack();
      frame.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        setSymbols();
        setPrices();
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
        
        String price = "not found";
        String lineFind = (":[\"" + sym + "\",\"");
        System.out.println(lineFind); // Delete
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
        System.out.println(price); // Delete
        return price;
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