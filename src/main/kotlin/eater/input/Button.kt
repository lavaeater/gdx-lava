package eater.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

sealed class Button(val playstationButtonName: String) {
    //Fix disposing of this later
    val ps4Buttons by lazy {
        val texture = Texture(Gdx.files.internal("controllers/PS4.png"))
        mapOf(
            "cross" to TextureRegion(texture, 32, 48, 16, 16),
            "square" to TextureRegion(texture, 32, 64, 16, 16),
            "triangle" to TextureRegion(texture, 32, 80, 16, 16),
            "circle" to TextureRegion(texture, 32, 96, 16, 16),
            "dpadup" to TextureRegion(texture, 0, 16, 16, 16),
            "dpadright" to TextureRegion(texture, 0, 32, 16, 16),
            "dpaddown" to TextureRegion(texture, 0, 48, 16, 16),
            "dpadleft" to TextureRegion(texture, 0, 64, 16, 16)
        )
    }

    val image by lazy { ps4Buttons[playstationButtonName]!! }
    object Cross : Button("cross")
    object Ring : Button("ring")
    object Square : Button("square")
    object Triangle : Button("triangle")
    object DPadLeft : Button("dpadleft")
    object DPadUp : Button("dpadup")
    object DPadDown : Button("dpaddown")
    object DPadRight : Button("dpadright")
    object Share : Button("share")
    object Options : Button("options")
    object PsButton : Button("psbutton")
    object L3 : Button("l3")
    object R3 : Button("r3")
    object L1 : Button("l1")
    object R1 : Button("r1")
    object Unknown : Button("Unknown")

    companion object {
        fun getButton(buttonCode: Int): Button {
            return when (codesToButtons.containsKey(buttonCode)) {
                true -> codesToButtons[buttonCode]!!
                false -> Unknown
            }
        }

        fun getButtonCode(button: Button): Int {
            return when(buttonsToCodes.containsKey(button)) {
                true -> buttonsToCodes[button]!!
                false -> -1
            }
        }

        val codesToButtons = mapOf(
            0 to Cross,
            1 to Ring,
            2 to Square,
            3 to Triangle,
            4 to Share,
            5 to PsButton,
            6 to Options,
            7 to L3,
            8 to R3,
            9 to L1,
            10 to R1,
            11 to DPadUp,
            12 to DPadDown,
            13 to DPadLeft,
            14 to DPadRight,
            -1 to Unknown
        )

        val buttonsToCodes = codesToButtons.entries.associate { (k,v) -> v to k }.toMap()
        val buttons = codesToButtons.values.toList()
    }
}