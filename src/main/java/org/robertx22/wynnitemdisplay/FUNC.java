package org.robertx22.wynnitemdisplay;

import java.io.FileNotFoundException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;




public interface FUNC {
	
		

	
	
	public default String removeFormatting(String s){    
		return s.replaceAll("\u00a7.", "");
	}

	public default int indexLoreEquals(List<String> lores, String string){
		
		for (int i = 0 ; i< lores.size() ; i++){
	    String lore = lores.get(i).replaceAll("[^A-Za-z]", "");
			if (lore.equals(string)){					
				return i;
			}
			
		}
		
		return -1;
	}
	
public default int indexLoreContains(List<String> lores, String string){
		
		for (int i = 0 ; i< lores.size() ; i++){
	    String lore = lores.get(i).replaceAll("[^A-Za-z]", "");
			if (lore.contains(string)){					
				return i;
			}
			
		}
		
		return -1;
	}
	
public default String giveLoreEquals(List<String> lores, String string){
		
		for (int i = 0 ; i< lores.size() ; i++){
		    String lore = lores.get(i).replaceAll("[^A-Za-z]", "");
			if (lore.equals(string)){					
				return lores.get(i);
			}
			
		}
		
		return "";
	}
	
	
public default String giveLoreContains(List<String> lores, String string){
	
	for (int i = 0 ; i< lores.size() ; i++){
	    String lore = lores.get(i).replaceAll("[^A-Za-z]", "");
		if (lore.contains(string)){					
			return lores.get(i);
		}
		
	}
	
	return "";
}


	public default int getNumber(String string){
		
		string = string.replaceAll("[^0-9]", "");
		if (string.length() > 0){
		return Integer.parseInt(string);			
		}
		return -1;
	}
	
public default String getText(String string){
		
		string = string.replaceAll("[^A-Za-z]", "");
		if (string.length() > 0){
		return string;			
		}
		return "";
	}
	


public default void createFilterFromJson() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
	

	ITEM_DATABASE.reloadFilter();
	
	JsonParser parser = new JsonParser();
	JsonArray  obj = ITEM_DATABASE.getFilter();
			
	
	ITEMFILTER.colorItem.clear();	
	ITEMFILTER.showItem.clear();		
	
	for (Object o : obj){
		
	JsonObject filter = (JsonObject) o;
	
	JsonElement type = null;
	JsonElement color = null;	
	JsonElement show = null;
	
	
	if (filter.has("Type")){ type =  filter.get("Type");}
	if (filter.has("Color")){ color =  filter.get("Color");}
	if (filter.has("Show")){ show =  filter.get("Show");}
	
	if (type == null || color == null || show == null)continue;
	
	ITEMFILTER.colorItem.put(type.getAsString(),color.getAsString());		
	ITEMFILTER.showItem.put(type.getAsString(),show.getAsString());
	
	}
	
	
	
}
	
	
	
}
