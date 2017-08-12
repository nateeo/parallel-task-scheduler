# 306-scheduling

[![Build Status](https://travis-ci.com/Nateeo/306-scheduling.svg?token=8jyemjiGm66sspKKLBKp&branch=master)](https://travis-ci.com/Nateeo/306-scheduling)

Parallel processor task scheduling application with visualisation.

## Team Hi-5

- Edison Rho   (@OurEdds)
- Eli Salter   (@elisalter)
- Samule Li    (@sli473)
- Zihao Yang   (@zihao123yang)
- Sueyeon Lee  (@sy133)
- Nathan Hur   (@Nateeo)

This project uses Java and Maven.

**Command-line usage**

`java -jar scheduler.jar INPUT.dot P [OPTION]`

- `INPUT.dot` .dot file representing the task graph
- `P` number of processors to schedule on
- `[OPTION]`
    * `-v` visualise search
    * `-o` specify output file (default is `INPUT-output.dot`)

**Building from source**

execute the maven goal: `clean compile assembly:single`

**IntelliJ setup:**

1. Import from VCS

2. `IntelliJ IDEA -> Preferences -> Build, Execution, Deployment -> Build Tools -> Maven -> Importing -> Import Maven projects automatically (tick)` This downloads dependencies automatically

3. `View -> Tool Windows -> Maven Projects -> Execute Maven Goal` (box with a lowercase m)

4. Run Maven target 'install'

5. Build will be in `/target` folder
