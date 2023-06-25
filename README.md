# museumHeist

This repository contains all work related to the practical component of the Distributed Systems Course taken during the year 2022/2023

The starting point of all 3 assignments is described in the file named *enunciado.pdf* with each assignment requiring three specific implementations of the project description:

The objectives of the assignments was to implement the solutions in the following ways:

- First assigment was to implement the simulation using a local, multi-threaded solution;
- Second assigment required us to migrate the first solution from a local environment to a distributed environment based on the message passing paradigm;
- Third assignment required us to migrate the first solution from a local environment to a distributed environment based on the remote object paradigm;

Each solution is contained within its own branch:

- the *main* branch contains the multi-threaded implementation;
- the *MessagePassing* branch contains the distributed solution based on message passing;
- the *RMISolution* branch contains the distributed solution using RMI;

All solutions were implemented in Java using whatever standard libraries were needed for each situation as well as 2 extra libraries provided to us by our professor: *genclass* and *ClientCom*.
The former being a library meant to make printing to the terminal easier and the latter used for the communication infrastructure required to implement the message passing solution for the second assigment.
