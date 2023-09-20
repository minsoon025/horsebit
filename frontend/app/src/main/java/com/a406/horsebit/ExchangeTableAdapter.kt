import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.ExchangeData
import com.a406.horsebit.R

class ExchangeTableAdapter(private val context: Context, private val exchangeDataList: List<ExchangeData>) :
    RecyclerView.Adapter<ExchangeTableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // HorizontalScrollView로 감싸진 아이템 레이아웃을 생성합니다.
        val scrollView = HorizontalScrollView(context)
        val itemLayout = LinearLayout(context)
        itemLayout.orientation = LinearLayout.HORIZONTAL
        val view =
            LayoutInflater.from(context).inflate(R.layout.exchange_item, itemLayout, false)
        itemLayout.addView(view)
        scrollView.addView(itemLayout)
        return ViewHolder(scrollView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exchangeData = exchangeDataList[position]

        holder.tvOrderTime.text = exchangeData.orderTime
        holder.tvCoinName.text = exchangeData.coinName
        holder.tvType.text = exchangeData.type
        holder.tvSeep.text = exchangeData.seep
        holder.tvOne.text = exchangeData.one
        holder.tvMoney.text = exchangeData.money
        holder.tvFee.text = exchangeData.fee
        holder.tvRealMoney.text = exchangeData.realMoney
        holder.tvOrderTime2.text = exchangeData.orderTime2
    }

    override fun getItemCount(): Int {
        return exchangeDataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderTime: TextView = itemView.findViewById(R.id.tv_iExOrderTime)
        val tvCoinName: TextView = itemView.findViewById(R.id.tv_iExCoin)
        val tvType: TextView = itemView.findViewById(R.id.tv_iExType)
        val tvSeep: TextView = itemView.findViewById(R.id.tv_iExSeep)
        val tvOne: TextView = itemView.findViewById(R.id.tv_iExOne)
        val tvMoney: TextView = itemView.findViewById(R.id.tv_IExMoney)
        val tvFee: TextView = itemView.findViewById(R.id.tv_iExFee)
        val tvRealMoney: TextView = itemView.findViewById(R.id.tv_iExRealMoney)
        val tvOrderTime2: TextView = itemView.findViewById(R.id.tv_iExOrderTime2)

        init {
            // HorizontalScrollView가 스크롤되면 모든 아이템이 함께 스크롤되도록 설정합니다.
            (itemView as HorizontalScrollView).setOnScrollChangeListener { _, scrollX, _, _, _ ->
                for (i in 0 until itemCount) {
                    val item = (itemView as HorizontalScrollView).getChildAt(0) as LinearLayout
                    val textView = item.getChildAt(i) as TextView
                    textView.translationX = -scrollX.toFloat()
                }
            }
        }
    }
}
