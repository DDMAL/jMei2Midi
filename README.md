jMei2Midi Description:

MeiStatTrack Description:

API
===

	MeiSequence sampleMeiSequence = new MeiSequence(filename);
	Sequence javaSequence = sampleMeiSequence.getSequence();

Notes: Constructor can be given a String or a File object.
       Also a stat tracker can be given as a second parameter for
       multiple files and if not given one is instantiated automatically.

	//To get the MeiStatTracker object
	MeiStatTracker stats = sampleMeiSequence.getStats();
	
	//To get a List<> of all the file names this has seen
	List<String> allFileNames = stats.getAllFiles();

	//To get a Map of all the incorrect files this has seen
	//key : file name, value : list of incorrect elements for specified file
	HashMap<String,List<String>> = stats.getIncorrectFiles();

	//To get a Map of all invalid instruments
	//key : file name, value : list of invalid instruments for specified file
	HashMap<String,List<String>> = stats.getInvalidInstruments(); 
	
	//To get Map of all invalid tempos
	//key : file name, value : list of invalid tempos for specified file
	HashMap<String,List<String>> = stats.getInvalidTempos();

Commmand Line Interface
=======================

	java -jar jMei2Midi-1.0-jar-with-dependencies.jar filenamein filenameout

filenamein/filenameout options:
1. If filenamein is a valid file, then a file will be written out.
2. If both filenamein and filenameout are directories, then files will be converted from in to out.
3. Otherwise, an error will be logged and printed to the screen.

Notes: JAR build is required before running through cmd line.
       Filenamein and filenameout can be both directories or files
       but both must be of the same type.

Development
===========

For further development, it would be highly recommended to first read through and understand the MeiSequence class.
One will notice that any newly implemented MEI element can be done so in 1 of 3 ways.

1. If the MEI element is a general element that affects the recursive XML stack, 
   then it should be added to the meielements.general package and the class should extend MeiGeneral.
2. If the MEI element is involved with the staff information (i.e. key, instrument, mode...),
   then it should be added to the meielement.staffinfo package and the class may or may not extend MeiStaffBuilder.
   It is very likely that the staffinfo package can be simply updated to deal with minor changes.
3. If the MEI element is a child of the MEI layer element and/or involved with partial/direct midi information,
   then it should be added to the meielements.layerchild package and the class should extend LayerChild.
