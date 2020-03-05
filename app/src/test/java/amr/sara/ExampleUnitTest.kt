package amr.sara

import alistar.app.treasury.TreasuryRegex
import alistar.app.treasury.TreasuryRegex.timeAndDateRegex
import alistar.app.utils.date.DateConverter
import alistar.app.utils.date.PersianDate
import org.junit.Test
import java.util.*

class ExampleUnitTest {

    companion object {
        const val withdrawalSms = """
            *بانك پاسارگاد*
            کارت
            برداشت از: 3103.8000.13570862.1
            مبلغ: 841,200 ريال
            98/12/15_17:11
            موجودي: 9,944,124 ريال
        """

        const val depositSms = """
            *بانك پاسارگاد*
            بانکداري مدرن
            واريز به: 3103.8000.13570862.2
            مبلغ: 40,000,000 ريال
            98/12/12_14:06
            موجودي: 151,462,823 ريال
        """

        const val test = """*بانك پاسارگاد*
کارت
برداشت از: 3103.8000.13570862.1
مبلغ: 841,200 """
    }

    @Test
    fun accountNumberInWithdrawalRegex() {
        println(withdrawalSms)
        val matches = TreasuryRegex.accountNumberInWithdrawalRegex.matcher(withdrawalSms)
        matches.find()
        assert(matches.group("AccountNumber") == "3103.8000.13570862.1")
    }

    @Test
    fun accountNumberInDepositRegex() {
        println(depositSms)
        val matches = TreasuryRegex.accountNumberInDepositRegex.matcher(depositSms)
        matches.find()
        assert(matches.group("AccountNumber") == "3103.8000.13570862.2")
    }

    @Test
    fun amountRegex() {
        println(test)
        val matches = TreasuryRegex.amountRegex.matcher(test)
        matches.find()
        assert(matches.group("Amount") == "841,200")
    }

    @Test
    fun dateTest() {
        val timeAndDateMatcher = timeAndDateRegex.matcher(withdrawalSms)
        timeAndDateMatcher.find()
        val persianYear = ("13" + timeAndDateMatcher.group("Year")).toInt()
        val persianMonth = timeAndDateMatcher.group("Month").toInt()
        val persianDay = timeAndDateMatcher.group("Day").toInt()
        val hour = timeAndDateMatcher.group("Hour").toInt()
        val minute = timeAndDateMatcher.group("Minute").toInt()
        val persianDate = PersianDate(persianYear, persianMonth, persianDay)
        val civilDate = DateConverter.persianToCivil(persianDate)
        val calendar = Calendar.getInstance()
        calendar.set(civilDate.year, civilDate.month - 1, civilDate.dayOfMonth, hour, minute)
        assert(calendar.get(Calendar.YEAR) == 2020)
        assert(calendar.get(Calendar.MONTH) == Calendar.MARCH)
        assert(calendar.get(Calendar.DAY_OF_MONTH) == 5)
    }
}
