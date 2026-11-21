package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.game.BoosterType
import com.example.ui.theme.AlertRed
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.HudBlue
import com.example.ui.theme.HudBlueDark
import com.example.ui.theme.OutlineInk
import com.example.ui.theme.PlayGreen
import com.example.ui.theme.PlayGreenDark
import com.example.ui.theme.ShopPurple
import com.example.ui.theme.ShopPurpleDark

@Composable
fun GlossyButton(
  text: String,
  topColor: Color,
  bottomColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  testTag: String? = null,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, spring(), label = "btn_scale")

  Box(
    modifier =
      modifier
        .scale(scale)
        .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
        .clip(RoundedCornerShape(18.dp))
        .background(bottomColor)
        .padding(bottom = 5.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(topColor)
        .border(2.5.dp, OutlineInk, RoundedCornerShape(16.dp))
        .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
        .padding(horizontal = 18.dp, vertical = 12.dp),
    contentAlignment = Alignment.Center,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
      if (icon != null) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
      }
      Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily, fontSize = 16.sp),
        color = Color.White,
        maxLines = 1,
      )
    }
  }
}

@Composable
fun CoinPill(
  coins: Int,
  onAddClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier =
      modifier
        .width(132.dp)
        .height(48.dp)
        .clickable(onClick = onAddClick)
        .testTag("coin_pill"),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      painter = painterResource(id = R.drawable.btn_hud_coin),
      contentDescription = "Coins: $coins",
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxSize(),
    )
    Text(
      text = coins.toString(),
      style =
        MaterialTheme.typography.labelLarge.copy(
          fontFamily = FredokaFontFamily,
          fontSize = if (coins >= 1000) 13.sp else 16.sp,
          fontWeight = FontWeight.Bold,
          shadow = Shadow(color = OutlineInk, offset = Offset(0f, 2f), blurRadius = 2f),
        ),
      color = Color.White,
      maxLines = 1,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth().padding(start = 36.dp, end = 34.dp),
    )
  }
}

@Composable
fun CartoonPopupCard(
  title: String,
  headerColor: Color = HudBlue,
  onDismiss: () -> Unit,
  content: @Composable () -> Unit,
) {
  Dialog(onDismissRequest = onDismiss) {
    Column(
      modifier =
        Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(26.dp))
          .background(Color.White)
          .border(3.dp, OutlineInk, RoundedCornerShape(26.dp)),
    ) {
      Box(
        modifier =
          Modifier
            .fillMaxWidth()
            .background(headerColor)
            .padding(vertical = 14.dp, horizontal = 18.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge.copy(fontFamily = FredokaFontFamily),
          color = Color.White,
          textAlign = TextAlign.Center,
        )
      }
      Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        content()
      }
    }
  }
}

@Composable
fun WinLevelDialog(
  levelNumber: Int,
  stars: Int,
  onNextLevel: () -> Unit,
  onHome: () -> Unit,
) {
  CartoonPopupCard(title = "LEVEL $levelNumber COMPLETE!", headerColor = PlayGreen, onDismiss = {}) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      for (s in 1..3) {
        Icon(
          imageVector = if (s <= stars) Icons.Filled.Star else Icons.Outlined.StarBorder,
          contentDescription = "Star $s",
          tint = GoldCoin,
          modifier = Modifier.size(44.dp),
        )
      }
    }
    Spacer(Modifier.height(14.dp))
    Box(
      modifier =
        Modifier
          .clip(RoundedCornerShape(14.dp))
          .background(GoldCoin.copy(alpha = 0.22f))
          .border(2.dp, GoldCoinDark, RoundedCornerShape(14.dp))
          .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
      Text(
        text = "+50 Coins Reward!",
        style = MaterialTheme.typography.titleMedium.copy(fontFamily = FredokaFontFamily),
        color = OutlineInk,
      )
    }
    Spacer(Modifier.height(20.dp))
    GlossyButton(
      text = "Next Level",
      topColor = PlayGreen,
      bottomColor = PlayGreenDark,
      onClick = onNextLevel,
      modifier = Modifier.fillMaxWidth(),
      testTag = "next_level_button",
    )
    Spacer(Modifier.height(10.dp))
    GlossyButton(
      text = "Home",
      topColor = HudBlue,
      bottomColor = HudBlueDark,
      onClick = onHome,
      modifier = Modifier.fillMaxWidth(),
      testTag = "win_home_button",
    )
  }
}

