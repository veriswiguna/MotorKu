package com.example.lifecycledemo

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_TRANSMISI = "extra_transmisi"
        const val EXTRA_UMUR_MOTOR = "extra_umur_motor"
        const val EXTRA_ODOMETER = "extra_odometer"
        const val EXTRA_HEALTH_SCORE = "extra_health_score"
        const val EXTRA_STATUS_MOTOR = "extra_status_motor"
        const val EXTRA_PERSEN_OLI = "extra_persen_oli"
        const val EXTRA_SISA_OLI_KM = "extra_sisa_oli_km"
        const val EXTRA_PERSEN_TRANSMISI = "extra_persen_transmisi"
        const val EXTRA_SISA_TRANSMISI_KM = "extra_sisa_transmisi_km"
        const val EXTRA_PERSEN_AKI = "extra_persen_aki"
        const val EXTRA_PERSEN_FILTER = "extra_persen_filter"
        const val EXTRA_TOTAL_BIAYA = "extra_total_biaya"
        const val EXTRA_RINCIAN_BIAYA = "extra_rincian_biaya"
    }

    private lateinit var tvAvatarInitials: TextView
    private lateinit var tvRiderName: TextView
    private lateinit var tvRiderEmail: TextView
    private lateinit var tvMotorSpecs: TextView
    private lateinit var tvBadgeTransmisi: TextView

    private lateinit var tvHealthScore: TextView
    private lateinit var tvStatusCategory: TextView
    private lateinit var pbHealthScore: ProgressBar

    private lateinit var tvTitleOli: TextView
    private lateinit var tvPersenOli: TextView
    private lateinit var tvStatusOli: TextView
    private lateinit var pbOli: ProgressBar

    private lateinit var tvTitleTransmisi: TextView
    private lateinit var tvPersenTransmisi: TextView
    private lateinit var tvStatusTransmisi: TextView
    private lateinit var pbTransmisi: ProgressBar

    private lateinit var tvTitleAki: TextView
    private lateinit var tvPersenAki: TextView
    private lateinit var tvStatusAki: TextView
    private lateinit var pbAki: ProgressBar

    private lateinit var tvTitleFilter: TextView
    private lateinit var tvPersenFilter: TextView
    private lateinit var tvStatusFilter: TextView
    private lateinit var pbFilter: ProgressBar

    private lateinit var tvEstimasiBiaya: TextView
    private lateinit var tvRincianBiaya: TextView
    private lateinit var btnKembali: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        initViews()
        bindDataAndAnimate()
    }

    private fun initViews() {
        tvAvatarInitials = findViewById(R.id.tvAvatarInitials)
        tvRiderName = findViewById(R.id.tvRiderName)
        tvRiderEmail = findViewById(R.id.tvRiderEmail)
        tvMotorSpecs = findViewById(R.id.tvMotorSpecs)
        tvBadgeTransmisi = findViewById(R.id.tvBadgeTransmisi)

        tvHealthScore = findViewById(R.id.tvHealthScore)
        tvStatusCategory = findViewById(R.id.tvStatusCategory)
        pbHealthScore = findViewById(R.id.pbHealthScore)

        tvTitleOli = findViewById(R.id.tvTitleOli)
        tvPersenOli = findViewById(R.id.tvPersenOli)
        tvStatusOli = findViewById(R.id.tvStatusOli)
        pbOli = findViewById(R.id.pbOli)

        tvTitleTransmisi = findViewById(R.id.tvTitleTransmisi)
        tvPersenTransmisi = findViewById(R.id.tvPersenTransmisi)
        tvStatusTransmisi = findViewById(R.id.tvStatusTransmisi)
        pbTransmisi = findViewById(R.id.pbTransmisi)

        tvTitleAki = findViewById(R.id.tvTitleAki)
        tvPersenAki = findViewById(R.id.tvPersenAki)
        tvStatusAki = findViewById(R.id.tvStatusAki)
        pbAki = findViewById(R.id.pbAki)

        tvTitleFilter = findViewById(R.id.tvTitleFilter)
        tvPersenFilter = findViewById(R.id.tvPersenFilter)
        tvStatusFilter = findViewById(R.id.tvStatusFilter)
        pbFilter = findViewById(R.id.pbFilter)

        tvEstimasiBiaya = findViewById(R.id.tvEstimasiBiaya)
        tvRincianBiaya = findViewById(R.id.tvRincianBiaya)
        btnKembali = findViewById(R.id.btnKembali)

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun bindDataAndAnimate() {
        val localeId = Locale.forLanguageTag("id-ID")

        // Ambil data Intent Extra dengan Default Fallback
        val nama = intent.getStringExtra(EXTRA_NAMA) ?: "Rider Motor"
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "rider@motorku.id"
        val transmisi = intent.getStringExtra(EXTRA_TRANSMISI) ?: "Matic (CVT)"
        val umurMotor = intent.getIntExtra(EXTRA_UMUR_MOTOR, 3)
        val odo = intent.getLongExtra(EXTRA_ODOMETER, 0L)
        val healthScore = intent.getIntExtra(EXTRA_HEALTH_SCORE, 100)
        val statusMotor = intent.getStringExtra(EXTRA_STATUS_MOTOR) ?: "PERFORMA PRIMA"

        val persenOli = intent.getIntExtra(EXTRA_PERSEN_OLI, 100)
        val sisaOliKm = intent.getLongExtra(EXTRA_SISA_OLI_KM, 2500L)

        val persenTransmisi = intent.getIntExtra(EXTRA_PERSEN_TRANSMISI, 100)
        val sisaTransmisiKm = intent.getLongExtra(EXTRA_SISA_TRANSMISI_KM, 12000L)

        val persenAki = intent.getIntExtra(EXTRA_PERSEN_AKI, 100)
        val persenFilter = intent.getIntExtra(EXTRA_PERSEN_FILTER, 100)
        val totalBiaya = intent.getIntExtra(EXTRA_TOTAL_BIAYA, 50000)
        val rincianBiayaText = intent.getStringExtra(EXTRA_RINCIAN_BIAYA) ?: "• Jasa Bengkel Dasar: Rp 50.000"

        // 1. Olah Inisial Rider (2 Huruf Depan)
        val initials = nama.split(" ")
            .asSequence()
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "MK" }

        tvAvatarInitials.text = initials
        tvRiderName.text = nama
        tvRiderEmail.text = email

        val formatOdo = NumberFormat.getNumberInstance(localeId).format(odo)
        tvMotorSpecs.text = "Tipe: $transmisi | Umur: $umurMotor Th | Odo: $formatOdo KM"

        val isMatic = transmisi.contains("Matic", ignoreCase = true)
        tvBadgeTransmisi.text = if (isMatic) "🛵 TRANSMISI MATIC (CVT & V-BELT)" else "🏍️ TRANSMISI MANUAL / SPORT (GEAR & RANTAI)"

        // Judul Komponen Transmisi Dinamis
        tvTitleTransmisi.text = if (isMatic) {
            getString(R.string.lbl_cvt_title)
        } else {
            getString(R.string.lbl_chain_title)
        }

        tvStatusCategory.text = statusMotor

        // Warna Kategori Status
        val categoryColor = when {
            healthScore >= 85 -> Color.parseColor("#00F5D4") // Cyber Cyan
            healthScore >= 65 -> Color.parseColor("#7B2CBF") // Purple Accent
            healthScore >= 40 -> Color.parseColor("#FFBD00") // Warning Gold
            else              -> Color.parseColor("#FF0054") // Danger Rose
        }
        tvHealthScore.setTextColor(categoryColor)
        tvStatusCategory.setTextColor(categoryColor)
        pbHealthScore.progressTintList = ColorStateList.valueOf(categoryColor)

        // 2. Format Teks Status Komponen Nyata Bengkel
        val formatOliKm = NumberFormat.getNumberInstance(localeId).format(sisaOliKm)
        tvPersenOli.text = getString(R.string.percent_format, persenOli)
        tvStatusOli.text = if (sisaOliKm > 0) "Sisa Kuota: $formatOliKm KM lagi (Viskositas Ideal)" else "⚠️ Wajib Ganti Oli Mesin Synthetic Sekarang!"

        val formatTransmisiKm = NumberFormat.getNumberInstance(localeId).format(sisaTransmisiKm)
        tvPersenTransmisi.text = getString(R.string.percent_format, persenTransmisi)
        tvStatusTransmisi.text = if (sisaTransmisiKm > 0) "Sisa Kuota: $formatTransmisiKm KM lagi" else "⚠️ Wajib Servis Penggerak Sekarang!"

        val teganganAki = when {
            umurMotor <= 2 -> "12.6V (Normal/Optimal)"
            umurMotor <= 4 -> "12.2V (Perlu Re-charge)"
            else -> "11.5V (Tegangan Drop/Aus)"
        }
        tvPersenAki.text = getString(R.string.percent_format, persenAki)
        tvStatusAki.text = "Tegangan Aki: $teganganAki (Usia: $umurMotor Th)"

        tvPersenFilter.text = getString(R.string.percent_format, persenFilter)
        tvStatusFilter.text = if (persenFilter >= 50) "Kondisi Busi & Filter Udara: Clean & Normal" else "⚠️ Busi / Filter Udara Kotor & Perlu Diganti!"

        // Format Estimasi Biaya (Rupiah)
        val formatRupiah = NumberFormat.getCurrencyInstance(localeId).format(totalBiaya).replace(",00", "")
        tvEstimasiBiaya.text = formatRupiah
        tvRincianBiaya.text = rincianBiayaText

        // ==========================================
        // ANIMASI SMOOTH BERBASIS SDK NATIVE
        // ==========================================

        // Animasi Counter Angka Health Score
        val animScore = ValueAnimator.ofInt(0, healthScore).apply {
            duration = 1200
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                tvHealthScore.text = getString(R.string.percent_format, animator.animatedValue as Int)
            }
        }
        animScore.start()

        // Animasi Progress Bar Health Score
        ObjectAnimator.ofInt(pbHealthScore, "progress", 0, healthScore).apply {
            duration = 1200
            interpolator = DecelerateInterpolator()
            start()
        }

        // Animasi Progress Bar Komponen
        ObjectAnimator.ofInt(pbOli, "progress", 0, persenOli).setDuration(1000).start()
        ObjectAnimator.ofInt(pbTransmisi, "progress", 0, persenTransmisi).setDuration(1000).start()
        ObjectAnimator.ofInt(pbAki, "progress", 0, persenAki).setDuration(1000).start()
        ObjectAnimator.ofInt(pbFilter, "progress", 0, persenFilter).setDuration(1000).start()
    }
}
