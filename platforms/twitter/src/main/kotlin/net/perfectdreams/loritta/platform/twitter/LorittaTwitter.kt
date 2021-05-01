package net.perfectdreams.loritta.platform.twitter

import blue.starry.penicillin.PenicillinClient
import blue.starry.penicillin.core.session.config.account
import blue.starry.penicillin.core.session.config.application
import blue.starry.penicillin.core.session.config.token
import blue.starry.penicillin.core.streaming.listener.FilterStreamListener
import blue.starry.penicillin.endpoints.stream
import blue.starry.penicillin.endpoints.stream.filter
import blue.starry.penicillin.extensions.models.text
import blue.starry.penicillin.models.Status
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import net.perfectdreams.loritta.commands.images.ArtExecutor
import net.perfectdreams.loritta.commands.images.BobBurningPaperExecutor
import net.perfectdreams.loritta.commands.images.BolsoDrakeExecutor
import net.perfectdreams.loritta.commands.images.BolsoFrameExecutor
import net.perfectdreams.loritta.commands.images.Bolsonaro2Executor
import net.perfectdreams.loritta.commands.images.BolsonaroExecutor
import net.perfectdreams.loritta.commands.images.BriggsCoverExecutor
import net.perfectdreams.loritta.commands.images.BuckShirtExecutor
import net.perfectdreams.loritta.commands.images.CanellaDvdExecutor
import net.perfectdreams.loritta.commands.images.CepoDeMadeiraExecutor
import net.perfectdreams.loritta.commands.images.ChicoAtaExecutor
import net.perfectdreams.loritta.commands.images.CortesFlowExecutor
import net.perfectdreams.loritta.commands.images.DrakeExecutor
import net.perfectdreams.loritta.commands.images.EdnaldoBandeiraExecutor
import net.perfectdreams.loritta.commands.images.EdnaldoTvExecutor
import net.perfectdreams.loritta.commands.images.GessyAtaExecutor
import net.perfectdreams.loritta.commands.images.GetOverHereExecutor
import net.perfectdreams.loritta.commands.images.InvertColorsExecutor
import net.perfectdreams.loritta.commands.images.KnuxThrowExecutor
import net.perfectdreams.loritta.commands.images.LoriAtaExecutor
import net.perfectdreams.loritta.commands.images.LoriDrakeExecutor
import net.perfectdreams.loritta.commands.images.LoriSignExecutor
import net.perfectdreams.loritta.commands.images.MonicaAtaExecutor
import net.perfectdreams.loritta.commands.images.NichijouYuukoPaperExecutor
import net.perfectdreams.loritta.commands.images.PassingPaperExecutor
import net.perfectdreams.loritta.commands.images.PepeDreamExecutor
import net.perfectdreams.loritta.commands.images.PetPetExecutor
import net.perfectdreams.loritta.commands.images.QuadroExecutor
import net.perfectdreams.loritta.commands.images.RipTvExecutor
import net.perfectdreams.loritta.commands.images.RomeroBrittoExecutor
import net.perfectdreams.loritta.commands.images.StudiopolisTvExecutor
import net.perfectdreams.loritta.commands.images.SustoExecutor
import net.perfectdreams.loritta.commands.images.ToBeContinuedExecutor
import net.perfectdreams.loritta.commands.images.TrumpExecutor
import net.perfectdreams.loritta.commands.images.declarations.ArtCommand
import net.perfectdreams.loritta.commands.images.declarations.AtaCommand
import net.perfectdreams.loritta.commands.images.declarations.BobBurningPaperCommand
import net.perfectdreams.loritta.commands.images.declarations.BolsonaroCommand
import net.perfectdreams.loritta.commands.images.declarations.BriggsCoverCommand
import net.perfectdreams.loritta.commands.images.declarations.BuckShirtCommand
import net.perfectdreams.loritta.commands.images.declarations.CanellaDvdCommand
import net.perfectdreams.loritta.commands.images.declarations.CepoDeMadeiraCommand
import net.perfectdreams.loritta.commands.images.declarations.CortesFlowCommand
import net.perfectdreams.loritta.commands.images.declarations.DrakeCommand
import net.perfectdreams.loritta.commands.images.declarations.EdnaldoCommand
import net.perfectdreams.loritta.commands.images.declarations.GetOverHereCommand
import net.perfectdreams.loritta.commands.images.declarations.InvertColorsCommand
import net.perfectdreams.loritta.commands.images.declarations.KnuxThrowCommand
import net.perfectdreams.loritta.commands.images.declarations.LoriSignCommand
import net.perfectdreams.loritta.commands.images.declarations.NichijouYuukoPaperCommand
import net.perfectdreams.loritta.commands.images.declarations.PassingPaperCommand
import net.perfectdreams.loritta.commands.images.declarations.PepeDreamCommand
import net.perfectdreams.loritta.commands.images.declarations.PetPetCommand
import net.perfectdreams.loritta.commands.images.declarations.QuadroCommand
import net.perfectdreams.loritta.commands.images.declarations.RipTvCommand
import net.perfectdreams.loritta.commands.images.declarations.RomeroBrittoCommand
import net.perfectdreams.loritta.commands.images.declarations.StudiopolisTvCommand
import net.perfectdreams.loritta.commands.images.declarations.SustoCommand
import net.perfectdreams.loritta.commands.images.declarations.ToBeContinuedCommand
import net.perfectdreams.loritta.commands.images.declarations.TrumpCommand
import net.perfectdreams.loritta.common.LorittaBot
import net.perfectdreams.loritta.common.locale.LocaleManager
import net.perfectdreams.loritta.common.memory.services.MemoryServices
import net.perfectdreams.loritta.common.utils.ConfigUtils
import net.perfectdreams.loritta.common.utils.config.LorittaConfig
import net.perfectdreams.loritta.platform.twitter.commands.CommandManager
import net.perfectdreams.loritta.platform.twitter.utils.config.TwitterConfig

