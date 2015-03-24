package peter.ambulance.rit.edu;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class PinStart extends Thread{
	GpioPinDigitalOutput gpio;
	public PinStart(GpioPinDigitalOutput pin1)
	{
		this.gpio = pin1;
	}
	public void run()
	{
		gpio.high();
		try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gpio.low();
	}
}
