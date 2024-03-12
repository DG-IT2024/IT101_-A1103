# IT101_-A1103
MotorPH Project

The program will calculate the payroll based on the number of hours worked by an employee. You will be asked to provide the employee number, and time IN and time Out. 

**Input Notes**
- Separate time entries with comma
- Write Time-In followed by Time-Out.
- Time should follow HH:mm format.
- Input time in 24-hour format

**Limitations**
- 12:00-13:00 (BreakTime). Not counted in computed worked hours
- Maximum regular paid hours is 8 hours
- Maximum worked days is 20days for one month.
- Hourly rate is the quotient of BasicSalary divided by the product of maximum regulars hours (8) and maximum worked days(20). 
- Gross Income (Pay for hours worked) is used to determine the SSS, PhilHealth, Pag-ibig deductions
- Overtime is only computed if employee works for more than 8 hours
- Overtime pay consideration.
Options:
  1. Don't credit overtime (rate set to 0)
  2. Set overtime pay rate(e.g. 1.25)
- Program computes for one month payroll.
- Work starts at 8:00AM
- Grace period of 10 mins. Considered late if Time-in 8:11.
- Only consider worked hours per day. Any fraction thereof is not considered in the payroll computation

**Features**
- Clear Console (limited to platforms that allow ANSI. Generates newline after ANSI)
- Continuous processing of payroll
- TimeSheet is generated after encoding all the Time-In/Time-Out for the month
- Computed regular and overtime worked hours are shown after each Time-In/Time-Out entry
- Allows setting of overtime rate per day
- Option to disregard overtime if it is not approved
- Calculate overtime hours
- Calculate regular hours
- Calculate SSS, Pag-ibig, Philhealth, and Withholding tax
- Calculate net salary based on worked hours, deductions, and benefits
- Generate payslip after getting the employee number and timesheet