class LorittaTwitter(config: LorittaConfig, val twitterConfig: TwitterConfig): LorittaBot(config) {
    companion object {
        private const val LORITTA_MENTION = "@LorittaEdit"
    }

    val client = PenicillinClient {
        account {
            application(twitterConfig.consumerKey, twitterConfig.consumerSecret)
            token(twitterConfig.accessToken, twitterConfig.accessTokenSecret)
        }
    }

    val commandManager = CommandManager(this)
    val localeManager = LocaleManager(
        ConfigUtils.localesFolder
    )

    val http = HttpClient {
        expectSuccess = false
    }

    override val services = MemoryServices()

    fun start() {
        localeManager.loadLocales()

        // ===[ IMAGES ]===
        commandManager.register(AtaCommand, MonicaAtaExecutor(emotes, http), ChicoAtaExecutor(emotes, http), LoriAtaExecutor(emotes, http), GessyAtaExecutor(emotes, http))
        commandManager.register(DrakeCommand, DrakeExecutor(emotes, http), BolsoDrakeExecutor(emotes, http), LoriDrakeExecutor(emotes, http))
        // commandManager.register(ManiaTitleCardCommand, ManiaTitleCardExecutor(http))
        commandManager.register(ArtCommand, ArtExecutor(emotes, http))
        commandManager.register(BobBurningPaperCommand, BobBurningPaperExecutor(emotes, http))
        commandManager.register(BolsonaroCommand, BolsonaroExecutor(emotes, http), Bolsonaro2Executor(emotes, http), BolsoFrameExecutor(emotes, http))
        commandManager.register(BriggsCoverCommand, BriggsCoverExecutor(emotes, http))
        commandManager.register(BuckShirtCommand, BuckShirtExecutor(emotes, http))
        commandManager.register(CanellaDvdCommand, CanellaDvdExecutor(emotes, http))
        commandManager.register(EdnaldoCommand, EdnaldoBandeiraExecutor(emotes, http), EdnaldoTvExecutor(emotes, http))
        commandManager.register(LoriSignCommand, LoriSignExecutor(emotes, http))
        commandManager.register(PassingPaperCommand, PassingPaperExecutor(emotes, http))
        commandManager.register(PepeDreamCommand, PepeDreamExecutor(emotes, http))
        commandManager.register(PetPetCommand, PetPetExecutor(emotes, http))
        commandManager.register(QuadroCommand, QuadroExecutor(emotes, http))
        commandManager.register(RipTvCommand, RipTvExecutor(emotes, http))
        commandManager.register(RomeroBrittoCommand, RomeroBrittoExecutor(emotes, http))
        commandManager.register(StudiopolisTvCommand, StudiopolisTvExecutor(emotes, http))
        commandManager.register(SustoCommand, SustoExecutor(emotes, http))
        commandManager.register(CortesFlowCommand, CortesFlowExecutor(http))
        commandManager.register(KnuxThrowCommand, KnuxThrowExecutor(emotes, http))
        commandManager.register(CepoDeMadeiraCommand, CepoDeMadeiraExecutor(emotes, http))
        commandManager.register(GetOverHereCommand, GetOverHereExecutor(emotes, http))
        commandManager.register(NichijouYuukoPaperCommand, NichijouYuukoPaperExecutor(emotes, http))
        commandManager.register(TrumpCommand, TrumpExecutor(emotes, http))
        // commandManager.register(TerminatorAnimeCommand, TerminatorAnimeExecutor(http))
        // commandManager.register(SAMCommand, SAMExecutor(http))
        commandManager.register(ToBeContinuedCommand, ToBeContinuedExecutor(emotes, http))
        commandManager.register(InvertColorsCommand, InvertColorsExecutor(emotes, http))
        // commandManager.register(MemeMakerCommand, MemeMakerExecutor(http))

        // ===[ VIDEOS ]===
        // commandManager.register(CarlyAaahCommand, CarlyAaahExecutor(emotes, http))
        // commandManager.register(AttackOnHeartCommand, AttackOnHeartExecutor(emotes, http))
        // commandManager.register(FansExplainingCommand, FansExplainingExecutor(http))

        runBlocking {
            client.stream.filter(track = listOf(LORITTA_MENTION)).listen(
                object : FilterStreamListener {
                    override suspend fun onStatus(status: Status) {
                        try {
                            // Ignore retweets
                            if (status.retweeted)
                                return

                            // Ignore possibly sensitive tweets to avoid issues with Twitter
                            /* if (status.possiblySensitive)
                                return */

                            println("Text: ${status.text}")
                            println("Text Raw: ${status.textRaw}")
                            println("Full Text Raw: ${status.fullTextRaw}")

                            // So if the status is
                            // Hello Hello @LorittaEdit canelladvd
                            // The text will be "canelladvd"
                            // We will use "substringAfterLast" because Twitter gives this when replying to a conversation where
                            // Loritta is already included
                            // @LorittaEdit Hello Hello @LorittaEdit canelladvd
                            val textAfterMention = status.text.substringAfterLast(LORITTA_MENTION).trim()

                            commandManager.matches(status, textAfterMention)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )
        }
    }
}