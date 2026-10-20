# thread_cards
Threading my cards since 2026

## Running the cards.jar file
1. Download the cards.jar file
2. Ensure that your Java installation has read/write permissions for the directory that cards.jar is in 
3. Run the command 'java -jar cards.jar'
4. Enter the number of players in the game
5. Enter the complete file path to the pack file (n.b. partial/relative file paths may/may not work depending on your environment and OS)
6. The game will run and produce the required output files (n.b it will run indefinitely if there is no winning hand in the pack, so may need to be interrupted)

## Viewing the contents of the cards.jar file
1. Download the cards.jar file
2. Run the command 'jar xf cards.jar'
3. The cards.jar file will be extracted to all the bytecode (.class) files and source code (.java) files.

## Compilation and running of tests
1. Download JUnit4.13.2
2. Download source code
3. Run 'compile_tests.bat' or command "javac -cp lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;bin;src/thread_cards -d bin src/thread_cards/*.java"
4. Run the tests via 'run_tests.bat' or command "java -cp lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar:src/thread_cards/:bin/src/thread_cards RunTestSuite"

## Dependencies
- JUnit4.13.2
- HamcrestCore 1.3
- Java 25.0.1+


NOTES FOR README:
JUnit4.13.2
Pack file must be given as a full path
test_log_files, log_files must exist next to the jar file
test_pack_files must exist next to the jar file