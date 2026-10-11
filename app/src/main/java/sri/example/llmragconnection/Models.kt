package sri.example.llmragconnection

data class SearchRequest(
    val query: String
)

data class SearchResult(
    val text: String,
    val distance: Double
)

data class SearchResponse(
    val query: String,
    val results: List<SearchResult>
)