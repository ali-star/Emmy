package alistar.app.treasury

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import alistar.app.R
import android.app.Activity
import android.widget.Toast
import com.google.code.regexp.Pattern

class TreasuryActivity : Activity() {

    val testSms = """"بانك پاسارگاد
کارت
برداشت از: 3103.8000.13570862.1
مبلغ: 841,200 ريال
98/12/15_17:11
موجودي: 9,944,124 ريال""""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasury)

        val matcher = Pattern.compile("""(?<g>test)""")
        val matches = matcher.matcher("test")
        matches.find()
        Toast.makeText(this, matches.group("g"), Toast.LENGTH_LONG).show()
    }

}