@Composable
fun LoseLevelDialog(
  coins: Int,
  notice: String?,
  onRetry: () -> Unit,
  onRevive: () -> Unit,
  onHome: () -> Unit,
) {
  CartoonPopupCard(title = "LEVEL FAILED", headerColor = AlertRed, onDismiss = {}) {
    Text(
      text = "All waiting slots are full and no parked bus matches the next passenger!",
      style = MaterialTheme.typography.bodyLarge,
      color = OutlineInk,
      textAlign = TextAlign.Center,
    )
    if (notice != null) {
      Spacer(Modifier.height(8.dp))
      Text(
        text = notice,
        style = MaterialTheme.typography.labelLarge,
        color = AlertRed,
      )
    }
    Spacer(Modifier.height(16.dp))
    GlossyButton(
      text = "Revive (+1 Slot) • 200",
      topColor = GoldCoin,
      bottomColor = GoldCoinDark,
      onClick = onRevive,
      modifier = Modifier.fillMaxWidth(),
      testTag = "revive_button",
    )
    Spacer(Modifier.height(10.dp))
    GlossyButton(
      text = "Retry Level",
      topColor = HudBlue,
      bottomColor = HudBlueDark,
      onClick = onRetry,
      modifier = Modifier.fillMaxWidth(),
      testTag = "retry_button",
    )
    Spacer(Modifier.height(10.dp))
    GlossyButton(
      text = "Home",
      topColor = Color(0xFF7B8BA3),
      bottomColor = Color(0xFF566376),
      onClick = onHome,
      modifier = Modifier.fillMaxWidth(),
      testTag = "lose_home_button",
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = "Your Coins: $coins",
      style = MaterialTheme.typography.labelSmall,
      color = Color(0xFF5C6C84),
    )
  }
}

