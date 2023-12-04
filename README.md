# Real-time Transaction Notification System

This project implements a real-time transaction notification system using Apache Kafka, PySpark, and Twilio. It monitors transactions in a financial system and sends immediate WhatsApp notifications to users when a transaction occurs.

## Use Case

### Workflow

1. **Data Ingestion and Processing:**
    - `Main.java`: Produces simulated transaction data and sends it to the Kafka topic `transaction_Card_2`.
    - `Python notebook`: Listens to `transaction_Card_2`, processes incoming data using PySpark, and enriches it with user information from `accounts.csv`.

2. **Notification Generation:**
    - PySpark identifies new transactions, formats personalized messages, and sends WhatsApp notifications using Twilio.

3. **Real-time Output:**
    - Processed data with personalized messages stored in an in-memory table using PySpark.
    - Users can query the table to view transaction details and notifications in real-time.

4. **Deployment and Monitoring:**
    - Deployable to cloud-based services or local servers.
    - Includes functionalities to monitor and stop the streaming process.

### Repository Structure

- **Producer Code (`Main.java`):**
    - `src/`: Contains Java code for simulating transaction data.
    - `README.md`: Instructions to execute the producer code.

- **Consumer Code (`Transaction_Notification_System.ipynb`):**
    - Jupyter notebook with PySpark code for processing Kafka stream data, sending notifications, and managing streaming.
    - `accounts.csv`: Dataset with user information.
    - `README.md`: Setup instructions and prerequisites.

- **Deployment Scripts:**
    - Shell scripts or Dockerfiles for deployment on different environments.
    - Configuration files for Kafka and Spark configurations.



---

Feel free to adjust the headings, content, and sections according to your preferences and project specifics.
