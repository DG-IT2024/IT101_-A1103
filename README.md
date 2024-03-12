# IT101_-A1103
MotorPH Project

The program will calculate the payroll based on the number of hours worked by an employee. You will be asked to provide the employee number, and time IN and time Out. 

**Input Notes**
- Separate time entries with comma
- Write Time-In followed by Time-Out.
- Time should follow HH:mm format.
- Input 24-hour format

**Limitations**

- 12:00-13:00 (BreakTime). Not counted in computed worked hours
- Maximum regular paid hours is 8 hours
- Gross Income (Pay for hours worked) is used to determine the SSS, PhilHealth, Pag-ibig deductions
- Overtime is only computed if employee works for more than 8 hours
- Overtime pay consideration.
 options:
  1. Don't consider Overtime (rate set to 0)
  2. Set overtime Pay rate(e.g. 1.25)
- Program computes for one month payroll.
- Maximum Working-days is 20days for one month.
- Maximum days Work starts at 8:00 AM
- Grace period of 10 mins. Considered late if Time-in 8:11.
- Only consider worked hours per day. Any fraction thereof is not considered in the payroll computation

**Features**
- Clear Console (limited to platforms that allow ANSI)
- Continuous processing of payroll
- TimeSheet is generated after encoding the TimeIn/TimeOut
- Regular and overtime hours separated
- Allows setting of overtime rate per day
- Option to disregard overtime if it is not approved
- Calculate overtime hours
- Calculate regular hours
- Calculate SSS, Pag-ibig, Philhealth, and Withholding tax
- Calculate net salary based on worked hours, deductions, and benefits
