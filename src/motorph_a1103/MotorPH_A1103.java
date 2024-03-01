/*Notes:
Time in 12:00 to 13:00. no counted
Time in before 8:00. credited as regular paid hour
Maximum regular paid hours is 8hours
Separate time entries with comma
Write Time In followed by Timeout.
Time should follow HH:MM format
overtime pay consideration: 25% of the regular hourly rate
Deductions and benefits will only be considered as part of the end-the-month payroll
No need to put timeIn, TimeOut for absent

 */

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalTime;

public class MotorPH_A1103 {

    public static void main(String[] args) {

        Scanner entry = new Scanner(System.in);

        while (true) {
            System.out.println("\nEnter Employee Number:");
            String employeeNumber = entry.nextLine();
            int employeeNumber_ = Integer.parseInt(employeeNumber);

            System.out.println("\nEnter multiple Time-In and Time-out:\n(format: HH:MM,HH:MM. Type P to process)");

            ArrayList<String> inputs = new ArrayList<>();

            while (true) {
                String inputline = entry.nextLine(); //before "\n" newline charter
                if (inputline.equalsIgnoreCase("p")) { // equalsIgnoreCase ignores the case (uppercase or lowercase) of "done"
                    processData(employeeNumber, employeeNumber_, inputs);
                    System.out.println("");
                    break;

                }
                if (!inputline.isEmpty()) { // to ensure that there are no blank entry or accidentally pressing of enter
                    String[] elements = inputline.split(",");// Split input by delimiter ","
                    for (String element : elements) {
                        inputs.add(element.trim()); // Trim spaces and add each element
                    }
                }

            }

            System.out.println("\nDo you want to clear the console? (Y/N)");
            String response1 = entry.nextLine();
            if (response1.equalsIgnoreCase("y")) {
                System.out.println("\033c");
                System.out.println("Terminal Cleared");
            }
            System.out.println("\nDo you want to process another  payroll? (Y/N)");
            String response2 = entry.nextLine();

            if (!response2.equalsIgnoreCase("y")) {
                System.out.print("Terminating Program...");
                break;
            }

        }
    }

