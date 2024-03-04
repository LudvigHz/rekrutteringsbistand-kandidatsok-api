package no.nav.toi.kandidatsøk.filter

import no.nav.toi.*
import no.nav.toi.kandidatsøk.FilterParametre
import org.opensearch.client.opensearch._types.query_dsl.Operator

fun List<Filter>.medArbeidsønskefilter() = this + Arbeidsønskefilter()

private class Arbeidsønskefilter: Filter {
    private var arbeidsønske = emptyList<String>()
    override fun berikMedParameter(filterParametre: FilterParametre) {
        arbeidsønske = filterParametre.ønsketYrke ?: emptyList()
    }

    override fun erAktiv() = arbeidsønske.isNotEmpty()

    override fun lagESFilterFunksjon(): FilterFunksjon = {
        must_ {
            bool_ {
                apply {
                    arbeidsønske.forEach { ønsketYrke ->
                        should_ {
                            match_ {
                                field("yrkeJobbonskerObj.styrkBeskrivelse")
                                fuzziness("0")
                                operator(Operator.And)
                                query(ønsketYrke)
                            }
                        }
                        should_ {
                            match_ {
                                field("yrkeJobbonskerObj.sokeTitler")
                                fuzziness("0")
                                operator(Operator.And)
                                query(ønsketYrke)
                            }
                        }
                    }
                }
            }
        }
    }
}