
/*Notes:
Author @dgiltendez 
Group 5

Separate time entries with comma
Write Time In followed by Timeout.
Time should follow HH:mm format.
Input 24-hour format


Limitations and considerations:
Time in 12:00 to 13:00.BreakTime. not counted.
Maximum regular paid hours is 8hours
GrossIncome(Pay for hours worked) is used to determine the SSS, PhilHealth, Pag-ibig deductions
Overtime is only counted if employee works for more than 8hours
Overtime pay consideration: 25% more of the regular hourly rate.
This program computes for one month payroll. 
Working days is 20days. Maximum days
No need to put timeIn, TimeOut for absent
 
 */
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class MotorPH_A1103 {

    public static void main(String[] args) {
        int coveredDays;
        coveredDays = 20; //Set to maximum workings day in a month

        int maxRegularHours;
        maxRegularHours = 8; //Set to maximum regular hours in a day

        while (true) {

            Scanner entry = new Scanner(System.in);

            int employeeNumber_ = employeeNoEntry(coveredDays, maxRegularHours);

            timeEntry(employeeNumber_,coveredDays, maxRegularHours);

            // clear console prompt
            String response1;
            do {
                System.out.println("\nDo you want to clear the terminal? (Y/N)");
                response1 = entry.nextLine().trim().toLowerCase(); // remove whitespaces
                if (!response1.equals("y") && !response1.equals("n")) {
                    System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                }
            } while (!response1.equals("y") && !response1.equals("n"));

            if (response1.equals("y")) {
                System.out.println("\033c"); // remove all the prior text (tested on command prompt)
                clearTerminal(); //generates nextline to hide the text
                System.out.println("Console Cleared");
            }

            // process new employee console prompt
            String response2;
            do {
                System.out.println("\nDo you want to process another  payroll? (Y/N)");
                response2 = entry.nextLine().trim().toLowerCase(); // remove whitespaces
                if (!response2.equals("y") && !response2.equals("n")) {
                    System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                }
            } while (!response2.equals("y") && !response2.equals("n"));

            if (!response2.equalsIgnoreCase("y")) {
                System.out.print("Terminating Program...");
                entry.close(); //close scanner
                System.exit(0); //close java virtual machine
                break;
            }

        }
    }

    public static int employeeNoEntry(int coveredDays, int maxRegularHours) {

        Scanner employeeNoEntry = new Scanner(System.in);

        while (true) {
            try {
                //Employee Entry
                System.out.println("\nEnter Employee Number:");
                String employeeNumber = employeeNoEntry.nextLine();
                int employeeNumber_ = Integer.parseInt(employeeNumber);

                // Validate employee number
                if (employeeNumber_ > 34 || employeeNumber_ <= 0) {
                    throw new IllegalArgumentException("Invalid Input.---");
                }
                employeeInformation(employeeNumber_, coveredDays, maxRegularHours);

                return employeeNumber_;

            } catch (NumberFormatException e) {
                System.out.println("--- Error: Invalid Input. Please enter a valid Employee Number. ---");
            } catch (IllegalArgumentException e) {
                System.out.println("--- Error: " + e.getMessage());
            }

        }

    }

    public static void timeEntry(int employeeNumber_, int coveredDays, int maxRegularHours) {
        Scanner timeEntryScan = new Scanner(System.in);

        while (true) {
            System.out.println("\nEnter how many days the employee worked.");
            System.out.println("(Note: maximum working days is set to " + coveredDays + " days.)");

            String workedDaysInput = timeEntryScan.nextLine();

            int workedDays;

            // Create array for timesheet
            ArrayList<Integer> daysList = new ArrayList<>();
            ArrayList<String> timeInList = new ArrayList<>();
            ArrayList<String> timeOutList = new ArrayList<>();
            ArrayList<Integer> regularHoursList = new ArrayList<>();
            ArrayList<Integer> overtimeHoursList = new ArrayList<>();

            try {
                workedDays = Integer.parseInt(workedDaysInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number of days.");
                continue; // Restart the loop
            }
            if (workedDays > coveredDays) {
                System.out.println("Invalid input. The number of days worked exceeds the covered days.");
                continue; // Restart the loop
            }
            if (workedDays == 0) {
                System.out.println("Invalid input. The number of days is null.");
                continue; // Restart the loop
            }

            for (int i = 1; i <= workedDays; i++) {
                System.out.println("\nEnter Day " + i + ": TimeIn, TimeOut\n(Format: HH:mm,HH:mm)");
                String inputLine = timeEntryScan.nextLine();

                String[] timeSheet = inputLine.split(",");
                if (timeSheet.length != 2 || !isValidTimeFormat(timeSheet[0].trim()) || !isValidTimeFormat(timeSheet[1].trim())) {
                    System.out.println("--- Error: Invalid input format. Please enter time in the correct format.---\n Example: 08:00,17:00 ");
                    i -= 1; //Repeat the loop at the same day
                    continue;
                }

                String timeIn = timeSheet[0].trim();
                String timeOut = timeSheet[1].trim();

                daysList.add(i); // append days to daysList
                timeInList.add(timeIn);// append days to timeInList
                timeOutList.add(timeOut);// append days to timeOutList

                int dailyWorkedHours = calculateWorkedHours(timeIn, timeOut);

                int workedregularHours = Math.min(dailyWorkedHours, maxRegularHours);
                regularHoursList.add(workedregularHours);

                int workedOvertimeHours = 0;
                if (dailyWorkedHours > maxRegularHours) {
                    workedOvertimeHours = dailyWorkedHours - maxRegularHours;
                }
                overtimeHoursList.add(workedOvertimeHours);

                System.out.println("\nRegular Time : " + workedregularHours);
                System.out.println("Overtime Time : " + workedOvertimeHours);
                System.out.println("-".repeat(55));
            }

            printTimeSheet(daysList, timeInList, timeOutList, regularHoursList, overtimeHoursList);
            
       
            processData(employeeNumber_,maxRegularHours ,workedDays, coveredDays,regularHoursList, overtimeHoursList);
            
            
           
            break;
        }

    }

    public static Integer regularWorkedHours(ArrayList< Integer> regularHoursList) {
        int totalRegularHour = 0;

        for (int i = 0; i < regularHoursList.size(); i++) {
            totalRegularHour += regularHoursList.get(i);
        }
        return totalRegularHour;
    }

    public static Integer overtimeHours(ArrayList<Integer> overtimeHoursList) {
        
        int totalOvertimeHour = 0;

        for (int i = 0; i < overtimeHoursList.size(); i++) {
            totalOvertimeHour += overtimeHoursList.get(i);
        }

        return totalOvertimeHour;
    }
    
    
    private static boolean isValidTimeFormat(String time) {
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    public static void printTimeSheet(ArrayList<Integer> daysList, ArrayList<String> timeInList,
            ArrayList<String> timeOutList,
            ArrayList<Integer> regularHoursList,
            ArrayList<Integer> overtimeHoursList) {

        System.out.printf("\n\n%" + (55 + "x x x x x TIMESHEET x x x x x ".length()) / 2 + "s%n", "x x x x x TIMESHEET x x x x x ");
        System.out.printf("\n%-5s%-12s%-12s%-14s%-8s\n", "Day", "Time-In", "Time-Out", "Worked Hour", "Overtime");
        for (int i = 0; i < daysList.size(); i++) {
            System.out.printf("%-5d%-12s%-12s%-14d%-8d\n", daysList.get(i), timeInList.get(i), timeOutList.get(i), regularHoursList.get(i), overtimeHoursList.get(i));
        }
        System.out.println("");
        System.out.println(" x".repeat(27));
    }

    public static void processData(int employeeNumber_, int maxRegularHours ,int workedDays, int coveredDays, ArrayList<Integer> regularHoursList, ArrayList<Integer> overtimeHoursList) {

        int index_;
        index_ = employeeNumber_ - 1; //determine the values of each variable

        //Employee Information   
        String employeePosition_;
        String lastName;
        String firstName;
        double grossSemi_monthlyRate;
        double basicSalary;

        employeePosition_ = employeePosition(index_);
        lastName = employeeNameDB(index_).get(0);
        firstName = employeeNameDB(index_).get(1);
        grossSemi_monthlyRate = basicSalaryDB(index_) / 2;
        basicSalary = basicSalaryDB(index_);

        //Benefits
        double riceSubsidy;
        double phoneAllowance;
        double clothingAllowance;
        double totalBenefits;

        riceSubsidy = riceSubsidyDB(index_);
        phoneAllowance = phoneAllowanceDB(index_);
        clothingAllowance = clothingAllowanceDB(index_);
        totalBenefits = riceSubsidy + phoneAllowance + clothingAllowance;

        // Determine Regular Working Hours and Overtime HOurs
        int regularWorkedHour;
        int overtimeHour;

        regularWorkedHour = regularWorkedHours(regularHoursList);
        overtimeHour = overtimeHours(overtimeHoursList);

                
        
        // Computation of Earnings
        double dailyRateCutoff;
        double hourlyRateCutoff;
        double grossIncome;
        double overtimeRate;
        double regularPay;
        double overtimePay;
                
        double takeHomePay;

        dailyRateCutoff = basicSalary / coveredDays;
        hourlyRateCutoff = dailyRateCutoff / maxRegularHours;
        regularPay = hourlyRateCutoff * regularWorkedHour;
        overtimeRate = 1.25; //set overtime pay rate to 25% of the hourlyRate
        overtimePay = overtimeHour * overtimeRate * hourlyRateCutoff;
        grossIncome =  regularPay + overtimePay;

        //Government Deductions (SSS, PhilHealth, Pagibig)
        double sssDeduction;
        double philHealthDeduction;
        double pagIbigDeduction;
        double benefitDeduction;
        double netMonthPay;
        double withHoldingTax;
        double totalDeduction;
        double taxableMonthlyPay;
        double basis;

        basis = grossIncome;  //Value to be considered to determine SSS, Philhealth and Pag-ibig
        sssDeduction = calculateSSS(basis);
        philHealthDeduction = calculatePhilHealth(basis);
        pagIbigDeduction = calculatePagIbig(basis);
        benefitDeduction = sssDeduction + pagIbigDeduction + philHealthDeduction;
        netMonthPay = grossIncome - benefitDeduction;
        taxableMonthlyPay = netMonthPay;
        withHoldingTax = calculateWHTax(taxableMonthlyPay);
        totalDeduction = withHoldingTax + benefitDeduction;
        takeHomePay = grossIncome - totalDeduction + totalBenefits;

        //Print PaySlip
        // Print Personal Information Section
        System.out.println();
        System.out.printf("%" + (55 + "EMPLOYEE PAYSLIP".length()) / 2 + "s%n", "EMPLOYEE PAYSLIP");
        System.out.println("-".repeat(55));
        System.out.println("EMPLOYEE INFORMATION:");
        System.out.printf("%-30s: %s, %s%n", "Name", lastName, firstName);
        System.out.printf("%-30s: %s%n", "Employee Position/ Department", employeePosition_);
        System.out.printf("%-30s: %d%n", "Employee Number", employeeNumber_);
        System.out.printf("%-30s: %s%n", "Cut-off Covered Days", coveredDays);

        // Print Earnings Section
        System.out.println("\nEARNINGS:");
        System.out.printf("%-30s: P%,.2f%n", "Basic Salary", basicSalary);
        System.out.printf("%-30s: P%,.2f%n", "Semi-monthly Rate", grossSemi_monthlyRate);
        System.out.printf("%-30s: P%,.2f%n", "Hourly Rate", hourlyRateCutoff);
        System.out.printf("%-30s: %d%n", "Days Worked", workedDays);
        System.out.printf("%-30s: %d%n", "Hours Worked ", regularWorkedHour);
        System.out.printf("%-30s: P%,.2f%n", "Regular Pay ", regularPay);
        System.out.printf("%-30s: %d%n", "Overtime Hour", overtimeHour);
        System.out.printf("%-30s: P%,.2f%n", "Overtime Pay", overtimePay);
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
        System.out.printf("%-30s: P%,.2f%n", "Phone Allowance", phoneAllowance);
        System.out.printf("%-30s: P%,.2f%n", "Clothing Allowance", clothingAllowance);
        System.out.printf("%-30s: P%,.2f%n", "Total Benefits", totalBenefits);

        // Print Summary Section
        System.out.println("\nSUMMARY:");
        System.out.printf("%-30s: P%,.2f%n", "Gross Income", grossIncome);
        System.out.printf("%-30s: P%,.2f%n", "Total Benefits", totalBenefits);
        System.out.printf("%-30s: P%,.2f%n", "Total Deduction", totalDeduction);
        System.out.printf("%-30s: P%,.2f%n", "Take-Home Pay", takeHomePay);
    }

    public static int calculateWorkedHours(String timeIn, String timeOut) {
        // Split the input string by ":"
        String[] part1 = timeIn.split(":");
        String[] part2 = timeOut.split(":");

        // Convert the hour and minute parts to integers
        int hour_TimeIN = Integer.parseInt(part1[0]);
        int minute_TimeIN = Integer.parseInt(part1[1]);

        int hour_TimeOut = Integer.parseInt(part2[0]);
        int minute_TimeOut = Integer.parseInt(part2[1]);

        int gracePeriod = 10; // grace period in minutes

        LocalTime targetTime = LocalTime.of(8, gracePeriod + 1); //parameter for grace period

        LocalTime parsedTime1 = LocalTime.parse(timeIn); // Parse the TimeIN into a LocalTime object

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

        LocalTime parsedTime2 = LocalTime.parse(timeOut); // Parse the TimeOut into a LocalTime object

        int breakTime = 0; // initialize breakTime
        if (parsedTime1.isBefore(breakStart) && parsedTime2.isAfter(breakEnd)) { //TimeIn after 12:00PM but before 1:00PM, not counted
            breakTime = 1;
        }

        int workedHour_ = workedHour - breakTime;

        return workedHour_;

    }

    public static void employeeInformation(int employeeNumber_, int coveredDays, int maxRegularHours) {
        String lastName;
        String firstName;
        String birthday;
        String address;
        String phoneNumber;
        String sssNumber;
        String philhealthNo;
        String tinNo;
        String pagibig;
        String status;
        String position;
        String immediateSupervisor;
        double basicSalary;
        double riceSubsidy;
        double phoneAllowance;
        double clothingAllowance;
        double grossSemiMonthlyRate;
        double hourlyRate;
        int index_;

        index_ = employeeNumber_ - 1;
        lastName = employeeNameDB(index_).get(0);
        firstName = employeeNameDB(index_).get(1);
        birthday = birthdayDB(index_);
        address = addressDB(index_);
        phoneNumber = phoneNumberDB(index_);
        sssNumber = sssNumberDB(index_);
        philhealthNo = philhealthNoDB(index_);
        tinNo = tinNoDB(index_);
        pagibig = pagibigDB(index_);
        status = employeeStatusDB(index_);
        position = employeePositionDB(index_);
        immediateSupervisor = immediateSupervisorsDB(index_);
        basicSalary = basicSalaryDB(index_);
        riceSubsidy = riceSubsidyDB(index_);
        phoneAllowance = phoneAllowanceDB(index_);
        clothingAllowance = clothingAllowanceDB(index_);
        grossSemiMonthlyRate = basicSalary / 2;
        hourlyRate = basicSalary / (coveredDays * maxRegularHours);

        //Print All information of the Employee
        System.out.println("");
        System.out.printf("%" + (55 + "EMPLOYEE INFORMATION".length()) / 2 + "s%n", "EMPLOYEE INFORMATION");
        System.out.println("-".repeat(55));
        System.out.printf("%-30s: %s%n", "Employee No.", employeeNumber_);
        System.out.printf("%-30s: %s%n", "Last Name", lastName);
        System.out.printf("%" + (55 + "EMPLOYEE INFORMATION".length()) / 2 + "s%n", "EMPLOYEE INFORMATION");
        System.out.printf("%-30s: %s%n", "First Name", firstName);
        System.out.printf("%-30s: %s%n", "Birthday", birthday);
        System.out.printf("%-30s: %s%n", "Address", address);
        System.out.printf("%-30s: %s%n", "Phone Number", phoneNumber);
        System.out.printf("%-30s: %s%n", "SSS Number", sssNumber);
        System.out.printf("%-30s: %s%n", "Philhealth Number", philhealthNo);
        System.out.printf("%-30s: %s%n", "TIN Number", tinNo);
        System.out.printf("%-30s: %s%n", "Pag-ibig Number", pagibig);
        System.out.printf("%-30s: %s%n", "Status", status);
        System.out.printf("%-30s: %s%n", "Position", position);
        System.out.printf("%-30s: %s%n", "Immediate Supervisor", immediateSupervisor);
        System.out.printf("%-30s: P%,.2f%n", "Basic Salary", basicSalary);
        System.out.printf("%-30s: P%,.2f%n", "Rice Subsidy", riceSubsidy);
        System.out.printf("%-30s: P%,.2f%n", "Phone Allowance", phoneAllowance);
        System.out.printf("%-30s: P%,.2f%n", "Clothing Allowance", clothingAllowance);
        System.out.printf("%-30s: P%,.2f%n", "Gross Semi-monthly Rate", grossSemiMonthlyRate);
        System.out.printf("%-30s: P%,.2f%n", "Hourly Rate", hourlyRate);
        System.out.println("-".repeat(55));

    }

    public static double calculateWHTax(double taxableMonthlyPay) {
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

    public static double calculateSSS(double basis) {
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
            if (basis < sssSalary.get(i)) {
                SSS_ = sssContribution.get(i);
                break;
            } else {
                SSS_ = 1125.0;   // max contribution
            }
        }

        return SSS_;
    }

    public static double calculatePagIbig(double basis) {
        double pagIBIG = 0;

        if (basis >= 1000 && basis <= 1500) {
            pagIBIG = basis * 0.01;
        } else if (basis > 1500) {
            pagIBIG = basis * 0.02;
        }

        double maxContribution = 100.0; //  set max pag-ibig contribution to 100 
        double pagIBIG_ = Math.min(pagIBIG, maxContribution);

        return pagIBIG_;
    }

    public static double calculatePhilHealth(double basis) {
        double philHealth = basis * 0.03 / 2; // Employees contribution half of 3% of basicSalary.2020 mandate

        int minValue = 300 / 2; // minimum value
        int maxValue = 1800 / 2; // maximum value
        double philHealth_;
        philHealth_ = Math.min(Math.max(philHealth, minValue), maxValue);

        return philHealth_;
    }

    //Methods below are for the database
    public static ArrayList<String> employeeNameDB(int index_) {
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

    public static int[] employeeNumberDB() {
        int[] employeeNumber = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34};

        return employeeNumber;
    }

    public static double basicSalaryDB(int index_) {
        int[] salaries = {90000, 60000, 60000, 60000, 52670, 52670, 42975, 22500, 22500, 52670,
            50825, 38475, 24000, 24000, 53500, 42975, 41850, 22500, 22500, 23250,
            23250, 24000, 22500, 22500, 24000, 24750, 24750, 24000, 22500, 22500,
            22500, 52670, 52670, 52670};

        int basicSalary = salaries[index_];
        return basicSalary;
    }

    public static double phoneAllowanceDB(int index_) {
        // Phone allowance based on search criteria
        int[] phoneAllowance = {2000, 2000, 2000, 1000, 1000, 800, 500, 500, 1000, 1000,
            800, 500, 500, 1000, 800, 800, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 1000, 1000, 1000};

        int phoneAllowance_ = phoneAllowance[index_];
        return phoneAllowance_;
    }

    public static double clothingAllowanceDB(int index_) {
        // Clothing allowance based on search criteria
        int[] clothingAllowance = {1000, 1000, 1000, 1000, 1000, 800, 500, 500, 1000, 1000,
            800, 500, 500, 1000, 800, 800, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 1000, 1000, 1000};

        int clothingAllowance_ = clothingAllowance[index_];
        return clothingAllowance_;
    }

    public static double riceSubsidyDB(int index_) {

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

    public static String birthdayDB(int index_) {

        String[] birthdayDB = {
            "10/11/1983", "06/19/1988", "08/04/1989", "06/16/1994", "09/23/1989",
            "02/14/1988", "03/15/1996", "05/14/1992", "09/24/1948", "03/30/1988",
            "09/14/1993", "01/14/1987", "01/11/1942", "07/11/1970", "03/10/1985",
            "10/21/1987", "02/20/1975", "06/24/1986", "10/06/1996", "02/12/1991",
            "11/25/1985", "02/26/1980", "12/31/1983", "12/18/1978", "05/19/1984",
            "12/18/1970", "08/28/1986", "12/12/1981", "08/20/1978", "04/14/1973",
            "01/27/1989", "02/09/1992", "11/16/1990", "08/07/1990"
        };

        String birthdayDB_ = birthdayDB[index_];

        return birthdayDB_;

    }

    public static String addressDB(int index_) {

        String[] addressDB = {
            "Valero Carpark Building Valero Street 1227, Makati City",
            "San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite",
            "Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City",
            "460 Solanda Street Intramuros 1000, Manila",
            "National Highway, Gingoog, Misamis Occidental",
            "17/85 Stracke Via Suite 042, Poblacion, Las Piñas 4783 Dinagat Islands",
            "99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi",
            "12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte",
            "90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte",
            "#284 T. Morato corner, Scout Rallos Street, Quezon City",
            "93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate",
            "49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro",
            "42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte",
            "37/46 Kulas Roads, Maragondon 0962 Quirino",
            "22A/52 Lubowitz Meadows, Pililla 4895 Zambales",
            "90 O'Keefe Spur Apt. 379, Catigbian 2772 Sulu",
            "89A Armstrong Trace, Compostela 7874 Maguindanao",
            "08 Grant Drive Suite 406, Poblacion, Iloilo City 9186 La Union",
            "93A/21 Berge Points, Tapaz 2180 Quezon",
            "65 Murphy Center Suite 094, Poblacion, Palayan 5636 Quirino",
            "47A/94 Larkin Plaza Apt. 179, Poblacion, Caloocan 2751 Quirino",
            "06A Gulgowski Extensions, Bongabon 6085 Zamboanga del Sur",
            "99A Padberg Spring, Poblacion, Mabalacat 3959 Lanao del Sur",
            "80A/48 Ledner Ridges, Poblacion, Kabankalan 8870 Marinduque",
            "96/48 Watsica Flats Suite 734, Poblacion, Malolos 1844 Ifugao",
            "58A Wilderman Walks, Poblacion, Digos 5822 Davao del Sur",
            "60 Goyette Valley Suite 219, Poblacion, Tabuk 3159 Lanao del Sur",
            "66/77 Mann Views, Luisiana 1263 Dinagat Islands",
            "72/70 Stamm Spurs, Bustos 4550 Iloilo",
            "50A/83 Bahringer Oval Suite 145, Kiamba 7688 Nueva Ecija",
            "95 Cremin Junction, Surallah 2809 Cotabato",
            "Hi-way, Yati, Liloan Cebu",
            "Bulala, Camalaniugan",
            "Agapita Building, Metro Manila"
        };
        String addressDB_ = addressDB[index_];

        return addressDB_;
    }

    public static String phoneNumberDB(int index_) {
        String[] phoneNumberDB = {
            "966-860-270", "171-867-411", "966-889-370", "786-868-477", "088-861-012",
            "918-621-603", "797-009-261", "983-606-799", "266-036-427", "053-381-386",
            "070-766-300", "478-355-427", "329-034-366", "877-110-749", "023-079-009",
            "783-776-744", "975-432-139", "179-075-129", "868-819-912", "683-725-348",
            "740-721-558", "739-443-033", "955-879-269", "882-550-989", "675-757-366",
            "512-899-876", "948-628-136", "332-372-215", "250-700-389", "973-358-041",
            "529-705-439", "332-424-955", "078-854-208", "526-639-511"
        };
        String phoneNumberDB_ = phoneNumberDB[index_];

        return phoneNumberDB_;
    }

    public static String sssNumberDB(int index_) {
        String[] sssNumberDB = {
            "44-4506057-3", "52-2061274-9", "30-8870406-2", "40-2511815-0", "50-5577638-1",
            "49-1632020-8", "40-2400714-1", "55-4476527-2", "41-0644692-3", "64-7605054-4",
            "26-9647608-3", "44-8563448-3", "45-5656375-0", "27-2090996-4", "26-8768374-1",
            "49-2959312-6", "27-2090208-8", "45-3251383-0", "49-1629900-2", "49-1647342-5",
            "45-5617168-2", "52-0109570-6", "52-9883524-3", "45-5866331-6", "47-1692793-0",
            "40-9504657-8", "45-3298166-4", "40-2400719-4", "60-1152206-4", "54-1331005-0",
            "52-1859253-1", "26-7145133-4", "11-5062972-7", "20-2987501-5"
        };
        String sssNumberDB_ = sssNumberDB[index_];

        return sssNumberDB_;
    }

    public static String philhealthNoDB(int index_) {
        String[] philhealthNoDB = {
            "820126853951", "331735646338", "177451189665", "341911411254", "957436191812",
            "382189453145", "239192926939", "545652640232", "708988234853", "578114853194",
            "126445315651", "431709011012", "233693897247", "515741057496", "308366860059",
            "824187961962", "587272469938", "745148459521", "579253435499", "399665157135",
            "606386917510", "357451271274", "548670482885", "953901539995", "753800654114",
            "797639382265", "810909286264", "934389652994", "351830469744", "465087894112",
            "136451303068", "601644902402", "380685387212", "918460050077"
        };
        String philhealthNoDB_ = philhealthNoDB[index_];

        return philhealthNoDB_;
    }

    public static String pagibigDB(int index_) {
        String[] pagibigNumbers = {
            "691295330870", "663904995411", "171519773969", "416946776041", "952347222457",
            "441093369646", "210850209964", "211385556888", "260107732354", "799254095212",
            "218002473454", "113071293354", "631130283546", "101205445886", "223057707853",
            "631052853464", "719007608464", "114901859343", "265104358643", "260054585575",
            "104907708845", "113017988667", "360028104576", "913108649964", "210546661243",
            "210897095686", "211274476563", "122238077997", "212141893454", "515012579765",
            "110018813465", "697764069311", "993372963726", "874042259378"
        };
        String pagibigNumbers_ = pagibigNumbers[index_];

        return pagibigNumbers_;
    }

    public static String tinNoDB(int index_) {
        String[] tinNumbers = {
            "442-605-657-000", "683-102-776-000", "971-711-280-000", "876-809-437-000", "031-702-374-000",
            "317-674-022-000", "672-474-690-000", "888-572-294-000", "604-997-793-000", "525-420-419-000",
            "210-805-911-000", "218-489-737-000", "210-835-851-000", "275-792-513-000", "598-065-761-000",
            "103-100-522-000", "482-259-498-000", "121-203-336-000", "122-244-511-000", "273-970-941-000",
            "354-650-951-000", "187-500-345-000", "101-558-994-000", "560-735-732-000", "841-177-857-000",
            "502-995-671-000", "336-676-445-000", "210-395-397-000", "395-032-717-000", "215-973-013-000",
            "599-312-588-000", "404-768-309-000", "256-436-296-000", "911-529-713-000"
        };

        String tinNumbers_ = tinNumbers[index_];

        return tinNumbers_;
    }

    public static String employeePositionDB(int index_) {

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

    public static String immediateSupervisorsDB(int index_) {
        String[] immediateSupervisorsDB = {
            "N/A", "Garcia, Manuel III", "Garcia, Manuel III", "Garcia, Manuel III", "Lim, Antonio",
            "Lim, Antonio", "Villanueva, Andrea Mae", "San, Jose Brad", "San, Jose Brad", "Aquino, Bianca Sofia",
            "Alvaro, Roderick", "Salcedo, Anthony", "Salcedo, Anthony", "Salcedo, Anthony", "Lim, Antonio",
            "Romualdez, Fredrick", "Romualdez, Fredrick", "Mata, Christian", "Mata, Christian", "Mata, Christian",
            "Mata, Christian", "Mata, Christian", "Mata, Christian", "Mata, Christian", "De Leon, Selena",
            "De Leon, Selena", "De Leon, Selena", "De Leon, Selena", "De Leon, Selena", "De Leon, Selena",
            "De Leon, Selena", "Reyes, Isabella", "Reyes, Isabella", "Reyes, Isabella"
        };

        String immediateSupervisorsDB_ = immediateSupervisorsDB[index_];

        return immediateSupervisorsDB_;
    }

    public static String employeeStatusDB(int index_) {
        String[] employeeStatusDB = {
            "Regular", "Regular", "Regular", "Regular", "Regular", "Regular",
            "Regular", "Regular", "Regular", "Regular", "Regular", "Regular",
            "Regular", "Regular", "Regular", "Regular", "Regular", "Regular",
            "Regular", "Regular", "Probationary", "Probationary", "Probationary", "Probationary",
            "Probationary", "Probationary", "Probationary", "Probationary", "Probationary", "Probationary",
            "Regular", "Regular", "Regular", "Regular"
        };

        String employeeStatusDB_ = employeeStatusDB[index_];

        return employeeStatusDB_;

    }

    public static void clearTerminal() {
        for (int i = 0; i < 60; i++) {    //creates nexline 60
            System.out.println("");
        }
    }

}
