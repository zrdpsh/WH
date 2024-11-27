package org.example.VSCardItem.src;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.Duration;
import java.util.Map;
import java.util.function.BiFunction;

/* первая версия программы :
 - захардкожены строки вывода в консоль, строка валидации и все строки вывода
 - обилие однотипного кода и однотипных имён переменных
 */

class TimeOfLife {
    private String myBirthTime;
    private String myBirthDay;
    private String myBirthMonth;
    private String myBirthYear;

    private long lifeInMiliSeconds;
    private long lifeInSeconds;
    private long lifeInMinutes;
    private long lifeInHours;
    private long lifeInDays;
    private long lifeInWeeks;
    private long lifeInMonths;
    private long lifeInYears;


    TimeOfLife() {
        System.out.println("\nВведите свое время/дату рождения в формате (\"hh:mm dd mm yyyy\" или \"dd mm yyyy\"): ");
        body(infoFromScanner());
    }

    TimeOfLife(String birthInfoString) {
        body(birthInfoString);
    }

    private void setTimeOfLife(String birthDay, String birthMonth, String birthYear) {
        this.myBirthTime = "00:00";
        this.myBirthDay = birthDay;
        this.myBirthMonth = birthMonth;
        this.myBirthYear = birthYear;
    }

    private void setTimeOfLife(String birthTime, String birthDay, String birthMonth, String birthYear) {
        this.myBirthTime = birthTime;
        this.myBirthDay = birthDay;
        this.myBirthMonth = birthMonth;
        this.myBirthYear = birthYear;
    }

    private String[] getBirthday() {
        return new String[]{this.myBirthTime, this.myBirthDay, this.myBirthMonth, this.myBirthYear};
    }

    private long[] getTimeOfLife() {
        return new long[]{this.lifeInMiliSeconds, this.lifeInSeconds, this.lifeInMinutes, this.lifeInHours, this.lifeInDays, this.lifeInWeeks, this.lifeInMonths, this.lifeInYears};
    }

    public static void main(String[] args) {
        String infoString = "05 03 2020";
        //String infoString = null;
        //String infoString = "";

        if (args.length > 0)
            new TimeOfLife(TimeOfLife.massToStr(args));
        else {
            if (infoString == null)
                new TimeOfLife();
            else {
                if (infoString.trim().equals(""))
                    new TimeOfLife();
                else new TimeOfLife(infoString);
            }
        }
    }

    private void body(String strBirdthInfo) {
        System.out.println("Вы ввели: " + strBirdthInfo + "\n_____________________________________");
        if (verifyInfoString(strBirdthInfo)) {

            String[] masScan = strBirdthInfo.split(" ");
            if (masScan.length == 4)
                setTimeOfLife(masScan[0], masScan[1], masScan[2], masScan[3]);
            else
                setTimeOfLife(masScan[0], masScan[1], masScan[2]);
        } else
            errorOutAndExit();


        try {
            calculateTimeOfLife(); //java.time.format.DateTimeParseException: Text '999-03-05 00:00' could not be parsed at index 0
        } catch (RuntimeException e) {
            System.out.println("ERROR: " + e);
            errorOutAndExit();
        }

        System.out.println("BIRTHDAY INFO: " + getBirthday()[0] + " " + getBirthday()[1] + " " + getBirthday()[2] + " " + getBirthday()[3]);
        System.out.println("_____________________________________");
        System.out.println("Time of life in miliSeconds = " + getTimeOfLife()[0]);
        System.out.println("Time of life in seconds = " + getTimeOfLife()[1]);
        System.out.println("Time of life in minutes = " + getTimeOfLife()[2]);
        System.out.println("Time of life in hours = " + getTimeOfLife()[3]);
        System.out.println("Time of life in days = " + getTimeOfLife()[4]);
        System.out.println("Time of life in weeks = " + getTimeOfLife()[5]);
        System.out.println("Time of life in months = " + getTimeOfLife()[6]);
        System.out.println("Time of life in years = " + getTimeOfLife()[7]);

    }

    private Boolean verifyInfoString(String userInfo) {
        Boolean pat1 = Pattern.matches("^[012]\\d:[012345]\\d\\s[0123]\\d\\s[01]\\d\\s[12]\\d{3}$", userInfo.trim());
        Boolean pat2 = Pattern.matches("^[0123]\\d\\s[01]\\d\\s[12]\\d{3}$", userInfo.trim());
        return pat1 || pat2;
    }

    static String infoFromScanner() {
        Scanner scan = new Scanner(System.in);
        String z = scan.nextLine();
        scan.close();
        return z;
    }

    static String massToStr(String[] mass) {
        String str = "";
        for (int i = 0; i < mass.length; i++)
            str = (i < mass.length - 1) ? str.concat(mass[i] + " ") : str.concat(mass[i]);
        return str;
    }

    private void calculateTimeOfLife() throws RuntimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String birthdayString = this.myBirthYear + "-" + this.myBirthMonth + "-" + this.myBirthDay + " " + this.myBirthTime;

        LocalDateTime birthDataTime = LocalDateTime.parse(birthdayString, formatter);
        //System.out.println("birthDataTime: "+ birthDataTime);
        LocalDateTime nowDateTime = LocalDateTime.now();
        System.out.println("nowDateTime: " + nowDateTime);

