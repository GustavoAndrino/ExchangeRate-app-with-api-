package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.JSONObject;

import connections.ExchangeApi;

public class MainGui extends JFrame {
	JSONObject coin1;
	JSONObject coin2;
	
	JTextField firstCurrencyRate = new JTextField();
	JTextField secondCurrencyRate = new JTextField();
	
	JLabel firstCurrency = new JLabel("USD");
	JLabel secondCurrency = new JLabel("EUR");
	
	double exchangeRate = 1;
	
	private DocumentListener firstCurrencyListener;
	private DocumentListener secondCurrencyListener;
	
	public MainGui() {
		super("Forex Exchange app");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(650, 450);
		
		setResizable(false);
		
		setLayout(null);
		
		setLocationRelativeTo(null);
		
		addGuiElements();
	
	}
	
	public void addGuiElements() {
		
		firstCurrency.setText("USD");
		firstCurrency.setBounds(80, 20, 150, 100);
		firstCurrency.setFont(new Font("Dialog", Font.BOLD, 50));
		add(firstCurrency);
		
		secondCurrency.setText("EUR");
		secondCurrency.setBounds(420, 20, 150, 100);
		secondCurrency.setFont(new Font("Dialog", Font.BOLD, 50));
		add(secondCurrency);
		
		firstCurrencyRate.setText("1");
		firstCurrencyRate.setBounds(80, 100, 150, 50);
		firstCurrencyRate.setFont(new Font("Dialog", Font.PLAIN, 25));
		firstCurrencyRate.setHorizontalAlignment(SwingConstants.CENTER);
		firstCurrencyListener = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
			
			}		
			@Override
			public void insertUpdate(DocumentEvent e) {
				removeListeners();
				updateNumbers1();
				addListeners();
			}			
			@Override
			public void changedUpdate(DocumentEvent e) {
				removeListeners();
				updateNumbers1();
				addListeners();	
			}
		};
		firstCurrencyRate.getDocument().addDocumentListener(firstCurrencyListener);
		add(firstCurrencyRate);
		
		secondCurrencyRate.setText("0,8");
		secondCurrencyRate.setBounds(420, 100, 150, 50);
		secondCurrencyRate.setFont(new Font("Dialog", Font.PLAIN, 25));
		secondCurrencyRate.setHorizontalAlignment(SwingConstants.CENTER);
		secondCurrencyListener = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
			
			}		
			@Override
			public void insertUpdate(DocumentEvent e) {
				removeListeners();
				updateNumbers2();
				addListeners();
			}			
			@Override
			public void changedUpdate(DocumentEvent e) {
				removeListeners();
				updateNumbers2();
				addListeners();		
			}
		};
		secondCurrencyRate.getDocument().addDocumentListener(secondCurrencyListener);
		add(secondCurrencyRate);
		
		JButton changeRateButton = new JButton("Select new rates");
		changeRateButton.setBounds(250, 350, 150, 25);
		changeRateButton.setFont(new Font("Dialog", Font.BOLD, 12));
		changeRateButton.setText("Select new rates");
		changeRateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewWindow();
				 
			}
		});
		
		add(changeRateButton);
		
	}
	
	private void createNewWindow() {
		JFrame changeCurrencyFrame = new JFrame("Change currencies");
		changeCurrencyFrame.setSize(300,200);
		changeCurrencyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		changeCurrencyFrame.setLocationRelativeTo(this);
		changeCurrencyFrame.setResizable(false);
		changeCurrencyFrame.setLayout(null);
		changeCurrencyFrame.setVisible(true);
		
		JTextField currencyOne = new JTextField();
		currencyOne.setBounds(20, 50, 80, 20);
		currencyOne.setFont(new Font("Dialog", Font.PLAIN, 12));
		currencyOne.setHorizontalAlignment(SwingConstants.CENTER);
		changeCurrencyFrame.add(currencyOne);
		
		JTextField currencyTwo = new JTextField();
		currencyTwo.setBounds(180, 50, 80, 20);
		currencyTwo.setFont(new Font("Dialog", Font.PLAIN, 12));
		currencyTwo.setHorizontalAlignment(SwingConstants.CENTER);
		changeCurrencyFrame.add(currencyTwo);
		
		JButton change = new JButton("Change");
		change.setBounds(90,120, 100, 20);
		change.setFont(new Font("Dialog", Font.PLAIN, 10));
		change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeListeners();
				String currency1 = currencyOne.getText();
				String currency2 = currencyTwo.getText();
				
				coin1 = ExchangeApi.exchangeData(currency1);
				
				double currencyValue1 = 1;
				double currencyValue2 = (double) coin1.get(currency2);
				exchangeRate = currencyValue2;
				BigDecimal format = new BigDecimal(currencyValue2);
				BigDecimal formatted = format.setScale(2, RoundingMode.HALF_UP);
				currencyValue2 = formatted.doubleValue();
				
				changeLabels(currencyValue1, currencyValue2, currency1, currency2);
				
				changeCurrencyFrame.dispose();
				
				addListeners();
			}
		});
		changeCurrencyFrame.add(change);
		
	}
	
	public void changeLabels(double currencyValue1, double currencyValue2, String currency1, String currency2) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				removeListeners();
				
				firstCurrencyRate.setText(String.valueOf(currencyValue1));
				firstCurrency.setText(currency1.toUpperCase());
				secondCurrencyRate.setText(String.valueOf(currencyValue2));
				secondCurrency.setText(currency2.toUpperCase());
				
				addListeners();
			}		
		});		
	}
	
	public void updateNumbers1() {
		String number = firstCurrencyRate.getText();
		double result = Double.parseDouble(number) * exchangeRate;	
		BigDecimal format = new BigDecimal(result);
		BigDecimal formatted = format.setScale(2, RoundingMode.HALF_UP);
		result = formatted.doubleValue();
		secondCurrencyRate.setText(String.valueOf(result));
	}
	
	public void updateNumbers2() {
		String number = secondCurrencyRate.getText();
		double result = Double.parseDouble(number) / exchangeRate;
		BigDecimal format = new BigDecimal(result);
		BigDecimal formatted = format.setScale(2, RoundingMode.HALF_UP);
		result = formatted.doubleValue();
		firstCurrencyRate.setText(String.valueOf(result));
	}
	
	private void removeListeners() {
		firstCurrencyRate.getDocument().removeDocumentListener(firstCurrencyListener);
		secondCurrencyRate.getDocument().removeDocumentListener(secondCurrencyListener);
	}
	
	private void addListeners() {
		firstCurrencyRate.getDocument().addDocumentListener(firstCurrencyListener);
		secondCurrencyRate.getDocument().addDocumentListener(secondCurrencyListener);
	}
}
