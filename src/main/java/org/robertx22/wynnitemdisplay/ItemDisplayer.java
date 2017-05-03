package org.robertx22.wynnitemdisplay;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class ItemDisplayer implements FUNC
{
  private Minecraft mc;
  
  public ItemDisplayer(Minecraft mc)
  {
  this.mc = mc;
  }
  
int ticks = 0;
final int tickRate = 10;
public String ItemPart = null;

boolean mythicDrop = false;
boolean mythic = false;


@SubscribeEvent
public void onTickForMythicAlert(RenderGameOverlayEvent.Post event) throws IOException{
	
	if (mythicDrop){		
    	
    	try	{     		

    		ScaledResolution res = new ScaledResolution(mc);
    		FontRenderer fontRender = mc.fontRendererObj;
    		int width = res.getScaledWidth();
    		int height = res.getScaledHeight();		
    		int color = Integer.parseInt("FFAA00",16);		

    		String text = "A Mythic Has Appeared!!";
    		
    		mc.fontRendererObj.drawString(text, width/2 - mc.fontRendererObj.getStringWidth(text)/2, height/2, color);
    		
    		   		
    		{};} 
    	catch (Exception e) {}
		
	
	}
	
}




@SubscribeEvent
public void onTick(TickEvent.ClientTickEvent event) throws IOException{

	
	// #############################
	// this makes sure game doesnt crash
	// #############################
	 this.ticks += 1;
	 if (this.ticks % 60 != 0) return;
	 if (this.mc.theWorld == null)return;         
	 if (this.mc.theWorld.loadedEntityList == null){
	 mythicDrop = false;	
	 return;
	 }
	 if (this.mc.theWorld.loadedEntityList.size() == 0){
	 mythicDrop = false;	 
	 return;
	 }
	 this.mc.mcProfiler.startSection("item_display");         
	// #############################
	// this makes sure game doesnt crash
	// #############################
	
mythic = false;
	
List<Entity> entities = mc.theWorld.loadedEntityList;

for (int i = 0; i < entities.size(); i++){

Entity e = entities.get(i);

if ( !(e instanceof EntityItem) ) continue;

	
	EntityItem item = (EntityItem)e;
	ItemStack sitem = item.getEntityItem();
	Item item_ = sitem.getItem();

	String name = sitem.getDisplayName();
	
	List<String> lores = sitem.getTooltip(mc.thePlayer, false);
	
	name = name.replaceAll("\u00a7.", "");	
	
	String type = type(name,lores);		
	
	if (ITEMFILTER.showItem.get(type).equals("false"))continue;
	
	
	if (type.equals("Powder")){
	String[] romans = {"empty","I","II","III","IV","V","VI"};
	String[] powders = {"THUNDER","AIR","WATER","FIRE","EARTH"};
	for (String s: powders){
		if (name.contains(s)){
			int n = getNumber(name);
			String roman = romans[n];
			name = s.substring(0,1) + s.substring(1).toLowerCase() + " " + "Powder" + " " + roman;
		}
		
	}
	}
	
	
	// Mask item names
	String[] rarities = {"Mythic","Legendary","Rare","Unique","Normal","Set"};
	for (int x = 0 ; x < rarities.length ; x++){
		String s = rarities[x];	
		if (type.equals(s)){
			if (ItemPart == null){
			name = rarities[x] + " " + "Item";
			}
			else {
			name = rarities[x] + " " + ItemPart;
			}
		}
	}
	
	
	if (type.equals("Unknown")){
		name = "Unknown Item";
	}
	
	if (type.equals("Mythic")){		
		mythic = true;		
	}
	
	
	String color = "\u00a7" + ITEMFILTER.colorItem.get(type);
	
	e.setCustomNameTag(color + name);
	e.setAlwaysRenderNameTag(true);
	
	


}

if(mythic){mythicDrop = true;}
else{mythicDrop = false;}


}


String type (String name, List<String> lores) throws IOException{
	
	if (name.equalsIgnoreCase("Emerald") || name.equalsIgnoreCase("Block of Emerald") || name.equalsIgnoreCase("Liquid Emerald")){
		return "Currency";
	}
	
	if (name.contains("HEALING") || name.contains("DEXTERITY")){
		return "Potion";
	}
	
	
	for (String s : lores){
		if (s.contains("Junk Item") || s.contains("Misc. Item") || name.contains("DROP")){
			return "Misc";			
		}		
	}
		
	String[] powders = {"THUNDER","AIR","WATER","FIRE","EARTH"};
	for (String s: powders){
		String nameNoNumbers = name.replaceAll("[^a-zA-Z]", "");		
		if (nameNoNumbers.equalsIgnoreCase(s)){
			return "Powder";
		}	
	}
	
	
	

	for (JsonElement o : ITEM_DATABASE.getDatabase()){
		
		ItemPart = null;
		
		if (o == null || !(o instanceof JsonObject) ) return "Unknown";
		
		JsonObject item = o.getAsJsonObject();
		
		String s = item.get("name").getAsString();		
	
		String one = name.replace(" ", "");
		String two = s.replace(" ", "");
		
		
		if (one.equalsIgnoreCase(two)){
			
			if (item.has("type")){
			ItemPart = item.get("type").getAsString();
			}
			return item.get("tier").getAsString();
		}
		
		
	}
	
	
	
	// IF nothing works
	return "Unknown";
}





}