    public static void processData(String employeeNumber, int employeeNumber_, ArrayList<String> inputs) {
        int coveredDays;
        coveredDays = 26; //Set to maximum workings day in a month

        //determine the values of each variable
        int index_;
        index_ = employeeNumber_ - 1;

        //Employee Information   
        String employeePosition_;
        String lastName;
        String firstName;
        double grossSemi_monthlyRate;
        double basicSalary;

        employeePosition_ = employeePosition(index_);
        lastName = employeeName(index_).get(0);
        firstName = employeeName(index_).get(1);
        grossSemi_monthlyRate = basicSalaryDatabase(index_) / 2;
        basicSalary = basicSalaryDatabase(index_);

        //Benefits
        double riceSubsidy;
        double phoneAllowance;
        double clothingAllowance;
        double totalBenefits;

        riceSubsidy = riceSubsidyDatabase(index_);
        phoneAllowance = phoneAllowanceDatabase(index_);
        clothingAllowance = clothingAllowanceDatabase(index_);
        totalBenefits = riceSubsidy + phoneAllowance + clothingAllowance;

        //Timesheet
        ArrayList<String> timeSheet = new ArrayList<>(inputs);

        //create timesheet containing timeIN and time 
        ArrayList<String> timeIn = extractTimeIn(timeSheet);
        ArrayList<String> timeOut = extractTimeOut(timeSheet);

        //Create arrayList compute worked hours per day
        ArrayList< Integer> dailyWorkedHours = new ArrayList<>();
        for (int i = 0; i < timeIn.size(); i++) {
            dailyWorkedHours.add(calculateWorkedHours(timeIn.get(i), timeOut.get(i)));
        }
        
        int maxRegularHours;
        maxRegularHours = 8; // Maximum paid regular hours : 8
        
        int regularWorkedHour;
        regularWorkedHour = regularWorkedHoursComputation(dailyWorkedHours, maxRegularHours);

        int overtimeHour;
        overtimeHour = overtimeComputation(dailyWorkedHours, maxRegularHours);

        // Computation of Earnings
        double dailyRateCutoff;
        double hourlyRateCutoff; //regular hours per day 
        double grossIncome;
        double overtimePay;
        double overtimeRate;
        double takeHomePay;
        int numWorkedDays;

        dailyRateCutoff = basicSalary / coveredDays;
        hourlyRateCutoff = dailyRateCutoff / maxRegularHours; 
        overtimeRate = 1.25; //set overtime pay rate to 25% of the hourlyRate
        overtimePay = overtimeHour * overtimeRate * hourlyRateCutoff;
        grossIncome = hourlyRateCutoff * regularWorkedHour + overtimePay;
        //Government Deductions (SSS, PhilHealth, Pagibig)
        double sssDeduction;
        double philHealthDeduction;
        double pagIbigDeduction;
        double benefitDeduction;
        double netMonthPay;
        double withHoldingTax;
        double totalDeduction;
        double taxableMonthlyPay;

        sssDeduction = calculateSSSDeduction(basicSalary);
        philHealthDeduction = calculatePhilHealthDeduction(basicSalary);
        pagIbigDeduction = calculatePagIbigDeduction(basicSalary);
        benefitDeduction = sssDeduction + pagIbigDeduction + philHealthDeduction;
        netMonthPay = grossIncome - benefitDeduction;
        taxableMonthlyPay = netMonthPay;
        withHoldingTax = calculateWithholdingTax(taxableMonthlyPay);
        totalDeduction = withHoldingTax + benefitDeduction;

        takeHomePay = grossIncome - totalDeduction + totalBenefits;
        numWorkedDays = timeSheet.size() / 2;

        // Print Personal Information Section
        int printOutWidth = 55;
        System.out.printf("%" + (55 + "EMPLOYEE PAYSLIP".length()) / 2 + "s%n", "EMPLOYEE PAYSLIP");
        System.out.println("-".repeat(printOutWidth));
        System.out.println("EMPLOYEE INFORMATION:");
        System.out.printf("%-30s: %s, %s%n", "Name", lastName, firstName);
        System.out.printf("%-30s: %s%n", "Employee Position/ Department", employeePosition_);
        System.out.printf("%-30s: %s%n", "Employee Number", employeeNumber);
        System.out.printf("%-30s: %s%n", "Cut-off Covered Days", coveredDays);

        // Print Earnings Section
        System.out.println("\nEARNINGS:");
        System.out.printf("%-30s: P%,.2f%n", "Basic Salary", basicSalary);
        System.out.printf("%-30s: P%,.2f%n", "Semi-monthly Rate", grossSemi_monthlyRate);
        System.out.printf("%-30s: %d%n", "Days Worked", numWorkedDays);
        System.out.printf("%-30s: %d%n", "Hours Worked ", regularWorkedHour);
        System.out.printf("%-30s: %d%n", "Overtime Hour", overtimeHour);
        System.out.printf("%-30s: P%,.2f%n", "Gross Income", grossIncome);

        // Print Deductions Section
        System.out.println("\nDEDUCTIONS:");
        System.out.printf("%-30s: P%,.2f%n", "SSS Deduction", sssDeduction);
        System.out.printf("%-30s: P%,.2f%n", "PhilHealth Deduction", philHealthDeduction);
        System.out.printf("%-30s: P%,.2f%n", "Pag-Ibig Deduction", pagIbigDeduction);
        System.out.printf("%-30s: P%,.2f%n", "Withholding Tax", withHoldingTax);
        System.out.printf("%-30s: P%,.2f%n", "Total Deduction", totalDeduction);

        // Print Benefits Section
        System.out.println("\nBENEFITS ");
        System.out.printf("%-30s: P%,.2f%n", "Rice Subsidy", riceSubsidy);
        System.out.printf("%-30s: P%,.2f%n", "phone Allowance", phoneAllowance);
        System.out.printf("%-30s: P%,.2f%n", "Clothing Allowance", clothingAllowance);
        System.out.printf("%-30s: P%,.2f%n", "Total Benefits", totalBenefits);

        // Print Summary Section
        System.out.println("\nSUMMARY:");
        System.out.printf("%-30s: P%,.2f%n", "Gross Income", grossIncome);
        System.out.printf("%-30s: P%,.2f%n", "Total Benefits", totalBenefits);
        System.out.printf("%-30s: P%,.2f%n", "Total Deduction", totalDeduction);
//System.out.printf("%-30s: P%,.2f%n", "Pay Adjustments", payAdjustments);
        System.out.printf("%-30s: P%,.2f%n", "Take-Home Pay", takeHomePay);

    }

