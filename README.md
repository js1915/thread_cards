# thread_cards
Threading my cards since 2026

## Compilation of Tests
1. Download JUnit4.13.2
2. Download source code
3. Run 'compile_tests.bat' or command "javac -cp lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;bin;src/thread_cards -d bin src/thread_cards/*.java"
4. Run the tests via 'run_tests.bat' or command "java -cp lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar:src/thread_cards/:bin/src/thread_cards RunTestSuite"


NOTES FOR README:
JUnit4.13.2
Pack file must be given as a full path
test_log_files, log_files must exist next to the jar file
test_pack_files must exist next to the jar file