@Composable
fun PauseDialog(
  soundEnabled: Boolean,
  vibrationEnabled: Boolean,
  cloudSaveEnabled: Boolean,
  languageLabel: String,
  onToggleSound: (Boolean) -> Unit,
  onToggleVibration: (Boolean) -> Unit,
  onToggleCloudSave: (Boolean) -> Unit,
  onCycleLanguage: () -> Unit,
  onOpenSupport: () -> Unit,
  onResume: () -> Unit,
  onRestart: () -> Unit,
  onHome: () -> Unit,
) {
  Dialog(
    onDismissRequest = onResume,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    val cardAspect = 1016f / 1425f
    BoxWithConstraints(
      modifier =
        Modifier
          .widthIn(max = 360.dp)
          .fillMaxWidth(0.88f)
          .aspectRatio(cardAspect)
          .testTag("settings_pause_dialog"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

      Image(
        painter = painterResource(id = R.drawable.popup_settings),
        contentDescription = "Settings",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
      )

      // Top off-white/cream box: Slot 1 = Green Volume, Slot 2 = Green Vibrate, Slot 3 = Green Guide
      val iconBoxW = cardW * 0.180f
      val iconBoxH = maxOf(48.dp, cardH * 0.122f)
      val iconTopY = cardH * 0.178f

      // 1. Green Volume (ON / OFF muted speaker)
      SettingsSpriteButton(
        drawableRes = if (soundEnabled) R.drawable.btn_settings_sound_on else R.drawable.btn_settings_sound_off,
        contentDescription = if (soundEnabled) "Sound On" else "Sound Off",
        onClick = { onToggleSound(!soundEnabled) },
        testTag = "settings_sound_toggle",
        modifier =
          Modifier
            .offset(x = cardW * 0.165f, y = iconTopY)
            .size(width = iconBoxW, height = iconBoxH),
      )

      // 2. Green Vibrate (diagonal red slash overlay when toggled OFF)
      Box(
        modifier =
          Modifier
            .offset(x = cardW * 0.410f, y = iconTopY)
            .size(width = iconBoxW, height = iconBoxH),
        contentAlignment = Alignment.Center,
      ) {
        SettingsSpriteButton(
          drawableRes = R.drawable.btn_settings_vibrate,
          contentDescription = if (vibrationEnabled) "Vibration On" else "Vibration Off",
          onClick = { onToggleVibration(!vibrationEnabled) },
          testTag = "settings_vibration_toggle",
          modifier = Modifier.fillMaxSize(),
        )
        if (!vibrationEnabled) {
          androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize(0.72f)) {
            drawLine(
              color = AlertRed,
              start = Offset(size.width * 0.14f, size.height * 0.86f),
              end = Offset(size.width * 0.86f, size.height * 0.14f),
              strokeWidth = size.minDimension * 0.12f,
              cap = StrokeCap.Round,
            )
          }
        }
      }

      // 3. Green Guide / How to Play icon (2nd green icon from Tropical icons sheet)
      SettingsSpriteButton(
        drawableRes = R.drawable.btn_settings_guide,
        contentDescription = "How to Play",
        onClick = onOpenSupport,
        testTag = "settings_guide_button",
        modifier =
          Modifier
            .offset(x = cardW * 0.655f, y = iconTopY)
            .size(width = iconBoxW, height = iconBoxH),
      )

      // Middle vertical stack: Home (Red), Replay (Green), Support (Orange), English (Purple)
      val stackBtnW = cardW * 0.455f
      val stackBtnH = maxOf(48.dp, cardW * 0.455f * (146f / 394f))
      val stackX = (cardW - stackBtnW) * 0.5f

      SettingsSpriteButton(
        drawableRes = R.drawable.btn_settings_home,
        contentDescription = "Home",
        onClick = onHome,
        testTag = "pause_home_button",
        modifier =
          Modifier
            .offset(x = stackX, y = cardH * 0.333f)
            .size(width = stackBtnW, height = stackBtnH),
      )

      SettingsSpriteButton(
        drawableRes = R.drawable.btn_settings_replay,
        contentDescription = "Replay Level",
        onClick = onRestart,
        testTag = "restart_button",
        modifier =
          Modifier
            .offset(x = stackX, y = cardH * 0.463f)
            .size(width = stackBtnW, height = stackBtnH),
      )

      SettingsSpriteButton(
        drawableRes = R.drawable.btn_settings_support,
        contentDescription = "Support and Help",
        onClick = onOpenSupport,
        testTag = "settings_support_button",
        modifier =
          Modifier
            .offset(x = stackX, y = cardH * 0.593f)
            .size(width = stackBtnW, height = stackBtnH),
      )

      Box(
        modifier =
          Modifier
            .offset(x = stackX, y = cardH * 0.723f)
            .size(width = stackBtnW, height = stackBtnH),
        contentAlignment = Alignment.Center,
      ) {
        SettingsSpriteButton(
          drawableRes = R.drawable.btn_settings_language,
          contentDescription = "Language: $languageLabel",
          onClick = onCycleLanguage,
          testTag = "settings_language_button",
          modifier = Modifier.fillMaxSize(),
        )
        if (languageLabel != "English") {
          Box(
            modifier =
              Modifier
                .width(stackBtnW * 0.80f)
                .height(stackBtnH * 0.68f)
                .clip(RoundedCornerShape(14.dp))
                .background(ShopPurple),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = languageLabel,
              style =
                MaterialTheme.typography.titleMedium.copy(
                  fontFamily = FredokaFontFamily,
                  fontWeight = FontWeight.ExtraBold,
                  shadow = Shadow(color = OutlineInk, offset = Offset(0f, 2f), blurRadius = 2f),
                ),
              color = Color.White,
              maxLines = 1,
            )
          }
        }
      }

      // Bottom row: Cloud Save label + Green ON / Green OFF toggle button
      Row(
        modifier =
          Modifier
            .offset(x = cardW * 0.135f, y = cardH * 0.858f)
            .width(cardW * 0.730f)
            .height(maxOf(48.dp, cardH * 0.092f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Image(
          painter = painterResource(id = R.drawable.lbl_settings_cloud_save),
          contentDescription = "Cloud Save",
          contentScale = ContentScale.Fit,
          modifier = Modifier.weight(0.62f).fillMaxSize(),
        )
        Spacer(Modifier.width(cardW * 0.03f))
        SettingsSpriteButton(
          drawableRes = if (cloudSaveEnabled) R.drawable.btn_settings_cloud_on else R.drawable.btn_settings_cloud_off,
          contentDescription = if (cloudSaveEnabled) "Cloud Save On" else "Cloud Save Off",
          onClick = { onToggleCloudSave(!cloudSaveEnabled) },
          testTag = "settings_cloud_save_toggle",
          modifier = Modifier.width(cardW * 0.205f).height(maxOf(48.dp, cardH * 0.092f)),
        )
      }
    }
  }
}

@Composable
private fun SettingsSpriteButton(
  drawableRes: Int,
  contentDescription: String,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier,
) {
  val interaction = remember { MutableInteractionSource() }
  val pressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(if (pressed) 0.92f else 1f, spring(), label = "${testTag}_scale")

  Box(
    modifier =
      modifier
        .scale(scale)
        .clickable(
          interactionSource = interaction,
          indication = null,
          onClick = onClick,
        )
        .testTag(testTag),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      painter = painterResource(id = drawableRes),
      contentDescription = contentDescription,
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxSize(),
    )
  }
}

