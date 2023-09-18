// ExchangeFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.ExchangeData
import com.a406.horsebit.R

class ExchangeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExchangeTableAdapter
    private lateinit var exchangeDataList: List<ExchangeData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange, container, false)

        recyclerView = view.findViewById(R.id.rv_ExchangeTable)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // 더미 데이터 생성 (실제 데이터로 대체해야 함)
        exchangeDataList = listOf(
            ExchangeData("체결시간1", "코인명1", "종류1", "거래수량1", "거래단가1", "거래금액1", "수수료1", "정산금액1", "주문시간1"),
            ExchangeData("체결시간2", "코인명2", "종류2", "거래수량2", "거래단가2", "거래금액2", "수수료2", "정산금액2", "주문시간2"),
            ExchangeData("2023-09-15 10:30:00", "Bitcoin", "매수", "2.5 BTC", "$45,000", "$112,500", "$10", "$112,490", "2023-09-15 10:29:55"),
            ExchangeData("2023-09-14 15:20:00", "Ethereum", "매도", "10 ETH", "$3,500", "$35,000", "$5", "$34,995", "2023-09-14 15:19:55"),
            ExchangeData("2023-09-13 09:45:00", "Ripple", "매수", "100 XRP", "$1.25", "$125", "$2", "$123", "2023-09-13 09:44:55"),
            ExchangeData("2023-09-12 14:15:00", "Litecoin", "매도", "5 LTC", "$150", "$750", "$3", "$747", "2023-09-12 14:14:55"),
            ExchangeData("2023-09-11 11:10:00", "Cardano", "매수", "50 ADA", "$2", "$100", "$1", "$99", "2023-09-11 11:09:55"),
            ExchangeData("2023-09-10 16:40:00", "Polkadot", "매도", "20 DOT", "$30", "$600", "$4", "$596", "2023-09-10 16:39:55"),
            ExchangeData("2023-09-09 12:05:00", "Chainlink", "매수", "30 LINK", "$25", "$750", "$5", "$745", "2023-09-09 12:04:55"),
            ExchangeData("2023-09-08 14:50:00", "Stellar", "매도", "200 XLM", "$0.5", "$100", "$1", "$99", "2023-09-08 14:49:55"),
            ExchangeData("2023-09-07 17:55:00", "Tezos", "매수", "15 XTZ", "$6", "$90", "$2", "$88", "2023-09-07 17:54:55"),
            ExchangeData("2023-09-06 11:25:00", "VeChain", "매도", "1000 VET", "$0.1", "$100", "$3", "$97", "2023-09-06 11:24:55")
        )

        adapter = ExchangeTableAdapter(requireActivity(), exchangeDataList)
        recyclerView.adapter = adapter

        return view
    }
}
