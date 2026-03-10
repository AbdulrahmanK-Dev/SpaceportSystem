 # 🚀 Spaceport Management System
The Spaceport Management System is an object-oriented Java application designed to manage a commercial spaceport facility.
The system handles rocket operations, trip scheduling, passenger bookings, maintenance operations, and personnel management through a structured class hierarchy and database integration. I mainly built it for learning Purposes and Pure Interest.
---


:box: ## Technologies

- Java
- Object-Oriented Programming
- UML Design
- Database Integration

--
 ## Features

- 🚀 Trip scheduling and ticket booking
- 👨‍🚀 Employee management (Administrators, Pilots, Technicians)
- 🛰 Launch pad management
- 🧭 Navigation system integration
- 🔧 Rocket maintenance management

---
## Object-Oriented Design

The system demonstrates several core OOP principles:

- **Inheritance** – shared behavior across system classes
- **Encapsulation** – controlled access to internal state
- **Composition** – Spaceport contains Location and NavigationSystem
- **Aggregation** – MaintenanceTeam manages multiple Technicians
- **Runtime Polymorphism** – method calls resolved based on object type
 ---

 ## System Roles

The system supports four main user roles:

### Passenger
- Browse available trips
- Book tickets
- Choose class tier (Economy, Business, First)

### Administrator
- Schedule trips
- Manage rocket status
- View passenger manifests
- Execute launches
- Register rockets

### Pilot
- Start rocket engine
- Estimate fuel consumption
- Adjust throttle
- Execute launches

### Technician
- Maintain rockets
- Manage inventory parts
- Assign rockets for service
- Hire technicians
--

## UML Diagram
I couldn't make it any more readable than this. Sorry for your eyes.



![SpaceportUML](https://github.com/user-attachments/assets/62dc9afb-46b6-44de-905b-017015ac50e6)
