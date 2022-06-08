package juuxel.adorn.item

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object AdornItems {
    @JvmField
    val ITEMS: Registrar<Item> = PlatformBridges.registrarFactory.create(Registry.ITEM_KEY)
    val GROUP = PlatformBridges.items.createAdornItemGroup()
    private val DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build()

    private fun drinkFoodComponentBuilder() = FoodComponent.Builder().hunger(4).saturationModifier(0.3F).alwaysEdible()

    val STONE_ROD by ITEMS.register("stone_rod") { ItemWithDescription(Item.Settings().group(ItemGroup.MISC)) }
    val MUG by ITEMS.register("mug") { ItemWithDescription(Item.Settings().group(ItemGroup.FOOD).maxCount(16)) }
    val HOT_CHOCOLATE by ITEMS.register("hot_chocolate") {
        DrinkInMugItem(Item.Settings().group(ItemGroup.FOOD).food(DRINK_FOOD_COMPONENT).maxCount(1))
    }
    val SWEET_BERRY_JUICE by ITEMS.register("sweet_berry_juice") {
        DrinkInMugItem(Item.Settings().group(ItemGroup.FOOD).food(DRINK_FOOD_COMPONENT).maxCount(1))
    }
    val GLOW_BERRY_TEA by ITEMS.register("glow_berry_tea") {
        DrinkInMugItem(
            Item.Settings()
                .group(ItemGroup.FOOD)
                .food(drinkFoodComponentBuilder().statusEffect(StatusEffectInstance(StatusEffects.GLOWING, 400), 1.0f).build())
                .maxCount(1)
        )
    }
    val NETHER_WART_COFFEE by ITEMS.register("nether_wart_coffee") {
        DrinkInMugItem(Item.Settings().group(ItemGroup.FOOD).food(DRINK_FOOD_COMPONENT).maxCount(1))
    }

    val STONE_TORCH by ITEMS.register("stone_torch") {
        WallBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    }

    val GUIDE_BOOK by ITEMS.register("guide_book") {
        AdornBookItem(AdornCommon.id("guide"), Item.Settings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON))
    }

    val TRADERS_MANUAL by ITEMS.register("traders_manual") {
        AdornBookItem(AdornCommon.id("traders_manual"), Item.Settings().group(ItemGroup.MISC))
    }
    /*val TRADING_STATION_STORAGE_UPGRADE: Item by ITEMS.register("trading_station_storage_upgrade") {
        TradingStationUpgradeItem(Item.Settings().group(ItemGroup.MISC), TradingStationUpgradeItem.Type.STORAGE)
    }*/
    val TRADING_STATION_IO_UPGRADE: Item by ITEMS.register("trading_station_io_upgrade") {
        TradingStationUpgradeItem(Item.Settings().group(ItemGroup.MISC), TradingStationUpgradeItem.Type.IO)
    }
    val TRADING_STATION_LINK_UPGRADE: Item by ITEMS.register("trading_station_link_upgrade") {
        TradingStationUpgradeItem(Item.Settings().group(ItemGroup.MISC), TradingStationUpgradeItem.Type.LINK)
    }
    val TRADING_STATION_VOID_UPGRADE: Item by ITEMS.register("trading_station_void_upgrade") {
        TradingStationUpgradeItem(Item.Settings().group(ItemGroup.MISC), TradingStationUpgradeItem.Type.VOID)
    }
    val TRADING_STATION_INFINITE_STOCK_UPGRADE: Item by ITEMS.register("trading_station_infinite_stock_upgrade") {
        TradingStationUpgradeItem(Item.Settings().group(ItemGroup.MISC), TradingStationUpgradeItem.Type.INFINITE_STOCK)
    }

    fun init() {
    }

    fun isIn(group: ItemGroup?, item: Item): Boolean = when (group) {
        null -> false
        GROUP, ItemGroup.SEARCH -> true
        item.group -> PlatformBridges.configManager.config.client.showItemsInStandardGroups
        else -> false
    }
}
