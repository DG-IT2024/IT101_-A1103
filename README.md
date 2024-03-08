# IT101_-A1103
MotorPH Project

The program will calculate the payroll based on the number of hours worked by an employee. You will be asked to provide the employee number, and time IN and time Out. 


<Input Notes>
Separate time entries with comma
Write Time-In followed by Time-Out.
Time should follow HH:mm format.
Input 24-hour format

<Limitations>
12:00-13:00(BreakTime). not counted in computed worked hours
Maximum regular paid hours is 8hours
GrossIncome(Pay for hours worked) is used to determine the SSS, PhilHealth, Pag-ibig deductions
Overtime is only computed if employee works for more than 8hours
Overtime pay consideration. Available options: 1)Don't consider Overtime(rate set to 0) 2)Overtime Pay rate 
This program computes for one month payroll. 
Working days is 20days for one month. Maximum days
Work starts at 8:00AM
Grace period of 10mins. Considered late if Time-in 8:11.
Only consider worked hours per day. any fraction thereof is not considered in the payroll computation

<Features>
Clear Console (limited to platforms that allows ANSI )
Continuous proccessing of payroll
TimeSheet is generated after encoding the TimeIn/TimeOut
Regular and overtime hours separated 
Allows setting of overtime rate per day
Option to disregard overtime if it is not approved
Calculate overtime hours
Calculate regular hours
calculate SSS, Pag-ibig, Philhealth and Withholding tax
Calculate net salary based on worked hours, deductions and benefits

