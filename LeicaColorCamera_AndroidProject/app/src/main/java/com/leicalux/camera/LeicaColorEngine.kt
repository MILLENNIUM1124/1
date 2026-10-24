package com.leicalux.camera

import android.graphics.ColorMatrix

data class LeicaProfile(
    val id: String,
    val name: String,
    val cnName: String,
    val swatchesHex: List<Long>,
    val defaultTone: Float,
    val defaultColor: Float,
    val baseMatrix: FloatArray
)

object LeicaColorEngine {
    val profiles = listOf(
        LeicaProfile(
            id = "classic",
            name = "Leica Classic",
            cnName = "经典传奇 · M11",
            swatchesHex = listOf(0xFF1A181B, 0xFF5E3A32, 0xFFC8232C, 0xFFD69F58, 0xFFF2E8D5),
            defaultTone = 15f,
            defaultColor = 12f,
            baseMatrix = floatArrayOf(
                1.08f, -0.04f, -0.04f, 0f, 0f,
               -0.02f,  1.02f,  0.00f, 0f, 0f,
               -0.05f, -0.03f,  1.08f, 0f, 0f,
                0f,     0f,     0f,    1f, 0f
            )
        ),
        LeicaProfile(
            id = "contemporary",
            name = "Leica Contemporary",
            cnName = "现代纪实 · Q3",
            swatchesHex = listOf(0xFF151E24, 0xFF3B5C6E, 0xFFA85D44, 0xFFD8A686, 0xFFEFECE6),
            defaultTone = 8f,
            defaultColor = -6f,
            baseMatrix = floatArrayOf(
                1.02f, -0.01f, -0.01f, 0f, 0f,
                0.00f,  1.03f, -0.03f, 0f, 0f,
               -0.02f,  0.04f,  0.98f, 0f, 0f,
                0f,     0f,     0f,    1f, 0f
            )
        ),
        LeicaProfile(
            id = "eternal",
            name = "Leica Eternal",
            cnName = "永恒胶片 · 柯达64",
            swatchesHex = listOf(0xFF131926, 0xFF1D4E89, 0xFFD92B2B, 0xFFE89B2D, 0xFFF7F1E1),
            defaultTone = -18f,
            defaultColor = 34f,
            baseMatrix = floatArrayOf(
                1.15f, -0.08f, -0.07f, 0f, -6f,
               -0.04f,  1.08f, -0.04f, 0f, -6f,
               -0.06f, -0.05f,  1.11f, 0f, -6f,
                0f,     0f,     0f,    1f,  0f
            )
        ),
        LeicaProfile(
            id = "brass",
            name = "Leica Brass",
            cnName = "复古黑金 · 露铜",
            swatchesHex = listOf(0xFF191612, 0xFF4A3B28, 0xFF8C6239, 0xFFD4AF37, 0xFFF3E5AB),
            defaultTone = -12f,
            defaultColor = 45f,
            baseMatrix = floatArrayOf(
                1.12f,  0.04f, -0.16f, 0f, 4f,
                0.02f,  0.98f, -0.10f, 0f, 2f,
               -0.08f, -0.06f,  0.84f, 0f, -8f,
                0f,     0f,     0f,    1f, 0f
            )
        ),
        LeicaProfile(
            id = "monochrome_hc",
            name = "Monochrome HC",
            cnName = "高反差黑白 · 森山",
            swatchesHex = listOf(0xFF050505, 0xFF1F1F1F, 0xFF595959, 0xFFC4C4C4, 0xFFFFFFFF),
            defaultTone = -38f,
            defaultColor = 20f,
            baseMatrix = floatArrayOf(
                0.46f, 0.44f, 0.10f, 0f, -15f,
                0.46f, 0.44f, 0.10f, 0f, -15f,
                0.46f, 0.44f, 0.10f, 0f, -15f,
                0f,    0f,    0f,    1f,  0f
            )
        )
    )

    /**
     * Combines the selected Leica Profile with the iPhone-style 2D Tone (Y) & Color (X) pad offsets.
     */
    fun buildCombinedColorMatrix(
        profile: LeicaProfile,
        tone: Float,     // -100f..100f
        color: Float,    // -100f..100f
        intensity: Float // 0f..1f
    ): ColorMatrix {
        val matrix = ColorMatrix(profile.baseMatrix.clone())
        val satMatrix = ColorMatrix()
        val satBoost = (1f + (color / 100f) * 0.35f * intensity).coerceIn(0f, 2f)
        satMatrix.setSaturation(if (profile.id.startsWith("monochrome")) 0f else satBoost)
        matrix.postConcat(satMatrix)

        val toneShift = (tone / 100f) * 22f * intensity
        val warmShift = (color / 100f) * 18f * intensity
        val shiftArray = floatArrayOf(
            1f, 0f, 0f, 0f, toneShift + warmShift,
            0f, 1f, 0f, 0f, toneShift,
            0f, 0f, 1f, 0f, toneShift - warmShift,
            0f, 0f, 0f, 1f, 0f
        )
        matrix.postConcat(ColorMatrix(shiftArray))
        return matrix
    }
}
