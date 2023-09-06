# 시작하기  

## gradle 파일 편집  
[Gradle Scripts] -> [build.gradle] 에 다래와 같은 코드를 추가  
```kotlin
android {

   // 생략

    buildFeatures {
        viewBinding true
    }
}
```

그 후 Sync Now 링크를 클릭하여 싱크  

## 공통된 엑티비티의 과정  
모든 엑티비티에 대하여 아래와 같은 기본 틀을 사용  

```kotlin
class [엑티비티이름] : AppCompatActivity() {

    private val binding by lazy {
        [엑티비티이름]Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.[뷰 이름].[동작]
    }
}
```

주의 할 점: [엑티비티이름]이 "MainActivity"라면..  
class MainActivity : AppCompatActivity() { 와 같이 쓰고,  
ActivityMainBinding.inflate(layoutInflater) 와 같이 쓴다.  

## 화면이동 및 데이터 전달  
보내는 쪽  (MainActivity)  
```kotlin
val intent = Intent(this, SubActivity::class.java)  // [이동할 엑티비티 이름]::class.java
intent.putExtra("key", "value") // intent.putExtra("[키]", "[값]")
startActivity(intent)
// finish() -> 엑티비티 파괴
```

받는 쪽 (SubActivity)  
```kotlin
if(intent.hasExtra("key")) {    // intent에 [키]의 값이 있다면 
    binding.[뷰 이름].text = intent.getStringExtra("key")
}
```

## 이미지 추가  
[무료 이미지](https://www.flaticon.com/)  
[app] -> [res] -> [drawable]에 이미지를 추가  
```kotlin
binding.[이미지 뷰 이름].setImageResource(R.drawable.[이미지 이름])
```

## 토스트 메시지
```kotlin
Toast.makeText(this, "[내용]", Toast.LENGTH_SHORT).show()
```

## 리스트 뷰 (쉬움 버전)  
```kotlin
val [array 이름] : Array<String> = arrayOf("[item1]", "[item2]", "[item3]")
binding.[리스트 뷰 이름].adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, [array 이름])
```

## 리스트 뷰 (커스텀 버전)  
[app] -> [java] -> [com.[페키지이름].[프로젝트 이름]] -> [DataModels.kt] 에 class를 추가한다.  
```kotlin
data class [클래스 이름] (
    val [변수 이름]: [변수 타입],
)
```
[app] -> [res] -> [layout] 에 레이아웃을 추가한다. (파일명은 snake_case)  
[app] -> [java] -> [com.[페키지이름].[프로젝트 이름]]에 Adapter를 추가한다. (파일명은 PascalCase)  
아래와 같은 기본 틀을 같는다.  
```kotlin
class [어뎁터 이름](val context: Context, val [리스트 이름]: ArrayList<[리스트 클래스]>) : BaseAdapter() {

    override fun getCount(): Int {
        return [리스트 이름].size
    }

    override fun getItem(position: Int): Any {
        return [리스트 이름][position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val binding = [레이아웃 이름 PascalCase]Binding.inflate(LayoutInflater.from(context))
        val [클래스 변수 이름] = [클래스 이름][position]

        binding.[뷰 이름].[타입] = [클래스 변수 이름].[변수 이름] // 값 이름

        return binding.root
    }
}
```
리스트 뷰를 띄울 레이아웃에서는 다음과 같이 넣을 수 있다.  
```kotlin
val Adapter = [어뎁터 이름](this, [화면에 표시할 데이터])
binding.[뷰 이름].adapter = Adapter
```
추가로 각 리스트 뷰에 이벤트를 추가하는 방법은 아래와 같다.  
```kotlin
binding.[레이아웃 이름].onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
    val selectItem = parent.getItemAtPosition(position) as [클래스 이름]

    // selectItem.[변수 이름] 으로 데이터 출력 가능
}
```
