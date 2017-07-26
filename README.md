# 306-scheduling
This project uses Maven.

**IntelliJ setup:**

1. Import from VCS

2. `IntelliJ IDEA -> Preferences -> Build, Execution, Deployment -> Build Tools -> Maven -> Importing -> Import Maven projects automatically (tick)` This downloads dependencies automatically

3. `View -> Tool Windows -> Maven Projects -> Execute Maven Goal` (box with a lowercase m)

4. Run Maven target 'install'

5. Build will be in `/target` folder

## package structure
starts from **src/main/java**

#### package: scheduler
- **Scheduler** class: contains entry-point to scheduling algorithm
