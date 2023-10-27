# Trendyol API Integration and Inventory Management System

This project is designed to integrate with the Trendyol API to fetch orders and update the stock levels in our local store. It also includes functionality to promote products on Trendyol by running a scheduled task regularly. Additionally, the system allows for sales tracking using barcodes and sends email notifications in case of low stock or any errors.

## Features

- **Trendyol API Integration**: Automatically fetch orders from Trendyol and update local inventory.
- **Product Promotion**: Regularly promote products on Trendyol using a scheduled task.
- **Barcode Sales Tracking**: Track sales and manage inventory using barcodes.
- **Email Notifications**: Send email notifications for low stock levels or in case of errors.
- **Inventory Management**: Keep track of products in the store, updating stock levels as sales are made.

## Technologies Used

- **Spring Boot**: For creating the REST API and scheduled tasks.
- **Java Persistence API (JPA)**: For interacting with the PostgreSQL database.
- **PostgreSQL**: As the relational database to store inventory and order data.
- **Maven**: For dependency management and project build.

## Getting Started

### Prerequisites

- Java JDK 11 or later
- Maven
- PostgreSQL

### Installation

1. Clone the repository:
   ```bash
    [git clone](https://github.com/farukaydogan/TrendyolAndMyMarketStockTrackingIntegrationAndBarcodeSale.git)


   Navigate to the project directory:


Copy code
cd yourrepository
Install the dependencies:


Copy code
mvn install
Configure your database settings in src/main/resources/application.properties.

Run the application:

bash
Copy code
mvn spring-boot:run
Usage
The application can be used to manage inventory, track sales, and integrate with the Trendyol API.

Fetch Orders: The application will automatically fetch orders from Trendyol and update the local inventory.
Promote Products: Products will be regularly promoted on Trendyol via a scheduled task.
Manage Sales: Sales can be tracked and managed using barcode scanning.
Receive Notifications: Receive email notifications for low stock levels or errors.
Contributing
Contributions are welcome! For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

License
This project is licensed under the MIT License - see the LICENSE file for details

Contact
If you have any questions or feedback, please contact me at faruk034595@gmail.com


<img width="1499" alt="Screenshot 2023-10-28 at 02 26 44" src="https://github.com/farukaydogan/TrendyolAndMyMarketStockTrackingIntegrationAndBarcodeSale/assets/57232389/e6882123-1540-4cf1-a3fd-8d1b28a8f3f9">
<img width="1465" alt="Screenshot 2023-10-28 at 02 27 02" src="https://github.com/farukaydogan/TrendyolAndMyMarketStockTrackingIntegrationAndBarcodeSale/assets/57232389/6427443a-4584-496c-b061-4279ddf045d7">
<img width="836" alt="Screenshot 2023-10-28 at 02 27 21" src="https://github.com/farukaydogan/TrendyolAndMyMarketStockTrackingIntegrationAndBarcodeSale/assets/57232389/453da61f-d48e-4a96-9c17-d434c4f38196">
<img width="709" alt="Screenshot 2023-10-28 at 02 27 39" src="https://github.com/farukaydogan/TrendyolAndMyMarketStockTrackingIntegrationAndBarcodeSale/assets/57232389/2ef1adff-7143-446f-8977-97b21986714e">
