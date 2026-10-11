package sri.example.llmragconnection

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etQuestion: android.widget.EditText
    private lateinit var btnAsk: android.widget.Button
    private lateinit var progressBar: android.widget.ProgressBar
    private lateinit var tvRagAnswer: android.widget.TextView
    private lateinit var tvContext: android.widget.TextView
    private lateinit var tvStatus: android.widget.TextView
    private lateinit var tvDirectAnswer: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etQuestion = findViewById(R.id.etQuestion)
        btnAsk = findViewById(R.id.btnAsk)
        progressBar = findViewById(R.id.progressBar)
        tvRagAnswer = findViewById(R.id.tvRagAnswer)
        tvContext = findViewById(R.id.tvContext)
        tvStatus = findViewById(R.id.tvStatus)
        tvDirectAnswer = findViewById(R.id.tvDirectAnswer)

        btnAsk.setOnClickListener {
            searchDocument()
        }
    }

    private fun searchDocument() {

        val question = etQuestion.text.toString().trim()

        if (question.isEmpty()) {
            Toast.makeText(this, "Enter a question", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        tvStatus.text = "Status: Connecting to backend..."

        val request = SearchRequest(question)

        RetrofitClient.api.searchDocument(request)
            .enqueue(object : Callback<SearchResponse> {

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {

                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {

                        val data = response.body()!!

                        if (data.results.isNotEmpty()) {

                            val context = data.results.joinToString("\n\n") {
                                it.text
                            }

                            tvContext.text = context

                            tvRagAnswer.text =
                                "Retrieved information successfully. " +
                                        "LLM answer will be generated from this context."

                            tvDirectAnswer.text =
                                "Direct LLM answer will be added in the next step."

                            tvStatus.text = "Status: Backend connected successfully"

                        } else {
                            tvContext.text = "No relevant documents found."
                            tvStatus.text = "Status: No results"
                        }

                    } else {
                        tvStatus.text =
                            "Status: Backend error ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                    progressBar.visibility = View.GONE

                    tvStatus.text =
                        "Status: Connection failed"

                    Toast.makeText(
                        this@MainActivity,
                        "Backend connection failed: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}