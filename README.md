# QoE Measurement App

## Introduction

Network operators constantly strive to evaluate and improve the efficiency and performance of their networks. One critical aspect of this evaluation is the Key Performance Indicator (KPI). However, what truly matters to operators is understanding the end user's perception of the quality of services provided, known as Quality of Experience (QoE) parameters.

Measuring QoE parameters from an end user's perspective offers valuable insights, especially since many KPI parameters related to the Radio Access Network (RAN) and the network core can be derived from these measurements. Despite the challenge that a single user's view might not represent the entire network, this method is invaluable when the user is moving through the network.

To address this, we have developed an application that measures various parameters from an end user's device.

## Features

The application collects and records the following parameters:

1. **Location of 10 Users:**
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

## Installation

To install the application on your Android device, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/qoe-measurement-app.git
    ```
2. Navigate to the project directory:
    ```sh
    cd qoe-measurement-app
    ```
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

### Screenshot 1

*Description of Screenshot 1.*

![Screenshot 1](path/to/screenshot1.png)

### Screenshot 2

*Description of Screenshot 2.*

![Screenshot 2](path/to/screenshot2.png)

### Screenshot 3

*Description of Screenshot 3.*

![Screenshot 3](path/to/screenshot3.png)

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests for any features, enhancements, or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For any questions or feedback, please contact [your name] at [your email address].

