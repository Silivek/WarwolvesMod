package soii.warwolves.util;
 
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import soii.warwolves.Warwolves;
import soii.warwolves.items.MagicMealItem;

@SuppressWarnings("unused")
public class RegistryHandler
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Warwolves.MOD_ID);
	//public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Warwolves.MOD_ID);
  
	public static void init() {
		//BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<Item> MAGIC_MEAL = ITEMS.register("magic_meal", MagicMealItem::new);
}