# miniCsharp

## Simple sintax analizer implemented in java using the Jflex library
### Project for the Compiler course of Rafael Landivar´s University
#### by David Munguia

##### How to use

1. Run the miniCsharp.jar file stored in the dist folder of this project
2. Set the path of a .frag file that contain the code that is going to be analized
3. The .out file is going to be created with the same path and the same name of the .frag file
4. If the .frag file contain a lexical error the program should let you know

#### Tokens
This lexical analyzer recognizes this list of tokens:

1. IDENTIFIER
  A 31 character length identifier
2. INTEGER
  Normal integer with hex support
3. DOUBLE
  Normal double wit hex support
4. RESERVERDWORDS
   1.  void
   2.  int
   3.  double
   4.  class
   5.  bool
   6.  string
   7.  interface
   8.  null
   9.  extends
   10. implements
   11. for
   12. while
   13. if
   14. else
   15. return
   16. break
   17. New
   18. NewArray
5. OPERATOR
  1.  +
  2.  -
  3.  *
  4.  /
  5.  %
  6.  <
  7.  <=
  8.  >
  9.  >=
  10. =
  11. ==
  12. !=
  13. &&
  14. ||
  15. !
  16. ;
  17. ,
  18. .
  19. []
  20. ()
  21. {}
  22. [
  23. ]
  24. {
  25. }
  26. (
  27. )
6. BOOLEAN
7. STRING
8. NORMALCOMMENT
9. MULTILINECOMMENT

