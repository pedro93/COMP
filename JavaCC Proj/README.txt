**PROJECT TITLE: #18 cykparser

**GROUP: T01G05

(Names, numbers, self assessment, and contribution of the members of the group according to:)

NAME1: Carlos Matias, NR1: 201105623, GRADE1: 20, CONTRIBUTION: 25%.

NAME2: Luís Abreu, NR2: 201106789, GRADE2: 20, CONTRIBUTION: 25%.

NAME3: Pavel Alexeenko, NR3: 201104331, GRADE3: 20, CONTRIBUTION: 25%.

NAME4: Pedro Silva, NR4: 201109244, GRADE4: 20, CONTRIBUTION 25%.


** SUMMARY: (Describe what your tool does and its main features.)

A CYK Parser with the capability of parsing and validating an input string (sentence) using the CYK Algorithm, given a CNF (Chosmky Normal Form).

Our application is capable of doing not only the aforementioned, but also has some added features compared to the traditional version:

- Grammar conversion from CFG to CNF. The program does not need a specific CNF grammar in order to function. The group created a conversion algorithm that accepts a CFG grammar validated by our JavaCC rules:
	> Expression -> StartAttribution+ Attribution* "END"
	> StartAttribution -> "START" ASSIGN STMT (OR STMT)* ENDPROD
	> Attribution -> NONTERM ASSIGN STMT (OR STMT)* ENDPROD
	> STMT -> STMT2+
	> STMT2 -> TERM | STMT3 | EPSILON
	> STMT3 -> NONTERM | "START"
	> ASSIGN -> ":="
	> ENDPROD -> ";"
	> EPSILON -> "epsilon"
	> OR -> "|"
	> NONTERM ->  ["A"-"Z"](["a"-"z","A"-"Z","0"-"9"])*	 
	> TERM ->  ["a"-"z","0"-"9"](["a"-"z","A"-"Z","0"-"9"])*	

- Graphic envirnoment (created in Java Swing). 

How the application works:
Start the application by clicking the jar file provided. When the graphical window appears, the user can select a grammar-related file by choosing the grammars available in the application directory or by choosing to browse the computer file system, which maybe be validated by the application or not depending on the file extension. A validated grammar has the ".ser" extension while a grammar file to test(and consequently "compiled") needs to have a ".grm" extension. If the grammar has the ".grm" extension, the program analyses the grammar, shows the user, the parsing execued and concludes if the grammar is valid or not (if it is the program creates a ".ser" grammar file). After this the user enters another window where he must write a sentence, or sequence of characters (with spaces in between), and is then shown, the table processing animation executed by the CYK algorithm. The animation concludes by telling the user if his input was accepted by the grammar or not.

**DEALING WITH SYNTACTIC ERRORS: (Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)

**SEMANTIC ANALYSIS: (Refer the possible semantic rules implemented by your tool.)

**INTERMEDIATE REPRESENTATIONS (IRs): (describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)

**CODE GENERATION:

For our application Code Generation was not a requirement.

**OVERVIEW: (refer the approach used in your tool, the main algorithms, etc.)

The CYK algorithm has two main ways of being implemented. It can be implemented through a processing table or through dynamic programming. In our software we implemented a processing table. Using the processing table made the algorithm easier to understand but created a challenge in the implementation stage. 

Our employment of the algorithm starts by processing a serialized grammar (".ser") to get the grammar productions. After this the algorithm creates a table where the results of iteration will be stored. In the first iteration the algorithm matches the user input to the productions and creates the first level of the table. The following iterations do not need the user input any longer and are done using the existing filled table rows. 


**TESTSUITE AND TEST INFRASTRUCTURE: (Describe the content of your testsuite regarding the number of examples, the approach to automate the test, etc.)

**TASK DISTRIBUTION: 

> Carlos Matias: Implementation of the CYK algorithm. Graphical animation of the algorithm's processing table.
> Luís Abreu: Grammar conversion from CFG to CNF. Extended grammar to include the epsilon token. Syntax analysis of the CNF.
> Pavel Alexeenko: Grammar conversion from CFG to CNF. Extended grammar to include the epsilon token. Syntax analysis of the CNF.
> Pedro Silva: File reading. Semantical, syntactical and lexical analysis of the CFG. Implementation of the intermediate representation. Definition of the JavaCC rules (expressed above). Development of the GUI. 

**PROS: (Identify the most positive aspects of your tool)

Graphical envrionment to facilitate the user interaction with the application.
Extension of the accepted grammars, broadening the user possibilities of input.

**CONS: (Identify the most negative aspects of your tool)

Limitations of the JavaCC accepted tokens. The further extension of the tokens would difficult the grammar conversion.
For grammars which allow a significant amount of epislon productions (above 6), the developed program may become slow to parse and convert ε-CFG to CNF.
