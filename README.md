# Data Structure Visualizer

An interactive educational platform built with JavaFX and MongoDB that enhances computer science learning through visualization of fundamental data structures.

## Overview

The Data Structures Visualizer is an educational application designed to help students learn and understand data structures through interactive visualizations. It provides animated demonstrations of fundamental data structures including arrays, linked lists, stacks, queues, trees, and graphs, along with structured tutorials and practice problems.

<img width="1254" alt="Screenshot 2025-04-23 at 4 17 42 PM" src="https://github.com/user-attachments/assets/9c43b667-61c5-403a-adf2-7d37c491e1c5" />

## Features

### Interactive Visualizations
- Real-time, manipulable visualizations of six core data structures
- Step-by-step animations of fundamental operations
- User-friendly interface with intuitive controls

<img width="1202" alt="Screenshot 2025-04-23 at 3 55 33 PM" src="https://github.com/user-attachments/assets/cb8ef2a6-8610-4de5-9c9e-78e9377426ae" />

### Comprehensive Tutorials
- Structured learning materials for each data structure
- Step-by-step explanations with external resource links
- Progress tracking across tutorials

<img width="1191" alt="Screenshot 2025-04-23 at 3 54 25 PM" src="https://github.com/user-attachments/assets/8fd33c02-b059-43af-80eb-adecb2913cac" />

### Practice Problems
- Curated LeetCode integration for practical application
- Organized by data structure type and difficulty level
- Progress monitoring and recommendations

<img width="1198" alt="Screenshot 2025-04-23 at 3 55 04 PM" src="https://github.com/user-attachments/assets/b25528c6-aca8-4ccd-8fe6-f05a421feb97" />

### User Profiles
- Personalized accounts to track progress
- Comprehensive progress monitoring
- Analytics-driven dashboard with recommendations

<img width="1222" alt="Screenshot 2025-04-23 at 4 19 34 PM" src="https://github.com/user-attachments/assets/b95bd4ad-aebb-47c8-bad5-9e89ed4e04f8" />

## Technology Stack

- **Frontend**: JavaFX, FXML, CSS
- **Backend**: Java 21
- **Database**: MongoDB (Cloud Atlas)
- **Libraries**: 
  - JBCrypt (v0.4) for secure password hashing
  - org.json (v20210307) for JSON data processing
  - JavaFX WebView for tutorial content
- **Build Tool**: Maven

## Object-Oriented Design Patterns

- **MVC Architecture**: Clear separation of models, views, and controllers
- **Singleton Pattern**: Used for UserSession, TutorialService, and ProblemService
- **Factory Method**: For creating User objects from MongoDB documents
- **Observer Pattern**: Implemented through JavaFX event listeners and property bindings

## Setup Instructions

### Prerequisites
- Java JDK 21
- Maven (or use the included wrapper)
- IntelliJ IDEA
- JavaFX library

### Installation
1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Configure JavaFX:
   - Go to Run → Edit Configurations
   - Click "+" to create new configuration → select "Application"
   - Name the configuration "Main"
   - Main class: `org.example.demo1.HelloApplication`
   - VM options: `--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml,javafx.web`
4. Run the application

## Features in Action

### Array Operations
<img width="1168" alt="Screenshot 2025-04-23 at 3 59 03 PM" src="https://github.com/user-attachments/assets/a7f52ecc-965c-4876-9911-91f2d7fde96e" />

### Linked List Operations
<img width="1172" alt="Screenshot 2025-04-23 at 3 58 22 PM" src="https://github.com/user-attachments/assets/0f504ccd-e042-41b8-90fa-4eefaf48a7e6" />

### Stack and Queue Operations
<img width="1183" alt="Screenshot 2025-04-23 at 3 57 52 PM" src="https://github.com/user-attachments/assets/79f50106-f5d3-441f-97bc-091721b810e7" />
<img width="1186" alt="Screenshot 2025-04-23 at 3 57 21 PM" src="https://github.com/user-attachments/assets/f8fc5c0c-c740-404c-9b63-1ce267e01238" />

### Tree Operations
<img width="1269" alt="Screenshot 2025-04-23 at 3 56 53 PM" src="https://github.com/user-attachments/assets/b48de359-12c1-47c0-ab0f-9f0c09fee42f" />

### Graph Traversal
<img width="1198" alt="Screenshot 2025-04-23 at 3 55 44 PM" src="https://github.com/user-attachments/assets/388ef2c0-5a84-4661-978a-022f1c5193a1" />

## Team

- Yugandhar Adhik Khair
- Neha Sawant
- Vrushabh Deogirikar
