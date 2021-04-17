package net.perfectdreams.loritta.commands.`fun`

import net.perfectdreams.loritta.commands.`fun`.declarations.RateWaifuCommand
import net.perfectdreams.loritta.common.commands.CommandArguments
import net.perfectdreams.loritta.common.commands.CommandContext
import net.perfectdreams.loritta.common.commands.CommandExecutor
import net.perfectdreams.loritta.common.commands.declarations.CommandExecutorDeclaration
import net.perfectdreams.loritta.common.commands.options.CommandOptions
import net.perfectdreams.loritta.common.emotes.Emotes
import net.perfectdreams.loritta.common.locale.LocaleKeyData
import kotlin.random.Random

class RateWaifuExecutor(val emotes: Emotes) : CommandExecutor() {
    companion object : CommandExecutorDeclaration(RateWaifuExecutor::class) {
        object Options : CommandOptions() {
            val waifu = string("waifu", LocaleKeyData("TODO_FIX_THIS"))
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: CommandContext, args: CommandArguments) {
        val waifu = args[options.waifu]

        val waifuLowerCase = waifu.toLowerCase()
        // TODO: Fix RANDOM with the current day of the year
        val random = Random(waifuLowerCase.hashCode().toLong()) // Usar um RANDOM sempre com a mesma seed
        val nota = random.nextInt(0, 11)

        val scoreReason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.note${nota}").random()

        var reason = when (nota) {
            10 -> "$scoreReason ${emotes.loriWow}"
            9 -> "$scoreReason ${emotes.loriHeart}"
            8 -> "$scoreReason ${emotes.loriPat}"
            7 -> "$scoreReason ${emotes.loriSmile}"
            3 -> "$scoreReason ${emotes.loriShrug}"
            2 -> "$scoreReason ${emotes.loriHmpf}"
            1 -> "$scoreReason ${emotes.loriRage}"
            else -> scoreReason
        }

        var strNota = nota.toString()
        if (waifuLowerCase == "loritta" || waifuLowerCase == "lori") {
            strNota = "∞"
            reason = "${context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteLoritta").random()} ${emotes.loriYay}"
        }
        if (waifuLowerCase == "pollux") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.notePollux").random()
        }
        if (waifuLowerCase == "pantufa") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.notePantufa").random() + " ${emotes.loriHeart}"
        }
        if (waifuLowerCase == "tatsumaki") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteTatsumaki").random()
        }
        if (waifuLowerCase == "mee6") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteMee6").random()
        }
        if (waifuLowerCase == "mantaro") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteMantaro").random()
        }
        if (waifuLowerCase == "dyno") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteDyno").random()
        }
        if (waifuLowerCase == "mudae") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteMudae").random()
        }
        if (waifuLowerCase == "nadeko") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteNadeko").random()
        }
        if (waifuLowerCase == "unbelievaboat") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteUnbelievaBoat").random()
        }
        if (waifuLowerCase == "chino kafuu") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteChinoKafuu").random()
        }
        if (waifuLowerCase == "groovy") {
            strNota = "10"
            reason = context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteGroovy").random()
        }
        if (waifuLowerCase == "lorita" || waifuLowerCase == "lorrita") {
            strNota = "-∞"
            reason = "${context.locale.getList("${RateWaifuCommand.LOCALE_PREFIX}.noteLorrita").random()} ${emotes.loriHmpf}"
        }

        // TODO: Fix stripCodeMarks, maybe implement a safer way to sanitize user input?
        context.sendReply { 
            content = context.locale["${RateWaifuCommand.LOCALE_PREFIX}.result", strNota, waifu, reason]
            prefix = "\uD83E\uDD14"
        }
    }
}