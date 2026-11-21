package com.example

import androidx.test.core.app.ApplicationProvider
import com.example.game.AssetManager
import com.example.game.BusView
import com.example.game.VehicleColor
import com.example.game.recolorBlueMascotSheetPixels
import com.example.game.recolorGreenBusPixels
import com.example.game.recolorGreenWalkSheetPixels
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PassengerColorRecolorTest {

  private fun argb(a: Int, r: Int, g: Int, b: Int): Int =
    ((a and 0xFF) shl 24) or ((r and 0xFF) shl 16) or ((g and 0xFF) shl 8) or (b and 0xFF)

  @Test
  fun recolorBlueMascotPreservesEyesMouthHighlightsAndProducesAccurateHues() {
    val transparent = argb(0, 0, 0, 0)
    val blackEye = argb(255, 16, 18, 22)
    val whiteShine = argb(255, 252, 254, 253)
    val redMouth = argb(255, 232, 54, 88)
    val skyBlueMid = argb(251, 48, 178, 252)
    val skyBlueDark = argb(252, 18, 92, 175)

    val sampleBlueMascot =
      intArrayOf(
        transparent, // 0
        blackEye,    // 1
        whiteShine,  // 2
        redMouth,    // 3
        skyBlueMid,  // 4
        skyBlueDark, // 5
      )

    for (color in VehicleColor.entries) {
      val out = recolorBlueMascotSheetPixels(sampleBlueMascot, color)
      assertEquals(sampleBlueMascot.size, out.size)
      assertEquals(transparent, out[0])
      assertEquals("Black eye must remain untouched for $color", blackEye, out[1])
      assertEquals("White eye shine must remain untouched for $color", whiteShine, out[2])
      assertEquals("Red smile interior must remain untouched for $color", redMouth, out[3])
      assertEquals(251, (out[4] ushr 24) and 0xFF)

      val mr = (out[4] ushr 16) and 0xFF
      val mg = (out[4] ushr 8) and 0xFF
      val mb = (out[4] and 0xFF)

      when (color) {
        VehicleColor.PURPLE -> assertTrue("Purple ($mr,$mg,$mb)", mb > mr && mr > mg)
        VehicleColor.WHITE -> assertTrue("White ($mr,$mg,$mb)", mr >= 200 && mg >= 205 && mb >= 210)
        VehicleColor.GREEN -> assertTrue("Green ($mr,$mg,$mb)", mg > mr && mg > mb)
        VehicleColor.CYAN -> assertTrue("Cyan ($mr,$mg,$mb)", mg >= 185 && mb >= 185 && mr < 120)
        VehicleColor.RED -> assertTrue("Red ($mr,$mg,$mb)", mr >= 190 && mg < 100 && mb < 100)
        VehicleColor.PINK -> assertTrue("Pink ($mr,$mg,$mb)", mr >= 195 && mb >= 120 && mb > mg)
        VehicleColor.BLUE -> assertEquals(skyBlueMid, out[4])
        VehicleColor.YELLOW -> assertTrue("Yellow ($mr,$mg,$mb)", mr >= 200 && mg >= 165 && mb < 100)
        VehicleColor.ORANGE -> assertTrue("Orange ($mr,$mg,$mb)", mr >= 200 && mg in 95..165 && mb < 90)
        VehicleColor.BROWN -> assertTrue("Brown ($mr,$mg,$mb)", mr > mg && mg > mb && mr in 125..210)
      }

      val greenOut = recolorGreenWalkSheetPixels(intArrayOf(transparent, argb(251, 14, 212, 3)), color)
      assertEquals(2, greenOut.size)
    }
  }

  @Test
  fun assetManagerProvidesChibiSpritesOnlyForProvidedColorsWithoutRecoloring() {
    val assets = AssetManager(ApplicationProvider.getApplicationContext())
    val provided = setOf(
      VehicleColor.BLUE,
      VehicleColor.PINK,
      VehicleColor.PURPLE,
      VehicleColor.WHITE,
      VehicleColor.GREEN,
      VehicleColor.YELLOW,
      VehicleColor.BROWN,
      VehicleColor.RED,
      VehicleColor.ORANGE,
    )
    for (color in VehicleColor.entries) {
      if (color in provided) {
        assertNotNull("Chibi sprite must load for $color", assets.getPassengerSprite(color))
        assertEquals(12 to 1, assets.getPassengerGrid(color))
      } else {
        org.junit.Assert.assertNull(
          "Must not invent/recolor unprovided passenger color $color",
          assets.getPassengerSprite(color),
        )
      }
    }
  }

  @Test
  fun recolorGreenBusPixelsKeepsWindowsAndChevronsIntactWhileRecoloringBody() {
    val darkWindow = argb(255, 22, 42, 74)
    val whiteChevron = argb(255, 250, 252, 255)
    val yellowHeadlight = argb(255, 255, 220, 45)
    val greenBody = argb(255, 18, 228, 12)
    val transparent = argb(0, 0, 0, 0)

    val src = intArrayOf(transparent, darkWindow, whiteChevron, yellowHeadlight, greenBody)
    for (color in VehicleColor.entries) {
      val out = recolorGreenBusPixels(src, color)
      assertEquals(transparent, out[0])
      assertEquals("Window tint preserved for $color", darkWindow, out[1])
      assertEquals("White chevron preserved for $color", whiteChevron, out[2])
      assertEquals("Headlight preserved for $color", yellowHeadlight, out[3])
      if (color == VehicleColor.RED) {
        val r = (out[4] ushr 16) and 0xFF
        val g = (out[4] ushr 8) and 0xFF
        assertTrue("Red bus body should be red ($r,$g)", r >= 200 && g < 80)
      }
    }
  }

  @Test
  fun assetManagerProvidesAllBusSpritesAndAnimationSheetsForEveryColor() {
    val assets = AssetManager(ApplicationProvider.getApplicationContext())
    assertNotNull("bus_exhaust_puff_sheet.png must load", assets.getExhaustPuffSheet())

    for (color in VehicleColor.entries) {
      assertNotNull("Lot TOP_FRONT missing for $color", assets.getBusSprite(color, BusView.TOP_FRONT))
      assertNotNull("Lot TOP_REAR missing for $color", assets.getBusSprite(color, BusView.TOP_REAR))
      assertNotNull("Lot HORIZ missing for $color", assets.getBusSprite(color, BusView.HORIZ))
      assertNotNull("Lot SIDE missing for $color", assets.getBusSprite(color, BusView.SIDE))
      assertNotNull("Waiting bay bus missing for $color", assets.getWaitingBayBusSprite(color))
    }

    for (color in VehicleColor.busColors) {
      for (view in BusView.entries) {
        assertNotNull("SUPERFAST $view missing for $color", assets.getBusSprite(color, view))
      }
      assertNotNull("Exit blur/side sheet missing for $color", assets.getBusExitBlurSheet(color))
      assertNotNull("Turn/iso sheet missing for $color", assets.getBusTurnSheet(color))
      assertNotNull("Sunroof/bay sheet missing for $color", assets.getBusSunroofSheet(color))
    }
  }

  @Test
  fun level1LaunchesWith14BusesCapacity40MatchingCrowdAndWorkingBoardingFlow() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.game.GameViewModel(app)
    vm.updateLaneViewport(411f, 915f)
    vm.startLevel(1)

    val initial = vm.uiState.value
    assertEquals("Level 1 must start with 14 buses", 14, initial.vehicles.size)
    assertTrue("Every Level 1 bus must have capacity 40", initial.vehicles.all { it.capacity == 40 })
    assertEquals("Total passengers in Level 1 must equal 14 x 40", 560, initial.totalPassengersInLevel)
    assertTrue("Level 1 tutorial should highlight an unblocked bus", initial.isTutorial)
    assertNotNull("Tutorial vehicle ID should be resolved", initial.tutorialVehicleId)

    val level1Colors = setOf(VehicleColor.RED, VehicleColor.BLUE, VehicleColor.YELLOW)
    assertEquals(level1Colors, initial.vehicles.map { it.color }.toSet())
    assertTrue("Passengers must match Level 1 bus colors", initial.passengers.all { it.color in level1Colors })
    assertEquals(3, initial.passengers.map { it.segment }.distinct().size)

    // Tap the clear tutorial bus to verify it drives smoothly to slot 1
    val starter = initial.vehicles.first { it.id == initial.tutorialVehicleId }
    vm.onLotTap(starter.x, starter.y)
    val afterTap = vm.uiState.value
    val movedBus = afterTap.vehicles.first { it.id == starter.id }
    assertEquals(com.example.game.VehicleStage.DRIVING_TO_SLOT, movedBus.stage)

    // Advance frames so it parks, opens its roof, and passengers begin jumping to board
    repeat(120) { vm.onFrame(0.016f) }
    val afterDrive = vm.uiState.value
    assertEquals(com.example.game.GameOutcome.PLAYING, afterDrive.outcome)
    val parkedBus = afterDrive.vehicles.first { it.id == starter.id }
    assertEquals(com.example.game.VehicleStage.PARKED, parkedBus.stage)
    assertTrue("Parked bus roof must open for boarding", parkedBus.roofOpenProgress >= 0.99f)
  }
}