    public static ArrayList<String> extractTimeIn(ArrayList<String> timeSheet) {
        ArrayList<String> timeIn = new ArrayList<>();

        for (int i = 0; i < timeSheet.size(); i += 2) {
            if (!timeSheet.get(i).isEmpty()) {
                timeIn.add(timeSheet.get(i));
            }
        }
        return timeIn;
    }

    public static ArrayList<String> extractTimeOut(ArrayList<String> timeSheet) {
        ArrayList<String> timeOut = new ArrayList<>();

        for (int i = 1; i < timeSheet.size(); i += 2) {
            if (!timeSheet.get(i).isEmpty()) {
                timeOut.add(timeSheet.get(i));
            }
        }
        return timeOut;
    }

    public static int calculateWorkedHours(String TimeIn, String TimeOut) {
        // Split the input string by ":"
        String[] part1 = TimeIn.split(":");
        String[] part2 = TimeOut.split(":");

        // Convert the hour and minute parts to integers
        int hour_TimeIN = Integer.parseInt(part1[0]);
        int minute_TimeIN = Integer.parseInt(part1[1]);

        int hour_TimeOut = Integer.parseInt(part2[0]);
        int minute_TimeOut = Integer.parseInt(part2[1]);

        int gracePeriod = 10; // grace period in minutes

        LocalTime targetTime = LocalTime.of(8, gracePeriod + 1); //parameter for grace period

        LocalTime parsedTime1 = LocalTime.parse(TimeIn); // Parse the TimeIN into a LocalTime object

        if (parsedTime1.isBefore(targetTime) && hour_TimeIN == 8) {  //if within graceperiod 8:00AM - 8:10AM. set TimeIN = 8:00AM
            minute_TimeIN = 0;
        }

        // Calculate the difference in minutes
        int totalMinutes1 = hour_TimeIN * 60 + minute_TimeIN;
        int totalMinutes2 = hour_TimeOut * 60 + minute_TimeOut;

        int workedMinutes = totalMinutes2 - totalMinutes1;

        // Calculate the worked hours. only consider hours. paid by the hour.
        int workedHour = workedMinutes / 60;

        LocalTime breakStart = LocalTime.of(12, 1);//Set breaktime starts 12PM
        LocalTime breakEnd = LocalTime.of(12, 59);//Set breaktime ends 12:59PM

        LocalTime parsedTime2 = LocalTime.parse(TimeOut); // Parse the TimeOut into a LocalTime object

        int breakTime = 0; // initialize breakTime
        if (parsedTime1.isBefore(breakStart) && parsedTime2.isAfter(breakEnd)) { //TimeIn after 12:00PM but before 1:00PM, not counted
            breakTime = 1;
        }

       int workedHour_ = workedHour - breakTime;

        return workedHour_;

    }

