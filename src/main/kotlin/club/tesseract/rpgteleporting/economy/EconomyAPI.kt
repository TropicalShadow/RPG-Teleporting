package club.tesseract.rpgteleporting.economy

interface EconomyAPI {

    /**
     * Get name of the economy plugin
     *
     * @return name of the economy plugin
     */
    fun getEconomyName(): String

    /**
     * Get balance of player
     *
     * @param player Player to get balance of
     * @return Balance of player
     */
    fun getBalance(player: org.bukkit.entity.Player): Double

    /**
     * Withdraw money from a player
     *
     * @param player The player to withdraw money from
     * @param amount The amount to withdraw
     * @return The new balance of the player
     */
    fun withdraw(player: org.bukkit.entity.Player, amount: Double): Boolean

    /**
     * Deposit money into a player's account
     *
     * @param player The player to deposit money into
     * @param amount The amount to deposit
     * @return True if the deposit was successful, false otherwise
     */
    fun deposit(player: org.bukkit.entity.Player, amount: Double): Boolean

}