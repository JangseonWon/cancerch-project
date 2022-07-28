package com.gcgenome.lims.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "index_sequence", indexes = [
    Index(columnList = "type, sequence", unique = true),
    Index(columnList = "type, plate, position", unique = true),
    Index(columnList = "plate")
])
class IndexSequence {
    @EmbeddedId val pk : IndexSequencePK = IndexSequencePK()
    @Column(name = "sequence", length = 10) val sequence: String = ""
    @Column(name = "plate", length = 8) val plate: String = ""
    @Column(name = "position", length = 3) val position: String = ""

    @Embeddable
    data class IndexSequencePK (
        @Column(name="type", length=2, nullable = false, updatable = false)
        var type: String = "",
        @Column(name = "id", length=8, nullable = false, updatable = false)
        private var id: String = ""
    ): Serializable
}