    public static double calculateWithholdingTax(double taxableMonthlyPay) {
        double[] BIRincomeThresholds = {
            20833,
            33333,
            66667,
            166667,
            666667,};
        double[] BIRTaxRate = {0,
            0.2 * (taxableMonthlyPay - BIRincomeThresholds[0]),
            2500 + 0.25 * (taxableMonthlyPay - BIRincomeThresholds[1]),
            10833 + 0.3 * (taxableMonthlyPay - BIRincomeThresholds[2]),
            40833.33 + 0.32 * (taxableMonthlyPay - BIRincomeThresholds[3]),
            200833.33 + 0.35 * (taxableMonthlyPay - BIRincomeThresholds[4]),};

        double whTax = 0;
        for (int i = 0; i < BIRincomeThresholds.length; i++) {
            if (taxableMonthlyPay < BIRincomeThresholds[i]) {
                whTax = BIRTaxRate[i];
                break;
            } else {
                whTax = BIRTaxRate[i + 1];
            }
        }
        return whTax;
    }

    public static double calculateSSSDeduction(double basicSalary) {
        ArrayList<Integer> sssSalary = new ArrayList<>();
        ArrayList<Double> sssContribution = new ArrayList<>();

        // create Salary range
        for (int i = 0; i < 44; i++) {
            int premium = 3250 + 500 * i;
            sssSalary.add(premium);
        }

        // create contribution range
        for (int i = 0; i < 44; i++) {
            double contribution = 135 + 22.5 * i;
            sssContribution.add(contribution);
        }

        // determine SSS deduction
        double SSS_ = 0;
        for (int i = 0; i < 44; i++) {
            if (basicSalary < sssSalary.get(i)) {
                SSS_ = sssContribution.get(i);
                break;
            } else {
                SSS_ = 1125.0;   // max contribution
            }
        }

        return SSS_;
    }

    public static double calculatePagIbigDeduction(double basicSalary) {
        double pagIBIG = 0;

        if (basicSalary >= 1000 && basicSalary <= 1500) {
            pagIBIG = basicSalary * 0.02;
        } else if (basicSalary > 1500) {
            pagIBIG = basicSalary * 0.01;
        }

        double maxContribution = 100.0; // ( need to verify) set max pag-ibig contribution to 100 
        double pagIBIG_ = Math.min(pagIBIG, maxContribution);

        return pagIBIG_;
    }

    public static double calculatePhilHealthDeduction(double basicSalary) {
        double philHealth = basicSalary * 0.03 / 2; // Employees contribution half of 3% of basicSalary.2020 mandate

        int minValue = 300 / 2; // Example minimum value
        int maxValue = 1800 / 2; // Example maximum value
        double philHealth_;
        philHealth_ = Math.min(Math.max(philHealth, minValue), maxValue);

        return philHealth_;
    }

    public static ArrayList<String> employeeName(int index_) {
        String[] lastName = {
            "Garcia",
            "Lim",
            "Aquino",
            "Reyes",
            "Hernandez",
            "Villanueva",
            "San Jose",
            "Romualdez",
            "Atienza",
            "Alvaro",
            "Salcedo",
            "Lopez",
            "Farala",
            "Martinez",
            "Romualdez",
            "Mata",
            "De Leon",
            "San Jose",
            "Rosario",
            "Bautista",
            "Lazaro",
            "Delos Santos",
            "Santos",
            "Del Rosario",
            "Tolentino",
            "Gutierrez",
            "Manalaysay",
            "Villegas",
            "Ramos",
            "Maceda",
            "Aguilar",
            "Castro",
            "Martinez",
            "Santos"
        };

        // Create Array with First Names
        String[] firstName = {
            "Manuel III",
            "Antonio",
            "Bianca Sofia",
            "Isabella",
            "Eduard",
            "Andrea Mae",
            "Brad",
            "Alice",
            "Rosie",
            "Roderick",
            "Anthony",
            "Josie",
            "Martha",
            "Leila",
            "Fredrick",
            "Christian",
            "Selena",
            "Allison",
            "Cydney",
            "Mark",
            "Darlene",
            "Kolby",
            "Vella",
            "Tomas",
            "Jacklyn",
            "Percival",
            "Garfield",
            "Lizeth",
            "Carol",
            "Emelia",
            "Delia",
            "John Rafael",
            "Carlos Ian",
            "Beatriz"
        };

        ArrayList<String> employeeName = new ArrayList<>();
        employeeName.add(lastName[index_]);
        employeeName.add(firstName[index_]);

        return employeeName;
    }

