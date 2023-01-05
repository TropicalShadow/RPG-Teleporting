package club.tesseract.rpgteleporting.economy

import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player

class VaultEconomy(private val economy: Economy): EconomyAPI {
    override fun getEconomyName(): String {
        return economy.name
    }

    override fun getBalance(player: Player): Double {
        return economy.getBalance(player)
    }

    override fun withdraw(player: Player, amount: Double): Boolean {
        return economy.withdrawPlayer(player, amount).transactionSuccess()
    }

    override fun deposit(player: Player, amount: Double): Boolean {
        return economy.depositPlayer(player, amount).transactionSuccess()
    }
}