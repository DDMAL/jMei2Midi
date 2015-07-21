API
===

	MeiSequence sampleMeiSequence = new MeiSequence(filename);
	Sequence javaSequence = sampleMeiSequence.getSequence();

Notes: Constructor can be given a String or a File object.
       Also a stat tracker can be given as a second parameter for
       multiple files and if not given one is instantiated automatically.
		MeiStatTracker stats = sampleMeiSequence.getStats();

Commmand Line Interface
=======================

	java -jar jMei2Midi-1.0-jar-with-dependencies.jar filenamein filenameout

Notes: JAR build is required before running through cmd line.
       Filenamein and filenameout can be both directories or files
       but both must be of the same type.
