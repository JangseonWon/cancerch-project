package com.gcgenome.lims.client.work;

public enum WorkModel {
    ConcTape("conc_tapestation", "Lib conc(ng/ul) Tapestation"),
    ConcQubit("conc_qubit", "Lib conc(ng/ul) Qubit"),
    FragSize("frag_size", "fragment size (bp)"),
    NM("nm", "convert to nM"),
    Assuming("assuming", "Assuming a Mr"),
    Dilution("dilution", "nM of dilution"),
    Volume("volume", "Total Vol(ul)"),
    LibraryVolume("library_volume", "Library volume(ul)"),
    TEBuffer("buffer", "TE buffer (ul)");
    final String id;
    final String label;
    WorkModel(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
