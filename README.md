# 306-scheduling

[![Build Status](https://travis-ci.com/Nateeo/306-scheduling.svg?token=8jyemjiGm66sspKKLBKp&branch=master)](https://travis-ci.com/Nateeo/306-scheduling)

Parallel processor task scheduling application with visualisation, built with Java and [ParallelTask](http://parallel.auckland.ac.nz/ParallelIT/PT_About.html).

## Command-line usage

`java -jar scheduler.jar INPUT.dot P [OPTION]`

- `INPUT.dot` .dot file representing the task graph
- `P` number of processors to schedule on
- `[OPTION]`
    * `-v` visualise search
    * `-o` specify output file (default is `INPUT-output.dot`)

## Building from source

execute the maven goal: `clean compile assembly:single`

## Final release with source code and jar download

[Final release tag](https://github.com/Nateeo/306-scheduling/releases/tag/FINAL)

## Documentation, implementation decisions

[Hi-5 Team Wiki](https://github.com/Nateeo/306-scheduling/wiki)

## Planning, timeline, issue tracking

[https://trello.com/b/T6QTKZpC/hi-5](https://trello.com/b/T6QTKZpC/hi-5)


## Team Hi-5

- Edison Rho   **(@OurEdds)**
- Eli Salter   **(@elisalter)**
- Samule Li    **(@sli473)**
- Zihao Yang   **(@zihao123yang)**
- Sueyeon Lee  **(@sy133)**
- Nathan Hur   **(@Nateeo)** (Team Leader)
