**PROJECT TITLE: #18 cykparser

**GROUP: T01G05

(Names, numbers, self assessment, and contribution of the members of the group according to:)

NAME1: Carlos Matias, NR1: 201105623, GRADE1: 20, CONTRIBUTION: 25%.

NAME2: Luís Abreu, NR2: 201106789, GRADE2: 20, CONTRIBUTION: 25%.

NAME3: Pavel Alexeenko, NR3: 201104331, GRADE3: 20, CONTRIBUTION: 25%.

NAME4: Pedro Silva, NR4: 201109244, GRADE4: 20, CONTRIBUTION 25%.


** SUMMARY: (Describe what your tool does and its main features.)

A CYK Parser has the capability of parsing a input string (sentence), using a CFG (Context Free Grammar) in the CNF (Chosmky Normal Form), running the CYK Algorithm. 

Our application is capable of doing this and has some added features to the traditional implementation:
- Grammar conversion from CFG to CNF. The program does not need a specific CNF grammar in order to function. The group created a conversion algorithm that accepts a CFG grammar validated by our JavaCC rules:
	> There must a START rule. At least One.
	> The file must end with an "END" tag.
	> Each rule must follow the NON-TERM := 
	Besides this, our validator accepts grammar with symbols, numbers and the ε character.
- Graphic envirnoment (created in Java Swing) where the user can select a grammar, which can be validated by the application or not. If it is not the program analyses the grammar, shows the user the parsing made and concludes if the grammar is valid or not. After this the user enters another window where he must write a sentence, or sequence of characters (with spaces inbetween), and he then watches the table processing the algorithm does.

**DEALING WITH SYNTACTIC ERRORS: (Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)

**SEMANTIC ANALYSIS: (Refer the possible semantic rules implemented by your tool.)

**INTERMEDIATE REPRESENTATIONS (IRs): (describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)

**CODE GENERATION: (Describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)

For our application Code Generation was not a requirement.

**OVERVIEW: (refer the approach used in your tool, the main algorithms, etc.)

**TESTSUITE AND TEST INFRASTRUCTURE: (Describe the content of your testsuite regarding the number of examples, the approach to automate the test, etc.)

**TASK DISTRIBUTION: (Identify the set of tasks done by each member of the project.)

**PROS: (Identify the most positive aspects of your tool)

**CONS: (Identify the most negative aspects of your tool)
