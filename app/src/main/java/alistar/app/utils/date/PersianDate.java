package alistar.app.utils.date;

import java.util.Calendar;

/**
 * @author Amir
 * @author ebraminio (implementing isLeapYear)
 */

public class PersianDate extends AbstractDate {
    private int year;
    private int month;
    private int day;

    private final String[] persianMonthName = {"", "فروردین",
            "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
            "آذر", "دی", "بهمن", "اسفند"};

    private final String[] persianMonthNum = {"", "1",
            "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "11", "12"};

	private final String[] dayOfWeekName = {"", "یکشنبه", "دوشنبه", "سه‌شنبه",
			"چهارشنبه", "پنجشنبه", "جمعه", "شنبه"};

    public PersianDate(int year, int month, int day) {
        setYear(year);
        // Initialize day, so that we get no exceptions when setting month
        this.day = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public PersianDate clone() {
        return new PersianDate(getYear(), getMonth(), getDayOfMonth());
    }

    public int getDayOfMonth() {
        return day;
    }

    public void setDayOfMonth(int day) {
        if (day < 1)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (month <= 6 && day > 31)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (month > 6 && month <= 12 && day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if (isLeapYear() && month == 12 && day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        if ((!isLeapYear()) && month == 12 && day > 29)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException(
                    Constants.MONTH + " " + month + " " + Constants.IS_OUT_OF_RANGE);

        // Set the day again, so that exceptions are thrown if the
        // day is out of range
        setDayOfMonth(day);

        this.month = month;
    }

    public int getWeekOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year == 0)
            throw new YearOutOfRangeException(Constants.YEAR_0_IS_INVALID);

        this.year = year;
    }

    public void rollDay(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public void rollMonth(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public void rollYear(int amount, boolean up) {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public String getEvent() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getDayOfYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getWeekOfMonth() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public boolean isLeapYear() {
        int y;
        if (year > 0)
            y = year - 474;
        else
            y = 473;
        return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    public boolean equals(PersianDate persianDate) {
        return this.getDayOfMonth() == persianDate.getDayOfMonth()
                && this.getMonth() == persianDate.getMonth()
                && (this.getYear() == persianDate.getYear() || this.getYear() == -1);
    }

    public String getMonthName() {
        return getMonthsList()[month];
    }

    public String getMonthNum() {
        return getMonthsListNum()[month];
    }

    public String[] getMonthsList() {
        return persianMonthName;
    }

    public String[] getMonthsListNum() {
        return persianMonthNum;
    }

	@Override
	public String getMonthNamePersian() {
		return null;
	}
	
	public int getDaysInMonth(int year, int month){
		PersianDate persianDate = DateConverter.civilToPersian(new CivilDate());
		persianDate.setYear(year);
		persianDate.setMonth(month);
		int days = 0;
		boolean stop = false;
		while (!stop) {
			try{
				days++;
				persianDate.setDayOfMonth(days);
			}catch(Exception e){
				stop = true;
			}
		}
		return days-1;
	}
	
	public String getDayOfWeekName () {
		return dayOfWeekName[DateConverter.persianToCivil(this).getDayOfWeek()];
	}
}
