package org.robertx22.wynnitemdisplay;



import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import java.io.IOException;

import org.lwjgl.input.Keyboard;


public class KEY_INPUT implements FUNC
{
	
	/** Storing an instance of Minecraft in a local variable saves having to get it every time */
	private Minecraft mc = Minecraft.getMinecraft();
	
	/** Key index for easy handling */
	

	public  final int ReloadFilter = 0;

	

	/** Key descriptions; use a language file to localize the description later */
	private  final String[] desc = 
		{
		"Reload the filter from file - useful for testing colors",			
					
		};

	/** Default key values */
	private  final int[] keyValues = 
		{
				Keyboard.KEY_NUMPAD1,			
				
								
		};

	/** Make this public or provide a getter if you'll need access to the key bindings from elsewhere */
	public  final KeyBinding[] keys = new KeyBinding[desc.length];

	public KEY_INPUT(Minecraft mc) {
		this.mc = mc;
		for (int i = 0; i < desc.length; ++i) {
			keys[i] = new KeyBinding(desc[i], keyValues[i], ("Wynncraft Item Display Mod"));
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}

	/**
	 * KeyInputEvent is in the FML package, so we must register to the FML event bus
	 * @throws IOException 
	 */
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) throws IOException {
		// checking inGameHasFocus prevents your keys from firing when the player is typing a chat message
		// NOTE that the KeyInputEvent will NOT be posted when a gui screen such as the inventory is open
		// so we cannot close an inventory screen from here; that should be done in the GUI itself
		if (mc.inGameHasFocus) {
			
			if (keys[ReloadFilter].isKeyDown()) {
			createFilterFromJson();
			}
		
			
			
		}
	}
}