@Composable
fun SettingToggleRow(
  label: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFFF1F6FC))
        .padding(horizontal = 14.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(text = label, style = MaterialTheme.typography.bodyLarge, color = OutlineInk)
    Switch(checked = checked, onCheckedChange = onCheckedChange)
  }
}

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
  CartoonPopupCard(title = "HOW TO PLAY", headerColor = HudBlue, onDismiss = onDismiss) {
    HelpStepRow(
      step = "1",
      title = "Tap Clear Buses",
      desc = "Vehicles drive in the direction of their roof arrow. Tap unblocked vehicles to send them to the waiting bay.",
    )
    Spacer(Modifier.height(10.dp))
    HelpStepRow(
      step = "2",
      title = "Board Matching Passengers",
      desc = "Stickmen ride the top conveyor belt and jump into waiting buses of the same color.",
    )
    Spacer(Modifier.height(10.dp))
    HelpStepRow(
      step = "3",
      title = "Watch Slots & Tunnels",
      desc = "Don't fill all waiting slots with mismatched buses! Tunnels spawn extra vehicles once space opens.",
    )
    Spacer(Modifier.height(18.dp))
    GlossyButton(
      text = "Got It!",
      topColor = PlayGreen,
      bottomColor = PlayGreenDark,
      onClick = onDismiss,
      modifier = Modifier.fillMaxWidth(),
      testTag = "help_close_button",
    )
  }
}

@Composable
private fun HelpStepRow(step: String, title: String, desc: String) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFFF3F8FF))
        .padding(10.dp),
    verticalAlignment = Alignment.Top,
  ) {
    Box(
      modifier = Modifier.size(26.dp).clip(CircleShape).background(HudBlue),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = step,
        style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily),
        color = Color.White,
      )
    }
    Spacer(Modifier.width(10.dp))
    Column {
      Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp), color = OutlineInk)
      Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4A5870))
    }
  }
}

