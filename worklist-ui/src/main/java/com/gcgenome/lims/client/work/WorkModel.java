package com.gcgenome.lims.client.work;

public enum WorkModel {
    ConcNa(     "conc_na",          "Na Conc(pg/ul)"),
    ConcInput(  "conc_input",       "input Conc(ng)"),
    LibPrep(    "lib_prep",         "Library Prep"),
    ConcTape(   "conc_tapestation", "Lib conc(ng/ul) Tapestation"),
    ConcQubit(  "conc_qubit",       "Lib conc(ng/ul) Qubit"),
    FragSize(   "frag_size",        "fragment size (bp)"),
    NM(         "nm",               "convert to nM"),
    Dilution(   "dilution",         "nM of dilution"),
    Volume(     "volume",           "Total Vol(ul)"),
    LibraryVolume("library_volume", "Library volume(ul)"),
    TEBuffer(   "buffer",           "TE buffer (ul)"),
    QC(         "result",           "QC"),
    Address(    "address",          "Position"),
    IndexI7(    "i7Index",          "I7 Index ID"),
    SequenceI7( "i7Seq",            "I7 Sequence"),
    IndexI5(    "i5Index",          "I5 Index ID"),
    SequenceI5( "i5Seq",            "I5 Sequence");
    final String id;
    final String label;
    WorkModel(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
