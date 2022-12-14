# 6-in-1 Calculator



Being a student, one often comes across calculations that invlove converting a value from one standard unit to the other. The 6-in-1 calculator app is built essentially for this purpose. It provides major mathematical/computational convertors used frequently, all packed in one application, to save time!

## 🚩 Table of Contents
- [Installation instructions](#Installation-instructions)
- [App description](#App-description)
  1. [Currency](#Currency)
  2. [Weight](#Weight)
  3. [Temperature](#Temperature)
  4. [Length](#Length)
  5. [Volume](#Volume)
  6. [Power](#Power)
  7. [Calculator](#Calculator)   
- [Manifest files](#Manifest-files)
- [How it works](#How-it-works)
- [License](#License)
- [Contact information](#Contact-Information)
- [Trouble-shooting Tips](#TroubleShooting-tips)
- [Credits & Acknowledgments](#Credits-and-acknowledgements)
- [Donations](#Donations)
- [FAQs](#FAQ)


### **App description**
_The application is built upon Model-View-Control Architecture. Here is a description about what to expect after opening the application on your android device
A default screen with the calculator appears. A side window with all options pops open on clicking the hamburger icon on top-left_
|Currency|weight|temperature|
|---|---|---|
|length|volume|power

### Currency
Supported with the latest currency market values, you can convert and view between nine major currencies of the world; i.e. 81 different conversion rates. 
Choose the currency you want to convert, from the drop down-menu on the left-hand side to the currency that you select from the drop-down menu on the right-hand side of the screen. The conversion is automated to reflect on the screen with no further instruction or assistance from the user.

![currency](https://user-images.githubusercontent.com/70349256/115792221-e1e1e880-a397-11eb-986e-52f87ca71c34.jpeg)



### Weight
The user can convert values between kilogram, gram, pound and ounce. Conversions are automated once the units are selected on either side.  
### Temperature
Temperature units provided are between degree Celcius, Fahrenheit and the Kelvin scale. 
### Length
Users are provided with five length units namely miles, feet, kilometer, meter and centimeter. They can convert values between any two in the above list.
### Volume
Voulme units provided are cubic meter, cubic centimeter, cubic milimeter, litre and millilitre.
### Power
Power units available for conversion are Watts, Kilowatts, Megawatts and Gigawatts
### Calculator
No application built for mathematical assitance could be complete without a calculator. The calculator in the 6-in-1 application provides a range of functions such as: basic mathematical opeerations, squared, multiplied, bodmas, negative numbers and decimal numbers.

![output-onlinegiftools](https://user-images.githubusercontent.com/70349256/115795665-53249a00-a39e-11eb-8915-f00c1881ea58.gif)

## Manifest files
The manifest file is a very important component; it describes essential information about an application to the Android build tools and operating system. <br/>
* The build first executes the instructions in splash activity leading to the appliaction icon inflation onto the screen, for about 2 seconds. Following this the instruction sets in start activity are executed leading to currency initialization using API. A database is created simultaneously to store the currency values.
* When the application is not connected to the internet, it computes using previously stored currency values in the database. 
* Set of instructions in Home Activity inflate the calculator on the main screen
* Set of instructions in Main Activity inflates the navigation drawer and the multiple fragments associated with its implementation. 
___

## How it works
* This application uses Model-View-Controller design to combine and operate different units as a single application. 
* Adapters and databases are created for each unit. When the user taps on a specific convertor and selects a unit for conversion from the drop down menu; the adapter inflates the converted value of the choice/selection using set-values from the database. This design organizes each individual unit in the application in a way to make the implementation simple and effective.

***
* While measurement units like Length, power, temperature, volume and weight are standardised by the International System of units; a medium like currency is ever-changing. For the application to be effective and reliable it is very important to incorporate a means to update the currency values within the application. For this `API` was used. [This](https://openexchangerates.org/) is the website used for currency updation.

```kotlin

// This creates the database required to handle data from the stated website
    private val databaseHelper: DatabaseHelper = DatabaseHelperImpl(context)

    // This method is used to get live updates from exchange rates website using api and uses different method to handle the situations
    fun getExchangeRates() {
        service.getLatestExchangeRates().enqueue(object : retrofit2.Callback<ExchangeRatesResponse> {
            override fun onFailure(call: retrofit2.Call<ExchangeRatesResponse>, t: Throwable) {
                Log.e(TAG, t.message)
            }
```

## Contact Information
This application was built as an independent project for the course COMP-2430

## Credits and acknowledgements
Dr Trevor Tomesh - Mentoring and guidance through whole project.

## FAQ
Does the application require internet?
For using calculator, weight, temperature, power, volume and length conversions an internet connection is not required. You can use the currency convertor offline, but the conversion rates will be according to the last stored update. Therefore, it is advised to use an internet connection to get the most accurate rates.

How often is the currency rate updated?
All currencies are updated at the end of each day.