@Composable
fun BoosterPurchaseDialog(
  booster: BoosterType,
  coins: Int,
  notice: String?,
  onGetFree: () -> Unit,
  onBuy: () -> Unit,
  onDismiss: () -> Unit,
) {
  val drawableRes =
    when (booster) {
      BoosterType.BOARD -> R.drawable.popup_booster_board
      BoosterType.REFRESH -> R.drawable.popup_booster_refresh
      BoosterType.VIP_SLOT -> R.drawable.popup_booster_vip
    }
  val aspectRatio =
    when (booster) {
      BoosterType.BOARD -> 894f / 1124f
      BoosterType.REFRESH -> 890f / 1122f
      BoosterType.VIP_SLOT -> 890f / 1143f
    }
  val closeCenterY = if (booster == BoosterType.VIP_SLOT) 0.161f else 0.148f
  val buttonsTopY = if (booster == BoosterType.VIP_SLOT) 0.775f else 0.790f

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Column(
      modifier = Modifier.widthIn(max = 350.dp).fillMaxWidth(0.86f),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      BoxWithConstraints(
        modifier =
          Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .testTag("booster_popup_card"),
      ) {
        val cardW = maxWidth
        val cardH = maxHeight

        Image(
          painter = painterResource(id = drawableRes),
          contentDescription = booster.title,
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxSize(),
        )

        // Top-right red 'X' close button
        val closeSize = maxOf(48.dp, cardW * 0.145f)
        Box(
          modifier =
            Modifier
              .offset(
                x = cardW * 0.932f - closeSize / 2f,
                y = cardH * closeCenterY - closeSize / 2f,
              )
              .size(closeSize)
              .clip(CircleShape)
              .clickable(onClick = onDismiss)
              .semantics { contentDescription = "Close ${booster.title} popup" }
              .testTag("booster_popup_close"),
        )

        // Bottom-left 'Get' free booster button
        Box(
          modifier =
            Modifier
              .offset(x = cardW * 0.105f, y = cardH * buttonsTopY)
              .size(width = cardW * 0.375f, height = maxOf(48.dp, cardH * 0.155f))
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onGetFree)
              .semantics { contentDescription = "Get ${booster.title}" }
              .testTag("booster_popup_get"),
        )

        // Bottom-right '200' coin purchase button
        Box(
          modifier =
            Modifier
              .offset(x = cardW * 0.505f, y = cardH * buttonsTopY)
              .size(width = cardW * 0.385f, height = maxOf(48.dp, cardH * 0.155f))
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onBuy)
              .semantics { contentDescription = "Buy ${booster.title} for ${booster.costCoins} coins" }
              .testTag("buy_booster_confirm"),
        )
      }

      if (notice != null) {
        Spacer(Modifier.height(10.dp))
        Box(
          modifier =
            Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White.copy(alpha = 0.96f))
              .border(2.dp, AlertRed, RoundedCornerShape(14.dp))
              .padding(horizontal = 14.dp, vertical = 6.dp),
        ) {
          Text(
            text = "$notice (Coins: $coins)",
            style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily),
            color = AlertRed,
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}

@Composable
fun UnlockSlotDialog(
  coins: Int,
  notice: String?,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit,
) {
  CartoonPopupCard(title = "UNLOCK EXTRA SLOT", headerColor = PlayGreen, onDismiss = onDismiss) {
    Text(
      text = "Open an extra waiting slot permanently for this level to hold more buses at once!",
      style = MaterialTheme.typography.bodyLarge,
      color = OutlineInk,
      textAlign = TextAlign.Center,
    )
    if (notice != null) {
      Spacer(Modifier.height(8.dp))
      Text(text = notice, style = MaterialTheme.typography.labelLarge, color = AlertRed)
    }
    Spacer(Modifier.height(16.dp))
    GlossyButton(
      text = "Unlock • 300 Coins",
      topColor = GoldCoin,
      bottomColor = GoldCoinDark,
      onClick = onConfirm,
      modifier = Modifier.fillMaxWidth(),
      testTag = "confirm_unlock_slot",
    )
    Spacer(Modifier.height(8.dp))
    Text(text = "Your balance: $coins coins", style = MaterialTheme.typography.labelSmall, color = Color(0xFF5A6A80))
  }
}

