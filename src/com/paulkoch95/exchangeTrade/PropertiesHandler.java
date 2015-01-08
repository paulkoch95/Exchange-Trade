package com.paulkoch95.exchangeTrade;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PropertiesHandler extends ExTradeMain{

	public static Properties prop = new Properties();
	
	public void loadPropdata(){
		try {
			prop.load(new FileInputStream(System.getProperty("user.dir")+"/playerData.properties"));//load .properties Data by FIS
			getLogger().info("Player Data succesfully loaded!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getKeyContent(String key){
		String readKey = prop.getProperty(key);
		return readKey;
	}
	public void setProp(String key, String keyContent){
		prop.setProperty(key,keyContent);
	}
	public void savePropData(){
		try {
			prop.store(new FileOutputStream("playerData.properties"), null);//save .properties Data
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
