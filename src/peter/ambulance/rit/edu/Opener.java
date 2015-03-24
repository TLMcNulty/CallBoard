/**
 * 
 */
package peter.ambulance.rit.edu;


import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.naming.CommunicationException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


public class Opener implements SerialPortEventListener{

    //map the port names to CommPortIdentifiers

    //this is the object that contains the opened port
    private SerialPort serialPort = null;
    static GpioController gpio = GpioFactory.getInstance();
    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    final static int END_OF_LINE_ASCII = 04;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    
    Date date;
    String EMD;
    String boxNumber;
    String priority;
    String address;
    String callDescription;
    String misc;
    //Call Num
    //Call Date
    //Call Time
    //EMD
    //Priority
    //Box
    //Location
    //Call Description
    //Misc
    SimpleDateFormat formatter;
    String text = "";
    boolean startRead = false;
    boolean foundX = false;
    int newLineCount = 0;
    boolean sent;
    String newText = "";
    boolean end = false;
    static String port;
    static GpioPinDigitalOutput pin1;
    static GpioPinDigitalOutput pin2;
    String cleanDescription = "";
    public void parseData()
    {
    	//data = data.replaceAll("\\W", ""); 
    	//text = text.concat(data);
    	//misc = misc.concat(logText);
    	//System.out.println(logText);
    	if(logText.contains("PERSONAL MESSAGE") || logText.contains("GROUP MESSAGE"));
    	{
    		
    			startRead = true;
    			sent = false;
    			// I can start making it flash here
    	}
    	text = text.concat(logText);    	
    	//System.out.println("INSERT INTO calls VALUES(default, CURRENT_DATE, CURRENT_TIME, '"+EMD+"', '"+priority+"','"+boxNumber+"','"+address+"', '"+callDescription+"',' ');");
    	if(text.contains("Enroute") && startRead)
    	{
    		newParse(text);
        	System.out.println("INSERT INTO calls VALUES(default, CURRENT_DATE, CURRENT_TIME, '"+EMD+"', '"+priority+"','"+boxNumber+"','"+address+"', '"+callDescription+"',' ');");
			System.out.println("Submitted query");
			for(int x = 0; x < callDescription.length(); x++)
			{
				char c = callDescription.charAt(x);
				if(c >= 32 && c <= 125)
				{
					String c1 = ""+ c;
					cleanDescription = cleanDescription.concat(c1);
				}
			}
			text = "";
    		logText = "";
    		foundX = false;
    		startRead = false;
    		try
    		{
    			if(!sent)
    			{
    				sendStatement();
    				PinStart start = new PinStart(pin1);
    				start.run();
    				sent = true;
    			}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		text = "";
    		logText = "";
    		foundX = false;
    		startRead = false;
    		
    		try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		new Opener();
    		return;
    	}
    	
    }
    public void newParse(String text)
    {
    	System.out.println(text);
    	text = Normalizer.normalize(text, Form.NFD)
    			// replace all combining diacritical marks and also everything that isn't a word or a whitespace character
    				.replaceAll("[\\n\\t\\r\\/]", "");
    			// replace all occurences of whitespaces or dashes with one single whitespace 
    	if(startRead && text.contains("B:"))
    	{
    		int indexOf = text.indexOf("B:");
    		int indexOf2 = text.indexOf("L:");
    		String box = (String) text.subSequence((indexOf+2), (indexOf + 6));
    		String priority = (String) text.subSequence(indexOf+7, indexOf2);
    		priority = priority.replaceAll(" ", "");
    		this.priority = priority;
    		this.boxNumber = box;
    	}
    	if(startRead && text.contains("L:"))
    	{
    		int indexOf = text.indexOf("L:");
    		indexOf += 2;
    		int indexEnd = text.indexOf("T:");
    		address = (String) text.subSequence(indexOf, indexEnd);
    	}
    	if(startRead && text.contains("T:"))
    	{
    		int indexOf = text.indexOf("T:");
    		indexOf +=2;
    		int endIndex = text.indexOf("X:");
    		EMD = (String) text.subSequence(indexOf, endIndex);
    		EMD = EMD.replaceAll(" ", "");
    	}
    	if(startRead && text.contains("X:"))
    	{
    		foundX = true;
    		int indexOf = text.indexOf("X:");
    		indexOf+=2;
    		int endIndex = text.indexOf("Enroute");
    		callDescription = (String) text.subSequence(indexOf, endIndex-1);
    		return;
    	}
    }
    public void sendStatement() throws SQLException
    {
    	try {
			openDatabase();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Statement stmt = connection.createStatement();
    	stmt.execute("INSERT INTO calls VALUES(default, CURRENT_DATE, CURRENT_TIME, '"+EMD+"', '"+priority+"','"+boxNumber+"','"+address+"', '"+cleanDescription+"',' ');");
    	connection.close();
    }
    Connection connection;
    public void openDatabase() throws ClassNotFoundException, SQLException
    {
    	Class.forName("com.mysql.jdbc.Driver");
    	connection = DriverManager.getConnection("jdbc:mysql://localhost/jobs?autoReconnect=true", "web", "rita6359");
    }
    
	public Opener()
	{
		connect();
	}
	
    public void serialEvent(SerialPortEvent evt) {
        if (evt.isRXCHAR())
        {
        	
            try
            {
            	
                String singleData = serialPort.readString();
                byte[] dataInByte = singleData.getBytes();
                if(!(dataInByte[0] == NEW_LINE_ASCII))
                {
                	logText = logText.concat(singleData);
                }
                else
                {
                		parseData();
                		logText = "";
                	
                	//logText = "";
                	//System.out.println(logText);
                }
                
            }
            catch (Exception e)
            {
//                logText = "Failed to read data. (" + e.toString() + ")";
//               System.err.println(logText);
               e.printStackTrace();
            }
        }else{
        	System.out.println("Parse");
        	disconnect();
        }   
    }
	public void connect()
    {
		serialPort = new SerialPort(port); 
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(9600, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(this);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

	
	 public void disconnect()
	 {
	        //close the serial port
	        try
	        {
	            serialPort.removeEventListener();
	            serialPort.closePort();
	            input.close();
	            output.close();
	            

	            logText = "Disconnected.";
	            System.out.println(logText);
	        }
	        catch (Exception e)
	        {
	            logText = "Failed to close " + serialPort
	                              + "(" + e.toString() + ")";
	        }
	 }
	 public static void main(String[] args)
	 {
		 System.out.println("Version 2.3.7");
		 port = "/dev/ttyUSB0";
		 
		 pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "Controller", PinState.LOW);
		 
		 Opener open = new Opener();
		
	 }
}