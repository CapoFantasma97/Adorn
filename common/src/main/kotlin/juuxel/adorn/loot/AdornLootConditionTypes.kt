package juuxel.adorn.loot

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.registry.RegistryKeys

object AdornLootConditionTypes {
    val LOOT_CONDITION_TYPES: Registrar<LootConditionType> = RegistrarFactory.get().create(RegistryKeys.LOOT_CONDITION_TYPE)
    val GAME_RULE: LootConditionType by LOOT_CONDITION_TYPES.register("game_rule") { LootConditionType(GameRuleLootCondition.Serializer) }

    fun init() {
    }
}
