package com.example.yandexmaptest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yandexmaptest.databinding.ActivityAddressesBinding
import com.example.yandexmaptest.source.AddressData
import com.example.yandexmaptest.ui.adapters.AddressAdapter
import com.example.yandexmaptest.utils.MyAddress
import com.yandex.mapkit.geometry.Point

class AddressesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressesBinding
    private lateinit var from: AddressData
    private lateinit var to: AddressData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvAddresses.apply {
            layoutManager = LinearLayoutManager(this@AddressesActivity, LinearLayoutManager.VERTICAL, false)
            adapter = addressAdapter
        }

        binding.tvWhere.text = "From:"


        addressAdapter.setOnClickListener = { address, pos ->
            Log.d("TTT", "bosilishni screen eshitdi")
            if (!::from.isInitialized) {
                binding.tvWhere.text = "To:"
                from = address
                addressList.remove(address)
                addressAdapter.notifyItemRemoved(pos)
            } else {
                to = address
                val mapIntent = Intent(this, MainActivity::class.java)
                MyAddress.from_address = from
                MyAddress.to_address = to
                startActivity(mapIntent)
            }

            Unit
        }
    }


    private val addressList = mutableListOf(
        AddressData(
            name = "Tashkent Television Tower",
            description = "Toshkent teleminora *",
            imageResId = R.drawable.tashkent_television,
            location = Point(41.345958215151114, 69.28561986053207)
        ),
        AddressData(
            name = "Tashkent Zoo",
            description = "Toshkent Hayvonot Bogʻi",
            imageResId = R.drawable.tashkent_zoo,
            location = Point(41.34758523003193, 69.32270384863473)
        ),
        AddressData(
            name = "Zomin",
            description = """
            Zomin
            Jizzakh Region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.zaamin,
            location = Point(39.94097223896012, 68.42961438968689)
        ),
        AddressData(
            name = "Batken",
            description = """
            Баткен
            Kyrgyzstan
        """.trimIndent(),
            imageResId = R.drawable.batken,
            location = Point(40.06136348787134, 70.82555219237899)
        ),
        AddressData(
            name = "Shakhrihan District",
            description = """
            Шаҳрихон тумани
            Andijan region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.shakhrihan,
            location = Point(40.7098709381041, 72.07619985993747)
        ),
        AddressData(
            name = "Gijduvon Bukhara",
            description = """
            G‘ijduvon
            Bukhara Region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.gijduvon,
            location = Point(40.081963782038756, 64.64423794241344)
        ),
        AddressData(
            name = "Khiva",
            description = """
            Xiva
            Xorazm Region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.khiva,
            location = Point(41.389525561119946, 60.33987434055896)
        ),
        AddressData(
            name = "Registan Square",
            description = "Registon Maydoni",
            imageResId = R.drawable.registan,
            location = Point(39.65498254038027, 66.97562690602447)
        ),
        AddressData(
            name = "Margilan",
            description = """
            Marg‘ilon
            Fergana Region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.margilan,
            location = Point(40.48131052983948, 71.72248019807523)
        ),
        AddressData(
            name = "Kamashi",
            description = """
            Qashqadaryo Region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.kamashi,
            location = Point(38.83419102723212, 65.37391657053458)
        ),
        AddressData(
            name = "Guliston",
            description = """
            Sirdaryo Region
            Uzbekistan
        """.trimIndent(),
            imageResId = R.drawable.gulistan,
            location = Point(38.80957766162307, 66.47904372145898)
        )
    )


    private val addressAdapter = AddressAdapter(addressList)
}