@Composable
fun ShopDialog(
  coins: Int,
  boardCount: Int,
  refreshCount: Int,
  vipCount: Int,
  notice: String?,
  onBuyBooster: (BoosterType) -> Unit,
  onClaimFreeCoins: () -> Unit,
  onDismiss: () -> Unit,
) {
  CartoonPopupCard(title = "BUS JAM SHOP", headerColor = ShopPurple, onDismiss = onDismiss) {
    Text(
      text = "Coins: $coins",
      style = MaterialTheme.typography.titleMedium,
      color = GoldCoinDark,
    )
    if (notice != null) {
      Spacer(Modifier.height(6.dp))
      Text(text = notice, style = MaterialTheme.typography.labelLarge, color = PlayGreenDark)
    }
    Spacer(Modifier.height(10.dp))
    ShopItemRow(BoosterType.BOARD, boardCount, onBuy = { onBuyBooster(BoosterType.BOARD) })
    Spacer(Modifier.height(8.dp))
    ShopItemRow(BoosterType.REFRESH, refreshCount, onBuy = { onBuyBooster(BoosterType.REFRESH) })
    Spacer(Modifier.height(8.dp))
    ShopItemRow(BoosterType.VIP_SLOT, vipCount, onBuy = { onBuyBooster(BoosterType.VIP_SLOT) })
    Spacer(Modifier.height(14.dp))
    GlossyButton(
      text = "Free Coin Chest (+100)",
      topColor = GoldCoin,
      bottomColor = GoldCoinDark,
      onClick = onClaimFreeCoins,
      modifier = Modifier.fillMaxWidth(),
      testTag = "claim_free_coins",
    )
    Spacer(Modifier.height(8.dp))
    GlossyButton(
      text = "Close",
      topColor = HudBlue,
      bottomColor = HudBlueDark,
      onClick = onDismiss,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

@Composable
private fun ShopItemRow(
  type: BoosterType,
  owned: Int,
  onBuy: () -> Unit,
) {
  Row(
    modifier =
      Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFFF3F7FD))
        .border(1.5.dp, Color(0xFFD0DCEB), RoundedCornerShape(14.dp))
        .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(text = "${type.title} ($owned)", style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp), color = OutlineInk)
      Text(text = "${type.costCoins} Coins", style = MaterialTheme.typography.labelSmall, color = GoldCoinDark)
    }
    Spacer(Modifier.width(8.dp))
    GlossyButton(
      text = "Buy",
      topColor = PlayGreen,
      bottomColor = PlayGreenDark,
      onClick = onBuy,
    )
  }
}

