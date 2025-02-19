package juuxel.adorn.platform.forge

import juuxel.adorn.item.group.ItemGroupModifyContext
import juuxel.adorn.lib.registry.Registered
import juuxel.adorn.platform.ItemGroupBridge
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraftforge.event.CreativeModeTabEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ItemGroupBridgeForge : ItemGroupBridge {
    private val entries: MutableList<Entry> = ArrayList()
    private val additions: MutableList<Pair<ItemGroup, ItemGroupModifyContext.() -> Unit>> = ArrayList()

    override fun register(id: Identifier, configurator: ItemGroup.Builder.() -> Unit): Registered<ItemGroup> {
        val entry = Entry(id, configurator)
        entries += entry
        return entry
    }

    @SubscribeEvent
    fun registerGroups(event: CreativeModeTabEvent.Register) {
        for (entry in entries) {
            val group = event.registerCreativeModeTab(entry.id) {
                it.displayName(Text.translatable("itemGroup.${entry.id.namespace}.${entry.id.path}"))
                entry.configurator(it)
            }
            entry.itemGroup = group
        }
        entries.clear()
    }

    override fun addItems(group: ItemGroup, configurator: ItemGroupModifyContext.() -> Unit) {
        additions += group to configurator
    }

    @SubscribeEvent
    fun addToGroups(event: CreativeModeTabEvent.BuildContents) {
        for ((group, configurator) in additions) {
            val context = object : ItemGroupModifyContext {
                override fun add(item: ItemConvertible) {
                    if (event.tab === group) {
                        event.add(item)
                    }
                }

                override fun addAfter(after: ItemConvertible, items: List<ItemConvertible>) {
                    if (event.tab === group) {
                        items.fold(ItemStack(after)) { nextAfter, item ->
                            val stack = ItemStack(item)
                            event.entries.putAfter(nextAfter, stack, DEFAULT_STACK_VISIBILITY)
                            stack
                        }
                    }
                }
            }
            configurator(context)
        }
    }

    companion object {
        private val DEFAULT_STACK_VISIBILITY = ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
    }

    private class Entry(val id: Identifier, val configurator: ItemGroup.Builder.() -> Unit) : Registered<ItemGroup> {
        var itemGroup: ItemGroup? = null

        override fun get(): ItemGroup =
            itemGroup ?: error("Registered item group not yet initialised")
    }
}
