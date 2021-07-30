package com.elibouassaba.quotes.quote.dto

import javax.validation.constraints.NotNull

data class EditQuoteDto(
    var content: @NotNull String? = null
)