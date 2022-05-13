package com.kl3jvi.animity.ui.activities.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kl3jvi.animity.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


    private fun launchDropIn() {
    }

    companion object {
        const val DROP_IN_REQUEST_CODE = 800
    }
}
