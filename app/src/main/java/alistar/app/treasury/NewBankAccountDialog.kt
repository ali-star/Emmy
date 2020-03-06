package alistar.app.treasury

import alistar.app.R
import alistar.app.brain.Memory
import alistar.app.databinding.NewBankAccountDialogBinding
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil

class NewBankAccountDialog(context: Context) : Dialog(context) {

    lateinit var binding: NewBankAccountDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.new_bank_account_dialog, null,false)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            if (binding.accountNumberEditText.text.isNullOrEmpty())
                return@setOnClickListener
            if (binding.cardNumberEditText.text.isNullOrEmpty())
                return@setOnClickListener

            val bankAccount = BankAccount()
            bankAccount.accountNumber = binding.accountNumberEditText.text.toString()
            bankAccount.cardNumber = binding.cardNumberEditText.text.toString()
            bankAccount.isDefault = binding.checkBox.isChecked

            val memory = Memory(context)
            memory.saveBankAccount(bankAccount)
            memory.close()
            dismiss()
        }
    }

}