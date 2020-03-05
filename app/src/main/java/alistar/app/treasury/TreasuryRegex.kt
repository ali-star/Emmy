package alistar.app.treasury

import com.google.code.regexp.Pattern

object TreasuryRegex {
    val accountNumberInWithdrawalRegex: Pattern = Pattern.compile("""(برداشت از: (?<AccountNumber>\d.+)\s)""")
    val accountNumberInDepositRegex: Pattern = Pattern.compile("""(واريز به: (?<AccountNumber>\d.+)\s)""")
    val amountRegex: Pattern = Pattern.compile("""(مبلغ: (?<Amount>\d.+) ريال)""")
    val timeAndDateRegex: Pattern = Pattern.compile("""(?<Year>\d{2})\/(?<Month>\d{2})\/(?<Day>\d{2})_(?<Hour>\d{2}):(?<Minute>\d{2})""")
    val inventoryRegex: Pattern = Pattern.compile("""(موجودي:\s*(?<Inventory>\d.+)\sريال)""")
}