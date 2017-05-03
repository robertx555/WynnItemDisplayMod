package org.robertx22.wynnitemdisplay;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ITEM_DATABASE  {

	 static private JsonArray ItemDatabase = new JsonArray();
	 static private JsonArray ItemFilter = new JsonArray();
	 static private int lastUpdateTimestamp = 0;
	 
	 
	 static public  JsonArray getDatabase(){		 
		 return ItemDatabase;		 
	 }
	 
	 static public  JsonArray getFilter(){		 
		 return ItemFilter;		 
	}
	    
	 static public  void setFilter(JsonArray f){		 
		 ITEM_DATABASE.ItemFilter = f;	 
	}
	 static public  void setDatabase(JsonArray d){		 
		 ITEM_DATABASE.ItemDatabase = d;	 
	}   
	 
	static public int getTimestamp(){
		
		return lastUpdateTimestamp;
	}
	 
	static public void reloadFilter() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonParser parser = new JsonParser();       	
    	
		ITEM_DATABASE.setFilter((JsonArray) parser.parse(new FileReader(Main.FilterPath.toString())));	
	}

	static public void reloadDatabase() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonParser parser = new JsonParser();       	
		
		JsonObject d = (JsonObject) parser.parse(new FileReader(Main.DatabasePath.toString()));
		
		JsonArray darray = (JsonArray) d.get("items");
		
		lastUpdateTimestamp = ((JsonObject) d.get("request")).get("timestamp").getAsInt();
    	
		ITEM_DATABASE.setDatabase(darray);	
	}
	

	static public void reloadTimestamp() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonParser parser = new JsonParser();       	
		
		JsonObject d = (JsonObject) parser.parse(new FileReader(Main.DatabasePath.toString()));			
		
		lastUpdateTimestamp = ((JsonObject) d.get("request")).get("timestamp").getAsInt();    	
	
	}
	
}