        this.lifeInMiliSeconds = java.time.Duration.between(birthDataTime, nowDateTime).toMillis();
        this.lifeInSeconds = java.time.Duration.between(birthDataTime, nowDateTime).getSeconds();
        this.lifeInMinutes = java.time.Duration.between(birthDataTime, nowDateTime).toMinutes();
        this.lifeInHours = java.time.Duration.between(birthDataTime, nowDateTime).toHours();
        this.lifeInDays = java.time.Duration.between(birthDataTime, nowDateTime).toDays();
        this.lifeInWeeks = this.lifeInDays / 7;

        LocalDate nowDate = LocalDate.now();
        //System.out.println("nowDate:" + nowDate);

        LocalDate birthday = LocalDate.of(Integer.parseInt(this.myBirthYear), Integer.parseInt(this.myBirthMonth), Integer.parseInt(this.myBirthDay));
        Period p = java.time.Period.between(birthday, nowDate);
        this.lifeInMonths = p.getYears() * 12 + p.getMonths();
        this.lifeInYears = p.getYears();
    }

    void errorOutAndExit() {
        System.out.println("Вы ввели информацию НЕ ПРАВИЛЬНО!\nНужно ввести ДОБ в таком виде: (\"00:00 18 06 1980\" или \"18 06 1980\")");
        System.exit(0);
    }
}


/*
логика работы программы словами:
 - получить на входе дату
 - сопоставить дату с неким образцом
 - если ошибка - прекратить выполнение
 - если получилось сопоставить - перевести во внутреннее представление (разбить на части /распарсить)
 - внутреннее представление - ещё одна трансформация данных - вывести в консоль

в коде очевидно выделяется тип данных, соответствующий единице времени - название и обработка каждой такой единицы - выделим её в отдельную структуру, TimeUnit.
Имея её, тривиально переписываются объявление класса, вычисление срока жизни и финальный вывод в консоль.
 */




public class TimeOfLifeRefactored {

    private LocalDateTime birthDateTime;
    private final String FORMAT_BIRTHDATE_STRING = "yyyy-MM-dd HH:mm";

    public TimeOfLifeRefactored(LocalDateTime birthDateTime) {
        this.birthDateTime = birthDateTime;
    }

    private enum TimeUnit {
        MILLISECONDS("milliseconds", (start, end) -> Duration.between(start, end).toMillis()),
        SECONDS("seconds", (start, end) -> Duration.between(start, end).getSeconds()),
        MINUTES("minutes", (start, end) -> Duration.between(start, end).toMinutes()),
        HOURS("hours", (start, end) -> Duration.between(start, end).toHours()),
        DAYS("days", (start, end) -> Duration.between(start, end).toDays()),
        WEEKS("weeks", (start, end) -> Duration.between(start, end).toDays() / 7),
        MONTHS("months", (start, end) -> Period.between(start.toLocalDate(), end.toLocalDate()).toTotalMonths()),
        YEARS("years", (start, end) -> Period.between(start.toLocalDate(), end.toLocalDate()).getYears());

        private final String name;
        private final BiFunction<LocalDateTime, LocalDateTime, Long> calculation;

        TimeUnit(String name, BiFunction<LocalDateTime, LocalDateTime, Long> calculation) {
            this.name = name;
            this.calculation = calculation;
        }

        public String getName() {
            return name;
        }

        public long calculate(LocalDateTime start, LocalDateTime end) {
            return calculation.apply(start, end);
        }
    }

     private Boolean verifyInfoString(String userInfo) {
        Boolean pat1 = Pattern.matches("^[012]\\d:[012345]\\d\\s[0123]\\d\\s[01]\\d\\s[12]\\d{3}$", userInfo.trim());
        Boolean pat2 = Pattern.matches("^[0123]\\d\\s[01]\\d\\s[12]\\d{3}$", userInfo.trim());
        return pat1 || pat2;
    }


    public void calculateAndDisplayTimeOfLife() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("День рождения: " + birthDateTime);
        System.out.println("Текущая дата: " + now);

        Map<TimeUnit, Long> timeValues = new EnumMap<>(TimeUnit.class);
        for (TimeUnit unit : TimeUnit.values()) {
            timeValues.put(unit, unit.calculate(birthDateTime, now));
        }

        timeValues.forEach((unit, value) ->
                System.out.printf("Время жизни в %s: %d%n", unit.getName(), value)
        );
    }

     static String infoFromScanner() {
        Scanner scan = new Scanner(System.in);
        String z = scan.nextLine();
        scan.close();
        return z;
    }

     void errorOutAndExit() {
        System.out.println("Вы ввели информацию НЕ ПРАВИЛЬНО!\nНужно ввести ДОБ в таком виде: (\"00:00 18 06 1980\" или \"18 06 1980\")");
        System.exit(0);
    }

    public static void main(String[] args) {
         System.out.println("\nВведите свое время/дату рождения в формате (\"hh:mm dd mm yyyy\" или \"dd mm yyyy\"): ");
         String info = infoFromScanner();
        if(verifyInfoString(info)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_BIRTHDATE_STRING);
            LocalDateTime birthDataTime = LocalDateTime.parse(info, formatter);
         
           TimeOfLife timeOfLife = new TimeOfLife(birthDateTime);
           timeOfLife.calculateAndDisplayTimeOfLife();
           return;
        }
        errorOutAndExit()
    }
}

