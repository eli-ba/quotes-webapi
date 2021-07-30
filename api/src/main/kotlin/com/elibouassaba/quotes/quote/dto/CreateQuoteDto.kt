package com.elibouassaba.quotes.quote.dto

import javax.validation.constraints.NotNull

data class CreateQuoteDto(
    @NotNull
    var content: String? = null
)