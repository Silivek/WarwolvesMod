package soii.warwolves;
 
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import soii.warwolves.util.RegistryHandler;
 
 
@Mod("soii_warwolves")
public class Warwolves
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "soii_warwolves";
   
	public Warwolves() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
     
		RegistryHandler.init();
     
		MinecraftForge.EVENT_BUS.register(this);
   }
   
   private void setup(FMLCommonSetupEvent event) {}
   private void doClientStuff(FMLClientSetupEvent event) {}
   public static final ItemGroup TAB = new ItemGroup("warwolvesTab") {
	   @Override
	   public ItemStack createIcon() {
		   return RegistryHandler.MAGIC_MEAL.get().getDefaultInstance();
	   }
   };
}