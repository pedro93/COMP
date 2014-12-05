## CYKParser

### Group members

Carlos Matias, link: http://sigarra.up.pt/feup/pt/fest_geral.cursos_list?pv_num_unico=201105623

Luís Abreu, link: http://sigarra.up.pt/feup/en/fest_geral.cursos_list?pv_num_unico=201106789

Pavel Alexeenko, link: http://sigarra.up.pt/feup/en/fest_geral.cursos_list?pv_num_unico=201104331

Pedro Silva, link: https://sigarra.up.pt/feup/en/fest_geral.cursos_list?pv_num_unico=201109244


### Summary:

A CYK Parser with the capability of parsing and validating an input string (sentence) using the CYK Algorithm, given a CNF (Chosmky Normal Form).

Our application is capable of doing not only the aforementioned, but also has some added features compared to the traditional version:

- Grammar conversion from CFG to CNF. The program does not need a specific CNF grammar in order to function. The group created a conversion algorithm that accepts a CFG grammar validated by our JavaCC rules:
	- Expression -> StartAttribution+ Attribution* "END"
	- StartAttribution -> "START" ASSIGN STMT (OR STMT)* ENDPROD
	- Attribution -> NONTERM ASSIGN STMT (OR STMT)* ENDPROD
	- STMT -> STMT2+
	- STMT2 -> TERM | STMT3 | EPSILON
	- STMT3 -> NONTERM | "START"
	- ASSIGN -> ":="
	- ENDPROD -> ";"
	- EPSILON -> "epsilon"
	- OR -> "|"
	- NONTERM ->  ["A"-"Z"](["a"-"z","A"-"Z","0"-"9"])*	 
	- TERM ->  ["a"-"z","0"-"9"](["a"-"z","A"-"Z","0"-"9"])*	


###  Graphical environment (created in Java Swing). 

How the application works:
Start the application by clicking the jar file provided. 
When the graphical window appears, the user can select a grammar-related file by choosing the grammars available in the application directory or by choosing to browse the computer file system, 
which maybe be validated by the application or not depending on the file extension. 
A validated grammar has the ".ser" extension while a grammar file to test(and consequently "compiled") needs to have a ".grm" extension. 
If the grammar has the ".grm" extension, the program analyses the grammar, shows the user, the parsing execued and concludes if the grammar is valid or not 
(if it is the program creates a ".ser" grammar file). After this the user enters another window where he must write a sentence, or sequence of characters (with spaces in between), and is then shown, the table processing animation executed by the CYK algorithm. 
The animation concludes by telling the user if his input was accepted by the grammar or not.

### Dealing with syntactical errors:
Before a conversion of a possible CFG into a CNF grammar, a syntactical analysis is executed by the Chosmky-Normal-Form validator which verifies if a given grammar follows the 
format rules of a chomsky normal form grammar. If any discrepancies are found, a warning is given for anomaly detected, and the converter attempts to correct it after having run this 
initial analysis. 

### Lexical analysis:
The lexical analysis is executed by the javacc code and 1 function created by the group (createSymbolTable), during the construction of the intermediate representation of the grammar
being analysed. The function starts by analysing the root node of javacc tree, for each node, the all accessible tokens in the current production are maintained in other to check that 
the following child nodes cant be accessed and consequently to confirm that no tokens are unreachable. 

### Semantical analysis:
After the IR has been created, a function (SemanticAnalysis)  searches the representation's productions right hand side (Non terminal tokens only), to check if they are also found in the
left hand side of the IR. That is to confirm that there are no tokens have no definition.

### Intermediate representation (IRs): 
Our intermediate representation is defined as follows: TreeMap<Structure, List<Vector<Structure>>> SymbolTable = new TreeMap<Structure,List<Vector<Structure>>>
The group chose to use TreeMap so that search a given production would be as efficient as possible O(1), whilst also providing easy and efficient search for a specific left-hand side
pattern. The class Structure mentioned was developed by the group which consists of:

	String name; -> String representation of the token read ("START";"A",etc...).
	Type type; -> The type of token read (TERM/NONTERM/START/EPSILON/NULL).
	int line; -> The line where the token was read.
	int column; -> the column where the token was read.

The key component of the TreeMap was developed to represent as mush as possible the visual aspect of the grammar. That is each key/value in the IR represents a "line" in the grammar.
The key represents the left-hand-side of the grammar line read and the value corresponds to all the token read in the right-hand-side of the grammar.
Each List element corresponds to one line where the key is the same. For example:
X:= A B 
X:= c
would be 2 elements in the list where the key would be "X".
The vector inside each list element corresponds to a series of tokens read as objects of the Structure class. Using the first production in the example above, each element of the vector
(of type Structure) would contain each to the variables shown including their type (in this case NONTERM), the line and column they were read in the file.

For the project given, no LLIR was needed as no code generation was required.

### Overview:

The CYK algorithm has two main ways of being implemented. It can be implemented through a processing table or through dynamic programming. In our software we implemented a processing table. Using the processing table made the algorithm easier to understand but created a challenge in the implementation stage. 

Our employment of the algorithm starts by processing a serialized grammar (".ser") to get the grammar productions. After this the algorithm creates a table where the results of iteration will be stored. In the first iteration the algorithm matches the user input to the productions and creates the first level of the table. The following iterations do not need the user input any longer and are done using the existing filled table rows. 

Video example of how it works: https://www.youtube.com/watch?v=b98Uyj7JHIU

### Task Distribution:

 - Carlos Matias: Implementation of the CYK algorithm. Graphical animation of the algorithm's processing table.
 - Luís Abreu: Grammar conversion from CFG to CNF. Extended grammar to include the epsilon token. Syntax analysis of the CNF.
 - Pavel Alexeenko: Grammar conversion from CFG to CNF. Extended grammar to include the epsilon token. Syntax analysis of the CNF.
 - Pedro Silva: File reading. Semantic, syntactical and lexical analysis of the CFG. Implementation of the intermediate representation. Definition of the JavaCC rules (expressed above). Development of the GUI. 

###Copyright
The project, including all files in this repository are protected under the GPL licence, for more information please view the LICENSE file.
