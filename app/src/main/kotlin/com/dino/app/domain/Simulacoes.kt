package com.dino.app.domain

object Simulacoes {
    data class ExtincaoResult(val tempo: List<Double>, val plantas: List<Double>, val herbivoros: List<Double>, val carnivoros: List<Double>)

    fun simularExtincao(bloqueio: Double, chuva: Double, anos: Int): ExtincaoResult {
        val dt = 0.05
        val passos = (anos / dt).toInt()
        var P = 100.0; var H = 40.0; var C = 15.0
        val t = ArrayList<Double>(); val ps = ArrayList<Double>()
        val hs = ArrayList<Double>(); val cs = ArrayList<Double>()
        val fb = 1.0 - bloqueio / 200.0; val fc = 1.0 - chuva / 300.0
        for (i in 0 until passos) {
            val k1 = deriva(P, H, C, fb, fc)
            val k2 = deriva(P + dt/2*k1[0], H + dt/2*k1[1], C + dt/2*k1[2], fb, fc)
            val k3 = deriva(P + dt/2*k2[0], H + dt/2*k2[1], C + dt/2*k2[2], fb, fc)
            val k4 = deriva(P + dt*k3[0], H + dt*k3[1], C + dt*k3[2], fb, fc)
            P = (P + dt/6 * (k1[0] + 2*k2[0] + 2*k3[0] + k4[0])).coerceAtLeast(0.0)
            H = (H + dt/6 * (k1[1] + 2*k2[1] + 2*k3[1] + k4[1])).coerceAtLeast(0.0)
            C = (C + dt/6 * (k1[2] + 2*k2[2] + 2*k3[2] + k4[2])).coerceAtLeast(0.0)
            t.add(i * dt); ps.add(P); hs.add(H); cs.add(C)
        }
        return ExtincaoResult(t, ps, hs, cs)
    }

    private fun deriva(P: Double, H: Double, C: Double, fb: Double, fc: Double): DoubleArray {
        val dP = (0.8 * fb * fc) * P * (1 - P / 100.0) - 0.01 * P * H
        val dH = (0.001 * 0.01 * 0.1) * P * H - 0.05 * H * C - 0.05 * H
        val dC = (0.001 * 0.05 * 0.1) * H * C - 0.05 * C
        return doubleArrayOf(dP, dH, dC)
    }

    fun calcularMassaTon(circCm: Double, bipede: Boolean): Double {
        val a = if (bipede) 0.00016 else 0.00049
        val b = if (bipede) 2.73 else 2.75
        return (a * Math.pow(circCm * 10, b)) / 1000.0
    }

    fun circFemoralPorMassa(massTon: Double, bipede: Boolean): Double {
        val a = if (bipede) 0.00016 else 0.00049
        val b = if (bipede) 2.73 else 2.75
        return if (massTon <= 0) 0.0 else Math.pow(massTon * 1000 / a, 1.0 / b) / 10.0
    }

    fun identificarIcnogenus(dedos: Int, garras: Boolean, tamanho: String?, forma: String?, proporcao: String?): String {
        return when {
            dedos == 3 && garras && tamanho == "grande" && forma == "alongada" -> "Eubrontes"
            dedos == 3 && !garras && tamanho == "pequeno" -> "Grallator"
            dedos == 4 && !garras && proporcao == "larga" -> "Anomoepus"
            dedos == 4 && garras -> "Chirotherium"
            else -> "Desconhecido"
        }
    }
}