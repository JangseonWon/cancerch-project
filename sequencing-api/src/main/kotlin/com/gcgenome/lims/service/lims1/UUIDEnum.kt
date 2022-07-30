package com.gcgenome.lims.service.lims1

import java.util.*

enum class UUIDEnum(val uuid: String) {
    TEMPLATE("114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2"),
    DEVICE("27fe2fed-f688-4400-a1f7-81c3a56af5b1"),
    COMPLETE_AT("051a640e-8a18-440e-8a3c-40eebaec64e3"),
    TOTAL_YIELD("3b863808-785a-4397-9c3a-77ee84f079a1"),
    CLUSTER_DENSITY("67fca06f-5676-40f9-8d42-de4a87822699"),
    Q30("aea0883c-2a91-421f-a26f-f7dc2934383b"),
    CLUSTER_PF("67fca06f-5676-40f9-8d42-de4a87822699"),
    ERROR_RATE("6a67648f-1dc6-4998-bed3-3468513f11d7"),
    PATH("637c9d46-82ec-4bf4-93ed-b490258e23fc"),
    PROGRESS("f39abbed-f4bd-4e01-af7a-3b75e2e0c200"),
    READ1("0c8eaf97-62a7-4de8-84e3-caa902ca4e76"),
    READ2("5e478ae9-50f8-4482-ba04-02d14952cc01"),
    TOTAL_CYCLE("5e478ae9-50f8-4482-ba04-02d14952cc01"),
    INDEX1("3fffff88-05ee-4792-a931-779c523f7526"),
    INDEX2("fc312d65-e14a-475b-a04a-dcbdb66e60e0"),
    MISMATCH("54a46cec-08f8-46eb-9412-69728e59e166"),
    RC("3bae5a4f-24e6-48a4-9f48-4c89e6b25936"),
    FPD("753b2790-d1e7-4d33-aa93-3eaaa99ed5a8"),
    SAMPLE_SHEET("384cc76e-3e35-4fbc-b8ff-7b73c0345812"),
    ADAPTOR1("c88233ed-c724-41b7-9f8e-9fadf01b031a"),
    ADAPTOR2("eb8829dd-97fb-4edd-a40e-88fa3a05c698"),
    RUNNING_TIME("dcde8d1b-e1f2-4791-9908-5db821ff6d5c"),
    RUN_ID("64740ccd-659a-4a62-8992-358cb07792da"),
    TOTAL_READ("ff8ff409-37c3-4713-bca3-77740353d843"),
    UNDETERMINED_RATIO("890d46d6-4555-43c6-8b9a-a0892039c65a"),
    UNDETERMINED_INDEX("441bef2c-5741-4864-b030-1c6c37d9a54d"),
    ETC("dc404f5b-621b-4775-94aa-bfd32e97e472"),
    WORKFLOW("31c18e1d-2b37-431f-9e88-0cd59d964373"),
    APPLICATION("dcf8f21f-231a-496e-a8da-f748a24c951c"),
    ASSAY("3f2f87ab-0e8c-4ce6-bc5b-9a0f3f72fd97"),
    CHEMISTRY("36f35add-6bfe-43ed-bca2-5a2d1bc87556"),
    MANIFESTS("dcde8d1b-e1f2-4791-9908-5db821ff6d5c");

    fun toUUID() : UUID{
        return UUID.fromString(uuid)
    }
}
