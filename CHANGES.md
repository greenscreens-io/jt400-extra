## *Version 1.2.0* (17.08.2020)

### Error Formats
 - Added ERRC0100 - program output error format
 - Added ERRC0200 - program output error format

### JT400Program
 - Added timeout option
 - Added threadSafe flag
 - Added procedureName - ServiceCall specific
 - Added returnType - ServiceCall specific 

### JT400Parameter
 - Added type support (by value or by reference flag)
 
### DynamicProxy
 - Added ServiceCall support
 - Added (int) retVal support (must be set on JT400Parameter class)
 - Added non-exception return, ERROR output parameter support

### Parameter Parser
 - Added Program Output parameters add other data formats; not only byte buffers
 - Fix - process all @Output, not only first one
 
### Format Parser 
 - Added @Id to format indicating output field data relation
 - Added child format structure in non-arrays
 - Added relative format class offsets
 - Added format inheritance support
 
### Examples
 - Added ServiceProgramCall examples (QTOCNETSTS, QZCACLT)
 - Added Format with relative offsets (TCPA0200) 
 - Improved toString to print subformats
 
## *Version 1.1.0*  (14.08.2020)

Most of the changes in this version relates to response byte parser to output data format class

### Fix

 - Format converter super class fields inclusion added
 - Format name set in program call parameters

### New

 - Method name change in JT400ExtFactory to follow default naming
 - Improved data converter for output format class
 - Added JT400ExtBinaryConverter class
 - Added AS400Date convert for output format class
 - Added AS400Time convert for output format class
 - Added AS400Timestap convert for output format class
 - Added data array support for output format class
 - Added field child formats deep parse for output format class
 - Added offset and length reference field support for array data
 - Added test demo dat for DEVD1100 to test arrays and subformat structures

## *Version 1.0.0*  (10.08.2020)

Initial release.
Structured parameters and parameter arrays are not supported yet.