# ⚽ Football Match Analysis & Prediction System

This project is dedicated to reproducing a near-professional environment for code development. It simulates real-world practices, including:

- Team structure and collaboration
- Workload planning and division using **GitLab Issues**
- Weekly team meetings for coordination

---

## 🎯 Project Objective

The subject of our work is the **analysis of football matches** and the **prediction of future game outcomes** based on historical data and statistical modeling.

---

## 🛠️ Operation Modes

The system supports **three main modes of operation**:

1. **CLI Mode**
   - User interacts directly with the main application through the command line.

2. **Client-Server CLI Mode (TCP/UDP)**
   - User communicates with a CLI **client** application.
   - The client connects to a **server** that performs calculations and accesses the data.

3. **GUI API Mode**
   - User interacts with a **web-based GUI**.
   - Hosted on a **Tomcat server** running a **Java EE** application.

---

## 📊 Features

- View detailed information on:
  - **Teams**
  - **Players**
  - **Coaches**
  - **Matches**
  
- Predict **win rate probabilities** for upcoming matches using historical data:
  - User selects a **home team** and an **away team**
  - The model calculates probabilities based on:
    - Team performance statistics
    - Head-to-head history

> **Note**: Predictions are directional. That is, analysis for *Team A (home)* vs *Team B (away)* may yield different results than the reverse.

---

## 📂 Data Source & Processing

- Raw data obtained from **Kaggle**, combining multiple datasets:
  - Players
  - Teams
  - Matches

- Extensive **ETL** (Extract, Transform, Load) was performed to:
  - Clean the data
  - Resolve inconsistencies
  - Merge multiple datasets into a single **concise database**

- Due to data gaps in some datasets (e.g., lack of women's data), the scope was **restricted to men's football** only.

---

## 🧱 Deployment Options

The project is available in **two build formats**:

- **Java SE**: `.jar` executable for CLI usage
- **Java EE**: `.war` archive deployed via **Tomcat 10**

> Built and managed using **Maven**.

---

## 👨‍💻 Authors

- Mikołaj Woźniak  
- Jakub Szurmak  
- Kacper Sochacki  
- Szymon Różycki  
- Michał Sondej
