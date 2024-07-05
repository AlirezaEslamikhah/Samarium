# QoE Measurement App

## Introduction

Network operators constantly strive to evaluate and improve the efficiency and performance of their networks. One critical aspect of this evaluation is the Key Performance Indicator (KPI). However, what truly matters to operators is understanding the end user's perception of the quality of services provided, known as Quality of Experience (QoE) parameters.

Measuring QoE parameters from an end user's perspective offers valuable insights, especially since many KPI parameters related to the Radio Access Network (RAN) and the network core can be derived from these measurements. Despite the challenge that a single user's view might not represent the entire network, this method is invaluable when the user is moving through the network.

To address this, we have developed an application that measures various parameters from an end user's device.

## Features

The application collects and records the following parameters:

1. **Location of Users:**
   - Latitude
   - Longitude

2. **Event Registration Time:**
   - Timestamp of each recorded event

3. **Cellular Technology:**
   - LTE
   - HSPA+
   - HSPA
   - UMTS
   - EDGE
   - GPRS
   - GSM
   - LTE-Advanced
   - 5G

4. **Cell Location Identifiers:**
   - TAC (Tracking Area Code)
   - RAC (Routing Area Code)
   - LAC (Location Area Code)
   - PLMN-Id (Public Land Mobile Network Identifier)
   - Cell ID

5. **Signal Quality and Quantity:**
   - Fourth Generation (4G):
     - RSRP (Reference Signal Received Power)
     - RSRQ (Reference Signal Received Quality)
   - Third Generation (3G):
     - RSCP (Received Signal Code Power)
     - Ec/N0 (Energy per chip over Noise)

## Important Note

The most important point in this section is that the user might be in a place where there is no GPS coverage. For example, the user could be in a subway or a long tunnel and moving along the subway or tunnel route. In such scenarios, relying solely on GPS for location tracking is not feasible. Alternative methods must be employed to determine the user's location accurately under these conditions.

## Installation

To install the application on your Android device, follow these steps:

1. Clone the repository:
    ```sh
    git clonehttps://github.com/AlirezaEslamikhah/Samarium.git
    ```
2. Navigate to the project directory:
3. Open the project in Android Studio.
4. Build and run the application on your device.

## Usage

1. Ensure the application has the necessary permissions to access location, cellular network information, and other required functionalities.
2. Open the application.
3. Start the measurement process by clicking the appropriate button.
4. The application will continuously collect and record the specified parameters as you move through different locations.
5. View the recorded data within the application.
6. Use export options to save the data for further analysis.

## Screenshots

Here are some screenshots of the application in action, displaying the collected parameters and their values.

### Map picture 

*Map showing which presents the the quality and the status of our location records*

![Screenshot 1]("pictures/pic1.png")

### Main Page Picture 

*An overview of the main page of the app showing the features and updates during the time *

![Screenshot 2]("pictures/pic2.png")




## Contributing

Contributions are welcome! Please fork the repository and submit pull requests for any features, enhancements, or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For any questions or feedback, please contact me at alirezasl2014@gmail.com or @alireza_1080 on Telegram 
