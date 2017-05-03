package org.robertx22.wynnitemdisplay;


import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;


@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main extends ITEM_DATABASE implements FUNC
{
    public static final String MODID = "Wynncraft Item Display Mod";
    public static final String VERSION = "0.1";    
    
    static public Path DatabasePath;
    static public Path FilterPath;
    
    
    Minecraft mc = Minecraft.getMinecraft();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) throws Exception {       	
    	
    	Files.createDirectories(Paths.get(Minecraft.getMinecraft().mcDataDir.toPath().toString() + "/mods/WynnItemDisplay"));
    	
    	 DatabasePath = Paths.get(Minecraft.getMinecraft().mcDataDir.toPath().toString() + "/mods/WynnItemDisplay/ItemDatabase.txt");
    	 FilterPath = Paths.get(Minecraft.getMinecraft().mcDataDir.toPath().toString() + "/mods/WynnItemDisplay/ItemFilter.txt");
    	     	
    	
    	ITEM_DATABASE.reloadTimestamp();	    		    	
       	
    	if (checkTimestamp()){
    	sendGet();    	
    	}
    	

    	ITEM_DATABASE.reloadDatabase();	
    	ITEM_DATABASE.reloadFilter();
		
    	
    	
    	createFilterFromJson();  	
    }       
           
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) throws IOException {		
    }
    
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)   {
    	
    	
    	 MinecraftForge.EVENT_BUS.register(new ItemDisplayer(mc)); 
    	 MinecraftForge.EVENT_BUS.register(new Main());   
    	 FMLCommonHandler.instance().bus().register(new KEY_INPUT(mc));   
    }
    
    
        
	// HTTPS GET request
	private boolean checkTimestamp() throws Exception {

		String url = "https://api.wynncraft.com/public_api.php?action=itemDB&search=timestamp";
		
		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");		


		BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {response.append(inputLine);}in.close();
		
		JsonParser parser = new JsonParser();
		
		JsonObject json = null;
		
		if ( parser.parse(response.toString()).isJsonArray() || parser.parse(response.toString()).isJsonObject()  ){
			 json = (JsonObject) parser.parse(response.toString());	
		}
		else{
			return false;
		}
		
				
		if (((JsonObject) json.get("request")).get("timestamp").isJsonNull()){
			return false;
			
		}
		
		int apitime = 0;
		
			if (((JsonObject) json.get("request")).get("timestamp").getAsInt() > 0){
				apitime = ((JsonObject) json.get("request")).get("timestamp").getAsInt();				
			}
		
			else {
				return false;
			}
	
			
		
		// time in days
		apitime /= 86400;		

		int timelastupdate = ITEM_DATABASE.getTimestamp() / 86400;
			
		
		// checks if database was updated in the last 3 days
		if (apitime > timelastupdate + 3){			
		return true;		
		}

		return false;
	
	}
    
	 
		// HTTPS GET request
		private void sendGet() throws Exception {

			String url = "https://api.wynncraft.com/public_api.php?action=itemDB&category=all";
			
			
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			
			// optional default is GET
			con.setRequestMethod("GET");		


			BufferedReader in = new BufferedReader(
			new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {response.append(inputLine);}in.close();
								
			
			Files.write(DatabasePath, response.toString().getBytes());
			
			
			
		
		}
    
  
    
}