@Composable
fun SettingsDialog(
  soundEnabled: Boolean,
  vibrationEnabled: Boolean,
  onToggleSound: (Boolean) -> Unit,
  onToggleVibration: (Boolean) -> Unit,
  onResetToLevel1: () -> Unit,
  onDismiss: () -> Unit,
) {
  CartoonPopupCard(title = "SETTINGS", headerColor = HudBlue, onDismiss = onDismiss) {
    SettingToggleRow(label = "Sound Effects", checked = soundEnabled, onCheckedChange = onToggleSound)
    Spacer(Modifier.height(8.dp))
    SettingToggleRow(label = "Vibration", checked = vibrationEnabled, onCheckedChange = onToggleVibration)
    Spacer(Modifier.height(14.dp))
    GlossyButton(
      text = "Replay From Level 1",
      topColor = GoldCoin,
      bottomColor = GoldCoinDark,
      onClick = onResetToLevel1,
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(10.dp))
    GlossyButton(
      text = "Done",
      topColor = PlayGreen,
      bottomColor = PlayGreenDark,
      onClick = onDismiss,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

@Composable
fun PromoModalDialog(
  title: String,
  description: String,
  buttonText: String,
  alreadyClaimed: Boolean,
  onAction: () -> Unit,
  onDismiss: () -> Unit,
) {
  CartoonPopupCard(title = title, headerColor = ShopPurple, onDismiss = onDismiss) {
    Icon(
      imageVector = Icons.Filled.DirectionsBus,
      contentDescription = null,
      tint = GoldCoinDark,
      modifier = Modifier.size(54.dp),
    )
    Spacer(Modifier.height(10.dp))
    Text(
      text = description,
      style = MaterialTheme.typography.bodyLarge,
      color = OutlineInk,
      textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(16.dp))
    if (!alreadyClaimed) {
      GlossyButton(
        text = buttonText,
        topColor = PlayGreen,
        bottomColor = PlayGreenDark,
        onClick = onAction,
        modifier = Modifier.fillMaxWidth(),
      )
      Spacer(Modifier.height(10.dp))
    }
    GlossyButton(
      text = "Close",
      topColor = HudBlue,
      bottomColor = HudBlueDark,
      onClick = onDismiss,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

@Composable
fun NoAdsPackDialog(
  alreadyUnlocked: Boolean,
  onActivate: () -> Unit,
  onDismiss: () -> Unit,
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    val cardAspect = 995f / 1474f
    BoxWithConstraints(
      modifier =
        Modifier
          .widthIn(max = 370.dp)
          .fillMaxWidth(0.90f)
          .aspectRatio(cardAspect)
          .testTag("no_ads_popup_card"),
    ) {
      val cardW = maxWidth
      val cardH = maxHeight

        Image(
          painter = painterResource(id = R.drawable.popup_no_ads_pack),
          contentDescription = "No Ads Pack",
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxSize(),
        )

        // Top-right glossy red 'X' close button positioned at the ribbon corner
        val closeTouchSize = maxOf(48.dp, cardW * 0.145f)
        val closeVisualSize = cardW * 0.122f
        val closeInteraction = remember { MutableInteractionSource() }
        val closePressed by closeInteraction.collectIsPressedAsState()
        val closeScale by animateFloatAsState(if (closePressed) 0.90f else 1f, spring(), label = "noads_close_scale")

        Box(
          modifier =
            Modifier
              .offset(
                x = cardW * 0.916f - closeTouchSize / 2f,
                y = cardH * 0.098f - closeTouchSize / 2f,
              )
              .size(closeTouchSize)
              .scale(closeScale)
              .clip(CircleShape)
              .clickable(
                interactionSource = closeInteraction,
                indication = null,
                onClick = onDismiss,
              )
              .semantics { contentDescription = "Close No Ads Pack popup" }
              .testTag("no_ads_popup_close"),
          contentAlignment = Alignment.Center,
        ) {
          androidx.compose.foundation.Canvas(modifier = Modifier.size(closeVisualSize)) {
            val r = size.minDimension * 0.5f
            val c = Offset(size.width * 0.5f, size.height * 0.5f)
            drawCircle(color = Color(0x55000000), radius = r, center = Offset(c.x, c.y + r * 0.12f))
            drawCircle(color = Color.White, radius = r, center = c)
            drawCircle(
              brush =
                androidx.compose.ui.graphics.Brush.verticalGradient(
                  colors = listOf(Color(0xFFFF5E62), Color(0xFFE01824), Color(0xFFB30B16)),
                ),
              radius = r * 0.85f,
              center = c,
            )
            drawOval(
              color = Color.White.copy(alpha = 0.35f),
              topLeft = Offset(c.x - r * 0.45f, c.y - r * 0.72f),
              size = androidx.compose.ui.geometry.Size(r * 0.90f, r * 0.42f),
            )
            val arm = r * 0.36f
            val strokeW = r * 0.24f
            drawLine(
              color = Color.White,
              start = Offset(c.x - arm, c.y - arm),
              end = Offset(c.x + arm, c.y + arm),
              strokeWidth = strokeW,
              cap = StrokeCap.Round,
            )
            drawLine(
              color = Color.White,
              start = Offset(c.x + arm, c.y - arm),
              end = Offset(c.x - arm, c.y + arm),
              strokeWidth = strokeW,
              cap = StrokeCap.Round,
            )
          }
        }

        // Bottom CTA positioned cleanly in the space below "Remove ads permanently"
        val btnW = cardW * 0.58f
        Box(
          modifier =
            Modifier
              .offset(
                x = (cardW - btnW) * 0.5f,
                y = cardH * 0.822f,
              )
              .width(btnW),
          contentAlignment = Alignment.Center,
        ) {
          if (!alreadyUnlocked) {
            GlossyButton(
              text = "Activate Ad-Free",
              topColor = PlayGreen,
              bottomColor = PlayGreenDark,
              onClick = onActivate,
              modifier = Modifier.fillMaxWidth(),
              testTag = "no_ads_activate_button",
            )
          } else {
            Box(
              modifier =
                Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(PlayGreen)
                  .border(2.5.dp, Color.White, RoundedCornerShape(16.dp))
                  .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center,
            ) {
              Text(
                text = "AD-FREE ACTIVE",
                style = MaterialTheme.typography.labelLarge.copy(fontFamily = FredokaFontFamily, fontSize = 15.sp),
                color = Color.White,
                maxLines = 1,
              )
          }
        }
      }
    }
  }
}
