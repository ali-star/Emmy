package alistar.app.treasury

import alistar.app.R
import alistar.app.databinding.ActivityTreasuryBinding
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil

class TreasuryActivity : Activity() {

    lateinit var binding: ActivityTreasuryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_treasury, null, false)
        setContentView(binding.root)

        binding.addAccountButton.setOnClickListener {
            NewBankAccountDialog(this).show()
        }
    }

}
