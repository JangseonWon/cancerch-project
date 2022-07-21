package com.gcgenome.lims.service.worklist

import com.gcgenome.lims.entity.QWorklist.worklist
import com.gcgenome.lims.entity.Worklist
import com.gcgenome.lims.service.Searchable
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.sql.RelationalPathBase
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class WorklistDao(private val repo: WorklistRepository): Searchable<Worklist>(repo) {
   override fun table(): RelationalPathBase<Worklist> = worklist
   override fun pk(): ComparablePath<*> = worklist.id
   override fun column(key: String): ComparableExpression<*> {
      return when {
         key.trim().isEmpty()                -> worklist.createAt
         "작성일".contentEquals(key)         -> worklist.createAt
         "상태".contentEquals(key)           -> worklist.status
         "워크리스트 명".contentEquals(key)  -> worklist.title
         else                                -> worklist.createAt
      }
   }

   override fun predicate(key: String?, value: String?): Predicate? {
      return when {
         key == null || key.trim().isEmpty() -> {
            val predicates = listOfNotNull(
               predicate("title", value),
               predicate("id", value),
               predicate("status", value),
               predicate("remark", value)
            )
            BooleanBuilder().andAnyOf(*predicates.toTypedArray())
         }
         "id".contentEquals(key, ignoreCase = true)      -> return if(value!=null) parseToUUID(value) else null
         "title".contentEquals(key, ignoreCase = true)   -> return if(value!=null) worklist.title.likeIgnoreCase("%$value%") else null
         "status".contentEquals(key, ignoreCase = true)  -> return if(value!=null) worklist.status.stringValue().likeIgnoreCase("%$value%").or(
            worklist.status.stringValue().likeIgnoreCase("%${statusMap[value]}%")) else null
         "remark".contentEquals(key, ignoreCase = true)  -> return if(value!=null) worklist.remark.likeIgnoreCase("%$value%") else null
         "domain".contentEquals(key, ignoreCase = true)  -> return if(value!=null) worklist.domain.eq(value) else null
         "to".contentEquals(key, ignoreCase = true)      -> return if(value!=null){
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.systemDefault())
            return worklist.createAt.loe(date)
         } else null
         "from".contentEquals(key, ignoreCase = true)      -> return if(value!=null){
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.systemDefault())
            return worklist.createAt.goe(date)
         } else null
         "confirmed".contentEquals(key, ignoreCase = true) -> return if(value!=null) worklist.status.stringValue().eq("NORMAL") else null
         else -> null
      }
   }
   private fun parseToUUID(value: String): Predicate? {
      try{
         return worklist.id.eq(UUID.fromString(value))
      }
      catch (_ : IllegalArgumentException){}
      return null
   }
   companion object {
      val statusMap : Map<String, String> = mapOf(
         "검증" to "Normal",
         "Plate 검증" to "Normal",
         "Plate 검증 완료" to "Normal"
      )
   }

}