    public static int employeeNumberDatabase(int index_) {
        int[] employeeNumber = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34};

        int employeeNumber_ = employeeNumber[index_];
        return employeeNumber_;
    }

    public static double basicSalaryDatabase(int index_) {
        int[] salaries = {90000, 60000, 60000, 60000, 52670, 52670, 42975, 22500, 22500, 52670,
            50825, 38475, 24000, 24000, 53500, 42975, 41850, 22500, 22500, 23250,
            23250, 24000, 22500, 22500, 24000, 24750, 24750, 24000, 22500, 22500,
            22500, 52670, 52670, 52670};

        int basicSalary = salaries[index_];
        return basicSalary;
    }

    public static double phoneAllowanceDatabase(int index_) {
        // Phone allowance based on search criteria
        int[] phoneAllowance = {2000, 2000, 2000, 1000, 1000, 800, 500, 500, 1000, 1000,
            800, 500, 500, 1000, 800, 800, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 1000, 1000, 1000};

        int phoneAllowance_ = phoneAllowance[index_];
        return phoneAllowance_;
    }

    public static double clothingAllowanceDatabase(int index_) {
        // Clothing allowance based on search criteria
        int[] clothingAllowance = {1000, 1000, 1000, 1000, 1000, 800, 500, 500, 1000, 1000,
            800, 500, 500, 1000, 800, 800, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 1000, 1000, 1000};

        int clothingAllowance_ = clothingAllowance[index_];
        return clothingAllowance_;
    }

    public static double riceSubsidyDatabase(int index_) {

        int riceSubsidy = 1500;
        return riceSubsidy;
    }

    public static String employeePosition(int index_) {

        String[] employeePosition = {
            "Chief Executive Officer",
            "Chief Operating Officer",
            "Chief Finance Officer",
            "Chief Marketing Officer",
            "IT Operations and Systems",
            "HR Manager",
            "HR Team Leader",
            "HR Rank and File",
            "HR Rank and File",
            "Accounting Head",
            "Payroll Manager",
            "Payroll Team Leader",
            "Payroll Rank and File",
            "Payroll Rank and File",
            "Account Manager",
            "Account Team Leader",
            "Account Team Leader",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Account Rank and File",
            "Sales & Marketing",
            "Supply Chain and Logistics",
            "Customer Service and Relations"
        };

        String employeePosition_ = employeePosition[index_];

        return employeePosition_;

    }
    
   public static Integer regularWorkedHoursComputation(ArrayList< Integer> dailyWorkedHours, Integer maxRegularHours) {
        ArrayList<Integer> dailyRegularHour = new ArrayList<>();

        int totalRegularHour = 0;

        for (int i = 0; i < dailyWorkedHours.size(); i++) {
            int dailyRegular = Math.min(dailyWorkedHours.get(i), maxRegularHours);
            dailyRegularHour.add(dailyRegular);
            totalRegularHour += dailyRegular;
        }
        return totalRegularHour;
    }

    public static Integer overtimeComputation(ArrayList< Integer> dailyWorkedHours, Integer maxRegularHours) {
        ArrayList<Integer> dailyOvertimeHour = new ArrayList<>();

        int totalOvertimeHour = 0;

        for (int i = 0; i < dailyWorkedHours.size(); i++) {
            int dailyOvertime = dailyWorkedHours.get(i) - maxRegularHours;
            if (dailyOvertime > 0) {
                dailyOvertimeHour.add(dailyOvertime);
                totalOvertimeHour += dailyOvertime;
            } else {
                dailyOvertime = 0;
                dailyOvertimeHour.add(dailyOvertime);
            }
        }

        return totalOvertimeHour;
    }

}
