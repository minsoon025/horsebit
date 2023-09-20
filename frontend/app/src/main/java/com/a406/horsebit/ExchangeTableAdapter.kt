import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.ExchangeData
import com.a406.horsebit.R

class ExchangeTableAdapter(private val context: Context, private val exchangeDataList: List<ExchangeData>) :
    RecyclerView.Adapter<ExchangeTableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.exchange_item, parent, false)
        return ViewHolder(view)
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
    }
}
